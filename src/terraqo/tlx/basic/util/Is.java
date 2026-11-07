package pxf.toolkit.basic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 常用判断类
 *
 * @author potatoxf
 * @date 2021/3/3
 */
public final class Is {

  private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

  private Is() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 任何对象是否为null
   *
   * @param any 任何对象
   * @return 如果{@code null}为 {@code true}，否则 {@code false}
   */
  public static boolean nvl(Object any) {
    return any == null;
  }

  /**
   * 任何对象是否不为null
   *
   * @param any 任何对象
   * @return 如果{@code null}为 {@code false}，否则 {@code true}
   */
  public static boolean nonvl(Object any) {
    return any != null;
  }

  /**
   * 任何对象是否为null
   *
   * @param any 任何对象
   * @return 如果{@code null}为 {@code true}，否则 {@code false}
   */
  public static boolean nvls(Object... any) {
    if (any == null) {
      return true;
    }
    for (Object a : any) {
      if (a == null) {
        return true;
      }
    }
    return false;
  }

  /**
   * 任何对象是否不为null
   *
   * @param any 任何对象
   * @return 如果{@code null}为 {@code false}，否则 {@code true}
   */
  public static boolean nonvls(Object... any) {
    if (any == null) {
      return true;
    }
    for (Object a : any) {
      if (a == null) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(boolean... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(byte... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(char... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(short... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(int... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(long... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(float... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(double... arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断数组对象是否为空
   *
   * @param arr 数组
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static <T> boolean empty(T[] arr) {
    return arr == null || arr.length == 0;
  }

  /**
   * 判断迭代器对象是否为空
   *
   * @param it 迭代器
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(Iterator<?> it) {
    return it == null || !it.hasNext();
  }

  /**
   * 判断迭代器对象是否为空
   *
   * @param it 迭代器
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(Iterable<?> it) {
    return it == null || !it.iterator().hasNext();
  }

  /**
   * 判断集合对象是否为空
   *
   * @param col 集合
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(Collection<?> col) {
    return col == null || col.size() == 0;
  }

  /**
   * 判断集合对象是否为空
   *
   * @param map 集合
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(Map<?, ?> map) {
    return map == null || map.size() == 0;
  }

  /**
   * 判断字符串对象是否为空
   *
   * @param cs 字符串
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  /**
   * 判断输入流对象是否为空
   *
   * @param in 输入流
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public static boolean empty(InputStream in) {
    if (in == null) {
      return true;
    }
    if (!in.markSupported()) {
      in = new PushbackInputStream(in, 1);
    }
    in.mark(1);
    try {
      int read = in.read();
      if (in instanceof PushbackInputStream) {
        PushbackInputStream pin = (PushbackInputStream) in;
        pin.unread(read);
      } else {
        in.reset();
      }
      return read == -1;
    } catch (IOException e) {
      return true;
    }
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(boolean... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(byte... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(char... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(short... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(int... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(long... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(float... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(double... arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断数组对象是否不为空
   *
   * @param arr 数组
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static <T> boolean noEmpty(T[] arr) {
    return !Is.empty(arr);
  }

  /**
   * 判断迭代器对象是否不为空
   *
   * @param it 迭代器
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(Iterator<?> it) {
    return !Is.empty(it);
  }

  /**
   * 判断迭代器对象是否不为空
   *
   * @param it 迭代器
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(Iterable<?> it) {
    return !Is.empty(it);
  }

  /**
   * 判断集合对象是否不为空
   *
   * @param col 集合
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(Collection<?> col) {
    return !Is.empty(col);
  }

  /**
   * 判断集合对象是否不为空
   *
   * @param map 集合
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(Map<?, ?> map) {
    return !Is.empty(map);
  }

  /**
   * 判断字符串对象是否不为空
   *
   * @param cs 字符串
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(CharSequence cs) {
    return !Is.empty(cs);
  }

  /**
   * 判断输入流对象是否不为空
   *
   * @param in 输入流
   * @return 如果非空为 {@code true}，否则 {@code false}
   */
  public static boolean noEmpty(InputStream in) {
    return !Is.empty(in);
  }

  /**
   * 是linux系统
   *
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean linuxSystem() {
    return OS_NAME.startsWith("linux");
  }

  /**
   * 是mac系统
   *
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean macSystem() {
    return OS_NAME.startsWith("mac");
  }

  /**
   * 是windows系统
   *
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean windowsSystem() {
    return OS_NAME.startsWith("windows");
  }

  /**
   * 是否包含换行符
   *
   * @param charSequence 字符串
   * @return 如果包含换行符则返回 {@code true}，否则 {@code false}
   */
  public static boolean containLineSeparator(CharSequence charSequence) {
    if (charSequence == null) {
      return false;
    }
    int length = charSequence.length();
    if (length == 0) {
      return false;
    }
    for (int i = 0; i < length; i++) {
      char input = charSequence.charAt(i);
      if (input == '\r' || input == '\n') {
        return true;
      }
    }
    return false;
  }

  /**
   * 是否是全角字符串
   *
   * @param codepoint unicode 码点
   * @return 如果是则返回 {@code true}，否则 {@code false}
   */
  public static boolean quadEm(int codepoint) {
    boolean isEm;
    if (codepoint < 0x9FFF) {
      isEm =
          (codepoint >= 0x1100 && codepoint <= 0x11FF)
              || (codepoint >= 0x2600 && codepoint <= 0x27BF)
              || (codepoint >= 0x2800 && codepoint <= 0x28FF)
              || (codepoint >= 0x2E80 && codepoint <= 0x2FDF)
              || (codepoint >= 0x2FF0 && codepoint <= 0x318F)
              || (codepoint >= 0x31A0 && codepoint <= 0x4DB5)
              || (codepoint >= 0x4DC0 && codepoint <= 0x9FBB);
    } else if (codepoint < 0xFAFF) {
      isEm =
          (codepoint >= 0xA000 && codepoint <= 0xA4CF)
              || (codepoint >= 0xAC00 && codepoint <= 0xD7AF)
              || (codepoint >= 0xF900 && codepoint <= 0xFA2D)
              || (codepoint >= 0xFA30 && codepoint <= 0xFA6A)
              || (codepoint >= 0xFA70 && codepoint <= 0xFAD9);
    } else {
      isEm =
          (codepoint >= 0xFE10 && codepoint <= 0xFE1F)
              || (codepoint >= 0xFE30 && codepoint <= 0xFE4F)
              || (codepoint >= 0xFF00 && codepoint <= 0xFFEF)
              || (codepoint >= 0x1D300 && codepoint <= 0x1D35F)
              || (codepoint >= 0x20000 && codepoint <= 0x2A6D6)
              || (codepoint >= 0x2F800 && codepoint <= 0x2FA1D);
    }
    return isEm;
  }

  /**
   * 是否为ASCII字符，ASCII字符位于0~127之间
   *
   * <pre>
   *   CharUtil.isAscii('a')  = true
   *   CharUtil.isAscii('A')  = true
   *   CharUtil.isAscii('3')  = true
   *   CharUtil.isAscii('-')  = true
   *   CharUtil.isAscii('\n') = true
   *   CharUtil.isAscii('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符处
   * @return true表示为ASCII字符，ASCII字符位于0~127之间
   */
  public static boolean ascii(char input) {
    return input < 128;
  }

  /**
   * 是否为可见ASCII字符，可见字符位于32~126之间
   *
   * <pre>
   *   CharUtil.isAsciiPrintable('a')  = true
   *   CharUtil.isAsciiPrintable('A')  = true
   *   CharUtil.isAsciiPrintable('3')  = true
   *   CharUtil.isAsciiPrintable('-')  = true
   *   CharUtil.isAsciiPrintable('\n') = false
   *   CharUtil.isAsciiPrintable('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符处
   * @return true表示为ASCII可见字符，可见字符位于32~126之间
   */
  public static boolean asciiPrintable(char input) {
    return input >= 32 && input < 127;
  }

  /**
   * 是否为ASCII控制符（不可见字符），控制符位于0~31和127
   *
   * <pre>
   *   CharUtil.isAsciiControl('a')  = false
   *   CharUtil.isAsciiControl('A')  = false
   *   CharUtil.isAsciiControl('3')  = false
   *   CharUtil.isAsciiControl('-')  = false
   *   CharUtil.isAsciiControl('\n') = true
   *   CharUtil.isAsciiControl('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为控制符，控制符位于0~31和127
   */
  public static boolean asciiControl(char input) {
    return input < 32 || input == 127;
  }

  /**
   * 判断是否为字母（包括大写字母和小写字母）<br>
   * 字母包括A~Z和a~z
   *
   * <pre>
   *   CharUtil.isLetter('a')  = true
   *   CharUtil.isLetter('A')  = true
   *   CharUtil.isLetter('3')  = false
   *   CharUtil.isLetter('-')  = false
   *   CharUtil.isLetter('\n') = false
   *   CharUtil.isLetter('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为字母（包括大写字母和小写字母）字母包括A~Z和a~z
   */
  public static boolean letter(char input) {
    return Is.letterUpper(input) || Is.letterLower(input);
  }

  /**
   * 判断是否为字母（包括大写字母和小写字母）<br>
   * 字母包括A~Z和a~z
   *
   * <pre>
   *   CharUtil.isLetter('a')  = true
   *   CharUtil.isLetter('A')  = true
   *   CharUtil.isLetter('3')  = false
   *   CharUtil.isLetter('-')  = false
   *   CharUtil.isLetter('\n') = false
   *   CharUtil.isLetter('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为字母（包括大写字母和小写字母）字母包括A~Z和a~z
   */
  public static boolean letter(int input) {
    return Is.letterUpper(input) || Is.letterLower(input);
  }

  /**
   * 判断是否为大写字母，大写字母包括A~Z
   *
   * <pre>
   *   CharUtil.isLetterUpper('a')  = false
   *   CharUtil.isLetterUpper('A')  = true
   *   CharUtil.isLetterUpper('3')  = false
   *   CharUtil.isLetterUpper('-')  = false
   *   CharUtil.isLetterUpper('\n') = false
   *   CharUtil.isLetterUpper('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为大写字母，大写字母包括A~Z
   */
  public static boolean letterUpper(char input) {
    return input >= 'A' && input <= 'Z';
  }

  /**
   * 判断是否为大写字母，大写字母包括A~Z
   *
   * <pre>
   *   CharUtil.isLetterUpper('a')  = false
   *   CharUtil.isLetterUpper('A')  = true
   *   CharUtil.isLetterUpper('3')  = false
   *   CharUtil.isLetterUpper('-')  = false
   *   CharUtil.isLetterUpper('\n') = false
   *   CharUtil.isLetterUpper('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为大写字母，大写字母包括A~Z
   */
  public static boolean letterUpper(int input) {
    return input >= Const.ASCII_UPPER_LETTER_LO && input <= Const.ASCII_UPPER_LETTER_HI;
  }

  /**
   * 检查字符是否为小写字母，小写字母指a~z
   *
   * <pre>
   *   CharUtil.isLetterLower('a')  = true
   *   CharUtil.isLetterLower('A')  = false
   *   CharUtil.isLetterLower('3')  = false
   *   CharUtil.isLetterLower('-')  = false
   *   CharUtil.isLetterLower('\n') = false
   *   CharUtil.isLetterLower('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为小写字母，小写字母指a~z
   */
  public static boolean letterLower(char input) {
    return input >= 'a' && input <= 'z';
  }

  /**
   * 检查字符是否为小写字母，小写字母指a~z
   *
   * <pre>
   *   CharUtil.isLetterLower('a')  = true
   *   CharUtil.isLetterLower('A')  = false
   *   CharUtil.isLetterLower('3')  = false
   *   CharUtil.isLetterLower('-')  = false
   *   CharUtil.isLetterLower('\n') = false
   *   CharUtil.isLetterLower('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为小写字母，小写字母指a~z
   */
  public static boolean letterLower(int input) {
    return input >= Const.ASCII_LOWER_LETTER_LO && input <= Const.ASCII_LOWER_LETTER_HI;
  }

  /**
   * 检查是否为数字字符，数字字符指0~9
   *
   * <pre>
   *   CharUtil.isNumber('a')  = false
   *   CharUtil.isNumber('A')  = false
   *   CharUtil.isNumber('3')  = true
   *   CharUtil.isNumber('-')  = false
   *   CharUtil.isNumber('\n') = false
   *   CharUtil.isNumber('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为数字字符，数字字符指0~9
   */
  public static boolean numberChar(char input) {
    return input >= '0' && input <= '9';
  }

  /**
   * 检查是否为数字字符，数字字符指0~9
   *
   * <pre>
   *   CharUtil.isNumber('a')  = false
   *   CharUtil.isNumber('A')  = false
   *   CharUtil.isNumber('3')  = true
   *   CharUtil.isNumber('-')  = false
   *   CharUtil.isNumber('\n') = false
   *   CharUtil.isNumber('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为数字字符，数字字符指0~9
   */
  public static boolean numberChar(int input) {
    return input >= Const.ASCII_NUMBER_LO && input <= Const.ASCII_NUMBER_HI;
  }

  /**
   * 是否为16进制规范的字符，判断是否为如下字符
   *
   * <pre>
   * 1. 0~9
   * 2. a~f
   * 4. A~F
   * </pre>
   *
   * @param input 字符
   * @return 是否为16进制规范的字符
   */
  public static boolean hexChar(char input) {
    return numberChar(input) || (input >= 'a' && input <= 'f') || (input >= 'A' && input <= 'F');
  }

  /**
   * 是否为16进制规范的字符，判断是否为如下字符
   *
   * <pre>
   * 1. 0~9
   * 2. a~f
   * 4. A~F
   * </pre>
   *
   * @param input 字符
   * @return 是否为16进制规范的字符
   */
  public static boolean hexChar(int input) {
    return numberChar(input) || (input >= 'a' && input <= 'f') || (input >= 'A' && input <= 'F');
  }

  /**
   * 是否为字符或数字，包括A~Z、a~z、0~9
   *
   * <pre>
   *   CharUtil.isLetterOrNumber('a')  = true
   *   CharUtil.isLetterOrNumber('A')  = true
   *   CharUtil.isLetterOrNumber('3')  = true
   *   CharUtil.isLetterOrNumber('-')  = false
   *   CharUtil.isLetterOrNumber('\n') = false
   *   CharUtil.isLetterOrNumber('&copy;') = false
   * </pre>
   *
   * @param input 被检查的字符
   * @return true表示为字符或数字，包括A~Z、a~z、0~9
   */
  public static boolean letterOrNumber(char input) {
    return letter(input) || numberChar(input);
  }

  /**
   * 给定类名是否为字符类，字符类包括：
   *
   * <pre>
   * Character.class
   * char.class
   * </pre>
   *
   * @param input 被检查的类
   * @return true表示为字符类
   */
  public static boolean charClass(Class<?> input) {
    return input == Character.class || input == char.class;
  }

  /**
   * 给定对象对应的类是否为字符类，字符类包括：
   *
   * <pre>
   * Character.class
   * char.class
   * </pre>
   *
   * @param input 被检查的对象
   * @return true表示为字符类
   */
  public static boolean charObject(Object input) {
    return input instanceof Character;
  }

  /**
   * 是否空白符<br>
   * 空白符包括空格、制表符、全角空格和不间断空格<br>
   *
   * @param input 字符
   * @return 是否空白符
   * @see Character#isWhitespace(int)
   * @see Character#isSpaceChar(int)
   */
  public static boolean blankChar(char input) {
    return blankChar((int) input);
  }

  /**
   * 是否空白符<br>
   * 空白符包括空格、制表符、全角空格和不间断空格<br>
   *
   * @param input 字符
   * @return 是否空白符
   * @see Character#isWhitespace(int)
   * @see Character#isSpaceChar(int)
   */
  public static boolean blankChar(int input) {
    return Character.isWhitespace(input)
        || Character.isSpaceChar(input)
        || input == '\ufeff'
        || input == '\u202a';
  }

  /**
   * 判断是否为emoji表情符<br>
   *
   * @param input 字符
   * @return 是否为emoji
   */
  public static boolean emoji(char input) {
    return !(input == 0x0
        || input == 0x9
        || input == 0xA
        || input == 0xD
        || input >= 0x20 && input <= 0xD7FF
        || input >= 0xE000 && input <= 0xFFFD);
  }

  /**
   * 是否为Windows或者Linux（Unix）文件分隔符<br>
   * Windows平台下分隔符为\，Linux（Unix）为/
   *
   * @param input 字符
   * @return 是否为Windows或者Linux（Unix）文件分隔符
   */
  public static boolean fileSeparator(char input) {
    return '/' == input || '\\' == input;
  }

  /**
   * 比较两个字符是否相同
   *
   * @param c1 字符1
   * @param c2 字符2
   * @param ignoreCase 是否忽略大小写
   * @return 是否相同
   */
  public static boolean equalChar(char c1, char c2, boolean ignoreCase) {
    if (ignoreCase) {
      return Character.toLowerCase(c1) == Character.toLowerCase(c2);
    }
    return c1 == c2;
  }

  public static boolean equalCharsWithString(
      char[] chars, int csi, int cei, String string, int strLen, boolean isIgnoreCase) {
    if (strLen == cei - csi) {
      boolean isEqual = true;
      for (int j = 0; j < strLen; j++) {
        if (!Is.equalChar(chars[csi + j], string.charAt(j), isIgnoreCase)) {
          isEqual = false;
        }
      }
      return isEqual;
    }
    return false;
  }
  // ------------------------------------------------------------------------ Blank

  /**
   * 字符串是否为空白 空白的定义如下： <br>
   * 1、为null <br>
   * 2、为不可见字符（如空格）<br>
   * 3、""<br>
   *
   * @param input 被检测的字符串
   * @return 是否为空
   */
  public static boolean blank(CharSequence input) {
    int length;

    if ((input == null) || ((length = input.length()) == 0)) {
      return true;
    }

    for (int i = 0; i < length; i++) {
      // 只要有一个非空字符即为非空字符串
      if (!Is.blankChar(input.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  /**
   * 如果对象是字符串是否为空白，空白的定义如下： <br>
   * 1、为null <br>
   * 2、为不可见字符（如空格）<br>
   * 3、""<br>
   *
   * @param input 对象
   * @return 如果为字符串是否为空串
   */
  public static boolean blankIfString(Object input) {
    if (null == input) {
      return true;
    } else if (input instanceof CharSequence) {
      return Is.blank((CharSequence) input);
    }
    return false;
  }

  /**
   * 字符串是否为非空白 空白的定义如下： <br>
   * 1、不为null <br>
   * 2、不为不可见字符（如空格）<br>
   * 3、不为""<br>
   *
   * @param input 被检测的字符串
   * @return 是否为非空
   */
  public static boolean noBlank(CharSequence input) {
    return !Is.blank(input);
  }

  /**
   * 是否包含空字符串
   *
   * @param inputs 字符串列表
   * @return 是否包含空字符串
   */
  public static boolean hasBlank(CharSequence... inputs) {
    if (Is.empty(inputs)) {
      return true;
    }

    for (CharSequence input : inputs) {
      if (Is.blank(input)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 给定所有字符串是否为空白
   *
   * @param inputs 字符串
   * @return 所有字符串是否为空白
   */
  public static boolean isAllBlank(CharSequence... inputs) {
    if (Is.empty(inputs)) {
      return true;
    }

    for (CharSequence input : inputs) {
      if (Is.noBlank(input)) {
        return false;
      }
    }
    return true;
  }
  /**
   * 判断String是否是整数<br>
   * 支持10、16进制
   *
   * @param input String
   * @return 是否为整数
   */
  public static boolean integerNumber(String input) {
    boolean[] numberString = Is.numberString(input);
    return numberString[0] && !numberString[1];
  }

  /**
   * 判断String是否是整数<br>
   * 支持10、16进制
   *
   * @param input String
   * @return 是否为整数
   */
  public static boolean integerNumberForHuman(String input) {
    boolean[] numberString = Is.numberString(input);
    return numberString[0] && !numberString[1] && !numberString[2];
  }

  /**
   * 判断字符串是否是浮点数
   *
   * @param input String
   * @return 是否为{@link Double}类型
   */
  public static boolean doubleNumber(String input) {
    boolean[] numberString = Is.numberString(input);
    return numberString[0] && numberString[1];
  }

  /**
   * 判断字符串是否是浮点数
   *
   * @param input String
   * @return 是否为{@link Double}类型
   */
  public static boolean doubleNumberForHuman(String input) {
    boolean[] numberString = Is.numberString(input);
    return numberString[0] && !numberString[1] && !numberString[2];
  }

  /**
   * 是否为数字
   *
   * @param input 字符串值
   * @return 是否为数字
   */
  public static boolean number(String input) {
    return Is.numberString(input)[0];
  }
  /**
   * 是否为人类易懂数字，排除16进制
   *
   * @param input 字符串值
   * @return 是否为数字
   */
  public static boolean numberForHuman(String input) {
    boolean[] numberString = Is.numberString(input);
    return numberString[0] && !numberString[2];
  }

  /**
   * 是否为数字
   *
   * @param input 字符串值
   * @return 是否为数字
   */
  private static boolean[] numberString(String input) {
    // 0 isNumber 1 isDecimal 2 isHex
    boolean[] error = {false, false, false};
    if (Is.blank(input)) {
      return error;
    }
    char[] chars = input.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;
    // deal with any possible sign up front
    int start = (chars[0] == '-') ? 1 : 0;
    if (sz > start + 1) {
      if (chars[start] == '0' && chars[start + 1] == 'x') {
        int i = start + 2;
        if (i == sz) {
          // input == "0x"
          return error;
        }
        // checking hex (it can't be anything else)
        for (; i < chars.length; i++) {
          if ((chars[i] < '0' || chars[i] > '9')
              && (chars[i] < 'a' || chars[i] > 'f')
              && (chars[i] < 'A' || chars[i] > 'F')) {
            return error;
          }
        }
        return new boolean[] {true, false, true};
      }
    }
    sz--; // don't want to loop to the last char, check it afterwords
    // for type qualifiers
    int i = start;
    // loop to the next to last char or to the last char if we need another digit to
    // make a valid number (e.g. chars[0..5] = "1234E")
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
      if (chars[i] >= '0' && chars[i] <= '9') {
        foundDigit = true;
        allowSigns = false;

      } else if (chars[i] == '.') {
        if (hasDecPoint || hasExp) {
          // two decimal points or dec in exponent
          return error;
        }
        hasDecPoint = true;
      } else if (chars[i] == 'e' || chars[i] == 'E') {
        // we've already taken care of hex.
        if (hasExp) {
          // two E's
          return error;
        }
        if (!foundDigit) {
          return error;
        }
        hasExp = true;
        allowSigns = true;
      } else if (chars[i] == '+' || chars[i] == '-') {
        if (!allowSigns) {
          return error;
        }
        allowSigns = false;
        foundDigit = false; // we need a digit after the E
      } else {
        return error;
      }
      i++;
    }
    if (i < chars.length) {
      if (chars[i] >= '0' && chars[i] <= '9') {
        // no type qualifier, OK
        return new boolean[] {true, hasDecPoint, false};
      }
      if (chars[i] == 'e' || chars[i] == 'E') {
        // can't have an E at the last byte
        return error;
      }
      if (chars[i] == '.') {
        if (hasDecPoint || hasExp) {
          // two decimal points or dec in exponent
          return error;
        }
        // single trailing decimal point after non-exponent is ok
        return new boolean[] {foundDigit, true, false};
      }
      if (!allowSigns
          && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
        return new boolean[] {foundDigit, hasDecPoint, false};
      }
      if (chars[i] == 'l' || chars[i] == 'L') {
        // not allowing L with an exponent
        return new boolean[] {foundDigit && !hasExp, hasDecPoint, false};
      }
      // last character is illegal
      return error;
    }
    // allowSigns is true iff the val ends in 'E'
    // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
    return new boolean[] {!allowSigns && foundDigit, hasDecPoint, false};
  }

  /**
   * 是否是质数（素数）<br>
   * 质数表的质数又称素数。指整数在一个大于1的自然数中,除了1和此整数自身外,没法被其他自然数整除的数。
   *
   * @param n 数字
   * @return 是否是质数
   */
  public static boolean primes(int n) {
    for (int i = 2; i <= Math.sqrt(n); i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * 判断两个数字是否相邻，例如1和2相邻，1和3不相邻<br>
   * 判断方法为做差取绝对值判断是否为1
   *
   * @param number1 数字1
   * @param number2 数字2
   * @return 是否相邻
   */
  public static boolean besideNumber(int number1, int number2) {
    return Math.abs(number1 - number2) == 1;
  }

  /**
   * 判断两个数字是否相邻，例如1和2相邻，1和3不相邻<br>
   * 判断方法为做差取绝对值判断是否为1
   *
   * @param number1 数字1
   * @param number2 数字2
   * @return 是否相邻
   */
  public static boolean besideNumber(long number1, long number2) {
    return Math.abs(number1 - number2) == 1;
  }

  /**
   * 比较大小，参数1大于参数2 返回true
   *
   * @param bigNum1 数字1
   * @param bigNum2 数字2
   * @return 是否大于
   */
  public static boolean greater(BigDecimal bigNum1, BigDecimal bigNum2) {
    return bigNum1.compareTo(bigNum2) > 0;
  }

  /**
   * 比较大小，参数1大于等于参数2 返回true
   *
   * @param bigNum1 数字1
   * @param bigNum2 数字2
   * @return 是否大于等于
   */
  public static boolean greaterOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
    return bigNum1.compareTo(bigNum2) >= 0;
  }

  /**
   * 比较大小，参数1小于参数2 返回true
   *
   * @param bigNum1 数字1
   * @param bigNum2 数字2
   * @return 是否小于
   */
  public static boolean less(BigDecimal bigNum1, BigDecimal bigNum2) {
    return bigNum1.compareTo(bigNum2) < 0;
  }

  /**
   * 比较大小，参数1小于等于参数2 返回true
   *
   * @param bigNum1 数字1
   * @param bigNum2 数字2
   * @return 是否小于等于
   */
  public static boolean isLessOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
    return bigNum1.compareTo(bigNum2) <= 0;
  }

  /**
   * 比较大小，值相等 返回true<br>
   * 此方法通过调用{@link BigDecimal#compareTo(BigDecimal)}方法来判断是否相等<br>
   * 此方法判断值相等时忽略精度的，既0.00 == 0
   *
   * @param bigNum1 数字1
   * @param bigNum2 数字2
   * @return 是否相等
   */
  public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
    return 0 == bigNum1.compareTo(bigNum2);
  }

  /**
   * 判断集合个数是否是偶数
   *
   * @param values 集合
   * @return 如果是奇数返回 {@code true}，否则 {@code false}
   */
  public static boolean even(Collection<?> values) {
    return even(values.size());
  }

  /**
   * 判断集合个数是否是偶数
   *
   * @param values 数组
   * @return 如果是奇数返回 {@code true}，否则 {@code false}
   */
  public static boolean even(Object[] values) {
    return even(values.length);
  }

  /**
   * 判断是否是偶数
   *
   * @param v 值
   * @return 如果是奇数返回 {@code true}，否则 {@code false}
   */
  public static boolean even(int v) {
    return v % 2 == 0;
  }

  /**
   * 判断集合个数是否是奇数
   *
   * @param values 集合
   * @return 如果是奇数返回 {@code true}，否则 {@code false}
   */
  public static boolean odd(Collection<?> values) {
    return odd(values.size());
  }

  /**
   * 判断集合个数是否是奇数
   *
   * @param values 数组
   * @return 如果是奇数返回 {@code true}，否则 {@code false}
   */
  public static boolean odd(Object[] values) {
    return odd(values.length);
  }

  /**
   * 判断是否是奇数
   *
   * @param v 值
   * @return 如果是奇数返回 {@code true}，否则 {@code false}
   */
  public static boolean odd(int v) {
    return v % 2 != 0;
  }

  public static boolean positive(int v) {
    return v > 0;
  }

  public static boolean negative(int v) {
    return v < 0;
  }

  public static boolean inside(int v, int s, int e) {
    return v >= s && v < e;
  }

  public static boolean insideClosed(int v, int s, int e) {
    return v >= s && v <= e;
  }

  public static boolean inside(long v, long s, long e) {
    return v >= s && v < e;
  }

  public static boolean insideClosed(long v, long s, long e) {
    return v >= s && v <= e;
  }

  public static boolean inside(double v, double s, double e) {
    return v >= s && v < e;
  }

  public static boolean insideClosed(double v, double s, double e) {
    return v >= s && v <= e;
  }

  public static boolean outside(int v, int u, int l) {
    return v < u || v >= l;
  }

  public static boolean outsideOpened(int v, int u, int l) {
    return v < u || v > l;
  }

  public static boolean outside(long v, long u, long l) {
    return v < u || v >= l;
  }

  public static boolean outsideOpened(long v, long u, long l) {
    return v < u || v > l;
  }

  public static boolean outside(double v, double u, double l) {
    return v < u || v >= l;
  }

  public static boolean outsideOpened(double v, double u, double l) {
    return v < u || v > l;
  }

  /**
   * 在当天的几点之前
   *
   * @param time 几点，不包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean beforeHour(int time) {
    return beforeHour(System.currentTimeMillis(), time);
  }

  /**
   * 在当天的几点之前
   *
   * @param currentTimeMillis 当前时间毫秒数
   * @param time 几点，不包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean beforeHour(long currentTimeMillis, int time) {
    if (time < 0 || time > 24) {
      throw new IllegalArgumentException("The hour range must be 0-24");
    }
    int nowHour = TimeHelper.getHour(currentTimeMillis);
    return nowHour < time;
  }

  /**
   * 在当天小时中几分之前
   *
   * @param time 几分，不包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean beforeMinute(int time) {
    return beforeMinute(System.currentTimeMillis(), time);
  }

  /**
   * 在当天小时中几分之前
   *
   * @param currentTimeMillis 当前时间毫秒数
   * @param time 几分，不包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean beforeMinute(long currentTimeMillis, int time) {
    if (time < 0 || time > 60) {
      throw new IllegalArgumentException("The hour range must be 0-60");
    }
    int nowMinute = TimeHelper.getMinute(currentTimeMillis);
    return nowMinute < time;
  }

  /**
   * 在当天的几点之后
   *
   * @param time 几点，包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean afterHour(int time) {
    return !beforeHour(time);
  }

  /**
   * 在当天的几点之后
   *
   * @param currentTimeMillis 当前时间毫秒数
   * @param time 几点，包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean afterHour(long currentTimeMillis, int time) {
    return !beforeHour(currentTimeMillis, time);
  }

  /**
   * 在当天小时中几分之后
   *
   * @param time 几分，包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean afterMinute(int time) {
    return !beforeMinute(time);
  }

  /**
   * 在当天小时中几分之后
   *
   * @param currentTimeMillis 当前时间毫秒数
   * @param time 几分，包括输入时间
   * @return 如果在几点之前返回 {@code true}否则返回 {@code false}
   */
  public static boolean afterMinute(long currentTimeMillis, int time) {
    return !beforeMinute(currentTimeMillis, time);
  }

  // ---------------------------------------------------------------------------

  public static boolean assignmentCompatible(@Nullable Class<?> src, @Nullable Class<?> dist) {
    if (src == null) {
      return dist == null;
    }
    if (dist == null) {
      return false;
    }
    if (src.equals(dist)) {
      return true;
    }
    if (src.isPrimitive() && dist.isPrimitive()) {
      return false;
    }
    if (src.isPrimitive()) {
      return Cast.wrapperClass(src).isAssignableFrom(dist);
    }
    if (dist.isPrimitive()) {
      return Cast.wrapperClass(dist).isAssignableFrom(src);
    }
    return src.isAssignableFrom(dist);
  }

  public static boolean accessibleClass(@Nonnull Class<?> clz) {
    return Modifier.isPublic(clz.getModifiers());
  }

  /**
   * 是否可访问的
   *
   * @param member 被访问成员
   * @return 如果可以被访问则 {@code true}，否则 {@code false}
   */
  public static boolean accessibleMember(@Nonnull Member member) {
    Class<?> dc = member.getDeclaringClass();
    int dcm = dc.getModifiers();
    if (!Modifier.isPublic(dcm)) {
      return false;
    }
    int m = member.getModifiers();
    return Modifier.isPublic(m);
  }

  public static boolean arrayObj(Object obj) {
    if (obj == null) {
      return false;
    }
    return arrayType(obj.getClass());
  }

  public static boolean basicObj(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof Class) {
      return basicType((Class<?>) obj);
    }
    return obj instanceof String
        || basicNumberObj(obj)
        || obj instanceof Boolean
        || obj instanceof Character;
  }

  public static boolean basicNumberObj(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof Class) {
      return basicNumberType((Class<?>) obj);
    }
    return intObj(obj)
        || doubleObj(obj)
        || longObj(obj)
        || floatObj(obj)
        || shortObj(obj)
        || byteObj(obj);
  }

  public static boolean boolObj(Object obj) {
    return obj instanceof Boolean;
  }

  public static boolean charObj(Object obj) {
    return obj instanceof Character;
  }

  public static boolean byteObj(Object obj) {
    return obj instanceof Byte;
  }

  public static boolean shortObj(Object obj) {
    return obj instanceof Short;
  }

  public static boolean intObj(Object obj) {
    return obj instanceof Integer;
  }

  public static boolean longObj(Object obj) {
    return obj instanceof Long;
  }

  public static boolean floatObj(Object obj) {
    return obj instanceof Float;
  }

  public static boolean doubleObj(Object obj) {
    return obj instanceof Double;
  }

  public static boolean arrayType(Class<?> clz) {
    return clz.isArray();
  }

  public static boolean basicType(Class<?> clz) {
    return String.class == clz || basicNumberType(clz) || boolType(clz) || charType(clz);
  }

  public static boolean basicNumberType(Class<?> clz) {
    return intType(clz)
        || doubleType(clz)
        || longType(clz)
        || floatType(clz)
        || shortType(clz)
        || byteType(clz);
  }

  public static boolean boolType(Class<?> clz) {
    return boolean.class == clz || Boolean.class == clz;
  }

  public static boolean charType(Class<?> clz) {
    return char.class == clz || Character.class == clz;
  }

  public static boolean byteType(Class<?> clz) {
    return byte.class == clz || Byte.class == clz;
  }

  public static boolean shortType(Class<?> clz) {
    return short.class == clz || Short.class == clz;
  }

  public static boolean intType(Class<?> clz) {
    return int.class == clz || Integer.class == clz;
  }

  public static boolean longType(Class<?> clz) {
    return long.class == clz || Long.class == clz;
  }

  public static boolean floatType(Class<?> clz) {
    return float.class == clz || Float.class == clz;
  }

  public static boolean doubleType(Class<?> clz) {
    return double.class == clz || Double.class == clz;
  }

  public static boolean interfaceClass(Class<?> clz) {
    return clz.isInterface();
  }

  public static boolean publicInterfaceClass(Class<?> clz) {
    if (clz.isInterface()) {
      return Is.publicClass(clz);
    }
    return false;
  }

  public static boolean publicClass(Class<?> clz) {
    int modifiers = clz.getModifiers();
    return Modifier.isPublic(modifiers);
  }

  // ---------------------------------------------------------------------------
  /**
   * 是否是公开静态常量
   *
   * @param field 属性域
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean publicStaticVariables(Field field) {
    int modifiers = field.getModifiers();
    return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers);
  }

  /**
   * 是否是公开静态常量
   *
   * @param field 属性域
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean publicStaticConstants(Field field) {
    int modifiers = field.getModifiers();
    return Modifier.isPublic(modifiers)
        && Modifier.isStatic(modifiers)
        && Modifier.isFinal(modifiers);
  }

  // ---------------------------------------------------------------------------

  /**
   * 是否是路径分隔符 {@code \}或 {@code /}
   *
   * @param c 字符
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean pathDelimiter(char c) {
    return c == '/' || c == '\\';
  }

  /**
   * 是否是路径分隔符 {@code \}或 {@code /}
   *
   * @param c1 字符
   * @param c2 字符
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean pathDelimiter(char c1, char c2) {
    return (c1 == '/' || c1 == '\\') && (c2 == '/' || c2 == '\\');
  }

  /**
   * 判断url是否是http资源
   *
   * @param url url
   * @return 是否http
   */
  public static boolean httpUrl(URL url) {
    return "http".equalsIgnoreCase(url.getProtocol());
  }

  /**
   * 判断url是否是http资源
   *
   * @param url url
   * @return 是否http
   */
  public static boolean httpsUrl(URL url) {
    return "https".equalsIgnoreCase(url.getProtocol());
  }

  /**
   * 判断url是否是http资源
   *
   * @param url url
   * @return 是否http
   */
  public static boolean fileUrl(URL url) {
    return "file".equalsIgnoreCase(url.getProtocol());
  }

  /**
   * 判断url是否是ftp资源
   *
   * @param url url
   * @return 是否ftp
   */
  public static boolean ftpUrl(URL url) {
    return "ftp".equalsIgnoreCase(url.getProtocol());
  }
}
