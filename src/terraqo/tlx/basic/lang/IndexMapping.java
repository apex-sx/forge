package pxf.toolkit.basic.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * 索引映射
 *
 * @author potatoxf
 * @date 2021/1/14
 */
public class IndexMapping<T> {

  private final T ref;
  private final int idx;

  protected IndexMapping(T ref, int idx) {
    this.ref = ref;
    this.idx = idx;
  }

  @Nonnull
  public static <T> IndexMapping<T> of(T t) {
    return new IndexMapping<>(t, -1);
  }

  /**
   * 构建对应的索引映射
   *
   * @param list 要建立原映射的列表
   * @param <T> 元素类型
   * @return 返回 {@code List<IndexMapping<T>>}
   */
  @Nonnull
  public static <T extends Comparable<T>> List<IndexMapping<T>> of(@Nonnull List<T> list) {
    int size = list.size();
    List<IndexMapping<T>> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      result.add(new IndexMapping<>(list.get(i), i));
    }
    Collections.sort(list);
    return Collections.unmodifiableList(result);
  }

  /**
   * 构建对应的索引映射
   *
   * @param list 要建立原映射的列表
   * @param comparator 比较器
   * @param <T> 元素类型
   * @return 返回 {@code List<IndexMapping<T>>}
   */
  @Nonnull
  public static <T> List<IndexMapping<T>> of(
      @Nonnull List<T> list, @Nonnull Comparator<T> comparator) {
    int size = list.size();
    List<IndexMapping<T>> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      result.add(new IndexMapping<>(list.get(i), i));
    }
    result.sort((o1, o2) -> comparator.compare(o1.ref(), o2.ref()));
    return Collections.unmodifiableList(result);
  }

  public final T ref() {
    return ref;
  }

  public final int idx() {
    return idx;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ref);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    IndexMapping<?> that = (IndexMapping<?>) object;
    return ref.equals(that.ref);
  }
}
