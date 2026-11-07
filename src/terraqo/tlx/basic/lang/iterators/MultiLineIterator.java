package pxf.toolkit.basic.lang.iterators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.IOFlowException;
import pxf.toolkit.basic.util.Close;
import pxf.toolkit.basic.util.ListOut;

/**
 * 行迭代器
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public class MultiLineIterator implements Iterator<String> {

  /** 最小缓存 */
  public static final int MIN_CACHE_SIZE = 1024;

  private static final Logger LOG = LoggerFactory.getLogger(MultiLineIterator.class);
  /** 默认缓存1M */
  private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
  /** 路径迭代器 */
  private final Iterator<Path> pathIterator;
  /** 缓存大小 */
  private final int cacheSize;
  /** 字符集 */
  private final Charset charset;
  /** 是否异常终止 */
  private final boolean isExceptionTerminal;
  /** 当前行读取器 */
  private LineNumberReader currentLineNumberReader;
  /** 当前路径 */
  private Path currentPath;
  /** 当前行内容 */
  private String currentCacheLine;
  /** 当前行号 */
  private int currentNumber;

  public MultiLineIterator(Path rootDirectory, String... filenames) {
    this(
        ListOut.pathsExisting(rootDirectory, filenames),
        false,
        DEFAULT_CACHE_SIZE,
        StandardCharsets.UTF_8);
  }

  public MultiLineIterator(int cacheSize, Path rootDirectory, String... filenames) {
    this(ListOut.pathsExisting(rootDirectory, filenames), false, cacheSize, StandardCharsets.UTF_8);
  }

  public MultiLineIterator(
      List<Path> paths, boolean isExceptionTerminal, int cacheSize, Charset charset) {
    if (paths.size() == 0) {
      throw new IllegalArgumentException("Contains at least one file path");
    }
    if (cacheSize < MIN_CACHE_SIZE) {
      cacheSize = DEFAULT_CACHE_SIZE;
    }
    if (charset == null) {
      charset = StandardCharsets.UTF_8;
    }
    this.pathIterator = paths.iterator();
    this.isExceptionTerminal = isExceptionTerminal;
    this.cacheSize = cacheSize;
    this.charset = charset;
  }

  /**
   * 获取当前行号
   *
   * @return 返回当前行号，如果返回 {@code 0}则表示未读
   */
  public int getCurrentNumber() {
    return currentNumber;
  }

  /**
   * 获取当前文件
   *
   * @return 获取当前行号，如果返回 {@code null}则表示未读
   */
  public String getCurrentPath() {
    return currentPath == null ? null : currentPath.toString();
  }

  @Override
  public String next() {
    if (currentCacheLine == null) {
      throw new NoSuchElementException("The iteration has no more elements");
    }
    return currentCacheLine;
  }

  @Override
  public boolean hasNext() {
    while (currentLineNumberReader != null || pathIterator.hasNext()) {
      if (currentLineNumberReader == null) {
        currentPath = pathIterator.next();
        currentLineNumberReader = buildCurrentLineNumberReader();
      }
      if (currentLineNumberReader == null) {
        continue;
      }
      currentCacheLine = readCurrentLine();
      if (currentCacheLine != null) {
        return true;
      }
      Close.closeableSilently(currentLineNumberReader);
      currentLineNumberReader = null;
      currentNumber = 0;
    }
    return false;
  }

  private LineNumberReader buildCurrentLineNumberReader() {
    try {
      return new LineNumberReader(
          new InputStreamReader(new FileInputStream(currentPath.toFile()), charset), cacheSize);
    } catch (FileNotFoundException e) {
      if (isExceptionTerminal || LOG.isWarnEnabled()) {
        String msg = String.format("The file [%s] not found, will be skipped", currentPath);
        if (LOG.isWarnEnabled()) {
          LOG.warn(msg, e);
        }
        if (isExceptionTerminal) {
          throw new IOFlowException(msg, e);
        }
      }
      e.printStackTrace();
    }
    return null;
  }

  private String readCurrentLine() {
    currentNumber++;
    try {
      return currentLineNumberReader.readLine();
    } catch (IOException e) {
      if (isExceptionTerminal || LOG.isWarnEnabled()) {
        String msg =
            String.format(
                "An exception occurred while reading the file [%s] at line [%d] and will be skipped",
                currentPath, currentNumber);
        if (LOG.isWarnEnabled()) {
          LOG.warn(msg, e);
        }
        if (isExceptionTerminal) {
          throw new IOFlowException(msg, e);
        }
      }
      e.printStackTrace();
    }
    return null;
  }
}
