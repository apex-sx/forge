package potatoxf.helper.api.iterate;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;

/**
 * 行可迭代
 *
 * @author potatoxf
 * @date 2021/3/20
 */
public class LineIterable implements Iterable<String> {

  /** */
  private final LineIterator lineIterator;

  /**
   * @param inputStream
   */
  public LineIterable(InputStream inputStream) {
    this(new LineIterator(inputStream));
  }

  /**
   * @param reader
   */
  public LineIterable(Reader reader) {
    this(new LineIterator(reader));
  }

  /**
   * @param inputStream
   * @param isAutoClose
   */
  public LineIterable(InputStream inputStream, boolean isAutoClose) {
    this(new LineIterator(inputStream, isAutoClose));
  }

  /**
   * @param reader
   * @param isAutoClose
   */
  public LineIterable(Reader reader, boolean isAutoClose) {
    this(new LineIterator(reader, isAutoClose));
  }

  /**
   * @param lineIterator
   */
  public LineIterable(LineIterator lineIterator) {
    this.lineIterator = lineIterator;
  }

  /**
   * @return
   */
  @Override
  public Iterator<String> iterator() {
    return lineIterator;
  }
}
