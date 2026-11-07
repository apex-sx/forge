/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */

package apexsx.forge.core.cc;

import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

/**
 * 索引范围类，用于表示一个左闭右开的整数区间[startInclusive, endExclusive)
 *
 * @param startInclusive 起始索引（包含）
 * @param endExclusive   结束索引（不包含）
 * @author Terraqo
 */
public record Bounds(int startInclusive, int endExclusive) {

    /**
     * 根据偏移量和数量创建一个索引范围
     *
     * @param offset 偏移量，作为范围的起始位置
     * @param count  数量，表示范围的长度
     * @return 返回一个Bounds实例，表示从offset开始，长度为count的范围
     */
    public static Bounds withOffset(int offset, int count) {
        return new Bounds(offset, offset + count);
    }

    /**
     * 根据起始索引和数量创建一个索引范围
     *
     * @param offset 起始索引，作为范围的起始位置
     * @param count  数量，表示范围的长度
     * @return 返回一个Bounds实例，表示从offset开始，长度为count的范围
     */
    public static Bounds withIndex(int offset, int count) {
        return new Bounds(offset, offset + count);
    }

    /**
     * Returns the length of the given array in a null-safe manner.
     *
     * @param array an array or null
     * @return the array length, or 0 if the given array is null.
     */
    public static int length(byte[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * Returns the length of the given array in a null-safe manner.
     *
     * @param array an array or null
     * @return the array length, or 0 if the given array is null.
     */
    public static int length(char[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * Returns the length of the given CharSequence in a null-safe manner.
     *
     * @param csq a CharSequence or null
     * @return the CharSequence length, or 0 if the given CharSequence is null.
     */
    public static int length(CharSequence csq) {
        return csq == null ? 0 : csq.length();
    }

    /**
     * Returns the length of the given array in a null-safe manner.
     *
     * @param array an array or null
     * @return the array length, or 0 if the given array is null.
     */
    public static int length(Object[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * 根据给定的索引和大小，计算并返回一个有效的索引值。
     * 该函数处理了索引超出范围的情况，确保返回的索引在有效范围内。
     *
     * @param size  数组或列表的大小，用于确定索引的有效范围。
     * @param index 原始索引值，可以是正数、负数或超出范围的值。
     * @return 返回一个经过调整后的有效索引值，确保在 [0, size-1] 范围内。
     */
    public static int idx(int size, int index) {
        // 如果大小小于等于0，直接返回0，因为无法计算有效索引
        if (size < 0) {
            return -1;
        }
        // 处理负索引：如果索引为负数，则将其转换为正数索引
        while (index < 0) {
            index += size;
        }
        // 确保索引不小于0
        while (index >= size) {
            index -= size;
        }
        return index;
    }

    /**
     * 根据给定的起始和结束索引，返回一个包含有效索引范围的数组。
     * 该函数会确保起始和结束索引在有效范围内，并检查起始索引是否小于等于结束索引。
     *
     * @param size  数组或列表的总大小，用于确定索引的有效范围。
     * @param start 起始索引，可以是正数或负数（负数表示从末尾开始计算）。
     * @param end   结束索引，可以是正数或负数（负数表示从末尾开始计算）。
     * @return 包含有效起始和结束索引的数组，格式为 [start, end]。
     * @throws IllegalArgumentException 如果起始索引大于结束索引，则抛出此异常。
     */
    public static int[] idx(int size, int start, int end) {
        // 确保起始和结束索引在有效范围内
        start = idx(size, start);
        end = idx(size, end);

        // 检查起始索引是否小于等于结束索引
        if (start > end) {
            throw new IllegalArgumentException("The start or end is illegal");
        }

        // 返回包含有效索引范围的数组
        return new int[]{start, end};
    }

    /**
     * 验证索引是否在有效范围内
     *
     * @param index 待验证的索引值
     * @param size  总大小或最大边界值
     * @return 如果索引在有效范围内返回true，否则返回false
     */
    public static boolean isValidIndex(int index, int size) {
        return Bounds.isValidIndex(index, size, true);
    }

    /**
     * 验证索引是否在有效范围内
     *
     * @param index     待验证的索引值
     * @param size      总大小或最大边界值
     * @param exclusive 是否排除最后一个位置，true表示排除，false表示包含
     * @return 如果索引在有效范围内返回true，否则返回false
     */
    public static boolean isValidIndex(int index, int size, boolean exclusive) {
        return exclusive ? (index >= 0 && index < size) : (index >= 0 && index <= size);
    }

    /**
     * 验证索引范围是否有效
     *
     * @param startInclusive 起始索引（包含）
     * @param endExclusive   结束索引（不包含）
     * @param length         字符序列长度
     * @return 如果索引范围有效返回true，否则返回false
     */
    public static boolean isValidRange(int startInclusive, int endExclusive, int length) {
        // 起始索引必须在有效范围内[0, length)
        if (startInclusive < 0 || startInclusive > length) {
            return false;
        }
        // 结束索引必须大于0且起始索引不能超过长度
        if (endExclusive < 0 || endExclusive > length) {
            return false;
        }
        // 起始索引必须小于结束索引
        return startInclusive <= endExclusive;
    }

    /**
     * 验证输入和目标的索引范围是否都有效
     *
     * @param inputStartInclusive  输入起始索引（包含）
     * @param inputEndExclusive    输入结束索引（不包含）
     * @param inputLength          输入字符序列长度
     * @param targetStartInclusive 目标起始索引（包含）
     * @param targetEndExclusive   目标结束索引（不包含）
     * @param targetLength         目标字符序列长度
     * @return 如果两个索引范围都有效返回true，否则返回false
     */
    public static boolean isValidRange(int inputStartInclusive, int inputEndExclusive, int inputLength, int targetStartInclusive, int targetEndExclusive, int targetLength) {
        // 验证输入索引范围是否有效
        if (!isValidRange(inputStartInclusive, inputEndExclusive, inputLength)) {
            return false;
        }
        // 验证目标索引范围是否有效
        if (!isValidRange(targetStartInclusive, targetEndExclusive, targetLength)) {
            return false;
        }
        // 输入范围长度必须大于等于目标范围长度
        return inputEndExclusive - inputStartInclusive >= targetEndExclusive - targetStartInclusive;
    }

    /**
     * 验证偏移量和计数是否有效
     *
     * @param offset 偏移量
     * @param count  计数
     * @param length 字符序列长度
     * @return 如果偏移量和计数有效返回true，否则返回false
     */
    public static boolean isValidOffset(int offset, int count, int length) {
        // 偏移量必须非负，计数和长度必须大于0
        if (offset < 0 || count < 0 || length < 0) {
            return false;
        }
        // 偏移量加上计数不能超过总长度
        return offset + count <= length;
    }

    /**
     * 验证输入和目标的偏移量和计数是否都有效
     *
     * @param inputOffset  输入偏移量
     * @param inputCount   输入计数
     * @param inputLength  输入字符序列长度
     * @param targetOffset 目标偏移量
     * @param targetCount  目标计数
     * @param targetLength 目标字符序列长度
     * @return 如果两个偏移量和计数都有效返回true，否则返回false
     */
    public static boolean isValidOffset(int inputOffset, int inputCount, int inputLength, int targetOffset, int targetCount, int targetLength) {
        // 验证输入偏移量和计数是否有效
        if (!isValidOffset(inputOffset, inputCount, inputLength)) {
            return false;
        }
        // 验证目标偏移量和计数是否有效
        if (!isValidOffset(targetOffset, targetCount, targetLength)) {
            return false;
        }
        // 输入范围的结束位置必须大于等于目标范围的结束位置
        return inputOffset + inputCount >= targetOffset + targetCount;
    }

    /**
     * 检查索引是否合法。
     *
     * @param index 要检查的索引
     * @param size  集合或数组的大小
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public static void validIndex(int index, int size) {
        // 检查索引是否在有效范围内
        if (!Bounds.isValidIndex(index, size)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * 检查索引是否合法。
     *
     * @param index     要检查的索引
     * @param size      集合或数组的大小
     * @param exclusive 是否排除最后一个索引
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public static void validIndex(int index, int size, boolean exclusive) {
        if (!Bounds.isValidIndex(index, size, exclusive)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * 检查索引范围是否有效，如果无效则抛出异常
     *
     * @param startInclusive 起始索引（包含）
     * @param endExclusive   结束索引（不包含）
     * @param length         字符序列长度
     * @throws IndexOutOfBoundsException 如果索引范围无效
     */
    public static void validRange(int startInclusive, int endExclusive, int length) {
        if (!Bounds.isValidRange(startInclusive, endExclusive, length)) {
            throw new IndexOutOfBoundsException(
                    "startInclusive: " + startInclusive + ", endExclusive: " + endExclusive + ", length: " + length);
        }
    }

    /**
     * 检查输入和目标的索引范围是否都有效，如果无效则抛出异常
     *
     * @param inputStartInclusive  输入起始索引（包含）
     * @param inputEndExclusive    输入结束索引（不包含）
     * @param inputLength          输入字符序列长度
     * @param targetStartInclusive 目标起始索引（包含）
     * @param targetEndExclusive   目标结束索引（不包含）
     * @param targetLength         目标字符序列长度
     * @throws IndexOutOfBoundsException 如果任一索引范围无效
     */
    public static void validRange(int inputStartInclusive, int inputEndExclusive, int inputLength, int targetStartInclusive, int targetEndExclusive, int targetLength) {
        if (!Bounds.isValidRange(inputStartInclusive, inputEndExclusive, inputLength,
                targetStartInclusive, targetEndExclusive, targetLength)) {
            throw new IndexOutOfBoundsException(
                    "inputStartInclusive: " + inputStartInclusive + ", inputEndExclusive: " + inputEndExclusive + ", inputLength: " + inputLength +
                            ", targetStartInclusive: " + targetStartInclusive + ", targetEndExclusive: " + targetEndExclusive + ", targetLength: " + targetLength);
        }
    }

    /**
     * 检查偏移量和计数是否有效，如果无效则抛出异常
     *
     * @param offset 偏移量
     * @param count  计数
     * @param length 字符序列长度
     * @throws IndexOutOfBoundsException 如果偏移量和计数无效
     */
    public static void validOffset(int offset, int count, int length) {
        if (!Bounds.isValidOffset(offset, count, length)) {
            throw new IndexOutOfBoundsException("offset: " + offset + ", count: " + count + ", length: " + length);
        }
    }

    /**
     * 检查输入和目标的偏移量和计数是否都有效，如果无效则抛出异常
     *
     * @param inputOffset  输入偏移量
     * @param inputCount   输入计数
     * @param inputLength  输入字符序列长度
     * @param targetOffset 目标偏移量
     * @param targetCount  目标计数
     * @param targetLength 目标字符序列长度
     * @throws IndexOutOfBoundsException 如果任一偏移量和计数无效
     */
    public static void validOffset(int inputOffset, int inputCount, int inputLength, int targetOffset, int targetCount, int targetLength) {
        if (!Bounds.isValidOffset(inputOffset, inputCount, inputLength, targetOffset, targetCount, targetLength)) {
            throw new IndexOutOfBoundsException(
                    "inputOffset: " + inputOffset + ", inputCount: " + inputCount + ", inputLength: " + inputLength +
                            ", targetOffset: " + targetOffset + ", targetCount: " + targetCount + ", targetLength: " + targetLength);
        }
    }

    /**
     * 获取结束索引（不包含）
     *
     * @return 结束索引值
     */
    public int endInclusive() {
        return endExclusive - 1;
    }

    /**
     * 计算范围的长度
     *
     * @return 范围的长度，如果范围无效则返回0
     */
    public int length() {
        // 计算范围长度，确保不为负数
        return Math.max(endExclusive - startInclusive, 0);
    }

    /**
     * 判断范围是否为空
     *
     * @return 当起始位置大于等于结束位置时返回true，表示范围为空；否则返回false
     */
    public boolean isEmpty() {
        return startInclusive >= endExclusive;
    }

    /**
     * 创建一个从起始值（包含）到结束值（不包含）的整数流
     *
     * @return 返回一个IntStream对象，包含从startInclusive到endExclusive-1的所有整数
     */
    public IntStream each() {
        return IntStream.range(startInclusive, endExclusive);
    }

    /**
     * 对指定范围内的每个整数执行给定的操作
     *
     * @param consumer 要对每个整数执行的操作，不能为null
     */
    public void each(IntConsumer consumer) {
        // 遍历从startInclusive到endExclusive-1的每个整数
        for (int i = startInclusive; i < endExclusive; i++) {
            consumer.accept(i);
        }
    }

    /**
     * 对指定范围内的每个整数执行给定的操作
     *
     * @param skipper 要对每个整数应用的跳步操作，不能为null
     */
    public void each(IntUnaryOperator skipper) {
        // 遍历从startInclusive到endExclusive-1的每个整数
        for (int i = startInclusive; i < endExclusive; ) {
            i += skipper.applyAsInt(i);
        }
    }

    /**
     * 对指定范围内的每个整数执行给定的操作
     *
     * @param initialIndex 初始索引值，用于传递给skipper操作
     * @param skipper      跳步操作函数，接受当前整数和索引值，返回需要跳过的步长
     */
    public void each(int initialIndex, IntBinaryOperator skipper) {
        // 遍历从startInclusive到endExclusive-1的每个整数
        for (int current = startInclusive, index = initialIndex; current < endExclusive; ) {
            int skip = skipper.applyAsInt(current, index);
            index += skip;
            current += skip;
        }
    }

    /**
     * 返回索引范围的字符串表示形式
     * 格式为：[startInclusive , endExclusive]
     *
     * @return 索引范围的字符串表示
     */
    @Override
    public String toString() {
        return "[" + startInclusive + " , " + endExclusive + "]";
    }

}
