package pxf.toolkit.basic.function;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

/**
 * 单例提供器
 *
 * @author potatoxf
 * @date 2021/1/25
 */
public final class SingletonSupplier<T> implements Supplier<T> {

  private final Supplier<? extends T> instanceSupplier;
  private final Supplier<? extends T> defaultSupplier;
  private volatile T singletonInstance;

  private SingletonSupplier(
      T singletonInstance,
      Supplier<? extends T> instanceSupplier,
      Supplier<? extends T> defaultSupplier) {
    this.singletonInstance = singletonInstance;
    this.instanceSupplier = instanceSupplier;
    this.defaultSupplier = defaultSupplier;
  }

  public static <T> SingletonSupplier<T> of(@Nonnull T instance) {
    return new SingletonSupplier<>(instance, null, null);
  }

  public static <T> SingletonSupplier<T> of(@Nonnull Supplier<T> instanceSupplier) {
    return new SingletonSupplier<>(null, instanceSupplier, null);
  }

  public static <T> SingletonSupplier<T> of(
      @Nonnull Supplier<T> instanceSupplier, @Nonnull Supplier<T> defaultSupplier) {
    return new SingletonSupplier<>(null, instanceSupplier, defaultSupplier);
  }

  @Override
  public T get() {
    T instance = singletonInstance;
    if (instance == null) {
      synchronized (this) {
        instance = singletonInstance;
        if (instance == null) {
          if (instanceSupplier != null) {
            instance = instanceSupplier.get();
          }
          if (instance == null && defaultSupplier != null) {
            instance = defaultSupplier.get();
          }
          singletonInstance = instance;
        }
      }
    }
    return instance;
  }
}
