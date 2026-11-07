package potatoxf.helper.basic.general.global;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import potatoxf.helper.api.Whether;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author potatoxf
 * @date 2021/8/16
 */
@Component
public final class ContextHolder
    implements BeanFactoryPostProcessor,
        EnvironmentAware,
        ApplicationContextAware,
        PriorityOrdered {
  private static final AtomicReference<Environment> ENVIRONMENT = new AtomicReference<>();
  private static final AtomicReference<ConfigurableListableBeanFactory> BEAN_FACTORY =
      new AtomicReference<>();
  private static final AtomicReference<ApplicationContext> APPLICATION_CONTEXT =
      new AtomicReference<>();

  @Nonnull
  public static Environment getEnvironment() {
    return checkAndGet(ENVIRONMENT);
  }

  @Nonnull
  public static ConfigurableListableBeanFactory getBeanFactory() {
    return checkAndGet(BEAN_FACTORY);
  }

  @Nonnull
  public static ApplicationContext getApplicationContext() {
    return checkAndGet(APPLICATION_CONTEXT);
  }

  public static boolean isExistBean(Class<?> type) {
    if (type != null) {
      return Whether.noEmpty(APPLICATION_CONTEXT.get().getBeanNamesForType(type));
    }
    return false;
  }

  /**
   * 通过name获取 Bean.
   *
   * @param name
   * @return
   */
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  /**
   * 通过class获取Bean
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   *
   * @param name
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }

  /**
   * @return
   */
  public static HttpSession getCurrentSession() {
    return getCurrentRequest().getSession();
  }

  /**
   * @return
   */
  public static ServletContext getServletContext() {
    return getCurrentSession().getServletContext();
  }

  /**
   * @return
   */
  public static HttpServletRequest getCurrentRequest() {
    return getServletRequestAttributes().getRequest();
  }

  /**
   * @return
   */
  public static HttpServletResponse getCurrentResponse() {
    return getServletRequestAttributes().getResponse();
  }

  /**
   * @return
   */
  public static ServletRequestAttributes getServletRequestAttributes() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      return (ServletRequestAttributes) requestAttributes;
    }
    throw new RuntimeException("The current environment is not a servlet environment");
  }

  private static <T> T checkAndGet(@Nonnull AtomicReference<T> atomicReference) {
    T t = atomicReference.get();
    if (t == null) {
      throw new RuntimeException("The BeanFactory not autowire into the Spring container");
    }
    return t;
  }

  @Override
  public void setEnvironment(@Nonnull Environment environment) {
    ENVIRONMENT.set(environment);
  }

  @Override
  public void setApplicationContext(@Nonnull ApplicationContext applicationContext)
      throws BeansException {
    APPLICATION_CONTEXT.set(applicationContext);
  }

  @Override
  public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    BEAN_FACTORY.set(beanFactory);
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
