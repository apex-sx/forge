package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;

/**
 * 字符串编码器
 *
 * <p>该类是线程安全的，因为它没有属性，都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public abstract class AbstractStringCoder implements StringEncoder, StringDecoder {

  @Override
  public String decode(String source) throws DecoderException {
    return decode(source, Charset.defaultCharset());
  }

  /**
   * 解码
   *
   * @param source 字符串输入源
   * @return 解码后的值字符串
   * @throws DecoderException 解码异常
   */
  @Override
  public String decode(String source, Charset charset) throws DecoderException {
    Charset cs = charset == null ? charset : Charset.defaultCharset();
    try {
      return doDecode(source, cs);
    } catch (Exception e) {
      throw new DecoderException(e);
    }
  }

  @Override
  public String encode(String source) throws EncoderException {
    return encode(source, Charset.defaultCharset());
  }

  /**
   * 编码
   *
   * @param source 字符串输入源
   * @return 编码后的值字符串
   * @throws EncoderException 编码异常
   */
  @Override
  public String encode(String source, Charset charset) throws EncoderException {
    Charset cs = charset == null ? charset : Charset.defaultCharset();
    try {
      return doEncode(source, cs);
    } catch (Exception e) {
      throw new EncoderException(e);
    }
  }

  protected abstract String doDecode(String source, Charset charset) throws Exception;

  protected abstract String doEncode(String source, Charset charset) throws Exception;
}
