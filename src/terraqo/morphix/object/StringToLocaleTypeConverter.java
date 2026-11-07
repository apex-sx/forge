/*
 * Copyright (c) 2026.
 */

package terraqo.morphix.object;

import terraqo.morphix.CharSequenceTypeConverter;

import java.util.Locale;

/**
 * String到Locale类型转换器
 * <p>
 * 用于将字符序列转换为Locale类型。支持多种格式的locale字符串，
 * 包括用下划线、连字符、空格分隔的语言、国家和地区代码。
 * 支持带有变体标识符（以#开头）的locale格式。
 * </p>
 *
 * @author Terraqo
 */
public class StringToLocaleTypeConverter extends CharSequenceTypeConverter<String, Locale> {
    /**
     * 单例实例
     */
    public static final StringToLocaleTypeConverter INSTANCE = new StringToLocaleTypeConverter();

    /**
     * 构造函数，初始化目标类型为Locale.class
     */
    protected StringToLocaleTypeConverter() {
        super(String.class, Locale.class);
    }

    /**
     * 将给定的字符序列解析为Locale对象
     * <p>
     * 支持的格式包括：
     * <ul>
     *   <li>语言代码：如 en
     *   <li>语言和国家代码：如 en_US, en-US, en US
     *   <li>带变体的完整格式：如 en_US_WIN, en_US#WIN
     * </ul>
     *
     * @param input 源字符序列，表示locale信息
     * @return 相应的Locale实例，如果输入为空则返回null
     * @throws IllegalArgumentException 当输入包含非法字符时抛出
     */
    @Override
    protected Locale convertsUncheckedString(String input) throws Throwable {
        if (input == null || input.isEmpty()) {
            return null;
        }

        char[] inputChars = input.toCharArray();

        // 验证字符有效性并统一替换分隔符
        for (int i = 0; i < inputChars.length; i++) {
            char c = inputChars[i];
            if (c == '_' || c == '#') {
                continue;
            }
            if (c == ' ' || c == '-') {
                inputChars[i] = '_';
            } else if (!Character.isLetterOrDigit(c)) {
                throw new IllegalArgumentException(
                        "Invalid character '" + c + "' at index " + i +
                                ". The locale string can only contain letters, digits, spaces, '-', '_', or '#'. Input: \"" + input + "\""
                );
            }
        }

        // 使用标准split代替手动分割
        String[] segments = new String(inputChars).split("_", -1);

        String language = segments.length > 0 ? segments[0] : "";
        String country = segments.length > 1 ? segments[1] : "";
        String variant = "";

        // 处理variant部分（包括以#开头的情况）
        if (segments.length > 2) {
            StringBuilder variantBuilder = new StringBuilder();
            for (int i = 2; i < segments.length; i++) {
                if (i > 2) variantBuilder.append('_');
                variantBuilder.append(segments[i]);
            }
            variant = variantBuilder.toString();
        }

        // 特殊情况处理：country以#开头视为variant的一部分
        if (!country.isEmpty() && country.charAt(0) == '#') {
            if (!variant.isEmpty()) {
                variant = country + "_" + variant;
            } else {
                variant = country;
            }
            country = "";
        }

        // 构造Locale对象
        return Locale.of(language, country, variant);
    }
}
