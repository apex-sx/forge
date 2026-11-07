/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数组工具类
 *
 * @author potatoxf
 */
public interface GeneralArray {

    /**
     * 获取数组排列组合索引列表
     *
     * @param lengths 每个元素数组的索引数组
     * @return 返回数组排列组合索引列表
     */
    static int[][] combine(int[] lengths) {
        int len = lengths.length;
        int[] indexes = new int[len];
        List<int[]> results = new ArrayList<>();
        if (lengths[0] <= 0) throw new IllegalArgumentException();
        while (indexes[0] < lengths[0]) {
            results.add(Arrays.copyOf(indexes, len));
            for (int i = len - 1; i >= 0; ) {
                indexes[i] = indexes[i] + 1;
                if (lengths[i] <= 0) throw new IllegalArgumentException();
                if (indexes[i] < lengths[i]) break;
                if (i > 0) indexes[i] = 0;
                i--;
            }
        }
        return results.toArray(new int[0][]);
    }

}
