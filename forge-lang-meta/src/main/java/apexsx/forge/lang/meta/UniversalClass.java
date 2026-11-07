package apexsx.forge.lang.meta;

import apexsx.forge.core.BasicTypeInformation;
import apexsx.forge.core.Logger;
import apexsx.forge.core.UniversalConstant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 通用工具类，提供反射相关的各种操作方法
 * 包括类加载、实例创建、方法调用、字段访问等功能
 *
 * @author zhitron
 */
public class UniversalClass {
    /**
     * 私有构造函数，防止外部实例化
     */
    protected UniversalClass() {
    }

    /**
     * 获取类的缓存实例（默认使用懒加载）
     *
     * @param clazz 需要获取缓存的类
     * @return 类对应的ClassMetadataCache实例
     */
    public static ClassMetadataCache loadClassMetadataCache(Class<?> clazz) {
        return UniversalClass.loadClassMetadataCache(clazz, true);
    }

    /**
     * 获取类的缓存实例
     *
     * @param clazz 需要获取缓存的类
     * @param lazy  是否使用懒加载模式，true表示延迟加载元数据，false表示立即加载
     * @return 类对应的ClassMetadataCache实例
     */
    public static ClassMetadataCache loadClassMetadataCache(Class<?> clazz, boolean lazy) {
        return ClassMetadataCache.of(clazz, lazy);
    }

    /**
     * 清除所有缓存
     */
    public static void clearClassMetadataCache() {
        ClassMetadataCache.clear();
    }

    /**
     * 清除特定类的缓存
     *
     * @param clazz 需要清除缓存的类
     */
    public static void removeClassMetadataCache(Class<?> clazz) {
        ClassMetadataCache.remove(clazz);
    }

    /**
     * 检查目标类型与参数值是否兼容
     *
     * @param targetTypes 目标类型数组
     * @param parameters  参数值数组
     * @throws IllegalArgumentException 当参数与目标类型不兼容时抛出异常
     */
    public static void checkCompatibility(Class<?>[] targetTypes, Object[] parameters) {
        if (!UniversalClass.isCompatibility(targetTypes, parameters)) {
            throw new IllegalArgumentException("Parameters are not compatible with target types");
        }
    }

    /**
     * 检查目标类型与参数类型是否兼容
     *
     * @param targetTypes    目标类型数组
     * @param parameterTypes 参数类型数组
     * @throws IllegalArgumentException 当参数类型与目标类型不兼容时抛出异常
     */
    public static void checkCompatibility(Class<?>[] targetTypes, Class<?>[] parameterTypes) {
        if (!UniversalClass.isCompatibility(targetTypes, parameterTypes)) {
            throw new IllegalArgumentException("Parameter types are not compatible with target types");
        }
    }

