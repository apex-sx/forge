package pxf.toolkit.basic.function;

import java.util.function.Predicate;

/**
 * 优先级断言器
 *
 * @author potatoxf
 * @date 2021/1/16
 */
public interface PriorityPredicate<T> extends Predicate<T>, Comparable<PriorityPredicate<T>> {

  /**
   * 优先级
   *
   * @return 优先级
   */
  default int priority() {
    return 0;
  }

  /**
   * 比较优先级
   *
   * @param o {@code PriorityPredicate<T>}
   * @return 返回比较结果
   */
  @Override
  default int compareTo(PriorityPredicate<T> o) {
    return priority() - o.priority();
  }
}
