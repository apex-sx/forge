package pxf.toolkit.basic.secret.coder;

import java.util.Base64;

/**
 * Base64二进制编码
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class Base64BinaryCoder extends AbstractBinaryCoder {

  private final Base64.Encoder encoder;
  private final Base64.Decoder decoder;

  public Base64BinaryCoder() {
    this(Base64Type.STANDARD);
  }

  public Base64BinaryCoder(Base64Type base64Type) {
    switch (base64Type) {
      case URL_SAFE:
        this.encoder = Base64.getUrlEncoder();
        this.decoder = Base64.getUrlDecoder();
        break;
      case MIME_SAFE:
        this.encoder = Base64.getMimeEncoder();
        this.decoder = Base64.getMimeDecoder();
        break;
      default:
        this.encoder = Base64.getEncoder();
        this.decoder = Base64.getDecoder();
        break;
    }
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
    return this.decoder.decode(source);
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
    return this.encoder.encode(source);
  }
}
