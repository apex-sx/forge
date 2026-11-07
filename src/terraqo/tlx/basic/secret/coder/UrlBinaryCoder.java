package pxf.toolkit.basic.secret.coder;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;
import pxf.toolkit.basic.secret.CodecHelper;

/**
 * URL二进制编码器
 *
 * <p>该类是线程安全的，因为它没有属性，都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class UrlBinaryCoder extends AbstractBinaryCoder {

  /**
   * 获取URL安全字符集
   *
   * @return {@code BitSet}
   */
  private static BitSet getUrlSafeCharBitSet() {
    BitSet safeChars = new BitSet(256);
    // alpha characters
    for (int i = 'a'; i <= 'z'; i++) {
      safeChars.set(i);
    }
    for (int i = 'A'; i <= 'Z'; i++) {
      safeChars.set(i);
    }
    // numeric characters
    for (int i = '0'; i <= '9'; i++) {
      safeChars.set(i);
    }
    // special chars
    safeChars.set('-');
    safeChars.set('_');
    safeChars.set('.');
    safeChars.set('*');
    // blank to be replaced with +
    safeChars.set(' ');
    return safeChars;
  }

  /**
   * 解码
   *
   * @param source 二进制输入源
   * @return 解码后的值二进制
   * @throws Exception 任何异常
   */
  @Override
  protected byte[] doDecode(byte[] source) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (int i = 0; i < source.length; i++) {
      int b = source[i];
      if (b == '+') {
        buffer.write(' ');
      } else if (b == '%') {
        try {
          byte v = CodecHelper.hexToDigit(source[++i], source[++i]);
          buffer.write(v);
        } catch (ArrayIndexOutOfBoundsException e) {
          throw new DecoderException("Invalid URL encoding: ", e);
        }
      } else {
        buffer.write(b);
      }
    }
    return buffer.toByteArray();
  }

  /**
   * 编码
   *
   * @param source 二进制输入源
   * @return 编码后的值二进制
   * @throws Exception 任何异常
   */
  @Override
  protected byte[] doEncode(byte[] source) throws Exception {
    BitSet WWW_FORM_URL_SAFE = getUrlSafeCharBitSet();
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (byte c : source) {
      int b = c;
      if (b < 0) {
        b = 256 + b;
      }
      if (WWW_FORM_URL_SAFE.get(b)) {
        if (b == ' ') {
          b = '+';
        }
        buffer.write(b);
      } else {
        buffer.write('%');
        char[] chars = CodecHelper.digitToHex(b);
        for (char aChar : chars) {
          buffer.write(aChar);
        }
      }
    }
    return buffer.toByteArray();
  }
}
