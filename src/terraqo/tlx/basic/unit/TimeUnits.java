package pxf.toolkit.basic.unit;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import pxf.toolkit.basic.unit.impl.NonIsometric;
import pxf.toolkit.basic.unit.impl.UnitConverter;
import pxf.toolkit.basic.unit.impl.UnitConverterImpl;

/**
 * 时间单位
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public enum TimeUnits implements NonIsometric<TimeUnits>, UnitConverterImpl<TimeUnits> {
  /** 纳秒 */
  NS(TimeUnit.NANOSECONDS, ChronoUnit.NANOS, 1000),
  /** 微妙 */
  MMS(TimeUnit.MICROSECONDS, ChronoUnit.MICROS, 1000),
  /** 毫秒 */
  MS(TimeUnit.MILLISECONDS, ChronoUnit.MILLENNIA, 1000),
  /** 秒 */
  SEC(TimeUnit.SECONDS, ChronoUnit.SECONDS, 60),
  /** 分钟 */
  MIN(TimeUnit.MINUTES, ChronoUnit.MINUTES, 60),
  /** 小时 */
  HOUR(TimeUnit.HOURS, ChronoUnit.HOURS, 24),
  /** 天 */
  DAY(TimeUnit.DAYS, ChronoUnit.DAYS, -1);
  private final UnitConverter<TimeUnits> converter = UnitConverter.of(this);
  private final TimeUnit timeUnit;
  private final ChronoUnit chronoUnit;
  private final int next;

  TimeUnits(final TimeUnit timeUnit, ChronoUnit chronoUnit, final int next) {
    this.timeUnit = timeUnit;
    this.chronoUnit = chronoUnit;
    this.next = next;
  }

  public static TimeUnits from(final TimeUnit timeUnit) {
    Optional<TimeUnits> found =
        Arrays.stream(TimeUnits.values())
            .filter(timeUnits -> timeUnits.timeUnit.equals(timeUnit))
            .findFirst();
    if (found.isPresent()) {
      return found.get();
    }
    throw new IllegalArgumentException("No TimeUnit equivalent for " + timeUnit);
  }

  public static TimeUnits from(ChronoUnit chronoUnit) {
    Optional<TimeUnits> found =
        Arrays.stream(TimeUnits.values())
            .filter(timeUnits -> timeUnits.chronoUnit.equals(chronoUnit))
            .findFirst();
    if (found.isPresent()) {
      return found.get();
    }
    throw new IllegalArgumentException("No TimeUnit equivalent for " + chronoUnit);
  }

  public final TimeUnit toTimeUnit() {
    return timeUnit;
  }

  public final ChronoUnit toChronoUnit() {
    return chronoUnit;
  }

  @Override
  public int nextInterval() {
    return next;
  }

  /**
   * 返回持有单位转换器
   *
   * @return {@code UnitConverter<E>}
   */
  @Override
  public UnitConverter<TimeUnits> holdingUnitConverter() {
    return converter;
  }
}
