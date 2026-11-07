package potatoxf.helper.api;

import potatoxf.helper.api.base.Option;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author potatoxf
 * @date 2021/8/24
 */
public class HelperOnLambda {
  private HelperOnLambda() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code Optional}. It
   * is no thread-safety of the {@code Optional} returned;
   *
   * @param <T> the value of the input elements
   * @return a {@code Collector} which collects all the input elements into a {@code Optional}
   */
  public static <T> Collector<T, ?, Optional<T>> toSingleOptional() {
    return Collector.of(
        Option::new,
        (a, b) -> {
          if (a.isNoPresent()) {
            a.setValue(b);
          }
        },
        (left, right) -> {
          if (left.isPresent()) {
            return left;
          }
          if (right.isPresent()) {
            return right;
          }
          return left;
        },
        (Function<Option<T>, Optional<T>>) option -> Optional.of(option.get()));
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code Optional}. It
   * is no thread-safety of the {@code Optional} returned;
   *
   * @param <T> the value of the input elements
   * @return a {@code Collector} which collects all the input elements into a {@code Optional}
   */
  public static <T> Collector<T, ?, T> toSingle() {
    return HelperOnLambda.toSingle(Option::get);
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code Optional}. It
   * is no thread-safety of the {@code Optional} returned;
   *
   * @param typeConvert the result type convert
   * @return a {@code Collector} which collects all the input elements into a {@code Optional}
   * @param <T> the value of the input elements
   * @param <R> the value of the output elements
   */
  public static <T, R> Collector<T, ?, R> toSingle(Function<Option<T>, R> typeConvert) {
    return Collector.of(
        Option::new,
        (a, b) -> {
          if (a.isNoPresent()) {
            a.setValue(b);
          }
        },
        (left, right) -> {
          if (left.isPresent()) {
            return left;
          }
          if (right.isPresent()) {
            return right;
          }
          return left;
        },
        typeConvert);
  }

  /**
   * @param <T>
   * @return
   */
  public static <T> Collector<T, ?, List<T>> toSyncList() {
    return Collectors.toCollection(CopyOnWriteArrayList::new);
  }
  /**
   * @param <T>
   * @return
   */
  public static <T> Collector<T, ?, Set<T>> toSyncSet() {
    return Collectors.toCollection(ConcurrentSkipListSet::new);
  }

  /**
   * @param <T>
   * @return
   */
  public static <T> Collector<T, ?, Set<T>> toLinkSet() {
    return Collectors.toCollection(LinkedHashSet::new);
  }
}
