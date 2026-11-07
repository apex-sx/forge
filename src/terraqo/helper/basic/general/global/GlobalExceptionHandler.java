package potatoxf.helper.basic.general.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import potatoxf.helper.basic.general.ApiResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author potatoxf
 * @date 2022/7/13
 */
@ControllerAdvice
@Configuration
public class GlobalExceptionHandler {

  private final GlobalExceptionApiResultHandler globalExceptionApiResultHandler;

  public GlobalExceptionHandler(
      @Qualifier(GlobalConfiguration.GLOBAL_EXCEPTION_API_RESULT_HANDLER_BEAN_NAME)
          GlobalExceptionApiResultHandler globalExceptionApiResultHandler) {
    this.globalExceptionApiResultHandler = globalExceptionApiResultHandler;
  }

  /**
   * @param request
   * @param exception
   * @return
   * @throws Exception
   */
  @ExceptionHandler(value = Throwable.class)
  @ResponseBody
  public ApiResult<?> handleException(HttpServletRequest request, Throwable exception) {
    return globalExceptionApiResultHandler.handleException(request, exception);
  }
}
