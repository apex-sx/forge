/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 精确的浮点数运算
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralArithmetic {
    /**
     * 百分比
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    static double percent(double v1, double v2, int scale) {
        return GeneralArithmetic.percent(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 百分比
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        表示表示需要精确到小数点以后几位
     * @param roundingMode 凑整模式
     * @return 两个参数的商
     */
    static double percent(double v1, double v2, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal r;
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            r = BigDecimal.ZERO;
        } else {
            r = b1.divide(b2, scale + 2, roundingMode);
        }
        r = r.multiply(BigDecimal.valueOf(100));
        r = r.divide(BigDecimal.ONE, scale, roundingMode);
        return r.doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1    被加数
     * @param v2    加数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 两个参数的和
     */
    static double add(double v1, double v2, int scale) {
        return GeneralArithmetic.add(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1           被加数
     * @param v2           加数
     * @param scale        表示表示需要精确到小数点以后几位
     * @param roundingMode 凑整模式
     * @return 两个参数的和
     */
    static double add(double v1, double v2, int scale, RoundingMode roundingMode) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).divide(BigDecimal.ONE, scale, roundingMode).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1    被减数
     * @param v2    减数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 两个参数的差
     */
    static double sub(double v1, double v2, int scale) {
        return GeneralArithmetic.sub(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1           被减数
     * @param v2           减数
     * @param scale        表示表示需要精确到小数点以后几位
     * @param roundingMode 凑整模式
     * @return 两个参数的差
     */
    static double sub(double v1, double v2, int scale, RoundingMode roundingMode) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).divide(BigDecimal.ONE, scale, roundingMode).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1    被乘数
     * @param v2    乘数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 两个参数的积
     */
    static double mul(double v1, double v2, int scale) {
        return GeneralArithmetic.mul(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1           被乘数
     * @param v2           乘数
     * @param scale        表示表示需要精确到小数点以后几位
     * @param roundingMode 凑整模式
     * @return 两个参数的积
     */
    static double mul(double v1, double v2, int scale, RoundingMode roundingMode) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).divide(BigDecimal.ONE, scale, roundingMode).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    static double div(double v1, double v2) {
        return GeneralArithmetic.div(v1, v2, 10);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    static double div(double v1, double v2, int scale) {
        return GeneralArithmetic.div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        表示表示需要精确到小数点以后几位
     * @param roundingMode 凑整模式
     * @return 两个参数的商
     */
    static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, roundingMode).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    static double round(double v, int scale) {
        return GeneralArithmetic.round(v, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v            需要四舍五入的数字
     * @param scale        小数点后保留几位
     * @param roundingMode 凑整模式
     * @return 四舍五入后的结果
     */
    static double round(double v, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.divide(BigDecimal.ONE, scale, roundingMode).doubleValue();
    }
}
