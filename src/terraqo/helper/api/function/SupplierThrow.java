package potatoxf.helper.api.function;

/**
 * Represents a supplier of results.
 *
 * <p>There is no requirement that a new or distinct result be returned each time the supplier is
 * invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 * @author potatoxf
 * @date 2022/07/02
 */
public interface SupplierThrow<T, E extends Throwable> {

  /**
   * Gets a result.
   *
   * @return a result
   * @throws E Throw an exception
   */
  T get() throws E;
}
