package pxf.toolkit.basic.secret;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * 指纹加密编码
 *
 * @author potatoxf
 * @date 2021/03/12
 */
class MessageDigestFingerprintCodec implements FingerprintCodec {

  private final MessageDigest messageDigest;

  public MessageDigestFingerprintCodec(MessageDigest messageDigest) {
    this.messageDigest = messageDigest;
  }

  /**
   * 更新加密数据
   *
   * @param input 输入
   */
  @Override
  public void update(byte[] input, int offset, int len) {
    messageDigest.update(input, offset, len);
  }

  /**
   * 更新加密数据
   *
   * @param input 输入
   */
  @Override
  public void update(ByteBuffer input) {
    messageDigest.update(input);
  }

  /** 重置 */
  @Override
  public void reset() {
    messageDigest.reset();
  }

  /**
   * 完成
   *
   * @return 返回指纹结果
   */
  @Override
  public byte[] finish() {
    return messageDigest.digest();
  }
}
