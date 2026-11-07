/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */
package io.github.terraqo.foundation.text;

/**
 * 文本解析异常。
 * <p>
 * 当在文本解析过程中发生错误时抛出此异常。
 * 该异常不仅包含错误信息，还包含发生错误的具体位置（{@link TextLocation}）。
 *
 * @author Terraqo
 */
public class TextParseException extends RuntimeException {

    /**
     * 发生解析错误的位置。
     */
    private final TextLocation textLocation;

    /**
     * 使用指定的详细消息和错误位置构造一个新的 {@code TextParseException}。
     *
     * @param message      详细错误消息
     * @param textLocation 发生错误的位置
     */
    public TextParseException(String message, TextLocation textLocation) {
        super(message + " at " + textLocation);
        this.textLocation = textLocation;
    }

    /**
     * 返回发生错误的位置。
     *
     * @return 错误位置
     */
    public TextLocation getTextLocation() {
        return textLocation;
    }

}
