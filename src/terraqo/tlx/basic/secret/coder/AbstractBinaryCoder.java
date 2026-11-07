package pxf.toolkit.basic.secret.coder;

/**
 * 二进制编码器
 *
 * <p>该类是线程安全的，因为它没有属性，都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public abstract class AbstractBinaryCoder implements BinaryEncoder, BinaryDecoder {

  /**
   * 解码
   *
   * @param source 二进制输入源
   * @return 解码后的值二进制
   * @throws DecoderException 解码异常
   */
  @Override
  public byte[] decode(byte[] source) throws DecoderException {
    try {
      return doDecode(source);
    } catch (Exception e) {
      throw new DecoderException(e);
    }
  }

  /**
   * 编码
   *
   * @param source 二进制输入源
   * @return 编码后的值二进制
   * @throws EncoderException 编码异常
   */
  @Override
  public byte[] encode(byte[] source) throws EncoderException {
    try {
      return doEncode(source);
    } catch (Exception e) {
      throw new EncoderException(e);
    }
  }

  /**
   * 解码
   *
   * @param source 二进制输入源
   * @return 解码后的值二进制
   * @throws Exception 任何异常
   */
  protected abstract byte[] doDecode(byte[] source) throws Exception;

  /**
   * 编码
   *
   * @param source 二进制输入源
   * @return 编码后的值二进制
   * @throws Exception 任何异常
   */
  protected abstract byte[] doEncode(byte[] source) throws Exception;
}
