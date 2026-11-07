package pxf.toolkit.basic.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.lang.AsciiTableMatcher;

/**
 * 获取操作
 *
 * @author potatoxf
 * @date 2021/3/12
 */
public final class Get {

  /** ascii 可见字符 */
  public static final String ASCII_VISIBLE_CHARS_REG =
      "[0-9A-Za-z`~!@#%&-_=;:'\",<>/$()*+.\\[\\]?^{}\\\\]+";
  /** 有效字符 */
  public static final Pattern VALID_CHARACTER_PATTERN =
      Pattern.compile("^" + ASCII_VISIBLE_CHARS_REG + "$");
  /** 文件目录名 */
  public static final String FILE_DIR_NAME_REG = "[^:<>\"/^|*?\\\\]+";
  /** 文件目录名正则表达式 */
  public static final Pattern FILE_DIR_NAME_PATTERN =
      Pattern.compile("^" + FILE_DIR_NAME_REG + "$");
  /** 邮箱 */
  public static final String EMAIL_REG = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";
  /** 邮箱正则表达式 */
  public static final Pattern EMAIL_PATTERN = Pattern.compile("^" + EMAIL_REG + "$");
  // --------------------------------------------------------------------------- 端口号
  /** 十六进制端口号 */
  public static final String PORT_HEX_REG = "0[xX][0-9A-Fa-f]{0,4}";
  /** 十六进制端口号正则表达式 */
  public static final Pattern PORT_HEX_PATTERN = Pattern.compile("^" + PORT_HEX_REG + "$");
  // --------------------------------------------------------------------------- IP
  /** ipv4 */
  public static final String IPV4_REG =
      "(25[0-5]|(2[0-4]|1\\d|[1-9]?)\\d)(\\.(25[0-5]|(2[0-4]|1\\d|[1-9]?)\\d)){3}";
  /** ipv4正则表达式 */
  public static final Pattern IPV4_PATTERN = Pattern.compile("^" + IPV4_REG + "$");
  /** 端口号 */
  public static final String PORT_REG =
      "0|6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}";
  /** 端口号正则表达式 */
  public static final Pattern PORT_PATTERN = Pattern.compile("^" + PORT_REG + "$");
  // --------------------------------------------------------------------------- 数字正则表达式
  /** 八进制 */
  public static final Pattern OCT_PATTERN = Pattern.compile("^0[0-7]+$");
  /** 十进制 */
  public static final Pattern DEC_PATTERN = Pattern.compile("^\\d+$");
  /** 十六进制 */
  public static final Pattern HEX_PATTERN = Pattern.compile("^0[xX][\\dA-Fa-f]+$");
  /** 整数 */
  public static final Pattern INTEGER_PATTERN = Pattern.compile("^[\\-+]?(\\d+)$");
  /** 小数 */
  public static final Pattern DECIMAL_PATTERN = Pattern.compile("^[\\-+]?(\\d+)(\\.\\d+)?$");
  private static final Logger LOG = LoggerFactory.getLogger(Get.class);
  /** @see Throwable#initCause(Throwable) */
  private static final Method INIT_CAUSE_METHOD = initCause();

  private Get() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 获得服务器的IP地址
   *
   * @return ip地址
   */
  @Nonnull
  public static String localIP() {
    String sIP = "";
    InetAddress ip = null;
    try {
      boolean bFindIP = false;
      Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
      while (netInterfaces.hasMoreElements()) {
        if (bFindIP) {
          break;
        }
        NetworkInterface ni = netInterfaces.nextElement();
        Enumeration<InetAddress> ips = ni.getInetAddresses();
        while (ips.hasMoreElements()) {
          ip = ips.nextElement();
          if (!ip.isLoopbackAddress() && IPV4_PATTERN.matcher(ip.getHostAddress()).matches()) {
            bFindIP = true;
            break;
          }
        }
      }
    } catch (Exception ignored) {
    }
    if (null != ip) {
      sIP = ip.getHostAddress();
    }
    return sIP;
  }

