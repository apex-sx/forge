package apexsx.forge.util.concurrent;

import apexsx.forge.core.AbstractInvocationHandler;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 通用的元素池，用于缓存常用的实用类
 *
 * @author potatoxf
 * @date 2021/5/18
 */
public final class ItemPool<T extends ItemPool.PoolItem> {

  /** 统计构造数量 */
  private final AtomicInteger count = new AtomicInteger(0);
  /** 最大大小 */
  private final int maxSize;
  /** 有限阻塞队列 */
  private final ArrayBlockingQueue<PoolItemProxy<T>> blockingQueue;
  /** 池Item构造工厂 */
  private final PoolItemFactory<T> poolItemFactory;
  /** 接口 */
  private final Class<T> interfaceClass;
  /** 类加载器 */
  private final ClassLoader classLoader;

  public ItemPool(Class<T> interfaceClass, PoolItemFactory<T> poolItemFactory) {
    this(interfaceClass, poolItemFactory, 20);
  }

  public ItemPool(Class<T> interfaceClass, PoolItemFactory<T> poolItemFactory, int maxSize) {
    if (!interfaceClass.isInterface()) {
      throw new IllegalArgumentException("The interface class must be interface");
    }
    if (poolItemFactory == null) {
      throw new IllegalArgumentException("The PoolItemFactory must be no null");
    }
    if (maxSize < 0) {
      throw new IllegalArgumentException("The max size must be greater 0");
    }
    this.maxSize = maxSize;
    this.blockingQueue = new ArrayBlockingQueue<>(maxSize);
    this.poolItemFactory = poolItemFactory;
    this.interfaceClass = interfaceClass;
    this.classLoader = interfaceClass.getClassLoader();
  }

  /**
   * 获取一个 {@code PoolItem}元素，
   *
   * <p>如果没有达到上限并且池中没有元素则创建并返回一个元素
   *
   * <p>如果没有达到上限但池中有元素直接返回一个元素
   *
   * <p>如果达到上线则阻塞等待可用元素调用 {@link PoolItem#isClose()}返回一个元素
   *
   * @return 返回一个 {@code PoolItem}元素
   */
  public T takeItem() {
    try {
      int i = count.incrementAndGet();
      if (i <= maxSize) {
        if (blockingQueue.size() == 0 || blockingQueue.size() < count.getAcquire()) {
          PoolItemProxy<T> poolItemProxy = initPoolItemProxy();
          blockingQueue.put(poolItemProxy);
        }
      } else {
        count.decrementAndGet();
      }
      PoolItemProxy<T> item = blockingQueue.take();
      return item.getProxy();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  /**
   * 初始化池元素代理器
   *
   * @return {@code PoolItemProxy<T>}
   */
  private PoolItemProxy<T> initPoolItemProxy() {
    T origin = poolItemFactory.get();
    PoolItemProxy<T> poolItemProxy = new PoolItemProxy<>(this, origin);
    T proxy = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, poolItemProxy);
    poolItemProxy.setProxy(proxy);
    return poolItemProxy;
  }
 /**
   * 释放元素
   *
   * @param item 元素
   */
  private void release(PoolItemProxy<T> item) {
    try {
      blockingQueue.put(item);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  /** 池元素工厂 */
  public interface PoolItemFactory<T extends PoolItem> extends Supplier<T> {

    /**
     * 创建池元素
     *
     * @return {@code T}
     */
    
    @Override
    T get();
  }

  /** 池项目 */
  public interface PoolItem extends Closeable {

    /**
     * 当前元素是否处于关闭状态
     *
     * <p>不要实现该方法，通过 {@link ItemPool}会自动实现该方法
     *
     * @return 如果是返回 {@code true}，否则 {@code false}
     */
    default boolean isClose() {
      throw new UnsupportedOperationException();
    }

    /**
     * 关闭当前元素
     *
     * <p>当元素不被使用的时，必须调用此方法将元素返回到池中
     *
     * <p>如果未返回到池中，就会造成后续线程无法使用到共有元素
     *
     * <p>不要实现该方法，通过 {@link ItemPool}会自动实现该方法
     */
    @Override
    default void close() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * 池元素反射代理器
   *
   * @param <T> {@code <T extends PoolItem>}
   */
  private static class PoolItemProxy<T extends PoolItem> extends AbstractInvocationHandler {

    /** {@code isClose}方法 */
    public static final String IS_CLOSE = "isClose";
    /** {@code close}方法 */
    public static final String CLOSE = "close";
    /** 元素池 */
    private final ItemPool<T> itemPool;
    /** 代理对象 */
    private T proxy;
    /** 是否关闭 */
    private boolean isClose = false;

    private PoolItemProxy(ItemPool<T> itemPool, T origin) {
      super(origin);
      this.itemPool = itemPool;
    }

    /**
     * 获取代理对象
     *
     * @return 代理对象
     */
    private T getProxy() {
      isClose = false;
      return this.proxy;
    }

    /**
     * 设置代理对象
     *
     * @param proxy 代理对象
     */
    private void setProxy(T proxy) {
      this.proxy = proxy;
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
      String name = method.getName();
      if (IS_CLOSE.equals(name)) {
        return isClose;
      }
      if (CLOSE.equals(name)) {
        isClose = true;
        itemPool.release(this);
        return null;
      }
      Class<?> declaringClass = method.getDeclaringClass();
      if (Object.class.equals(declaringClass)) {
        return method.invoke(origin, args);
      }
      if (isClose) {
        throw new IllegalStateException(
            "The current object has been closed and operation is not allowed");
      }
      return method.invoke(origin, args);
    }
  }
}
