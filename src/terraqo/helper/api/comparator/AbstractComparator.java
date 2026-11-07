package potatoxf.helper.api.comparator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;

/**
 * 抽象比较器
 *
 * <p>实现对基本可比较对象{@code Null},{@code Comparable}进行实现, 并预留{@code doCompareBefore}和{@code
 * doCompareAfter}接口 对子类进行自定义比较
 *
 * @author potatoxf
 * @date 2022/07/02
 */
@SuppressWarnings("unchecked")
public abstract class AbstractComparator<T> implements Comparator<T> {

  /** 是否空元素放在最后 */
  protected final boolean isNullElementLast;

  public AbstractComparator() {
    this(true);
  }

  public AbstractComparator(boolean isNullElementLast) {
    this.isNullElementLast = isNullElementLast;
  }

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
   * as the first argument is less than, equal to, or greater than the second.
   *
   * <p>
   *
   * <p>The implementor must ensure that {@code sgn(compare(x, y)) == -sgn(compare(y, x))} for all
   * {@code x} and {@code y}. (This implies that {@code compare(x, y)} must throw an exception if
   * and only if {@code compare(y, x)} throws an exception.)
   *
   * <p>
   *
   * <p>The implementor must also ensure that the relation is transitive: {@code ((compare(x, y)>0)
   * && (compare(y, z)>0))} implies {@code compare(x, z)>0}.
   *
   * <p>
   *
   * <p>Finally, the implementor must ensure that {@code compare(x, y)==0} implies that {@code
   * sgn(compare(x, z))==sgn(compare(y, z))} for all {@code z}.
   *
   * <p>
   *
   * <p>It is generally the case, but <i>not</i> strictly required that {@code (compare(x, y)==0) ==
   * (x.equals(y))}. Generally speaking, any comparator that violates this condition should clearly
   * indicate this fact. The recommended language is "Note: this comparator imposes orderings that
   * are inconsistent with equals."
   *
   * <p>
   *
   * <p>In the foregoing description, the notation {@code sgn(}<i>expression</i>{@code )} designates
   * the mathematical <i>signum</i> function, which is defined to return one of {@code -1}, {@code
   * 0}, or {@code 1} according to whether the value of <i>expression</i> is negative, zero, or
   * positive, respectively.
   *
   * <p>Compare two elements, if both are {@code null}, they are equal, if {@code isNullElementLast}
   * is {@code true}, the empty element is the largest, otherwise it is the smallest
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *     equal to, or greater than the second.
   */
  @Override
  public final int compare(T o1, T o2) {
    Integer x = compareNull(o1, o2);
    if (x != null) {
      return x;
    }
    x = doCompareBefore(o1, o2);
    if (x != null) {
      return x;
    }

    x = compareComparable(o1, o2);
    if (x != null) {
      return x;
    }
    return doCompareAfter(o1, o2);
  }

  protected Integer compareNull(Object o1, Object o2) {
    if (o1 == null && o2 == null) {
      return 0;
    }
    if (o1 == null) {
      return isNullElementLast ? 1 : -1;
    }
    if (o2 == null) {
      return isNullElementLast ? -1 : 1;
    }
    return null;
  }

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
   * as the first argument is less than, equal to, or greater than the second.
   *
   * <p>
   *
   * <p>The implementor must ensure that {@code sgn(compare(x, y)) == -sgn(compare(y, x))} for all
   * {@code x} and {@code y}. (This implies that {@code compare(x, y)} must throw an exception if
   * and only if {@code compare(y, x)} throws an exception.)
   *
   * <p>
   *
   * <p>The implementor must also ensure that the relation is transitive: {@code ((compare(x, y)>0)
   * && (compare(y, z)>0))} implies {@code compare(x, z)>0}.
   *
   * <p>
   *
   * <p>Finally, the implementor must ensure that {@code compare(x, y)==0} implies that {@code
   * sgn(compare(x, z))==sgn(compare(y, z))} for all {@code z}.
   *
   * <p>
   *
   * <p>It is generally the case, but <i>not</i> strictly required that {@code (compare(x, y)==0) ==
   * (x.equals(y))}. Generally speaking, any comparator that violates this condition should clearly
   * indicate this fact. The recommended language is "Note: this comparator imposes orderings that
   * are inconsistent with equals."
   *
   * <p>
   *
   * <p>In the foregoing description, the notation {@code sgn(}<i>expression</i>{@code )} designates
   * the mathematical <i>signum</i> function, which is defined to return one of {@code -1}, {@code
   * 0}, or {@code 1} according to whether the value of <i>expression</i> is negative, zero, or
   * positive, respectively.
   *
   * <p>Compare two elements, if both are {@code null}, they are equal, if {@code isNullElementLast}
   * is {@code true}, the empty element is the largest, otherwise it is the smallest
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *     equal to, or greater than the second.
   */
  protected Integer doCompareBefore(T o1, T o2) {
    return null;
  }

