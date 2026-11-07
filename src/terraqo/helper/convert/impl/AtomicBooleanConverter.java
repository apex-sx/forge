package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.util.BooleanUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link AtomicBoolean}转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class AtomicBooleanConverter extends AbstractConverter<AtomicBoolean> {
  private static final long serialVersionUID = 1L;

  @Override
  protected AtomicBoolean convertInternal(Object value) {
    if (value instanceof Boolean) {
      return new AtomicBoolean((Boolean) value);
    }
    final String valueStr = convertToStr(value);
    return new AtomicBoolean(BooleanUtil.toBoolean(valueStr));
  }
}
