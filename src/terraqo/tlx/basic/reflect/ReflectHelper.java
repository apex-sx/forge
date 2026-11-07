package pxf.toolkit.basic.reflect;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import pxf.toolkit.basic.exception.ReflectException;
import pxf.toolkit.basic.exception.UnsupportedException;
import pxf.toolkit.basic.lang.JavaBasicConverter;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.Is;

/**
 * 反射助手类
 *
 * @author potatoxf
 * @date 2021/5/7
 */
public class ReflectHelper {

  private static final Cache CACHE = new Cache(100, 30);

  /**
   * 构建新的对象
   *
   * @param type 对象类型
   * @return 对象
   */
  @Nonnull
  public static Object newInstance(@Nonnull Class<?> type) {
    return CACHE.getConstructorAccess(type).newInstance();
  }
  /**
   * 获取静态域值
   *
   * @param type 类型
   * @param fieldName 域名
   * @return {@code Field}
   */
  @Nonnull
  public static Object getStaticFieldValue(@Nonnull Class<?> type, @Nonnull String fieldName)
      throws NoSuchFieldException {
    FieldAccess fieldAccess = CACHE.getFieldAccess(type);
    Object[] field = getField(fieldAccess, fieldName);
    if (Is.publicStaticVariables((Field) field[1])) {
      return fieldAccess.get(null, (Integer) field[0]);
    }
    throw new NoSuchFieldException("There is no static field '" + fieldName + "'");
  }
  /**
   * 获取静态域
   *
   * @param type 类型
   * @param fieldName 域名
   * @return {@code Field}
   */
  @Nonnull
  public static Field getStaticField(@Nonnull Class<?> type, @Nonnull String fieldName)
      throws NoSuchFieldException {
    Field[] fields = getStaticFields(type);
    for (Field field : fields) {
      if (field.getName().equals(fieldName)) {
        return field;
      }
    }
    throw new NoSuchFieldException("There is no static field '" + fieldName + "'");
  }

  /**
   * 获取静态域
   *
   * @param type 类型
   * @return {@code Field[]}
   */
  @Nonnull
  public static Field[] getStaticFields(@Nonnull Class<?> type) {
    return Arrays.stream(getFields(type)).filter(Is::publicStaticVariables).toArray(Field[]::new);
  }

