package pxf.toolkit.basic.secret;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import pxf.toolkit.basic.constants.FindableEnumConstant;
import pxf.toolkit.basic.exception.AbnormalException;

/**
 * HMAC算法
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public enum HmacAlgorithm implements FindableEnumConstant<HmacAlgorithm> {
  /** HMAC算法 */
  MD5("HmacMD5"),
  SHA_1("HmacSHA1"),
  SHA_224("HmacSHA224"),
  SHA_256("HmacSHA256"),
  SHA_384("HmacSHA384"),
  SHA_512("HmacSHA512");
  private final String algorithm;

  HmacAlgorithm(final String algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * 创建{@code DigitalSignatureHandler}
   *
   * @return {@code DigitalSignatureHandler}
   */
  public FingerprintHandler createHandler(byte[] key) throws InvalidKeyException {
    Mac mac = createMac(key);
    return new GenericFingerprintHandler(createCodec(key));
  }

  /**
   * 创建{@code SecretCoder}
   *
   * @param key 键
   * @return {@code SecretCoder}
   * @throws InvalidKeyException 无效键
   */
  public FingerprintCodec createCodec(byte[] key) throws InvalidKeyException {
    return new HmacFingerprintCodec(createMac(key));
  }

  /**
   * 创建{@code Mac}
   *
   * @param key 键
   * @return {@code Mac}
   */
  public Mac createMac(byte[] key) throws InvalidKeyException {
    SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
    Mac macInstance = createMac();
    macInstance.init(keySpec);
    return macInstance;
  }

  /**
   * 创建{@code Mac}
   *
   * @return {@code Mac}
   */
  public Mac createMac() {
    try {
      return Mac.getInstance(algorithm);
    } catch (final NoSuchAlgorithmException ignored) {
      throw new AbnormalException("Impossible");
    }
  }
}
