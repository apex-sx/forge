package pxf.toolkit.basic.secret.coder;

/**
 * 编码器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface Encoder<O, T> {

  /**
   * 编码
   *
   * @param source 输入源
   * @return 编码后的值
   * @throws EncoderException 编码异常
   */
  T encode(O source) throws EncoderException;
}
