package pxf.toolkit.extension.captcha;

import java.util.Properties;

/**
 * 默认验证码构造器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public class GifCaptchaProducerBuilder extends RandomCaptchaProducerBuilder {

  /** 图像数量 */
  private Integer imageAmount;
  /** 图像重复次数 */
  private Integer repeatCount;
  /** 延迟 */
  private Integer delay;

  @Override
  protected CaptchaProducer create() {
    return new GifCaptchaProducer();
  }

  @Override
  protected void config(Properties properties) {
    super.config(properties);
    properties.put(GifCaptchaProducer.PARAM_IMAGE_AMOUNT, getImageAmount());
    properties.put(GifCaptchaProducer.PARAM_REPEAT_COUNT, getRepeatCount());
    properties.put(GifCaptchaProducer.PARAM_DELAY, getDelay());
  }

  public Integer getImageAmount() {
    return imageAmount;
  }

  public void setImageAmount(Integer imageAmount) {
    this.imageAmount = imageAmount;
  }

  public Integer getRepeatCount() {
    return repeatCount;
  }

  public void setRepeatCount(Integer repeatCount) {
    this.repeatCount = repeatCount;
  }

  public Integer getDelay() {
    return delay;
  }

  public void setDelay(Integer delay) {
    this.delay = delay;
  }
}
