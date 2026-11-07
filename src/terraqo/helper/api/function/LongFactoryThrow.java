package potatoxf.helper.api.function;

/**
 * Represents a supplier of results.
 *
 * <p>There is no requirement that a new or distinct result be returned each time the supplier is
 * invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #handle(Object)} )}.
 *
 * @author potatoxf
 * @date 2022/07/02
 */
public interface LongFactoryThrow<P, E extends Throwable> {
  /**
   * Gets a result.
   *
   * @param p a param
   * @return a result
   * @throws E Throw an exception
   */
  long handle(P p) throws E;
}
