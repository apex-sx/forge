package pxf.toolkit.basic.text.parser.parameter;

import java.util.Date;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.lang.TimeRange;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Split;

/**
 * {@code TimeRange}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class TimeRangeTextParameterParser extends AbstractTimeTextParameterParser<TimeRange> {

  /** 时间分隔符 */
  private final char timeSplit;

  public TimeRangeTextParameterParser(
      char arraySplit,
      TimeRange defaultValue,
      String timePattern,
      Supplier<Date> defaultDateSupplier,
      char timeSplit) {
    super(arraySplit, defaultValue, timePattern, defaultDateSupplier);
    this.timeSplit = timeSplit;
  }

  public TimeRangeTextParameterParser(
      char arraySplit,
      Supplier<TimeRange> defaultValueSupplier,
      String timePattern,
      Supplier<Date> defaultDateSupplier,
      char timeSplit) {
    super(arraySplit, defaultValueSupplier, timePattern, defaultDateSupplier);
    this.timeSplit = timeSplit;
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return value instanceof TimeRange;
  }

  @Override
  public TimeRange doParseValue(@Nonnull String input) {
    String[] inputs = Split.simpleString(input, String.valueOf(timeSplit));
    if (Is.empty(inputs)) {
      return getDefaultValue();
    }
    TimeRange timeRange = new TimeRange();
    Date start = parse(inputs[0]);
    timeRange.setStart(start);
    if (inputs.length > 1) {
      Date end = parse(inputs[1]);
      timeRange.setEnd(end);
    }
    return timeRange;
  }

  @Override
  protected TimeRange[] createArray(int length) {
    return new TimeRange[length];
  }

  public char getTimeSplit() {
    return timeSplit;
  }
}
