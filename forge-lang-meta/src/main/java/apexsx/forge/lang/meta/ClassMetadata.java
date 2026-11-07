package apexsx.forge.lang.meta;


import apexsx.forge.core.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 类元数据缓存工具类，用于高效存储和检索类的反射信息。
 * <p>
 * 该类通过预计算和缓存机制，避免了重复的反射操作，提升性能。
 * 使用 Caffeine 缓存框架进行类元数据的缓存管理。
 *
 * @author Terraqo
 */
public final class ClassMetadata {

    // 使用 Caffeine 缓存框架缓存 ClassMetadata 实例，避免重复创建
    private static final Map<Class<?>, ClassMetadata> CACHE = new ConcurrentHashMap<>();

    // 当前类的 Class 对象
    private final Class<?> clazz;

    // 父类的 ClassMetadata 实例（暂未使用，保留扩展性）
    private final ClassMetadata parent;

    // 当前类声明的所有字段
    private final Field[] declaredFields;

    // 当前类声明的所有方法
    private final Method[] declaredMethods;

    // 当前类声明的所有构造器
    private final Constructor<?>[] declaredConstructors;

    // 静态字段映射（字段名 -> Field 对象）
    private final Map<String, FieldInfo> staticFields;

    // 非静态字段映射（字段名 -> Field 对象）
    private final Map<String, FieldInfo> nonStaticFields;

    // 静态方法映射（方法唯一键 -> Method 对象）
    private final Map<String, MethodInfo> staticMethods;

    // 非静态方法映射（方法唯一键 -> Method 对象）
    private final Map<String, MethodInfo> nonStaticMethods;

    // 公共构造器映射（构造器唯一键 -> Constructor 对象）
    private final Map<String, ConstructorInfo> publicConstructors;

    // 非公共构造器映射（构造器唯一键 -> Constructor 对象）
    private final Map<String, ConstructorInfo> nonPublicConstructors;

    /**
     * 获取指定类的 ClassMetadata 实例。
     * <p>
     * 该方法通过缓存机制避免重复创建 ClassMetadata 实例，提升性能。
     * 若缓存中不存在，则递归获取父类元数据并创建新的实例。
     *
     * @param clazz 目标类的 Class 对象
     * @return 对应的 ClassMetadata 实例
     */
    public static ClassMetadata of(Class<?> clazz) {
        // 从缓存中获取类元数据，如果不存在则创建新的实例
        return CACHE.computeIfAbsent(clazz, key -> {
            Class<?> superclass = key.getSuperclass();
            ClassMetadata parent = null;
            // 递归获取父类的元数据（如果存在）
            if (superclass != null && superclass != Object.class) {
                parent = ClassMetadata.of(superclass);
            }
            // 创建当前类的元数据实例
            return new ClassMetadata(key, parent);
        });
    }

    /**
     * 构造函数，初始化类元数据并构建字段、方法和构造器的映射关系。
     *
     * @param clazz  当前类的 Class 对象
     * @param parent 父类的 ClassMetadata 实例（可为 null）
     */
    private ClassMetadata(Class<?> clazz, ClassMetadata parent) {
        this.clazz = clazz;
        this.parent = parent;

        // 获取类声明的所有字段、方法和构造器
        this.declaredFields = clazz.getDeclaredFields();
        this.declaredMethods = clazz.getDeclaredMethods();
        this.declaredConstructors = clazz.getDeclaredConstructors();

        // 预先统计各类成员的数量，避免后续重复遍历
        int staticFieldCount = 0, nonStaticFieldCount = 0;
        int staticMethodCount = 0, nonStaticMethodCount = 0;
        int publicConstructorCount = 0, nonPublicConstructorCount = 0;

        // 统计静态/非静态字段数量
        for (Field field : this.declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                staticFieldCount++;
            } else {
                nonStaticFieldCount++;
            }
        }

        // 统计静态/非静态方法数量
        for (Method method : this.declaredMethods) {
            if (Modifier.isStatic(method.getModifiers())) {
                staticMethodCount++;
            } else {
                nonStaticMethodCount++;
            }
        }

