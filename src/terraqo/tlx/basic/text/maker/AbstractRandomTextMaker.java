package pxf.toolkit.basic.text.maker;

import java.util.Random;
import pxf.toolkit.basic.util.Empty;

/**
 * @author potatoxf
 * @date 2021/4/14
 */
public abstract class AbstractRandomTextMaker implements TextMaker {

  private static final int DEFAULT_LENGTH = 4;
  private Random random = new Random();
  private int length = DEFAULT_LENGTH;
  private String excludeString;

  /**
   * 制造文本
   *
   * @return {@code String}
   */
  @Override
  public final String make() {
    Random random = getRandom();
    if (random == null) {
      random = new Random();
    }
    int length = getLength();
    if (length < DEFAULT_LENGTH) {
      length = DEFAULT_LENGTH;
    }
    String excludeString = getExcludeString();
    if (excludeString == null) {
      excludeString = Empty.STRING;
    }
    StringBuilder container = new StringBuilder();
    makeRandomText(container, random, length, excludeString);
    return container.toString();
  }

  /**
   * 生成随机文本
   *
   * @param container 文本容器
   * @param random 随机器
   * @param length 总长度
   * @param excludeString 排除文本
   */
  protected abstract void makeRandomText(
      StringBuilder container, Random random, int length, String excludeString);

  public Random getRandom() {
    return random;
  }

  public void setRandom(Random random) {
    this.random = random;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public String getExcludeString() {
    return excludeString;
  }

  public void setExcludeString(String excludeString) {
    this.excludeString = excludeString;
  }
}
