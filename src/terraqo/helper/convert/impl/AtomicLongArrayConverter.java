package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.convert.Convert;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * {@link AtomicLongArray}转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class AtomicLongArrayConverter extends AbstractConverter<AtomicLongArray> {
  private static final long serialVersionUID = 1L;

  @Override
  protected AtomicLongArray convertInternal(Object value) {
    return new AtomicLongArray(Convert.convert(long[].class, value));
  }
}
