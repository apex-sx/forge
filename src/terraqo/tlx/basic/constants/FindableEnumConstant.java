package pxf.toolkit.basic.constants;

import pxf.toolkit.basic.util.Const;

/**
 * 可查找常量
 *
 * <p>通过整数和字符来查找某一个常量
 *
 * @author potatoxf
 * @date 2021/3/12
 * @see Const#parseFindableConstant(Class, int)
 * @see Const#parseFindableConstant(Class, int, FindableConstant)
 * @see Const#parseFindableConstant(Class, String)
 * @see Const#parseFindableConstant(Class, String, FindableConstant)
 */
public interface FindableEnumConstant<
        E extends Enum<E> & FindableEnumConstant<E> & FindableConstant<E>>
    extends FindableConstant<E> {

  /**
   * 通过身份牌查找
   *
   * @return 返回唯一身份牌
   */
  @Override
  default int identity() {
    return self().ordinal();
  }

  /**
   * 通过唯一名称查找
   *
   * @return 返回名称
   */
  @Override
  default String identityName() {
    return self().name().toUpperCase();
  }
}
