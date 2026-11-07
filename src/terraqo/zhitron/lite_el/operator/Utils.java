package com.github.zhitron.lite_el.operator;

/**
 * @author zhitron
 */
public class Utils {


    public static Object BIT_OR(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object BIT_OR(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_OR(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_OR(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_OR(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_OR(float left, float right) {
        return left * right;
    }

    public static Object BIT_OR(double left, double right) {
        return left * right;
    }





    public static Object BIT_AND(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object BIT_AND(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_AND(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_AND(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_AND(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_AND(float left, float right) {
        return left * right;
    }

    public static Object BIT_AND(double left, double right) {
        return left * right;
    }




    public static Object BIT_XOR(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object BIT_XOR(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_XOR(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_XOR(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_XOR(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_XOR(float left, float right) {
        return left * right;
    }

    public static Object BIT_XOR(double left, double right) {
        return left * right;
    }





    public static Object BIT_LEFT_SHIFT(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object BIT_LEFT_SHIFT(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_LEFT_SHIFT(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_LEFT_SHIFT(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_LEFT_SHIFT(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_LEFT_SHIFT(float left, float right) {
        return left * right;
    }

    public static Object BIT_LEFT_SHIFT(double left, double right) {
        return left * right;
    }




    public static Object BIT_RIGHT_SHIFT(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object BIT_RIGHT_SHIFT(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_RIGHT_SHIFT(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_RIGHT_SHIFT(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_RIGHT_SHIFT(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object BIT_RIGHT_SHIFT(float left, float right) {
        return left * right;
    }

    public static Object BIT_RIGHT_SHIFT(double left, double right) {
        return left * right;
    }





    public static Object UNSIGNED_RIGHT_SHIFT(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object UNSIGNED_RIGHT_SHIFT(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object UNSIGNED_RIGHT_SHIFT(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object UNSIGNED_RIGHT_SHIFT(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object UNSIGNED_RIGHT_SHIFT(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object UNSIGNED_RIGHT_SHIFT(float left, float right) {
        return left * right;
    }

    public static Object UNSIGNED_RIGHT_SHIFT(double left, double right) {
        return left * right;
    }




    public static Object MULTIPLY(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object MULTIPLY(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MULTIPLY(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MULTIPLY(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MULTIPLY(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MULTIPLY(float left, float right) {
        return left * right;
    }

    public static Object MULTIPLY(double left, double right) {
        return left * right;
    }


    public static Object POWER(boolean left, boolean right) {
        return 1;
    }

    public static Object POWER(char left, char right) {
        return Utils.POWER((int) left, right);
    }

    public static Object POWER(byte left, byte right) {
        return Utils.POWER((int) left, right);
    }

    public static Object POWER(short left, short right) {
        return Utils.POWER((int) left, right);
    }

    public static Object POWER(int left, int right) {
        return Utils.POWER((long) left, right);
    }

    public static Object POWER(long left, long right) {
        // 特殊情况：0的负数次幂未定义
        if (left == 0 && right < 0) {
            throw new ArithmeticException("0的负数次幂未定义");
        }

        // 特殊情况：任何数的0次方等于1
        if (right == 0) {
            return 1L;
        }

        // 特殊情况：1的任意次幂都为1
        if (left == 1) {
            return 1L;
        }

        // 特殊情况：-1的奇/偶次幂分别为-1/1
        if (left == -1) {
            return (right % 2 == 0) ? 1L : -1L;
        }

        // 快速幂算法用于较小指数的情况（避免Math.pow带来的性能损耗）
        if (right >= 0 && right <= 62) { // 避免溢出风险，限制合理范围
            return fastPower(left, right);
        } else if (right < 0 && right >= -62) {
            // 支持小负指数的快速幂处理
            long positiveExpResult = fastPower(left, -right);
            if (positiveExpResult == 0) {
                throw new ArithmeticException("底数非零但结果为0，无法取倒数");
            }
            double reciprocal = 1.0 / positiveExpResult;
            if (Math.abs(reciprocal - Math.round(reciprocal)) > 1e-10) {
                throw new ArithmeticException("负指数结果不是整数，无法表示为long");
            }
            return Math.round(reciprocal);
        } else {
            // 使用Math.pow处理其他复杂情况
            double result = Math.pow(left, right);

            // 检查NaN
            if (Double.isNaN(result)) {
                throw new ArithmeticException("计算结果为NaN");
            }

            // 检查无穷大
            if (Double.isInfinite(result)) {
                throw new ArithmeticException("计算结果超出long类型范围");
            }

            // 更安全的方式判断是否为整数
            double floorResult = Math.floor(result);
            if (Math.abs(result - floorResult) > 1e-10) {
                throw new ArithmeticException("计算结果不是整数，无法转换为long类型");
            }

            // 检查是否超出long范围
            if (floorResult > Long.MAX_VALUE || floorResult < Long.MIN_VALUE) {
                throw new ArithmeticException("计算结果超出long类型范围");
            }

            return (long) floorResult;
        }
    }

    // 提取快速幂逻辑作为独立函数
    private static long fastPower(long base, long exp) {
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                // 注意此处可能发生溢出，但由于限制了最大指数为62，一般不会发生
                result *= base;
            }
            base *= base;
            exp >>= 1;
        }
        return result;
    }


    public static Object POWER(float left, float right) {
        // 处理特殊情况：0的负数次幂未定义
        if (left == 0 && right < 0) {
            throw new ArithmeticException("0的负数次幂未定义");
        }

        // 处理特殊情况：负数的非整数次幂在实数范围内未定义
        if (left < 0 && right < 0) {
            // 这里允许负底数的负指数运算，Math.pow会返回正确结果
        }

        double result = Math.pow(left, right);

        // 检查结果是否超出long范围
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("计算结果超出long类型范围");
        }

        // 检查结果是否为NaN
        if (Double.isNaN(result)) {
            throw new ArithmeticException("计算结果为NaN");
        }

        return (long) result;
    }

    public static Object POWER(double left, double right) {
        // 处理特殊情况：0的负数次幂未定义
        if (left == 0 && right < 0) {
            throw new ArithmeticException("0的负数次幂未定义");
        }

        // 处理特殊情况：负数的非整数次幂在实数范围内未定义
        if (left < 0 && right < 0) {
            // 这里允许负底数的负指数运算，Math.pow会返回正确结果
        }

        double result = Math.pow(left, right);

        // 检查结果是否超出long范围
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("计算结果超出long类型范围");
        }

        // 检查结果是否为NaN
        if (Double.isNaN(result)) {
            throw new ArithmeticException("计算结果为NaN");
        }

        return (long) result;
    }


    public static Object DIVIDING(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object DIVIDING(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object DIVIDING(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object DIVIDING(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object DIVIDING(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object DIVIDING(float left, float right) {
        return left * right;
    }

    public static Object DIVIDING(double left, double right) {
        return left * right;
    }


    public static Object MODULO(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object MODULO(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MODULO(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MODULO(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MODULO(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MODULO(float left, float right) {
        return left * right;
    }

    public static Object MODULO(double left, double right) {
        return left * right;
    }

    public static Object PLUS(Object left, Object right) {
        return null;
    }

    public static Object PLUS(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object PLUS(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object PLUS(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object PLUS(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object PLUS(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object PLUS(float left, float right) {
        return left * right;
    }

    public static Object PLUS(double left, double right) {
        return left * right;
    }

    public static Object MINUS(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object MINUS(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MINUS(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MINUS(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MINUS(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MINUS(float left, float right) {
        return left * right;
    }

    public static Object MINUS(double left, double right) {
        return left * right;
    }


    public static Object LESS_THEN(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object LESS_THEN(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN(float left, float right) {
        return left * right;
    }

    public static Object LESS_THEN(double left, double right) {
        return left * right;
    }


    public static Object LESS_THEN(String left, String right) {
        return null;
    }


    public static Object LESS_THEN_OR_EQUAL(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object LESS_THEN_OR_EQUAL(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN_OR_EQUAL(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN_OR_EQUAL(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN_OR_EQUAL(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object LESS_THEN_OR_EQUAL(float left, float right) {
        return left * right;
    }

    public static Object LESS_THEN_OR_EQUAL(double left, double right) {
        return left * right;
    }

    public static Object LESS_THEN_OR_EQUAL(String left, String right) {
        return null;
    }



    public static Object MORE_THEN(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object MORE_THEN(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN(float left, float right) {
        return left * right;
    }

    public static Object MORE_THEN(double left, double right) {
        return left * right;
    }
    public static Object MORE_THEN(String left, String right) {
        return null;
    }



    public static Object MORE_THEN_OR_EQUAL(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object MORE_THEN_OR_EQUAL(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN_OR_EQUAL(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN_OR_EQUAL(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN_OR_EQUAL(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object MORE_THEN_OR_EQUAL(float left, float right) {
        return left * right;
    }

    public static Object MORE_THEN_OR_EQUAL(double left, double right) {
        return left * right;
    }
    public static Object MORE_THEN_OR_EQUAL(String left, String right) {
        return null;
    }



    public static Object EQUAL(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object EQUAL(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object EQUAL(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object EQUAL(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object EQUAL(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object EQUAL(float left, float right) {
        return left * right;
    }

    public static Object EQUAL(double left, double right) {
        return left * right;
    }
    public static Object EQUAL(String left, String right) {
        return null;
    }


    public static Object NOT_EQUAL(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object NOT_EQUAL(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object NOT_EQUAL(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object NOT_EQUAL(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object NOT_EQUAL(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object NOT_EQUAL(float left, float right) {
        return left * right;
    }

    public static Object NOT_EQUAL(double left, double right) {
        return left * right;
    }
    public static Object NOT_EQUAL(String left, String right) {
        return null;
    }



    public static Object AND(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object AND(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object AND(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object AND(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object AND(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object AND(float left, float right) {
        return left * right;
    }

    public static Object AND(double left, double right) {
        return left * right;
    }
    public static Object AND(String left, String right) {
        return null;
    }



    public static Object OR(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object OR(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object OR(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object OR(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object OR(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object OR(float left, float right) {
        return left * right;
    }

    public static Object OR(double left, double right) {
        return left * right;
    }
    public static Object OR(String left, String right) {
        return null;
    }


    public static Object CONTAIN(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object CONTAIN(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object CONTAIN(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object CONTAIN(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object CONTAIN(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object CONTAIN(float left, float right) {
        return left * right;
    }

    public static Object CONTAIN(double left, double right) {
        return left * right;
    }

    public static Object CONTAIN(String left, String right) {
        return null;
    }


    public static Object START(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object START(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START(float left, float right) {
        return left * right;
    }

    public static Object START(double left, double right) {
        return left * right;
    }

    public static Object START(String left, String right) {
        return null;
    }

    public static Object START_WIDTHS_IGNORED_CASE(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object START_WIDTHS_IGNORED_CASE(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START_WIDTHS_IGNORED_CASE(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START_WIDTHS_IGNORED_CASE(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START_WIDTHS_IGNORED_CASE(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object START_WIDTHS_IGNORED_CASE(float left, float right) {
        return left * right;
    }

    public static Object START_WIDTHS_IGNORED_CASE(double left, double right) {
        return left * right;
    }

    public static Object START_WIDTHS_IGNORED_CASE(String left, String right) {
        return null;
    }


    public static Object END_WIDTHS(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object END_WIDTHS(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS(float left, float right) {
        return left * right;
    }

    public static Object END_WIDTHS(double left, double right) {
        return left * right;
    }

    public static Object END_WIDTHS(String left, String right) {
        return null;
    }


    public static Object END_WIDTHS_IGNORED_CASE(boolean left, boolean right) {
        return left && right ? 1 : 0;
    }

    public static Object END_WIDTHS_IGNORED_CASE(char left, char right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS_IGNORED_CASE(byte left, byte right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS_IGNORED_CASE(short left, short right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS_IGNORED_CASE(long left, long right) {
        return Math.multiplyExact(left, right);
    }

    public static Object END_WIDTHS_IGNORED_CASE(float left, float right) {
        return left * right;
    }

    public static Object END_WIDTHS_IGNORED_CASE(double left, double right) {
        return left * right;
    }

    public static Object END_WIDTHS_IGNORED_CASE(String left, String right) {
        return null;
    }
}
