package pxf.toolkit.basic.secret.coder;

/**
 * 二进制编码器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface BinaryEncoder extends Encoder<byte[], byte[]> {

  /**
   * 编码
   *
   * @param source 二进制输入源
   * @return 编码后的值二进制
   * @throws EncoderException 编码异常
   */
  @Override
  byte[] encode(byte[] source) throws EncoderException;
}