  /**
   * 获取域值
   *
   * @param type 类型
   * @param fieldName 域名
   * @param target 目标对象
   * @return {@code Field}
   */
  @Nonnull
  public static Object getFieldValue(
      @Nonnull Class<?> type, @Nonnull String fieldName, @Nonnull Object target)
      throws NoSuchFieldException {
    FieldAccess fieldAccess = CACHE.getFieldAccess(type);
    Object[] fields = getField(fieldAccess, fieldName);
    return fieldAccess.get(target, (Integer) fields[0]);
  }
  /**
   * 获取域
   *
   * @param type 类型
   * @param fieldName 域名
   * @return {@code Field}
   */
  @Nonnull
  public static Field getField(@Nonnull Class<?> type, @Nonnull String fieldName)
      throws NoSuchFieldException {
    FieldAccess fieldAccess = CACHE.getFieldAccess(type);
    return (Field) getField(fieldAccess, fieldName)[1];
  }
  /**
   * 获取域
   *
   * @param fieldAccess 域访问器
   * @param fieldName 域名
   * @return {@code Object[]{index,Field}}
   */
  @Nonnull
  private static Object[] getField(@Nonnull FieldAccess fieldAccess, @Nonnull String fieldName)
      throws NoSuchFieldException {
    Field[] fields = fieldAccess.getFields();
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getName().equals(fieldName)) {
        return new Object[] {i, fields[i]};
      }
    }
    throw new NoSuchFieldException("There is no field '" + fieldName + "'");
  }
  /**
   * 获取域
   *
   * @param type 类型
   * @return {@code Field[]}
   */
  @Nonnull
  public static Field[] getFields(@Nonnull Class<?> type) {
    return getFields(CACHE.getFieldAccess(type));
  }

  /**
   * 获取域
   *
   * @param fieldAccess 域访问器
   * @return {@code Field[]}
   */
  @Nonnull
  private static Field[] getFields(@Nonnull FieldAccess fieldAccess) {
    return fieldAccess.getFields();
  }
  /**
   * 调用静态方法
   *
   * @param clz 类
   * @param methodName 方法名
   * @param args 参数
   * @return 返回调用方法的返回值
   * @throws ReflectException 当调用发生异常
   */
  @Nonnull
  public static Object invokeStaticMethodSilently(
      @Nonnull Class<?> clz, @Nonnull String methodName, @Nonnull Object... args) {
    return invokeMethodSilently(clz, null, methodName, args);
  }

  /**
   * 调用方法
   *
   * @param clz 类
   * @param target 目标对象
   * @param methodName 方法名
   * @param args 参数
   * @return 返回调用方法的返回值
   * @throws ReflectException 当调用发生异常
   */
  @Nonnull
  public static Object invokeMethodSilently(
      @Nonnull Class<?> clz, Object target, @Nonnull String methodName, @Nonnull Object... args) {
    try {
      return invokeMethod(clz, target, methodName, args);
    } catch (NoSuchMethodException e) {
      throw new ReflectException(e);
    }
  }
  /**
   * 调用静态方法
   *
   * @param clz 类
   * @param methodName 方法名
   * @param args 参数
   * @return 返回调用方法的返回值
   * @throws NoSuchMethodException 不存在方法异常
   */
  @Nonnull
  public static Object invokeStaticMethod(
      @Nonnull Class<?> clz, @Nonnull String methodName, @Nonnull Object... args)
      throws NoSuchMethodException {
    return invokeMethod(clz, null, methodName, args);
  }

  /**
   * 调用方法
   *
   * @param clz 类
   * @param target 目标对象
   * @param methodName 方法名
   * @param args 参数
   * @return 返回调用方法的返回值
   * @throws NoSuchMethodException 不存在方法异常
   */
  @Nonnull
  public static Object invokeMethod(
      @Nonnull Class<?> clz, Object target, @Nonnull String methodName, @Nonnull Object... args)
      throws NoSuchMethodException {
    MethodAccess methodAccess = CACHE.getMethodAccess(clz);
    Object[] result = getMethodRealParams(methodAccess, clz, methodName, args);
    int methodIndex = (int) result[0];
    Object[] realParams = (Object[]) result[1];
    return methodAccess.invoke(target, methodIndex, realParams);
  }

  /**
   * 获取方法的真实参数
   *
   * @param clz 类
   * @param methodName 方法名
   * @param args 参数
   * @return {@code Object[]}返回参数
   * @throws NoSuchMethodException 不存在方法异常
   */
  @Nonnull
  public static Object[] getMethodRealParams(
      @Nonnull Class<?> clz, @Nonnull String methodName, @Nonnull Object... args)
      throws NoSuchMethodException {
    MethodAccess methodAccess = CACHE.getMethodAccess(clz);
    return (Object[]) getMethodRealParams(methodAccess, clz, methodName, args)[1];
  }
  /**
   * 获取方法的真实参数
   *
   * @param clz 类
   * @param methodName 方法名
   * @param args 参数
   * @return {@code Object[]{index,Object[]}}返回索引和参数
   * @throws NoSuchMethodException 不存在方法异常
   */
  @Nonnull
  private static Object[] getMethodRealParams(
      @Nonnull MethodAccess methodAccess,
      @Nonnull Class<?> clz,
      @Nonnull String methodName,
      @Nonnull Object... args)
      throws NoSuchMethodException {
    String[] methodNames = methodAccess.getMethodNames();
    Class<?>[][] parameterTypes = methodAccess.getParameterTypes();
    Object[] result = args.length == 0 ? Empty.OBJECT_ARRAY : new Object[args.length];
    for (int i = 0; i < methodNames.length; i++) {
      if (!methodNames[i].equals(methodName)) {
        continue;
      }
      Class<?>[] parameterType = parameterTypes[i];
      if (parameterType.length != args.length) {
        continue;
      }
      int j = 0;
      for (; j < parameterType.length; j++) {
        try {
          result[j] = JavaBasicConverter.cast(args[j], parameterType[j], true);
        } catch (UnsupportedException ignored) {
          break;
        }
      }
      if (j == parameterType.length) {
        return new Object[] {i, result};
      }
    }
    throw new NoSuchMethodException(
        "There is no method '"
            + methodName
            + "("
            + (args.length == 0 ? "" : StringUtils.join(args))
            + ")'");
  }

  private static class Cache {
    private final LoadingCache<Class<?>, MethodAccess> methodCache;
    private final LoadingCache<Class<?>, FieldAccess> fieldCache;
    private final LoadingCache<Class<?>, ConstructorAccess> constructorCache;

    @Nonnull
    public MethodAccess getMethodAccess(@Nonnull Class<?> clz) {
      try {
        return methodCache.get(clz);
      } catch (ExecutionException e) {
        throw new ReflectException(e);
      }
    }

    @Nonnull
    public FieldAccess getFieldAccess(@Nonnull Class<?> clz) {
      try {
        return fieldCache.get(clz);
      } catch (ExecutionException e) {
        throw new ReflectException(e);
      }
    }

    @Nonnull
    public ConstructorAccess getConstructorAccess(@Nonnull Class<?> clz) {
      try {
        return constructorCache.get(clz);
      } catch (ExecutionException e) {
        throw new ReflectException(e);
      }
    }

    private Cache(int maxSize, int durationMinute) {
      this.methodCache =
          CacheBuilder.newBuilder()
              .maximumSize(maxSize)
              .expireAfterAccess(durationMinute, TimeUnit.MINUTES)
              .weakValues()
              .build(new AccessLoader<>(MethodAccess::get));
      this.fieldCache =
          CacheBuilder.newBuilder()
              .maximumSize(maxSize)
              .expireAfterAccess(durationMinute, TimeUnit.MINUTES)
              .weakValues()
              .build(new AccessLoader<>(FieldAccess::get));
      this.constructorCache =
          CacheBuilder.newBuilder()
              .maximumSize(maxSize)
              .expireAfterAccess(durationMinute, TimeUnit.MINUTES)
              .weakValues()
              .build(new AccessLoader<>(ConstructorAccess::get));
    }
  }

  private static class AccessLoader<T> extends CacheLoader<Class<?>, T> {

    private final Function<Class<?>, T> loader;

    private AccessLoader(Function<Class<?>, T> loader) {
      this.loader = loader;
    }

    @Override
    public T load(@Nonnull Class<?> clz) {
      return loader.apply(clz);
    }
  }
}
