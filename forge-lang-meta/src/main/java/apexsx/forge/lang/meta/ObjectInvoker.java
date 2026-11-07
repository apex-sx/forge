package apexsx.forge.lang.meta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ObjectInvoker 是一个基于反射机制的工具类，用于动态调用对象的方法和创建实例。
 * 它通过缓存类及其方法来提高性能，并支持多种返回值类型的调用。
 * <p>
 * 主要功能包括：
 * - 添加支持的类到缓存中以便后续使用
 * - 调用指定对象的方法并处理异常情况
 * - 创建新实例，支持不同构造函数参数类型
 * - 提供类型安全的返回值转换
 *
 * @author zhitron
 */
public class ObjectInvoker {
    /**
     * 缓存支持的类及其方法映射。
     * 键为类名，值为该类中方法的映射（方法名为键，Method/Constructor对象为值）。
     */
    private final Map<String, ClassMetadataCache> cacheClassInfo = new HashMap<>(2, 1);

    /**
     * 构造函数接受一个 Consumer 函数式接口，用于注册 addSupportClass 方法。
     * 这样可以在外部定义如何添加支持的类。
     *
     * @param consumer 消费者函数，用于注册添加支持类的方法
     */
    public ObjectInvoker(Consumer<BiConsumer<String, ClassLoader>> consumer) {
        consumer.accept((className, classLoader) -> {
            Class<?> clazz = UniversalClass.loadClass(className, classLoader);
            if (clazz != null) {
                cacheClassInfo.computeIfAbsent(className, k -> UniversalClass.loadClassMetadataCache(clazz));
            }
        });
    }

    /**
     * 调用返回值为 void 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     */
    public void invokeToVoid(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 boolean 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public boolean invokeToBoolean(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 int 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public int invokeToInt(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 long 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public long invokeToLong(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 double 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public double invokeToDouble(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 float 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public float invokeToFloat(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 short 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public short invokeToShort(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 byte 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public byte invokeToByte(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用返回值为 char 的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用结果
     */
    public char invokeToChar(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return invoke(object, methodName, parameterTypes, args);
    }

    /**
     * 调用指定对象的方法。
     *
     * @param object         被调用方法的对象
     * @param methodName     方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @param <R>            返回值类型
     * @return 方法调用的结果
     * @throws RuntimeException 如果方法调用过程中发生异常
     */
    @SuppressWarnings("unchecked")
    public <R> R invoke(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (methodName == null || methodName.isEmpty()) {
            throw new IllegalArgumentException("Method name cannot be null or empty");
        }
        if (parameterTypes == null) {
            throw new IllegalArgumentException("Parameter types array cannot be null");
        }
        Method method = getClassMetadataCache(object.getClass()).getPublicInheritedMethod(methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Method not found: " + methodName + " in meta " + object.getClass().getName());
        }
        Object result = UniversalClass.invokeMethod(object, method, args);
        if (result == null) {
            return null;
        }
        try {
            return (R) result;
        } catch (ClassCastException e) {
            throw new RuntimeException("Return type conversion error: " + e.getMessage(), e);
        }
    }

    /**
     * 根据参数类型匹配并创建实例
     *
     * @param parameterTypes 构造函数参数类型数组
     * @param args           构造函数参数值数组
     * @param <R>            返回值类型
     * @return 创建的实例
     * @throws RuntimeException 如果找不到匹配的构造函数或参数不匹配
     */
    public <R> R newInstance(Class<?>[] parameterTypes, Object... args) {
        UniversalClass.checkCompatibility(parameterTypes, args);
        Set<String> classNames = cacheClassInfo.keySet();
        for (String className : classNames) {
            Class<?> clazz = cacheClassInfo.get(className).getMetadataClass();
            try {
                return newInstance(clazz, parameterTypes, args);
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("Unsupported types [" + UniversalClass.getConstructorKey(parameterTypes) + "] Only supported types" + classNames);
    }

    /**
     * 通过反射调用指定类的构造函数并创建实例。
     *
     * @param className      类的全限定名
     * @param classLoader    类加载器
     * @param parameterTypes 构造函数参数类型数组
     * @param args           构造函数参数值数组
     * @param <R>            返回值类型
     * @return 创建的实例
     * @throws IllegalArgumentException 如果参数不合法（如类名为空、参数数量不匹配等）
     */
    public <R> R newInstance(String className, ClassLoader classLoader, Class<?>[] parameterTypes, Object... args) {
        Class<?> clazz = UniversalClass.loadClass(className, classLoader);
        if (clazz == null) {
            throw new IllegalArgumentException("Class [" + className + "] not found");
        }
        return this.newInstance(clazz, parameterTypes, args);
    }

    /**
     * 调用指定类的构造函数并创建实例。
     *
     * @param clazz          类
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @param <R>            返回值类型
     * @return 方法调用的结果
     * @throws IllegalArgumentException 如果类为 null
     */
    public <R> R newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object... args) {
        Constructor<?> constructor = getClassMetadataCache(clazz).getPublicDeclaredConstructor(parameterTypes);
        if (constructor == null) {
            throw new IllegalArgumentException("No suitable constructor found for meta: " + clazz);
        }
        //noinspection unchecked
        return (R) UniversalClass.invokeConstructor(constructor, args);
    }

    /**
     * 获取指定类的方法缓存。
     *
     * @param clazz 类对象
     * @return 该类的方法缓存映射
     * @throws RuntimeException 如果类不在支持列表中
     */
    private ClassMetadataCache getClassMetadataCache(Class<?> clazz) {
        if (clazz == null) {
            throw new RuntimeException("Class parameter cannot be null");
        }
        // 直接通过类名查找缓存
        ClassMetadataCache classMetadataCache = cacheClassInfo.get(clazz.getName());
        if (classMetadataCache != null) {
            return classMetadataCache;
        }
        // 如果直接查找失败，再进行继承关系检查
        Set<String> classNames = cacheClassInfo.keySet();
        for (String className : classNames) {
            classMetadataCache = cacheClassInfo.get(className);
            Class<?> type = classMetadataCache.getMetadataClass();
            if (type.isAssignableFrom(clazz)) {
                return classMetadataCache;
            }
        }
        throw new RuntimeException("Unsupported types [" + clazz.getName() + "] Only supported types: " + classNames);
    }
}
