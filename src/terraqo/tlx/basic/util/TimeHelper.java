package pxf.toolkit.basic.util;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.concurrent.pool.ItemPool;
import pxf.toolkit.basic.concurrent.pool.ItemPool.PoolItem;

/**
 * 时间助手类
 *
 * @author potatoxf
 * @date 2021/5/15
 */
public final class TimeHelper {
  private static final Logger LOG = LoggerFactory.getLogger(TimeHelper.class);
  /** SimpleDateFormat缓存池大小键 */
  public static final String SDF_POOL_SIZE_KEY = TimeHelper.class + ".SDF_POOL_SIZE_KEY";
  /** 中文日期数字 */
  private static final String[] CHINESE_TIME_NUMBERS =
      new String[] {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
  /** 中文日期单位 */
  private static final String[] CHINESE_DATE_UNIT = {"年", "月", "日"};
  /** 日期格式 */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  /** 日期时间格式 */
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  /** 日期时间格式 */
  public static final String DATE_TIME_SAFE_FORMAT = "yyyy-MM-ddTHH:mm:ss";
  /** 数字日期格式 */
  public static final String DATE_NUMERICAL_FORMAT = "yyyyMMdd";
  /** {@link SimpleDateFormat}的池 */
  private static final ItemPool<SimpleDateFormatter> SDF_POOL = createSimpleDateFormatterItemPool();
  /** 1s有多少ms */
  private static final long ONE_SECOND_MILLIS = 1000;
  /** 1min有多少ms */
  private static final long ONE_MINUTE_MILLIS = 60000;
  /** 1h有多少ms */
  private static final long ONE_HOUR_MILLIS = 3600000;
  /** 1d有多少ms */
  private static final long ONE_DAY_MILLIS = 86400000;
  /** 时间起始偏移ms */
  private static final long OFFSET_MILLIS = 28800000;

  private TimeHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 创建 {@code SimpleDateFormatter}池
   *
   * @return {@code ItemPool<SimpleDateFormatter>}
   */
  @Nonnull
  private static ItemPool<SimpleDateFormatter> createSimpleDateFormatterItemPool() {
    String property = System.getProperty(SDF_POOL_SIZE_KEY);
    if (property == null) {
      return new ItemPool<>(SimpleDateFormatter.class, DefaultSimpleDateFormatter::new);
    }
    int maSize = Cast.intValue(property, 20);
    return new ItemPool<>(SimpleDateFormatter.class, DefaultSimpleDateFormatter::new, maSize);
  }

  @Nonnull
  public static String formatChineseDate(Date date) {
    String dateString = formatDefaultDate(date);
    return formatChineseDate(dateString);
  }

  @Nonnull
  private static String formatChineseDate(String dateString) {
    StringBuilder sb = new StringBuilder();
    int sign = 0;
    boolean isChanged = false;
    for (int i = 0; i < dateString.length(); i++) {
      char c = dateString.charAt(i);
      if (Character.isDigit(c)) {
        int n = c - 48;
        if (sign == 1 && isChanged) {
          isChanged = false;
          if (n == 0) {
            continue;
          }
        }
        sb.append(CHINESE_TIME_NUMBERS[n]);
      } else if (c == '-') {
        if (sign < 3) {
          sb.append(CHINESE_DATE_UNIT[sign++]);
          isChanged = true;
        } else {
          throw new IllegalArgumentException(
              "The time parameter must be in format for" + DATE_FORMAT);
        }
      } else {
        throw new IllegalArgumentException(
            "The time parameter must be in format for" + DATE_FORMAT);
      }
    }
    return sb.toString();
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(long input) {
    return format(input, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(Instant input) {
    return format(input, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(Date input) {
    return format(input, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(LocalDateTime input) {
    return format(input, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param zoneId 时区
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(LocalDateTime input, ZoneId zoneId) {
    return format(input, zoneId, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(LocalDate input) {
    return format(input, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param zoneId 时区
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDate(LocalDate input, ZoneId zoneId) {
    return format(input, zoneId, DATE_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(long input) {
    return format(input, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(Instant input) {
    return format(input, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(Date input) {
    return format(input, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(LocalDateTime input) {
    return format(input, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param zoneId 时区
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(LocalDateTime input, ZoneId zoneId) {
    return format(input, zoneId, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(LocalDate input) {
    return format(input, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param zoneId 时区
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String formatDefaultDatetime(LocalDate input, ZoneId zoneId) {
    return format(input, zoneId, DATE_TIME_FORMAT);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(long input, String format) {
    return format(new Date(input), format);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(Instant input, String format) {
    return format(Date.from(input), format);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(LocalDateTime input, String format) {
    return format(input, ZoneId.systemDefault(), format);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param zoneId 时区
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(LocalDateTime input, ZoneId zoneId, String format) {
    return format(Cast.date(input, zoneId), format);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(LocalDate input, String format) {
    return format(input, ZoneId.systemDefault(), format);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param zoneId 时区
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(LocalDate input, ZoneId zoneId, String format) {
    return format(Cast.date(input, zoneId), format);
  }

  /**
   * 格式化日期时间
   *
   * @param input 输入的日期时间
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static String format(Date input, String format) {
    try (SimpleDateFormatter simpleDateFormatter = SDF_POOL.takeItem()) {
      return simpleDateFormatter.format(input, format);
    }
  }

  /**
   * 解析日期
   *
   * @param date 日期
   * @return 返回日期 {@code int}值
   */
  public static int parseDateToInt(Date date) {
    return Integer.parseInt(format(date, DATE_NUMERICAL_FORMAT));
  }

  /**
   * 解析日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nullable
  public static Date parseDefaultDate(String input) {
    return parse(input, DATE_FORMAT);
  }

  /**
   * 解析日期时间
   *
   * @param input 输入的日期时间
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static Date parseDefaultDatetime(String input) {
    return parse(input, DATE_TIME_FORMAT);
  }

  /**
   * 解析日期时间
   *
   * @param input 输入的日期时间
   * @param format 格式
   * @return 返回格式后的日期时间
   */
  @Nonnull
  public static Date parse(String input, String format) {
    try (SimpleDateFormatter simpleDateFormatter = SDF_POOL.takeItem()) {
      return simpleDateFormatter.parse(input, format);
    }
  }

  /**
   * 获取当前季度
   *
   * @return {@code 1-4}
   */
  public static int nowQuarter() {
    return nowQuarter(null);
  }

  /**
   * 获取当前季度
   *
   * @param locale 区域
   * @return {@code 1-4}
   */
  public static int nowQuarter(Locale locale) {
    Calendar calendar = locale == null ? Calendar.getInstance() : Calendar.getInstance(locale);
    return calendar.get(Calendar.MONTH) % 3 + 1;
  }

  /**
   * 获取季度
   *
   * @param date 日期
   * @return {@code 1-4}
   */
  public static int getQuarter(Date date) {
    int month = Cast.localDate(date).getMonthValue();
    return (month - 1) % 3 + 1;
  }

  /**
   * 获取季度
   *
   * @param date 日期
   * @param zoneId 时区
   * @return {@code 1-4}
   */
  public static int getQuarter(Date date, ZoneId zoneId) {
    int month = Cast.localDate(date, zoneId).getMonthValue();
    return (month - 1) % 3 + 1;
  }

  /**
   * 获取季度
   *
   * @param localDate 日期
   * @return {@code 1-4}
   */
  public static int getQuarter(LocalDate localDate) {
    int month = localDate.getMonthValue();
    return (month - 1) % 3 + 1;
  }

  /**
   * 获取季度
   *
   * @param localDateTime 日期
   * @return {@code 1-4}
   */
  public static int getQuarter(LocalDateTime localDateTime) {
    int month = localDateTime.getMonthValue();
    return (month - 1) % 3 + 1;
  }
  /**
   * JDK启动时间
   *
   * @return {@code Date}
   */
  @Nonnull
  public static Date getJvmStartTime() {
    long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    return new Date(startTime);
  }
  /**
   * 获取当前的天时分秒
   *
   * @return {@code int[4]} 天时分秒
   */
  public static int[] nowFieldTime() {
    return getFieldTime(System.currentTimeMillis());
  }
  /**
   * 获取天时分秒
   *
   * @param relativeMillisSecond 相对时间
   * @return {@code int[4]} 天时分秒
   */
  public static int[] getFieldTime(long relativeMillisSecond) {
    return new int[] {
      (int) (relativeMillisSecond / (ONE_DAY_MILLIS)),
      (int) (relativeMillisSecond % ONE_DAY_MILLIS / ONE_HOUR_MILLIS),
      (int) (relativeMillisSecond % ONE_HOUR_MILLIS / ONE_MINUTE_MILLIS),
      (int) (relativeMillisSecond % ONE_MINUTE_MILLIS / ONE_SECOND_MILLIS)
    };
  }

  /**
   * 获得当前时间戳
   *
   * @param lastTimeMillis 上次时间戳
   * @return 当前时间戳
   * @throws java.lang.RuntimeException 如果发生时间回退
   */
  public static long currentTimeMillis(long lastTimeMillis) {
    long timestamp = System.currentTimeMillis();
    if (timestamp < lastTimeMillis) {
      String msg =
          "Clock moved backwards. Last timestamp is %d milliseconds,now is %d milliseconds";
      throw new RuntimeException(String.format(msg, lastTimeMillis, timestamp));
    }
    return timestamp;
  }

  /**
   * 阻塞到下一个毫秒，即直到获得新的时间戳
   *
   * @param lastTimeMillis 上次时间戳
   * @return 获取下一个时间戳
   * @throws java.lang.RuntimeException 如果发生时间回退
   */
  public static long nextTimeMillis(long lastTimeMillis) {
    long timestamp = currentTimeMillis(lastTimeMillis);
    while (timestamp <= lastTimeMillis) {
      timestamp = System.currentTimeMillis();
    }
    return timestamp;
  }

  /**
   * 获取当前日期时间，包含日期和时间
   *
   * @return 返回当前日期时间
   */
  @Nonnull
  public static Date nowDateTime() {
    return new Date(System.currentTimeMillis());
  }

  /**
   * 获取当前日期时间，包含日期和时间
   *
   * @param offsetSecond 偏移秒
   * @return 返回当前偏移后的日期时间
   */
  @Nonnull
  public static Date nowDateTimeOffsetSecond(int offsetSecond) {
    return calculateTimeOffset(offsetSecond * ONE_SECOND_MILLIS);
  }

  /**
   * 获取当前日期时间，包含日期和时间
   *
   * @param offsetMinute 偏移分
   * @return 返回当前偏移后的日期时间
   */
  @Nonnull
  public static Date nowDateTimeOffsetMinute(int offsetMinute) {
    return calculateTimeOffset(offsetMinute * ONE_MINUTE_MILLIS);
  }

  /**
   * 获取当前日期时间，包含日期和时间
   *
   * @param offsetHour 偏移时
   * @return 返回当前偏移后的日期时间
   */
  @Nonnull
  public static Date nowDateTimeOffsetHour(int offsetHour) {
    return calculateTimeOffset(offsetHour * ONE_HOUR_MILLIS);
  }

  /**
   * 获取当前日期时间，包含日期和时间
   *
   * @param offsetDay 偏移天
   * @return 返回当前偏移后的日期时间
   */
  @Nonnull
  public static Date nowDateTimeOffsetDay(int offsetDay) {
    return calculateTimeOffset(offsetDay * ONE_DAY_MILLIS);
  }

  /**
   * 获取当前日期，只包含日期
   *
   * @return 返回当前日期
   */
  @Nonnull
  public static Date nowDate() {
    return nowDateOffsetDay(0);
  }

  /**
   * 获取当前日期，只包含日期
   *
   * @param offsetDay 偏移天数
   * @return 返回当前偏移后的日期时间
   */
  @Nonnull
  public static Date nowDateOffsetDay(int offsetDay) {
    return calculateTimeOffset(nowDayMillis(), offsetDay * ONE_DAY_MILLIS);
  }

  /**
   * 获取当前日期的毫秒数
   *
   * @return 返回当前日期的毫秒数
   */
  public static long nowDayMillis() {
    return getDayMillis(System.currentTimeMillis());
  }

  /**
   * 获取当前日期的毫秒数
   *
   * @return 返回当前日期的毫秒数
   */
  public static long getDayMillis(long timeMillis) {
    return timeMillis / ONE_DAY_MILLIS * ONE_DAY_MILLIS - OFFSET_MILLIS;
  }

  /**
   * 获取当前小时
   *
   * @return 返回当前小时
   */
  public static int nowHour() {
    return getHour(System.currentTimeMillis());
  }

  /**
   * 获取当前小时
   *
   * @param timeMillis 时间毫秒数
   * @return 返回当前小时
   */
  public static int getHour(long timeMillis) {
    return (int)
        ((roundingTimeTail(timeMillis, ONE_DAY_MILLIS) + OFFSET_MILLIS) / (ONE_HOUR_MILLIS));
  }

  /**
   * 获取当前分钟
   *
   * @return 返回当前分钟
   */
  public static int nowMinute() {
    return getMinute(System.currentTimeMillis());
  }

  /**
   * 获取当前分钟
   *
   * @param timeMillis 时间毫秒数
   * @return 返回当前分钟
   */
  public static int getMinute(long timeMillis) {
    return (int) ((roundingTimeTail(timeMillis, ONE_HOUR_MILLIS)) / (ONE_MINUTE_MILLIS));
  }

  /**
   * 获取当前秒数
   *
   * @return 返回当前秒数
   */
  public static int nowSecond() {
    return getSecond(System.currentTimeMillis());
  }

  /**
   * 获取当前秒数
   *
   * @param timeMillis 时间毫秒数
   * @return 返回当前秒数
   */
  public static int getSecond(long timeMillis) {
    return (int) (roundingTimeTail(timeMillis, ONE_MINUTE_MILLIS) / (ONE_SECOND_MILLIS));
  }

  /**
   * 时间取整之后多余处理的时间
   *
   * @param time 时间
   * @param rounding 取整时间
   * @return 返回剩余时间
   */
  private static long roundingTimeTail(long time, long rounding) {
    return time - (time / rounding * rounding);
  }

  /**
   * 计算偏移时间
   *
   * @param offsetMillis 偏移毫秒
   * @return 偏移后的时间
   */
  @Nonnull
  private static Date calculateTimeOffset(long offsetMillis) {
    return calculateTimeOffset(System.currentTimeMillis(), offsetMillis);
  }

  /**
   * 计算偏移时间
   *
   * @param timeMillis 时间毫秒
   * @param offsetMillis 偏移毫秒
   * @return 偏移后的时间
   */
  @Nonnull
  private static Date calculateTimeOffset(long timeMillis, long offsetMillis) {
    return new Date(Math.addExact(timeMillis, offsetMillis));
  }

  public interface SimpleDateFormatter extends PoolItem {

    String format(Date input, String format);

    Date parse(String input, String format);
  }

  private static class DefaultSimpleDateFormatter implements SimpleDateFormatter {

    private final SimpleDateFormat instance = new SimpleDateFormat();

    @Override
    public String format(Date input, String format) {
      instance.applyPattern(format);
      return instance.format(input);
    }

    @Override
    public Date parse(String input, String format) {
      instance.applyPattern(format);
      try {
        return instance.parse(input);
      } catch (ParseException e) {
        throw new IllegalArgumentException(
            String.format(
                "An exception occurred when parsing time [%s] in the format [%s]", input, format),
            e);
      }
    }
  }
}
