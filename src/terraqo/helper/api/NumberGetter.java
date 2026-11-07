package potatoxf.helper.api;

/**
 * @author potatoxf
 * @date 2022/02/01
 */
public interface NumberGetter {

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  byte getByteValue(byte defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  short getShortValue(short defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  int getIntValue(int defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  long getLongValue(long defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  float getFloatValue(float defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  double getDoubleValue(double defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Byte getByteValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Short getShortValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Integer getIntegerValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Long getLongValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Float getFloatValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Double getDoubleValue();
}
