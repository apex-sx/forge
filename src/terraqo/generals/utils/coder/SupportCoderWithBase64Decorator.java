/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.coder;

import com.github.potatoxf.generals.symbolic.Decorator;

/**
 * 支持编码
 *
 * @author potatoxf
 */
public interface SupportCoderWithBase64Decorator extends SupportCoderWithBase64, Decorator<SupportCoderWithBase64> {

    /**
     * 用base64加密
     *
     * @param input 数据
     * @return 返回值
     */
    @Override
    default String encodeWithBase64(byte[] input) {
        return decorated().encodeWithBase64(input);
    }

    /**
     * 用base64解密
     *
     * @param input 数据
     * @return 返回值
     */
    @Override
    default byte[] decodeWithBase64(String input) {
        return decorated().decodeWithBase64(input);
    }
}
