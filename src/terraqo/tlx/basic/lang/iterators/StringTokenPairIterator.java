package pxf.toolkit.basic.lang.iterators;

import java.util.Iterator;
import pxf.toolkit.basic.lang.ImmutablePair;
import pxf.toolkit.basic.lang.Pair;

/**
 * @author potatoxf
 * @date 2021/5/20
 */
public class StringTokenPairIterator implements Iterator<Pair<String, String>> {
  private final StringTokenIterator stringTokenIterator;

  public StringTokenPairIterator(Iterator<String> stringIterator, String splitRegexp) {
    stringTokenIterator = new InnerStringTokenIterator(stringIterator, splitRegexp);
  }

  @Override
  public boolean hasNext() {
    return stringTokenIterator.hasNext();
  }

  @Override
  public Pair<String, String> next() {
    String[] result = stringTokenIterator.next();
    String key = resolveKey(result);
    String value = resolveValue(result);
    return new ImmutablePair<>(key, value);
  }

  protected String preprocessedString(String string) {
    return string;
  }

  protected String resolveKey(String[] result) {
    return result[0];
  }

  protected String resolveValue(String[] result) {
    return result[1];
  }

  private class InnerStringTokenIterator extends StringTokenIterator {

    public InnerStringTokenIterator(Iterator<String> stringIterator, String splitRegexp) {
      super(stringIterator, splitRegexp, 2);
    }

    @Override
    protected String preprocessedString(String string) {
      return StringTokenPairIterator.this.preprocessedString(string);
    }
  }
}