  /**
   * 获得服务器的IP地址(多网卡)
   *
   * @return ip地址
   */
  @Nonnull
  public static List<String> localIPS() {
    InetAddress ip;
    List<String> ipList = new ArrayList<>();
    try {
      Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
      while (netInterfaces.hasMoreElements()) {
        NetworkInterface ni = netInterfaces.nextElement();
        Enumeration<InetAddress> ips = ni.getInetAddresses();
        while (ips.hasMoreElements()) {
          ip = ips.nextElement();
          if (!ip.isLoopbackAddress() && IPV4_PATTERN.matcher(ip.getHostAddress()).matches()) {
            ipList.add(ip.getHostAddress());
          }
        }
      }
    } catch (Exception ignored) {
    }
    return ipList;
  }

  /**
   * 获取自身类和继承类
   *
   * @param obj 对象
   * @return 返回自身类和继承类
   */
  @Nonnull
  public static Set<Class<?>> selfClasses(@Nullable Object obj) {
    if (obj == null) {
      return Collections.emptySet();
    }
    return obj instanceof Class ? selfClasses((Class<?>) obj) : selfClasses(obj.getClass());
  }

  /**
   * 获取自身类和继承类
   *
   * @param clz 类
   * @return 返回自身类和继承类
   */
  @Nonnull
  public static Set<Class<?>> selfClasses(@Nullable Class<?> clz) {
    if (clz == null) {
      return Collections.emptySet();
    }
    int count = Statics.countClass(clz);
    Set<Class<?>> result = new LinkedHashSet<>(count);
    for (Class<?> c = clz; c != null; c = c.getSuperclass()) {
      result.add(c);
    }
    return result;
  }

  /**
   * 获取 {@code Throwable#initCause(Throwable)}
   *
   * @return {@code Throwable#initCause(Throwable)}
   * @see Throwable#initCause(Throwable)
   */
  public static Method initCauseMethod() {
    return INIT_CAUSE_METHOD;
  }

  // ---------------------------------------------------------------------------

  /**
   * 获取父类泛型类型
   *
   * @param fromType 从哪个类型获取
   * @param index 获取第几个
   * @return {@code Class<?> }
   * @throws ClassNotFoundException 没有找到该类型
   */
  public static Class<?> genericFromSuperclass(@Nonnull Class<?> fromType, int index)
      throws ClassNotFoundException {
    Assert.noNegative(index, "]index");
    Type superclass = fromType.getGenericSuperclass();
    Type[] actualTypeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
    if (actualTypeArguments.length <= index) {
      throw new IllegalArgumentException(
          "The index out of actual fromType length for " + actualTypeArguments.length);
    }
    return Class.forName(((Class) actualTypeArguments[index]).getName());
  }

  /**
   * 获取系统行分割符
   *
   * @return 行风格符
   */
  @Nonnull
  public static String systemLineSeparator() {
    // avoid security issues
    StringWriter buf = new StringWriter(2);
    PrintWriter out = new PrintWriter(buf);
    out.println();
    return buf.toString();
  }

  /**
   * 获取系统路径分割符
   *
   * @return 路径分割符
   */
  public static char systemPathSeparator() {
    return Is.windowsSystem() ? '\\' : '/';
  }

  /**
   * 获取文件扩展名的索引
   *
   * @param filepath 文件路径
   * @return 返回文件名称
   */
  public static int extensionIndex(@Nullable CharSequence filepath) {
    if (filepath == null) {
      return -1;
    }
    int length = filepath.length();
    if (length == 0) {
      return -1;
    }
    String s = filepath.toString();
    int dotI = s.lastIndexOf(".");
    if (dotI != -1) {
      int sI = s.lastIndexOf("/");
      int bsI = s.lastIndexOf("\\");
      if (dotI > Math.max(sI, bsI)) {
        return dotI;
      }
    }
    return -1;
  }

  /**
   * 获取文件扩展名
   *
   * @param filepath 文件路径
   * @return 返回文件名称
   */
  @Nonnull
  public static CharSequence extensionName(@Nullable CharSequence filepath) {
    if (filepath == null) {
      return Empty.STRING;
    }
    int length = filepath.length();
    if (length == 0) {
      return Empty.STRING;
    }
    int pi = extensionIndex(filepath);
    if (pi == -1) {
      return Empty.STRING;
    }
    return filepath.subSequence(pi, length);
  }

  public static int filenameIndex(@Nullable CharSequence filepath) {
    if (filepath == null) {
      return -1;
    }
    int length = filepath.length();
    if (length == 0) {
      return -1;
    }
    String s = filepath.toString();
    int pi = Math.max(s.lastIndexOf('/'), s.lastIndexOf('\\'));
    if (pi == -1 || pi + 1 == length) {
      return -1;
    }
    return pi + 1;
  }

