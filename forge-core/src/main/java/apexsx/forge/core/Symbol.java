/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */

package apexsx.forge.core;

import java.util.Objects;

/**
 * Symbol类表示一个符号，它遵循特定的格式规范。
 * 符号必须以字母开头，中间字符只能是字母、数字、下划线。
 *
 * @param name 符号的名称，必须符合规范格式
 * @author Terraqo
 */
public record Symbol(String name) implements Comparable<Symbol> {

    /**
     * Symbol记录的构造器，验证name参数是否符合规范的符号格式。
     *
     * @param name 符号名称，必须符合规范格式
     * @throws IllegalArgumentException 如果name不符合规范格式
     */
    public Symbol {
        // 验证name参数是否为规范符号格式
        if (!isCanonicalSymbol(name)) {
            throw new IllegalArgumentException("Symbol name must be a canonical symbol format: start with a letter, " + "end with a letter or digit, and contain only letters, digits or _ in between. Got: " + name);
        }
    }

    /**
     * 创建一个Symbol对象。
     *
     * @param name 符号的名称，必须符合规范格式
     * @return 一个新的Symbol对象
     */
    public static Symbol of(String name) {
        return new Symbol(name);
    }

    /**
     * 检查输入字符串是否为规范的符号格式。
     * <p>
     * 规范格式要求：
     * - 不为null且长度大于0
     * - 首字符必须是字母
     * - 中间字符只能是字母、数字、下划线
     *
     * @param input 待检查的字符串
     * @return 如果输入是规范的符号格式则返回true，否则返回false
     */
    public static boolean isCanonicalSymbol(String input) {
        if (input == null) {
            return false;
        }
        int length = input.length();
        if (length == 0) {
            return false;
        }
        char c;

        // 验证首字符：必须是字母
        c = input.charAt(0);
        if (!Character.isLetter(c) && c != '_') {
            return false;
        }

        // 验证尾字符：必须是字母或数字
        c = input.charAt(length - 1);
        if (!Character.isLetterOrDigit(c)) {
            return false;
        }

        // 验证中间字符：字母、数字、下划线、点号、连字符、井号
        for (int i = 1; i < length - 1; i++) {
            c = input.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }

        return true;
    }

    /**
     * 实现Comparable接口的compareTo方法，用于比较两个Symbol对象的顺序。
     * 比较基于Symbol的name字段的字典序。
     *
     * @param o 要与当前对象比较的Symbol对象
     * @return 如果当前Symbol的name在字典序上小于、等于或大于参数Symbol的name，
     * 则分别返回负整数、0或正整数
     */
    @Override
    public int compareTo(Symbol o) {
        return name.compareTo(o.name);
    }

    /**
     * 重写equals方法，用于比较两个Symbol对象是否相等。
     * 只有当两个Symbol对象的name值相等时，这两个对象才相等。
     *
     * @param o 要与当前对象比较的对象
     * @return 如果两个Symbol对象的name相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof String other) {
            return Objects.equals(name, other);
        } else if (o instanceof Symbol(String name1)) {
            return Objects.equals(name, name1);
        }
        return false;
    }

    /**
     * 重写hashCode方法，基于Symbol的name字段生成哈希码。
     *
     * @return name字段的哈希码值
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    /**
     * 重写toString方法，返回Symbol的字符串表示。
     * 该方法返回Symbol的name字段的值。
     *
     * @return Symbol的name字段值
     */
    @Override
    public String toString() {
        return name;
    }
}
