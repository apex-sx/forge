package potatoxf.helper.basic.general.global;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author potatoxf
 * @date 2022/7/13
 */
public abstract class AbstractHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
  /**
   * Whether the given {@linkplain MethodParameter method parameter} is supported by this resolver.
   *
   * @param parameter the method parameter to check
   * @return {@code true} if this resolver supports the supplied parameter; {@code false} otherwise
   */
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return getClass() == parameter.getParameterType();
  }

  /**
   * Resolves a method parameter into an argument value from a given request. A {@link
   * ModelAndViewContainer} provides access to the model for the request. A {@link
   * WebDataBinderFactory} provides a way to create a {@link WebDataBinder} instance when needed for
   * data binding and type conversion purposes.
   *
   * @param parameter the method parameter to resolve. This parameter must have previously been
   *     passed to {@link #supportsParameter} which must have returned {@code true}.
   * @param mavContainer the ModelAndViewContainer for the current request
   * @param webRequest the current request
   * @param binderFactory a factory for creating {@link WebDataBinder} instances
   * @return the resolved argument value, or {@code null} if not resolvable
   * @throws Exception in case of errors with the preparation of argument values
   */
  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    String parameterName = parameter.getParameterName();

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    String[] parameterValues = request.getParameterValues(parameterName);

    String[] inputs;
    if (isArray()) {
      if (parameterValues == null || parameterValues.length == 0) {
        inputs = new String[0];
      } else {
        inputs = parameterValues;
      }
    }else {



    }

    return null;
  }

  protected abstract boolean isArray();
}