  /**
   * 获取文件名称
   *
   * @param filepath 文件路径
   * @return 返回文件名称
   */
  @Nonnull
  public static CharSequence filename(@Nullable CharSequence filepath) {
    if (filepath == null) {
      return Empty.STRING;
    }
    int length = filepath.length();
    if (length == 0) {
      return Empty.STRING;
    }
    int pi = filenameIndex(filepath);
    if (pi == -1) {
      return filepath;
    }
    return filepath.subSequence(pi, length);
  }

  /**
   * 获取父级目录
   *
   * @param filepath 文件路径
   * @return 返回父级目录
   */
  @Nonnull
  public static CharSequence parentDirectory(@Nullable CharSequence filepath) {
    if (filepath == null) {
      return Empty.STRING;
    }
    int length = filepath.length();
    if (length == 0) {
      return Empty.STRING;
    }
    int pi = filenameIndex(filepath);
    if (pi == -1) {
      return Empty.STRING;
    }
    return filepath.subSequence(0, pi);
  }

  /**
   * 获取字符
   *
   * @param sb {@code StringBuffer}
   * @return 字符串
   */
  @Nonnull
  public static String string(StringBuffer sb) {
    if (Is.empty(sb)) {
      return Empty.STRING;
    }
    return sb.toString();
  }

  /**
   * 获取字符
   *
   * @param sb {@code StringBuffer}
   * @param trimTailLength 清除尾部长度，如果超出字符长度则全部删除
   * @return 字符串
   */
  @Nonnull
  public static String stringAndTrimTail(StringBuffer sb, int trimTailLength) {
    if (Is.empty(sb)) {
      return Empty.STRING;
    }
    int i = sb.length() - trimTailLength;
    if (i < 0) {
      return Empty.STRING;
    }
    return sb.delete(i, Integer.MAX_VALUE).toString();
  }

  /**
   * 获取字符并清空
   *
   * @param sb {@code StringBuffer}
   * @return 字符串
   */
  @Nonnull
  public static String stringAndClear(StringBuffer sb) {
    if (Is.empty(sb)) {
      return Empty.STRING;
    }
    String result = sb.toString();
    sb.delete(0, Integer.MAX_VALUE);
    return result;
  }

  /**
   * 获取字符
   *
   * @param sb {@code StringBuilder}
   * @return 字符串
   */
  @Nonnull
  public static String string(StringBuilder sb) {
    if (Is.empty(sb)) {
      return Empty.STRING;
    }
    return sb.toString();
  }

  /**
   * 获取字符
   *
   * @param sb {@code StringBuffer}
   * @param trimTailLength 清除尾部长度，如果超出字符长度则全部删除
   * @return 字符串
   */
  @Nonnull
  public static String stringAndTrimTail(StringBuilder sb, int trimTailLength) {
    if (Is.empty(sb)) {
      return Empty.STRING;
    }
    int i = sb.length() - trimTailLength;
    if (i < 0) {
      return Empty.STRING;
    }
    return sb.delete(i, Integer.MAX_VALUE).toString();
  }
  // ---------------------------------------------------------------------------

  /**
   * 获取字符并清空
   *
   * @param sb {@code StringBuilder}
   * @return 字符串
   */
  @Nonnull
  public static String stringAndClear(StringBuilder sb) {
    if (Is.empty(sb)) {
      return Empty.STRING;
    }
    String result = sb.toString();
    sb.delete(0, Integer.MAX_VALUE);
    return result;
  }

  /**
   * 返回数字的长度
   *
   * @param num 数字
   * @return 返回长度
   */
  public static int len(@Nullable Integer num) {
    if (num == null) {
      return 0;
    }
    if (num == 0) {
      return 1;
    }
    int value = num;
    int count = 0;
    do {
      count++;
      value /= 10;
    } while (value != 0);
    return count;
  }

