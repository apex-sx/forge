package potatoxf.helper.basic.general.global;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.basic.general.ApiResult;
import potatoxf.helper.basic.general.ApiResultException;
import potatoxf.helper.api.lang.MappingFileCached;
import potatoxf.helper.api.lang.MappingFolderCached;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * @author potatoxf
 * @date 2022/7/16
 */
public class GlobalExceptionApiResultHandlerOnJson implements GlobalExceptionApiResultHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  private static final String EXCEPTION_MESSAGE_CONFIG_JSON_RESOURCE_NAME =
      "exception-message.json";
  private final MappingFileCached<JsonNode> mappingFileCached;

  public GlobalExceptionApiResultHandlerOnJson(
      MappingFolderCached cachedFolderMapping, ObjectMapper objectMapper) {
    this.mappingFileCached =
        new MappingFileCached<JsonNode>(
            cachedFolderMapping, EXCEPTION_MESSAGE_CONFIG_JSON_RESOURCE_NAME) {
          @Override
          protected JsonNode resolveFileData(InputStream inputStream) throws IOException {
            return objectMapper.readValue(inputStream, JsonNode.class);
          }
        };
  }

  /**
   * 处理请求发生的异常并返回相应的结果
   *
   * @param request 请求
   * @param exception 异常
   * @return {@code ApiResult<?>}
   */
  @Nonnull
  @Override
  public ApiResult<?> handleException(
      @Nonnull HttpServletRequest request, @Nonnull Throwable exception) {
    String message = null;
    String exceptionName = exception.getClass().getName();
    JsonNode jsonNode = mappingFileCached.getData();
    if (jsonNode != null) {
      if (jsonNode.isObject()) {
        jsonNode = jsonNode.get(exceptionName);
      } else {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error(
              "The configuration file ["
                  + mappingFileCached.getOuterFilePath()
                  + "] is not a JSON object type");
        }
        jsonNode = null;
      }
    } else {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn(
            "Unable to get exception information file ["
                + mappingFileCached.getOuterFilePath()
                + "]");
      }
    }

    if (jsonNode != null) {
      if (jsonNode.isTextual()) {
        message = jsonNode.asText();
      } else if (jsonNode.isObject()) {
        Locale locale = request.getLocale();
        String node = locale.toString();
        JsonNode messageNode = jsonNode.get(node);
        if (messageNode == null) {
          if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(
                "Unable to get ["
                    + node
                    + "] exception information in the configuration file ["
                    + mappingFileCached.getOuterFilePath()
                    + "] at the exception node["
                    + exceptionName
                    + "]");
          }
          node = "default";
          messageNode = jsonNode.get(node);
          if (messageNode == null) {
            if (LOGGER.isWarnEnabled()) {
              LOGGER.warn(
                  "Unable to get ["
                      + node
                      + "] exception information in the configuration file ["
                      + mappingFileCached.getOuterFilePath()
                      + "] at the exception node["
                      + exceptionName
                      + "]");
            }
          } else {
            message = getExceptionMessage(messageNode, node, exceptionName);
          }
        } else {
          message = getExceptionMessage(messageNode, node, exceptionName);
        }
      } else {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error(
              "The exception node ["
                  + exceptionName
                  + "] information is not a string type or an object type in the configuration file ["
                  + mappingFileCached.getOuterFilePath()
                  + "]");
        }
      }
    }

    ApiResult<?> apiResult;
    if (exception instanceof ApiResultException) {
      apiResult = ((ApiResultException<?>) exception).getApiResult();
    } else {
      apiResult = ApiResult.ofFail(exception.getMessage());
    }

    if (message != null) {
      apiResult = ApiResult.copyExceptMessage(apiResult, message);
    }
    return apiResult;
  }

  private String getExceptionMessage(JsonNode messageNode, String node, String exceptionName) {
    if (messageNode.isTextual()) {
      return messageNode.asText();
    } else {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(
            "Error type to get ["
                + node
                + "] exception information in the configuration file ["
                + mappingFileCached.getOuterFilePath()
                + "] at the exception node["
                + exceptionName
                + "]");
      }
      return null;
    }
  }
}
