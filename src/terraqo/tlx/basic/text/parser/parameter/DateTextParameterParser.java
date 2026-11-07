package pxf.toolkit.basic.text.parser.parameter;

import java.util.Date;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * {@code Date}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class DateTextParameterParser extends AbstractTimeTextParameterParser<Date> {

  public DateTextParameterParser(
      char arraySplit, Date defaultValue, String timePattern, Supplier<Date> defaultDateSupplier) {
    super(arraySplit, defaultValue, timePattern, defaultDateSupplier);
  }

  public DateTextParameterParser(
      char arraySplit,
      Supplier<Date> defaultValueSupplier,
      String timePattern,
      Supplier<Date> defaultDateSupplier) {
    super(arraySplit, defaultValueSupplier, timePattern, defaultDateSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return value instanceof Date;
  }

  @Override
  public Date doParseValue(@Nonnull String input) {
    return parse(input);
  }

  @Override
  protected Date[] createArray(int length) {
    return new Date[length];
  }
}
