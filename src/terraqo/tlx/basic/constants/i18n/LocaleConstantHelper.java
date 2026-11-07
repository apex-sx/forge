package pxf.toolkit.basic.constants.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.AbnormalException;

/**
 * 本地化常量助手
 *
 * @author potatoxf
 * @date 2020/10/31
 */
public class LocaleConstantHelper {

  private static final Logger LOG = LoggerFactory.getLogger(LocaleConstantHelper.class);
  private final String resourceBundleName;

  public LocaleConstantHelper(@Nonnull String resourceBundleName) {
    this.resourceBundleName = Objects.requireNonNull(resourceBundleName,
        "The name of the internationalized resource pack is not allowed to be null");
  }

  /**
   * 获取本地化名称
   *
   * @param key 键
   * @return 名称
   */
  @Nonnull
  public String getValidLocaleName(@Nullable Locale locale, @Nonnull String key) {
    if (locale == null) {
      return getDefaultLocaleName(key);
    }
    return getLocaleName(locale, key);
  }

  /**
   * 获取本地化名称
   *
   * @param key 键
   * @return 名称
   */
  @Nonnull
  public String getDefaultLocaleName(@Nonnull String key) {
    String country = System.getProperty("user.country");
    String language = System.getProperty("user.language");
    return getLocaleName(new Locale(language, country), key);
  }

  /**
   * 获取名称
   *
   * @param locale 本地化语言
   * @return 名称
   */
  @Nonnull
  String getLocaleName(@Nonnull Locale locale, @Nonnull String key) {
    ResourceBundle countryResourceBundle = ResourceBundle.getBundle(resourceBundleName, locale);
    try {
      return countryResourceBundle.getString(key);
    } catch (MissingResourceException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(
            String.format("No value matching the key [%s] was found in the resource bundle", key),
            e);
      }
    }
    throw new AbnormalException();
  }
}
