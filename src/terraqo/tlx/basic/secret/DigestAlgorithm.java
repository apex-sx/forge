package pxf.toolkit.basic.secret;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import pxf.toolkit.basic.constants.FindableEnumConstant;
import pxf.toolkit.basic.exception.AbnormalException;

/**
 * 信息摘要算法
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public enum DigestAlgorithm implements FindableEnumConstant<DigestAlgorithm> {
  /** 信息摘要算法 */
  MD2("MD2"),
  MD5("MD5"),
  SHA_1("SHA-1"),
  SHA_224("SHA-224"),
  SHA_256("SHA-256"),
  SHA_384("SHA-384"),
  SHA_512("SHA-512"),
  SHA_512_224("SHA-512/224"),
  SHA_512_256("SHA-512/256"),
  SHA3_224("SHA3-224"),
  SHA3_256("SHA3-256"),
  SHA3_384("SHA3-384"),
  SHA3_512("SHA3-512");
  private final String name;

  DigestAlgorithm(String name) {
    this.name = name;
  }

  /**
   * 创建{@code DigitalSignatureHandler}
   *
   * @return {@code DigitalSignatureHandler}
   */
  public FingerprintHandler createHandler() {
    return new GenericFingerprintHandler(createCodec());
  }

  /**
   * 创建{@code SecretCoder}
   *
   * @return {@code SecretCoder}
   */
  public FingerprintCodec createCodec() {
    return new MessageDigestFingerprintCodec(createMessageDigest());
  }

  /**
   * 创建{@code MessageDigest}
   *
   * @return {@code MessageDigest}
   */
  public MessageDigest createMessageDigest() {
    try {
      return MessageDigest.getInstance(name);
    } catch (NoSuchAlgorithmException ignored) {
      throw new AbnormalException("Impossible");
    }
  }
}
