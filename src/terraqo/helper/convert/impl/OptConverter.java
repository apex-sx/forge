package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.lang.Opt;

/**
 * {@link Opt}对象转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class OptConverter extends AbstractConverter<Opt<?>> {
  private static final long serialVersionUID = 1L;

  @Override
  protected Opt<?> convertInternal(Object value) {
    return Opt.ofNullable(value);
  }
}
