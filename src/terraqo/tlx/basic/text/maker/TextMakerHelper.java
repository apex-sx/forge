package pxf.toolkit.basic.text.maker;

import java.util.function.Supplier;
import pxf.toolkit.basic.lang.SequenceSelector;

/**
 * 文本制造器助手类
 *
 * @author potatoxf
 * @date 2021/4/14
 */
public final class TextMakerHelper {

  private static final SequenceSelector<TextMakerSupplier> HUMAN_ENGLISH_RANDOM_SELECTOR =
      SequenceSelector.of(
          new LetterNumberRandomTextMakerSupplier(),
          new NumberRandomTextMakerSupplier(),
          new LetterRandomTextMakerSupplier(),
          new UpperLetterNumberRandomTextMakerSupplier(),
          new LowerLetterNumberRandomTextMakerSupplier(),
          new UpperLetterRandomTextMakerSupplier(),
          new LowerLetterRandomTextMakerSupplier());

  private TextMakerHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 获取默认易懂随机英文文本
   *
   * @return {@code String}
   * @see #getDefaultHumanEnglishRandomTextMaker()
   */
  public static String getDefaultHumanEnglishRandomText() {
    return getDefaultHumanEnglishRandomTextMaker().make();
  }

  /**
   * 获取易懂随机英文文本
   *
   * <pre>
   * type=0， 类型为数字和大小写字母混合
   * type=1，类型为仅数字,即0~9
   * type=2，类型为仅字母,即大小写字母混合
   * type=3，类型为数字和大写字母混合
   * type=4，类型为数字和小写字母混合
   * type=5，类型为仅大写字母
   * type=6，类型为仅小写字母
   * </pre>
   *
   * @param type 类型
   * @return {@code String}
   * @see #getHumanEnglishRandomTextMaker(int)
   */
  public static String getHumanEnglishRandomText(int type) {
    return getHumanEnglishRandomTextMaker(type).make();
  }

  /**
   * 获取默认易懂英文文本随机生成器
   *
   * @return {@code AbstractRandomTextMaker}
   * @see #getHumanEnglishRandomTextMaker(int)
   */
  public static AbstractRandomTextMaker getDefaultHumanEnglishRandomTextMaker() {
    return getHumanEnglishRandomTextMaker(0);
  }

  /**
   * 获取易懂英文文本随机生成器
   *
   * <pre>
   * type=0， 类型为数字和大小写字母混合
   * type=1，类型为仅数字,即0~9
   * type=2，类型为仅字母,即大小写字母混合
   * type=3，类型为数字和大写字母混合
   * type=4，类型为数字和小写字母混合
   * type=5，类型为仅大写字母
   * type=6，类型为仅小写字母
   * </pre>
   *
   * @param type 类型
   * @return {@code AbstractRandomTextMaker}
   */
  public static AbstractRandomTextMaker getHumanEnglishRandomTextMaker(int type) {
    return HUMAN_ENGLISH_RANDOM_SELECTOR.selectAction(type).get();
  }

  interface TextMakerSupplier extends Supplier<AbstractRandomTextMaker> {}

  static class NumberRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new NumberRandomTextMaker();
    }
  }

  static class LetterRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new LetterRandomTextMaker();
    }
  }

  static class LetterNumberRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new LetterNumberRandomTextMaker();
    }
  }

  static class UpperLetterNumberRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new UpperLetterNumberRandomTextMaker();
    }
  }

  static class LowerLetterNumberRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new LowerLetterNumberRandomTextMaker();
    }
  }

  static class UpperLetterRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new UpperLetterRandomTextMaker();
    }
  }

  static class LowerLetterRandomTextMakerSupplier implements TextMakerSupplier {

    @Override
    public AbstractRandomTextMaker get() {
      return new LowerLetterRandomTextMaker();
    }
  }
}
