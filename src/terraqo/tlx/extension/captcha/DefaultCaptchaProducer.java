package pxf.toolkit.extension.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.imageio.ImageIO;

/**
 * 默认验证码提供器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
class DefaultCaptchaProducer implements CaptchaProducer {

  private final DefaultKaptcha defaultKaptcha = new DefaultKaptcha();

  @Override
  public void init(Properties properties) {
    defaultKaptcha.setConfig(new Config(properties));
  }

  @Override
  public Captcha createCaptcha() {
    try {
      String text = defaultKaptcha.createText();
      BufferedImage image = defaultKaptcha.createImage(text);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", byteArrayOutputStream);
      return new Captcha(text, "jpg", byteArrayOutputStream.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
