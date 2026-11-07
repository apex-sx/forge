package pxf.toolkit.basic.text.parser.parameter;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.Is;

/**
 * {@code Integer}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class IntegerTextParameterParser extends AbstractTextParameterParser<Integer> {

  public IntegerTextParameterParser(char arraySplit, Integer defaultValue) {
    super(arraySplit, defaultValue);
  }

  public IntegerTextParameterParser(char arraySplit, Supplier<Integer> defaultValueSupplier) {
    super(arraySplit, defaultValueSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return Is.intObj(value);
  }

  @Override
  public Integer doParseValue(@Nonnull String input) {
    return Cast.intValue(input, getDefaultValue());
  }

  @Override
  protected Integer[] createArray(int length) {
    return new Integer[length];
  }
}
