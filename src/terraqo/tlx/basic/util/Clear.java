package pxf.toolkit.basic.util;

/**
 * 清理操作
 *
 * @author potatoxf
 * @date 2021/3/10
 */
public final class Clear {

  private Clear() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 清理字符串构建器
   *
   * @param stringBuilder {@code StringBuilder}
   * @return 返回清理之前的字符串
   */
  public static String stringBuilder(StringBuilder stringBuilder) {
    String result = stringBuilder.toString();
    //    stringBuilder.delete(0, Integer.MAX_VALUE);
    stringBuilder.setLength(0);
    return result;
  }

  /**
   * 清理字符串构建器
   *
   * @param stringBuffer {@code StringBuffer}
   * @return 返回清理之前的字符串
   */
  public static String stringBuffer(StringBuffer stringBuffer) {
    String result = stringBuffer.toString();
    //    stringBuffer.delete(0, Integer.MAX_VALUE);
    stringBuffer.setLength(0);
    return result;
  }

  /**
   * 清除前导 {@code --}，{@code -}字符串
   *
   * @param string 输入字符串
   * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串
   */
  public static String leadingHyphens(String string) {
    return Clear.leading(string, "--", "-");
  }

  /**
   * 清除前导字符串
   *
   * @param string 输入字符串
   * @param prefixes 要删除的前缀
   * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串，如果没有前缀则原样返回
   */
  public static String leading(String string, String... prefixes) {
    if (string == null) {
      return Empty.STRING;
    }
    for (String prefix : prefixes) {
      if (string.startsWith(prefix)) {
        return string.substring(prefix.length());
      }
    }
    return string;
  }

  /**
   * Remove the leading and trailing quotes from <code>string</code>. E.g. if string is '"one two"',
   * then 'one two' is returned.
   *
   * @param string The string from which the leading and trailing quotes should be removed.
   * @return The string without the leading and trailing quotes.
   */
  public static String leadingAndTrailingQuotes(String string) {
    int length = string.length();
    if (length > 1
        && string.startsWith("\"")
        && string.endsWith("\"")
        && string.substring(1, length - 1).indexOf('"') == -1) {
      string = string.substring(1, length - 1);
    }
    return string;
  }
}
