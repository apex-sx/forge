package potatoxf.helper.basic.general.global;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author potatoxf
 * @date 2021/8/28
 */
@Component
public class RequestArgumentBeanPostProcessor
    implements BeanPostProcessor, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public Object postProcessAfterInitialization(@Nonnull Object bean,@Nonnull  String beanName) throws BeansException {
    RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;

    // 找出Bean容器中所有的 {@code HandlerMethodArgumentResolver}
    Map<String, HandlerMethodArgumentResolver> beansOfType =
        applicationContext.getBeansOfType(HandlerMethodArgumentResolver.class);
    List<HandlerMethodArgumentResolver> list =
        beansOfType.values().stream()
            .distinct()
            .sorted(new AnnotationAwareOrderComparator())
            .collect(Collectors.toList());

    // 正常的顺序
    List<HandlerMethodArgumentResolver> already = adapter.getArgumentResolvers();
    if (already != null) {
      for (HandlerMethodArgumentResolver handlerMethodArgumentResolver : already) {
        list.remove(handlerMethodArgumentResolver);
      }
      list.addAll(already);
    }
    adapter.setArgumentResolvers(list);
    return bean;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
