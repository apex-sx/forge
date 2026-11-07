/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * @author Terraqo
 */
public class MethodHandleUtils {
    public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    public static final MethodHandle BOOLEAN_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(boolean[].class);
    public static final MethodHandle CHAR_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(char[].class);
    public static final MethodHandle BYTE_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(byte[].class);
    public static final MethodHandle SHORT_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(short[].class);
    public static final MethodHandle INT_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(int[].class);
    public static final MethodHandle LONG_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(long[].class);
    public static final MethodHandle FLOAT_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(float[].class);
    public static final MethodHandle DOUBLE_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(double[].class);
    public static final MethodHandle OBJECT_ARRAY_LENGTH = MethodHandleUtils.getArrayLength(Object[].class);

    private static MethodHandle getArrayLength(Class<?> arrayClass) {
        try {
            // Java 9+ provides MethodHandles.arrayLength
            return MethodHandles.arrayLength(arrayClass);
        } catch (Throwable e) {
            // Fallback for Java 8 and earlier: access the 'length' field via lookup
            try {
                return LOOKUP.findStaticGetter(arrayClass, "length", int.class);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static MethodHandle setArray(Class<?> arrayClass, int index,Object value) {
        return MethodHandles.arrayElementSetter(arrayClass);
    }

    public static void main(String[] args) throws Throwable {
        System.out.println(OBJECT_ARRAY_LENGTH.invoke(new String[]{"true", "false"}));
    }
}
