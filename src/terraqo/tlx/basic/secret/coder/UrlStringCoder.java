package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * URL字符串编码器
 *
 * <p>该类是线程安全的，因为它没有属性，都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class UrlStringCoder extends AbstractStringCoder {

  private final UrlBinaryCoder BINARY_CODEC = new UrlBinaryCoder();

  @Override
  protected String doDecode(String str, Charset charset) throws DecoderException {
    return new String(BINARY_CODEC.decode(str.getBytes(StandardCharsets.US_ASCII)), charset);
  }

  @Override
  protected String doEncode(String str, Charset charset) throws EncoderException {
    return new String(BINARY_CODEC.encode(str.getBytes(charset)), StandardCharsets.US_ASCII);
  }
}
