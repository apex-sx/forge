package potatoxf.helper.convert.impl;

import potatoxf.helper.api.map.MapProxy;
import potatoxf.helper.bean.BeanUtil;
import potatoxf.helper.bean.copier.BeanCopier;
import potatoxf.helper.bean.copier.CopyOptions;
import potatoxf.helper.bean.copier.ValueProvider;
import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.convert.ConvertException;
import potatoxf.helper.util.ObjectUtil;
import potatoxf.helper.util.ReflectUtil;
import potatoxf.helper.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean转换器，支持：
 *
 * <pre>
 * Map =》 Bean
 * Bean =》 Bean
 * ValueProvider =》 Bean
 * </pre>
 *
 * @param <T> Bean类型
 * @author potatoxf
 * @date 2021/9/26
 */
public class BeanConverter<T> extends AbstractConverter<T> {
  private static final long serialVersionUID = 1L;

  private final Type beanType;
  private final Class<T> beanClass;
  private final CopyOptions copyOptions;

  /**
   * 构造，默认转换选项，注入失败的字段忽略
   *
   * @param beanType 转换成的目标Bean类型
   */
  public BeanConverter(Type beanType) {
    this(beanType, CopyOptions.create().setIgnoreError(true));
  }

  /**
   * 构造，默认转换选项，注入失败的字段忽略
   *
   * @param beanClass 转换成的目标Bean类
   */
  public BeanConverter(Class<T> beanClass) {
    this(beanClass, CopyOptions.create().setIgnoreError(true));
  }

  /**
   * 构造
   *
   * @param beanType 转换成的目标Bean类
   * @param copyOptions Bean转换选项参数
   */
  @SuppressWarnings("unchecked")
  public BeanConverter(Type beanType, CopyOptions copyOptions) {
    this.beanType = beanType;
    this.beanClass = (Class<T>) TypeUtil.getClass(beanType);
    this.copyOptions = copyOptions;
  }

  @Override
  protected T convertInternal(Object value) {
    if (value instanceof Map
        || value instanceof ValueProvider
        || BeanUtil.isBean(value.getClass())) {
      if (value instanceof Map && this.beanClass.isInterface()) {
        // 将Map动态代理为Bean
        return MapProxy.create((Map<?, ?>) value).toProxyBean(this.beanClass);
      }

      // 限定被转换对象类型
      return BeanCopier.create(
              value,
              ReflectUtil.newInstanceIfPossible(this.beanClass),
              this.beanType,
              this.copyOptions)
          .copy();
    } else if (value instanceof byte[]) {
      // 尝试反序列化
      return ObjectUtil.deserialize((byte[]) value);
    }

    throw new ConvertException("Unsupported source type: {}", value.getClass());
  }

  @Override
  public Class<T> getTargetType() {
    return this.beanClass;
  }
}
