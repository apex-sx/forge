package pxf.toolkit.basic.lang;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import org.mozilla.intl.chardet.nsDetector;
import pxf.toolkit.basic.util.Cast;

/**
 * 字符集检测器
 *
 * @author potatoxf
 * @date 2021/4/17
 */
public class CharsetDetector implements Callable<Set<String>> {

  private final InputStream inputStream;
  private final int lang;
  private final int cacheSize;
  private boolean found = false;

  /**
   * 字符集侦测构造
   *
   * @param inputStream 输入流
   * @param lang 语言 {@link nsDetector}
   */
  public CharsetDetector(InputStream inputStream, int lang) {
    this(inputStream, lang, 2048);
  }

  /**
   * 字符集侦测构造
   *
   * @param inputStream 输入流
   * @param lang 语言 {@link nsDetector}
   * @param cacheSize 缓存大小
   */
  public CharsetDetector(InputStream inputStream, int lang, int cacheSize) {
    this.inputStream = inputStream;
    this.lang = lang;
    this.cacheSize = cacheSize;
  }

  @Override
  public Set<String> call() throws Exception {
    return doCall();
  }

  public Set<String> doCall() throws IOException {
    Set<String> prob = new HashSet<>();
    // Initalize the nsDetector() ;
    nsDetector det = new nsDetector(lang);
    // Set an observer...
    // The Notify() will be called when a matching charset is found.
    det.Init(
        charset -> {
          found = true;
          prob.add(charset.toUpperCase());
        });
    BufferedInputStream imp =
        inputStream instanceof BufferedInputStream
            ? (BufferedInputStream) inputStream
            : new BufferedInputStream(inputStream, cacheSize);
    byte[] buf = new byte[cacheSize];
    int len;
    boolean isAscii = true;
    while ((len = imp.read(buf, 0, buf.length)) != -1) {
      // Check if the stream is only ascii.
      if (isAscii) {
        isAscii = det.isAscii(buf, len);
      }
      // DoIt if non-ascii and not done yet.
      if (!isAscii) {
        if (det.DoIt(buf, len, false)) {
          break;
        }
      }
    }
    imp.close();
    det.DataEnd();
    if (isAscii) {
      found = true;
      return Set.of("ASCII");
    } else if (found) {
      return Collections.unmodifiableSet(prob);
    } else {
      return Set.of(Cast.uppercase(det.getProbableCharsets()));
    }
  }
}
