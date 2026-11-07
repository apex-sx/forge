/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.converter.object;

import terraqo.cheetah.converter.CharSequenceTypeConverter;

import java.util.Locale;

/**
 * CharSequence到Locale类型转换器
 * <p>
 * 用于将字符序列转换为Locale类型。支持多种格式的locale字符串，
 * 包括用下划线、连字符、空格分隔的语言、国家和地区代码。
 * 支持带有变体标识符（以#开头）的locale格式。
 * </p>
 *
 * @author Terraqo
 */
public class CharSequenceToLocaleTypeConverter extends CharSequenceTypeConverter<CharSequence, Locale> {
    /**
     * 单例实例
     */
    public static final CharSequenceToLocaleTypeConverter INSTANCE = new CharSequenceToLocaleTypeConverter();

    /**
     * 构造函数，初始化目标类型为Locale.class
     */
    protected CharSequenceToLocaleTypeConverter() {
        super(Locale.class);
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
        return StringToLocaleTypeConverter.INSTANCE.convertsUnchecked(input);
    }
}
