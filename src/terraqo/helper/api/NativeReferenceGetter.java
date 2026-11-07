package potatoxf.helper.api;

/**
 * 数字类型获取器
 *
 * @author potatoxf
 * @date 2021/06/19
 */
public interface NativeReferenceGetter extends NumberReferenceGetter, NativeGetter {

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  boolean getBooleanValue(String key, boolean defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  char getCharValue(String key, char defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Boolean getBooleanValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Character getCharacterValue(String key);
}
