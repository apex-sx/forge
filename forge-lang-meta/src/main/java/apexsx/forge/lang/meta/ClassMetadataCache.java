package apexsx.forge.lang.meta;

import apexsx.forge.core.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类元数据缓存工具类
 * <p>
 * 该类用于缓存Java类的反射元数据，包括方法、字段和构造函数等信息，
 * 以提高重复获取相同类元数据时的性能。
 * <p>
 * 使用ConcurrentHashMap实现线程安全的缓存机制，支持懒加载和预加载模式。
 */
public class ClassMetadataCache implements Iterable<ClassMetadataCache> {
    /**
     * 全局缓存，存储每个类与其对应的ClassMetadataCache实例的映射关系
     * <p>
     * 使用ConcurrentHashMap保证线程安全，避免多线程环境下出现竞态条件
     */
    private static final ConcurrentHashMap<Class<?>, ClassMetadataCache> CLASS_METADATA_CACHE = new ConcurrentHashMap<>();
    /**
     * 方法缓存，存储方法名+参数类型签名与Method对象的映射关系
     * <p>
     * 通过方法名和参数类型唯一确定一个方法，提升方法查找效率
     */
    private final ConcurrentHashMap<String, Method> methodCache = new ConcurrentHashMap<>();
    /**
     * 字段缓存，存储字段名与Field对象的映射关系
     * <p>
     * 通过字段名唯一确定一个字段，提升字段查找效率
     */
    private final ConcurrentHashMap<String, Field> fieldCache = new ConcurrentHashMap<>();
    /**
     * 构造函数缓存，存储参数类型签名与Constructor对象的映射关系
     * <p>
     * 通过参数类型唯一确定一个构造函数，提升构造函数查找效率
     */
    private final ConcurrentHashMap<String, Constructor<?>> constructorCache = new ConcurrentHashMap<>();
    /**
     * 当前缓存关联的类对象
     * <p>
     * 用于获取该类的元数据信息，如方法、字段和构造函数等
     */
    private final Class<?> metadataClass;
    /**
     * 父类元数据缓存实例
     * <p>
     * 用于支持继承链上的元数据查找，采用懒加载模式，只有在需要时才初始化
     */
    private volatile ClassMetadataCache superClassCache;
    /**
     * 标识当前类的元数据是否已经预加载完成
     * <p>
     * 使用volatile关键字确保多线程环境下的可见性，避免重复预加载
     */
    private volatile boolean isPreloadMetadata;

    /**
     * 私有构造函数，防止外部直接实例化
     *
     * @param metadataClass 需要缓存元数据的类，不能为null
     * @throws NullPointerException 当clazz为null时抛出
     */
    protected ClassMetadataCache(Class<?> metadataClass) {
        this.metadataClass = Objects.requireNonNull(metadataClass, "The meta must not be null");
    }

    /**
     * 获取类的缓存实例
     *
     * @param clazz 需要获取缓存的类
     * @param lazy  是否使用懒加载模式，true表示延迟加载元数据，false表示立即加载
     * @return 类对应的ClassMetadataCache实例
     */
    public static ClassMetadataCache of(Class<?> clazz, boolean lazy) {
        return CLASS_METADATA_CACHE.computeIfAbsent(clazz, clz -> {
            ClassMetadataCache cache = new ClassMetadataCache(clz);
            if (!lazy) {
                cache.preloadMetadata(false);
            }
            return cache;
        });
    }

    /**
     * 清除所有缓存
     */
    public static void clear() {
        ClassMetadataCache.CLASS_METADATA_CACHE.clear();
    }

    /**
     * 清除特定类的缓存
     *
     * @param clazz 需要清除缓存的类
     */
    public static void remove(Class<?> clazz) {
        ClassMetadataCache.CLASS_METADATA_CACHE.remove(clazz);
    }

