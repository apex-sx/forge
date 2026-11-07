package pxf.toolkit.basic.text.parser.parameter;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.Is;

/**
 * {@code Double}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class DoubleTextParameterParser extends AbstractTextParameterParser<Double> {

  public DoubleTextParameterParser(char arraySplit, Double defaultValue) {
    super(arraySplit, defaultValue);
  }

  public DoubleTextParameterParser(char arraySplit, Supplier<Double> defaultValueSupplier) {
    super(arraySplit, defaultValueSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return Is.doubleObj(value);
  }

  @Override
  public Double doParseValue(@Nonnull String input) {
    return Cast.doubleValue(input, getDefaultValue());
  }

  @Override
  protected Double[] createArray(int length) {
    return new Double[length];
  }
}
