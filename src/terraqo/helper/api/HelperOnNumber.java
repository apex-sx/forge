package potatoxf.helper.api;

import potatoxf.helper.api.lang.AsciiTableMatcher;

import java.util.Map;

/**
 * 数字助手类
 *
 * @author potatoxf
 * @date 2021/7/24
 */
public final class HelperOnNumber {

  private static final int[] ARABIAN_ROMAN_NUMBERS =
      new int[] {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
  private static final String[] ROMAN_NUMBERS =
      new String[] {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
  private static final Map<Character, Integer> ROMAN_NUMBERS_MAPPING =
      HelperOnCollection.ofMap('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);

  private HelperOnNumber() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * Compares two {@code int} values numerically. The value returned is identical to what would be
   * returned by:
   *
   * <pre>
   *    Integer.valueOf(x).compareTo(Integer.valueOf(y))
   * </pre>
   *
   * @param x the first {@code int} to compare
   * @param y the second {@code int} to compare
   * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y};
   *     and a value greater than {@code 0} if {@code x > y}
   */
  public static int compare(int x, int y) {
    return Integer.compare(x, y);
  }

  /**
   * Compares two {@code int} values numerically treating the values as unsigned.
   *
   * @param x the first {@code int} to compare
   * @param y the second {@code int} to compare
   * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y} as
   *     unsigned values; and a value greater than {@code 0} if {@code x > y} as unsigned values
   */
  public static int compareUnsigned(int x, int y) {
    return compare(x + Integer.MIN_VALUE, y + Integer.MIN_VALUE);
  }

  /**
   * Returns a power of two size for the given target number.
   *
   * @param num target number
   * @param max max number,must be greater 0
   */
  public static int floorBinaryMultiple(int num, int max) {
    int n = num - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= max && max > 0) ? max : n + 1;
  }

  /**
   * @param length
   * @param block
   * @return
   */
  public static int extendBinaryMultiple(int length, int block) {
    if (length > block) {
      return (length / block + 1) * floorBinaryMultiple(block, Integer.MAX_VALUE);
    } else {
      return floorBinaryMultiple(block, Integer.MAX_VALUE);
    }
  }

  /**
   * Find the number of digits in a number.
   *
   * @param input number to find
   * @return number of digits of given number
   */
  public static int numberOfDigits(Integer input) {
    if (input == null) {
      return 0;
    }
    long number = input;
    int digits = 0;
    do {
      digits++;
      number /= 10;
    } while (number != 0);
    return digits;
  }

  /**
   * Find the number of digits in a number.
   *
   * @param input number to find
   * @return number of digits of given number
   */
  public static int numberOfDigits(Long input) {
    if (input == null) {
      return 0;
    }
    long number = input;
    int digits = 0;
    do {
      digits++;
      number /= 10;
    } while (number != 0);
    return digits;
  }

  /**
   * Converting Integers into Roman Numerals
   *
   * <p>('I', 1); ('IV',4); ('V', 5); ('IV',9); ('X', 10); ('XL',40; ('L', 50); ('XC',90); ('C',
   * 100); ('D', 500); ('M', 1000);
   *
   * @param number number
   * @return Roman Numerals
   */
  public static String intToRoman(int number) {
    if (number <= 0) {
      return "";
    }

    StringBuilder builder = new StringBuilder();

    for (int a = 0; a < ARABIAN_ROMAN_NUMBERS.length; a++) {
      int times = number / ARABIAN_ROMAN_NUMBERS[a];
      for (int b = 0; b < times; b++) {
        builder.append(ROMAN_NUMBERS[a]);
      }

      number -= times * ARABIAN_ROMAN_NUMBERS[a];
    }

    return builder.toString();
  }

  /**
   * This function convert Roman number into Integer
   *
   * @param number Roman number string
   * @return integer
   */
  public static int romanToInt(String number) {
    number = number.toUpperCase();
    char prev = ' ';

    int sum = 0;

    int newPrev = 0;
    for (int i = number.length() - 1; i >= 0; i--) {
      char c = number.charAt(i);
      Integer currentNum = ROMAN_NUMBERS_MAPPING.get(c);

      if (currentNum == null) {
        throw new IllegalArgumentException("Contains wrong characters [" + c + "]");
      }
      if (prev != ' ') {
        // checking current Number greater then previous or not
        newPrev =
            ROMAN_NUMBERS_MAPPING.get(prev) > newPrev ? ROMAN_NUMBERS_MAPPING.get(prev) : newPrev;
      }

      // if current number greater then prev max previous then add
      if (currentNum >= newPrev) {
        sum += currentNum;
      } else {
        // subtract upcoming number until upcoming number not greater then prev max
        sum -= currentNum;
      }

      prev = c;
    }

    return sum;
  }

  // ---------------------------------------------------------------------------

  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param high 高位字符
   * @param low 低位字符
   * @return 返回转换后的数字
   */
  public static byte hexToDigit(byte high, byte low) {
    return hexToDigit((char) high, (char) low);
  }

  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param high 高位字符
   * @param low 低位字符
   * @return 返回转换后的数字
   */
  public static byte hexToDigit(char high, char low) {
    int r = hexToDigit(high) << 4;
    r = r | hexToDigit(low);
    return (byte) r;
  }

  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param b 字符
   * @return 返回转换后的数字
   */
  public static int hexToDigit(byte b) {
    return hexToDigit((char) b);
  }

  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param c 字符
   * @return 返回转换后的数字
   */
  public static int hexToDigit(char c) {
    int i = Character.digit(c, 16);
    if (i == -1) {
      throw new IllegalArgumentException("Invalid digit (radix 16): " + c);
    }
    return i;
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @return 返回16进制 {@code 0-9A-F}字符，长度为2
   */
  public static char[] digitToHex(byte value) {
    return digitToHex(value, false);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @return 返回16进制 {@code 0-9A-F}字符，长度为2
   */
  public static char[] digitToHex(byte value, boolean isLowFirst) {
    return digitToHex(value, isLowFirst, true);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @param isUpper 是否大写
   * @return 返回16进制 {@code 0-9A-F}字符，长度为2
   */
  public static char[] digitToHex(byte value, boolean isLowFirst, boolean isUpper) {
    return digitToHex(value, 2, isLowFirst, isUpper);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @return 返回16进制 {@code 0-9A-F}字符，长度为4
   */
  public static char[] digitToHex(short value) {
    return digitToHex(value, false);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @return 返回16进制 {@code 0-9A-F}字符，长度为4
   */
  public static char[] digitToHex(short value, boolean isLowFirst) {
    return digitToHex(value, isLowFirst, true);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @param isUpper 是否大写
   * @return 返回16进制 {@code 0-9A-F}字符，长度为4
   */
  public static char[] digitToHex(short value, boolean isLowFirst, boolean isUpper) {
    return digitToHex(value, 4, isLowFirst, isUpper);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @return 返回16进制 {@code 0-9A-F}字符，长度为8
   */
  public static char[] digitToHex(int value) {
    return digitToHex(value, false);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @return 返回16进制 {@code 0-9A-F}字符，长度为8
   */
  public static char[] digitToHex(int value, boolean isLowFirst) {
    return digitToHex(value, isLowFirst, true);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @param isUpper 是否大写
   * @return 返回16进制 {@code 0-9A-F}字符，长度为8
   */
  public static char[] digitToHex(int value, boolean isLowFirst, boolean isUpper) {
    return digitToHex(value, 8, isLowFirst, isUpper);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @return 返回16进制 {@code 0-9A-F}字符，长度为16
   */
  public static char[] digitToHex(long value) {
    return digitToHex(value, false);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @return 返回16进制 {@code 0-9A-F}字符，长度为16
   */
  public static char[] digitToHex(long value, boolean isLowFirst) {
    return digitToHex(value, isLowFirst, true);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param isLowFirst 是否低位在前
   * @param isUpper 是否大写
   * @return 返回16进制 {@code 0-9A-F}字符，长度为16
   */
  public static char[] digitToHex(long value, boolean isLowFirst, boolean isUpper) {
    return digitToHex(value, 16, isLowFirst, isUpper);
  }

  /**
   * 将 {@code 0-16}数字转为16进制 {@code 0-9A-F}字符
   *
   * @param value 值
   * @param halfByteCount 值的4位统计个数
   * @param isLowFirst 是否低位在前
   * @param isUpper 是否大写
   * @return 返回16进制 {@code 0-9A-F}字符
   */
  private static char[] digitToHex(
      long value, int halfByteCount, boolean isLowFirst, boolean isUpper) {
    if (halfByteCount < 0 || halfByteCount > 16) {
      throw new IllegalArgumentException(
          "The half byte count must from 1 to 16 with max half byte for 64bit");
    }
    char[] results = new char[halfByteCount];
    long low4Bit = 15L;
    for (int i = 0; i < halfByteCount; i++) {
      int offset = i * 4;
      int l = (int) ((value >> offset) & low4Bit);
      int destIndex = !isLowFirst ? halfByteCount - i - 1 : i;
      results[destIndex] = Character.forDigit(l, 16);
      if (isUpper) {
        results[destIndex] = Character.toUpperCase(results[destIndex]);
      }
    }
    return results;
  }

  /**
   * 获取带有小数的数字
   *
   * @param input 输入字符串
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  public static String extractDecimalNumber(String input, String defaultValue) {
    return extractDecimalNumber(input, 1, defaultValue);
  }

  /**
   * 提取提取带有小数的数字
   *
   * @param input 输入字符串
   * @param findCount 提取第几个数字
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  public static String extractDecimalNumber(String input, int findCount, String defaultValue) {
    return extractNumber(input, findCount, true, defaultValue);
  }

  /**
   * 提取整数
   *
   * @param input 输入字符串
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  public static String extractIntegerNumber(String input, String defaultValue) {
    return extractIntegerNumber(input, 1, defaultValue);
  }

  /**
   * 提取整数
   *
   * @param input 输入字符串
   * @param findCount 提取第几个数字
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  public static String extractIntegerNumber(String input, int findCount, String defaultValue) {
    return extractNumber(input, findCount, false, defaultValue);
  }

  /**
   * 提取数字
   *
   * @param input 输入字符串
   * @param findCount 提取第几个数字
   * @param defaultValue 找不到时的默认值
   * @return 数字字符串
   */
  private static String extractNumber(
      String input, int findCount, boolean isDecimal, String defaultValue) {
    if (findCount <= 0) {
      throw new IllegalArgumentException("The find count must be greater 0");
    }
    if (Whether.empty(input)) {
      return defaultValue;
    }
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
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Character#compare(char, char)
   */
  public static int compareValue(char x, char y) {
    return x - y;
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Double#compare(double, double)
   */
  public static int compareValue(double x, double y) {
    return Double.compare(x, y);
  }

  public static String showNumber(long number, int length, char pad) {
    int len = HelperOnNumber.numberOfDigits(number);
    if (len >= length) {
      return String.valueOf(number);
    }
    StringBuilder stringBuilder = new StringBuilder(length);
    for (int i = 0; i < length - len; i++) {
      stringBuilder.append(pad);
    }
    stringBuilder.append(number);
    return stringBuilder.toString();
  }
}
