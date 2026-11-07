package pxf.toolkit.basic.constants;


import java.util.Objects;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.constants.i18n.LocaleConstantHelper;

/**
 * 抽象可查找本地常量
 * <p>
 * 子类的时候需要实现一个静态 {@code LocaleConstantHelper}然后将该类传递给该参数
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public abstract class AbstractLocaleFindableConstant<T extends AbstractLocaleFindableConstant<T>>
    extends AbstractFindableConstant<T>
    implements LocaleFindableConstant<T> {

  /**
   * 国际化常量助手
   */
  private final LocaleConstantHelper localeConstantHelper;
  /**
   * 国际化键
   */
  private final String localeKey;

  protected AbstractLocaleFindableConstant(int identity, String identityName, String comment,
      LocaleConstantHelper localeConstantHelper) {
    this(identity, identityName, comment, localeConstantHelper, null);
  }

  protected AbstractLocaleFindableConstant(int identity, String identityName, String comment,
      LocaleConstantHelper localeConstantHelper,
      String localeKey) {
    super(identity, identityName, comment);
    this.localeKey = localeKey;
    this.localeConstantHelper = Objects.requireNonNull(localeConstantHelper,
        "The helper of the internationalized resource pack is not allowed to be null");
  }

  /**
   * 本地化常量助手，子类需要传递这个常量
   *
   * @return {@code LocaleConstantHelper}
   */
  @Nonnull
  @Override
  public LocaleConstantHelper localeConstantHelper() {
    return localeConstantHelper;
  }

  /**
   * 获取本地化语言键，返回枚举的名称并将 {@code _}换成 {@code .}
   *
   * @return 返回本地化语言键
   */
  @Nonnull
  @Override
  public String localeKey() {
    if (localeKey != null) {
      return localeKey;
    }
    return self().identityName().replace('_', '.').toLowerCase();
  }
}
