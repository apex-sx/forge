package pxf.toolkit.extension.captcha;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;
import pxf.toolkit.basic.image.AnimatedGifEncoder;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.Create;

/**
 * 默认验证码提供器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
class GifCaptchaProducer extends RandomCaptchaProducer {

  public static final String PARAM_IMAGE_AMOUNT = "gif.captcha.image.amount";
  public static final String PARAM_REPEAT_COUNT = "gif.captcha.repeat.count";
  public static final String PARAM_DELAY = "gif.captcha.delay";

  /** 图像数量 */
  int imageAmount;
  /** 图像重复次数 */
  int repeatCount;
  /** 延迟 */
  int delay;

  @Override
  public void init(Properties properties) {
    super.init(properties);
    imageAmount = (int) properties.getOrDefault(PARAM_IMAGE_AMOUNT, 10);
    repeatCount = (int) properties.getOrDefault(PARAM_REPEAT_COUNT, 0);
    delay = (int) properties.getOrDefault(PARAM_DELAY, 200);
  }

  @Override
  public Captcha createCaptcha() {
    if (textMaker == null) {
      throw new IllegalCallerException("Please execute first init(Properties properties)");
    }
    String textCode = textMaker.make();
    // 创建一个临时文件
    File file = Create.temporaryFile("gif");
    AnimatedGifEncoder e = new AnimatedGifEncoder();
    // 设置GIF重复次数
    e.setRepeat(repeatCount);
    // 设置合成位置
    e.start(file.getAbsolutePath());
    for (int i = 0; i < imageAmount; i++) {
      BufferedImage bufferedImage = createBufferedImage(textCode);
      // 设置播放的延迟时间
      e.setDelay(delay);
      // 添加到帧中
      e.addFrame(bufferedImage);
    }
    e.finish();
    return new Captcha(textCode, "gif", Cast.bytesFromFile(file));
  }
}
