package pxf.toolkit.basic.secret.coder;

import pxf.toolkit.basic.secret.CodecHelper;

/**
 * Hex二进制编码器
 *
 * <p>该类是线程安全的，因为它没有属性，都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class HexBinaryCoder extends AbstractBinaryCoder {

  /**
   * 解码
   *
   * @param source 二进制输入源
   * @return 解码后的值二进制
   * @throws Exception 任何异常
   */
  @Override
  protected byte[] doDecode(byte[] source) throws Exception {
    int len = source.length;
    if (len % 2 != 0) {
      throw new DecoderException("Odd number of characters.");
    }
    byte[] out = new byte[len >> 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; j < len; i++) {
      out[i] = CodecHelper.hexToDigit(source[j++], source[j++]);
    }
    return out;
  }

  /**
   * 编码
   *
   * @param source 二进制输入源
   * @return 编码后的值二进制
   */
  @Override
  protected byte[] doEncode(byte[] source) {
    int len = source.length;
    byte[] out = new byte[len << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < len; i++) {
      char[] chars = CodecHelper.digitToHex(source[i]);
      out[j++] = (byte) chars[0];
      out[j++] = (byte) chars[1];
    }
    return out;
  }
}