    /**
     * 检查当前类的元数据是否已经预加载完成
     *
     * @return 如果元数据已预加载完成返回true，否则返回false
     */
    public boolean isPreloadMetadata() {
        return isPreloadMetadata;
    }

    /**
     * 预加载类的元数据到缓存中
     *
     * @param loadSuperClassMetadata 是否同时加载父类的元数据
     * @return 当前ClassMetadataCache实例，支持链式调用
     */
    public ClassMetadataCache preloadMetadata(boolean loadSuperClassMetadata) {
        if (!isPreloadMetadata) {
            synchronized (this) {
                if (!isPreloadMetadata) {
                    // 预加载类的所有构造函数到缓存中
                    for (Constructor<?> constructor : metadataClass.getDeclaredConstructors()) {
                        constructorCache.putIfAbsent(UniversalClass.getConstructorKey(constructor.getParameterTypes()), constructor);
                    }
                    // 预加载类的所有方法到缓存中
                    for (Method method : metadataClass.getDeclaredMethods()) {
                        methodCache.putIfAbsent(UniversalClass.getMethodKey(method.getName(), method.getParameterTypes()), method);
                    }
                    // 预加载类的所有字段到缓存中
                    for (Field field : metadataClass.getDeclaredFields()) {
                        fieldCache.putIfAbsent(field.getName(), field);
                    }
                    isPreloadMetadata = true;
                }
            }
        }
        // 根据参数决定是否加载父类元数据
        if (loadSuperClassMetadata) {
            ClassMetadataCache superClassMetadataCache = getSuperClassCache();
            if (superClassMetadataCache != null) {
                superClassMetadataCache.preloadMetadata(true);
            }
        }
        return this;
    }

    /**
     * 获取当前缓存实例关联的类对象
     *
     * @return 当前缓存关联的Class对象
     */
    public final Class<?> getMetadataClass() {
        return metadataClass;
    }

    /**
     * 获取指定参数类型的构造函数
     *
     * @param parameterTypes 构造函数的参数类型列表
     * @return 对应的构造函数对象
     * @throws RuntimeException 当找不到对应构造函数时抛出
     */
    public Constructor<?> getDeclaredConstructor(Class<?>... parameterTypes) {
        return constructorCache.computeIfAbsent(UniversalClass.getConstructorKey(parameterTypes), k -> {
            try {
                return metadataClass.getDeclaredConstructor(parameterTypes);
            } catch (NoSuchMethodException e) {
                return Logger.warnOrThrowError(false, e, "Error to getting constructor from '" + metadataClass + "'");
            }
        });
    }

    /**
     * 获取当前类所有声明的构造函数
     *
     * @return 包含当前类所有声明构造函数的数组
     */
    public Constructor<?>[] getDeclaredConstructors() {
        return preloadMetadata(false).constructorCache.values().toArray(new Constructor[0]);
    }

