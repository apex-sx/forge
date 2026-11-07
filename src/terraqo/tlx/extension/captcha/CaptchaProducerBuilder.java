package pxf.toolkit.extension.captcha;

import pxf.toolkit.basic.function.Builder;

/**
 * 验证码提供器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public interface CaptchaProducerBuilder extends Builder<CaptchaProducer> {

  /**
   * 创建验证码提供器
   *
   * @return {@code CaptchaProducer}
   */
  @Override
  CaptchaProducer build();
}
