package pxf.toolkit.extension.captcha;

import java.util.Properties;

/**
 * 验证码提供器
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public interface CaptchaProducer {

  /**
   * 初始化
   *
   * @param properties 参数
   */
  void init(Properties properties);

  /**
   * 创建一个验证码
   *
   * @return {@code Captcha}
   */
  Captcha createCaptcha();
}
