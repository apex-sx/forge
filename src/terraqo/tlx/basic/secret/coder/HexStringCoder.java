package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;

/**
 * Hex字符串编码器
 *
 * <p>该类是线程安全的，因为它没有属性，都是瞬时对象
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class HexStringCoder extends AbstractStringCoder {

  private final HexBinaryCoder BINARY_CODEC = new HexBinaryCoder();

  @Override
  protected String doDecode(String source, Charset charset) throws Exception {
    return new String(this.BINARY_CODEC.decode(source.getBytes(charset)), charset);
  }

  @Override
  protected String doEncode(String source, Charset charset) throws Exception {
    return new String(this.BINARY_CODEC.encode(source.getBytes(charset)), charset);
  }
}
