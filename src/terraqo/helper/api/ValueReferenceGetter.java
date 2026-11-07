package potatoxf.helper.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author potatoxf
 * @date 2021/8/19
 */
public interface ValueReferenceGetter extends NativeReferenceGetter, ValueGetter {

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  BigInteger getBigIntegerValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  BigDecimal getBigDecimalValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  String getStringValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  String getStringValue(String key, String defaultValue);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Object getObjectValue(String key);

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Object getObjectValue(String key, Object defaultValue);
}
