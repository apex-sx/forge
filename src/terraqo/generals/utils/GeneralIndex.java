/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

/**
 * @author potatoxf
 */
public interface GeneralIndex {

    /**
     * 合法化索引，索引必须在{@code 0<=index<len}中
     *
     * @param len   总长度
     * @param index 索引
     * @return int[]{startIndex,endIndex}
     */
    static int idx(int len, int index) {
        while (index < 0) index += len;
        while (index >= len) index -= len;
        return index;
    }

    /**
     * 合法化索引
     *
     * @param len               总长度
     * @param startIndex        开始索引
     * @param endIndex          结束索引
     * @param isExcludeEndIndex 是否包括结束索引
     * @return int[]{startIndex,endIndex}
     */
    static int[] idx(int len, int startIndex, int endIndex, boolean isExcludeEndIndex) {
        startIndex = GeneralIndex.idx(len, startIndex);
        endIndex = GeneralIndex.idx(len, isExcludeEndIndex ? endIndex - 1 : endIndex);
        if (endIndex < startIndex) {
            startIndex ^= endIndex;
            endIndex ^= startIndex;
            startIndex ^= endIndex;
        }
        return new int[]{startIndex, isExcludeEndIndex ? endIndex + 1 : endIndex};
    }
}
