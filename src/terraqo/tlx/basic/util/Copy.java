package pxf.toolkit.basic.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;
import pxf.toolkit.basic.lang.JavaBasicConverter;

/**
 * 复制操作
 *
 * @author potatoxf
 * @date 2021/4/17
 */
public final class Copy {

  private Copy() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 复制Bean到一个新的对象
   *
   * @param src 要复制的Bean
   * @param <T> bean类型
   * @return 返回新的一个对象
   */
  public static <T> T bean(@Nonnull T src) {
    Class<?> clz = src.getClass();
    BeanCopier copier = BeanCopier.create(clz, clz, false);
    T dist = NewSilently.instance(clz);
    copier.copy(src, dist, null);
    return dist;
  }

  /**
   * 复制Bean到一个新的对象
   *
   * @param src 要复制的Bean
   * @param <T> bean类型
   * @param <R> 目标bean类型
   * @return 返回新的一个对象
   */
  public static <T, R> R bean(@Nonnull T src, @Nonnull Class<R> targetClass) {
    BeanCopier copier = BeanCopier.create(src.getClass(), targetClass, false);
    R dist = NewSilently.instance(targetClass);
    copier.copy(src, dist, null);
    return dist;
  }

  /**
   * 复制Bean到一个对象
   *
   * @param src 要复制的Bean
   * @param dist 目标的bean
   * @param <T> bean类型
   * @param <R> 目标bean类型
   */
  public static <T, R> void bean(@Nonnull T src, @Nonnull R dist) {
    BeanCopier copier = BeanCopier.create(src.getClass(), dist.getClass(), false);
    copier.copy(src, dist, null);
  }

  /**
   * 复制Bean到一个新的对象，并且如果是基本类型会自动转换到对应类型
   *
   * @param src 要复制的Bean
   * @param <T> bean类型
   * @return 返回新的一个对象
   */
  public static <T> T beanWithBasicConverter(@Nonnull T src) {
    return Copy.beanWithConverter(
        src, (value, target, context) -> JavaBasicConverter.cast(value, (Class<?>) target));
  }

  /**
   * 复制Bean到一个新的对象，并且如果是基本类型会自动转换到对应类型
   *
   * @param src 要复制的Bean
   * @param <T> bean类型
   * @param <R> 目标bean类型
   * @return 返回新的一个对象
   */
  public static <T, R> R beanWithBasicConverter(@Nonnull T src, @Nonnull Class<R> targetClass) {
    return Copy.beanWithConverter(
        src,
        targetClass,
        (value, target, context) -> JavaBasicConverter.cast(value, (Class<?>) target));
  }

  /**
   * 复制Bean到一个对象，并且如果是基本类型会自动转换到对应类型
   *
   * @param src 要复制的Bean
   * @param dist 目标的bean
   * @param <T> bean类型
   * @param <R> 目标bean类型
   */
  public static <T, R> void beanWithBasicConverter(@Nonnull T src, @Nonnull R dist) {
    Copy.beanWithConverter(
        src, dist, (value, target, context) -> JavaBasicConverter.cast(value, (Class<?>) target));
  }

  /**
   * 复制Bean到一个新的对象，提供一个自定义类型转换器
   *
   * @param src 要复制的Bean
   * @param converter 转换器
   * @param <T> Bean类型
   * @return 返回新的一个对象
   */
  public static <T> T beanWithConverter(@Nonnull T src, @Nonnull Converter converter) {
    Class<?> clz = src.getClass();
    BeanCopier copier = BeanCopier.create(clz, clz, true);
    T dist = NewSilently.instance(clz);
    copier.copy(src, dist, converter);
    return dist;
  }

  /**
   * 复制Bean到一个新的对象，提供一个自定义类型转换器
   *
   * @param src 要复制的Bean
   * @param converter 转换器
   * @param <T> Bean类型
   * @param <R> 目标bean类型
   * @return 返回新的一个对象
   */
  public static <T, R> R beanWithConverter(
      @Nonnull T src, @Nonnull Class<R> targetClass, @Nonnull Converter converter) {
    Class<?> clz = src.getClass();
    BeanCopier copier = BeanCopier.create(clz, targetClass, true);
    R dist = NewSilently.instance(clz);
    copier.copy(src, dist, converter);
    return dist;
  }

  /**
   * 复制Bean到一个对象，提供一个自定义类型转换器
   *
   * @param src 要复制的Bean
   * @param dist 目标的bean
   * @param converter 转换器
   * @param <T> Bean类型
   */
  public static <T, R> void beanWithConverter(
      @Nonnull T src, @Nonnull R dist, @Nonnull Converter converter) {
    BeanCopier copier = BeanCopier.create(src.getClass(), dist.getClass(), true);
    copier.copy(src, dist, converter);
  }

  /**
   * 将Bean复制到Map
   *
   * @param bean 要复制的Bean
   * @return {@code Map}
   */
  public static Map beanForMap(@Nonnull Object bean) {
    return BeanMap.create(bean);
  }

  /**
   * 将Map复制到Bean
   *
   * @param bean 要复制的Bean
   * @param container 要复制的Bean
   */
  public static void mapForBean(@Nonnull Object bean, @Nonnull Map<String, Object> container) {
    BeanMap.create(bean).putAll(container);
  }

  /**
   * 复制Bean列表到一个新的列表
   *
   * @param src 要复制的Bean列表
   * @param <T> Bean类型
   * @return {@code List<T>}
   */
  @Nonnull
  public static <T> List<T> beanList(@Nonnull List<T> src) {
    return src.stream().map(Copy::bean).collect(Collectors.toList());
  }

  /**
   * 复制Bean列表到一个新的列表
   *
   * @param src 要复制的Bean列表
   * @param <T> Bean类型
   * @return {@code List<T>}
   */
  @Nonnull
  public static <T, R> List<R> beanList(@Nonnull List<T> src, @Nonnull Class<R> targetClass) {
    return src.stream().map(e -> Copy.bean(e, targetClass)).collect(Collectors.toList());
  }
}
