package pxf.toolkit.basic.text.parser.parameter;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.Is;

/**
 * {@code Long}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class LongTextParameterParser extends AbstractTextParameterParser<Long> {

  public LongTextParameterParser(char arraySplit, Long defaultValue) {
    super(arraySplit, defaultValue);
  }

  public LongTextParameterParser(char arraySplit, Supplier<Long> defaultValueSupplier) {
    super(arraySplit, defaultValueSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return Is.longObj(value);
  }

  @Override
  public Long doParseValue(@Nonnull String input) {
    return Cast.longValue(input, getDefaultValue());
  }

  @Override
  protected Long[] createArray(int length) {
    return new Long[length];
  }
}
