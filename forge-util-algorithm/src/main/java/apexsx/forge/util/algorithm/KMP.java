package apexsx.forge.util.algorithm;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

/**
 * KMP算法实现类，用于高效地在数组或列表中查找子序列。
 * <p>
 * KMP算法的核心思想是当匹配失败时，利用已经匹配的部分信息，
 * 避免从头开始匹配，从而提高匹配效率。
 * </p>
 * <p>
 * 该类通过设置输入序列和目标序列的访问器和长度提供器，
 * 以及元素比较器，实现对不同数据结构的通用匹配。
 * </p>
 *
 * @param <E> 元素类型
 * @author zhitron
 */
@SuppressWarnings("NumberEquality")
public final class KMP<E> {
    /**
     * 表示未找到匹配项时的返回值
     */
    public static final int NOT_FOUND = -1;
    /**
     * 输入序列的长度提供器
     */
    private IntSupplier inputLength;
    /**
     * 输入序列的元素访问器
     */
    private IntFunction<E> inputAccessor;
    /**
     * 模式序列的长度提供器
     */
    private IntSupplier targetLength;
    /**
     * 模式序列的元素访问器
     */
    private IntFunction<E> targetAccessor;
    /**
     * 元素比较器，默认使用Objects::equals
     */
    private BiPredicate<E, E> compare = Objects::equals;

    /**
     * 创建一个新的KMP实例
     *
     * @param <E> 元素类型
     * @return 新的KMP实例
     */
    public static <E> KMP<E> of() {
        return new KMP<>();
    }

