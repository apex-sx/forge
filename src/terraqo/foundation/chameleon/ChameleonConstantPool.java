/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.chameleon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Terraqo
 */
public class ChameleonConstantPool {
    public static final byte BYTE_0 = 0, BYTE_1 = 1;
    public static final short SHORT_0 = 0, SHORT_1 = 1;
    public static final int INT_0 = 0, INT_1 = 1;
    public static final long LONG_0 = 0, LONG_1 = 1;
    public static final float FLOAT_0 = 0, FLOAT_1 = 1;
    public static final double DOUBLE_0 = 0, DOUBLE_1 = 1;
    public static final char CHAR_0 = '0', CHAR_1 = '1';
    public static final char CHAR_T = 'T', CHAR_F = 'F';
    // 缓存 -100 到 100 的 byte 值
    public static final Map<String, Byte> BYTE = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::byteValue));
    // 缓存 -100 到 100 的 short 值
    public static final Map<String, Short> SHORT = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::shortValue));
    // 缓存 -100 到 100 的 int 值
    public static final Map<String, Integer> INT = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::intValue));
    // 缓存 -100 到 100 的 long 值
    public static final Map<String, Long> LONG = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::longValue));
    // 缓存 -100 到 100 的 float 值
    public static final Map<String, Float> FLOAT = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::floatValue));
    // 缓存 -100 到 100 的 double 值
    public static final Map<String, Double> DOUBLE = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::doubleValue));
    // 缓存 -100 到 100 的 BigInteger 值
    public static final Map<String, BigInteger> BIG_INTEGER = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, v -> BigInteger.valueOf(v)));
    // 缓存 -100 到 100 的 BigDecimal 值
    public static final Map<String, BigDecimal> BIG_DECIMAL = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, BigDecimal::new));

    static {
        for (int i = 0; i <= 1000; i += 100) {
            Number v = i;
            String num = String.valueOf(v);
            SHORT.put(num, v.shortValue());
            INT.put(num, v.intValue());
            LONG.put(num, v.longValue());
            FLOAT.put(num, v.floatValue());
            DOUBLE.put(num, v.doubleValue());
            BIG_INTEGER.put(num, new BigInteger(num));
            BIG_DECIMAL.put(num, new BigDecimal(num));
            if (i != 0) {
                num = "+" + num;
                SHORT.put(num, v.shortValue());
                INT.put(num, v.intValue());
                LONG.put(num, v.longValue());
                FLOAT.put(num, v.floatValue());
                DOUBLE.put(num, v.doubleValue());
                BIG_INTEGER.put(num, new BigInteger(num));
                BIG_DECIMAL.put(num, new BigDecimal(num));
                v = -i;
                num = String.valueOf(v);
                SHORT.put(num, v.shortValue());
                INT.put(num, v.intValue());
                LONG.put(num, v.longValue());
                FLOAT.put(num, v.floatValue());
                DOUBLE.put(num, v.doubleValue());
                BIG_INTEGER.put(num, new BigInteger(num));
                BIG_DECIMAL.put(num, new BigDecimal(num));
            }
        }
        for (int i = 0; i <= 10000; i += 1000) {
            Number v = i;
            String num = String.valueOf(v);
            SHORT.put(num, v.shortValue());
            INT.put(num, v.intValue());
            LONG.put(num, v.longValue());
            FLOAT.put(num, v.floatValue());
            DOUBLE.put(num, v.doubleValue());
            BIG_INTEGER.put(num, new BigInteger(num));
            BIG_DECIMAL.put(num, new BigDecimal(num));
            if (i != 0) {
                num = "+" + num;
                SHORT.put(num, v.shortValue());
                INT.put(num, v.intValue());
                LONG.put(num, v.longValue());
                FLOAT.put(num, v.floatValue());
                DOUBLE.put(num, v.doubleValue());
                BIG_INTEGER.put(num, new BigInteger(num));
                BIG_DECIMAL.put(num, new BigDecimal(num));
                v = -i;
                num = String.valueOf(v);
                SHORT.put(num, v.shortValue());
                INT.put(num, v.intValue());
                LONG.put(num, v.longValue());
                FLOAT.put(num, v.floatValue());
                DOUBLE.put(num, v.doubleValue());
                BIG_INTEGER.put(num, new BigInteger(num));
                BIG_DECIMAL.put(num, new BigDecimal(num));
            }
        }

        // 增加 0.01 至 0.99 的缓存到 FLOAT_CACHE、DOUBLE_CACHE 和 BIG_DECIMAL_CACHE 中
        for (int i = 1; i <= 99; i++) {
            double v = i / 100.0;
            String num = String.format("%.2f", v);
            FLOAT.put(num, (float) v);
            DOUBLE.put(num, v);
            BIG_DECIMAL.put(num, BigDecimal.valueOf(v));

            // 添加带 '+' 符号的正数版本
            num = "+" + num;
            FLOAT.put(num, (float) v);
            DOUBLE.put(num, v);
            BIG_DECIMAL.put(num, BigDecimal.valueOf(v));

            // 添加负数版本
            v = -v;
            num = String.format("%.2f", v);
            FLOAT.put(num, (float) v);
            DOUBLE.put(num, v);
            BIG_DECIMAL.put(num, BigDecimal.valueOf(v));
        }
    }

    private ChameleonConstantPool() {
        throw new UnsupportedOperationException("The class cannot be instantiated");
    }
}
