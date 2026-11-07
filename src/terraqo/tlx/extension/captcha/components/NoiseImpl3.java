package pxf.toolkit.extension.captcha.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;
import pxf.toolkit.basic.lang.ColorHelper;
import pxf.toolkit.basic.util.Cast;

/**
 * 线性噪音
 *
 * @author potatoxf
 * @date 2021/4/14
 */
public class NoiseImpl3 extends NoiseImpl2 {

  public static final String KAPTCHA_NOISE_LINE_COLOR = "kaptcha.noise.line.color";

  @Override
  public void makeNoise(
      BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
    Color color = getConfig().getNoiseColor();
    Properties properties = getConfig().getProperties();
    String lineAmountValue = properties.getProperty(KAPTCHA_NOISE_LINE_AMOUNT);
    String lineColorValue = properties.getProperty(KAPTCHA_NOISE_LINE_COLOR);
    int lineAmount = Cast.intValue(lineAmountValue, 4);
    Color lineColor = Color.getColor(lineColorValue, null);
    // image size
    int width = image.getWidth();
    int height = image.getHeight();
    Graphics graphics = image.createGraphics();
    // 随机产生16条灰色干扰线，使图像中的认证码不易识别
    graphics.setColor(color);
    // 创建一个随机数生成器类 用于随机产生干扰线
    Random random = new Random();
    // 定义坐标
    int y1, y2;
    for (int i = 0; i < lineAmount; i++) {
      graphics.setColor(lineColor == null ? ColorHelper.randomColor() : lineColor);
      // 重直方向随机起点
      y1 = random.nextInt(height);
      // 重直方向随机终点
      y2 = random.nextInt(height);
      // 第一个参数：第一个点的x坐标；第二个参数：第一个点的y坐标；第三个参数：第二个点的x坐标；第四个参数：第二个点的y坐标；
      graphics.drawLine(0, y1, width, y2);
    }
  }
}
