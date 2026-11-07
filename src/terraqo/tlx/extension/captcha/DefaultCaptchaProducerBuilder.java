package pxf.toolkit.extension.captcha;

import com.google.code.kaptcha.Constants;
import java.util.Properties;
import pxf.toolkit.basic.constants.impl.BooleanValue;
import pxf.toolkit.basic.lang.SafeProperties;

/**
 * 默认验证码构造器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public class DefaultCaptchaProducerBuilder implements CaptchaProducerBuilder {

  private Properties config;
  private BooleanValue border = BooleanValue.FALSE;
  private String borderColor = "34,114,200";
  private Integer borderThickness = 1;
  private String noiseColor = "99,152,123";
  private String noiseImpl = "pxf.toolkit.extension.captcha.impl.NoiseImpl1";
  private String obscurificatorImpl = "com.google.code.kaptcha.impl.ShadowGimpy";
  private String textProducerImpl = "pxf.toolkit.extension.captcha.components.TextProducer1";
  private String textProducerCharString = null;
  private Integer textProducerCharLength = 4;
  private Integer textProducerCharSpace = null;
  private String textProducerFontNames =
      "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana";
  private String textProducerFontColor = "black";
  private Integer textProducerFontSize = 38;
  private String wordRendererImpl = "pxf.toolkit.extension.captcha.impl.WordRenderer1";
  private String backgroundImpl = null;
  private String backgroundClrFrom = "white";
  private String backgroundClrTo = "white";
  private Integer imageWidth = 125;
  private Integer imageHeight = 45;

  @Override
  public CaptchaProducer build() {
    DefaultCaptchaProducer defaultCaptchaProducer = new DefaultCaptchaProducer();
    Properties properties = new SafeProperties(20);
    properties.put(Constants.KAPTCHA_BORDER, getBorder().alias()[1]);
    properties.put(Constants.KAPTCHA_BORDER_COLOR, getBorderColor());
    properties.put(Constants.KAPTCHA_BORDER_THICKNESS, getBorderThickness());
    properties.put(Constants.KAPTCHA_NOISE_COLOR, getNoiseColor());
    properties.put(Constants.KAPTCHA_NOISE_IMPL, getNoiseImpl());
    properties.put(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, getObscurificatorImpl());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_IMPL, getTextProducerImpl());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, getTextProducerCharString());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, getTextProducerCharLength());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, getTextProducerCharSpace());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, getTextProducerFontNames());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, getTextProducerFontColor());
    properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, getTextProducerFontSize());
    properties.put(Constants.KAPTCHA_WORDRENDERER_IMPL, getWordRendererImpl());
    properties.put(Constants.KAPTCHA_BACKGROUND_IMPL, getBackgroundImpl());
    properties.put(Constants.KAPTCHA_BACKGROUND_CLR_FROM, getBackgroundClrFrom());
    properties.put(Constants.KAPTCHA_BACKGROUND_CLR_TO, getBackgroundClrTo());
    properties.put(Constants.KAPTCHA_IMAGE_WIDTH, getImageWidth());
    properties.put(Constants.KAPTCHA_IMAGE_HEIGHT, getImageHeight());
    properties.putAll(config);
    defaultCaptchaProducer.init(properties);
    return defaultCaptchaProducer;
  }

  public void setValue(String key, Object value) {
    if (config == null) {
      config = new SafeProperties();
    }
    config.put(key, value);
  }

  public BooleanValue getBorder() {
    return border;
  }

  public DefaultCaptchaProducerBuilder setBorder(BooleanValue border) {
    this.border = border;
    return this;
  }

  public String getBorderColor() {
    return borderColor;
  }

  public DefaultCaptchaProducerBuilder setBorderColor(String borderColor) {
    this.borderColor = borderColor;
    return this;
  }

  public Integer getBorderThickness() {
    return borderThickness;
  }

  public DefaultCaptchaProducerBuilder setBorderThickness(Integer borderThickness) {
    this.borderThickness = borderThickness;
    return this;
  }

  public String getNoiseColor() {
    return noiseColor;
  }

  public DefaultCaptchaProducerBuilder setNoiseColor(String noiseColor) {
    this.noiseColor = noiseColor;
    return this;
  }

  public String getNoiseImpl() {
    return noiseImpl;
  }

  public DefaultCaptchaProducerBuilder setNoiseImpl(String noiseImpl) {
    this.noiseImpl = noiseImpl;
    return this;
  }

  public String getObscurificatorImpl() {
    return obscurificatorImpl;
  }

  public DefaultCaptchaProducerBuilder setObscurificatorImpl(String obscurificatorImpl) {
    this.obscurificatorImpl = obscurificatorImpl;
    return this;
  }

  public String getTextProducerImpl() {
    return textProducerImpl;
  }

  public DefaultCaptchaProducerBuilder setTextProducerImpl(String textProducerImpl) {
    this.textProducerImpl = textProducerImpl;
    return this;
  }

  public String getTextProducerCharString() {
    return textProducerCharString;
  }

  public DefaultCaptchaProducerBuilder setTextProducerCharString(String textProducerCharString) {
    this.textProducerCharString = textProducerCharString;
    return this;
  }

  public Integer getTextProducerCharLength() {
    return textProducerCharLength;
  }

  public DefaultCaptchaProducerBuilder setTextProducerCharLength(Integer textProducerCharLength) {
    this.textProducerCharLength = textProducerCharLength;
    return this;
  }

  public Integer getTextProducerCharSpace() {
    return textProducerCharSpace;
  }

  public DefaultCaptchaProducerBuilder setTextProducerCharSpace(Integer textProducerCharSpace) {
    this.textProducerCharSpace = textProducerCharSpace;
    return this;
  }

  public String getTextProducerFontNames() {
    return textProducerFontNames;
  }

  public DefaultCaptchaProducerBuilder setTextProducerFontNames(String textProducerFontNames) {
    this.textProducerFontNames = textProducerFontNames;
    return this;
  }

  public String getTextProducerFontColor() {
    return textProducerFontColor;
  }

  public DefaultCaptchaProducerBuilder setTextProducerFontColor(String textProducerFontColor) {
    this.textProducerFontColor = textProducerFontColor;
    return this;
  }

  public Integer getTextProducerFontSize() {
    return textProducerFontSize;
  }

  public DefaultCaptchaProducerBuilder setTextProducerFontSize(Integer textProducerFontSize) {
    this.textProducerFontSize = textProducerFontSize;
    return this;
  }

  public String getWordRendererImpl() {
    return wordRendererImpl;
  }

  public DefaultCaptchaProducerBuilder setWordRendererImpl(String wordRendererImpl) {
    this.wordRendererImpl = wordRendererImpl;
    return this;
  }

  public String getBackgroundImpl() {
    return backgroundImpl;
  }

  public DefaultCaptchaProducerBuilder setBackgroundImpl(String backgroundImpl) {
    this.backgroundImpl = backgroundImpl;
    return this;
  }

  public String getBackgroundClrFrom() {
    return backgroundClrFrom;
  }

  public DefaultCaptchaProducerBuilder setBackgroundClrFrom(String backgroundClrFrom) {
    this.backgroundClrFrom = backgroundClrFrom;
    return this;
  }

  public String getBackgroundClrTo() {
    return backgroundClrTo;
  }

  public DefaultCaptchaProducerBuilder setBackgroundClrTo(String backgroundClrTo) {
    this.backgroundClrTo = backgroundClrTo;
    return this;
  }

  public Integer getImageWidth() {
    return imageWidth;
  }

  public DefaultCaptchaProducerBuilder setImageWidth(Integer imageWidth) {
    this.imageWidth = imageWidth;
    return this;
  }

  public Integer getImageHeight() {
    return imageHeight;
  }

  public DefaultCaptchaProducerBuilder setImageHeight(Integer imageHeight) {
    this.imageHeight = imageHeight;
    return this;
  }
}