    /**
     * 获取指定名称和参数类型的方法（仅在当前类中查找）
     *
     * @param name           方法名称
     * @param parameterTypes 方法参数类型列表
     * @return 对应的方法对象，如果未找到则返回null
     */
    public Method getDeclaredMethod(String name, Class<?>... parameterTypes) {
        return methodCache.computeIfAbsent(UniversalClass.getMethodKey(name, parameterTypes), k -> {
            try {
                return metadataClass.getDeclaredMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {
                return Logger.warnOrThrowError(false, e, "Error to getting method '" + name + "' from '" + metadataClass + "'");
            }
        });
    }

    /**
     * 获取当前类所有声明的方法
     *
     * @return 包含当前类所有声明方法的数组
     */
    public Method[] getDeclaredMethods() {
        return preloadMetadata(false).methodCache.values().toArray(new Method[0]);
    }

    /**
     * 获取指定名称的字段（仅在当前类中查找）
     *
     * @param name 字段名称
     * @return 对应的字段对象，如果未找到则返回null
     */
    public Field getDeclaredField(String name) {
        return fieldCache.computeIfAbsent(name, k -> {
            try {
                return metadataClass.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                return Logger.warnOrThrowError(false, e, "Error to getting field '" + name + "' from '" + metadataClass + "'");
            }
        });
    }

    /**
     * 获取当前类所有声明的字段
     *
     * @return 包含当前类所有声明字段的数组
     */
    public Field[] getDeclaredFields() {
        return preloadMetadata(false).fieldCache.values().toArray(new Field[0]);
    }

    /**
     * 获取指定名称和参数类型的方法（会在父类中递归查找）
     *
     * @param name           方法名称
     * @param parameterTypes 方法参数类型列表
     * @return 对应的方法对象，如果未找到则返回null
     */
    public Method getInheritedMethod(String name, Class<?>... parameterTypes) {
        // 先尝试从当前类获取方法
        Method method = getDeclaredMethod(name, parameterTypes);
        if (method != null) {
            return method;
        }
        // 如果当前类没有该方法，则尝试从父类获取
        ClassMetadataCache superCache = getSuperClassCache();
        return superCache != null ? superCache.getInheritedMethod(name, parameterTypes) : null;
    }

    /**
     * 获取当前类及其父类中所有声明的方法
     *
     * @return 包含当前类及其父类所有声明方法的数组
     */
    public Method[] getInheritedMethods() {
        List<Method> results = new ArrayList<>();
        for (ClassMetadataCache classMetadataCache : this) {
            Collections.addAll(results, classMetadataCache.getDeclaredMethods());
        }
        return results.toArray(new Method[0]);
    }

    /**
     * 获取指定名称的字段（会在父类中递归查找）
     *
     * @param name 字段名称
     * @return 对应的字段对象，如果未找到则返回null
     */
    public Field getInheritedField(String name) {
        // 先尝试从当前类获取字段
        Field field = getDeclaredField(name);
        if (field != null) {
            return field;
        }
        // 如果当前类没有该字段，则尝试从父类获取
        ClassMetadataCache superCache = getSuperClassCache();
        return superCache != null ? superCache.getInheritedField(name) : null;
    }

    /**
     * 获取当前类及其父类中所有声明的字段
     *
     * @return 包含当前类及其父类所有声明字段的数组
     */
    public Field[] getInheritedFields() {
        List<Field> results = new ArrayList<>();
        for (ClassMetadataCache classMetadataCache : this) {
            Collections.addAll(results, classMetadataCache.getDeclaredFields());
        }
        return results.toArray(new Field[0]);
    }

    /**
     * 获取指定参数类型的公共构造函数
     *
     * @param parameterTypes 构造函数的参数类型列表
     * @return 对应的公共构造函数对象，如果不存在或不是公共的则返回null
     */
    public Constructor<?> getPublicDeclaredConstructor(Class<?>... parameterTypes) {
        Constructor<?> member = getDeclaredConstructor(parameterTypes);
        if (ModifierChecker.isPublic(member)) {
            return member;
        }
        return null;
    }

    /**
     * 获取当前类所有声明的公共构造函数
     *
     * @return 包含当前类所有声明公共构造函数的数组
     */
    public Constructor<?>[] getPublicDeclaredConstructors() {
        return Arrays.stream(getDeclaredConstructors()).filter(ModifierChecker::isPublic).toArray(Constructor[]::new);
    }

    /**
     * 获取指定名称和参数类型的公共方法（仅在当前类中查找）
     *
     * @param name           方法名称
     * @param parameterTypes 方法参数类型列表
     * @return 对应的公共方法对象，如果不存在或不是公共的则返回null
     */
    public Method getPublicDeclaredMethod(String name, Class<?>... parameterTypes) {
        Method member = getDeclaredMethod(name, parameterTypes);
        if (ModifierChecker.isPublic(member)) {
            return member;
        }
        return null;
    }

    /**
     * 获取当前类所有声明的公共方法
     *
     * @return 包含当前类所有声明公共方法的数组
     */
    public Method[] getPublicDeclaredMethods() {
        return Arrays.stream(getDeclaredMethods()).filter(ModifierChecker::isPublic).toArray(Method[]::new);
    }

    /**
     * 获取指定名称的公共字段（仅在当前类中查找）
     *
     * @param name 字段名称
     * @return 对应的公共字段对象，如果不存在或不是公共的则返回null
     */
    public Field getPublicDeclaredField(String name) {
        Field member = getDeclaredField(name);
        if (ModifierChecker.isPublic(member)) {
            return member;
        }
        return null;
    }

    /**
     * 获取当前类所有声明的公共字段
     *
     * @return 包含当前类所有声明公共字段的数组
     */
    public Field[] getPublicDeclaredFields() {
        return Arrays.stream(getDeclaredFields()).filter(ModifierChecker::isPublic).toArray(Field[]::new);
    }

    /**
     * 获取指定名称和参数类型的公共方法（会在父类中递归查找）
     *
     * @param name           方法名称
     * @param parameterTypes 方法参数类型列表
     * @return 对应的公共方法对象，如果不存在或不是公共的则返回null
     */
    public Method getPublicInheritedMethod(String name, Class<?>... parameterTypes) {
        Method member = getInheritedMethod(name, parameterTypes);
        if (ModifierChecker.isPublic(member)) {
            return member;
        }
        return null;
    }

    /**
     * 获取当前类及其父类中所有声明的公共方法
     *
     * @return 包含当前类及其父类所有声明公共方法的数组
     */
    public Method[] getPublicInheritedMethods() {
        return Arrays.stream(getInheritedMethods()).filter(ModifierChecker::isPublic).toArray(Method[]::new);
    }

    /**
     * 获取指定名称的公共字段（会在父类中递归查找）
     *
     * @param name 字段名称
     * @return 对应的公共字段对象，如果不存在或不是公共的则返回null
     */
    public Field getPublicInheritedField(String name) {
        Field member = getInheritedField(name);
        if (ModifierChecker.isPublic(member)) {
            return member;
        }
        return null;
    }

    /**
     * 获取当前类及其父类中所有声明的公共字段
     *
     * @return 包含当前类及其父类所有声明公共字段的数组
     */
    public Field[] getPublicInheritedFields() {
        return Arrays.stream(getInheritedFields()).filter(ModifierChecker::isPublic).toArray(Field[]::new);
    }

    /**
     * 根据可见性范围查找方法数组
     *
     * @param classMetadataVisibilityScope 方法的可见性范围枚举值，不能为null
     * @return 符合可见性范围的方法数组
     * @throws IllegalArgumentException 当classMetadataVisibilityScope为null或不支持的枚举值时抛出
     */
    public Method[] findMethods(ClassMetadataVisibilityScope classMetadataVisibilityScope) {
        if (classMetadataVisibilityScope != null) {
            switch (classMetadataVisibilityScope) {
                case DECLARED:
                    return getDeclaredMethods();
                case INHERITED:
                    return getInheritedMethods();
                case PUBLIC_DECLARED:
                    return getPublicDeclaredMethods();
                case PUBLIC_INHERITED:
                    return getPublicInheritedMethods();
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Unsupported ClassMetadataVisibility: " + classMetadataVisibilityScope);
    }

    /**
     * 根据可见性范围查找字段数组
     *
     * @param classMetadataVisibilityScope 字段的可见性范围枚举值，不能为null
     * @return 符合可见性范围的字段数组
     * @throws IllegalArgumentException 当classMetadataVisibilityScope为null或不支持的枚举值时抛出
     */
    public Field[] findFields(ClassMetadataVisibilityScope classMetadataVisibilityScope) {
        if (classMetadataVisibilityScope != null) {
            switch (classMetadataVisibilityScope) {
                case DECLARED:
                    return getDeclaredFields();
                case INHERITED:
                    return getInheritedFields();
                case PUBLIC_DECLARED:
                    return getPublicDeclaredFields();
                case PUBLIC_INHERITED:
                    return getPublicInheritedFields();
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Unsupported ClassMetadataVisibility: " + classMetadataVisibilityScope);
    }

    /**
     * 获取父类的元数据缓存实例
     *
     * @return 父类的ClassMetadataCache实例，如果没有父类则返回null
     */
    public final ClassMetadataCache getSuperClassCache() {
        if (superClassCache == null) {
            Class<?> superclass = metadataClass.getSuperclass();
            if (superclass != null) {
                synchronized (this) {
                    if (superClassCache == null) {
                        superClassCache = ClassMetadataCache.of(superclass, true);
                    }
                }
            }
        }
        return superClassCache;
    }

    /**
     * 创建字段名称格式化器函数
     *
     * @param classMetadataVisibilityScope 类元数据可见性，用于确定要查找的字段范围
     * @param fieldFilter                  字段过滤器，用于筛选符合条件的字段，可为null表示不过滤
     * @param fieldSorter                  字段排序器，用于对字段进行排序，可为null表示不排序
     * @return 返回一个函数，该函数接收ClassMetadataNameFormat参数，返回格式化后的字段名称数组
     */
    public Function<ClassMetadataNameFormat, String[]> createFieldNameFormatter(ClassMetadataVisibilityScope classMetadataVisibilityScope, Predicate<Field> fieldFilter, Comparator<Field> fieldSorter) {
        // 查找指定可见性级别的所有字段
        Stream<Field> fieldStream = Arrays.stream(findFields(classMetadataVisibilityScope));
        // 根据是否提供过滤器来决定字段流的构建方式
        if (fieldFilter != null) {
            fieldStream = fieldStream.filter(fieldFilter);
        }
        if (fieldSorter != null) {
            fieldStream = fieldStream.sorted(fieldSorter);
        }
        List<Field> list = fieldStream.collect(Collectors.toList());
        // 返回一个函数，该函数使用给定的名称格式化器处理字段流
        return (classMetadataNameFormat) -> {
            StringBuilder result = new StringBuilder(120);
            return list.stream().map(field -> classMetadataNameFormat.formatName(field, result)).toArray(String[]::new);
        };
    }

    /**
     * 返回一个遍历器，用于遍历当前类及其父类的元数据缓存
     *
     * @return ClassMetadataCache迭代器
     */
    @Override
    public Iterator<ClassMetadataCache> iterator() {
        return new IteratorImpl(this);
    }

    /**
     * 内部迭代器实现类，用于遍历类继承链上的ClassMetadataCache实例
     * <p>
     * 该迭代器从当前类开始，沿着继承链向上遍历，直到Object类为止。
     * 每次调用next()方法会返回当前节点的ClassMetadataCache实例，
     * 并将游标移动到父类的ClassMetadataCache实例。
     */
    protected static class IteratorImpl implements Iterator<ClassMetadataCache> {
        /**
         * 当前遍历到的ClassMetadataCache实例
         * <p>
         * 初始值为迭代器创建时传入的实例，每次调用next()方法后更新为父类的实例
         */
        protected ClassMetadataCache current;

        /**
         * 构造函数，初始化迭代器
         *
         * @param metadataCache 起始的ClassMetadataCache实例，不能为null
         */
        protected IteratorImpl(ClassMetadataCache metadataCache) {
            current = metadataCache;
        }

        /**
         * 检查是否还有下一个元素可以遍历
         *
         * @return 如果还有元素返回true，否则返回false
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * 获取下一个ClassMetadataCache实例
         *
         * @return 下一个ClassMetadataCache实例
         * @throws NoSuchElementException 当没有更多元素可遍历时抛出
         */
        @Override
        public ClassMetadataCache next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ClassMetadataCache result = current;
            current = current.getSuperClassCache();
            return result;
        }
    }
}
