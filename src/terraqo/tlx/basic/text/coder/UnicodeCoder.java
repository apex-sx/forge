package pxf.toolkit.basic.text.coder;

import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.secret.CodecHelper;
import pxf.toolkit.basic.util.Is;

/**
 * Unicode编码器
 *
 * @author potatoxf
 * @date 2021/5/23
 */
@ThreadSafe
public final class UnicodeCoder {

  private static final int ASCII_LIMIT = 255;
  /** 前缀 */
  private final String prefix;
  /** 前缀长度 */
  private final int prefixLength;
  /** 后缀 */
  private final String suffix;
  /** 后缀长度 */
  private final int suffixLength;

  public UnicodeCoder(String prefix) {
    this(prefix, "");
  }

  public UnicodeCoder(String prefix, String suffix) {
    assert prefix != null && prefix.length() > 0 : "The prefix string must not be empty";
    assert suffix != null : "The suffix string must not be null";
    this.prefix = prefix;
    this.prefixLength = prefix.length();
    this.suffix = suffix;
    this.suffixLength = suffix.length();
  }

  /**
   * 将unicode编码成转义代码
   *
   * @param input 输入字符串
   */
  public void encode(StringBuilder input) {
    char[] cache4 = new char[4];
    char[] cache6 = new char[6];
    for (int i = 0; i < input.length(); i++) {
      int codepoint = input.codePointAt(i);
      if (codepoint <= ASCII_LIMIT) {
        continue;
      }
      input.deleteCharAt(i);
      // len for 8
      char[] chars = CodecHelper.digitToHex(codepoint);
      int l = 0;
      while (l < 4 && chars[l] == '0' && chars[l + 1] == '0') {
        l += 2;
      }
      input.insert(i, prefix);
      i += prefixLength;
      if (l == 2) {
        System.arraycopy(chars, l, cache6, 0, 6);
        input.insert(i, cache6);
        i += 6;
      } else if (l == 4) {
        System.arraycopy(chars, l, cache4, 0, 4);
        input.insert(i, cache4);
        i += 4;
      } else {
        input.insert(i, chars);
        i += 8;
      }
      input.insert(i, suffix);
      i += suffixLength;
      i--;
    }
  }
  /**
   * 将unicode编码成转义代码
   *
   * @param input 输入字符串
   * @return 返回编码后的字符串
   */
  public String encode(String input) {
    StringBuilder sb = new StringBuilder(input.length() * 8);
    char[] cache4 = new char[4];
    char[] cache6 = new char[6];
    for (int i = 0; i < input.length(); i++) {
      int codepoint = input.codePointAt(i);
      if (codepoint <= ASCII_LIMIT) {
        sb.append(Character.toChars(codepoint));
        continue;
      }
      // len for 8
      char[] chars = CodecHelper.digitToHex(codepoint);
      int l = 0;
      while (l < 4 && chars[l] == '0' && chars[l + 1] == '0') {
        l += 2;
      }
      sb.append(prefix);
      if (l == 2) {
        System.arraycopy(chars, l, cache6, 0, 6);
        sb.append(cache6);
      } else if (l == 4) {
        System.arraycopy(chars, l, cache4, 0, 4);
        sb.append(cache4);
      } else {
        sb.append(chars);
      }
      sb.append(suffix);
    }
    return sb.toString();
  }
  /**
   * 将带有unicode转义代码解码
   *
   * @param input 输入字符串
   */
  public void decode(StringBuilder input) {
    if (suffixLength != 0) {
      int off = 0, ssi, hl;
      while ((off = input.indexOf(prefix, off)) >= 0) {
        ssi = input.indexOf(suffix, off);
        if (ssi < 0) {
          break;
        }
        hl = ssi - off - prefixLength;
        if (hl != 4 && hl != 6 && hl != 8) {
          // not unicode
          continue;
        }
        off += replaceUnicodeFromCodepoint(input, off, ssi + suffixLength, off + prefixLength, ssi);
      }
    } else {
      int off = 0, psi, hl;
      while ((off = input.indexOf(prefix, off)) >= 0) {
        psi = off;
        off += prefixLength;
        while (off < input.length() && Is.hexChar(input.codePointAt(off))) {
          off++;
        }
        hl = off - psi - prefixLength;
        if (hl != 4 && hl != 6 && hl != 8) {
          // not unicode
          continue;
        }
        off = psi + replaceUnicodeFromCodepoint(input, psi, off, psi + prefixLength, off);
      }
    }
  }

  /**
   * 将带有unicode转义代码解码
   *
   * @param input 输入字符串
   * @return 返回解码后的字符串
   */
  public String decode(String input) {
    int length = input.length();
    StringBuilder sb = new StringBuilder(length);
    int asi = 0;
    if (suffixLength != 0) {
      int off = 0, ssi;
      while ((off = input.indexOf(prefix, off)) >= 0) {
        sb.append(input, asi, off);
        asi += off;
        off += prefixLength;
        ssi = input.indexOf(suffix, off);
        if (ssi < 0) {
          break;
        }
        int hl = ssi - off;
        if (hl != 4 && hl != 6 && hl != 8) {
          // not unicode
          continue;
        }
        if (appendUnicodeFromCodepoint(input, off, ssi, sb)) {
          // skip
          off = ssi + suffixLength;
          asi = off;
        }
      }
    } else {
      int off = 0, psi;
      while ((off = input.indexOf(prefix, off)) >= 0) {
        sb.append(input, asi, off);
        asi += off;
        psi = off;
        off += prefixLength;
        while (off < length && Is.hexChar(input.codePointAt(off))) {
          off++;
        }
        if (off >= length) {
          break;
        }
        int hl = off - psi - prefixLength;
        if (hl != 4 && hl != 6 && hl != 8) {
          // not unicode
          continue;
        }
        if (appendUnicodeFromCodepoint(input, psi + prefixLength, off, sb)) {
          // skip
          asi = off;
        }
      }
    }
    if (asi != length) {
      sb.append(input, asi, length);
    }
    return sb.toString();
  }

  /**
   * 从码点替换unicode
   *
   * @param input 输入字符串
   * @param deleteStart 删除字符串起始位置
   * @param deleteEnd 删除字符串结束位置
   * @param substrStart 子字符串起始位置
   * @param substrEnd 子字符串结束位置
   * @return 返回更新长度
   */
  private static int replaceUnicodeFromCodepoint(
      StringBuilder input, int deleteStart, int deleteEnd, int substrStart, int substrEnd) {
    try {
      int codepoint = Integer.parseInt(input.substring(substrStart, substrEnd), 16);
      char[] chars = Character.toChars(codepoint);
      input.delete(deleteStart, deleteEnd).insert(deleteStart, chars);
      return chars.length;
    } catch (NumberFormatException ignored) {
    }
    return 0;
  }
  /**
   * 从码点添加unicode
   *
   * @param input 输入字符串
   * @param substrStart 子字符串起始位置
   * @param substrEnd 子字符串结束位置
   * @return 返回是否添加
   */
  private static boolean appendUnicodeFromCodepoint(
      String input, int substrStart, int substrEnd, StringBuilder container) {
    try {
      int codepoint = Integer.parseInt(input.substring(substrStart, substrEnd), 16);
      char[] chars = Character.toChars(codepoint);
      container.append(chars);
      return true;
    } catch (NumberFormatException ignored) {
      return false;
    }
  }
}
