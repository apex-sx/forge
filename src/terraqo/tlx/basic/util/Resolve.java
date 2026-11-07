package pxf.toolkit.basic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.mozilla.intl.chardet.nsDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.ExpressionException;
import pxf.toolkit.basic.lang.CharsetDetector;

/**
 * 解析操作
 *
 * @author potatoxf
 * @date 2021/3/9
 */
public final class Resolve {

  public static final int DEFAULT_RESOLVE_CHARSET_CACHE_SIZE = 2048;
  private static final Logger LOG = LoggerFactory.getLogger(Resolve.class);

  private Resolve() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(String file, String defaultCharset) {
    return Resolve.charsetSimpleChinese(file, defaultCharset, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param cacheSize 缓存大小
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(String file, String defaultCharset, int cacheSize) {
    return Resolve.charsetSimpleChinese(new File(file), defaultCharset, cacheSize);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(Path file, String defaultCharset) {
    return Resolve.charsetSimpleChinese(file, defaultCharset, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param cacheSize 缓存大小
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(Path file, String defaultCharset, int cacheSize) {
    return charsetSimpleChinese(file.toFile(), defaultCharset, cacheSize);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(File file, String defaultCharset) {
    return Resolve.charsetSimpleChinese(file, defaultCharset, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param cacheSize 缓存大小
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(File file, String defaultCharset, int cacheSize) {
    try {
      Set<String> charsets = Resolve.charset(file, nsDetector.SIMPLIFIED_CHINESE, cacheSize);
      return getSimpleChineseString(defaultCharset, charsets);
    } catch (IOException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(String.format("Error parsing character set from file [%s]", file), e);
      }
      return defaultCharset;
    }
  }

  /**
   * 检测字符集
   *
   * @param inputStream 输入流
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(InputStream inputStream, String defaultCharset) {
    return Resolve.charsetSimpleChinese(
        inputStream, defaultCharset, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param inputStream 输入流
   * @param cacheSize 缓存大小
   * @param defaultCharset 默认字符集
   * @return 返回可能字符集
   */
  public static String charsetSimpleChinese(
      InputStream inputStream, String defaultCharset, int cacheSize) {
    try {
      Set<String> charsets = Resolve.charset(inputStream, nsDetector.SIMPLIFIED_CHINESE, cacheSize);
      return getSimpleChineseString(defaultCharset, charsets);
    } catch (IOException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(
            String.format("Error parsing character set from inputStream [%s]", inputStream), e);
      }
      return defaultCharset;
    }
  }

  private static String getSimpleChineseString(String defaultCharset, Set<String> charsets) {
    if (charsets.isEmpty()) {
      return defaultCharset;
    }
    if (charsets.contains("UTF8") || charsets.contains("UTF-8")) {
      return "UTF-8";
    } else if (charsets.contains("GBK")) {
      return "GBK";
    } else if (charsets.contains("GB2312")) {
      return "GB2312";
    }
    return charsets.iterator().next();
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param lang 语言 {@link nsDetector}
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(String file, int lang) throws IOException {
    return Resolve.charset(file, lang, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param lang 语言 {@link nsDetector}
   * @param cacheSize 缓存大小
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(String file, int lang, int cacheSize) throws IOException {
    return Resolve.charset(new File(file), lang, cacheSize);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param lang 语言 {@link nsDetector}
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(Path file, int lang) throws IOException {
    return Resolve.charset(file, lang, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param lang 语言 {@link nsDetector}
   * @param cacheSize 缓存大小
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(Path file, int lang, int cacheSize) throws IOException {
    return Resolve.charset(file.toFile(), lang, cacheSize);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param lang 语言 {@link nsDetector}
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(File file, int lang) throws IOException {
    return Resolve.charset(file, lang, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param file 文件
   * @param lang 语言 {@link nsDetector}
   * @param cacheSize 缓存大小
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(File file, int lang, int cacheSize) throws IOException {
    return Resolve.charset(new FileInputStream(file), lang, cacheSize);
  }

  /**
   * 检测字符集
   *
   * @param inputStream 输入流
   * @param lang 语言 {@link nsDetector}
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(InputStream inputStream, int lang) throws IOException {
    return Resolve.charset(inputStream, lang, DEFAULT_RESOLVE_CHARSET_CACHE_SIZE);
  }

  /**
   * 检测字符集
   *
   * @param inputStream 输入流
   * @param lang 语言 {@link nsDetector}
   * @param cacheSize 缓存大小
   * @return 返回可能字符集
   * @throws IOException 如果发生错误
   */
  public static Set<String> charset(InputStream inputStream, int lang, int cacheSize)
      throws IOException {
    return new CharsetDetector(inputStream, lang, cacheSize).doCall();
  }

  /**
   * 解析特殊字符 {@code \t\n\r\f}
   *
   * @param c 字符
   * @return 返回特殊字符，如果不是则原样返回
   */
  public static char specialCharacters(char c) {
    if (c == Const.SC_T) {
      return '\t';
    }
    if (c == Const.SC_N) {
      return '\n';
    }
    if (c == Const.SC_R) {
      return '\r';
    }
    if (c == Const.SC_F) {
      return '\f';
    }
    if (c == Const.SC_B) {
      return '\b';
    }
    return c;
  }

  /**
   * 数组表达式解析
   *
   * @param input 数组表达式
   * @return 返回解析处理的数组
   * @throws ExpressionException 当数组表达式错误时
   * @see #arrayExpression(CharSequence, char, char, char, char)
   */
  public static String[] arrayExpression(CharSequence input) {
    return arrayExpression(input, ',', '\\', '[', ']');
  }

  /**
   * 数组表达式解析
   *
   * <p>支持特殊字符 {@code \t\n\r\f}
   *
   * <p>括号可有可无
   *
   * <p>引号内除特殊字符外不用转义
   *
   * <table>
   *   <tbody>
   *     <tr>
   *       <td>[a,b,c]</td>
   *       <td><code>a  b  c</code></td>
   *     </tr>
   *     <tr>
   *       <td>[a a,b b,c c]</td>
   *       <td><code>aa  bb  cc</code></td>
   *     </tr>
   *     <tr>
   *       <td>["a a","b b",c c]</td>
   *       <td><code>a a  b b  cc</code></td>
   *     </tr>
   *     <tr>
   *       <td>["a 'a'","b b",'c c']</td>
   *       <td><code>a 'a'  b b  c c</code></td>
   *     </tr>
   *     <tr>
   *       <td>["a \"a\"","b b",'c c']</td>
   *       <td><code>a "a"  b b  c c</code></td>
   *     </tr>
   *   </tbody>
   * </table>
   *
   * @param input 数组表达式
   * @param splitChar 指定分隔符
   * @param escapeChar 指定转义符
   * @param openingBrackets 左括号
   * @param closingBrackets 右括号
   * @return 返回解析处理的数组
   * @throws ExpressionException 当数组表达式错误时
   */
  public static String[] arrayExpression(
      CharSequence input,
      char splitChar,
      char escapeChar,
      char openingBrackets,
      char closingBrackets) {
    if (input == null) {
      return Empty.STRING_ARRAY;
    }
    int length = input.length();
    StringBuilder sb = new StringBuilder(length);
    List<String> list = new ArrayList<>(length / 20 + 10);
    boolean escape = false,
        startBrackets = false,
        endBrackets = false,
        doubleQuotes = false,
        singleQuotes = false,
        nextSplit = false;
    for (int i = 0; i < length; i++) {
      char c = input.charAt(i);
      // 跳过空白符
      if (!doubleQuotes && !singleQuotes && Const.INVALID_BLANK.indexOf(c) != -1) {
        continue;
      }
      // 记录转义
      if (c == escapeChar) {
        // 记录转义
        if (escape) {
          sb.append(escapeChar);
        }
        escape = !escape;
        continue;
      }
      // 为转义字符，直接作为普通字符串
      if (escape) {
        sb.append(Resolve.specialCharacters(c));
        escape = false;
        continue;
      }
      if (c == Const.C_QUOTES) {
        if (singleQuotes) {
          sb.append(Const.C_QUOTES);
        } else if (!(doubleQuotes = !doubleQuotes)) {
          nextSplit = true;
        }
        continue;
      }
      if (c == Const.C_QUOTE) {
        if (doubleQuotes) {
          sb.append(Const.C_QUOTE);
        } else if (!(singleQuotes = !singleQuotes)) {
          nextSplit = true;
        }
        continue;
      }
      if (c == openingBrackets) {
        if (startBrackets) {
          throw new ExpressionException("Repeat opening parenthesis");
        }
        if (list.size() != 0 && sb.length() != 0) {
          throw new ExpressionException(
              "There can be no element before the opening parenthesis");
        }
        startBrackets = true;
      } else if (c == closingBrackets) {
        if (!startBrackets) {
          throw new ExpressionException("Missing opening parenthesis");
        }
        if (endBrackets) {
          throw new ExpressionException("Repeat closing parenthesis");
        }
        endBrackets = true;
      } else if (!singleQuotes && !doubleQuotes && !endBrackets && c == splitChar) {
        list.add(Clear.stringBuilder(sb));
        nextSplit = false;
      } else {
        if (endBrackets) {
          throw new ExpressionException(
              "There can be no element after the closing parenthesis");
        }
        if (nextSplit) {
          throw new ExpressionException(
              "Must be an array separator after the end of the quotation mark");
        }
        sb.append(c);
      }
    }
    if (doubleQuotes) {
      throw new ExpressionException("Unterminated double quotation mark");
    }
    if (singleQuotes) {
      throw new ExpressionException("unterminated single quotation mark");
    }
    if (sb.length() != 0) {
      list.add(Clear.stringBuilder(sb));
    }
    return list.toArray(String[]::new);
  }
}
