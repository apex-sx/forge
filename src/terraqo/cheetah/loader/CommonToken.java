/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

/**
 * 表示词法分析器生成的一个标记（token）
 *
 * @param commonTokenType 标记的类型
 * @param value           标记的值
 * @param line            标记所在的行号（从1开始）
 * @param column          标记所在的列号（从1开始）
 * @author Terraqo
 */
public record CommonToken(CommonTokenType commonTokenType, String value, int line, int column) {

    /**
     * 生成此 token 的字符串表示形式
     *
     * @return 格式为 "CommonToken(type, 'value', line:column)" 的字符串
     */
    @Override
    public String toString() {
        // 返回一个格式化的字符串表示这个 token，包括类型、值和位置信息
        return String.format("CommonToken(%s, '%s', %d:%d)", commonTokenType, value, line, column);
    }
}