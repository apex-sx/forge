package pxf.toolkit.basic.text.parser.parameter;

import java.util.Date;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.TimeHelper;

/**
 * 时间类型文本参数解析器
 *
 * @see pxf.toolkit.basic.util.TimeHelper
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
abstract class AbstractTimeTextParameterParser<T> extends AbstractTextParameterParser<T> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractTimeTextParameterParser.class);
  /** 时间模式 */
  private final String timePattern;
  /** 默认时间提供器 */
  private final Supplier<Date> defaultDateSupplier;

  public AbstractTimeTextParameterParser(
      char arraySplit, T defaultValue, String timePattern, Supplier<Date> defaultDateSupplier) {
    super(arraySplit, defaultValue);
    this.timePattern = timePattern;
    this.defaultDateSupplier = defaultDateSupplier;
  }

  public AbstractTimeTextParameterParser(
      char arraySplit,
      Supplier<T> defaultValueSupplier,
      String timePattern,
      Supplier<Date> defaultDateSupplier) {
    super(arraySplit, defaultValueSupplier);
    this.timePattern = timePattern;
    this.defaultDateSupplier = defaultDateSupplier;
  }

  /**
   * 解析日期时间
   *
   * <p>该方法线程安全
   *
   * @param input 输入字符串
   * @return {@code Date}
   */
  @Nonnull
  protected Date parse(String input) {
    try {
      return TimeHelper.parse(input, timePattern);
    } catch (Throwable e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(
            String.format(
                "An exception occurred when parsing time [%s] in the format [%s]",
                input, timePattern),
            e);
      }
      Date defaultDate = defaultDateSupplier.get();
      if (LOG.isWarnEnabled()) {
        LOG.warn(
            String.format(
                "Default value [%s] will be used", TimeHelper.formatDefaultDatetime(defaultDate)));
      }
      return defaultDate;
    }
  }

  /**
   * 格式化日期时间
   *
   * <p>该方法线程安全
   *
   * @param input 输入字符串
   * @return {@code String}
   */
  @Nonnull
  protected String format(Date input) {
    try {
      return TimeHelper.format(input, timePattern);
    } catch (Throwable e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(
            String.format(
                "An exception occurred when format time [%s] in the format [%s]",
                TimeHelper.formatDefaultDatetime(input), timePattern),
            e);
      }
      return Empty.STRING;
    }
  }
}
