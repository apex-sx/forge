/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.chameleon;

/**
 * ChameleonException 是类型转换过程中发生的异常。
 * 当类型转换失败或遇到不支持的类型转换时，将抛出此异常。
 *
 * @author Terraqo
 */
public class ChameleonException extends RuntimeException {
    /**
     * 构造一个没有详细消息的新 ChameleonException 实例。
     */
    public ChameleonException() {
    }

    /**
     * 构造一个带有指定详细消息的新 ChameleonException 实例。
     *
     * @param message 异常的详细信息
     */
    public ChameleonException(String message) {
        super(message);
    }

    /**
     * 构造一个带有指定详细消息和原因的新 ChameleonException 实例。
     *
     * @param message 异常的详细信息
     * @param cause   异常的原因
     */
    public ChameleonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带有指定原因的新 ChameleonException 实例。
     *
     * @param cause 异常的原因
     */
    public ChameleonException(Throwable cause) {
        super(cause);
    }
}
