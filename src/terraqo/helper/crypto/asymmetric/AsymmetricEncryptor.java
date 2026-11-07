package potatoxf.helper.crypto.asymmetric;

import potatoxf.helper.HelpString;
import potatoxf.helper.codec.BCD;
import potatoxf.helper.codec.Base64;
import potatoxf.helper.io.IORuntimeException;
import potatoxf.helper.io.IoUtil;
import potatoxf.helper.util.CharsetUtil;
import potatoxf.helper.util.HexUtil;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 非对称加密器接口，提供：
 *
 * <ul>
 *   <li>加密为bytes
 *   <li>加密为Hex(16进制)
 *   <li>加密为Base64
 *   <li>加密为BCD
 * </ul>
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public interface AsymmetricEncryptor {

  /**
   * 加密
   *
   * @param data 被加密的bytes
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return 加密后的bytes
   */
  byte[] encrypt(byte[] data, KeyType keyType);

  /**
   * 编码为Hex字符串
   *
   * @param data 被加密的bytes
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Hex字符串
   */
  default String encryptHex(byte[] data, KeyType keyType) {
    return HexUtil.encodeHexStr(encrypt(data, keyType));
  }

  /**
   * 编码为Base64字符串
   *
   * @param data 被加密的bytes
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Base64字符串
   */
  default String encryptBase64(byte[] data, KeyType keyType) {
    return Base64.encode(encrypt(data, keyType));
  }

  /**
   * 加密
   *
   * @param data 被加密的字符串
   * @param charset 编码
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return 加密后的bytes
   */
  default byte[] encrypt(String data, String charset, KeyType keyType) {
    return encrypt(HelpString.bytes(data, charset), keyType);
  }

  /**
   * 加密
   *
   * @param data 被加密的字符串
   * @param charset 编码
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return 加密后的bytes
   */
  default byte[] encrypt(String data, Charset charset, KeyType keyType) {
    return encrypt(HelpString.bytes(data, charset), keyType);
  }

  /**
   * 加密，使用UTF-8编码
   *
   * @param data 被加密的字符串
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return 加密后的bytes
   */
  default byte[] encrypt(String data, KeyType keyType) {
    return encrypt(HelpString.utf8Bytes(data), keyType);
  }

  /**
   * 编码为Hex字符串
   *
   * @param data 被加密的字符串
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Hex字符串
   */
  default String encryptHex(String data, KeyType keyType) {
    return HexUtil.encodeHexStr(encrypt(data, keyType));
  }

  /**
   * 编码为Hex字符串
   *
   * @param data 被加密的bytes
   * @param charset 编码
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Hex字符串
   */
  default String encryptHex(String data, Charset charset, KeyType keyType) {
    return HexUtil.encodeHexStr(encrypt(data, charset, keyType));
  }

  /**
   * 编码为Base64字符串，使用UTF-8编码
   *
   * @param data 被加密的字符串
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Base64字符串
   */
  default String encryptBase64(String data, KeyType keyType) {
    return Base64.encode(encrypt(data, keyType));
  }

  /**
   * 编码为Base64字符串
   *
   * @param data 被加密的字符串
   * @param charset 编码
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Base64字符串
   */
  default String encryptBase64(String data, Charset charset, KeyType keyType) {
    return Base64.encode(encrypt(data, charset, keyType));
  }

  /**
   * 加密
   *
   * @param data 被加密的数据流
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return 加密后的bytes
   * @throws IORuntimeException IO异常
   */
  default byte[] encrypt(InputStream data, KeyType keyType) throws IORuntimeException {
    return encrypt(IoUtil.readBytes(data), keyType);
  }

  /**
   * 编码为Hex字符串
   *
   * @param data 被加密的数据流
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Hex字符串
   */
  default String encryptHex(InputStream data, KeyType keyType) {
    return HexUtil.encodeHexStr(encrypt(data, keyType));
  }

  /**
   * 编码为Base64字符串
   *
   * @param data 被加密的数据流
   * @param keyType 私钥或公钥 {@link KeyType}
   * @return Base64字符串
   */
  default String encryptBase64(InputStream data, KeyType keyType) {
    return Base64.encode(encrypt(data, keyType));
  }

  /**
   * 分组加密
   *
   * @param data 数据
   * @param keyType 密钥类型
   * @return 加密后的密文
   */
  default String encryptBcd(String data, KeyType keyType) {
    return encryptBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
  }

  /**
   * 分组加密
   *
   * @param data 数据
   * @param keyType 密钥类型
   * @param charset 加密前编码
   * @return 加密后的密文
   */
  default String encryptBcd(String data, KeyType keyType, Charset charset) {
    return BCD.bcdToStr(encrypt(data, charset, keyType));
  }
}
