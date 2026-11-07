package pxf.toolkit.basic.unit.impl;

import pxf.toolkit.basic.function.Reference;

/**
 * 等距单位接口
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public interface Isometric<E extends Enum<E> & Isometric<E>> extends Reference<E> {

  /**
   * 精确间隔
   *
   * @return 返回相邻从小单位到大单位的倍数
   */
  int exactInterval();

  /**
   * 当前单位与目标单位的精确转换的因子
   *
   * @param target {@code E extends Enum<E> & UnitIsometric<E>}
   * @return 返回数字等级
   */
  default int exactFactor(final E target) {
    final int diff = poorGrade(target);
    final int interval = exactInterval();
    if (diff > 0) {
      return (int) Math.pow(interval, diff);
    }
    if (diff < 0) {
      return (int) Math.pow(interval, -diff);
    }
    return 1;
  }

  /**
   * 人性化间隔，默认情况等于精确间隔
   *
   * @return 返回相邻从小单位到大单位的倍数
   */
  default int humanInterval() {
    return exactInterval();
  }

  /**
   * 当前单位与目标单位的人性化转换的因子
   *
   * @param target {@code E extends Enum<E> & UnitIsometric<E>}
   * @return 返回数字等级
   */
  default int humanFactor(final E target) {
    final int diff = poorGrade(target);
    final int interval = humanInterval();
    if (diff > 0) {
      return (int) Math.pow(interval, diff);
    }
    if (diff < 0) {
      return (int) Math.pow(interval, -diff);
    }
    return 1;
  }

  /**
   * 登记与与目标的差值
   *
   * <p>默认 {@code level() - target.level()}，代表当前等级与目标等级差值
   *
   * <p>如果大于等于1说明，当前等级高于目标，转换则需要乘以
   *
   * <p>如果小于等于-1说明，当前等级低于目标，转换则需要除以
   *
   * @param target {@code E extends Enum<E> & UnitIsometric<E>}
   * @return 返回数字等级
   */
  default int poorGrade(final E target) {
    return grade() - target.grade();
  }

  /**
   * 获取等级，从小到大依次增加，每相差一就代表两个单位相差一级
   *
   * @return 返回数字等级
   */
  default int grade() {
    return self().ordinal();
  }
}
