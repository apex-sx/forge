package pxf.springboot.starter.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pxf.toolkit.extension.captcha.Captcha;
import pxf.toolkit.extension.captcha.CaptchaProducer;
import pxf.toolkit.extension.captcha.DefaultCaptchaProducerBuilder;

/**
 * 验证码管理中心
 *
 * @author potatoxf
 * @date 2021/4/17
 */
@Controller
public class CaptchaManagerCenter {

  private static final Logger LOG = LoggerFactory.getLogger(CaptchaManagerCenter.class);
  public static final String CAPTCHA_SESSION_KEY =
      CaptchaManagerCenter.class + ".CAPTCHA_SESSION_KEY";

  @RequestMapping("/captcha")
  public void getCaptcha(@Autowired CaptchaProducer captchaProducer, HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {
    Captcha captcha = captchaProducer.createCaptcha();
    HttpSession session = request.getSession();

    response.setDateHeader("Expires", 0);

    // Set standard HTTP/1.1 no-cache headers.
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

    // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");

    // Set standard HTTP/1.0 no-cache header.
    response.setHeader("Pragma", "no-cache");

    // return a jpeg
    response.setContentType("image/" + captcha.getFormat());
    // create the text for the image
    String capText = captcha.getText();
    if (LOG.isDebugEnabled()) {
      LOG.debug("******************Captcha: " + capText + "******************");
    }
    session.setAttribute(CAPTCHA_SESSION_KEY, capText);
    // create the image with the text
    response.getOutputStream().write(captcha.getImage());
  }

  @Bean
  @ConditionalOnMissingBean
  public CaptchaProducer captchaProducer() {
    return new DefaultCaptchaProducerBuilder().build();
  }
}
