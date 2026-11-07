package pxf.toolkit.basic.secret.coder;

/**
 * 解码器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface Decoder<O, T> {

  /**
   * 解码
   *
   * @param source 输入源
   * @return 解码后的值
   * @throws DecoderException 解码异常
   */
  T decode(O source) throws DecoderException;
}