  /**
   * 提取对象类必须继承指定类型并且等于指定泛型 {@code class C implements Comparable<C>}
   *
   * @param object 被提取对象
   * @return 如果符合要求返回 {@code object}的 {@code Class}，否则返回 {@code null}
   */
  protected Class<?> classForImplementsGenericInterface(Object object) {
    Class<?> objectClass = object.getClass();
    if (!Comparable.class.isAssignableFrom(objectClass)) {
      return null;
    }
    Type[] genericInterfaces = objectClass.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      if (!(genericInterface instanceof ParameterizedType)) {
        continue;
      }
      ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
      Type rawType = parameterizedType.getRawType();
      if (rawType == Comparable.class) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
          throw new IllegalArgumentException(
              "The specified generic length must be equal to the target type generic length");
        }
        if (actualTypeArguments[0] == objectClass) {
          return objectClass;
        }
      }
    }
    return null;
  }

  /**
   * 比较可比较对象
   *
   * @param o1 {@code o1 instanceof Comparable}
   * @param o2 {@code o2 instanceof Comparable}
   * @return 如果为null则继续比较，否则返回比较值
   */
  protected Integer compareComparable(T o1, T o2) {
    Class<?> kc = classForImplementsGenericInterface(o1);
    if (o2.getClass() != kc) {
      //noinspection rawtypes
      return ((Comparable) o1).compareTo(o2);
    } else {
      Class<?> c1 = o1.getClass();
      Class<?> c2 = o2.getClass();
      if (o1 instanceof Comparable && o2 instanceof Comparable) {
        if (c1.equals(c2)) {
          return ((Comparable<Object>) o1).compareTo(o2);
        }
        if (c1.isAssignableFrom(c2)) {
          return ((Comparable<Object>) o1).compareTo(o2);
        }
        if (c2.isAssignableFrom(c1)) {
          return -((Comparable<Object>) o2).compareTo(o1);
        }
      }
    }
    return null;
  }

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
   * as the first argument is less than, equal to, or greater than the second.
   *
   * <p>
   *
   * <p>The implementor must ensure that {@code sgn(compare(x, y)) == -sgn(compare(y, x))} for all
   * {@code x} and {@code y}. (This implies that {@code compare(x, y)} must throw an exception if
   * and only if {@code compare(y, x)} throws an exception.)
   *
   * <p>
   *
   * <p>The implementor must also ensure that the relation is transitive: {@code ((compare(x, y)>0)
   * && (compare(y, z)>0))} implies {@code compare(x, z)>0}.
   *
   * <p>
   *
   * <p>Finally, the implementor must ensure that {@code compare(x, y)==0} implies that {@code
   * sgn(compare(x, z))==sgn(compare(y, z))} for all {@code z}.
   *
   * <p>
   *
   * <p>It is generally the case, but <i>not</i> strictly required that {@code (compare(x, y)==0) ==
   * (x.equals(y))}. Generally speaking, any comparator that violates this condition should clearly
   * indicate this fact. The recommended language is "Note: this comparator imposes orderings that
   * are inconsistent with equals."
   *
   * <p>
   *
   * <p>In the foregoing description, the notation {@code sgn(}<i>expression</i>{@code )} designates
   * the mathematical <i>signum</i> function, which is defined to return one of {@code -1}, {@code
   * 0}, or {@code 1} according to whether the value of <i>expression</i> is negative, zero, or
   * positive, respectively.
   *
   * <p>Compare two elements, if both are {@code null}, they are equal, if {@code isNullElementLast}
   * is {@code true}, the empty element is the largest, otherwise it is the smallest
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *     equal to, or greater than the second.
   */
  protected abstract int doCompareAfter(T o1, T o2);
}
