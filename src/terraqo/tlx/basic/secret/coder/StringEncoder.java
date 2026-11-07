package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;

/**
 * 字符串编码器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface StringEncoder extends Encoder<String, String> {

  /**
   * 编码
   *
   * @param source 字符串输入源
   * @return 编码后的值字符串
   * @throws EncoderException 编码异常
   */
  String encode(String source, Charset charset) throws EncoderException;
}
