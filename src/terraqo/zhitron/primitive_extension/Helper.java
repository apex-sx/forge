package com.github.zhitron.primitive_extension;


import com.github.zhitron.lambda.predicate.TwicePredicateInt;


/**
 * @author zhitron
 */
public final class Helper {

    /**
     * 根据给定的索引和大小，计算并返回一个有效的索引值。
     * 该函数处理了索引超出范围的情况，确保返回的索引在有效范围内。
     *
     * @param size      数组或列表的大小，用于确定索引的有效范围。
     * @param index     原始索引值，可以是正数、负数或超出范围的值。
     * @param exclusive 是否排除结束索引
     * @return 返回一个经过调整后的有效索引值，如果exclusive=false确保在 [0, size-1] 范围内，如果exclusive=true确保在 [0, size] 范围内。
     */
    public static int idx(int size, int index, boolean exclusive) {
        // 如果大小小于等于0，直接返回0，因为无法计算有效索引
        if (size <= 0) return 0;

        // 处理负索引：如果索引为负数，则将其转换为正数索引
        if (index < 0) index += size;
        // 确保索引不小于0
        if (index < 0) index = 0;
        else {
            if (index >= size) index = size - (exclusive ? 0 : 1);
        }
        return index;
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
        return Helper.idx(size, index, false);
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
        start = Helper.idx(size, start, false);
        end = Helper.idx(size, end, true);

        // 检查起始索引是否小于等于结束索引
        if (start > end) {
            throw new IllegalArgumentException("The start or end is illegal");
        }

        // 返回包含有效索引范围的数组
        return new int[]{start, end};
    }

    /**
     * 检查索引是否合法。
     *
     * @param index       要检查的索引
     * @param excludeLast 是否排除最后一个索引
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public static void idxVerify(int size, int index, boolean excludeLast) {
        if (index < 0 || index > size - (excludeLast ? 0 : 1)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * 生成KMP算法的next数组。
     * KMP算法是一种字符串匹配算法，next数组用于在匹配失败时确定模式串的滑动位置。
     *
     * @param length  模式串的长度
     * @param compare 用于比较模式串中字符的谓词
     * @return 返回生成的next数组，next数组的每个元素表示在匹配失败时模式串的滑动位置
     */
    public static int[] kmpNextArray(int length, TwicePredicateInt compare) {
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
}
