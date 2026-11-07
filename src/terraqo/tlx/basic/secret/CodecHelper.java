package pxf.toolkit.basic.secret;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import pxf.toolkit.basic.secret.coder.Base64StringCoder;
import pxf.toolkit.basic.secret.coder.Base64Type;
import pxf.toolkit.basic.secret.coder.DecoderException;
import pxf.toolkit.basic.secret.coder.EncoderException;
import pxf.toolkit.basic.secret.coder.HexBinaryCoder;
import pxf.toolkit.basic.secret.coder.HexStringCoder;
import pxf.toolkit.basic.secret.coder.MorseStringCoder;
import pxf.toolkit.basic.secret.coder.UrlBinaryCoder;
import pxf.toolkit.basic.secret.coder.UrlStringCoder;

/**
 * 16进制编码
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public final class CodecHelper {

  private static final MorseStringCoder DEFAULT_STRING_CODEC = new MorseStringCoder();
  private static final HexBinaryCoder BINARY_CODEC = new HexBinaryCoder();
  private static final HexStringCoder STRING_CODEC = new HexStringCoder();
  private static final UrlBinaryCoder URL_BINARY_CODEC = new UrlBinaryCoder();
  private static final UrlStringCoder URL_STRING_CODEC = new UrlStringCoder();
  private static final int RADIX = 16;

  private CodecHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static FingerprintHandler sign(DigestAlgorithm digestAlgorithm) {
    return digestAlgorithm.createHandler();
  }

  public static FingerprintHandler sign(HmacAlgorithm hmacAlgorithm, byte[] key)
      throws InvalidKeyException {
    return hmacAlgorithm.createHandler(key);
  }

  public static String encodeWithBase64(String inputs) throws EncoderException {
    return new Base64StringCoder().encode(inputs);
  }

  public static String decodeWithBase64(String inputs) throws DecoderException {
    return new Base64StringCoder().decode(inputs);
  }

  public static String encodeWithBase64(String inputs, Charset charset) throws EncoderException {
    return new Base64StringCoder().encode(inputs, charset);
  }

  public static String decodeWithBase64(String inputs, Charset charset) throws DecoderException {
    return new Base64StringCoder().decode(inputs, charset);
  }

  public static String encodeWithBase64(Base64Type base64Type, String inputs)
      throws EncoderException {
    return new Base64StringCoder(base64Type).encode(inputs);
  }

  public static String decodeWithBase64(Base64Type base64Type, String inputs)
      throws DecoderException {
    return new Base64StringCoder(base64Type).decode(inputs);
  }

  public static String encodeWithBase64(Base64Type base64Type, String inputs, Charset charset)
      throws EncoderException {
    return new Base64StringCoder(base64Type).encode(inputs, charset);
  }

  public static String decodeWithBase64(Base64Type base64Type, String inputs, Charset charset)
      throws DecoderException {
    return new Base64StringCoder(base64Type).decode(inputs, charset);
  }

  public static String encodeHexString(byte[] inputs) throws EncoderException {
    byte[] results = BINARY_CODEC.encode(inputs);
    return new String(results, StandardCharsets.UTF_8);
  }

  public static byte[] decodeHexString(String inputs) throws DecoderException {
    byte[] results = inputs.getBytes(StandardCharsets.US_ASCII);
    return BINARY_CODEC.decode(results);
  }

  public static String encodeStringHex(String inputs) throws EncoderException {
    return STRING_CODEC.encode(inputs);
  }

  public static String decodeStringHex(String inputs) throws DecoderException {
    return STRING_CODEC.decode(inputs);
  }

  public static String encodeStringHex(String inputs, Charset charset) throws EncoderException {
    return STRING_CODEC.encode(inputs, charset);
  }

  public static String decodeStringHex(String inputs, Charset charset) throws DecoderException {
    return STRING_CODEC.decode(inputs, charset);
  }

  /**
   * 解码
   *
   * @param source 字符串输入源
   * @return 解码后的值字符串
   * @throws DecoderException 解码异常
   */
  public static String decodeUrl(String source) throws DecoderException {
    return URL_STRING_CODEC.decode(source);
  }

  /**
   * 编码
   *
   * @param source 字符串输入源
   * @return 编码后的值字符串
   * @throws EncoderException 编码异常
   */
  public static String encodeUrl(String source) throws EncoderException {
    return URL_STRING_CODEC.encode(source);
  }

  /**
   * 解码
   *
   * @param source 二进制输入源
   * @return 解码后的值二进制
   * @throws DecoderException 解码异常
   */
  public static byte[] decodeUrl(byte[] source) throws DecoderException {
    return URL_BINARY_CODEC.decode(source);
  }

  /**
   * 编码
   *
   * @param source 二进制输入源
   * @return 编码后的值二进制
   * @throws EncoderException 编码异常
   */
  public static byte[] encodeUrl(byte[] source) throws EncoderException {
    return URL_BINARY_CODEC.encode(source);
  }

  /**
   * 将英文转成摩斯密码
   *
   * @param string 字符串
   * @return 摩斯密码
   */
  public static String encodeMorse(String string) throws EncoderException {
    return DEFAULT_STRING_CODEC.encode(string);
  }

  /**
   * 将英文转成摩斯密码
   *
   * @param string 字符串
   * @return 摩斯密码
   */
  public static String decodeMorse(String string) throws DecoderException {
    return DEFAULT_STRING_CODEC.decode(string);
  }

  /**
   * 将英文转成摩斯密码
   *
   * @param string 字符串
   * @param blockSplit 块分割符
   * @param split 分割符
   * @return 摩斯密码
   */
  public static String encodeMorse(String string, String blockSplit, String split)
      throws EncoderException {
    return new MorseStringCoder(blockSplit, split).encode(string);
  }

  /**
   * 将英文转成摩斯密码
   *
   * @param string 字符串
   * @param blockSplit 块分割符
   * @param split 分割符
   * @return 摩斯密码
   */
  public static String decodeMorse(String string, String blockSplit, String split)
      throws DecoderException {
    return new MorseStringCoder(blockSplit, split).decode(string);
  }

  // ---------------------------------------------------------------------------

  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param high 高位字符
   * @param low 低位字符
   * @return 返回转换后的数字
   * @throws DecoderException 如果字符不是16进制数字抛出该异常
   */
  public static byte hexToDigit(byte high, byte low) throws DecoderException {
    return hexToDigit((char) high, (char) low);
  }
  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param high 高位字符
   * @param low 低位字符
   * @return 返回转换后的数字
   * @throws DecoderException 如果字符不是16进制数字抛出该异常
   */
  public static byte hexToDigit(char high, char low) throws DecoderException {
    int r = hexToDigit(high) << 4;
    r = r | hexToDigit(low);
    return (byte) r;
  }
  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param b 字符
   * @return 返回转换后的数字
   * @throws DecoderException 如果字符不是16进制数字抛出该异常
   */
  public static int hexToDigit(byte b) throws DecoderException {
    return hexToDigit((char) b);
  }

  /**
   * 将16进制 {@code 0-9A-F}字符转为 {@code 0-16}数字
   *
   * @param c 字符
   * @return 返回转换后的数字
   * @throws DecoderException 如果字符不是16进制数字抛出该异常
   */
  public static int hexToDigit(char c) throws DecoderException {
    int i = Character.digit(c, RADIX);
    if (i == -1) {
      throw new DecoderException("Invalid digit (radix " + RADIX + "): " + c);
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
    long low4Bit = 0b1111L;
    for (int i = 0; i < halfByteCount; i++) {
      int offset = i * 4;
      int l = (int) ((value >> offset) & low4Bit);
      int destIndex = !isLowFirst ? halfByteCount - i - 1 : i;
      results[destIndex] = Character.forDigit(l, RADIX);
      if (isUpper) {
        results[destIndex] = Character.toUpperCase(results[destIndex]);
      }
    }
    return results;
  }
}
