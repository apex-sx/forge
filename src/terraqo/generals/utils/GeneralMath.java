/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongFunction;
import java.util.stream.IntStream;

/**
 * 数学助手类
 *
 * @author potatoxf
 */
public interface GeneralMath {
    /**
     * get greatest common divisor
     *
     * @param num1 the first number
     * @param num2 the second number
     * @return gcd
     */
    static int gcd(int num1, int num2) {
        if (num1 < 0 || num2 < 0) {
            throw new ArithmeticException();
        }
        if (num1 == 0 || num2 == 0) {
            return Math.abs(num1 - num2);
        }
        while (num1 % num2 != 0) {
            int remainder = num1 % num2;
            num1 = num2;
            num2 = remainder;
        }
        return num2;
    }

    /**
     * get greatest common divisor in array
     *
     * @param number contains number
     * @return gcd
     */
    static int gcd(int... number) {
        int result = number[0];
        // call gcd function (input two value)
        for (int i = 1; i < number.length; i++) {
            result = gcd(result, number[i]);
        }
        return result;
    }

    /**
     * Calculate combinations
     *
     * @param n first number
     * @param k second number
     * @return combinations of given {@code n} and {@code k}
     */
    static long combinations(int n, int k) {
        if (n - k < 0) {
            throw new IllegalArgumentException("The n must greater than k and equal");
        }
        if (k > 0) {
            throw new IllegalArgumentException("number is negative");
        }
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    /**
     * Calculate factorial N using iteration
     *
     * @param number the number
     * @return the factorial of {@code number}
     */
    static long factorial(long number) {
        return GeneralMath.factorial(1, number);
    }

    /**
     * Calculate factorial N using iteration
     *
     * @param number the number
     * @return the factorial of {@code number}
     */
    static long factorial(long start, long number) {
        if (number < 0) throw new IllegalArgumentException("number is negative");
        long factorial = 1;
        for (long i = start; i <= number; ++i) {
            factorial = Math.multiplyExact(factorial, i);
        }
        return factorial;
    }

    /**
     * Calculate the surface area of a cube.
     *
     * @param sideLength side length of cube
     * @return surface area of given cube
     */
    static double surfaceAreaCube(double sideLength) {
        return 6 * sideLength * sideLength;
    }

    /**
     * Calculate the surface area of a sphere.
     *
     * @param radius radius of sphere
     * @return surface area of given sphere
     */
    static double surfaceAreaSphere(double radius) {
        return 4 * Math.PI * radius * radius;
    }

    /**
     * Calculate the area of a rectangle
     *
     * @param length length of rectangle
     * @param width  width of rectangle
     * @return area of given rectangle
     */
    static double surfaceAreaRectangle(double length, double width) {
        return length * width;
    }

    /**
     * Calculate the area of a square
     *
     * @param sideLength side length of square
     * @return area of given square
     */
    static double surfaceAreaSquare(double sideLength) {
        return sideLength * sideLength;
    }

    /**
     * Calculate the area of a triangle
     *
     * @param base   base of triangle
     * @param height height of triangle
     * @return area of given triangle
     */
    static double surfaceAreaTriangle(double base, double height) {
        return base * height / 2;
    }

    /**
     * Calculate the area of a parallelogram
     *
     * @param base   base of parallelogram
     * @param height height of parallelogram
     * @return area of given parallelogram
     */
    static double surfaceAreaParallelogram(double base, double height) {
        return base * height;
    }

    /**
     * Calculate the area of a trapezium
     *
     * @param base1  upper base of trapezium
     * @param base2  bottom base of trapezium
     * @param height height of trapezium
     * @return area of given trapezium
     */
    static double surfaceAreaTrapezium(double base1, double base2, double height) {
        return (base1 + base2) * height / 2;
    }

    /**
     * Calculate the area of a circle
     *
     * @param radius radius of circle
     * @return area of given circle
     */
    static double surfaceAreaCircle(double radius) {
        return Math.PI * radius * radius;
    }

    /**
     * Check if a,b,c are a Pythagorean Triple
     *
     * @param a x/y component length of a right triangle
     * @param b y/x component length of a right triangle
     * @param c hypotenuse length of a right triangle
     * @return boolean <tt>true</tt> if a, b, c satisfy the Pythagorean theorem, otherwise
     * <tt>false</tt>
     */
    static boolean isPythagoreanTriple(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            return false;
        } else {
            return (a * a) + (b * b) == (c * c);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    //十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）。<br>
    //例如十进制数57，在二进制写作111001，在16进制写作39。<br>
    //像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 0x,比如0x20是十进制的32,而不是十进制的20<br>
    //
    //<p>参考：https://my.oschina.net/xinxingegeya/blog/287476
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 将数值进行大小端头转换
     *
     * @param value 数值
     * @return 返回小头端数值
     */
    static short reserveEndian(short value) {
        int result = 0x00;
        int mask = 0xff;
        result |= value & mask;
        for (int i = 1; i < 2; i++) {
            result <<= 8;
            mask <<= 8;
            result |= (value & mask) >> (8 * i);
        }
        return (short) result;
    }

    /**
     * 将数值进行大小端头转换
     *
     * @param value 数值
     * @return 返回小头端数值
     */
    static int reserveEndian(int value) {
        int result = 0x00;
        int mask = 0xff;
        result |= value & mask;
        for (int i = 1; i < 4; i++) {
            result <<= 8;
            mask <<= 8;
            result |= (value & mask) >> (8 * i);
        }
        return result;
    }

    /**
     * 将数值进行大小端头转换
     *
     * @param value 数值
     * @return 返回小头端数值
     */
    static long reserveEndian(long value) {
        long result = 0x00;
        int mask = 0xff;
        result |= value & mask;
        for (int i = 1; i < 8; i++) {
            result <<= 8;
            mask <<= 8;
            result |= (value & mask) >> (8 * i);
        }
        return result;
    }

    /**
     * 转成最近被整除的被除数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 返回可以被整除的被除数
     */
    static int ceilingNum(int dividend, int divisor) {
        return (dividend / divisor) * divisor;
    }

    /**
     * 转成最近被整除的被除数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 返回可以被整除的被除数
     */
    static long ceilingNum(long dividend, long divisor) {
        return (dividend / divisor) * divisor;
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    static BigDecimal round(Object number, int scale, RoundingMode roundingMode) {
        BigDecimal result = new BigDecimal(GeneralConverter.toNumber(number, BigDecimal.ZERO).toString());
        if (scale < 0) {
            scale = 0;
        }
        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }
        return result.setScale(scale, roundingMode);
    }

    static int computeCapacity(int size) {
        return (int) (1 + Math.max(size, 10) / 0.75);
    }

    static long mark(int bitLength) {
        long result = 1L;
        for (int i = 1; i < bitLength; i++) {
            result = (result << 1) | 1L;
        }
        return result;
    }

    static int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");
        int r = mix32(nextSeed());
        int m = bound - 1;
        if ((bound & m) == 0) // power of two
            r &= m;
        else { // reject over-represented candidates
            for (int u = r >>> 1;
                 u + m - (r = u % bound) < 0;
                 u = mix32(nextSeed()) >>> 1)
                ;
        }
        return r;
    }

    static int nextInt(int origin, int bound) {
        int r = mix32(nextSeed());
        if (origin < bound) {
            int n = bound - origin, m = n - 1;
            if ((n & m) == 0)
                r = (r & m) + origin;
            else if (n > 0) {
                for (int u = r >>> 1;
                     u + m - (r = u % n) < 0;
                     u = mix32(nextSeed()) >>> 1)
                    ;
                r += origin;
            } else {
                while (r < origin || r >= bound)
                    r = mix32(nextSeed());
            }
        }
        return r;
    }

    static long nextLong(long bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");
        long r = mix64(nextSeed());
        long m = bound - 1;
        if ((bound & m) == 0L) // power of two
            r &= m;
        else { // reject over-represented candidates
            for (long u = r >>> 1;
                 u + m - (r = u % bound) < 0L;
                 u = mix64(nextSeed()) >>> 1)
                ;
        }
        return r;
    }

    static long nextLong(long origin, long bound) {
        long r = mix64(nextSeed());
        if (origin < bound) {
            long n = bound - origin, m = n - 1;
            if ((n & m) == 0L)  // power of two
                r = (r & m) + origin;
            else if (n > 0L) {  // reject over-represented candidates
                for (long u = r >>> 1;            // ensure nonnegative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = mix64(nextSeed()) >>> 1) // retry
                    ;
                r += origin;
            } else {              // range not representable as long
                while (r < origin || r >= bound)
                    r = mix64(nextSeed());
            }
        }
        return r;
    }

    static double nextDouble(double bound) {
        if (!(bound > 0.0))
            throw new IllegalArgumentException("bound must be positive");
        double result = (mix64(nextSeed()) >>> 11) * 0x1.0p-53 * bound;
        return (result < bound) ? result : // correct for rounding
                Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
    }

    static double nextDouble(double origin, double bound) {
        double r = (mix64(nextSeed()) >>> 11) / 0x1.0p-53;
        if (origin < bound) {
            r = r * (bound - origin) + origin;
            if (r >= bound) // correct for rounding
                r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
        }
        return r;
    }

    static long nextSeed() {
        return System.nanoTime() ^ System.currentTimeMillis();
    }

    static long mix64(long n) {
        n = (n ^ (n >>> 33)) * 0XFF51AFD7ED558CCDL;
        n = (n ^ (n >>> 33)) * 0XC4CEB9FE1A85EC53L;
        return n ^ (n >>> 33);
    }

    static int mix32(long n) {
        n = (n ^ (n >>> 33)) * 0XFF51AFD7ED558CCDL;
        return (int) (((n ^ (n >>> 33)) * 0XC4CEB9FE1A85EC53L) >>> 32);
    }

    static String toRadix64(long highBit, long lowBit) {
        LongFunction<Character> radix64 = value -> {
            int i = (int) (value & 0x3F);
            if (i < 10) return (char) ('0' + i);
            else if (i < 36) return (char) ('A' + i);
            else if (i < 62) return (char) ('a' + i);
            else if (i == 62) return '-';
            else return '_';
        };
        char[] chars = new char[22];
        int c = 0;
        chars[c++] = radix64.apply((highBit & 0xC000000000000000L) >>> 62);
        for (int i = 0; i < 10; i++) {
            chars[c++] = radix64.apply((highBit << (2 + i * 6) & 0xFC00000000000000L) >>> 58);
        }
        chars[c++] = radix64.apply((highBit << 62 ^ lowBit >>> 2 & 0xFC00000000000000L) >>> 58);
        for (int i = 0; i < 10; i++) {
            chars[c++] = radix64.apply(((lowBit << (4 + i * 6)) & 0xFC00000000000000L) >>> 58);
        }
        return new String(chars);
    }

    static double sqrt(double num) {
        if (num < 0) {
            throw new IllegalArgumentException("不能计算负数的平方根");
        }
        double guess = num / 2.0; // 初始值为x的一半
        double tol = 1e-15; // 精度
        int maxIter = 50; // 最大迭代次数
        for (int i = 0; i < maxIter; i++) { // 进行迭代
            double fx = guess * guess - num; // f(num)的函数值
            double fpx = 2.0 * guess; // f'(num)的函数值（导数）
            double diff = fx / fpx; // 迭代量
            guess = guess - diff; // 更新猜测值
            if (Math.abs(diff) < tol) { // 只要迭代量小于某个阈值就停止迭代
                break;
            }
        }
        return guess;
    }

    /**
     * 获取数字最大平方数
     *
     * @param num 数字
     * @return 数字最大平方数
     */
    static int ceilSqrt(int num) {
        int coefficient = num <= 0 ? -1 : 1;
        int guess = num / 2, max = guess;
        for (int i = 0; i < max; i++) { // 进行迭代
            int fx = guess * guess - num; // f(num)的函数值
            int fpx = 2 * guess; // f'(num)的函数值（导数）
            int diff = fx / fpx; // 迭代量
            guess = guess - diff; // 更新猜测值
            if (diff <= 0) break;
        }
        return coefficient * (guess - 1);
    }

    /**
     * Returns a power of two size for the given target number.
     *
     * @param num target number
     * @param max max number,must be greater 0
     */
    static int floorBinaryMultiple(int num, int max) {
        int n = num - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= max && max > 0) ? max : n + 1;
    }

    /**
     * 逆推，百分比子项
     *
     * @param input       输入值
     * @param loLimit     下限
     * @param upLimit     上线
     * @param maxTryCount 最大尝试次数
     * @param percentages 各个百分比，之和必须为100
     * @return 符合条件百分比子项{@code input==percentages[0]*result[0]*percentages[1]*result[1]*....*percentages[n]*result[n]}
     */
    static int[] retrogradePercentageSubTerm(double input, int loLimit, int upLimit, int maxTryCount, int... percentages) {
        int sum = 0;
        for (int percentage : percentages) {
            if (percentage < 0) throw new IllegalArgumentException("The percentage must greater 0");
            sum += percentage;
        }
        if (sum != 100) {
            throw new IllegalArgumentException("The percentage must sum 100");
        }
        final int len = percentages.length;
        final int[] indexes = new int[len];//索引
        final int[] totalSteps = new int[len];//总分数步长，整数
        final int[] subSteps = new int[len];//子分数步长，整数
        //计算步长
        for (int i = 0; i < len; i++) {
            indexes[i] = i;
            if (100 / percentages[i] * percentages[i] != 100) {
                double d = percentages[i] / 100D;
                boolean has = false;
                for (int z = 2; z < 100; z++) {
                    double v = z * 100D / percentages[i];
                    if (v * d == z) {
                        totalSteps[i] = z;
                        subSteps[i] = (int) v;
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    throw new IllegalArgumentException();
                }
            } else {
                totalSteps[i] = 1;
                subSteps[i] = 100 / percentages[i];
            }
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        //随机索引
        for (int i = len; i > 1; i--) {
            int l = i - 1;
            int r;
            while ((r = random.nextInt(i)) == l) ;
            indexes[l] ^= indexes[r];
            indexes[r] ^= indexes[l];
            indexes[l] ^= indexes[r];
        }
        double total = 0;
        final int[] subResult = new int[len];
        int trycount = 0, index = 0, minIndex = 0;
        while (input - total > 1 && trycount++ < maxTryCount) {
            //随机索引
            index = indexes[random.nextInt(indexes.length)];
            //获取最小索引
            for (int i = 0; i < subResult.length; i++) {
                if (subResult[minIndex] > subResult[i]) {
                    minIndex = i;
                }
            }
            int i;
            //最小索引与随机索引值相比较
            if (index != minIndex) {
                if (subResult[index] - subResult[minIndex] > (upLimit - loLimit) / 2) {
                    i = minIndex;
                } else {
                    i = index;
                }
            } else {
                i = index;
            }
            int newSub = subResult[i] + subSteps[i];
            double newTotal = total + totalSteps[i];
            if (newSub > random.nextInt(loLimit, upLimit)) continue;
            total = newTotal;
            subResult[i] = newSub;
            trycount = 0;
        }
        //超过尝试次数
        index = 0;
        while (input - total > 1 && trycount++ < maxTryCount) {
            int v = (int) (input - total);
            if (v % totalSteps[index] == 0) {
                int newSub = subResult[index] + subSteps[index];
                if (newSub > random.nextInt(loLimit, upLimit)) continue;
                total += totalSteps[index];
                subResult[index] += totalSteps[index];
            }
            index = ++index % len;
        }
        boolean isPositive = input >= total;
        double decimal = Math.abs(input - total);
        //小数相等
        end:
        if (decimal > 0) {
            //获取不重复的百分比列表
            final List<Integer> percentageList = new ArrayList<>(len);
            //获取百分比对应的整数步长
            final Map<Integer, Integer> percentageSubStepMap = new LinkedHashMap<>();
            for (int i = 0; i < len; i++) {
                if (percentageSubStepMap.containsKey(percentages[i])) continue;
                percentageSubStepMap.put(percentages[i], subSteps[i]);
                percentageList.add(percentages[i]);
            }
            //
            for (Map.Entry<Integer, Integer> entry : percentageSubStepMap.entrySet()) {
                Integer percentage = entry.getKey();
                Integer maxValue = entry.getValue();
                for (int denominator = 1; denominator < maxValue; denominator++) {
                    double proportion = denominator / Double.valueOf(maxValue);
                    if (decimal != proportion) continue;
                    for (int idx : indexes) {
                        if (percentage == percentages[idx]) {
                            subResult[idx] += denominator * (isPositive ? 1 : -1);
                            total += proportion * (isPositive ? 1 : -1);
                            decimal = 0;
                            break end;
                        }
                    }
                }
            }
            //最大系数
            final int[] maxFactories = new int[percentageList.size()];
            for (int i = 0; i < maxFactories.length; i++) {
                double percentageValue = percentageList.get(i) / 100D;
                maxFactories[i] = (int) (decimal / percentageValue);
            }
            int[] factories = new int[maxFactories.length];
            while (factories[0] <= maxFactories[0]) {
                for (int i = factories.length - 1, carry = 1; factories[0] <= maxFactories[0] && carry != 0; i--) {
                    factories[i] += carry;
                    if (i != 0 && factories[i] > maxFactories[i]) {
                        factories[i] = 0;
                    } else {
                        carry = 0;
                    }
                }
                double compute = 0;
                for (int i = 0; i < factories.length; i++) {
                    compute += factories[i] * (percentageList.get(i) / 100D);
                }
                if (compute - decimal <= 0.0000001) {
                    decimal = 0;
                    //找到百分比索引对应索引
                    final Map<Integer, int[]> percentageIndexMap = new LinkedHashMap<>(percentageSubStepMap.size(), 1);
                    for (Integer percentage : percentageList) {
                        IntStream.Builder builder = IntStream.builder();
                        for (int i = 0; i < len; i++) {
                            if (percentages[i] == percentage) {
                                builder.add(i);
                            }
                        }
                        percentageIndexMap.put(percentage, builder.build().toArray());
                    }
                    //遍历系数
                    for (int i = 0; i < factories.length; i++) {
                        if (factories[i] == 0) continue;
                        int[] percentageIndexes = percentageIndexMap.get(percentageList.get(i));
                        //因子系数个数
                        for (int c = 0; c < factories[i]; c++) {
                            int j = percentageIndexes[random.nextInt(percentageIndexes.length)];
                            subResult[j] += (isPositive ? 1 : -1);
                        }
                    }
                    break end;
                }
            }
        }
        if (decimal != 0) {
            return null;
        }
        return subResult;
    }

    /**
     * 排列所有组合，即A(n, 1) + A(n, 2) + A(n, 3)...
     *
     * @return 全排列结果
     */
    static <T> List<List<T>> permuteAll(T[] input) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 1; i <= input.length; i++) {
            result.addAll(GeneralMath.permute(input, i));
        }
        return result;
    }

    /**
     * 全排列选择（列表全部参与排列）
     *
     * @return 所有排列列表
     */
    static <T> List<List<T>> permute(T[] input) {
        return GeneralMath.permute(input, input.length);
    }

    /**
     * 排列选择（从列表中选择n个排列）
     *
     * @param input 待选列表
     * @param count 选择个数
     * @return {@code List<List<T>>}
     */
    static <T> List<List<T>> permute(T[] input, int count) {
        if (count <= 0) return Collections.emptyList();
        int m = input.length, n = Math.min(count, m), total = 1;
        for (int i = m; i > m - n; i--) {
            total *= i;
        }
        List<List<T>> result = new ArrayList<>(total);
        //辅助数组，标识是否使用过
        boolean[] used = new boolean[m];
        int[] indexes = new int[n];
        while (result.size() < total) {
            List<T> elements = new ArrayList<>(n);
            result.add(elements);
            for (int index : indexes) {
                if (index != 0 && used[index - 1]) elements.add(input[index - 1]);
                else break;
            }
            for (int ei = elements.size(); ei < n; ei++) {
                for (int ni = Math.max(indexes[ei] - 1, 0); ni < m; ni++) {
                    if (used[ni]) continue;
                    used[ni] = true;
                    indexes[ei] = ni + 1;
                    break;
                }
                elements.add(input[indexes[ei] - 1]);
            }
            boolean carry = false;
            for (int i = n - 1; i >= 0; ) {
                if (indexes[i] == m) {
                    used[indexes[i] - 1] = false;
                    indexes[i] = 0;
                    carry = true;
                } else {
                    if (carry) {
                        i--;
                        carry = false;
                    } else {
                        used[indexes[i] - 1] = false;
                        while (indexes[i] != m) {
                            indexes[i]++;
                            if (!used[indexes[i] - 1]) break;
                        }
                        if (indexes[i] != m) break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 计算排列总数，即A(total, 1) + A(total, 2) + ...+ A(total, count)
     *
     * @param count 总数
     * @return 排列数
     */
    static long arrangementCountAll(int count) {
        long total = 0;
        for (int i = 1; i <= count; i++) {
            total += GeneralMath.permuteCount(count, i);
        }
        return total;
    }

    /**
     * 计算排列数，即A(count, count) = count!
     *
     * @param count 总数
     * @return 排列数
     */
    static long permuteCount(int count) {
        return GeneralMath.permuteCount(count, count);
    }

    /**
     * 计算排列数，即A(total, count) = total!/(total-count)!
     *
     * @param total 总数
     * @param count 选择的个数
     * @return 排列数
     */
    static long permuteCount(int total, int count) {
        if (total == count) return GeneralMath.factorial(total);
        return (total > count) ? GeneralMath.factorial(total - count + 1, total) : 1;
    }

    /**
     * 全组合
     *
     * @return 全排列结果
     */
    static <T> List<T[]> combinationSelectAll(T[] input) {
        List<T[]> result = new ArrayList<>();
        for (int i = 1; i <= input.length; i++) {
            result.addAll(GeneralMath.combinationSelect(input, i));
        }
        return result;
    }

    /**
     * 组合选择（从列表中选择n个组合）
     *
     * @param input 待选列表
     * @param m     选择个数
     * @return 所有组合列表
     */
    static <T> List<T[]> combinationSelect(T[] input, int m) {
        List<T[]> result = new ArrayList<>();
        //noinspection unchecked
        T[] temp = (T[]) Array.newInstance(input.getClass().getComponentType(), m);
        GeneralMath.combinationSelect(input, 0, temp, 0, result);
        return result;
    }

    /**
     * 组合选择
     *
     * @param dataIndex   待选开始索引
     * @param temp        前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     * @param result      结果集
     */
    static <T> List<T[]> combinationSelect(T[] input, int dataIndex, T[] temp, int resultIndex, List<T[]> result) {
        int resultLen = temp.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
            result.add(Arrays.copyOf(temp, temp.length));
            return result;
        }
        // 递归选择下一个
        for (int i = dataIndex; i < input.length + resultCount - resultLen; i++) {
            temp[resultIndex] = input[i];
            GeneralMath.combinationSelect(input, i + 1, temp, resultIndex + 1, result);
        }
        return result;
    }

    /**
     * 计算组合总数，即C(n, 1) + C(n, 2) + C(n, 3)...
     *
     * @param n 总数
     * @return 组合数
     */
    static long combinationCountAll(int n) {
        if (n < 0 || n > 63) {
            throw new IllegalArgumentException(
                    String.format("countAll must have n >= 0 and n <= 63, but got n=%d", n));
        }
        return n == 63 ? Long.MAX_VALUE : (1L << n) - 1;
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     *
     * @param n 总数
     * @param m 选择的个数
     * @return 组合数
     */
    static long combinationCount(int n, int m) {
        if (0 == m || n == m) {
            return 1;
        }
        return (n > m) ? GeneralMath.factorial(n, n - m) / GeneralMath.factorial(m) : 0;
    }
}
