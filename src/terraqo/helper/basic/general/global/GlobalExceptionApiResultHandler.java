package potatoxf.helper.basic.general.global;

import potatoxf.helper.basic.general.ApiResult;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author potatoxf
 * @date 2022/7/16
 */
public interface GlobalExceptionApiResultHandler {

  /**
   * 处理请求发生的异常并返回相应的结果
   *
   * @param request 请求
   * @param exception 异常
   * @return {@code ApiResult<?>}
   */
  @Nonnull
  ApiResult<?> handleException(@Nonnull HttpServletRequest request, @Nonnull Throwable exception);
}
