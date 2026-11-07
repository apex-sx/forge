/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.coder;

import com.github.potatoxf.generals.symbolic.Decorated;

import java.util.Base64;

/**
 * 支持编码
 *
 * @author potatoxf
 */
public interface SupportCoderWithBase64 extends Decorated {
    /**
     * 用base64加密
     *
     * @param input 数据
     * @return 返回值
     */
    default String encodeWithBase64(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    /**
     * 用base64解密
     *
     * @param input 数据
     * @return 返回值
     */
    default byte[] decodeWithBase64(String input) {
        return Base64.getDecoder().decode(input);
    }
}
