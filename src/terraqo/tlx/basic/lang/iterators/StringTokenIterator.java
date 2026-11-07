package pxf.toolkit.basic.lang.iterators;

import java.util.Arrays;
import java.util.Iterator;
import pxf.toolkit.basic.util.Empty;

/**
 * @author potatoxf
 * @date 2021/5/20
 */
public class StringTokenIterator implements Iterator<String[]> {

  private final Iterator<String> stringIterator;
  private final String splitRegexp;
  private final int fixedLength;

  public StringTokenIterator(Iterator<String> stringIterator, String splitRegexp, int fixedLength) {
    this.stringIterator = stringIterator;
    this.splitRegexp = splitRegexp;
    this.fixedLength = fixedLength;
  }

  @Override
  public boolean hasNext() {
    return stringIterator.hasNext();
  }

  @Override
  public String[] next() {
    String string = stringIterator.next();
    string = preprocessedString(string);
    if (string == null) {
      String[] empty = new String[fixedLength];
      Arrays.fill(empty, Empty.STRING);
      return empty;
    }
    String[] result = string.split(splitRegexp, fixedLength);
    if (result.length < fixedLength) {
      String[] copy = new String[fixedLength];
      System.arraycopy(result, 0, copy, 0, result.length);
      Arrays.fill(copy, result.length, fixedLength, Empty.STRING);
      return copy;
    }
    return result;
  }

  protected String preprocessedString(String string) {
    return string;
  }

  public String getSplitRegexp() {
    return splitRegexp;
  }

  public int getFixedLength() {
    return fixedLength;
  }
}
