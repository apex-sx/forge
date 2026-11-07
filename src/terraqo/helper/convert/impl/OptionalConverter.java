package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;

import java.util.Optional;

/**
 * {@link Optional}对象转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class OptionalConverter extends AbstractConverter<Optional<?>> {
  private static final long serialVersionUID = 1L;

  @Override
  protected Optional<?> convertInternal(Object value) {
    return Optional.ofNullable(value);
  }
}
