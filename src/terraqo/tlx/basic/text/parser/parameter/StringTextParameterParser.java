package pxf.toolkit.basic.text.parser.parameter;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * {@code String}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class StringTextParameterParser extends AbstractTextParameterParser<String> {

  public StringTextParameterParser(char arraySplit, String defaultValue) {
    super(arraySplit, defaultValue);
  }

  public StringTextParameterParser(char arraySplit, Supplier<String> defaultValueSupplier) {
    super(arraySplit, defaultValueSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return value instanceof String;
  }

  @Override
  public String doParseValue(@Nonnull String input) {
    return input;
  }

  @Override
  protected String[] createArray(int length) {
    return new String[length];
  }
}