  /**
   * 返回数字的长度
   *
   * @param num 数字
   * @return 返回长度
   */
  public static int len(@Nullable Long num) {
    if (num == null) {
      return 0;
    }
    if (num == 0) {
      return 1;
    }
    long value = num;
    int count = 0;
    do {
      count++;
      value /= 10L;
    } while (value != 0L);
    return count;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable boolean[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable byte[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable char[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable short[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable int[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable long[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable float[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable double[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static <T> int len(@Nullable T[] input) {
    if (input == null) {
      return 0;
    }
    return input.length;
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable CharSequence input) {
    if (input == null) {
      return 0;
    }
    return input.length();
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable Collection<?> input) {
    if (input == null) {
      return 0;
    }
    return input.size();
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable Map<?, ?> input) {
    if (input == null) {
      return 0;
    }
    return input.size();
  }

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable Iterable<?> input) {
    if (input == null) {
      return 0;
    }
    return len(input.iterator());
  }

  // ---------------------------------------------------------------------------

  /**
   * 获取长度，当为 {@code null}时则是0
   *
   * @param input 输入带有长度元素的值
   * @return 长度，当为 {@code null}时为0
   */
  public static int len(@Nullable Iterator<?> input) {
    if (input == null) {
      return 0;
    }
    int len = 0;
    while (input.hasNext()) {
      input.next();
      len++;
    }
    return len;
  }

  /**
   * 获取带有小数的数字
   *
   * @param input 输入字符串
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  @Nullable
  public static String decimalNumber(@Nullable String input, @Nullable String defaultValue) {
    return decimalNumber(input, 1, defaultValue);
  }

  /**
   * 找到找到带有小数的数字
   *
   * @param input 输入字符串
   * @param findCount 找到第几个数字
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  @Nullable
  public static String decimalNumber(
      @Nullable String input, int findCount, @Nullable String defaultValue) {
    return number(input, findCount, true, defaultValue);
  }

  /**
   * 找到整数
   *
   * @param input 输入字符串
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  @Nullable
  public static String integerNumber(@Nullable String input, @Nullable String defaultValue) {
    return integerNumber(input, 1, defaultValue);
  }

  /**
   * 找到整数
   *
   * @param input 输入字符串
   * @param findCount 找到第几个数字
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  @Nullable
  public static String integerNumber(
      @Nullable String input, int findCount, @Nullable String defaultValue) {
    return number(input, findCount, false, defaultValue);
  }

  /**
   * 找到数字
   *
   * @param input 输入字符串
   * @param findCount 找到第几个数字
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  @Nullable
  private static String number(
      @Nullable String input, int findCount, boolean isDecimal, @Nullable String defaultValue) {
    if (Is.empty(input)) {
      return defaultValue;
    }
    Assert.positive(findCount);
    int startIndex = -1;
    int count = 0;
    int len = input.length();
    for (int i = 0; i < len; i++) {
      char c = input.charAt(i);
      if (Character.isDigit(c)) {
        if (startIndex == -1) {
          startIndex = i;
        }
        continue;
      }
      if (startIndex != -1 && !(isDecimal && c == '.')) {
        count++;
        if (count != findCount) {
          startIndex = -1;
          continue;
        }
        char pre = input.charAt(startIndex);
        if (AsciiTableMatcher.isMatcherExceptAsciiChar(pre, AsciiTableMatcher.ARITHMETIC_ADD_SUB)) {
          startIndex--;
        }
        return input.substring(startIndex, i);
      }
    }
    if (startIndex != -1) {
      char pre = input.charAt(startIndex - 1);
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(pre, AsciiTableMatcher.ARITHMETIC_ADD_SUB)) {
        startIndex--;
      }
      return input.substring(startIndex);
    }
    return defaultValue;
  }

  /**
   * Returns a <code>Method<code> allowing access to {@link Throwable#initCause(Throwable)} method
   * of {@link Throwable}, or <code>null</code> if the method does not exist.
   *
   * @return A <code>Method<code> for <code>Throwable.initCause</code>, or
   * <code>null</code> if unavailable.
   */
  private static Method initCause() {
    try {
      Class<?>[] paramsClasses = new Class<?>[] {Throwable.class};
      return Throwable.class.getMethod("initCause", paramsClasses);
    } catch (NoSuchMethodException e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Throwable does not have initCause() method in JDK 1.3");
      }
      return null;
    } catch (Throwable e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Error getting the Throwable initCause() method", e);
      }
      return null;
    }
  }

  /**
   * 获取客户端主机名称
   *
   * @return {@code String} ,
   */
  public static String hostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ignored) {
    }
    return null;
  }

  /**
   * 获取JDK名称
   *
   * @return {@code String}
   */
  public static String jvmName() {
    return ManagementFactory.getRuntimeMXBean().getVmName();
  }
}
