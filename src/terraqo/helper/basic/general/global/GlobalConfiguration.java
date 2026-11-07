package potatoxf.helper.basic.general.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import potatoxf.helper.api.lang.MappingFolderCached;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author potatoxf
 * @date 2022/7/16
 */
@Order()
@Import({
  ContextHolder.class,
  GlobalExceptionHandler.class,
  ResponseResultHandler.class
})
@Configuration
public class GlobalConfiguration implements WebMvcConfigurer {

  public static final String GLOBAL_EXCEPTION_API_RESULT_HANDLER_BEAN_NAME =
      "globalExceptionApiResultHandler";

  private final GlobalProperties globalProperties;

  public GlobalConfiguration(GlobalProperties globalProperties) {
    this.globalProperties = globalProperties;
  }

  /**
   * @param cachedFolderMapping
   * @param objectMapper
   * @return
   */
  @ConditionalOnMissingBean(
      value = GlobalExceptionApiResultHandler.class,
      name = GLOBAL_EXCEPTION_API_RESULT_HANDLER_BEAN_NAME)
  @Bean
  public GlobalExceptionApiResultHandler globalExceptionApiResultHandler(
      MappingFolderCached cachedFolderMapping, ObjectMapper objectMapper) {
    return new GlobalExceptionApiResultHandlerOnJson(cachedFolderMapping, objectMapper);
  }

  /**
   * Configure the {@link HttpMessageConverter HttpMessageConverter}s for reading from the request
   * body and for writing to the response body.
   *
   * <p>By default, all built-in converters are configured as long as the corresponding 3rd party
   * libraries such Jackson JSON, JAXB2, and others are present on the classpath.
   *
   * <p><strong>Note</strong> use of this method turns off default converter registration.
   * Alternatively, use {@link #extendMessageConverters(List)} to modify that default list of
   * converters.
   *
   * @param converters initially an empty list of converters
   */
  @Override
  public void configureMessageConverters(@Nonnull List<HttpMessageConverter<?>> converters) {
    // 找出Bean容器中所有的 {@code HttpMessageConverter}
    List<?> list = getSpringContainerBeanSortedList(HttpMessageConverter.class, converters);
    converters.clear();
    list.forEach(o -> converters.add((HttpMessageConverter<?>) o));
  }

  /**
   * Add resolvers to support custom controller method argument types.
   *
   * <p>This does not override the built-in support for resolving handler method arguments. To
   * customize the built-in support for argument resolution, configure {@link
   * RequestMappingHandlerAdapter} directly.
   *
   * @param resolvers initially an empty list
   */
  @Override
  public void addArgumentResolvers(@Nonnull List<HandlerMethodArgumentResolver> resolvers) {
    // 找出Bean容器中所有的 {@code HandlerMethodArgumentResolver}
    List<HandlerMethodArgumentResolver> list =
        getSpringContainerBeanSortedList(HandlerMethodArgumentResolver.class, resolvers);
    resolvers.clear();
    resolvers.addAll(list);
  }

  @Nonnull
  private <T> List<T> getSpringContainerBeanSortedList(
      @Nonnull Class<T> clz, @Nonnull List<? extends T> builtinInstanceList) {
    Map<String, T> beansOfType = ContextHolder.getApplicationContext().getBeansOfType(clz);
    List<T> list =
        beansOfType.values().stream()
            .filter(Objects::nonNull)
            .distinct()
            .sorted(new AnnotationAwareOrderComparator())
            .collect(Collectors.toList());

    if (!builtinInstanceList.isEmpty()) {
      for (T t : builtinInstanceList) {
        if (list.contains(t)) {
          continue;
        }
        list.add(t);
      }
    }

    Set<T> result = new LinkedHashSet<>(list.size());

    Map<String, Integer> beanNameSorted = globalProperties.getBeanNameSorted();

    if (beanNameSorted != null) {

      List<String> beanNameList =
          beansOfType.keySet().stream()
              .filter(beanNameSorted::containsKey)
              .sorted(Comparator.comparingInt(beanNameSorted::get))
              .collect(Collectors.toList());

      for (String beanName : beanNameList) {
        result.add(beansOfType.get(beanName));
      }
    }

    list.removeAll(result);

    Map<Class<?>, Integer> beanTypeSorted = globalProperties.getBeanTypeSorted();

    if (beanTypeSorted != null) {

      List<Class<?>> beanTypeList =
          list.stream()
              .map(Object::getClass)
              .distinct()
              .filter(beanTypeSorted::containsKey)
              .sorted(Comparator.comparingInt(beanTypeSorted::get))
              .collect(Collectors.toList());

      for (Class<?> beanType : beanTypeList) {
        List<T> beanTypeFilter =
            list.stream().filter(t -> t.getClass() == beanType).collect(Collectors.toList());

        list.removeAll(beanTypeFilter);
        result.addAll(beanTypeFilter);
      }
    }

    if (!list.isEmpty()) {
      result.addAll(list);
    }
    return new ArrayList<>(result);
  }
}
