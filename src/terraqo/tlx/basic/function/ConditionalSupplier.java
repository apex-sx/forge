package pxf.toolkit.basic.function;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

/**
 * 条件提供器
 *
 * @author potatoxf
 * @date 2021/1/18
 */
public interface ConditionalSupplier<T, S> extends PriorityPredicate<T>, Supplier<S> {

  /**
   * 测试方法是否适合 {@link #get()}返回的 {@code T}处理条件
   *
   * @param t 测试对象
   * @return 如果适合返回 {@code true}，否则 {@code false}
   */
  @Override
  boolean test(@Nonnull T t);

  /**
   * 返回与 {@link #test(T)}相适应的 {@code BuiltIn}
   *
   * @return {@code BuiltIn}
   */
  @Nonnull
  @Override
  S get();
}
