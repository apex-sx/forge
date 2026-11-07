/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public interface GeneralPrimitive {
    Info<?> NULL_BASIC_TYPE = new Info<>(-1, 0, null, null, null, null, null);
    Info<Boolean> INFO_Z = new Info<>(0, 1, Kit.T_Z, Kit.T_WZ, false, Boolean::parseBoolean, (b, v) -> b.put((byte) (v != null && v ? 1 : 0)));
    Info<Character> INFO_C = new Info<>(0, 2, Kit.T_C, Kit.T_WC, '\0', s -> s.isEmpty() ? '\0' : s.charAt(0), ByteBuffer::putChar);
    Info<Byte> INFO_B = new Info<>(1, 1, Kit.T_B, Kit.T_WB, 0, Byte::parseByte, ByteBuffer::put);
    Info<Short> INFO_S = new Info<>(2, 2, Kit.T_S, Kit.T_WS, 0, Short::parseShort, ByteBuffer::putShort);
    Info<Integer> INFO_I = new Info<>(3, 4, Kit.T_I, Kit.T_WI, 0, Integer::parseInt, ByteBuffer::putInt);
    Info<Long> INFO_J = new Info<>(4, 8, Kit.T_J, Kit.T_WJ, 0, Long::parseLong, ByteBuffer::putLong);
    Info<Float> INFO_F = new Info<>(5, 4, Kit.T_F, Kit.T_WF, 0, Float::parseFloat, ByteBuffer::putFloat);
    Info<Double> INFO_D = new Info<>(6, 8, Kit.T_D, Kit.T_WD, 0, Double::parseDouble, ByteBuffer::putDouble);

    Map<Class<?>, Info<?>> BASIC_TYPE = Kit.unmodifiableMap(
            new Class<?>[]{
                    Kit.T_Z, Kit.T_WZ, Kit.T_C, Kit.T_WC, Kit.T_B, Kit.T_WB, Kit.T_S, Kit.T_WS,
                    Kit.T_I, Kit.T_WI, Kit.T_J, Kit.T_WJ, Kit.T_F, Kit.T_WF, Kit.T_D, Kit.T_WD,
            },
            new Info<?>[]{
                    INFO_Z, INFO_Z, INFO_C, INFO_C, INFO_B, INFO_B, INFO_S, INFO_S,
                    INFO_I, INFO_I, INFO_J, INFO_J, INFO_F, INFO_F, INFO_D, INFO_D,
            }
    );

    /**
     * 是否包括基本类型信息
     *
     * @param input 输入类型
     * @return 如果有返回true，否则返回false
     */
    static boolean hasPrimitiveInfo(Class<?> input) {
        return BASIC_TYPE.containsKey(input);
    }

    /**
     * 是否包括基本类型信息
     *
     * @param input 输入类型
     * @return 返回基本类型信息
     */
    @SuppressWarnings("unchecked")
    static <T> Info<T> getPrimitiveInfo(Class<T> input) {
        return (Info<T>) BASIC_TYPE.getOrDefault(input, NULL_BASIC_TYPE);
    }

    /**
     * 基本类型的默认值
     *
     * @param input 输入类型
     * @return 如果是基本类型则返回默认值，否则返回null
     */
    static <T> T getDefaultValue(Class<T> input) {
        //noinspection unchecked
        return (T) GeneralPrimitive.getPrimitiveInfo(input).dv;
    }

    /**
     * 基本类型的等级
     *
     * @param input 输入类型
     * @return 返回基本类型的等级
     */
    static byte getLevel(Class<?> input) {
        return GeneralPrimitive.getPrimitiveInfo(input).lv;
    }

    /**
     * 基本类型的字节数量
     *
     * @param input 输入类型
     * @return 返回基本类型的字节数量
     */
    static byte getByteCount(Class<?> input) {
        return GeneralPrimitive.getPrimitiveInfo(input).bc;
    }

    /**
     * 基本类型的等级
     *
     * @param input 输入类型
     * @return 返回基本类型的等级
     */
    static <T> BiFunction<String, String, T> getStringParser(Class<T> input) {
        return GeneralPrimitive.getPrimitiveInfo(input).sp;
    }

    /**
     * 是否为包装类型
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    static Class<?> getWrapperType(Class<?> clazz) {
        Class<?> c = getPrimitiveInfo(clazz).wt;
        if (c == null) c = clazz;
        return c;
    }

    /**
     * 是否为原生类型
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    static Class<?> getPrimitiveType(Class<?> clazz) {
        Class<?> c = getPrimitiveInfo(clazz).pt;
        if (c == null) c = clazz;
        return c;
    }

    /**
     * 是否为包装类型
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    static boolean isWrapperType(Class<?> clazz) {
        return null != clazz && !clazz.isPrimitive() && GeneralPrimitive.getPrimitiveInfo(clazz) != NULL_BASIC_TYPE;
    }

    /**
     * 是否为原生类型
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    static boolean isPrimitiveType(Class<?> clazz) {
        return null != clazz && clazz.isPrimitive();
    }

    /**
     * 基本类型转数组
     *
     * @param input 输入类型
     * @return 如果是基本类型则返回默认值，否则返回null
     */
    static <T> byte[] toByteArray(T input, boolean bigEndian) {
        Class<T> type;
        if (input instanceof Class) {
            type = (Class<T>) input;
        } else {
            type = GeneralClass.getClass(true, input);
        }
        return GeneralPrimitive.getPrimitiveInfo(type).bp.apply(input, bigEndian);
    }

    /**
     * 基本类型的包装类型与原生类型互相转换
     *
     * @param input 输入类型
     * @param wrap  是否转为包装类型
     * @return 返回指定类型，如果不支持则返回{@code input}
     */
    static Class<?> flip(Class<?> input, boolean wrap) {
        Class<?> r = null;
        if (input.isPrimitive()) {
            if (wrap) r = GeneralPrimitive.getPrimitiveInfo(input).wt;
        } else {
            if (!wrap) {
                r = GeneralPrimitive.getPrimitiveInfo(input).pt;
                if (r == null && Log.isEnabledDebug()) {
                    Log.debug("Not a known primitive wrapper class: " + input);
                }
            }
        }
        if (r == null) r = input;
        return r;
    }

    class Info<T> {
        /**
         * 基本类型等级
         */
        private final byte lv;
        /**
         * 基本类型转字节的数量
         */
        private final byte bc;
        /**
         * 基本类型的原生类型
         */
        private final Class<?> pt;
        /**
         * 基本类型的包装类型
         */
        private final Class<?> wt;
        /**
         * 基本类型的默认值
         */
        private final Object dv;
        /**
         * 基本类型的转字节数组
         */
        private final BiFunction<T, Boolean, byte[]> bp;
        /**
         * 基本类型字符串解析
         */
        private final BiFunction<String, String, T> sp;

        private Info(int lv, int bc, Class<?> pt, Class<?> wt, Object dv, Function<String, T> sp, BiConsumer<ByteBuffer, T> bp) {
            this.lv = (byte) lv;
            this.bc = (byte) bc;
            this.pt = pt;
            this.wt = wt;
            this.dv = dv;
            this.bp = bp == null ? (v, b) -> new byte[0] : (v, b) -> {
                ByteBuffer byteBuffer = ByteBuffer.allocate(this.bc);
                byteBuffer.order(b ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                bp.accept(byteBuffer, v);
                return byteBuffer.array();
            };
            this.sp = sp == null ? null : (value, main) -> {
                try {
                    return sp.apply(value);
                } catch (Throwable e) {
                    throw new IllegalArgumentException(String.format("The '%s' value was set to '%s', must be an %s", main, value, pt.getTypeName()), e);
                }
            };
        }
    }
}
