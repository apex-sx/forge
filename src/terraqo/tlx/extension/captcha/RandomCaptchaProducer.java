package pxf.toolkit.extension.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import pxf.toolkit.basic.lang.ColorHelper;
import pxf.toolkit.basic.text.maker.AbstractRandomTextMaker;
import pxf.toolkit.basic.text.maker.TextMaker;
import pxf.toolkit.basic.text.maker.TextMakerHelper;
import pxf.toolkit.basic.util.Cast;

/**
 * 默认验证码提供器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
class RandomCaptchaProducer implements CaptchaProducer {

  public static final String PARAM_TEXT_MAKER_TYPE = "random.captcha.text.maker.type";
  public static final String PARAM_EXCLUDE_TEXT = "random.captcha.exclude.text";
  public static final String PARAM_TEXT_LENGTH = "random.captcha.text.length";
  public static final String PARAM_WIDTH = "random.captcha.width";
  public static final String PARAM_HEIGHT = "random.captcha.height";
  public static final String PARAM_INTERLINE_AMOUNT = "random.captcha.interline.amount";
  public static final String PARAM_RANDOM_POSITION = "random.captcha.random.position";
  public static final String PARAM_HAS_BORDER = "random.captcha.has.border";
  public static final String PARAM_BORDER_COLOR = "random.captcha.border.color";
  public static final String PARAM_BACK_COLOR = "random.captcha.back.color";
  public static final String PARAM_FONT_COLOR = "random.captcha.font.color";
  public static final String PARAM_LINE_COLOR = "random.captcha.line.color";

  /** 文本制造器 */
  TextMaker textMaker;
  /** 图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度) */
  Integer width;
  /** 图片高度 */
  Integer height;
  /** 图片中干扰线的条数 */
  Integer interLineAmount;
  /** 每个字符的高低位置是否随机 */
  Boolean randomPosition;
  /** 是否画边框 */
  Boolean hasBorder;
  /** 边框颜色，若为null则表示采用随机颜色 */
  Color borderColor;
  /** 图片颜色,若为null则表示采用随机颜色 */
  Color backColor;
  /** 字体颜色,若为null则表示采用随机颜色 */
  Color fontColor;
  /** 干扰线颜色,若为null则表示采用随机颜色 */
  Color lineColor;

  @Override
  public void init(Properties properties) {
    Integer textMakerType = (Integer) properties.getOrDefault(PARAM_TEXT_MAKER_TYPE, 0);
    String excludeText = properties.getProperty(PARAM_EXCLUDE_TEXT, null);
    Integer textLength = (Integer) properties.getOrDefault(PARAM_TEXT_LENGTH, 4);
    AbstractRandomTextMaker humanEnglishRandomTextMaker =
        TextMakerHelper.getHumanEnglishRandomTextMaker(textMakerType);
    humanEnglishRandomTextMaker.setExcludeString(excludeText);
    humanEnglishRandomTextMaker.setLength(textLength);
    textMaker = humanEnglishRandomTextMaker;
    width = (Integer) properties.getOrDefault(PARAM_WIDTH, 120);
    height = (Integer) properties.getOrDefault(PARAM_HEIGHT, 40);
    interLineAmount = (Integer) properties.getOrDefault(PARAM_INTERLINE_AMOUNT, 5);
    randomPosition = (Boolean) properties.getOrDefault(PARAM_RANDOM_POSITION, true);
    hasBorder = (Boolean) properties.getOrDefault(PARAM_HAS_BORDER, true);
    borderColor = (Color) properties.getOrDefault(PARAM_BORDER_COLOR, null);
    backColor = (Color) properties.getOrDefault(PARAM_BACK_COLOR, new Color(238, 242, 237));
    fontColor = (Color) properties.getOrDefault(PARAM_FONT_COLOR, Color.black);
    lineColor = (Color) properties.getOrDefault(PARAM_LINE_COLOR, null);
  }

  @Override
  public Captcha createCaptcha() {
    if (textMaker == null) {
      throw new IllegalCallerException("Please execute first init(Properties properties)");
    }
    String textCode = textMaker.make();
    BufferedImage bufferedImage = createBufferedImage(textCode);
    return new Captcha(textCode, "jpg", Cast.bytesFromImage(bufferedImage, "jpg"));
  }

  public BufferedImage createBufferedImage(String textCode) {
    // 创建 图片缓存对象
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // 获取图形上下文
    Graphics graphics = bufferedImage.getGraphics();
    // 绘制背景图
    graphics.setColor(Objects.isNull(backColor) ? ColorHelper.randomColor() : backColor);
    // 填充一个矩形，第一个参数：要填充的矩形的起始x坐标；第二个参数：要填充的矩形的起始y坐标；第三个参数：要填充的矩形的宽度；第四个参数：要填充的矩形的高度；
    graphics.fillRect(0, 0, width, height);
    if (hasBorder) {
      // 画边框
      graphics.setColor(Objects.isNull(borderColor) ? ColorHelper.randomColor() : borderColor);
      graphics.drawRect(0, 0, width - 1, height - 1);
    }
    // 画干扰线
    Random random = new Random();
    if (interLineAmount > 0) {
      // 定义坐标
      int x1 = 0, y1, x2 = width, y2;
      for (int i = 0; i < interLineAmount; i++) {
        graphics.setColor(Objects.isNull(lineColor) ? ColorHelper.randomColor() : lineColor);
        // 重直方向随机起点
        y1 = random.nextInt(height);
        // 重直方向随机终点
        y2 = random.nextInt(height);
        // 第一个参数：第一个点的x坐标；第二个参数：第一个点的y坐标；第三个参数：第二个点的x坐标；第四个参数：第二个点的y坐标；
        graphics.drawLine(x1, y1, x2, y2);
      }
    }
    // 字体大小为图片高度的80%
    int fontSize = (int) (height * 0.8);
    // 设置第一个字符x坐标
    int fontX = height - fontSize;
    // 设置第一个字符y坐标
    int fontY = fontSize;
    // 设定字体
    graphics.setFont(new Font("Default", Font.PLAIN, fontSize));
    // 写验证码字符
    for (int i = 0; i < textCode.length(); i++) {
      fontY = randomPosition ? (int) ((Math.random() * 0.3 + 0.6) * height) : fontY;
      graphics.setColor(Objects.isNull(fontColor) ? ColorHelper.randomColor() : fontColor);
      // 将验证码字符显示到图象中，画字符串，x坐标即字符串左边位置，y坐标是指baseline的y坐标，即字体所在矩形的左上角y坐标+ascent
      graphics.drawString(String.valueOf(textCode.charAt(i)), fontX, fontY);
      // 移动下一个字符的x坐标
      fontX += fontSize * 0.9;
    }
    graphics.dispose();
    return bufferedImage;
  }
}
