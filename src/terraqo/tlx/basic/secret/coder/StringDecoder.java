package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;

/**
 * 字符串解码器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface StringDecoder extends Decoder<String, String> {

  /**
   * 解码
   *
   * @param source 字符串输入源
   * @return 解码后的值字符串
   * @throws DecoderException 解码异常
   */
  String decode(String source, Charset charset) throws DecoderException;
}
