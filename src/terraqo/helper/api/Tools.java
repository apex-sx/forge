package potatoxf.helper.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.api.function.FactoryThrow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author potatoxf
 * @date 2022/7/3
 */
public final class Tools {
  /** 无效数字，适合所有数字返回值判断 */
  public static final int EOF = -1;
  /** 零 */
  public static final byte ZERO_BYTE = 0;
  /** 零 */
  public static final double ZERO_DOUBLE = 0;
  /** 零 */
  public static final float ZERO_FLOAT = 0;
  /** 零 */
  public static final int ZERO_INT = 0;
  /** 零 */
  public static final long ZERO_LONG = 0;
  /** 零 */
  public static final short ZERO_SHORT = 0;
  /** Ascii 小写字母编码上界 */
  public static final int ASCII_LOWER_LETTER_HI = 122;
  /** Ascii 小写字母编码下界 */
  public static final int ASCII_LOWER_LETTER_LO = 97;
  /** Ascii 数字编码上界 */
  public static final int ASCII_NUMBER_HI = 57;
  /** Ascii 数字编码下界 */
  public static final int ASCII_NUMBER_LO = 48;
  /** Ascii 大写字母编码上界 */
  public static final int ASCII_UPPER_LETTER_HI = 90;
  /** Ascii 大写字母编码下界 */
  public static final int ASCII_UPPER_LETTER_LO = 65;
  /** 单引号 */
  public static final char C_QUOTE = '\'';
  /** 双引号 */
  public static final char C_QUOTES = '"';
  /** 默认分割符号 */
  public static final String DEFAULT_DELIMITER = ",";
  /** 无效字符串，适合所有数字返回值判断 */
  public static final String EMPTY_STRING = "";
  /** false */
  public static final boolean FALSE = false;
  /** 无效空白符 */
  public static final String INVALID_BLANK = " \t\n\r\f";
  /** 无效字符串，适合所有数字返回值判断 */
  public static final String NULL_STRING = "null";
  /** 特殊字符--> {@code \b} */
  public static final char SC_B = 'b';
  /** 特殊字符--> {@code \f} */
  public static final char SC_F = 'f';
  /** 特殊字符--> {@code \n} */
  public static final char SC_N = 'n';
  /** 特殊字符--> {@code \r} */
  public static final char SC_R = 'r';
  /** 特殊字符--> {@code \t} */
  public static final char SC_T = 't';
  /** 单引号 */
  public static final String S_QUOTE = "'";
  /** 双引号 */
  public static final String S_QUOTES = "\"";
  /** true */
  public static final boolean TRUE = true;
  /** 大写HEX */
  public static final char[] UPPER_HEX =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
  /** 小写HEX */
  public static final char[] LOWER_HEX =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  /** An empty immutable {@code Object} array. */
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  /** An empty immutable {@code Class} array. */
  public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
  /** An empty immutable {@code String} array. */
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  /** An empty immutable {@code byte} array. */
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  /** An empty immutable {@code Byte} array. */
  public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
  /** An empty immutable {@code short} array. */
  public static final short[] EMPTY_SHORT_ARRAY = new short[0];
  /** An empty immutable {@code Short} array. */
  public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
  /** An empty immutable {@code int} array. */
  public static final int[] EMPTY_INT_ARRAY = new int[0];
  /** An empty immutable {@code Integer} array. */
  public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
  /** An empty immutable {@code long} array. */
  public static final long[] EMPTY_LONG_ARRAY = new long[0];
  /** An empty immutable {@code Long} array. */
  public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
  /** An empty immutable {@code float} array. */
  public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
  /** An empty immutable {@code Float} array. */
  public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
  /** An empty immutable {@code double} array. */
  public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
  /** An empty immutable {@code Double} array. */
  public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
  /** An empty immutable {@code boolean} array. */
  public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
  /** An empty immutable {@code Boolean} array. */
  public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
  /** An empty immutable {@code char} array. */
  public static final char[] EMPTY_CHAR_ARRAY = new char[0];
  /** An empty immutable {@code Character} array. */
  public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
  /** 12小时制和24小时制 */
  public static final Pattern TIME =
      Pattern.compile("([aApP][mM] (0[1-9]|1[0-2])|([0-1]\\d|2[0-3]))(:[0-5]\\d)?(:[0-5]\\d)?");

  //////////////////////////////////////////////////////////////////////////////////////////
  // 正则表达式
  //////////////////////////////////////////////////////////////////////////////////////////
  /** 时间范围 */
  public static final Pattern TIME_RANGE =
      Pattern.compile(
          "(([aApP][mM] (0[1-9]|1[0-2])|([0-1]\\d|2[0-3]))(:[0-5]\\d)?(:[0-5]\\d)?)?-(([aApP][mM] (0[1-9]|1[0-2])|([0-1]\\d|2[0-3]))(:[0-5]\\d)?(:[0-5]\\d)?)?");
  /** 手机号码 */
  public static final Pattern PHONE =
      Pattern.compile(
          "(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})");
  /** 邮箱正则表达式 */
  public static final Pattern EMAIL_PATTERN =
      Pattern.compile("(\\w|-)+(\\.(\\w|-)+)*@(\\w|-)+(\\.(\\w|-)+)*");
  /** 文件目录名正则表达式 */
  public static final Pattern FILE_DIR_NAME_PATTERN = Pattern.compile("[^:<>\"/^|*?\\\\]+");
  /** 整数 */
  public static final Pattern NUM_INTEGER_PATTERN = Pattern.compile("^[\\-+]?(\\d+)$");
  /** 小数 */
  public static final Pattern NUM_DECIMAL_PATTERN = Pattern.compile("^[\\-+]?(\\d+)(\\.\\d+)?$");
  /** 数字十进制 */
  public static final Pattern NUM_DEC_PATTERN = Pattern.compile("^\\d+$");
  /** 数字八进制 */
  public static final Pattern NUM_OCT_PATTERN = Pattern.compile("^0[0-7]+$");
  /** 数字十六进制 */
  public static final Pattern NUM_HEX_PATTERN = Pattern.compile("^0[xX][\\dA-Fa-f]+$");
  /** 十六进制 */
  public static final Pattern HEX_PATTERN = Pattern.compile("^[\\dA-Fa-f]+$");
  /** 十六进制端口号正则表达式 */
  public static final Pattern PORT_HEX_PATTERN = Pattern.compile("0[xX][0-9A-Fa-f]{0,4}");
  /** ipv4正则表达式 */
  public static final Pattern IPV4_PATTERN =
      Pattern.compile("(25[0-5]|(2[0-4]|1\\d|[1-9]?)\\d)(\\.(25[0-5]|(2[0-4]|1\\d|[1-9]?)\\d)){3}");
  /** 端口号正则表达式 */
  public static final Pattern PORT_PATTERN =
      Pattern.compile(
          "0|6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}");
  /** 有效字符 */
  public static final Pattern VALID_CHARACTER_PATTERN =
      Pattern.compile("[0-9A-Za-z`~!@#%&-_=;:'\",<>/$()*+.\\[\\]?^{}\\\\]+");
  /** set前缀 */
  public static final Pattern SET_PREFIX_PATTERN = Pattern.compile("^set.*");
  /** TOKEN标识 */
  public static final Pattern TOKEN_PATTERN = Pattern.compile("^[a-zA-Z_$](\\w|\\$)*");
  /** 占位符表达式 */
  public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{[^{}$]+}");
  /** 正则表达式特殊字符 */
  public static final String ESCAPE_REGEX =
      "(\\^)|(\\$)|(\\\\)|(\\.)|(\\+)|(\\*)|(\\?)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\|)";
  /** 正则表达式特殊字符 */
  public static final Pattern ESCAPE_REGEX_PATTERN = Pattern.compile(ESCAPE_REGEX);
  /** The System property key for the user home directory. */
  public static final String USER_HOME_KEY = "user.home";
  //////////////////////////////////////////////////////////////////////////////////////////
  // 系统常量
  //////////////////////////////////////////////////////////////////////////////////////////
  /** The System property key for the user directory. */
  public static final String USER_DIR_KEY = "user.dir";
  /** The System property key for the Java IO temporary directory. */
  public static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
  /** The System property key for the Java home directory. */
  public static final String JAVA_HOME_KEY = "java.home";

