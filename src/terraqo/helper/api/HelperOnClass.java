package potatoxf.helper.api;

import potatoxf.helper.api.function.SupplierThrow;
import potatoxf.helper.api.iterate.ParentIterableOnClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author potatoxf
 * @date 2022/7/3
 */
public final class HelperOnClass {

  /**
   * 查找域
   *
   * @param target 目标
   * @param fieldName 域名
   * @param includeModifiers 包括修饰符
   * @param excludeModifiers 排除修饰符
   * @return {@code Field}
   */
  public static Field lookupField(
      @Nonnull Object target,
      @Nonnull String fieldName,
      @Nullable Integer includeModifiers,
      @Nullable Integer excludeModifiers) {
    final Class<?> clz = target instanceof Class ? (Class<?>) target : target.getClass();
    Field field = null;
    for (Class<?> c : new ParentIterableOnClass(clz)) {
      try {
        field = c.getDeclaredField(fieldName);
        if (isMatchModifiers(field, includeModifiers, excludeModifiers)) {
          break;
        }
      } catch (NoSuchFieldException e) {
        field = null;
      }
    }
    return field;
  }

  /**
   * 获取当前属性
   *
   * @param clz 类
   * @param includeModifiers 包括修饰符
   * @param excludeModifiers 排除修饰符
   * @return {@code Field[]}
   */
  public static Field[] getContextFields(
      @Nonnull Class<?> clz,
      @Nullable Integer includeModifiers,
      @Nullable Integer excludeModifiers) {
    List<Field[]> list = new ArrayList<>();
    for (Class<?> c : new ParentIterableOnClass(clz)) {
      list.add(getCurrentFields(c, includeModifiers, excludeModifiers));
    }
    return list.stream().flatMap(Arrays::stream).toArray(Field[]::new);
  }

  /**
   * 获取当前属性
   *
   * @param clz 类
   * @param includeModifiers 包括修饰符
   * @param excludeModifiers 排除修饰符
   * @return {@code Field[]}
   */
  public static Field[] getCurrentFields(
      @Nonnull Class<?> clz,
      @Nullable Integer includeModifiers,
      @Nullable Integer excludeModifiers) {
    return Arrays.stream(clz.getDeclaredFields())
        .filter(field -> isMatchModifiers(field, includeModifiers, excludeModifiers))
        .toArray(Field[]::new);
  }
  /**
   * 获取可追踪域值
   *
   * @param target 目标，如果获取静态域传入Class，如果是对象域则传入对象
   * @param type 指定域类型，不是该类型则不获取，如果传入null则不管域类型
   * @param fieldName 域名
   * @param includeModifiers 包括修饰符
   * @param excludeModifiers 排除修饰符
   * @return 如果是域是final类型，则返回该域上的值，否则返回SupplierThrow
   */
  public static Object getTrackFieldValue(
      Object target,
      Class<?> type,
      @Nonnull String fieldName,
      @Nullable Integer includeModifiers,
      @Nullable Integer excludeModifiers) {
    Object value = null;
    if (target != null) {
      @Nullable final Object object = target instanceof Class ? null : target;
      @Nullable
      final Field field = lookupField(target, fieldName, includeModifiers, excludeModifiers);
      if (field != null) {
        try {
          if (type.equals(field.getType()) || type.isAssignableFrom(field.getType())) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers)) {
              value =
                  (SupplierThrow<Object, Throwable>)
                      () -> {
                        field.setAccessible(true);
                        return field.get(object);
                      };
            } else {
              field.setAccessible(true);
              value = field.get(object);
            }
          }
        } catch (Exception ignored) {
        }
      }
    }
    return value;
  }

  /**
   * 获取父类泛型类型
   *
   * @param fromType 从哪个类型获取
   * @param index 获取第几个
   * @return {@code Class<?>}
   * @throws IllegalArgumentException 如果索引超出泛型个数
   */
  public static Class<?> extractGenericFromSuperclass(Class<?> fromType, int index) {
    List<Class<?>> list = new ArrayList<>();
    for (Class<?> from = fromType; from != null; from = from.getSuperclass()) {
      list.add(from);
      Type superclass = from.getGenericSuperclass();
      Type[] actualTypeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
      if (actualTypeArguments.length <= index) {
        continue;
      }
      try {
        return Class.forName(((Class<?>) actualTypeArguments[index]).getName());
      } catch (ClassNotFoundException ignored) {
      }
    }
    throw new IllegalArgumentException(
        String.format("No generic class found in [%d] on the parent class of %s", index, list));
  }

  /**
   * 匹配修饰符
   *
   * @param member 成员
   * @param includeModifiers 包括修饰符
   * @param excludeModifiers 排除修饰符
   * @return 是否匹配
   */
  public static boolean isMatchModifiers(
      Member member, @Nullable Integer includeModifiers, @Nullable Integer excludeModifiers) {
    int modifiers = member.getModifiers();
    return (includeModifiers == null || (includeModifiers & modifiers) == includeModifiers)
        && (excludeModifiers == null || (excludeModifiers & modifiers) == 0);
  }
}
