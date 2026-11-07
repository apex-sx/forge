package pxf.toolkit.basic.text.maker;

import java.util.Random;

/**
 * 生成随机文本为仅数字,即0~9
 *
 * @author potatoxf
 * @date 2021/4/14
 */
public class NumberRandomTextMaker extends AbstractRandomTextMaker {

  /**
   * 生成随机文本为仅数字,即0~9
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
      int t = random.nextInt(10);
      // 排除特殊字符
      if (excludeString.indexOf(String.valueOf(t)) > 0) {
        continue;
      }
      container.append(t);
      i++;
    }
  }
}
