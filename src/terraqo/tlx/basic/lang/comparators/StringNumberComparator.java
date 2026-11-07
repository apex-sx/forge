package pxf.toolkit.basic.lang.comparators;

import java.util.Comparator;
import pxf.toolkit.basic.util.Is;

/**
 * 字符串数字比较器
 *
 * @author potatoxf
 * @date 2021/1/20
 */
public class StringNumberComparator implements Comparator<String> {

  private static final String ZERO = "0";
  private final boolean isEmptyIsZero;
  private final boolean isDecimal;

  public StringNumberComparator(boolean isDecimal) {
    this(true, isDecimal);
  }

  public StringNumberComparator(boolean isEmptyIsZero, boolean isDecimal) {
    this.isEmptyIsZero = isEmptyIsZero;
    this.isDecimal = isDecimal;
  }

  @Override
  public int compare(String num1, String num2) {
    boolean empty1 = Is.empty(num1);
    boolean empty2 = Is.empty(num2);
    if (isEmptyIsZero) {
      if (empty1) {
        num1 = ZERO;
      }
      if (empty2) {
        num2 = ZERO;
      }
    } else {
      if (empty1 || empty2) {
        throw new IllegalArgumentException();
      }
    }
    if (isDecimal) {
      double d1 = Double.parseDouble(num1);
      double d2 = Double.parseDouble(num2);
      return Double.compare(d1, d2);
    }
    long l1 = Long.parseLong(num1);
    long l2 = Long.parseLong(num2);
    return Long.compare(l1, l2);
  }
}
