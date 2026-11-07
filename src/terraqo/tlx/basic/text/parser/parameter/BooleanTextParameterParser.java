package pxf.toolkit.basic.text.parser.parameter;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.util.Is;

/**
 * {@code Boolean}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class BooleanTextParameterParser extends AbstractTextParameterParser<Boolean> {

  public BooleanTextParameterParser(char arraySplit, Boolean defaultValue) {
    super(arraySplit, defaultValue);
  }

  public BooleanTextParameterParser(char arraySplit, Supplier<Boolean> defaultValueSupplier) {
    super(arraySplit, defaultValueSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return Is.boolObj(value);
  }

  @Override
  public Boolean doParseValue(@Nonnull String input) {
    int length = input.length();
    if (length == 1) {
      return "T".equalsIgnoreCase(input);
    } else if (length == 2) {
      return !"NO".equalsIgnoreCase(input);
    } else if (length == 3) {
      return "YES".equalsIgnoreCase(input);
    }
    return Boolean.parseBoolean(input);
  }

  @Override
  protected Boolean[] createArray(int length) {
    return new Boolean[length];
  }
}
