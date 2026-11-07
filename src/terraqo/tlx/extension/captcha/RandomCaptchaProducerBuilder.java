package pxf.toolkit.extension.captcha;

import java.util.Properties;
import pxf.toolkit.basic.lang.ColorHelper;
import pxf.toolkit.basic.lang.SafeProperties;

/**
 * 默认验证码构造器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public class RandomCaptchaProducerBuilder implements CaptchaProducerBuilder {

  /** 文本类型 */
  private Integer textMakerType;
  /** 排除字符串 */
  private String excludeText;
  /** 文本长度 */
  private Integer textLength;
  /** 图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度) */
  private Integer width;
  /** 图片高度 */
  private Integer height;
  /** 图片中干扰线的条数 */
  private Integer interLineAmount;
  /** 每个字符的高低位置是否随机 */
  private Boolean randomPosition;
  /** 是否画边框 */
  private Boolean hasBorder;
  /** 边框颜色，若为null则表示采用随机颜色 */
  private String borderColor;
  /** 图片颜色,若为null则表示采用随机颜色 */
  private String backColor;
  /** 字体颜色,若为null则表示采用随机颜色 */
  private String fontColor;
  /** 干扰线颜色,若为null则表示采用随机颜色 */
  private String lineColor;

  @Override
  public CaptchaProducer build() {
    CaptchaProducer captchaProducer = create();
    Properties properties = new SafeProperties();
    config(properties);
    captchaProducer.init(properties);
    return captchaProducer;
  }

  protected CaptchaProducer create() {
    return new RandomCaptchaProducer();
  }

  protected void config(Properties properties) {
    properties.put(RandomCaptchaProducer.PARAM_TEXT_MAKER_TYPE, getTextMakerType());
    properties.put(RandomCaptchaProducer.PARAM_EXCLUDE_TEXT, getExcludeText());
    properties.put(RandomCaptchaProducer.PARAM_TEXT_LENGTH, getTextLength());
    properties.put(RandomCaptchaProducer.PARAM_WIDTH, getWidth());
    properties.put(RandomCaptchaProducer.PARAM_HEIGHT, getHeight());
    properties.put(RandomCaptchaProducer.PARAM_INTERLINE_AMOUNT, getInterLineAmount());
    properties.put(RandomCaptchaProducer.PARAM_RANDOM_POSITION, getRandomPosition());
    properties.put(RandomCaptchaProducer.PARAM_HAS_BORDER, getHasBorder());
    properties.put(
        RandomCaptchaProducer.PARAM_BORDER_COLOR, ColorHelper.createColor(getBorderColor(), null));
    properties.put(
        RandomCaptchaProducer.PARAM_BACK_COLOR, ColorHelper.createColor(getBackColor(), null));
    properties.put(
        RandomCaptchaProducer.PARAM_FONT_COLOR, ColorHelper.createColor(getFontColor(), null));
    properties.put(
        RandomCaptchaProducer.PARAM_LINE_COLOR, ColorHelper.createColor(getLineColor(), null));
  }

  public Integer getTextMakerType() {
    return textMakerType;
  }

  public void setTextMakerType(Integer textMakerType) {
    this.textMakerType = textMakerType;
  }

  public String getExcludeText() {
    return excludeText;
  }

  public void setExcludeText(String excludeText) {
    this.excludeText = excludeText;
  }

  public Integer getTextLength() {
    return textLength;
  }

  public void setTextLength(Integer textLength) {
    this.textLength = textLength;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Integer getInterLineAmount() {
    return interLineAmount;
  }

  public void setInterLineAmount(Integer interLineAmount) {
    this.interLineAmount = interLineAmount;
  }

  public Boolean getRandomPosition() {
    return randomPosition;
  }

  public void setRandomPosition(Boolean randomPosition) {
    this.randomPosition = randomPosition;
  }

  public Boolean getHasBorder() {
    return hasBorder;
  }

  public void setHasBorder(Boolean hasBorder) {
    this.hasBorder = hasBorder;
  }

  public String getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(String borderColor) {
    this.borderColor = borderColor;
  }

  public String getBackColor() {
    return backColor;
  }

  public void setBackColor(String backColor) {
    this.backColor = backColor;
  }

  public String getFontColor() {
    return fontColor;
  }

  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }

  public String getLineColor() {
    return lineColor;
  }

  public void setLineColor(String lineColor) {
    this.lineColor = lineColor;
  }
}
