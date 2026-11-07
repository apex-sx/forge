package pxf.toolkit.basic.constants;

import javax.annotation.Nonnull;

/**
 * 本地化的枚举常量接口
 *
 * @author potatoxf
 * @date 2020/12/19
 */
public interface LocaleFindableEnumConstant<
        E extends Enum<E> & FindableEnumConstant<E> & LocaleFindableConstant<E>>
    extends FindableEnumConstant<E>, LocaleFindableConstant<E> {

  /**
   * 获取本地化语言键，返回枚举的名称并将 {@code _}换成 {@code .}
   *
   * @return 返回本地化语言键
   */
  @Nonnull
  @Override
  default String localeKey() {
    return self().name().replace('_', '.').toLowerCase();
  }
}
