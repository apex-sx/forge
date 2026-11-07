package potatoxf.helper.basic.general.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import potatoxf.helper.basic.general.ApiEncryption;
import potatoxf.helper.basic.general.ApiResult;
import potatoxf.helper.api.lang.SecretException;
import potatoxf.helper.api.lang.SecretService;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author potatoxf
 * @date 2022/7/19
 */
public class SecretSessionProcessHandler extends RequestBodyAdviceAdapter
    implements RequestBodyAdvice, ResponseBodyAdvice<Object> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SecretSessionProcessHandler.class);
  private final SecretKey secretKey;
  private final SecretService secretService;
  private final ObjectMapper objectMapper;

  public SecretSessionProcessHandler(
      ObjectMapper objectMapper, SecretService secretService, SecretKey secretKey) {
    this.objectMapper = objectMapper;
    this.secretService = secretService;
    this.secretKey = secretKey;
  }

  /**
   * Invoked first to determine if this interceptor applies.
   *
   * @param methodParameter the method parameter
   * @param targetType the target type, not necessarily the same as the method parameter type, e.g.
   *     for {@code HttpEntity<String>}.
   * @param converterType the selected converter type
   * @return whether this interceptor should be invoked or not
   */
  @Override
  public boolean supports(
      @Nonnull MethodParameter methodParameter,
      @Nonnull Type targetType,
      @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
    return methodParameter.hasMethodAnnotation(ApiEncryption.class)
        || methodParameter.getDeclaringClass().getAnnotation(ApiEncryption.class) != null;
  }

  /**
   * The default implementation returns the InputMessage that was passed in.
   *
   * @param inputMessage
   * @param parameter
   * @param targetType
   * @param converterType
   */
  @Nonnull
  @Override
  public HttpInputMessage beforeBodyRead(
      HttpInputMessage inputMessage,
      @Nonnull MethodParameter parameter,
      @Nonnull Type targetType,
      @Nonnull Class<? extends HttpMessageConverter<?>> converterType)
      throws IOException {
    try {
      InputStream inputStream =
          secretService.decrypt(inputMessage.getBody(), secretKey.getEncoded());
      return new HttpInputMessage() {
        @Nonnull
        @Override
        public InputStream getBody() {
          return inputStream;
        }

        @Nonnull
        @Override
        public HttpHeaders getHeaders() {
          return inputMessage.getHeaders();
        }
      };
    } catch (SecretException e) {
      throw new IOException("An exception occurred when decrypting the input stream", e);
    }
  }

  /**
   * Whether this component supports the given controller method return type and the selected {@code
   * HttpMessageConverter} type.
   *
   * @param returnType the return type
   * @param converterType the selected converter type
   * @return {@code true} if {@link #beforeBodyWrite} should be invoked; {@code false} otherwise
   */
  @Override
  public boolean supports(@Nonnull MethodParameter returnType, @Nonnull Class converterType) {
    return returnType.hasMethodAnnotation(ApiEncryption.class)
        || returnType.getDeclaringClass().getAnnotation(ApiEncryption.class) != null;
  }

  /**
   * Invoked after an {@code HttpMessageConverter} is selected and just before its write method is
   * invoked.
   *
   * @param body the body to be written
   * @param returnType the return type of the controller method
   * @param selectedContentType the content type selected through content negotiation
   * @param selectedConverterType the converter type selected to write to the response
   * @param request the current request
   * @param response the current response
   * @return the body that was passed in or a modified (possibly new) instance
   */
  @Override
  public Object beforeBodyWrite(
      Object body,
      @Nonnull MethodParameter returnType,
      @Nonnull MediaType selectedContentType,
      @Nonnull Class selectedConverterType,
      @Nonnull ServerHttpRequest request,
      @Nonnull ServerHttpResponse response) {
    if (body instanceof ApiResult) {
      String json;
      Object data = ((ApiResult<?>) body).getData();
      if (data != null) {
        try {
          json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
          if (LOGGER.isErrorEnabled()) {
            LOGGER.error("", e);
          }
          json = null;
        }
        if (json != null) {
          return ApiResult.copyExceptData((ApiResult<?>) body, json);
        } else {
          if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("");
          }
        }
      } else {
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn("");
        }
      }
    }
    return body;
  }
}
