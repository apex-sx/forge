package potatoxf.helper.basic.db;

import potatoxf.helper.api.Tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author potatoxf
 * @date 2022/05/13
 */
final class CommonParameterKey {
  private static final String DEFAULT = "DEFAULT";
  /** 主要键 */
  @Nonnull private final MainKey mainKey;
  /** 引用索引 */
  private final int ref;

  private CommonParameterKey(@Nonnull MainKey mainKey, final int ref) {
    this.mainKey = mainKey;
    this.ref = ref;
  }

  @Nonnull
  static CommonParameterKey of(
      @Nonnull final CommonParameterKey commonParameterKey, final int ref) {
    return new CommonParameterKey(commonParameterKey.mainKey, ref);
  }

  @Nonnull
  static CommonParameterKey of(
      @Nullable final String module,
      @Nullable final String type,
      @Nullable final String name,
      @Nullable final String person,
      final boolean enumerable,
      final int ref) {
    return new CommonParameterKey(new MainKey(module, type, name, person, enumerable), ref);
  }

  @Nonnull
  public MainKey getEntryKey() {
    return mainKey;
  }

  @Nonnull
  public String getModule() {
    return mainKey.module;
  }

  @Nonnull
  public String getType() {
    return mainKey.type;
  }

  @Nonnull
  public String getName() {
    return mainKey.name;
  }

  @Nonnull
  public String getPerson() {
    return mainKey.person;
  }

  public boolean isEnumerable() {
    return mainKey.enumerable;
  }

  public int getRef() {
    return ref;
  }

  public boolean seriesEquals(@Nonnull final CommonParameterKey that) {
    if (this == that) return true;
    return mainKey.equals(that.mainKey);
  }

  @Override
  public boolean equals(@Nullable final Object o) {
    if (this == o) return true;
    if (!(o instanceof CommonParameterKey)) return false;
    CommonParameterKey that = (CommonParameterKey) o;
    return ref == that.ref && seriesEquals(that);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mainKey, ref);
  }

  static final class MainKey {
    /** 所属模块名 */
    @Nonnull private final String module;
    /** 参数所属组 */
    @Nonnull private final String type;
    /** 参数名名称 */
    @Nonnull private final String name;
    /** 人员 */
    @Nonnull private final String person;
    /** 是否全局 */
    private final boolean enumerable;

    private MainKey(
        @Nonnull final String module,
        @Nonnull final String type,
        @Nonnull final String name,
        @Nonnull final String person,
        final boolean enumerable) {
      this.module = Tools.safeValue(module, DEFAULT);
      this.type = Tools.safeValue(type, DEFAULT);
      this.name = Tools.safeValue(name, DEFAULT);
      this.person = Tools.safeValue(person, DEFAULT);
      this.enumerable = enumerable;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof MainKey)) return false;
      MainKey mainKey = (MainKey) o;
      return enumerable == mainKey.enumerable
          && module.equals(mainKey.module)
          && type.equals(mainKey.type)
          && name.equals(mainKey.name)
          && person.equals(mainKey.person);
    }

    @Override
    public int hashCode() {
      return Objects.hash(module, type, name, person, enumerable);
    }
  }
}
