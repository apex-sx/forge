/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.symbolic.Tuples;

import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public interface GeneralClass {

    static <T extends AccessibleObject & Member> T setAccessible(T input) {
        if (!Modifier.isPublic(input.getModifiers())) {
            input.setAccessible(true);
        }
        return input;
    }

    static String getClassName(Class<?> clz) {
        // We want a human-readable class name. getName() returns JVM signature.
        // getCanonicalName() returns proper string, unless it is hits the bug.
        // If it fails, then we will fall back to getName()
        //   https://bugs.openjdk.java.net/browse/JDK-8057919
        try {
            String n = clz.getCanonicalName();
            if (n != null) {
                return n;
            }
        } catch (Throwable e) {
            // fall-through
        }
        return clz.getName();
    }

    static <T> Class<T> getClass(boolean isThrow, Object input) {
        try {
            return (Class<T>) (input instanceof Class ? (Class<?>) input : input.getClass());
        } catch (Throwable e) {
            return Log.warnOrThrowError(isThrow, e, "Error to getting class");
        }
    }

    static <T> Class<T> getClass(boolean isThrow, String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (Throwable e) {
            return Log.warnOrThrowError(isThrow, e, "Error to getting class from '" + className + "'");
        }
    }

    static <T> Class<T> getClass(boolean isThrow, String className, ClassLoader loader) {
        return GeneralClass.getClass(isThrow, className, true, loader);
    }

    static <T> Class<T> getClass(boolean isThrow, String className, boolean initialize, ClassLoader loader) {
        try {
            return (Class<T>) Class.forName(className, initialize, loader);
        } catch (Throwable e) {
            return Log.warnOrThrowError(isThrow, e, "Error to getting class from '" + className + "'");
        }
    }

    static <T> Class<T> getClassWithSystem(boolean isThrow, String className) {
        return GeneralClass.getClass(isThrow, className, true, ClassLoader.getSystemClassLoader());
    }

    static <T> Class<T> getClassWithSystem(boolean isThrow, String className, boolean initialize) {
        return GeneralClass.getClass(isThrow, className, initialize, ClassLoader.getSystemClassLoader());
    }

    static Constructor<?> getConstructor(boolean isThrow, boolean isMustPublic, String type, Class<?>... args) {
        return GeneralClass.getConstructor(isThrow, isMustPublic, GeneralClass.getClass(isThrow, type), args);
    }

    static Constructor<?> getConstructor(boolean isThrow, boolean isMustPublic, Class<?> type, Class<?>... args) {
        try {
            return GeneralClass.setAccessible(isMustPublic ? type.getConstructor(args) : type.getDeclaredConstructor(args));
        } catch (Throwable e) {
            return Log.warnOrThrowError(isThrow, e, "Error to calling '" + type + "#get" + (isMustPublic ? "" : "Declared") + "Constructor' for getting 'Constructor'");
        }
    }

    static Method getMethod(boolean isThrow, boolean isMustPublic, String type, String methodName, Class<?>... args) {
        return GeneralClass.getMethod(isThrow, isMustPublic, GeneralClass.getClass(isThrow, type), methodName, args);
    }

    static Method getMethod(boolean isThrow, boolean isMustPublic, Class<?> type, String methodName, Class<?>... args) {
        try {
            return GeneralClass.setAccessible(isMustPublic ? type.getMethod(methodName, args) : type.getDeclaredMethod(methodName, args));
        } catch (Throwable e) {
            return Log.warnOrThrowError(isThrow, e, "Error to calling '" + type + "#get" + (isMustPublic ? "" : "Declared") + "Method' for getting method of '" + methodName + "'");
        }
    }

    static Field getField(boolean isThrow, boolean isMustPublic, String type, String fieldName) {
        return GeneralClass.getField(isThrow, isMustPublic, GeneralClass.getClass(isThrow, type), fieldName);
    }

    static Field getField(boolean isThrow, boolean isMustPublic, Class<?> type, String fieldName) {
        try {
            return GeneralClass.setAccessible(isMustPublic ? type.getField(fieldName) : type.getDeclaredField(fieldName));
        } catch (Throwable e) {
            return Log.warnOrThrowError(isThrow, e, "Error to calling '" + type + "#get" + (isMustPublic ? "" : "Declared") + "Field' for getting field of '" + fieldName + "'");
        }
    }

    /**
     * 创建实例
     *
     * @param type 实例类型
     * @param args 参数
     * @param <T>  对象类型
     * @return 创建对象
     * @throws ReflectiveOperationException 如果反射构造对象抛出异常
     */
    static <T> T newInstance(String type, Object... args) throws ReflectiveOperationException {
        return GeneralClass.newInstance(GeneralClass.getClass(true, type), args);
    }

    /**
     * 创建实例
     *
     * @param type 实例类型
     * @param args 参数
     * @param <T>  对象类型
     * @return 创建对象
     * @throws ReflectiveOperationException 如果反射构造对象抛出异常
     */
    static <T> T newInstance(Class<T> type, Object... args) throws ReflectiveOperationException {
        int modifiers = type.getModifiers();
        if (Modifier.isAbstract(modifiers)) {
            throw new IllegalAccessException("The class " + type + " is an abstract class");
        }
        if (!Modifier.isPublic(modifiers)) {
            throw new IllegalAccessException("The class " + type + " is not a public class");
        }
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + type.getName());
        }
        Constructor<?> constructor = null;
        for (Constructor<?> ctor : constructors) {
            if (ctor.getParameterCount() != args.length) continue;
            Class<?>[] parameterTypes = ctor.getParameterTypes();
            boolean match = true;
            for (int n = 0; n < args.length; n++) {
                Class<?> argType = args[n] == null ? null : args[n].getClass();
                if (GeneralIF.isCompatibilityFrom(argType, parameterTypes[n])) continue;
                match = false;
                break;
            }
            if (match) {
                constructor = ctor;
                break;
            }
        }
        if (constructor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + type.getName());
        }
        //noinspection unchecked
        return (T) GeneralClass.setAccessible(constructor).newInstance(args);
    }

    static Class<?> lookupSuperclassGenericTypeCached(final Class<?> suberclass, final Class<?> superclass, final int index) {
        return Cache.SUPERCLASS_GENERIC_TYPE.computeIfAbsent(new Tuples(suberclass, superclass, index),
                tuples -> GeneralClass.lookupSuperclassGenericType(suberclass, superclass, index));
    }

    static Class<?> lookupSuperclassGenericType(final Class<?> suberclass, final Class<?> superclass, final int index) {
        if (!superclass.isAssignableFrom(suberclass))
            throw new IllegalArgumentException("The class [" + superclass + "] is not a superclass of [" + suberclass + "]");
        TypeVariable<? extends Class<?>>[] typeParameters = superclass.getTypeParameters();
        if (typeParameters.length == 0 || index >= typeParameters.length)
            throw new IllegalArgumentException("The index [" + index + "] exceeds generic type list length [" + typeParameters.length + "]");
        final int[] arr = new int[]{0}, idx = new int[]{index};
        final Class<?>[] foundType = new Class<?>[]{Object.class};
        LinkedList<Integer> list = new LinkedList<>();
        //继承链，suberc子类，superc父类
        BiFunction<Class<?>, Class<?>, LinkedList<Class<?>>> chain = (suberc, superc) -> {
            LinkedList<Class<?>> classes = new LinkedList<>();
            if (suberc.isInterface()) {
                //获取接口维承链
                for (Class<?> cur = suberc; cur != null; ) {
                    classes.addFirst(cur);
                    Class<?>[] interfaces = cur.getInterfaces();
                    for (int i = 0; i < interfaces.length; i++) {
                        if (superc.isAssignableFrom(interfaces[i])) {
                            cur = superc == interfaces[i] ? null : interfaces[i];
                            list.addFirst(i);
                            break;
                        }
                    }
                }
            } else {
                for (Class<?> cur = suberc; cur != superc; cur = cur.getSuperclass()) classes.addFirst(cur);
            }
            return classes;
        };
        BiPredicate<Class<?>, Type> lookupType = (cur, type) -> {
            //找到,数组类型
            for (; type instanceof GenericArrayType; arr[0]++) {
                type = ((GenericArrayType) type).getGenericComponentType();
            }
            if (type instanceof TypeVariable) {
                //查找同一泛型确定索引位置
                TypeVariable<? extends Class<?>>[] tvs = cur.getTypeParameters();
                for (int i = 0; i < tvs.length; i++) {
                    if (tvs[i] != type) continue;
                    idx[0] = i;
                    type = tvs[i].getBounds()[0];
                    foundType[0] = (Class<?>) (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type);
                    break;
                }
            } else {
                //确定泛型类型，结束查找
                foundType[0] = (Class<?>) (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type);
                return true;//结束
            }
            return false;
        };
        BiFunction<Class<?>, Class<?>, Class<?>> lookup = (suberc, superc) -> {
            LinkedList<Class<?>> classes = chain.apply(suberc, superc);
            for (int c = 0; c < classes.size(); c++) {
                Class<?> cur = classes.get(c);
                Type type;
                if (suberc.isInterface()) {
                    type = list.isEmpty() ? cur.getTypeParameters()[idx[0]] : ((ParameterizedType) cur.getGenericInterfaces()[list.get(c)]).getActualTypeArguments()[idx[0]];
                } else {
                    type = ((ParameterizedType) cur.getGenericSuperclass()).getActualTypeArguments()[idx[0]];
                }
                if (lookupType.test(cur, type)) break;
            }
            if (arr[0] != 0) {
                foundType[0] = Array.newInstance(foundType[0], new int[arr[0]]).getClass();
            }
            return foundType[0];
        };
        if (!superclass.isInterface() || suberclass.isInterface()) {
            return lookup.apply(suberclass, superclass);
        } else {
            LinkedList<Class<?>> classes = chain.apply(suberclass, Object.class);
            for (Class<?> cur : classes) {
                Type[] genericInterfaces = cur.getGenericInterfaces();
                for (Type genericInterface : genericInterfaces) {
                    if (genericInterface instanceof Class) {
                        if (!superclass.isAssignableFrom((Class<?>) genericInterface)) continue;
                        return lookup.apply((Class<?>) genericInterface, superclass);
                    }
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                    Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                    if (!superclass.isAssignableFrom(rawType)) continue;
                    lookup.apply(rawType, superclass);
                    Type type = parameterizedType.getActualTypeArguments()[idx[0]];
                    if (!lookupType.test(cur, type)) return lookup.apply(classes.peekLast(), cur);
                    if (arr[0] != 0) {
                        foundType[0] = Array.newInstance(foundType[0], new int[arr[0]]).getClass();
                    }
                    return foundType[0];

                }

            }
        }
        return foundType[0];
    }

    class Cache {
        private static final Map<Tuples, Class<?>> SUPERCLASS_GENERIC_TYPE = new ConcurrentHashMap<>(16, 1);

        private Cache() {
        }
    }
}