  private static final Logger LOGGER = LoggerFactory.getLogger(Tools.class);
  // These MUST be declared first. Other constants depend on this.
  /** IO 缓存大小 */
  public static final int IO_CACHE_SIZE =
      getSystemProperty("helper.toolkit", 8192, Integer::parseInt);
  /**
   * The {@code awt.toolkit} System Property.
   *
   * <p>Holds a class name, on Windows XP this is {@code sun.awt.windows.WToolkit}.
   *
   * <p><b>On platforms without a GUI, this value is {@code null}.</b>
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.1
   */
  public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
  /**
   * The {@code file.encoding} System Property.
   *
   * <p>File encoding, such as {@code Cp1252}.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.0
   * @since Java 1.2
   */
  public static final String FILE_ENCODING = getSystemProperty("file.encoding");
  /**
   * The {@code file.separator} System Property. The file separator is:
   *
   * <ul>
   *   <li>{@code "/"}</code> on UNIX
   *   <li>{@code "\"}</code> on Windows.
   * </ul>
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
  /**
   * The {@code java.awt.fonts} System Property.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.1
   */
  public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
  /**
   * The {@code java.awt.graphicsenv} System Property.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.1
   */
  public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
  /**
   * The {@code java.awt.headless} System Property. The value of this property is the String {@code
   * "true"} or {@code "false"}.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @see #isJavaAwtHeadless()
   * @since 2.1
   * @since Java 1.4
   */
  public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
  /**
   * The {@code java.awt.printerjob} System Property.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.1
   */
  public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
  /**
   * The {@code java.class.path} System Property. Java class path.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
  /**
   * The {@code java.class.version} System Property. Java class format version number.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
  /**
   * The {@code java.compiler} System Property. Name of JIT compiler to use. First in JDK version
   * 1.2. Not used in Sun JDKs after 1.2.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2. Not used in Sun versions after 1.2.
   */
  public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
  /**
   * The {@code java.endorsed.dirs} System Property. Path of endorsed directory or directories.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.4
   */
  public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
  /**
   * The {@code java.ext.dirs} System Property. Path of extension directory or directories.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.3
   */
  public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
  /**
   * The {@code java.library.path} System Property. List of paths to search when loading libraries.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
  /**
   * The {@code java.runtime.name} System Property. Java Runtime Environment name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.0
   * @since Java 1.3
   */
  public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
  /**
   * The {@code java.runtime.version} System Property. Java Runtime Environment version.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.0
   * @since Java 1.3
   */
  public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
  /**
   * The {@code java.specification.name} System Property. Java Runtime Environment specification
   * name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
  /**
   * The {@code java.specification.vendor} System Property. Java Runtime Environment specification
   * vendor.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_SPECIFICATION_VENDOR =
      getSystemProperty("java.specification.vendor");
  /**
   * The {@code java.specification.version} System Property. Java Runtime Environment specification
   * version.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.3
   */
  public static final String JAVA_SPECIFICATION_VERSION =
      getSystemProperty("java.specification.version");
  /**
   * Is {@code true} if this is Java version 1.1 (also 1.1.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   */
  public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");
  /**
   * Is {@code true} if this is Java version 1.2 (also 1.2.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   */
  public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");
  /**
   * Is {@code true} if this is Java version 1.3 (also 1.3.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   */
  public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");
  /**
   * Is {@code true} if this is Java version 1.4 (also 1.4.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   */
  public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");
  /**
   * Is {@code true} if this is Java version 1.5 (also 1.5.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   */
  public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
  /**
   * Is {@code true} if this is Java version 1.6 (also 1.6.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   */
  public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
  /**
   * Is {@code true} if this is Java version 1.7 (also 1.7.x versions).
   *
   * <p>The field will return {@code false} if {@link #JAVA_VERSION} is {@code null}.
   *
   * @since 3.0
   */
  public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
  /**
   * @since Java 1.1
   */
  private static final JavaVersion JAVA_SPECIFICATION_VERSION_AS_ENUM =
      JavaVersion.get(JAVA_SPECIFICATION_VERSION);
  /**
   * The {@code java.util.prefs.PreferencesFactory} System Property. A class name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.1
   * @since Java 1.4
   */
  public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY =
      getSystemProperty("java.util.prefs.PreferencesFactory");
  /**
   * The {@code java.vendor} System Property. Java vendor-specific string.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
  /**
   * The {@code java.vendor.url} System Property. Java vendor URL.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
  /**
   * The {@code java.version} System Property. Java version number.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String JAVA_VERSION = getSystemProperty("java.version");
  /**
   * The {@code java.vm.info} System Property. Java Virtual Machine implementation info.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.0
   * @since Java 1.2
   */
  public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
  /**
   * The {@code java.vm.name} System Property. Java Virtual Machine implementation name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
  /**
   * The {@code java.vm.specification.name} System Property. Java Virtual Machine specification
   * name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_VM_SPECIFICATION_NAME =
      getSystemProperty("java.vm.specification.name");
  /**
   * The {@code java.vm.specification.vendor} System Property. Java Virtual Machine specification
   * vendor.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_VM_SPECIFICATION_VENDOR =
      getSystemProperty("java.vm.specification.vendor");
  /**
   * The {@code java.vm.specification.version} System Property. Java Virtual Machine specification
   * version.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_VM_SPECIFICATION_VERSION =
      getSystemProperty("java.vm.specification.version");
  /**
   * The {@code java.vm.vendor} System Property. Java Virtual Machine implementation vendor.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
  /**
   * The {@code java.vm.version} System Property. Java Virtual Machine implementation version.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");
  /**
   * The {@code line.separator} System Property. Line separator (<code>&quot;\n&quot;</code> on
   * UNIX).
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
  /**
   * The {@code os.arch} System Property. Operating system architecture.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String OS_ARCH = getSystemProperty("os.arch");
  /**
   * The {@code os.name} System Property. Operating system name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String OS_NAME = getSystemProperty("os.name");
  /**
   * Is {@code true} if this is AIX.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_AIX = getOSMatchesName("AIX");
  /**
   * Is {@code true} if this is HP-UX.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_HP_UX = getOSMatchesName("HP-UX");
  /**
   * Is {@code true} if this is Irix.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_IRIX = getOSMatchesName("Irix");
  /**
   * Is {@code true} if this is Linux.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_LINUX = getOSMatchesName("Linux") || getOSMatchesName("LINUX");
  /**
   * Is {@code true} if this is Mac.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_MAC = getOSMatchesName("Mac");
  /**
   * Is {@code true} if this is Mac.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_MAC_OSX = getOSMatchesName("Mac OS X");
  /**
   * Is {@code true} if this is FreeBSD.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.1
   */
  public static final boolean IS_OS_FREE_BSD = getOSMatchesName("FreeBSD");
  /**
   * Is {@code true} if this is OpenBSD.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.1
   */
  public static final boolean IS_OS_OPEN_BSD = getOSMatchesName("OpenBSD");
  /**
   * Is {@code true} if this is NetBSD.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.1
   */
  public static final boolean IS_OS_NET_BSD = getOSMatchesName("NetBSD");
  /**
   * Is {@code true} if this is OS/2.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_OS2 = getOSMatchesName("OS/2");
  /**
   * Is {@code true} if this is Solaris.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_SOLARIS = getOSMatchesName("Solaris");
  /**
   * Is {@code true} if this is SunOS.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_SUN_OS = getOSMatchesName("SunOS");
  /**
   * Is {@code true} if this is a UNIX like system, as in any of AIX, HP-UX, Irix, Linux, MacOSX,
   * Solaris or SUN OS.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.1
   */
  public static final boolean IS_OS_UNIX =
      IS_OS_AIX
          || IS_OS_HP_UX
          || IS_OS_IRIX
          || IS_OS_LINUX
          || IS_OS_MAC_OSX
          || IS_OS_SOLARIS
          || IS_OS_SUN_OS
          || IS_OS_FREE_BSD
          || IS_OS_OPEN_BSD
          || IS_OS_NET_BSD;
  /**
   * The {@code os.version} System Property. Operating system version.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String OS_VERSION = getSystemProperty("os.version");
  /**
   * The {@code path.separator} System Property. Path separator (<code>&quot;:&quot;</code> on
   * UNIX).
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
  /**
   * The {@code user.country} or {@code user.region} System Property. User's country code, such as
   * {@code GB}. First in Java version 1.2 as {@code user.region}. Renamed to {@code user.country}
   * in 1.4
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.0
   * @since Java 1.2
   */
  public static final String USER_COUNTRY =
      getSystemProperty("user.country") == null
          ? getSystemProperty("user.region")
          : getSystemProperty("user.country");
  /**
   * The {@code user.language} System Property. User's language code, such as {@code "en"}.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.0
   * @since Java 1.2
   */
  public static final String USER_LANGUAGE = getSystemProperty("user.language");
  /**
   * The {@code user.name} System Property. User's account name.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String USER_NAME = getSystemProperty("user.name");
  /**
   * The {@code user.timezone} System Property. For example: {@code "America/Los_Angeles"}.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since 2.1
   */
  public static final String USER_TIMEZONE = getSystemProperty("user.timezone");
  /**
   * The {@code user.home} System Property. User's home directory.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);
  /**
   * The {@code user.dir} System Property. User's current working directory.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);
  /**
   * The {@code java.io.tmpdir} System Property. Default temp file path.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.2
   */
  public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);
  /**
   * The {@code java.home} System Property. Java installation directory.
   *
   * <p>Defaults to {@code null} if the runtime does not have security access to read this property
   * or the property does not exist.
   *
   * <p>This value is initialized when the class is loaded. If {@link System#setProperty(String,
   * String)} or {@link System#setProperties(java.util.Properties)} is called after this class is
   * loaded, the value will be out of sync with that System property.
   *
   * @since Java 1.1
   */
  public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);
  /** The prefix String for all Windows OS. */
  private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
  /**
   * Is {@code true} if this is Windows.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS = getOSMatchesName(OS_NAME_WINDOWS_PREFIX);
  /**
   * Is {@code true} if this is Windows 2000.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS_2000 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.0");
  /**
   * Is {@code true} if this is Windows 2003.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.1
   */
  public static final boolean IS_OS_WINDOWS_2003 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.2");
  /**
   * Is {@code true} if this is Windows 2008.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.1
   */
  public static final boolean IS_OS_WINDOWS_2008 =
      getOSMatches(OS_NAME_WINDOWS_PREFIX + " Server 2008", "6.1");
  /**
   * Is {@code true} if this is Windows 95.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS_95 = getOSMatches(OS_NAME_WINDOWS_PREFIX + " 9", "4.0");
  /**
   * Is {@code true} if this is Windows 98.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS_98 = getOSMatches(OS_NAME_WINDOWS_PREFIX + " 9", "4.1");
  /**
   * Is {@code true} if this is Windows ME.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS_ME = getOSMatches(OS_NAME_WINDOWS_PREFIX, "4.9");
  /**
   * Is {@code true} if this is Windows NT.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS_NT = getOSMatchesName(OS_NAME_WINDOWS_PREFIX + " NT");
  /**
   * Is {@code true} if this is Windows XP.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.0
   */
  public static final boolean IS_OS_WINDOWS_XP = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.1");
  /**
   * Is {@code true} if this is Windows Vista.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 2.4
   */
  public static final boolean IS_OS_WINDOWS_VISTA = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.0");
  /**
   * Is {@code true} if this is Windows 7.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.0
   */
  public static final boolean IS_OS_WINDOWS_7 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.1");
  /**
   * Is {@code true} if this is Windows 8.
   *
   * <p>The field will return {@code false} if {@code OS_NAME} is {@code null}.
   *
   * @since 3.2
   */
  public static final boolean IS_OS_WINDOWS_8 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.2");
  /** 正则表达式缓存 */
  private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

  private Tools() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 替换占位符
   *
   * @param template 模板
   * @param params 参数
   * @return 返回处理后的值
   */
  public static String replacePlaceholder(String template, Map<String, String> params) {
    return replacePlaceholder(template, params::get);
  }
  /**
   * 替换占位符
   *
   * @param template 模板
   * @param paramSupplier 参数
   * @return 返回处理后的值
   */
  public static String replacePlaceholder(String template, Function<String, String> paramSupplier) {
    StringBuffer sb = new StringBuffer();
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
    while (matcher.find()) {
      String ph = matcher.group();
      String name = ph.substring(2, ph.length() - 1);
      String value = paramSupplier.apply(name);
      if (value != null) {
        matcher.appendReplacement(sb, value);
      }
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  /**
   * Gets the Java home directory as a {@code File}.
   *
   * @return a directory
   * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
   *     method doesn't allow access to the specified system property.
   * @see System#getProperty(String)
   * @since 2.1
   */
  public static File getJavaHome() {
    return new File(System.getProperty(JAVA_HOME_KEY));
  }

  /**
   * Gets the Java IO temporary directory as a {@code File}.
   *
   * @return a directory
   * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
   *     method doesn't allow access to the specified system property.
   * @see System#getProperty(String)
   * @since 2.1
   */
  public static File getJavaIoTmpDir() {
    return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
  }

  /**
   * Decides if the Java version matches.
   *
   * @param versionPrefix the prefix for the java version
   * @return true if matches, or false if not or can't determine
   */
  private static boolean getJavaVersionMatches(final String versionPrefix) {
    return isJavaVersionMatch(JAVA_SPECIFICATION_VERSION, versionPrefix);
  }

  /**
   * Decides if the operating system matches.
   *
   * @param osNamePrefix the prefix for the os name
   * @param osVersionPrefix the prefix for the version
   * @return true if matches, or false if not or can't determine
   */
  private static boolean getOSMatches(final String osNamePrefix, final String osVersionPrefix) {
    return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
  }

  /**
   * Decides if the operating system matches.
   *
   * @param osNamePrefix the prefix for the os name
   * @return true if matches, or false if not or can't determine
   */
  private static boolean getOSMatchesName(final String osNamePrefix) {
    return isOSNameMatch(OS_NAME, osNamePrefix);
  }

  /**
   * Gets a System property, defaulting to {@code null} if the property cannot be read.
   *
   * <p>If a {@code SecurityException} is caught, the return value is {@code null} and a message is
   * written to {@code System.err}.
   *
   * @param property the system property name
   * @return the system property value or {@code null} if a security problem occurs
   */
  private static String getSystemProperty(String property) {
    return getSystemProperty(property, NULL_STRING, s -> s);
  }

  /**
   * Gets a System propertyName, defaulting to {@code null} if the propertyName cannot be read.
   *
   * <p>If a {@code SecurityException} is caught, the return value is {@code null} and a message is
   * written to {@code System.err}.
   *
   * @param propertyName the system propertyName name
   * @return the system propertyName value or {@code null} if a security problem occurs
   */
  @Nonnull
  private static <T> T getSystemProperty(
      String propertyName, T defaultValue, Function<String, T> convert) {
    try {
      String propertyValue = System.getProperty(propertyName);
      if (propertyValue != null && convert != null) {
        try {
          T result = convert.apply(propertyValue);
          if (result != null) {
            return result;
          }
        } catch (Throwable e) {
          if (LOGGER.isErrorEnabled()) {
            LOGGER.error(
                "Caught a Exception reading the system propertyValue name ["
                    + propertyName
                    + "] and the propertyValue value ["
                    + propertyValue
                    + "]",
                e);
          }
        }
      }
    } catch (final SecurityException ex) {
      // we are not allowed to look at this propertyName
      LOGGER.error(
          "Caught a SecurityException reading the system property name ["
              + propertyName
              + "]; the SystemUtils propertyName value will default to null.",
          ex);
    }
    return defaultValue;
  }

  /**
   * Gets the user directory as a {@code File}.
   *
   * @return a directory
   * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
   *     method doesn't allow access to the specified system property.
   * @see System#getProperty(String)
   * @since 2.1
   */
  public static File getUserDir() {
    return new File(USER_DIR);
  }

  /**
   * Gets the user home directory as a {@code File}.
   *
   * @return a directory
   * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess}
   *     method doesn't allow access to the specified system property.
   * @see System#getProperty(String)
   * @since 2.1
   */
  public static File getUserHome() {
    return new File(USER_HOME);
  }

  /**
   * Returns whether the {@link #JAVA_AWT_HEADLESS} value is {@code true}.
   *
   * @return {@code true} if {@code JAVA_AWT_HEADLESS} is {@code "true"}, {@code false} otherwise.
   * @see #JAVA_AWT_HEADLESS
   * @since 2.1
   * @since Java 1.4
   */
  public static boolean isJavaAwtHeadless() {
    return JAVA_AWT_HEADLESS != null && JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString());
  }

  /**
   * Is the Java version at least the requested version.
   *
   * <p>Example input:
   *
   * <ul>
   *   <li>{@code 1.2f} to test for Java 1.2
   *   <li>{@code 1.31f} to test for Java 1.3.1
   * </ul>
   *
   * @param requiredVersion the required version, for example 1.31f
   * @return {@code true} if the actual version is equal or greater than the required version
   */
  public static boolean isJavaVersionAtLeast(final JavaVersion requiredVersion) {
    return JAVA_SPECIFICATION_VERSION_AS_ENUM.atLeast(requiredVersion);
  }

  /**
   * Decides if the Java version matches.
   *
   * <p>This method is package private instead of private to support unit test invocation.
   *
   * @param version the actual Java version
   * @param versionPrefix the prefix for the expected Java version
   * @return true if matches, or false if not or can't determine
   */
  private static boolean isJavaVersionMatch(final String version, final String versionPrefix) {
    if (version == null) {
      return false;
    }
    return version.startsWith(versionPrefix);
  }

  /**
   * Decides if the operating system matches.
   *
   * <p>This method is package private instead of private to support unit test invocation.
   *
   * @param osName the actual OS name
   * @param osVersion the actual OS version
   * @param osNamePrefix the prefix for the expected OS name
   * @param osVersionPrefix the prefix for the expected OS version
   * @return true if matches, or false if not or can't determine
   */
  private static boolean isOSMatch(
      final String osName,
      final String osVersion,
      final String osNamePrefix,
      final String osVersionPrefix) {
    if (osName == null || osVersion == null) {
      return false;
    }
    return osName.startsWith(osNamePrefix) && osVersion.startsWith(osVersionPrefix);
  }

  /**
   * Decides if the operating system matches.
   *
   * <p>This method is package private instead of private to support unit test invocation.
   *
   * @param osName the actual OS name
   * @param osNamePrefix the prefix for the expected OS name
   * @return true if matches, or false if not or can't determine
   */
  private static boolean isOSNameMatch(final String osName, final String osNamePrefix) {
    if (osName == null) {
      return false;
    }
    return osName.startsWith(osNamePrefix);
  }

  /**
   * 获得服务器的IP地址
   *
   * @return ip地址
   */
  public static String localIp() {
    String ipStr = "";
    InetAddress ip = null;
    try {
      boolean isFindIp = false;
      Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
      while (netInterfaces.hasMoreElements()) {
        if (isFindIp) {
          break;
        }
        NetworkInterface ni = netInterfaces.nextElement();
        Enumeration<InetAddress> ips = ni.getInetAddresses();
        while (ips.hasMoreElements()) {
          ip = ips.nextElement();
          if (!ip.isLoopbackAddress() && IPV4_PATTERN.matcher(ip.getHostAddress()).matches()) {
            isFindIp = true;
            break;
          }
        }
      }
    } catch (Exception ignored) {
    }
    if (null != ip) {
      ipStr = ip.getHostAddress();
    }
    return ipStr;
  }

  /**
   * 获得服务器的IP地址(多网卡)
   *
   * @return ip地址
   */
  public static List<String> localIps() {
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
   * 获取系统行分割符
   *
   * @return 行风格符
   */
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
    return Whether.windowsSystem() ? '\\' : '/';
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

  /**
   * 获取jvm总计内存
   *
   * @return jvm总计内存
   */
  public static long jvmTotalMemory() {
    return Runtime.getRuntime().totalMemory();
  }

  /**
   * 获取jvm最大内存
   *
   * @return jvm最大内存
   */
  public static long jvmMaxMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  /**
   * 获取jvm空闲内存
   *
   * @return jvm空闲内存
   */
  public static long jvmFreeMemory() {
    return Runtime.getRuntime().freeMemory();
  }

  /**
   * 获取年正则表达式
   *
   * @param year 年分割符号
   * @return Pattern
   */
  public static Pattern getYearPattern(String year) {
    String s = "([1-9]\\d{0,3})${year}";
    String r = replacePlaceholder(s, HelperOnCollection.ofMap("year", year));
    return getOrCreatePattern(r);
  }

  /**
   * 获取月正则表达式
   *
   * @param month 月分割符号
   * @return Pattern
   */
  public static Pattern getMonthPattern(String month) {
    String s = "(1[012]|0?[1-9])${month}";
    String r = replacePlaceholder(s, HelperOnCollection.ofMap("month", month));
    return getOrCreatePattern(r);
  }

  /**
   * 获取月日正则表达式
   *
   * @param month 月分割符号
   * @param day 日分割符号
   * @return Pattern
   */
  public static Pattern getMonthDayPattern(String month, String day) {
    String s =
        "((1[02]|0?[13578])${month}([12]\\d|3[01]|0[1-9])${day})|((11|0?[469])${month}([12]\\d|30|0[1-9])${day})|(0?2${month}(1\\d|2[0-9]|0[1-9])${day})";
    String r = replacePlaceholder(s, HelperOnCollection.ofMap("month", month, "day", day));
    return getOrCreatePattern(r);
  }

  /**
   * 获取年月正则表达式
   *
   * @param year 年分割符号
   * @param month 月分割符号
   * @return Pattern
   */
  public static Pattern getYearMonthPattern(String year, String month) {
    String s = "[1-9]\\d{3}${year}(1[012]|0?[1-9])${month}";
    String r = replacePlaceholder(s, HelperOnCollection.ofMap("year", year, "month", month));
    return getOrCreatePattern(r);
  }

  /**
   * 获取年月日正则表达式
   *
   * @param year 年分割符号
   * @param month 月分割符号
   * @param day 日分割符号
   * @return Pattern
   */
  public static Pattern getYearMonthDayPattern(String year, String month, String day) {

    String leapMonthDayTemplate =
        "(((1[02]|0[13578])${month}([12]\\d|3[01]|0[1-9])${day})|((11|0[469])${month}([12]\\d|30|0[1-9])${day})|(02${month}(1\\d|2[0-9]|0[1-9])${day}))";
    String monthDayTemplate =
        "(((1[02]|0[13578])${month}([12]\\d|3[01]|0[1-9])${day})|((11|0[469])${month}([12]\\d|30|0[1-9])${day})|(02${month}(1\\d|2[0-8]|0[1-9])${day}))";

    String lx = "[48]";
    String lxx = "[2468][048]|[13579][26]";
    String lxxx = "[1235679](0[48]|[2468][048]|[13579][26])|[48]([02468][048]|[13579][26])";
    String lxxxx =
        "([2468][048]|[13579][26])([02468][048]|[13579][26])|([2468][1235679]|[13579][01345789])(0[48]|[2468][048]|[13579][26])";
    String leapYearTemplate = "((" + lxxxx + ")|(" + lxxx + ")|(" + lxx + ")|" + lx + ")${year}";

    String nx = "[1235679]";
    String nxx = "[2468][1235679]|[13579][01345789]";
    String nxxx =
        "[1235679](0[01235679]|[2468][1235679]|[13579][01345789])|[48]([02468][1235679]|[13579][01345789])";
    String nxxxx =
        "([2468][048]|[13579][26])([02468][1235679]|[13579][01345789])|([2468][1235679]|[13579][01345789])([02468][01235679]|[13579][01345789])";
    String yearTemplate = "((" + nxxxx + ")|(" + nxxx + ")|(" + nxx + ")|" + nx + ")${year}";

    String stringBuilder =
        "("
            + leapYearTemplate
            + "|"
            + yearTemplate
            + ")("
            + leapMonthDayTemplate
            + "|"
            + monthDayTemplate
            + ")";

    String regexp =
        replacePlaceholder(
            stringBuilder, HelperOnCollection.ofMap("year", year, "month", month, "day", day));
    return getOrCreatePattern(regexp);
  }

  /**
   * @param input
   * @return
   */
  public static String escapeRegExp(String input) {
    return ESCAPE_REGEX_PATTERN.matcher(input).replaceAll("\\\\$0");
  }

  /**
   * 要求匹配正则表达式
   *
   * @param pattern 正则表达式模式
   * @param string 输入字符串
   * @param isThrow 是否抛出异常
   * @return 如果匹配返回字符串，否则抛出异常
   */
  @Nullable
  public static String requireMatchPattern(
      @Nonnull final Pattern pattern, @Nullable final String string, final boolean isThrow) {
    if (isMatchPattern(pattern, string)) {
      return string;
    }
    if (isThrow) {
      throw new IllegalArgumentException(
          "The [" + string + "] does not match the regular expression [" + pattern.pattern() + "]");
    }
    return null;
  }

  /**
   * 是否匹配正则表达式
   *
   * @param pattern 正则表达式模式
   * @param string 输入字符串
   * @return 如果匹配返回true，否则返回false
   */
  public static boolean isMatchPattern(
      @Nonnull final Pattern pattern, @Nullable final String string) {
    return string != null && pattern.matcher(string).matches();
  }

  /**
   * Convert the given array (which may be a primitive array) to an object array (if necessary of
   * primitive wrapper objects).
   *
   * <p>A {@code null} source value will be converted to an empty Object array.
   *
   * @param source the (potentially primitive) array
   * @return the corresponding object array (never {@code null})
   * @throws IllegalArgumentException if the parameter is not an array
   */
  public static Object[] toObjectArray(Object source) {
    if (source instanceof Object[]) {
      return (Object[]) source;
    }
    if (source == null) {
      return new Object[0];
    }
    if (!source.getClass().isArray()) {
      throw new IllegalArgumentException("Source is not an array: " + source);
    }
    int length = Array.getLength(source);
    if (length == 0) {
      return new Object[0];
    }
    Class<?> wrapperType = Array.get(source, 0).getClass();
    Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
    for (int i = 0; i < length; i++) {
      newArray[i] = Array.get(source, i);
    }
    return newArray;
  }

  /**
   * 获取或创建正则表达式
   *
   * @param regexp 正则表达式
   * @return Pattern
   */
  public static Pattern getOrCreatePattern(String regexp) {
    Pattern pattern = PATTERN_CACHE.get(regexp);
    if (pattern == null) {
      pattern = Pattern.compile(regexp);
      PATTERN_CACHE.put(regexp, pattern);
    }
    return pattern;
  }

  /**
   * 清理路径，格式{@code /a/b/c}或{@code a/b/c}或{@code /a/b/c/}或{@code a/b/c/}
   *
   * <p>{@code isPathSplitEnd==true} 则会在路径尾部添加{@code /} {@code isPathSplitEnd==false}
   * 则会在路径尾部去除{@code /}
   *
   * <p>{@code isPathSplitStart==true} {@code file://D:/a/b/c}--->{@code D:/a/b/c/} {@code
   * file:/a/b/c/}--->{@code /a/b/c/} {@code a/b/c}--->{@code /a/b/c/} {@code a/b/c}--->{@code
   * /a/b/c/}
   *
   * <p>{@code isPathSplitStart==false} {@code file://D:/a/b/c}--->{@code D:/a/b/c/} {@code
   * file:/a/b/c/}--->{@code a/b/c/} {@code a/b/c}--->{@code a/b/c/} {@code a/b/c}--->{@codea
   * a/b/c/}
   *
   * @param isPathSplitStart 是否为路径分割符开始
   * @param isPathSplitEnd 是否为路径分割符结尾
   * @param paths 路径
   * @return 返回合法化文件路径
   */
  public static String safePath(boolean isPathSplitStart, boolean isPathSplitEnd, String... paths) {
    char pathSplit = '/';
    if (paths == null || paths.length == 0) {
      return isPathSplitEnd || isPathSplitStart ? String.valueOf(pathSplit) : "";
    }
    StringBuilder sb = new StringBuilder(256);
    int i = 0;
    while (i < paths.length) {
      if (paths[i] != null && paths[i].length() != 0) {
        break;
      }
      i++;
    }
    if (i >= paths.length) {
      return isPathSplitEnd || isPathSplitStart ? String.valueOf(pathSplit) : "";
    }

    StringTokenizer stringTokenizer = new StringTokenizer(paths[i++], "\\/");

    if (!isPathSplitStart && stringTokenizer.hasMoreTokens()) {
      sb.append(stringTokenizer.nextToken());
    }
    while (stringTokenizer.hasMoreTokens()) {
      sb.append(pathSplit).append(stringTokenizer.nextToken());
    }

    while (i < paths.length) {
      if (paths[i] == null || paths[i].length() == 0) {
        continue;
      }
      stringTokenizer = new StringTokenizer(paths[i++], "\\/");
      while (stringTokenizer.hasMoreTokens()) {
        sb.append(pathSplit).append(stringTokenizer.nextToken());
      }
    }
    if (isPathSplitEnd) {
      sb.append(pathSplit);
    }
    return sb.toString();
  }

  /**
   * 安全获取值，不会抛出异常
   *
   * @param instance 实例对象
   * @param getter 获取器
   * @param <T> 实例类型
   * @param <R> 返回类型
   * @return 返回值，失败返回null
   */
  @Nonnull
  public static <T, R> R safeGetOrDefault(
      @Nullable final T instance,
      @Nullable final T defaultInstance,
      @Nonnull final FactoryThrow<T, R, Throwable> getter,
      @Nonnull final R defaultValue) {
    R result = safeGet(instance, getter);
    if (result == null && instance != defaultInstance) {
      result = safeGet(defaultInstance, getter);
    }
    if (result == null) {
      result = defaultValue;
    }
    return result;
  }

  /**
   * 安全获取值，不会抛出异常
   *
   * @param instance 实例对象
   * @param getter 获取器
   * @param <T> 实例类型
   * @param <R> 返回类型
   * @return 返回值，失败返回null
   */
  @Nonnull
  public static <T, R> R safeGetOrDefault(
      @Nullable final T instance,
      @Nonnull final FactoryThrow<T, R, Throwable> getter,
      @Nonnull final R defaultValue) {
    R result = null;
    if (instance != null) {
      try {
        result = getter.handle(instance);
      } catch (Throwable ignored) {
      }
    }
    if (result == null) {
      result = defaultValue;
    }
    return result;
  }

  /**
   * 安全获取值，不会抛出异常
   *
   * @param instance 实例对象
   * @param getter 获取器
   * @param <T> 实例类型
   * @param <R> 返回类型
   * @return 返回值，失败返回null
   */
  @Nullable
  public static <T, R> R safeGet(
      @Nullable final T instance,
      @Nullable final T defaultInstance,
      @Nonnull final FactoryThrow<T, R, Throwable> getter) {
    R r = safeGet(instance, getter);
    if (r == null && instance != defaultInstance) {
      return safeGet(defaultInstance, getter);
    }
    return r;
  }

  /**
   * 安全获取值，不会抛出异常
   *
   * @param instance 实例对象
   * @param getter 获取器
   * @param <T> 实例类型
   * @param <R> 返回类型
   * @return 返回值，失败返回null
   */
  @Nullable
  public static <T, R> R safeGet(
      @Nullable final T instance, @Nonnull final FactoryThrow<T, R, Throwable> getter) {
    if (instance != null) {
      try {
        return getter.handle(instance);
      } catch (Throwable ignored) {
      }
    }
    return null;
  }

  public static String safeValue(String input) {
    return input == null ? EMPTY_STRING : input;
  }

  public static <T> T safeValue(T input, T defaultValue) {
    return input == null ? defaultValue : input;
  }

  public static <K, V> Map<K, V> safeValue(Map<K, V> input) {
    return input == null ? Collections.emptyMap() : input;
  }

  public static <T> List<T> safeValue(List<T> input) {
    return input == null ? Collections.emptyList() : input;
  }

  public static <T> Set<T> safeValue(Set<T> input) {
    return input == null ? Collections.emptySet() : input;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static boolean safeAt(boolean[] arr, int index, boolean defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static byte safeAt(byte[] arr, int index, byte defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static char safeAt(char[] arr, int index, char defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static short safeAt(short[] arr, int index, short defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static int safeAt(int[] arr, int index, int defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static long safeAt(long[] arr, int index, long defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static float safeAt(float[] arr, int index, float defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回默认值
   *
   * @param arr 数组
   * @param index 索引
   * @param defaultValue 默认值
   * @return 获取数组有效值，如果不有效，则返回默认值
   */
  public static double safeAt(double[] arr, int index, double defaultValue) {
    return arr.length > index && index >= 0 ? arr[index] : defaultValue;
  }

  /**
   * 获取数组有效值，如果不有效，则返回空字符串
   *
   * @param arr 数组
   * @param index 索引
   * @return 获取数组有效值，如果不有效，则返回空字符串
   */
  public static String safeAt(String[] arr, int index) {
    return arr.length > index && index >= 0 ? arr[index] : "";
  }

  /**
   * 获取数组有效值，如果不有效，则返回null
   *
   * @param arr 数组
   * @param index 索引
   * @return 获取数组有效值，如果不有效，则返回null
   */
  public static <E> E safeAt(E[] arr, int index) {
    return arr != null && arr.length > index && index >= 0 ? arr[index] : null;
  }

  /**
   * 如果值大于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于目标值返回原值，否则返回默认值
   */
  public static byte safeGt(Byte value, byte target, byte elseValue) {
    return value == null ? elseValue : (value > target ? value : elseValue);
  }

  /**
   * 如果值大于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于目标值返回原值，否则返回默认值
   */
  public static short safeGt(Short value, short target, short elseValue) {
    return value == null ? elseValue : (value > target ? value : elseValue);
  }

  /**
   * 如果值大于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于目标值返回原值，否则返回默认值
   */
  public static int safeGt(Integer value, int target, int elseValue) {
    return value == null ? elseValue : (value > target ? value : elseValue);
  }

  /**
   * 如果值大于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于目标值返回原值，否则返回默认值
   */
  public static long safeGt(Long value, long target, long elseValue) {
    return value == null ? elseValue : (value > target ? value : elseValue);
  }

  /**
   * 如果值大于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于目标值返回原值，否则返回默认值
   */
  public static float safeGt(Float value, float target, float elseValue) {
    return value == null ? elseValue : (value > target ? value : elseValue);
  }

  /**
   * 如果值大于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于目标值返回原值，否则返回默认值
   */
  public static double safeGt(Double value, double target, double elseValue) {
    return value == null ? elseValue : (value > target ? value : elseValue);
  }

  /**
   * 如果值大于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于等于目标值返回原值，否则返回默认值
   */
  public static byte safeGtEq(Byte value, byte target, byte elseValue) {
    return value == null ? elseValue : (value >= target ? value : elseValue);
  }

  /**
   * 如果值大于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于等于目标值返回原值，否则返回默认值
   */
  public static short safeGtEq(Short value, short target, short elseValue) {
    return value == null ? elseValue : (value >= target ? value : elseValue);
  }

  /**
   * 如果值大于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于等于目标值返回原值，否则返回默认值
   */
  public static int safeGtEq(Integer value, int target, int elseValue) {
    return value == null ? elseValue : (value >= target ? value : elseValue);
  }

  /**
   * 如果值大于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于等于目标值返回原值，否则返回默认值
   */
  public static long safeGtEq(Long value, long target, long elseValue) {
    return value == null ? elseValue : (value >= target ? value : elseValue);
  }

  /**
   * 如果值大于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于等于目标值返回原值，否则返回默认值
   */
  public static float safeGtEq(Float value, float target, float elseValue) {
    return value == null ? elseValue : (value >= target ? value : elseValue);
  }

  /**
   * 如果值大于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值大于等于目标值返回原值，否则返回默认值
   */
  public static double safeGtEq(Double value, double target, double elseValue) {
    return value == null ? elseValue : (value >= target ? value : elseValue);
  }

  /**
   * 如果值小于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于目标值返回原值，否则返回默认值
   */
  public static byte safeLt(Byte value, byte target, byte elseValue) {
    return value == null ? elseValue : (value < target ? value : elseValue);
  }

  /**
   * 如果值小于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于目标值返回原值，否则返回默认值
   */
  public static short safeLt(Short value, short target, short elseValue) {
    return value == null ? elseValue : (value < target ? value : elseValue);
  }

  /**
   * 如果值小于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于目标值返回原值，否则返回默认值
   */
  public static int safeLt(Integer value, int target, int elseValue) {
    return value == null ? elseValue : (value < target ? value : elseValue);
  }

  /**
   * 如果值小于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于目标值返回原值，否则返回默认值
   */
  public static long safeLt(Long value, long target, long elseValue) {
    return value == null ? elseValue : (value < target ? value : elseValue);
  }

  /**
   * 如果值小于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于目标值返回原值，否则返回默认值
   */
  public static float safeLt(Float value, float target, float elseValue) {
    return value == null ? elseValue : (value < target ? value : elseValue);
  }

  /**
   * 如果值小于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于目标值返回原值，否则返回默认值
   */
  public static double safeLt(Double value, double target, double elseValue) {
    return value == null ? elseValue : (value < target ? value : elseValue);
  }

  /**
   * 如果值小于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于等于目标值返回原值，否则返回默认值
   */
  public static byte safeLtEq(Byte value, byte target, byte elseValue) {
    return value == null ? elseValue : (value <= target ? value : elseValue);
  }

  /**
   * 如果值小于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于等于目标值返回原值，否则返回默认值
   */
  public static short safeLtEq(Short value, short target, short elseValue) {
    return value == null ? elseValue : (value <= target ? value : elseValue);
  }

  /**
   * 如果值小于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于等于目标值返回原值，否则返回默认值
   */
  public static int safeLtEq(Integer value, int target, int elseValue) {
    return value == null ? elseValue : (value <= target ? value : elseValue);
  }

  /**
   * 如果值小于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于等于目标值返回原值，否则返回默认值
   */
  public static long safeLtEq(Integer value, long target, long elseValue) {
    return value == null ? elseValue : (value <= target ? value : elseValue);
  }

  /**
   * 如果值小于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于等于目标值返回原值，否则返回默认值
   */
  public static float safeLtEq(Float value, float target, float elseValue) {
    return value == null ? elseValue : (value <= target ? value : elseValue);
  }

  /**
   * 如果值小于等于目标值返回原值，否则返回默认值
   *
   * @param value 值
   * @param target 目标值
   * @param elseValue 否则当不满条件的值
   * @return 如果值小于等于目标值返回原值，否则返回默认值
   */
  public static double safeLtEq(Double value, double target, double elseValue) {
    return value == null ? elseValue : (value <= target ? value : elseValue);
  }

  public static byte[] toPrimitiveValue(@Nullable Byte[] value) {
    return toPrimitiveValue(value, ZERO_BYTE);
  }

  public static short[] toPrimitiveValue(@Nullable Short[] value) {
    return toPrimitiveValue(value, ZERO_SHORT);
  }

  public static int[] toPrimitiveValue(@Nullable Integer[] value) {
    return toPrimitiveValue(value, ZERO_INT);
  }

  public static long[] toPrimitiveValue(@Nullable Long[] value) {
    return toPrimitiveValue(value, ZERO_LONG);
  }

  public static float[] toPrimitiveValue(@Nullable Float[] value) {
    return toPrimitiveValue(value, ZERO_FLOAT);
  }

  public static double[] toPrimitiveValue(@Nullable Double[] value) {
    return toPrimitiveValue(value, ZERO_DOUBLE);
  }

  public static boolean[] toPrimitiveValue(@Nullable Boolean[] value) {
    return toPrimitiveValue(value, Boolean.FALSE);
  }

  public static char[] toPrimitiveValue(@Nullable Character[] value) {
    return toPrimitiveValue(value, ' ');
  }

  public static byte[] toPrimitiveValue(@Nullable Byte[] value, byte defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_BYTE_ARRAY;
    }
    byte[] outputs = new byte[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static short[] toPrimitiveValue(@Nullable Short[] value, short defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_SHORT_ARRAY;
    }
    short[] outputs = new short[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static int[] toPrimitiveValue(@Nullable Integer[] value, int defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_INT_ARRAY;
    }
    int[] outputs = new int[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static long[] toPrimitiveValue(@Nullable Long[] value, long defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_LONG_ARRAY;
    }
    long[] outputs = new long[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static float[] toPrimitiveValue(@Nullable Float[] value, float defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_FLOAT_ARRAY;
    }
    float[] outputs = new float[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static double[] toPrimitiveValue(@Nullable Double[] value, double defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_DOUBLE_ARRAY;
    }
    double[] outputs = new double[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static boolean[] toPrimitiveValue(@Nullable Boolean[] value, boolean defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_BOOLEAN_ARRAY;
    }
    boolean[] outputs = new boolean[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static char[] toPrimitiveValue(@Nullable Character[] value, char defaultValue) {
    if (Whether.empty(value)) {
      return EMPTY_CHAR_ARRAY;
    }
    char[] outputs = new char[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = toPrimitiveValue(value[i], defaultValue);
    }
    return outputs;
  }

  public static byte toPrimitiveValue(@Nullable Byte value) {
    return toPrimitiveValue(value, ZERO_BYTE);
  }

  public static short toPrimitiveValue(@Nullable Short value) {
    return toPrimitiveValue(value, ZERO_SHORT);
  }

  public static int toPrimitiveValue(@Nullable Integer value) {
    return toPrimitiveValue(value, ZERO_INT);
  }

  public static long toPrimitiveValue(@Nullable Long value) {
    return toPrimitiveValue(value, ZERO_LONG);
  }

  public static float toPrimitiveValue(@Nullable Float value) {
    return toPrimitiveValue(value, ZERO_FLOAT);
  }

  public static double toPrimitiveValue(@Nullable Double value) {
    return toPrimitiveValue(value, ZERO_DOUBLE);
  }

  public static boolean toPrimitiveValue(@Nullable Boolean value) {
    return toPrimitiveValue(value, Boolean.FALSE);
  }

  public static char toPrimitiveValue(@Nullable Character value) {
    return toPrimitiveValue(value, ' ');
  }

  public static byte toPrimitiveValue(@Nullable Byte value, byte defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static short toPrimitiveValue(@Nullable Short value, short defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static int toPrimitiveValue(@Nullable Integer value, int defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static long toPrimitiveValue(@Nullable Long value, long defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static float toPrimitiveValue(@Nullable Float value, float defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static double toPrimitiveValue(@Nullable Double value, double defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static boolean toPrimitiveValue(@Nullable Boolean value, boolean defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static char toPrimitiveValue(@Nullable Character value, char defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static Byte[] toObjectValue(@Nullable byte[] value) {
    if (Whether.empty(value)) {
      return EMPTY_BYTE_OBJECT_ARRAY;
    }
    Byte[] outputs = new Byte[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Short[] toObjectValue(@Nullable short[] value) {
    if (Whether.empty(value)) {
      return EMPTY_SHORT_OBJECT_ARRAY;
    }
    Short[] outputs = new Short[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Integer[] toObjectValue(@Nullable int[] value) {
    if (Whether.empty(value)) {
      return EMPTY_INTEGER_OBJECT_ARRAY;
    }
    Integer[] outputs = new Integer[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Long[] toObjectValue(@Nullable long[] value) {
    if (Whether.empty(value)) {
      return EMPTY_LONG_OBJECT_ARRAY;
    }
    Long[] outputs = new Long[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Float[] toObjectValue(@Nullable float[] value) {
    if (Whether.empty(value)) {
      return EMPTY_FLOAT_OBJECT_ARRAY;
    }
    Float[] outputs = new Float[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Double[] toObjectValue(@Nullable double[] value) {
    if (Whether.empty(value)) {
      return EMPTY_DOUBLE_OBJECT_ARRAY;
    }
    Double[] outputs = new Double[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Boolean[] toObjectValue(@Nullable boolean[] value) {
    if (Whether.empty(value)) {
      return EMPTY_BOOLEAN_OBJECT_ARRAY;
    }
    Boolean[] outputs = new Boolean[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static Character[] toObjectValue(@Nullable char[] value) {
    if (Whether.empty(value)) {
      return EMPTY_CHARACTER_OBJECT_ARRAY;
    }
    Character[] outputs = new Character[value.length];
    for (int i = 0; i < value.length; i++) {
      outputs[i] = value[i];
    }
    return outputs;
  }

  public static CharSequenceAppendable toFunctionString(
      @Nonnull CharSequenceAppendable container,
      @Nonnull String functionName,
      @Nonnull String argName,
      int argLength) {
    if (argLength <= 0) {
      return toFunctionString(container, functionName);
    } else {
      String[] args = new String[argLength];
      for (int i = 0; i < argLength; i++) {
        args[i] = argName + i;
      }
      return toFunctionString(container, functionName, args);
    }
  }

  public static CharSequenceAppendable toFunctionString(
      @Nonnull CharSequenceAppendable container,
      @Nonnull String functionName,
      @Nullable Class<?>[] argTypes,
      @Nullable String[] argNames) {
    if (Whether.empty(argTypes) && Whether.empty(argNames)) {
      return toFunctionString(container, functionName);
    } else {
      if (argTypes.length != argNames.length) {
        throw new IllegalArgumentException(
            "The parameter type and the number of names do not match");
      }
      String[] args = new String[argTypes.length];
      for (int i = 0; i < args.length; i++) {
        args[i] = argTypes[i].getSimpleName() + " " + argNames[i];
      }
      return toFunctionString(container, functionName, args);
    }
  }

  public static CharSequenceAppendable toFunctionString(
      @Nonnull CharSequenceAppendable container,
      @Nonnull String functionName,
      @Nullable String... args) {
    container.append(functionName);
    if (args != null && args.length > 0) {
      container.append("(").append(args[0]);
      for (int i = 1; i < args.length; i++) {
        container.append(", ").append(args[i]);
      }
      container.append(")");
    } else {
      container.append("()");
    }
    return container;
  }

  /**
   * An enum representing all the versions of the Java specification. This is intended to mirror
   * available values from the <em>java.specification.version</em> System property.
   *
   * @version $Id: $
   * @since 3.0
   */
  public enum JavaVersion {

    /** The Java version reported by Android. This is not an official Java version number. */
    JAVA_0_9(1.5f, "0.9"),

    /** Java 1.1. */
    JAVA_1_1(1.1f, "1.1"),

    /** Java 1.2. */
    JAVA_1_2(1.2f, "1.2"),

    /** Java 1.3. */
    JAVA_1_3(1.3f, "1.3"),

    /** Java 1.4. */
    JAVA_1_4(1.4f, "1.4"),

    /** Java 1.5. */
    JAVA_1_5(1.5f, "1.5"),

    /** Java 1.6. */
    JAVA_1_6(1.6f, "1.6"),

    /** Java 1.7. */
    JAVA_1_7(1.7f, "1.7"),

    /** Java 1.8. */
    JAVA_1_8(1.8f, "1.8");

    /** The float value. */
    private final float value;
    /** The standard name. */
    private final String name;

    /**
     * Constructor.
     *
     * @param value the float value
     * @param name the standard name, not null
     */
    JavaVersion(final float value, final String name) {
      this.value = value;
      this.name = name;
    }

    // -----------------------------------------------------------------------

    /**
     * Transforms the given string with a Java version number to the corresponding constant of this
     * enumeration class. This method is used internally.
     *
     * @param nom the Java version as string
     * @return the corresponding enumeration constant or <b>null</b> if the version is unknown
     */
    // helper for static importing
    static JavaVersion getJavaVersion(final String nom) {
      return get(nom);
    }

    /**
     * Transforms the given string with a Java version number to the corresponding constant of this
     * enumeration class. This method is used internally.
     *
     * @param nom the Java version as string
     * @return the corresponding enumeration constant or <b>null</b> if the version is unknown
     */
    static JavaVersion get(final String nom) {
      if ("0.9".equals(nom)) {
        return JAVA_0_9;
      } else if ("1.1".equals(nom)) {
        return JAVA_1_1;
      } else if ("1.2".equals(nom)) {
        return JAVA_1_2;
      } else if ("1.3".equals(nom)) {
        return JAVA_1_3;
      } else if ("1.4".equals(nom)) {
        return JAVA_1_4;
      } else if ("1.5".equals(nom)) {
        return JAVA_1_5;
      } else if ("1.6".equals(nom)) {
        return JAVA_1_6;
      } else if ("1.7".equals(nom)) {
        return JAVA_1_7;
      } else if ("1.8".equals(nom)) {
        return JAVA_1_8;
      } else {
        return null;
      }
    }

    /**
     * Whether this version of Java is at least the version of Java passed in.
     *
     * <p>For example:<br>
     * {@code myVersion.atLeast(JavaVersion.JAVA_1_4)}
     *
     * <p>
     *
     * @param requiredVersion the version to check against, not null
     * @return true if this version is equal to or greater than the specified version
     */
    public boolean atLeast(final JavaVersion requiredVersion) {
      return this.value >= requiredVersion.value;
    }

    // -----------------------------------------------------------------------

    /**
     * The string value is overridden to return the standard name.
     *
     * <p>For example, <code>"1.5"</code>.
     *
     * @return the name, not null
     */
    @Override
    public String toString() {
      return name;
    }
  }
}
