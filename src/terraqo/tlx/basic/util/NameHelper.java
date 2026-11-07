package pxf.toolkit.basic.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.lang.AsciiTableMatcher;
import pxf.toolkit.basic.lang.CharStack;
import pxf.toolkit.basic.lang.ExpandStringBuilder;
import pxf.toolkit.basic.util.Assert;

/**
 * 名称操作类
 *
 * @author potatoxf
 * @date 2021/5/22
 */
@ThreadSafe
public final class NameHelper {

  private static final int LOCALE_PART_LENGTH = 2;

  /**
   * 将给定的{@code String}表示形式解析为{@link Locale}，格式如下：
   *
   * <ul>
   *   <li>en
   *   <li>en_US
   *   <li>en US
   *   <li>en-US
   * </ul>
   *
   * @param localeName 语言环境名称
   * @return 相应的{@code Locale}实例，如果没有，则为{@code null}
   * @throws IllegalArgumentException 如果在无效的语言环境规范的情况下
   */
  @Nullable
  public static Locale parseLocaleString(String localeName) {

    Assert.correctLocale(localeName);
    StringTokenizer st = new StringTokenizer(localeName, "_ ");
    List<String> tokens = new LinkedList<>();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      tokens.add(token);
    }
    int size = tokens.size();
    if (size == 1) {
      Locale resolved = Locale.forLanguageTag(tokens.get(0));
      if (!resolved.getLanguage().isEmpty()) {
        return resolved;
      }
    }
    String language = (size > 0 ? tokens.get(0) : "");
    String country = (size > 1 ? tokens.get(1) : "");
    String variant = "";
    if (size > LOCALE_PART_LENGTH) {
      // There is definitely a variant, and it is everything after the country
      // code sans the separator between the country code and the variant.
      int endIndexOfCountryCode = localeName.indexOf(country, language.length()) + country.length();
      // Strip off any leading '_' and whitespace, what's left is the variant.
      variant = localeName.substring(endIndexOfCountryCode).trim();
      int index = 0;
      for (int i = 0; i < variant.length(); i++) {
        if (variant.charAt(i) == '_') {
          index++;
        } else {
          break;
        }
      }
      variant = variant.substring(index);
    }
    if (variant.isEmpty() && country.startsWith("#")) {
      variant = country;
      country = "";
    }
    return (language.length() > 0 ? new Locale(language, country, variant) : null);
  }

  /**
   * 驼峰命名
   *
   * @param string 待处理字符串
   * @param isHeadLower 是否首字符小写
   * @return 驼峰式的字符串
   */
  @Nonnull
  public static String camelCase(@Nonnull CharSequence string, boolean isHeadLower) {

    List<String> keyWord = findKeyWord(string, false, false);
    StringBuilder sb = new StringBuilder(string.length() + 10);
    for (String key : keyWord) {
      sb.append(capitalize(key, false));
    }
    return capitalize(sb, isHeadLower).toString();
  }

  /**
   * 下划线命名
   *
   * @param string 待处理字符串
   * @param isUpper 是否大写
   * @return 下划线式的字符串
   */
  @Nonnull
  public static String underScoreCase(@Nonnull CharSequence string, boolean isUpper) {

    return mergeByDelimiter(string, isUpper, '_');
  }

  /**
   * 短横杠命名
   *
   * @param string 待处理字符串
   * @param isUpper 是否大写
   * @return 短横杠式的字符串
   */
  @Nonnull
  public static String kebabCase(@Nonnull CharSequence string, boolean isUpper) {

    return mergeByDelimiter(string, isUpper, '-');
  }

  /**
   * 转换首字符大写
   *
   * @param charSequence 字符序列
   * @param reserve 是否反转，当为 {@code true}时首字符转小写
   * @return {@code CharSequence}
   */
  public static CharSequence capitalize(@Nonnull CharSequence charSequence, boolean reserve) {

    int length = charSequence.length();
    if (length == 0) {
      return charSequence;
    }
    String str = charSequence.toString();
    if (length == 1) {
      return reserve ? str.toLowerCase() : str.toUpperCase();
    }
    String h = str.substring(0, 1);
    return (reserve ? h.toLowerCase() : h.toUpperCase()) + str.substring(1);
  }

  /**
   * 实现找单词或单词数字形式的字符串
   *
   * <p>注意： 1.如果非ascii会抛出异常 2.它会将非Java关键字的字符当做分割符 3.多个分割符只算一个分割符 4.Java关键字字符 {@code _}当做分割符
   * 5.Java关键字字符 {@code $}当做前缀符 6.单词与单词之间通过单词首字符大写来分割 7.全大写专有名词与单词之间通过单词首字符大写来分割 8.数字跟在字符或 {@code
   * $}后，如果数字前是分割符，则单独在一起 例如： 当 {@code isSplitNumber}为 {@code true}时
   *
   * <ul>
   *   <li><span>HTTPUtils123</span> <span>http utils 123</span>
   *   <li><span>TestTable</span> <span>test table</span>
   *   <li><span>X123TestTable</span> <span>x 123 test table</span>
   *   <li><span>TEST_TABLE_123</span> <span>test table 123</span>
   *   <li><span>TEST_TABLE123</span> <span>test table 123</span>
   *   <li><span>TEST_-_123TABLE</span> <span>test 123 table</span>
   * </ul>
   *
   * 当 {@code isSplitNumber}为 {@code false}时
   *
   * <ul>
   *   <li><span>HTTPUtils123</span> <span>http utils123</span>
   *   <li><span>TestTable</span> <span>test table</span>
   *   <li><span>X509TrustManager</span> <span>x509 trust manager</span>
   *   <li><span>TEST_TABLE_123</span> <span>test table 123</span>
   *   <li><span>TEST_TABLE123</span> <span>test table123</span>
   *   <li><span>TEST_-_123TABLE</span> <span>test 123 table</span>
   * </ul>
   *
   * @param ws 单词序列字符串
   * @param isSplitNumber 是否需要分割数字
   * @param isUpper 单词是否大写
   * @return 单词或单词数字列表
   * @throws java.lang.IllegalArgumentException 当输入字符串为 {@code null}，或者字符串里包含非Ascii吗则抛出该异常
   */
  @Nonnull
  public static List<String> findKeyWord(
      @Nonnull CharSequence ws, final boolean isSplitNumber, final boolean isUpper) {

    Assert.noEmpty(ws, "]word sequence");
    int len = ws.length();
    List<String> results = new LinkedList<>();
    CharStack stack = new CharStack();
    // 当不分割数字时一直为0
    int countNumber = 0;
    int countUpperLetter = 0;
    int countDollar = 0;
    for (int i = 0; i < len; i++) {
      char c = ws.charAt(i);
      if (!AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.JAVA_KEYWORD_CHAR)) {
        if (!AsciiTableMatcher.isAsciiChar(c)) {
          throw new IllegalArgumentException("Non-Ascii codes are not allowed");
        }
        addKeyWord(stack, results, isUpper);
        continue;
      }
      // 遇到了需要分割的数字
      if (Character.isDigit(c)) {
        // 是否要分割数字
        if (isSplitNumber) {
          if (countNumber == 0) {
            addKeyWord(stack, results, isUpper);
          }
          countNumber++;
        }
        stack.push(c);
      } else if (Character.isLetter(c) || c == '$') {
        if (countNumber > 0) {
          addKeyWord(stack, results, isUpper);
          countNumber = 0;
        }
        if (c == '$') {
          // 只有在第一个$时分割
          if (countDollar == 0) {
            addKeyWord(stack, results, isUpper);
            countUpperLetter = 0;
          }
          countDollar++;
          stack.push(c);
          continue;
        }
        if (stack.isEmpty()) {
          stack.push(c);
          countUpperLetter = countUpperLetter(c, countUpperLetter);
        } else {
          char top = stack.peek();
          // 前面是数字需要分割
          if (Character.isDigit(top)) {
            addKeyWord(stack, results, isUpper);
          }
          if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.UPPER_LETTER)) {
            if (countUpperLetter == 0 && countDollar == 0) {
              addKeyWord(stack, results, isUpper);
            }
            countUpperLetter = countUpperLetter(c, countUpperLetter);
          } else {
            // 只会出现{@code '$'} {@code '大写字符'}的情况，并且是 {@code '$'} {@code '大写字符'}是多个
            if (countUpperLetter > 1) {
              top = stack.pop();
              if (countDollar == 0 || stack.peek() != '$') {
                addKeyWord(stack, results, isUpper);
                stack.push(top);
              } else {
                stack.push(top);
                addKeyWord(stack, results, isUpper);
              }
            }
            countDollar = 0;
            countUpperLetter = 0;
          }
          stack.push(c);
        }
      } else {
        addKeyWord(stack, results, isUpper);
      }
    }
    addKeyWord(stack, results, isUpper);
    return results;
  }

  /** 如果值栈不为空的话，添加关键字到容器中，并清空值栈 */
  private static void addKeyWord(CharStack charStack, List<String> container, boolean isUpper) {

    if (charStack.isEmpty()) {
      return;
    }
    String string = charStack.toString();
    charStack.clear();
    container.add(isUpper ? string.toUpperCase() : string.toLowerCase());
  }

  /** 合并关键单词序列 */
  private static String mergeByDelimiter(
      @Nonnull CharSequence ws, boolean isUpper, char delimiter) {

    List<String> keyWord = findKeyWord(ws, false, isUpper);
    ExpandStringBuilder sb = new ExpandStringBuilder(ws.length() + 10);
    for (String key : keyWord) {
      sb.appendWhenNoEmpty(delimiter).append(key);
    }
    return sb.toString();
  }

  /** 统计大写字符个数 */
  private static int countUpperLetter(char c, int currentCount) {

    if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.UPPER_LETTER)) {
      return currentCount + 1;
    }
    return currentCount;
  }
}
