package pxf.toolkit.basic.lang.iterators;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.util.NewSilently;

/**
 * 行可迭代
 *
 * @author potatoxf
 * @date 2021/3/20
 */
public class LineIterable implements Iterable<String> {

  private final LineIterator lineIterator;

  public LineIterable(String file) {
    this(NewSilently.fileInputStream(file), true);
  }

  public LineIterable(File file) {
    this(NewSilently.fileInputStream(file), true);
  }

  public LineIterable(InputStream inputStream) {
    this(new LineIterator(inputStream));
  }

  public LineIterable(Reader reader) {
    this(new LineIterator(reader));
  }

  public LineIterable(InputStream inputStream, boolean isAutoClose) {
    this(new LineIterator(inputStream, isAutoClose));
  }

  public LineIterable(Reader reader, boolean isAutoClose) {
    this(new LineIterator(reader, isAutoClose));
  }

  public LineIterable(LineIterator lineIterator) {
    this.lineIterator = lineIterator;
  }

  @Nonnull
  @Override
  public Iterator<String> iterator() {
    return lineIterator;
  }
}
