package pxf.toolkit.extension.captcha.components;

import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;
import pxf.toolkit.basic.util.Cast;

/**
 * 线性噪音
 *
 * @author potatoxf
 * @date 2021/4/14
 */
public class NoiseImpl2 extends Configurable implements NoiseProducer {

  public static final String KAPTCHA_NOISE_LINE_AMOUNT = "kaptcha.noise.line.amount";

  @Override
  public void makeNoise(
      BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
    Color color = getConfig().getNoiseColor();
    Properties properties = getConfig().getProperties();
    String property = properties.getProperty(KAPTCHA_NOISE_LINE_AMOUNT);
    int lineAmount = Cast.intValue(property, 50);
    // image size
    int width = image.getWidth();
    int height = image.getHeight();
    Graphics graphics = image.createGraphics();
    // 随机产生16条灰色干扰线，使图像中的认证码不易识别
    graphics.setColor(color);
    // 创建一个随机数生成器类 用于随机产生干扰线
    Random random = new Random();
    int x1, y1, x2, y2;
    for (int i = 0; i < lineAmount; i++) {
      x1 = random.nextInt(width);
      y1 = random.nextInt(height);
      x2 = random.nextInt(12);
      y2 = random.nextInt(12);
      // 第一个参数：第一个点的x坐标；第二个参数：第一个点的y坐标；第三个参数：第二个点的x坐标；第四个参数：第二个点的y坐标；
      graphics.drawLine(x1, y1, x1 + x2, y1 + y2);
    }
  }
}
