package pxf.toolkit.basic.constants;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author potatoxf
 * @date 2021/4/18
 */
public interface FindablePriorityEnumConstant<E extends Enum<E> & FindablePriorityEnumConstant<E>>
    extends FindableEnumConstant<E>, Comparable<E> {

  /**
   * 优先级
   *
   * @param other 另一个元素
   * @return 返回比较值
   */
  @Override
  default int compareTo(E other) {
    return other.ordinal() - self().ordinal();
  }

  /**
   * 返回包含优先级以下的部分
   *
   * @param target 目标值
   * @return 返回小于目标值以下的所有集合
   */
  default Set<E> include(E target) {
    return (Set<E>)
        Arrays.stream(self().getClass().getEnumConstants())
            .filter(e -> target.compareTo((E) e) <= 0)
            .collect(Collectors.toUnmodifiableSet());
  }
}