        // 统计公共/非公共构造器数量
        for (Constructor<?> constructor : this.declaredConstructors) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                publicConstructorCount++;
            } else {
                nonPublicConstructorCount++;
            }
        }

        // 使用预估容量和加载因子 1.0 初始化 LinkedHashMap，减少扩容开销
        float loadFactor = 1.0f;
        Map<String, FieldInfo> staticFieldsMap = new LinkedHashMap<>(staticFieldCount, loadFactor);
        Map<String, FieldInfo> nonStaticFieldsMap = new LinkedHashMap<>(nonStaticFieldCount, loadFactor);
        Map<String, MethodInfo> staticMethodsMap = new LinkedHashMap<>(staticMethodCount, loadFactor);
        Map<String, MethodInfo> nonStaticMethodsMap = new LinkedHashMap<>(nonStaticMethodCount, loadFactor);
        Map<String, ConstructorInfo> publicConstructorsMap = new LinkedHashMap<>(publicConstructorCount, loadFactor);
        Map<String, ConstructorInfo> nonPublicConstructorsMap = new LinkedHashMap<>(nonPublicConstructorCount, loadFactor);

        // 构建字段映射：按是否为静态分类
        for (Field member : this.declaredFields) {
            String fieldKey = ClassMetadata.getFieldUniqueKey(member);
            int modifiers = member.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            boolean isPublic = Modifier.isPublic(modifiers);
            FieldInfo fieldInfo = new FieldInfo(member, isStatic, isPublic);
            if (isStatic) {
                staticFieldsMap.put(fieldKey, fieldInfo);
            } else {
                nonStaticFieldsMap.put(fieldKey, fieldInfo);
            }
        }

        // 构建方法映射：按是否为静态分类
        for (Method member : this.declaredMethods) {
            String methodKey = ClassMetadata.getMethodUniqueKey(member); // 获取方法唯一键
            int modifiers = member.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            boolean isPublic = Modifier.isPublic(modifiers);
            MethodInfo methodInfo = new MethodInfo(member, isStatic, isPublic);
            if (isStatic) {
                staticMethodsMap.put(methodKey, methodInfo);
            } else {
                nonStaticMethodsMap.put(methodKey, methodInfo);
            }
        }

        // 构建构造器映射：按是否为公共分类
        for (Constructor<?> member : this.declaredConstructors) {
            String constructorKey = ClassMetadata.getConstructorUniqueKey(member); // 获取构造器唯一键
            boolean isPublic = Modifier.isPublic(member.getModifiers());
            ConstructorInfo constructorInfo = new ConstructorInfo(member, isPublic);
            if (Modifier.isPublic(member.getModifiers())) {
                publicConstructorsMap.put(constructorKey, constructorInfo);
            } else {
                nonPublicConstructorsMap.put(constructorKey, constructorInfo);
            }
        }

        // 将映射设为不可变，确保线程安全和数据一致性
        this.staticFields = Collections.unmodifiableMap(staticFieldsMap);
        this.nonStaticFields = Collections.unmodifiableMap(nonStaticFieldsMap);
        this.staticMethods = Collections.unmodifiableMap(staticMethodsMap);
        this.nonStaticMethods = Collections.unmodifiableMap(nonStaticMethodsMap);
        this.publicConstructors = Collections.unmodifiableMap(publicConstructorsMap);
        this.nonPublicConstructors = Collections.unmodifiableMap(nonPublicConstructorsMap);
    }

    /**
     * 生成字段的唯一键（字段名）。
     *
     * @param field 字段对象
     * @return 字段名作为唯一键
     */
    public static String getFieldUniqueKey(Field field) {
        return field.getName();
    }

    /**
     * 生成方法的唯一键（方法名 + 参数类型签名）。
     *
     * @param method 方法对象
     * @return 方法唯一键
     */
    public static String getMethodUniqueKey(Method method) {
        return ClassMetadata.getUniqueKey(method.getName(), method.getParameterTypes());
    }

    /**
     * 生成构造器的唯一键（构造器名 + 参数类型签名）。
     *
     * @param constructor 构造器对象
     * @return 构造器唯一键
     */
    public static String getConstructorUniqueKey(Constructor<?> constructor) {
        return ClassMetadata.getUniqueKey(constructor.getName(), constructor.getParameterTypes());
    }

    /**
     * 根据名称和参数类型生成唯一键。
     *
     * @param name     名称（字段名、方法名或构造器名）
     * @param argTypes 参数类型数组（可为 null）
     * @return 唯一键字符串
     */
    public static String getUniqueKey(String name, Class<?>[] argTypes) {
        if (argTypes == null || argTypes.length == 0) {
            return name; // 无参数时直接返回名称
        }

        // 构建参数类型签名：(type1,type2,...)
        StringBuilder uniqueKeyBuilder = new StringBuilder(name.length() + argTypes.length * 100);
        uniqueKeyBuilder.append(name);
        uniqueKeyBuilder.append('(');
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                uniqueKeyBuilder.append(','); // 参数之间用逗号分隔
            }
            uniqueKeyBuilder.append(argTypes[i].getName()); // 添加参数类型全限定名
        }
        uniqueKeyBuilder.append(')');
        return uniqueKeyBuilder.toString();
    }

    /**
     * 判断类中是否包含指定名称的字段。
     *
     * @param name 字段名称
     * @return 若包含则返回 true，否则返回 false
     */
    public boolean containsField(String name) {
        String uniqueKey = ClassMetadata.getUniqueKey(name, null);
        return staticFields.containsKey(uniqueKey) || nonStaticFields.containsKey(uniqueKey);
    }

    /**
     * 判断类中是否包含指定名称和参数类型的方法。
     *
     * @param name     方法名称
     * @param argTypes 方法参数类型数组
     * @return 若包含则返回 true，否则返回 false
     */
    public boolean containsMethod(String name, Class<?>... argTypes) {
        String uniqueKey = ClassMetadata.getUniqueKey(name, argTypes);
        return staticMethods.containsKey(uniqueKey) || nonStaticMethods.containsKey(uniqueKey);
    }

    /**
     * 判断类中是否包含指定参数类型的构造器。
     *
     * @param argTypes 构造器参数类型数组
     * @return 若包含则返回 true，否则返回 false
     */
    public boolean containsConstructor(Class<?>... argTypes) {
        String uniqueKey = ClassMetadata.getUniqueKey(clazz.getName(), argTypes);
        return publicConstructors.containsKey(uniqueKey) || nonPublicConstructors.containsKey(uniqueKey);
    }

    /**
     * 获取指定名称的字段对象。
     *
     * @param name 字段名称
     * @return 对应的 Field 对象
     * @throws NoSuchFieldException 若字段不存在
     */
    public Field getField(String name) throws NoSuchFieldException {
        return this.getFieldInfo(name).field;
    }

    /**
     * 获取指定名称和参数类型的方法对象。
     *
     * @param name     方法名称
     * @param argTypes 方法参数类型数组
     * @return 对应的 Method 对象
     * @throws NoSuchMethodException 若方法不存在
     */
    public Method getMethod(String name, Class<?>... argTypes) throws NoSuchMethodException {
        return this.getMethodInfo(name, argTypes).method;
    }

    /**
     * 获取指定参数类型的构造器对象。
     *
     * @param argTypes 构造器参数类型数组
     * @return 对应的 Constructor 对象
     * @throws NoSuchMethodException 若构造器不存在
     */
    public Constructor<?> getConstructor(Class<?>... argTypes) throws NoSuchMethodException {
        return this.getConstructorInfo(argTypes).constructor;
    }

    /**
     * 获取指定字段的值。
     *
     * @param field  字段对象
     * @param object 字段所属的对象实例
     * @return 字段的值
     * @throws Throwable 若获取字段值过程中发生异常
     */
    public Object getFieldValue(Field field, Object object) throws Throwable {
        return this.getFieldInfo(field.getName()).getValue(object);
    }

    /**
     * 设置指定字段的值。
     *
     * @param field  字段对象
     * @param object 字段所属的对象实例
     * @param value  要设置的字段值
     * @throws Throwable 若设置字段值过程中发生异常
     */
    public void getFieldValue(Field field, Object object, Object value) throws Throwable {
        this.getFieldInfo(field.getName()).setValue(object, value);
    }

    /**
     * 调用指定方法并返回结果。
     *
     * @param method 方法对象
     * @param object 方法所属的对象实例
     * @param values 方法参数
     * @return 方法调用结果
     * @throws Throwable 若方法调用过程中发生异常
     */
    public Object invokeMethod(Method method, Object object, Object... values) throws Throwable {
        return this.getMethodInfo(method.getName(), method.getParameterTypes()).invoke(object, values);
    }

    /**
     * 获取指定名称的字段信息。
     *
     * @param name 字段名称
     * @return 对应的 FieldInfo 对象
     * @throws NoSuchFieldException 若字段不存在
     */
    private FieldInfo getFieldInfo(String name) throws NoSuchFieldException {
        String uniqueKey = ClassMetadata.getUniqueKey(name, null);
        if (staticFields.containsKey(uniqueKey)) {
            return staticFields.get(uniqueKey);
        } else if (nonStaticFields.containsKey(uniqueKey)) {
            return nonStaticFields.get(uniqueKey);
        } else {
            throw new NoSuchFieldException(String.format("The field '%s' not found in '%s'", uniqueKey, clazz));
        }
    }

    /**
     * 获取指定名称和参数类型的方法信息。
     *
     * @param name     方法名称
     * @param argTypes 方法参数类型数组
     * @return 对应的 MethodInfo 对象
     * @throws NoSuchMethodException 若方法不存在
     */
    private MethodInfo getMethodInfo(String name, Class<?>... argTypes) throws NoSuchMethodException {
        String uniqueKey = ClassMetadata.getUniqueKey(name, argTypes);
        if (staticMethods.containsKey(uniqueKey)) {
            return staticMethods.get(uniqueKey);
        } else if (nonStaticMethods.containsKey(uniqueKey)) {
            return nonStaticMethods.get(uniqueKey);
        } else {
            throw new NoSuchMethodException(String.format("The method '%s' not found in '%s'", uniqueKey, clazz));
        }
    }

    /**
     * 获取指定参数类型的构造器信息。
     *
     * @param argTypes 构造器参数类型数组
     * @return 对应的 ConstructorInfo 对象
     * @throws NoSuchMethodException 若构造器不存在
     */
    private ConstructorInfo getConstructorInfo(Class<?>... argTypes) throws NoSuchMethodException {
        String uniqueKey = ClassMetadata.getUniqueKey(clazz.getName(), argTypes);
        if (publicConstructors.containsKey(uniqueKey)) {
            return publicConstructors.get(uniqueKey);
        } else if (nonPublicConstructors.containsKey(uniqueKey)) {
            return nonPublicConstructors.get(uniqueKey);
        } else {
            throw new NoSuchMethodException(String.format("The constructor '%s' not found in '%s'", uniqueKey, clazz));
        }
    }

    public Field[] getDeclaredFields() {
        return Arrays.copyOf(declaredFields, declaredFields.length);
    }

    public Method[] getDeclaredMethods() {
        return Arrays.copyOf(declaredMethods, declaredMethods.length);
    }

    public Constructor<?>[] getDeclaredConstructors() {
        return Arrays.copyOf(declaredConstructors, declaredConstructors.length);
    }

    /**
     * 获取静态字段的迭代器。
     *
     * @param predicate 过滤条件
     * @return 静态字段的迭代器
     */
    public Iterator<Field> getStaticFieldIterator(Predicate<Field> predicate) {
        return new ItemIterator<>(staticFields, i -> i.field, predicate);
    }

    /**
     * 获取非静态字段的迭代器。
     *
     * @param predicate 过滤条件
     * @return 非静态字段的迭代器
     */
    public Iterator<Field> getNonStaticFieldIterator(Predicate<Field> predicate) {
        return new ItemIterator<>(nonStaticFields, i -> i.field, predicate);
    }

    /**
     * 获取静态方法的迭代器。
     *
     * @param predicate 过滤条件
     * @return 静态方法的迭代器
     */
    public Iterator<Method> getStaticMethodIterators(Predicate<Method> predicate) {
        return new ItemIterator<>(staticMethods, i -> i.method, predicate);
    }

    /**
     * 获取非静态方法的迭代器。
     *
     * @param predicate 过滤条件
     * @return 非静态方法的迭代器
     */
    public Iterator<Method> getNonStaticMethodIterator(Predicate<Method> predicate) {
        return new ItemIterator<>(nonStaticMethods, i -> i.method, predicate);
    }

    /**
     * 获取公共构造器的迭代器。
     *
     * @param predicate 过滤条件
     * @return 公共构造器的迭代器
     */
    public Iterator<Constructor<?>> getPublicConstructorIterator(Predicate<Constructor<?>> predicate) {
        return new ItemIterator<>(publicConstructors, i -> i.constructor, predicate);
    }

    /**
     * 获取非公共构造器的迭代器。
     *
     * @param predicate 过滤条件
     * @return 非公共构造器的迭代器
     */
    public Iterator<Constructor<?>> getNonPublicConstructorIterator(Predicate<Constructor<?>> predicate) {
        return new ItemIterator<>(nonPublicConstructors, i -> i.constructor, predicate);
    }

    /**
     * 内部静态类，封装字段的元数据信息，包括反射对象和高性能访问句柄。
     * <p>
     * 提供对字段的快速访问支持，包括：
     * <ul>
     *   <li>{@link Field}：Java 反射字段对象，用于获取字段的基本信息（如名称、类型、修饰符等）。</li>
     *   <li>{@link VarHandle}：Java 9+ 引入的高性能变量句柄，支持原子操作（如 get、set、compareAndSet 等）。</li>
     *   <li>{@link MethodHandle}：用于字段读取（getter）和写入（setter）的高性能句柄，替代传统反射调用。</li>
     * </ul>
     * <p>
     * 该类通过预初始化字段的高性能访问句柄，显著提升字段访问性能，同时支持静态字段和实例字段的统一处理。
     */
    private static final class FieldInfo {
        /**
         * Java 反射字段对象，用于获取字段的基本信息（如名称、类型、修饰符等）。
         */
        final Field field;

        /**
         * 高性能变量句柄，用于字段的原子操作（如 get、set、compareAndSet 等）。
         * <p>
         * 通过 {@link MethodHandles.Lookup} 查找并创建字段的 {@link VarHandle}，若创建失败则记录警告日志。
         */
        final VarHandle varHandle;

        /**
         * 字段读取方法句柄（getter），用于高性能字段值获取。
         * <p>
         * 通过 {@link MethodHandles.Lookup} 查找并创建字段的 getter {@link MethodHandle}，若创建失败则记录警告日志。
         */
        final MethodHandle getterHandle;

        /**
         * 字段写入方法句柄（setter），用于高性能字段值设置。
         * <p>
         * 通过 {@link MethodHandles.Lookup} 查找并创建字段的 setter {@link MethodHandle}，若创建失败则记录警告日志。
         */
        final MethodHandle setterHandle;

        /**
         * 构造函数，根据字段是否为静态字段初始化对应的高性能访问句柄。
         * <p>
         * 该构造函数会尝试为字段创建 {@link VarHandle}、getter 和 setter 的 {@link MethodHandle}，
         * 若创建失败则记录日志并保持对应句柄为 {@code null}。
         *
         * @param field    字段对象，表示需要封装的反射字段
         * @param isStatic 是否为静态字段，决定使用 {@code findStaticVarHandle} 还是 {@code findVarHandle}
         * @param isPublic 是否为公共字段，决定使用默认查找器还是私有查找器
         */
        public FieldInfo(Field field, boolean isStatic, boolean isPublic) {
            VarHandle varHandle = null;
            MethodHandle getterHandle = null;
            MethodHandle setterHandle = null;

            MethodHandles.Lookup lookup;
            try {
                // 根据字段可见性选择合适的 Lookup 实例
                if (isPublic) {
                    // 公共字段使用默认查找器
                    lookup = MethodHandles.lookup();
                } else {
                    // 非公共字段使用私有查找器以绕过访问限制
                    lookup = MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup());
                }

                // 处理静态字段：创建静态字段的 VarHandle 和 MethodHandle
                try {
                    // 查找静态字段的 VarHandle，支持原子操作
                    if (isStatic) {
                        varHandle = lookup
                                .findStaticVarHandle(field.getDeclaringClass(), field.getName(), field.getType());
                    } else {
                        varHandle = lookup
                                .findVarHandle(field.getDeclaringClass(), field.getName(), field.getType());
                    }
                } catch (Throwable e) {
                    // 记录 VarHandle 创建失败的日志
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Warning: Cannot create %s VarHandle for '%s' in '%s' - %s: %s",
                                isStatic ? "static" : "instance", field, field.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                    }
                }

                try {
                    if (isStatic) {
                        // 查找静态字段的 getter 方法句柄
                        getterHandle = lookup
                                .findStaticGetter(field.getDeclaringClass(), field.getName(), field.getType());
                    } else {
                        getterHandle = lookup
                                .findGetter(field.getDeclaringClass(), field.getName(), field.getType());
                    }
                } catch (Throwable e) {
                    // 记录 getter MethodHandle 创建失败的日志
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Warning: Cannot create %s getter MethodHandle for '%s' in '%s' - %s: %s",
                                isStatic ? "static" : "instance", field, field.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                    }
                }

                try {
                    if (isStatic) {
                        // 查找静态字段的 setter 方法句柄
                        setterHandle = lookup
                                .findStaticSetter(field.getDeclaringClass(), field.getName(), field.getType());
                    } else {
                        setterHandle = lookup
                                .findSetter(field.getDeclaringClass(), field.getName(), field.getType());
                    }
                } catch (Throwable e) {
                    // 记录 setter MethodHandle 创建失败的日志
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Warning: Cannot create %s setter MethodHandle for '%s' in '%s' - %s: %s",
                                isStatic ? "static" : "instance", field, field.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                    }
                }
            } catch (Throwable e) {
                // 记录整体初始化失败的日志
                if (Logger.isEnabledWarn()) {
                    Logger.warn("Warning: Failed to initialize FieldInfo for '%s' in '%s' - %s: %s",
                            field, field.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                }
            }

            // 初始化字段信息
            this.field = field;
            this.varHandle = varHandle;
            this.getterHandle = getterHandle;
            this.setterHandle = setterHandle;
        }

        /**
         * 获取字段的值。
         * <p>
         * 该方法优先使用 {@link VarHandle} 或 {@link MethodHandle} 获取字段值，
         * 若两者均不可用，则回退到传统反射方式。
         *
         * @param object 字段所属的对象实例（对于静态字段可为 null）
         * @return 字段的值
         * @throws RuntimeException 若获取字段值过程中发生异常
         */
        public Object getValue(Object object) {
            final Object instance = object;
            int modifiers = field.getModifiers();
            // 静态字段不需要对象实例，显式置为 null 并添加注释说明
            if (Modifier.isStatic(modifiers)) {
                object = null;
            } else if (object == null) {
                throw new IllegalArgumentException("Object instance cannot be null for non-static fields.");
            }

            // 优先使用 VarHandle 获取字段值
            if (varHandle != null) {
                try {
                    if (object == null) {
                        return varHandle.get();
                    } else {
                        return varHandle.get(object);
                    }
                } catch (Throwable e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Failed to get field value using VarHandle for '%s' in '%s'",
                                field, field.getDeclaringClass(), e.getMessage(), e);
                    }
                }
            }

            // 其次使用 MethodHandle (getter) 获取字段值
            if (getterHandle != null) {
                try {
                    if (object == null) {
                        return getterHandle.invoke();
                    } else {
                        return getterHandle.invoke(object);
                    }
                } catch (Throwable e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Failed to get field value using MethodHandle for '%s' in '%s'",
                                field, field.getDeclaringClass(), e.getMessage(), e);
                    }
                }
            }

            ClassMetadata.setAccessible(field, instance);
            // 最后使用传统反射方式获取字段值
            try {
                return field.get(object);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to set field value using Reflection for '" + field + "' in '" + field.getDeclaringClass() + "'", e);
            }
        }

        /**
         * 设置字段的值。
         * <p>
         * 该方法优先使用 {@link VarHandle} 或 {@link MethodHandle} 设置字段值，
         * 若两者均不可用，则回退到传统反射方式。
         *
         * @param object 字段所属的对象实例（对于静态字段可为 null）
         * @param value  要设置的字段值
         * @throws RuntimeException 若设置字段值过程中发生异常
         */
        public void setValue(Object object, Object value) {
            final Object instance = object;
            int modifiers = field.getModifiers();
            // 静态字段不需要对象实例，显式置为 null 并添加注释说明
            if (Modifier.isStatic(modifiers)) {
                object = null;
            } else if (object == null) {
                throw new IllegalArgumentException("Object instance cannot be null for non-static fields.");
            }

            // 优先使用 VarHandle 获取字段值
            if (varHandle != null) {
                try {
                    if (object == null) {
                        varHandle.set(value);
                    } else {
                        varHandle.set(object, value); // 执行赋值操作
                    }
                } catch (Throwable e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Failed to set field value using VarHandle for '%s' in '%s'",
                                field, field.getDeclaringClass(), e.getMessage(), e);
                    }
                }
            }

            // 其次使用 MethodHandle (getter) 获取字段值
            if (setterHandle != null) {
                try {
                    if (object == null) {
                        setterHandle.invoke(value);
                    } else {
                        setterHandle.invoke(object, value); // 执行赋值操作
                    }
                } catch (Throwable e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Failed to set field value using MethodHandle for '%s' in '%s'",
                                field, field.getDeclaringClass(), e.getMessage(), e);
                    }
                }
            }

            ClassMetadata.setAccessible(field, instance);
            // 最后使用传统反射方式获取字段值
            try {
                field.set(object, value);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to set field value using Reflection for '" + field + "' in '" + field.getDeclaringClass() + "'", e);
            }
        }
    }

    /**
     * 内部静态类，封装方法的元数据信息，包括反射对象和高性能调用句柄。
     * <p>
     * 提供对方法的快速调用支持，包括：
     * <ul>
     *   <li>{@link Method}：Java 反射方法对象，用于获取方法的基本信息（如名称、参数类型、返回类型等）。</li>
     *   <li>{@link MethodHandle}：用于方法调用的高性能句柄，替代传统反射调用以提升性能。</li>
     * </ul>
     * <p>
     * 该类通过 {@link MethodHandle} 替代传统的反射调用，显著提升方法调用性能，
     * 同时支持静态方法和实例方法的统一处理。
     */
    private static final class MethodInfo {
        /**
         * Java 反射方法对象，用于获取方法的基本信息（如名称、参数类型、返回类型等）。
         */
        final Method method;

        /**
         * 高性能方法调用句柄，用于替代反射调用以提升性能。
         * <p>
         * 通过 {@link MethodHandles.Lookup} 查找并创建方法句柄，支持静态方法和实例方法。
         * 若创建失败，则记录警告日志并保持句柄为 {@code null}。
         */
        final MethodHandle methodHandle;

        /**
         * 构造函数，根据方法是否为静态方法初始化对应的方法句柄。
         * <p>
         * 该构造函数会尝试为方法创建 {@link MethodHandle}，若创建失败则记录日志。
         *
         * @param method   方法对象，表示需要封装的反射方法
         * @param isStatic 是否为静态方法，决定使用 {@code findStatic} 还是 {@code findVirtual}
         * @param isPublic 是否为公共方法，决定使用默认查找器还是私有查找器
         */
        private MethodInfo(Method method, boolean isStatic, boolean isPublic) {
            MethodHandle handle = null;

            // 构建方法签名类型，包含返回类型和参数类型
            MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());

            MethodHandles.Lookup lookup;
            try {
                // 根据方法可见性选择合适的 Lookup 实例
                if (isPublic) {
                    // 公共方法使用默认查找器
                    lookup = MethodHandles.lookup();
                } else {
                    // 非公共方法使用私有查找器以绕过访问限制
                    lookup = MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup());
                }

                // 根据方法是否为静态方法选择对应的查找方式
                if (isStatic) {
                    // 处理静态方法：查找静态方法的 MethodHandle
                    handle = lookup.findStatic(method.getDeclaringClass(), method.getName(), methodType);
                } else {
                    // 处理实例方法：查找实例方法的 MethodHandle
                    handle = lookup.findVirtual(method.getDeclaringClass(), method.getName(), methodType);
                }
            } catch (Throwable e) {
                // 统一记录 MethodHandle 创建失败的日志
                if (Logger.isEnabledWarn()) {
                    Logger.warn("Warning: Cannot create MethodHandle for '%s' in '%s' - %s: %s",
                            method, method.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                }
            }

            // 初始化方法信息
            this.method = method;
            this.methodHandle = handle;
        }

        /**
         * 调用方法并返回结果。
         * <p>
         * 该方法优先使用 {@link MethodHandle} 调用方法，
         * 若不可用，则回退到传统反射方式。
         *
         * @param object 方法所属的对象实例（对于静态方法可为 null）
         * @param args   方法参数
         * @return 方法调用结果
         * @throws RuntimeException 若方法调用过程中发生异常
         */
        public Object invoke(Object object, Object... args) {
            final Object instance = object;
            int modifiers = method.getModifiers();
            // 静态字段不需要对象实例，显式置为 null 并添加注释说明
            if (Modifier.isStatic(modifiers)) {
                object = null;
            } else if (object == null) {
                throw new IllegalArgumentException("Object instance cannot be null for non-static methods.");
            }

            // 优先使用 MethodHandle 调用方法
            if (methodHandle != null) {
                try {
                    if (object == null) {
                        return methodHandle.invokeWithArguments(args);
                    } else {
                        Object[] newArgs = new Object[args.length + 1];
                        newArgs[0] = object;
                        System.arraycopy(args, 0, newArgs, 1, args.length);
                        return methodHandle.invokeWithArguments(newArgs);
                    }
                } catch (Throwable e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Failed to invoke method '%s' using MethodHandle in '%s'. Reason: %s",
                                ClassMetadata.getMethodUniqueKey(method), method.getDeclaringClass(), e.getMessage(), e);
                    }
                }
            }

            ClassMetadata.setAccessible(method, instance);
            // 回退到传统反射方式调用方法
            try {
                return method.invoke(object, args);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to invoke method '" + ClassMetadata.getMethodUniqueKey(method) + "' using Reflection in '" + method.getDeclaringClass() + "'. Reason: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 内部静态类，封装构造函数的元数据信息，包括反射对象和高性能调用句柄。
     * <p>
     * 提供对构造函数的快速调用支持，包括：
     * <ul>
     *   <li>{@link Constructor}：Java 反射构造函数对象，用于获取构造函数的基本信息（如参数类型等）。</li>
     *   <li>{@link MethodHandle}：用于构造函数调用的高性能句柄，替代传统反射调用以提升性能。</li>
     * </ul>
     * <p>
     * 该类通过 {@link MethodHandle} 替代传统的反射调用，显著提升构造函数调用性能，
     * 同时支持公共构造函数和非公共构造函数的统一处理。
     */
    private static final class ConstructorInfo {
        /**
         * Java 反射构造函数对象，用于获取构造函数的基本信息（如参数类型等）。
         */
        final Constructor<?> constructor;

        /**
         * 高性能构造函数调用句柄，用于替代反射调用以提升性能。
         * <p>
         * 通过 {@link MethodHandles.Lookup} 查找并创建构造函数的 {@link MethodHandle}，
         * 若创建失败则记录警告日志并保持句柄为 {@code null}。
         */
        final MethodHandle constructorHandle;

        /**
         * 构造函数，初始化构造函数的 MethodHandle。
         * <p>
         * 该构造函数会尝试为构造函数创建 {@link MethodHandle}，若创建失败则记录日志。
         *
         * @param constructor 构造函数对象，表示需要封装的反射构造函数
         * @param isPublic    是否为公共构造函数，决定使用默认查找器还是私有查找器
         */
        private ConstructorInfo(Constructor<?> constructor, boolean isPublic) {
            MethodHandle handle = null;
            try {
                // 构建构造函数签名类型
                MethodType constructorType = MethodType.methodType(void.class, constructor.getParameterTypes());
                if (isPublic) {
                    // 使用 MethodHandles 查找构造函数
                    handle = MethodHandles.lookup()
                            .findConstructor(constructor.getDeclaringClass(), constructorType);
                } else {
                    handle = MethodHandles.privateLookupIn(constructor.getDeclaringClass(), MethodHandles.lookup())
                            .findConstructor(constructor.getDeclaringClass(), constructorType);
                }
            } catch (Throwable e) {
                // 记录构造函数 MethodHandle 创建失败的日志
                if (Logger.isEnabledWarn()) {
                    Logger.warn("Warning: Cannot create MethodHandle for constructor in '%s' - %s: %s",
                            constructor.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                }
            }
            this.constructor = constructor;
            this.constructorHandle = handle;
        }

        /**
         * 使用 MethodHandle 调用构造函数创建实例。
         * <p>
         * 若 {@link MethodHandle} 不可用，则回退到传统反射方式调用构造函数。
         *
         * @param args 构造函数参数
         * @return 创建的实例
         * @throws Throwable 调用过程中可能抛出的异常
         */
        public Object newInstanceWithHandle(Object... args) throws Throwable {
            // 优先使用 MethodHandle 调用构造函数
            if (constructorHandle != null) {
                try {
                    return constructorHandle.invokeWithArguments(args);
                } catch (Throwable e) {
                    // 如果 MethodHandle 调用失败，记录日志并回退到传统反射方式
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Warning: Failed to invoke constructor using MethodHandle in class '%s' - %s: %s. Falling back to traditional reflection.",
                                constructor.getDeclaringClass(), e.getClass(), e.getMessage(), e);
                    }
                }
            }

            ClassMetadata.setAccessible(constructor, null);
            // 回退到传统反射方式调用构造函数
            try {
                return constructor.newInstance(args);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to create instance using constructor '" + constructor + "' in class '" + constructor.getDeclaringClass() + "'. Reason: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 内部迭代器类，用于遍历映射中的元素。
     * <p>
     * 支持过滤条件，仅返回满足条件的元素。
     *
     * @param <T> 元素类型
     * @param <C> 映射值类型
     */
    private static final class ItemIterator<T, C> implements Iterator<T> {
        /**
         * 映射条目的迭代器。
         */
        final Iterator<Map.Entry<String, C>> iterator;

        /**
         * 转换函数，将映射值转换为目标类型。
         */
        final Function<C, T> converter;

        /**
         * 过滤条件，用于筛选满足条件的元素。
         */
        final Predicate<T> filter;

        /**
         * 下一个满足条件的元素。
         */
        T nextField;

        /**
         * 构造函数，初始化迭代器。
         *
         * @param map       映射对象
         * @param converter 转换函数
         * @param filter    过滤条件
         */
        private ItemIterator(Map<String, C> map, Function<C, T> converter, Predicate<T> filter) {
            this.iterator = map.entrySet().iterator();
            this.converter = converter;
            this.filter = filter = filter == null ? field -> true : filter;
            this.nextField = findNext(filter);
        }

        @Override
        public boolean hasNext() {
            return nextField != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T current = nextField;
            nextField = findNext(filter);
            return current;
        }

        /**
         * 查找下一个满足条件的元素。
         *
         * @param filter 过滤条件
         * @return 满足条件的元素，若无则返回 null
         */
        private T findNext(Predicate<T> filter) {
            while (iterator.hasNext()) {
                Map.Entry<String, C> entry = iterator.next();
                T field = converter.apply(entry.getValue());
                if (filter.test(field)) {
                    return field;
                }
            }
            return null;
        }
    }


    /**
     * 设置字段的可访问性。
     * <p>
     * 若字段不可访问，则尝试将其设置为可访问状态。
     *
     * @param member   字段、方法或构造器对象，需实现 AccessibleObject 和 Member 接口
     * @param instance 字段所属的对象实例
     * @param <T>      泛型类型，必须同时实现 AccessibleObject 和 Member 接口
     * @throws RuntimeException 若设置可访问性失败，例如由于 JVM 模块系统限制
     */
    private static <T extends AccessibleObject & Member> void setAccessible(T member, Object instance) {
        try {
            // 显式检查并处理权限问题
            if (!member.canAccess(instance)) {
                member.setAccessible(true);
            }
        } catch (InaccessibleObjectException e) {
            throw new RuntimeException(
                    "Failed to access '" + member + "' in '" + member.getDeclaringClass() +
                            "' due to access restrictions. Consider using '--add-opens' JVM argument.", e);
        }
    }

}