    /**
     * 判断目标类型与参数类型是否兼容
     *
     * @param targetTypes 目标类型，不能为null
     * @param parameters  参数类型，可以为null
     * @return 如果类型兼容返回true，否则返回false
     */
    public static boolean isCompatibility(Class<?>[] targetTypes, Object[] parameters) {
        if (targetTypes == null || parameters == null || targetTypes.length != parameters.length) {
            return false;
        }
        for (int i = 0; i < targetTypes.length; i++) {
            if (!UniversalClass.isCompatibility(targetTypes[i], parameters[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断目标类型与参数类型是否兼容
     *
     * @param targetTypes    目标类型，不能为null
     * @param parameterTypes 参数类型，可以为null
     * @return 如果类型兼容返回true，否则返回false
     * @throws IllegalArgumentException 当targetType为null时抛出异常
     */
    public static boolean isCompatibility(Class<?>[] targetTypes, Class<?>[] parameterTypes) {
        if (targetTypes == null || parameterTypes == null || targetTypes.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < targetTypes.length; i++) {
            if (!UniversalClass.isCompatibility(targetTypes[i], parameterTypes[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断目标类型与参数类型是否兼容
     *
     * @param targetType 目标类型，不能为null
     * @param parameter  参数对象，可以为null
     * @return 如果类型兼容返回true，否则返回false
     * @throws IllegalArgumentException 当targetType为null时抛出异常
     */
    public static boolean isCompatibility(Class<?> targetType, Object parameter) {
        // 委托给UniversalClass的isCompatibility方法进行实际的兼容性检查
        // 如果parameter为null，则传入null作为参数类型；否则传入parameter的实际类型
        return UniversalClass.isCompatibility(targetType, parameter == null ? null : parameter.getClass());
    }

    /**
     * 判断目标类型与参数类型是否兼容
     *
     * @param targetType    目标类型，不能为null
     * @param parameterType 参数类型，可以为null
     * @return 如果类型兼容返回true，否则返回false
     * @throws IllegalArgumentException 当targetType为null时抛出异常
     */
    public static boolean isCompatibility(Class<?> targetType, Class<?> parameterType) {
        if (targetType == null) {
            return false;
        }
        // 处理参数类型为null的情况
        if (parameterType == null) {
            return !targetType.isPrimitive();
        }
        // 检查数组类型的兼容性
        boolean targetTypeArray = targetType.isArray();
        boolean parameterTypeArray = parameterType.isArray();
        if (targetTypeArray != parameterTypeArray) {
            return false;
        }
        if (targetTypeArray && parameterTypeArray) {
            return targetType.isAssignableFrom(parameterType);
        }
        // 检查基本类型和包装类型的兼容性
        BasicTypeInformation basicTypeInformation = UniversalConstant.BASIC_TYPE_INFORMATION.get(targetType);
        if (basicTypeInformation != null) {
            if (targetType.isPrimitive()) {
                return basicTypeInformation.primitiveCompatibleTypes.contains(parameterType);
            } else {
                return basicTypeInformation.wrapperCompatibleTypes.contains(parameterType);
            }
        } else {
            // 检查继承关系的兼容性
            return targetType.isAssignableFrom(parameterType);
        }
    }

    /**
     * 使用指定的类加载器加载类
     *
     * @param className   类名字符串
     * @param classLoader 类加载器
     * @return 加载的类对象，如果加载失败返回 null
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        if (className == null) {
            throw new IllegalArgumentException("The meta name cannot be null");
        }
        className = className.trim();
        if (className.isEmpty()) {
            throw new IllegalArgumentException("The meta name cannot be empty");
        }
        try {
            return Class.forName(className, true, classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader);
        } catch (Throwable e) {
            return Logger.warnOrThrowError(false, e, "Error to getting meta from '" + className + "'");
        }
    }

    /**
     * 通过反射调用指定类的构造函数并创建实例。
     *
     * @param className      类的全限定名
     * @param classLoader    类加载器
     * @param parameterTypes 构造函数参数类型数组
     * @param args           构造函数参数值数组
     * @return 创建的实例
     * @throws IllegalArgumentException 如果参数不合法（如类名为空、参数数量不匹配等）
     */
    public static Object newInstance(String className, ClassLoader classLoader, Class<?>[] parameterTypes, Object... args) {
        Class<?> clazz = UniversalClass.loadClass(className, classLoader);
        if (clazz == null) {
            throw new IllegalArgumentException("Class [" + className + "] not found");
        }
        return UniversalClass.newInstance(clazz, parameterTypes, args);
    }

    /**
     * 使用反射调用构造函数创建实例。
     *
     * @param clazz          类对象
     * @param parameterTypes 构造函数参数类型数组
     * @param args           构造函数参数值数组
     * @return 创建的实例
     * @throws RuntimeException 如果构造函数调用失败
     */
    public static Object newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object... args) {
        // 参数校验：检查类对象是否为空
        if (clazz == null) {
            throw new IllegalArgumentException("The meta must not be null");
        }
        // 检查参数类型和参数值的兼容性
        UniversalClass.checkCompatibility(parameterTypes, args);
        // 从缓存中获取指定参数类型的公共构造函数
        Constructor<?> constructor = UniversalClass.loadClassMetadataCache(clazz).getPublicDeclaredConstructor(parameterTypes);
        if (constructor == null) {
            throw new IllegalArgumentException("No suitable constructor found for meta: " + clazz.getName());
        }
        return UniversalClass.invokeConstructor(constructor, args);
    }

    /**
     * 调用指定构造函数创建对象实例。
     *
     * @param constructor 要调用的构造函数对象
     * @param args        构造函数参数值数组
     * @return 构造函数调用创建的对象实例
     * @throws RuntimeException 如果构造函数调用过程中发生异常
     */
    public static Object invokeConstructor(Constructor<?> constructor, Object... args) {
        // 检查构造函数对象是否为null
        if (constructor == null) {
            throw new IllegalArgumentException("The constructor must not be null");
        }
        // 检查构造函数参数类型与实际参数的兼容性
        UniversalClass.checkCompatibility(constructor.getParameterTypes(), args);
        try {
            // 执行构造函数调用并返回结果
            return constructor.newInstance(args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access while invoking constructor: " + constructor, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by invoked constructor: " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to instantiate meta: " + constructor, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke constructor: " + constructor, e);
        }
    }

    /**
     * 调用指定对象的声明方法。
     *
     * @param object         被调用方法的对象
     * @param method         要调用的方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用的结果
     * @throws RuntimeException 如果方法调用过程中发生异常
     */
    public static Object invokeDeclaredMethod(Object object, String method, Class<?>[] parameterTypes, Object... args) {
        return UniversalClass.invokeMethod(object, UniversalClass.loadClassMetadataCache(object.getClass()).getDeclaredMethod(method, parameterTypes), args);
    }

    /**
     * 调用指定对象的继承方法。
     *
     * @param object         被调用方法的对象
     * @param method         要调用的方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用的结果
     * @throws RuntimeException 如果方法调用过程中发生异常
     */
    public static Object invokeInheritedMethod(Object object, String method, Class<?>[] parameterTypes, Object... args) {
        return UniversalClass.invokeMethod(object, UniversalClass.loadClassMetadataCache(object.getClass()).getInheritedMethod(method, parameterTypes), args);
    }

    /**
     * 调用指定对象的公共声明方法。
     *
     * @param object         被调用方法的对象
     * @param method         要调用的方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用的结果
     * @throws RuntimeException 如果方法调用过程中发生异常
     */
    public static Object invokePublicDeclaredMethod(Object object, String method, Class<?>[] parameterTypes, Object... args) {
        return UniversalClass.invokeMethod(object, UniversalClass.loadClassMetadataCache(object.getClass()).getPublicDeclaredMethod(method, parameterTypes), args);
    }

    /**
     * 调用指定对象的公共继承方法。
     *
     * @param object         被调用方法的对象
     * @param method         要调用的方法名称
     * @param parameterTypes 方法参数类型数组
     * @param args           方法参数值数组
     * @return 方法调用的结果
     * @throws RuntimeException 如果方法调用过程中发生异常
     */
    public static Object invokePublicInheritedMethod(Object object, String method, Class<?>[] parameterTypes, Object... args) {
        return UniversalClass.invokeMethod(object, UniversalClass.loadClassMetadataCache(object.getClass()).getPublicInheritedMethod(method, parameterTypes), args);
    }

    /**
     * 调用指定对象的方法。
     *
     * @param object 被调用方法的对象
     * @param method 要调用的方法对象
     * @param args   方法参数值数组
     * @return 方法调用的结果
     * @throws RuntimeException 如果方法调用过程中发生异常
     */
    public static Object invokeMethod(Object object, Method method, Object... args) {
        // 检查方法对象是否为null
        if (method == null) {
            throw new IllegalArgumentException("The method must not be null");
        }
        // 检查方法参数类型与实际参数的兼容性
        UniversalClass.checkCompatibility(method.getParameterTypes(), args);
        try {
            // 执行方法调用并返回结果
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access while invoking method: " + method, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by invoked method: " + method, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + method, e);
        }
    }

    /**
     * 获取指定对象的声明字段值。
     *
     * @param object 被获取字段值的对象
     * @param field  要获取值的字段名称
     * @return 字段值获取的结果
     * @throws RuntimeException 如果字段值获取过程中发生异常
     */
    public static Object invokeDeclaredField(Object object, String field) {
        return UniversalClass.invokeField(object, UniversalClass.loadClassMetadataCache(object.getClass()).getDeclaredField(field));
    }

    /**
     * 获取指定对象的继承字段值。
     *
     * @param object 被获取字段值的对象
     * @param field  要获取值的字段名称
     * @return 字段值获取的结果
     * @throws RuntimeException 如果字段值获取过程中发生异常
     */
    public static Object invokeInheritedField(Object object, String field) {
        return UniversalClass.invokeField(object, UniversalClass.loadClassMetadataCache(object.getClass()).getInheritedField(field));
    }

    /**
     * 获取指定对象的公共声明字段值。
     *
     * @param object 被获取字段值的对象
     * @param field  要获取值的字段名称
     * @return 字段值获取的结果
     * @throws RuntimeException 如果字段值获取过程中发生异常
     */
    public static Object invokePublicDeclaredField(Object object, String field) {
        return UniversalClass.invokeField(object, UniversalClass.loadClassMetadataCache(object.getClass()).getPublicDeclaredField(field));
    }

    /**
     * 获取指定对象的公共继承字段值。
     *
     * @param object 被获取字段值的对象
     * @param field  要获取值的字段名称
     * @return 字段值获取的结果
     * @throws RuntimeException 如果字段值获取过程中发生异常
     */
    public static Object invokePublicInheritedField(Object object, String field) {
        return UniversalClass.invokeField(object, UniversalClass.loadClassMetadataCache(object.getClass()).getPublicInheritedField(field));
    }

    /**
     * 获取指定对象的字段值。
     *
     * @param object 被获取字段值的对象
     * @param field  要获取值的字段对象
     * @return 字段值获取的结果
     * @throws RuntimeException 如果字段值获取过程中发生异常
     */
    public static Object invokeField(Object object, Field field) {
        // 检查字段对象是否为null
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        try {
            // 获取字段值并返回结果
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access while getting field value: " + field, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field value: " + field, e);
        }
    }

    /**
     * 生成构造函数的唯一标识符，用于在缓存中作为 key
     * 该方法将构造函数视为名为"&lt;init&gt;"的特殊方法来处理
     *
     * @param parameterTypes 构造函数参数类型数组
     * @return 构造函数的唯一标识符字符串，格式为"&lt;init&gt;(参数类型1,参数类型2,...)"
     */
    public static String getConstructorKey(Class<?>[] parameterTypes) {
        return UniversalClass.getMethodKey("<init>", parameterTypes);
    }

    /**
     * 生成方法的唯一标识符，用于在缓存中作为 key
     * 标识符格式为"方法名(参数类型1,参数类型2,...)"
     *
     * @param name           方法名称
     * @param parameterTypes 方法参数类型数组
     * @return 方法的唯一标识符字符串
     */
    public static String getMethodKey(String name, Class<?>[] parameterTypes) {
        if (name == null) {
            throw new IllegalArgumentException("The name must not be null");
        }
        StringBuilder methodKey = new StringBuilder(100);
        methodKey.append(name.trim());
        methodKey.append('(');
        if (parameterTypes.length > 0) {
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType == null) {
                    throw new NullPointerException("Parameter type cannot be null");
                }
                methodKey.append(parameterType.getName()).append(',');
            }
            methodKey.setLength(methodKey.length() - 1); // 移除最后一个逗号
        }
        methodKey.append(')');
        return methodKey.toString();
    }

    /**
     * 获取指定基本数据类型对应的包装类类型
     *
     * @param clazz 基本数据类型类对象，不能为null
     * @return 对应的包装类类型，如果输入不是基本数据类型则返回原类对象
     * @throws IllegalArgumentException 当clazz为null时抛出异常
     */
    public static Class<?> getWrapperType(Class<?> clazz) {
        return UniversalClass.getClassByBasicTypeInformation(clazz, e -> e.wrapperType);
    }

    /**
     * 获取指定包装类类型对应的基本数据类型
     *
     * @param clazz 包装类类型对象，不能为null
     * @return 对应的基本数据类型，如果输入不是包装类类型则返回原类对象
     * @throws IllegalArgumentException 当clazz为null时抛出异常
     */
    public static Class<?> getPrimitiveType(Class<?> clazz) {
        return UniversalClass.getClassByBasicTypeInformation(clazz, e -> e.primitiveType);
    }

    /**
     * 根据基本类型信息获取对应的类型
     *
     * @param clazz                    输入的类对象，不能为null
     * @param informationClassFunction 用于从BasicTypeInformation中提取目标类型的函数
     * @return 根据函数定义返回对应的基本类型或包装类型
     * @throws IllegalArgumentException 当clazz为null时抛出异常
     */
    private static Class<?> getClassByBasicTypeInformation(Class<?> clazz, Function<BasicTypeInformation, Class<?>> informationClassFunction) {
        if (clazz == null) {
            throw new IllegalArgumentException("The meta must not be null");
        }
        BasicTypeInformation basicTypeInformation = UniversalConstant.BASIC_TYPE_INFORMATION.get(clazz);
        if (basicTypeInformation == null) {
            return clazz;
        }
        return informationClassFunction.apply(basicTypeInformation);
    }
}
