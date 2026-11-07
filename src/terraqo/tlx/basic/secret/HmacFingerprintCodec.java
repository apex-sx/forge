package pxf.toolkit.basic.secret;

import java.nio.ByteBuffer;
import javax.crypto.Mac;

/**
 * 指纹HMAC加密编码
 *
 * @author potatoxf
 * @date 2021/03/12
 */
class HmacFingerprintCodec implements FingerprintCodec {

  private final Mac mac;

  public HmacFingerprintCodec(Mac mac) {
    this.mac = mac;
  }

  /**
   * 更新加密数据
   *
   * @param input 输入
   */
  @Override
  public void update(byte[] input, int offset, int len) {
    mac.update(input, offset, len);
  }

  /**
   * 更新加密数据
   *
   * @param input 输入
   */
  @Override
  public void update(ByteBuffer input) {
    mac.update(input);
  }

  /** 重置 */
  @Override
  public void reset() {
    mac.reset();
  }

  /**
   * 完成
   *
   * @return 返回指纹结果
   */
  @Override
  public byte[] finish() {
    return mac.doFinal();
  }
}
