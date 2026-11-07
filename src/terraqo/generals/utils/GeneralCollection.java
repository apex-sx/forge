/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public interface GeneralCollection {
    /**
     * 复制并强转有效元素到列表中
     *
     * @param input            输入集合
     * @param containerCreator 容器创建器
     * @return 返回新的列表
     */
    static <OV, NV> List<NV> copyElementAndCastToList(OV[] input, IntFunction<List<NV>> containerCreator) {
        return GeneralCollection.copyElementToList(input, containerCreator, ov -> (NV) ov);
    }

    /**
     * 复制并强转有效元素到列表中
     *
     * @param input            输入集合
     * @param containerCreator 容器创建器
     * @return 返回新的列表
     */
    static <OV, NV> List<NV> copyElementAndCastToList(Collection<OV> input, IntFunction<List<NV>> containerCreator) {
        return GeneralCollection.copyElementToList(input, containerCreator, ov -> (NV) ov);
    }

    /**
     * 复制有效元素到列表中
     *
     * @param input            输入集合
     * @param containerCreator 容器创建器
     * @param elementConverter 元素转换器
     * @return 返回新的列表
     */
    static <OV, NV> List<NV> copyElementToList(OV[] input, IntFunction<List<NV>> containerCreator, Function<OV, NV> elementConverter) {
        List<NV> result = containerCreator.apply(input.length);
        for (OV ov : input) {
            NV nv = elementConverter.apply(ov);
            if (nv != null) {
                result.add(nv);
            }
        }
        return result;
    }

    /**
     * 复制有效元素到列表中
     *
     * @param input            输入集合
     * @param containerCreator 容器创建器
     * @param elementConverter 元素转换器
     * @return 返回新的列表
     */
    static <OV, NV> List<NV> copyElementToList(Collection<OV> input, IntFunction<List<NV>> containerCreator, Function<OV, NV> elementConverter) {
        List<NV> result = containerCreator.apply(input.size());
        for (OV ov : input) {
            NV nv = elementConverter.apply(ov);
            if (nv != null) {
                result.add(nv);
            }
        }
        return result;
    }
}