    /**
     * 创建一个KMP实例，用于在给定的数组中查找目标子数组。
     *
     * @param input  要搜索的数组
     * @param target 要查找的目标子数组
     * @param <E>    数组元素的类型
     * @return 配置好的KMP实例
     */
    public static <E> KMP<E> of(E[] input, E[] target) {
        return KMP.<E>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i]);
    }

    /**
     * 创建一个KMP实例，用于在给定的列表中查找目标子列表。
     *
     * @param input  要搜索的列表
     * @param target 要查找的目标子列表
     * @param <E>    列表元素的类型
     * @return 配置好的KMP实例
     */
    public static <E> KMP<E> of(List<E> input, List<E> target) {
        return KMP.<E>of()
                .setInputLength(() -> input == null ? 0 : input.size())
                .setInputAccessor(input::get)
                .setTargetLength(() -> target == null ? 0 : target.size())
                .setTargetAccessor(target::get);
    }

    /**
     * 创建一个KMP实例，用于在给定的boolean数组中查找目标子数组。
     *
     * @param input  要搜索的boolean数组
     * @param target 要查找的目标boolean子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Boolean> of(boolean[] input, boolean[] target) {
        return KMP.<Boolean>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> ie == te);
    }

    /**
     * 创建一个KMP实例，用于在给定的char数组中查找目标子数组。
     *
     * @param input  要搜索的char数组
     * @param target 要查找的目标char子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Character> of(char[] input, char[] target) {
        return KMP.<Character>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> ie == te);
    }

    /**
     * 创建一个KMP实例，用于在给定的byte数组中查找目标子数组。
     *
     * @param input  要搜索的byte数组
     * @param target 要查找的目标byte子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Byte> of(byte[] input, byte[] target) {
        return KMP.<Byte>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> ie == te);
    }

    /**
     * 创建一个KMP实例，用于在给定的short数组中查找目标子数组。
     *
     * @param input  要搜索的short数组
     * @param target 要查找的目标short子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Short> of(short[] input, short[] target) {
        return KMP.<Short>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> ie == te);
    }

    /**
     * 创建一个KMP实例，用于在给定的int数组中查找目标子数组。
     *
     * @param input  要搜索的int数组
     * @param target 要查找的目标int子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Integer> of(int[] input, int[] target) {
        return KMP.<Integer>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> ie == te);
    }

    /**
     * 创建一个KMP实例，用于在给定的long数组中查找目标子数组。
     *
     * @param input  要搜索的long数组
     * @param target 要查找的目标long子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Long> of(long[] input, long[] target) {
        return KMP.<Long>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> ie == te);
    }

    /**
     * 创建一个KMP实例，用于在给定的float数组中查找目标子数组。
     *
     * @param input  要搜索的float数组
     * @param target 要查找的目标float子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Float> of(float[] input, float[] target) {
        return KMP.<Float>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> Float.compare(ie, te) == 0);
    }

    /**
     * 创建一个KMP实例，用于在给定的double数组中查找目标子数组。
     *
     * @param input  要搜索的double数组
     * @param target 要查找的目标double子数组
     * @return 配置好的KMP实例
     */
    public static KMP<Double> of(double[] input, double[] target) {
        return KMP.<Double>of()
                .setInputLength(() -> input == null ? 0 : input.length)
                .setInputAccessor((i) -> input[i])
                .setTargetLength(() -> target == null ? 0 : target.length)
                .setTargetAccessor((i) -> target[i])
                .setCompare((ie, te) -> Double.compare(ie, te) == 0);
    }

    /**
     * 生成KMP算法的next数组。
     * KMP算法是一种字符串匹配算法，next数组用于在匹配失败时确定模式串的滑动位置。
     *
     * @param length  模式串的长度
     * @param compare 用于比较模式串中字符的谓词
     * @return 返回生成的next数组，next数组的每个元素表示在匹配失败时模式串的滑动位置
     */
    public static int[] generateNext(int length, KMPComparable compare) {
        int[] next = new int[length];
        next[0] = -1; // 初始化next数组的第一个元素为-1
        int i = 0, j = -1;
        // 通过双指针法计算next数组
        while (i < length - 1) {
            // 如果j为-1或者当前字符匹配成功，则i和j同时向后移动，并更新next数组
            if (j == -1 || compare.test(i, j)) {
                i++;
                j++;
                next[i] = j;
            } else {
                // 如果字符匹配失败，则j回退到next[j]的位置
                j = next[j];
            }
        }
        return next;
    }

    /**
     * 私有构造函数，防止外部实例化
     */
    private KMP() {
    }

    /**
     * 获取输入序列的长度
     *
     * @return 输入序列的长度
     */
    public int getInputLength() {
        return inputLength.getAsInt();
    }

    /**
     * 获取目标序列的长度
     *
     * @return 目标序列的长度
     */
    public int getTargetLength() {
        return targetLength.getAsInt();
    }

    /**
     * 设置输入序列的长度提供器
     *
     * @param inputLength 长度提供器
     * @return 当前KMP实例
     */
    public KMP<E> setInputLength(IntSupplier inputLength) {
        this.inputLength = inputLength;
        return this;
    }

    /**
     * 设置输入序列的元素访问器
     *
     * @param inputAccessor 元素访问器
     * @return 当前KMP实例
     */
    public KMP<E> setInputAccessor(IntFunction<E> inputAccessor) {
        this.inputAccessor = inputAccessor;
        return this;
    }

    /**
     * 设置模式序列的长度提供器
     *
     * @param targetLength 长度提供器
     * @return 当前KMP实例
     */
    public KMP<E> setTargetLength(IntSupplier targetLength) {
        this.targetLength = targetLength;
        return this;
    }

    /**
     * 设置模式序列的元素访问器
     *
     * @param targetAccessor 元素访问器
     * @return 当前KMP实例
     */
    public KMP<E> setTargetAccessor(IntFunction<E> targetAccessor) {
        this.targetAccessor = targetAccessor;
        return this;
    }

    /**
     * 设置元素比较器
     *
     * @param compare 元素比较器
     * @return 当前KMP实例
     */
    public KMP<E> setCompare(BiPredicate<E, E> compare) {
        this.compare = compare;
        return this;
    }

    /**
     * 查找指定数组在当前列表中的第一次出现的位置。
     * 该函数使用KMP算法进行匹配，以提高查找效率。
     *
     * @param offset 起始查找位置
     * @return 如果找到指定数组，则返回其在当前列表中的起始索引；否则返回 -1
     */
    public int indexOf(int offset) {
        return this.indexOf(offset, inputLength.getAsInt());
    }

    /**
     * 在指定范围内查找目标序列在输入序列中第一次出现的位置。
     * 使用KMP算法进行高效匹配。
     *
     * @param startInclusive 起始查找位置（包含）
     * @param endExclusive   结束查找位置（不包含）
     * @return 如果找到目标序列，则返回其在输入序列中的起始索引；否则返回 -1
     */
    public int indexOf(int startInclusive, int endExclusive) {
        // 检查查找范围是否有效：起始位置不能大于等于结束位置
        if (startInclusive >= endExclusive) return NOT_FOUND;

        // 获取输入序列和目标序列的长度
        int inputLen = inputLength.getAsInt();
        int valuesLen = targetLength.getAsInt();

        // 检查序列是否有效：输入序列或目标序列长度小于等于0，或者目标序列长度大于输入序列长度
        if (inputLen <= 0 || valuesLen <= 0 || valuesLen > inputLen) return NOT_FOUND;

        // 检查查找范围是否有效：结束位置小于等于0，或者起始位置超出输入序列范围
        if (endExclusive <= 0 || startInclusive >= inputLen) return NOT_FOUND;

        // 调整查找范围到有效边界内
        if (startInclusive < 0) startInclusive = 0;
        if (endExclusive > inputLen) endExclusive = inputLen;

        // 检查在指定范围内是否有足够的字符进行匹配
        if (valuesLen > endExclusive - startInclusive) return NOT_FOUND;

        // 生成KMP算法的next数组，用于优化匹配过程
        int[] next = KMP.generateNext(valuesLen, (i, j) -> compare.test(targetAccessor.apply(i), targetAccessor.apply(j)));

        // 初始化指针，i用于遍历输入序列，j用于遍历目标序列
        int i = startInclusive, j = 0;

        // 执行KMP匹配算法
        while (i < endExclusive && j < valuesLen) {
            // 如果字符匹配或j为-1（表示重新开始匹配），则继续比较下一个字符
            if (j == -1 || compare.test(inputAccessor.apply(i), targetAccessor.apply(j))) {
                i++;
                j++;
            } else {
                // 如果字符不匹配，则根据next数组调整j的位置
                j = next[j];
            }
        }

        // 如果j等于目标序列的长度，说明找到了完整的匹配，返回起始索引
        if (j == valuesLen) {
            return i - j;
        } else {
            // 否则返回-1，表示未找到匹配
            return NOT_FOUND;
        }
    }

    /**
     * 查找指定数组在当前列表中的最后一次出现的位置。
     * 该函数使用修改版的KMP算法从末尾开始匹配，以提高查找效率。
     *
     * @param offset 起始查找位置
     * @return 如果找到指定数组，则返回其在当前列表中的起始索引；否则返回 -1
     */
    public int lastIndexOf(int offset) {
        return this.lastIndexOf(0, offset + 1);
    }

    /**
     * 在指定范围内查找目标序列在输入序列中最后一次出现的位置。
     * 使用修改版的KMP算法从末尾开始匹配，以提高查找效率。
     *
     * @param startInclusive 起始查找位置（包含）
     * @param endExclusive   结束查找位置（不包含）
     * @return 如果找到目标序列，则返回其在输入序列中的起始索引；否则返回 -1
     */
    public int lastIndexOf(int startInclusive, int endExclusive) {
        // 检查查找范围是否有效：起始位置不能大于等于结束位置
        if (startInclusive >= endExclusive) return NOT_FOUND;

        // 获取输入序列和目标序列的长度
        int inputLen = inputLength.getAsInt();
        int valuesLen = targetLength.getAsInt();

        // 检查序列是否有效：输入序列或目标序列长度小于等于0，或者目标序列长度大于输入序列长度
        if (inputLen <= 0 || valuesLen <= 0 || valuesLen > inputLen) return NOT_FOUND;

        // 检查查找范围是否有效：结束位置小于等于0，或者起始位置超出输入序列范围
        if (endExclusive <= 0 || startInclusive >= inputLen) return NOT_FOUND;

        // 调整查找范围到有效边界内
        if (startInclusive < 0) startInclusive = 0;
        if (endExclusive > inputLen) endExclusive = inputLen;

        // 检查在指定范围内是否有足够的字符进行匹配
        if (valuesLen > endExclusive - startInclusive) return NOT_FOUND;

        // 使用KMP算法生成next数组，用于优化匹配过程
        int[] next = KMP.generateNext(valuesLen, (i, j) -> compare.test(targetAccessor.apply(i), targetAccessor.apply(j)));

        // 从指定范围的末尾开始匹配，i为输入序列中的起始位置，j为目标序列的匹配位置
        int i = endExclusive - valuesLen;
        int j = 0;

        // 使用修改版的KMP算法进行匹配
        while (i >= startInclusive && j < valuesLen) {
            // 如果字符匹配或j为-1（表示重新开始匹配），则继续比较下一个字符
            if (j == -1 || compare.test(inputAccessor.apply(i + j), targetAccessor.apply(j))) {
                j++;
            } else {
                // 如果字符不匹配，则根据next数组调整i的位置，并更新j的值
                if (j == 0) {
                    i--;
                } else {
                    i -= (j - next[j]);
                    j = next[j];
                }
            }
        }

        // 判断是否找到匹配的数组
        if (j == valuesLen) {
            return i;
        } else {
            return NOT_FOUND;
        }
    }
}
