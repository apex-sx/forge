/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

/**
 * Token类型枚举，用于标识解析器中不同类型的标记
 *
 * @author Terraqo
 */
public enum CommonTokenType {
    /**
     * 文件结束符 - 标识输入流的结束
     */
    EOF,

    /**
     * 空白字符 - 包括空格、制表符、换行符等空白符号
     */
    WHITESPACE,

    /**
     * 符号 - 特殊字符或标点符号，如括号、逗号、分号等
     */
    SYMBOL,

    /**
     * 字符串 - 用引号包围的文本内容
     */
    STRING,

    /**
     * 文本 - 普通的非引号包围的文本内容
     */
    TEXT,
}