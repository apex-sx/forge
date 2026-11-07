package potatoxf.helper.api.function;

/**
 * Represents a function that accepts two arguments and produces a result. This is the two-arity
 * specialization of {@link FactoryThrow}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #handle(Object)}.
 *
 * @param <P> the type of the first argument to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception
 * @author potatoxf
 * @date 2022/07/02
 * @see FactoryThrow
 */
public interface FactoryThrow<P, R, E extends Throwable> {

  /**
   * 将元素映射成目标元素
   *
   * @param p 指定参数
   * @return 函数返回结果
   * @throws E 抛出异常
   */
  R handle(P p) throws E;
}
