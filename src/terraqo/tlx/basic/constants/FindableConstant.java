package pxf.toolkit.basic.constants;

import pxf.toolkit.basic.function.Reference;
import pxf.toolkit.basic.util.Const;
import pxf.toolkit.basic.util.Empty;

/**
 * 可查找的常量
 *
 * <p>通过整数和字符来查找某一个常量
 *
 * @author potatoxf
 * @date 2021/3/12
 * @see Const#parseFindable(Class, int)
 * @see Const#parseFindable(Class, int, FindableConstant)
 * @see Const#parseFindable(Class, String)
 * @see Const#parseFindable(Class, String, FindableConstant)
 */
public interface FindableConstant<T extends FindableConstant<T>> extends Reference<T> {

  /**
   * 通过身份牌查找
   *
   * @return 返回唯一身份牌
   */
  int identity();

  /**
   * 通过唯一名称查找
   *
   * @return 返回名称
   */
  String identityName();

  /**
   * 通过别名查找
   *
   * @return 返回别名
   */
  default String[] alias() {
    return Empty.STRING_ARRAY;
  }

  /**
   * 该可查找信息注释信息
   *
   * @return 可查找信息注释信息
   */
  default String comment() {
    return Empty.STRING;
  }

  /**
   * 是否忽略身份牌大小写
   *
   * @return {@code true}忽略，否则 {@code false}
   */
  default boolean isIgnoreNameCase() {
    return true;
  }

  /**
   * 是否忽略别名大小写
   *
   * @return {@code true}忽略，否则 {@code false}
   */
  default boolean isIgnoreAliasCase() {
    return true;
  }

  /**
   * 是否自定义字符串匹配
   *
   * @return 如果自定义匹配则返回 {@code true}，否则 {@code false}
   */
  default boolean isCustomMatchString() {
    return false;
  }

  /**
   * 是否匹配输入字符串
   *
   * @param input 输入字符串
   * @return 如果匹配则返回 {@code true}，否则 {@code false}
   */
  default boolean isMatchString(String input) {
    return false;
  }
}
