package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.convert.Convert;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * {@link AtomicIntegerArray}转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class AtomicIntegerArrayConverter extends AbstractConverter<AtomicIntegerArray> {
  private static final long serialVersionUID = 1L;

  @Override
  protected AtomicIntegerArray convertInternal(Object value) {
    return new AtomicIntegerArray(Convert.convert(int[].class, value));
  }
}
