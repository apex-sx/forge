package potatoxf.helper.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author potatoxf
 * @date 2021/8/19
 */
public interface ValueGetter extends NativeGetter {

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  BigInteger getBigIntegerValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  BigDecimal getBigDecimalValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  String getStringValue();

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  Object getObjectValue();
}
