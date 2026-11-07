package potatoxf.helper.api;

/**
 * 原生类型获取器
 *
 * @author potatoxf
 * @date 2021/06/19
 */
public interface NativeGetter extends NumberGetter {
  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  boolean getBooleanValue(boolean defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  char getCharValue(char defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Boolean getBooleanValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Character getCharacterValue();
}
