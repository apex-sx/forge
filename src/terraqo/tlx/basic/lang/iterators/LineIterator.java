package pxf.toolkit.basic.lang.iterators;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import pxf.toolkit.basic.util.Close;
import pxf.toolkit.basic.util.NewSilently;

/**
 * 行迭代器
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public class LineIterator implements Iterator<String>, Closeable {

  private final BufferedReader bufferedReader;
  private final boolean isAutoClose;
  private String cachedLine;
  private boolean finished = false;

  public LineIterator(String file) {
    this(NewSilently.fileInputStream(file), true);
  }

  public LineIterator(File file) {
    this(NewSilently.fileInputStream(file), true);
  }

  public LineIterator(InputStream inputStream) {
    this(inputStream, true);
  }

  public LineIterator(InputStream inputStream, boolean isAutoClose) {
    this(new InputStreamReader(inputStream), isAutoClose);
  }

  public LineIterator(Reader reader) {
    this(reader, true);
  }

  public LineIterator(Reader reader, boolean isAutoClose) {
    Objects.requireNonNull(reader, "The reader must not be null");
    if (reader instanceof BufferedReader) {
      bufferedReader = (BufferedReader) reader;
    } else {
      bufferedReader = new BufferedReader(reader);
    }
    this.isAutoClose = isAutoClose;
  }

  /**
   * 关闭行迭代器并忽略异常
   *
   * @param lineIterator 关闭行迭代器
   */
  public static void closeQuietly(LineIterator lineIterator) {
    if (lineIterator != null) {
      lineIterator.close();
    }
  }

  /**
   * 指示<code> Reader </ code>是否有更多行。 *如果存在<code> IOException </ code>，则将在此实例上调用{@link #close（）}。
   *
   * @return 如果有下一行则返回 {@code true}
   * @throws IllegalStateException 如果发生IO异常
   */
  @Override
  public final boolean hasNext() {
    if (cachedLine != null) {
      return true;
    } else if (finished) {
      return false;
    } else {
      try {
        while (true) {
          String line = bufferedReader.readLine();
          if (line == null) {
            finished = true;
            return false;
          } else if (isValidLine(line)) {
            cachedLine = line;
            return true;
          }
        }
      } catch (IOException ioe) {
        close();
        throw new IllegalStateException(ioe.toString());
      }
    }
  }

  /**
   * 返回下一行。
   *
   * @return 输入的下一行
   * @throws NoSuchElementException 如果没有行返回
   */
  @Override
  public final String next() {
    String nextLine = nextLine();
    if (isAutoClose && !hasNext()) {
      close();
    }
    return nextLine;
  }

  /**
   * 不支持移除.
   *
   * @throws UnsupportedOperationException 总是抛出
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove unsupported on LineIterator");
  }

  /**
   * 返回包装的 {@code Reader}中的下一行。
   *
   * @return 输入的下一行
   * @throws NoSuchElementException 如果没有行返回
   */
  public String nextLine() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more lines");
    }
    String currentLine = cachedLine;
    cachedLine = null;
    return currentLine;
  }

  /** 关闭相关的流 */
  @Override
  public void close() {
    finished = true;
    Close.closeableSilently(bufferedReader);
    cachedLine = null;
  }

  /**
   * 用于验证返回的每一行的可重写方法。
   *
   * @param line 要验证的行
   * @return 如果有效，则返回 {@code true}；从迭代器中移除，返回 {@code false}
   */
  protected boolean isValidLine(String line) {
    return true;
  }
}
