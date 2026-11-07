package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;

import java.util.TimeZone;

/**
 * TimeZone转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone> {
  private static final long serialVersionUID = 1L;

  @Override
  protected TimeZone convertInternal(Object value) {
    return TimeZone.getTimeZone(convertToStr(value));
  }
}
