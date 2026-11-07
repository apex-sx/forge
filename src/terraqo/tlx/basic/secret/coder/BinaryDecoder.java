package pxf.toolkit.basic.secret.coder;

/**
 * 二进制解码器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface BinaryDecoder extends Decoder<byte[], byte[]> {

  /**
   * 解码
   *
   * @param source 二进制输入源
   * @return 解码后的值二进制
   * @throws DecoderException 解码异常
   */
  @Override
  byte[] decode(byte[] source) throws DecoderException;
}
