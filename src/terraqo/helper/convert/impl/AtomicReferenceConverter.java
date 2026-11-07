package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.convert.ConverterRegistry;
import potatoxf.helper.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link AtomicReference}转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
@SuppressWarnings("rawtypes")
public class AtomicReferenceConverter extends AbstractConverter<AtomicReference> {
  private static final long serialVersionUID = 1L;

  @Override
  protected AtomicReference<?> convertInternal(Object value) {

    // 尝试将值转换为Reference泛型的类型
    Object targetValue = null;
    final Type paramType = TypeUtil.getTypeArgument(AtomicReference.class);
    if (false == TypeUtil.isUnknown(paramType)) {
      targetValue = ConverterRegistry.getInstance().convert(paramType, value);
    }
    if (null == targetValue) {
      targetValue = value;
    }

    return new AtomicReference<>(targetValue);
  }
}
