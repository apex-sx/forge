package pxf.toolkit.basic.constants;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pxf.toolkit.basic.constants.i18n.LocaleConstantHelper;

/**
 * 本地化的常量接口
 *
 * @author potatoxf
 * @date 2020/12/19
 */
public interface LocaleFindableConstant<T extends LocaleFindableConstant<T>>
    extends FindableConstant<T> {

  /**
   * 本地化常量助手
   *
   * @return {@code LocaleConstantHelper}
   */
  @Nonnull
  LocaleConstantHelper localeConstantHelper();

  /**
   * 获取本地化语言键，返回枚举的名称并将 {@code _}换成 {@code .}
   *
   * @return 返回本地化语言键
   */
  @Nonnull
  default String localeKey() {
    return self().identityName().replace('_', '.').toLowerCase();
  }

  /**
   * 本地化常量显示名
   *
   * @return {@code String}
   */
  @Nonnull
  default String getLocaleName() {
    return getLocaleName(null);
  }

  /**
   * 获取本地化名字，如果不知持默认返回英文
   *
   * @param locale 本地化参数
   * @return {@code String}
   */
  @Nonnull
  default String getLocaleName(@Nullable Locale locale) {
    return localeConstantHelper().getValidLocaleName(locale, localeKey());
  }

  /**
   * 获取英文名
   *
   * @return {@code String}
   */
  @Nonnull
  default String getEnglishName() {
    return localeConstantHelper().getValidLocaleName(Locale.US, localeKey());
  }

  /**
   * 获取中文名
   *
   * @return {@code String}
   */
  @Nonnull
  default String getChineseName() {
    return localeConstantHelper().getValidLocaleName(Locale.CHINESE, localeKey());
  }
}
