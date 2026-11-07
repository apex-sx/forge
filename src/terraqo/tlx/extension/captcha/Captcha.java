package pxf.toolkit.extension.captcha;

/**
 * 验证码
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public class Captcha {

  private final String text;
  private final String format;
  private final byte[] image;

  Captcha(String text, String format, byte[] image) {
    this.text = text;
    this.format = format;
    this.image = image;
  }

  public String getText() {
    return text;
  }

  public String getFormat() {
    return format;
  }

  public byte[] getImage() {
    return image;
  }
}
