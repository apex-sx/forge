package pxf.toolkit.basic.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分隔操作
 *
 * @author potatoxf
 * @date 2021/3/14
 */
public final class Split {

  /**
   * 简单切分字符串，不进行其它操作
   *
   * @param input 输入字符串
   * @param splitChar 切分字符
   * @return 返回切分后的字符串数组
   */
  public static String[] simpleString(String input, String splitChar) {
    if (input == null) {
      return Empty.STRING_ARRAY;
    }
    int length = input.length();
    if (length == 0) {
      return Empty.STRING_ARRAY;
    }
    if (splitChar == null) {
      return new String[] {input};
    }
    int scLength = splitChar.length();
    if (scLength == 0) {
      return new String[] {input};
    }
    int count = 1;
    int si = 0, i = 0;
    List<String> list = new ArrayList<>();
    while ((i = input.indexOf(splitChar, si)) != -1) {
      list.add(input.substring(si, i));
      si = i + scLength;
    }
    list.add(input.substring(si));
    return list.toArray(String[]::new);
  }
}
