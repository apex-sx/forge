/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */
package io.github.terraqo.foundation.text;

/**
 * 一个不可变对象，表示解析文本中的位置。
 *
 * @param offset 绝对字符索引，从 0 开始。
 * @param line   行号，从 1 开始。
 * @param column 列号，从 1 开始。
 * @author Terraqo
 */
public record TextLocation(int offset, int line, int column) {

    /**
     * 返回此文本位置的字符串表示形式，格式为 "行号:列号"。
     *
     * @return 此文本位置的字符串表示形式。
     */
    @Override
    public String toString() {
        return line + ":" + column;
    }

    /**
     * 基于绝对字符偏移量返回此文本位置的哈希码值。
     *
     * @return 此文本位置的哈希码值。
     */
    @Override
    public int hashCode() {
        return offset;
    }

    /**
     * 比较指定对象与此文本位置是否相等。
     * 如果给定对象也是 {@code TextLocation} 并且具有相同的偏移量、行号和列号，则返回 {@code true}。
     *
     * @param obj 要与此文本位置进行相等性比较的对象。
     * @return 如果指定对象等于此文本位置，则返回 {@code true}，否则返回 {@code false}。
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TextLocation other = (TextLocation) obj;
        return offset == other.offset && column == other.column && line == other.line;
    }

}