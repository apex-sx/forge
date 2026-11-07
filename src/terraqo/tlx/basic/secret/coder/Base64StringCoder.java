package pxf.toolkit.basic.secret.coder;

import java.nio.charset.Charset;

/**
 * Base64字符串编码
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public class Base64StringCoder extends AbstractStringCoder {

  private final Base64BinaryCoder base64BinaryCoder;

  public Base64StringCoder() {
    this(Base64Type.STANDARD);
  }

  public Base64StringCoder(Base64Type base64Type) {
    this.base64BinaryCoder = new Base64BinaryCoder(base64Type);
  }

  public Base64StringCoder(Base64BinaryCoder base64BinaryCoder) {
    this.base64BinaryCoder = base64BinaryCoder;
  }

  @Override
  protected String doDecode(String source, Charset charset) throws Exception {
    return new String(this.base64BinaryCoder.decode(source.getBytes(charset)), charset);
  }

  @Override
  protected String doEncode(String source, Charset charset) throws Exception {
    return new String(this.base64BinaryCoder.encode(source.getBytes(charset)), charset);
  }
}
