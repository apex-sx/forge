package pxf.toolkit.basic.lang;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.lang.iterators.ArrayIterator;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.TimeHelper;

/**
 * 日期范围
 *
 * @author potatoxf
 * @date 2021/3/14
 */
public class TimeRange implements Serializable, Iterable<Date> {

  private static final long serialVersionUID = 6507768114235712839L;
  private Date start;
  private Date end;

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    if (end != null && start != null) {
      if (end.compareTo(start) < 0) {
        throw new IllegalArgumentException("The end time must be more than the start time");
      }
    }
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    if (start != null && end != null) {
      if (start.compareTo(end) > 0) {
        throw new IllegalArgumentException("The start time must be less than the end time");
      }
    }
    this.end = end;
  }

  @Nonnull
  @Override
  public Iterator<Date> iterator() {
    if (start == null && end == null) {
      return Empty.arrayIterator();
    }
    if (start == null) {
      return new ArrayIterator<>(end);
    }
    if (end == null) {
      return new ArrayIterator<>(start);
    }
    return new ArrayIterator<>(start, end);
  }

  @Override
  public String toString() {
    if (start == null && end == null) {
      return "NO TIME RANGE";
    }
    if (start == null) {
      return "To[" + TimeHelper.formatDefaultDatetime(end) + "]";
    }
    if (end == null) {
      return "From [" + TimeHelper.formatDefaultDatetime(start) + "]";
    }
    return "From ["
        + TimeHelper.formatDefaultDatetime(start)
        + "]To["
        + TimeHelper.formatDefaultDatetime(end)
        + "]";
  }
}
