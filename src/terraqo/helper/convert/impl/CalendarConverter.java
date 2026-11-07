package potatoxf.helper.convert.impl;

import potatoxf.helper.api.Whether;
import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.date.DateUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CalendarConverter extends AbstractConverter<Calendar> {
  private static final long serialVersionUID = 1L;

  /** 日期格式化 */
  private String format;

  /**
   * 获取日期格式
   *
   * @return 设置日期格式
   */
  public String getFormat() {
    return format;
  }

  /**
   * 设置日期格式
   *
   * @param format 日期格式
   */
  public void setFormat(String format) {
    this.format = format;
  }

  @Override
  protected Calendar convertInternal(Object value) {
    // Handle Date
    if (value instanceof Date) {
      return DateUtil.calendar((Date) value);
    }

    // Handle Long
    if (value instanceof Long) {
      // 此处使用自动拆装箱
      return DateUtil.calendar((Long) value);
    }

    final String valueStr = convertToStr(value);
    return DateUtil.calendar(
        Whether.blank(format) ? DateUtil.parse(valueStr) : DateUtil.parse(valueStr, format));
  }
}
