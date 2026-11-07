package potatoxf.helper.api.function;

/**
 * Represents a function that accepts two arguments and produces a result. This is the two-arity
 * specialization of {@link FactoryThrow}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #make(Object, Object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception
 * @author potatoxf
 * @date 2022/07/02
 * @see FactoryThrow
 */
public interface BiFactoryThrow<T, U, R, E extends Throwable> {

  /**
   * 将元素映射成目标元素
   *
   * @param t 指定参数1
   * @param u 指定参数2
   * @return 目标元素
   * @throws E 抛出异常
   */
  R make(T t, U u) throws E;
}
