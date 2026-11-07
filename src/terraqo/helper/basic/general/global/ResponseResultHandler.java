package potatoxf.helper.basic.general.global;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import potatoxf.helper.basic.general.ApiResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author potatoxf
 * @date 2021/9/19
 */
@ControllerAdvice
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
      @Nonnull MethodParameter methodParameter,
      @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
    return methodParameter.hasMethodAnnotation(ResponseBody.class)
        || methodParameter.getDeclaringClass().getAnnotation(RestController.class) != null;
  }

  @Override
  public Object beforeBodyWrite(
      @Nullable Object body,
      @Nonnull MethodParameter returnType,
      @Nonnull MediaType selectedContentType,
      @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
      @Nonnull ServerHttpRequest request,
      @Nonnull ServerHttpResponse response) {
    if (body == null) {
      return ApiResult.ofSuccess();
    } else {
      if (body instanceof Boolean) {
        return ApiResult.of((Boolean) body);
      }
      if (body instanceof HttpStatus) {
        return ApiResult.of((HttpStatus) body);
      }
      if (body instanceof ApiResult<?>) {
        return body;
      }
    }
    return ApiResult.ofSuccess(body);
  }
}
