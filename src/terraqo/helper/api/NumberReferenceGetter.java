package potatoxf.helper.api;

/**
 * 数字类型获取器
 *
 * @author potatoxf
 * @date 2021/06/19
 */
public interface NumberReferenceGetter extends NumberGetter {

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  byte getByteValue(String key, byte defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  short getShortValue(String key, short defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  int getIntValue(String key, int defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  long getLongValue(String key, long defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  float getFloatValue(String key, float defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  double getDoubleValue(String key, double defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Byte getByteValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Short getShortValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Integer getIntegerValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Long getLongValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Float getFloatValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Double getDoubleValue(String key);
}
