package pxf.toolkit.extension.captcha.components;

import com.google.code.kaptcha.text.TextProducer;
import com.google.code.kaptcha.util.Configurable;
import pxf.toolkit.basic.text.maker.AbstractRandomTextMaker;
import pxf.toolkit.basic.text.maker.TextMakerHelper;

/**
 * @author potatoxf
 * @date 2021/4/14
 */
public class TextProducer1 extends Configurable implements TextProducer {

  public static final String KAPTCHA_TEXTPRODUCER_CHAR_EXCLUDE_STRING =
      "kaptcha.textproducer.char.exclude.string";

  private static final String DEFAULT_EXCLUDE_STRING = "1ILl0o";

  private AbstractRandomTextMaker textMaker =
      TextMakerHelper.getDefaultHumanEnglishRandomTextMaker();

  @Override
  public String getText() {
    textMaker.setLength(getConfig().getTextProducerCharLength());
    String excludeString =
        getConfig()
            .getProperties()
            .getProperty(KAPTCHA_TEXTPRODUCER_CHAR_EXCLUDE_STRING, DEFAULT_EXCLUDE_STRING);
    textMaker.setExcludeString(excludeString);
    return textMaker.make();
  }

  public AbstractRandomTextMaker getTextMaker() {
    return textMaker;
  }

  public void setTextMaker(AbstractRandomTextMaker textMaker) {
    this.textMaker = textMaker;
  }
}
