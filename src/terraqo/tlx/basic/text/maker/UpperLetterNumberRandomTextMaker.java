package pxf.toolkit.basic.text.maker;

import java.util.Random;
import pxf.toolkit.basic.util.Const;
import pxf.toolkit.basic.util.Is;

/**
 * 生成随机文本为数字和大写字母混合
 *
 * @author potatoxf
 * @date 2021/4/14
 */
public class UpperLetterNumberRandomTextMaker extends AbstractRandomTextMaker {

  /**
   * 生成随机文本为数字和大写字母混合
   *
   * @param container 文本容器
   * @param random 随机器
   * @param length 总长度
   * @param excludeString 排除文本
   */
  @Override
  protected void makeRandomText(
      StringBuilder container, Random random, int length, String excludeString) {
    int i = 0;
    while (i < length) {
      int t = random.nextInt(Const.ASCII_UPPER_LETTER_HI + 1);
      if (Is.letterUpper(t) || Is.numberChar(t)) {
        if (excludeString.indexOf((char) t) > 0) {
          continue;
        }
        container.append((char) t);
        i++;
      }
    }
  }
}
