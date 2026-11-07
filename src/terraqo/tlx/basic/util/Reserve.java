package pxf.toolkit.basic.util;

/**
 * @author potatoxf
 * @date 2021/4/21
 */
public class Reserve {

  private Reserve() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 反转索引
   *
   * @param index 　索引
   * @param len 　长度
   * @param isReserve 　是否反转
   * @return 返回索引
   */
  public static int index(int index, int len, boolean isReserve) {
    if (isReserve) {
      return index(index, len);
    }
    return Legalize.index(index, len);
  }

  /**
   * 反转索引
   *
   * @param index 　索引
   * @param len 　长度
   * @return 返回反转索引
   */
  public static int index(int index, int len) {
    return len - Legalize.index(index, len);
  }
}
