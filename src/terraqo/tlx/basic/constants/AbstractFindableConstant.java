package pxf.toolkit.basic.constants;

import pxf.toolkit.basic.util.Empty;

/**
 * 抽象可查找常量
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public abstract class AbstractFindableConstant<T extends AbstractFindableConstant<T>>
    implements FindableConstant<T> {

  /** 唯一身份牌 */
  private final int identity;
  /** 唯一名称 */
  private final String identityName;
  /** 别名 */
  private final String[] alias;
  /** 注释 */
  private final String comment;
  /** 是否忽略名字大小写 */
  private final boolean isIgnoreNameCase;
  /** 是否忽略别名大小写 */
  private final boolean isIgnoreAliasCase;

  protected AbstractFindableConstant(int identity, String comment) {
    this(identity, String.valueOf(identity), comment);
  }

  protected AbstractFindableConstant(int identity, String identityName, String comment) {
    this(identity, true, identityName, comment, true, Empty.STRING_ARRAY);
  }

  protected AbstractFindableConstant(int identity, String identityName, String comment, String... alias) {
    this(identity, true, identityName, comment, true, alias);
  }

  protected AbstractFindableConstant(
      int identity,
      boolean isIgnoreNameCase,
      String identityName,
      String comment,
      boolean isIgnoreAliasCase,
      String... alias) {
    this.identity = identity;
    this.isIgnoreNameCase = isIgnoreNameCase;
    this.identityName = identityName;
    this.comment = comment;
    this.isIgnoreAliasCase = isIgnoreAliasCase;
    this.alias = alias;
  }

  /**
   * 通过身份牌查找
   *
   * @return 返回唯一身份牌
   */
  @Override
  public final int identity() {
    return identity;
  }

  /**
   * 通过唯一名称查找
   *
   * @return 返回名称
   */
  @Override
  public final String identityName() {
    return identityName;
  }

  /**
   * 通过别名查找
   *
   * @return 返回别名
   */
  @Override
  public final String[] alias() {
    return alias;
  }

  /**
   * 该可查找信息注释信息
   *
   * @return 可查找信息注释信息
   */
  @Override
  public final String comment() {
    return comment;
  }

  /**
   * 是否忽略身份牌大小写
   *
   * @return {@code true}忽略，否则 {@code false}
   */
  @Override
  public final boolean isIgnoreNameCase() {
    return isIgnoreNameCase;
  }

  /**
   * 是否忽略别名大小写
   *
   * @return {@code true}忽略，否则 {@code false}
   */
  @Override
  public final boolean isIgnoreAliasCase() {
    return isIgnoreAliasCase;
  }
}
