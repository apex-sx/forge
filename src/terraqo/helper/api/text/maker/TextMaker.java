package potatoxf.helper.api.text.maker;

/**
 * 文本制作器
 *
 * @author potatoxf
 * @date 2021/4/14
 */
public interface TextMaker {

  /**
   * 制造文本
   *
   * @return {@code String}
   */
  String make();

  /**
   * 制造文本
   *
   * @param length 指定长度
   * @return {@code String}
   */
  String make(int length);
}
