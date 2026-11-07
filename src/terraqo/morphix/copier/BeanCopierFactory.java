package terraqo.morphix.copier;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BeanCopierFactory {
    // 创建一个 void 返回值的空 MethodHandle

    /**
     * 为属性创建setter的MethodHandle
     * 自动处理void和builder两种返回类型
     */
    public static MethodHandle createPropertyGetterHandle(MethodHandles.Lookup lookup, Class<?> targetClass, String propertyName, Class<?> propertyType, String methodName) throws ReflectiveOperationException {
        return BeanCopierFactory.createPropertyHandle(lookup, targetClass, propertyName, propertyType, methodName, false);
    }

    /**
     * 为属性创建setter的MethodHandle
     * 自动处理void和builder两种返回类型
     */
    public static MethodHandle createPropertySetterHandle(MethodHandles.Lookup lookup, Class<?> targetClass, String propertyName, Class<?> propertyType, String methodName) throws ReflectiveOperationException {
        return BeanCopierFactory.createPropertyHandle(lookup, targetClass, propertyName, propertyType, methodName, true);
    }

    /**
     * 为属性创建setter的MethodHandle
     * 自动处理void和builder两种返回类型
     */
    private static MethodHandle createPropertyHandle(MethodHandles.Lookup lookup, Class<?> targetClass, String propertyName, Class<?> propertyType, String methodName, boolean isSetter) throws ReflectiveOperationException {
        // 尝试通过setter方法名构建MethodHandle
        MethodHandle methodHandle = null;

        if (methodName != null) {
            // 先尝试查找返回void的setter
            MethodType methodType = isSetter ? MethodType.methodType(void.class, propertyType) : MethodType.methodType(propertyType);
            try {
                methodHandle = lookup.findVirtual(targetClass, methodName, methodType);
            } catch (Throwable ignored) {
            }
            // 尝试查找返回自身类型的setter（builder模式）
            if (isSetter && methodHandle == null) {
                try {
                    methodHandle = lookup.findVirtual(targetClass, methodName, MethodType.methodType(targetClass, propertyType));
                } catch (Throwable ignored) {
                }
            }
            if (methodHandle == null) {
                // 尝试查找返回父类的情况
                for (Class<?> clazz = targetClass.getSuperclass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
                    // 先尝试查找返回void的setter
                    try {
                        methodHandle = lookup.findVirtual(targetClass, methodName, methodType);
                    } catch (Throwable ignored) {
                    }
                    // 尝试查找返回自身类型的setter（builder模式）
                    if (isSetter && methodHandle == null) {
                        try {
                            methodHandle = lookup.findVirtual(targetClass, methodName, MethodType.methodType(clazz, propertyType));
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }

            if (methodHandle != null) {
                if (isSetter) {
                    methodHandle = MethodHandles.dropReturn(methodHandle);
                }
                return methodHandle;
            }
        }
        // 回退到直接字段访问
        Field field = null;
        for (Class<?> clazz = targetClass; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                VarHandle varHandle = lookup.findVarHandle(clazz, propertyName, propertyType);
                methodHandle = varHandle.toMethodHandle(isSetter ? VarHandle.AccessMode.SET : VarHandle.AccessMode.GET);
            } catch (Throwable ignored) {
            }
            if (methodHandle != null) {
                break;
            }
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException ignored) {
            }
            if (field != null) {
                break;
            }
        }
        if (methodHandle != null) {
            return methodHandle;
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                if (isSetter) {
                    return lookup.unreflectSetter(field);
                } else {
                    return lookup.unreflectGetter(field);
                }
            } catch (IllegalAccessException e) {
                throw new ReflectiveOperationException("Cannot find field or '" + methodName + "' method for property: " + propertyName);
            }
        }
        throw new ReflectiveOperationException("Cannot find field or '" + methodName + "' method for property: " + propertyName);
    }


//    private final DefaultSupplier defaultSupplier = DefaultSupplier.UNIVERSAL_INSTANCE;
//    private final ConcurrentHashMap<BeanCopierKey, BeanCopier<?, ?>> CACHE = new ConcurrentHashMap<>();
//
//    private final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
//
//    public void clearCache() {
//        CACHE.clear();
//    }
//
//    public <S, T> BeanCopier<S, T> getCopier(Class<S> sourceClass, Class<T> targetClass) {
//        return getCopier(sourceClass, targetClass, null);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <S, T> BeanCopier<S, T> getCopier(Class<S> sourceClass, Class<T> targetClass, BeanCopierSetting config) {
//        BeanCopierKey key = new BeanCopierKey(sourceClass, targetClass, config);
//        return (BeanCopier<S, T>) CACHE.computeIfAbsent(key, k -> createCopier(sourceClass, targetClass, config));
//    }
//
//    private <S, T> BeanCopier<S, T> createCopier(Class<S> sourceClass, Class<T> targetClass, BeanCopierSetting config) {
//        List<PropertyMapping> mappings = findPropertyMappings(sourceClass, targetClass);
//        try {
//            MethodHandle copyMethodHandle = createCopyMethodHandle(mappings, config);
//            return BeanCopier.create(sourceClass, targetClass, LOOKUP, copyMethodHandle);
//        } catch (Throwable e) {
//            throw new RuntimeException("Failed to create bean copier for " +
//                    sourceClass.getName() + " -> " + targetClass.getName(), e);
//        }
//    }
//
//    private MethodHandle createCopyMethodHandle(List<PropertyMapping> mappings, BeanCopierSetting config) throws Throwable {
//        List<MethodHandle> handles = new ArrayList<>();
//
//        for (PropertyMapping mapping : mappings) {
//            MethodHandle getter = mapping.getter();
//            MethodHandle setter = mapping.setter();
//            Class<?> targetType = mapping.targetType();
//
//            MethodHandle handle;
//            if (config != null && config.hasConverter(mapping.propertyName())) {
//                handle = createConvertedCopyHandle(getter, setter, config, mapping.propertyName());
//            } else {
//                handle = createDirectCopyHandle(getter, setter, targetType);
//            }
//            handles.add(handle);
//        }
//
//        if (handles.isEmpty()) {
//            return LOOKUP.findStatic(BeanCopierFactory.class, "noOp",
//                    MethodType.methodType(void.class, Object.class, Object.class));
//        }
//
//        MethodHandle combined = handles.getFirst();
//        for (int i = 1; i < handles.size(); i++) {
//            combined = MethodHandles.collectArguments(combined, 0, handles.get(i));
//        }
//
//        return combined;
//    }
//
//    private MethodHandle createDirectCopyHandle(MethodHandle getter, MethodHandle setter, Class<?> targetType) throws Throwable {
//        if (!targetType.isPrimitive()) {
//            MethodHandle nullCheck = LOOKUP.findStatic(BeanCopierFactory.class, "handleNullForReference",
//                    MethodType.methodType(Object.class, Object.class, Object.class));
//
//            MethodHandle typedNullCheck = MethodHandles.explicitCastArguments(nullCheck,
//                    MethodType.methodType(targetType, targetType, targetType));
//
//            MethodHandle withDefault = MethodHandles.insertArguments(typedNullCheck, 1,
//                    defaultSupplier.getDefaultValue(targetType));
//
//            MethodHandle getValue = MethodHandles.filterReturnValue(getter, withDefault);
//
//            return MethodHandles.filterArguments(setter, 1, getValue);
//        } else {
//            MethodHandle copyHandle = MethodHandles.filterArguments(setter, 1, getter);
//
//            Object defaultValue = defaultSupplier.getDefaultValue(targetType);
//            MethodHandle primitiveDefault = MethodHandles.constant(targetType, defaultValue);
//
//            MethodHandle guard = MethodHandles.dropArguments(
//                    MethodHandles.constant(boolean.class, false),
//                    0, targetType
//            );
//
//            return MethodHandles.guardWithTest(guard, primitiveDefault, copyHandle);
//        }
//    }
//
//    private MethodHandle createConvertedCopyHandle(MethodHandle getter, MethodHandle setter,
//                                                   BeanCopierSetting config, String propertyName) throws Throwable {
//        PropertyConverter<?, ?> converter = config.getConverter(propertyName);
//        //MethodHandle converterHandle = LOOKUP.unreflect(
//        //        converter.getClass().getMethod("apply", Object.class)
//        //);
//
//        // 方案A：直接查找接口的虚方法
//        MethodHandle converterHandle = LOOKUP.findVirtual(
//                PropertyConverter.class,           // 接口类型
//                "apply",                           // 方法名
//                MethodType.methodType(Object.class, Object.class)  // 签名: (Object) -> Object
//        );
//
//// 使用时需要传入 converter 实例作为第一个参数
//// 等价于：converter.apply(value)
//        MethodHandle boundHandle = MethodHandles.insertArguments(converterHandle, 0, converter);
//
//
//        MethodHandle typedConverter = MethodHandles.explicitCastArguments(boundHandle,
//                MethodType.methodType(setter.type().parameterType(1), getter.type().returnType()));
//
//        MethodHandle getValue = MethodHandles.filterReturnValue(getter, typedConverter);
//        return MethodHandles.filterArguments(setter, 1, getValue);
//    }
//
//    public static <T> T handleNullForReference(T value, T defaultValue) {
//        return value != null ? value : defaultValue;
//    }
//
//    private static void noOp(Object source, Object target) {
//    }
//
//    private List<PropertyMapping> findPropertyMappings(Class<?> sourceClass, Class<?> targetClass) {
//        List<PropertyMapping> mappings = new ArrayList<>();
//
//        Map<String, Field> targetFields = new HashMap<>();
//        for (Field field : targetClass.getDeclaredFields()) {
//            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
//                field.setAccessible(true);
//                targetFields.put(field.getName(), field);
//            }
//        }
//
//        for (Field sourceField : sourceClass.getDeclaredFields()) {
//            if (Modifier.isStatic(sourceField.getModifiers()) || Modifier.isFinal(sourceField.getModifiers())) {
//                continue;
//            }
//
//            String fieldName = sourceField.getName();
//            Field targetField = targetFields.get(fieldName);
//
//            if (targetField == null) {
//                continue;
//            }
//
//            try {
//                sourceField.setAccessible(true);
//                MethodHandle getter = LOOKUP.unreflectGetter(sourceField);
//
//                targetField.setAccessible(true);
//                MethodHandle setter = LOOKUP.unreflectSetter(targetField);
//
//                mappings.add(new PropertyMapping(fieldName, getter, setter, targetField.getType()));
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException("Failed to access field: " + fieldName, e);
//            }
//        }
//
//        return mappings;
//    }

}
