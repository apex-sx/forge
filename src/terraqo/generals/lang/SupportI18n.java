/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;


import com.github.potatoxf.generals.utils.GeneralException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支持国家化语言
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public final class SupportI18n implements Internationalization {
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private static final ReferenceThreadLocal<Locale> CURRENT = ReferenceThreadLocal.softWithInitial(Locale::getDefault);
    private static final Pattern PATTERN_PREFIX = Pattern.compile("\\s*\\[[\\s\\w.$\\-/\\\\]+]*\\s*");
    private final String baseName, key;
    private final Supplier<String> content;
    private final Object[] args;

    public SupportI18n(String baseName, String key, String content, Object[] args) {
        this(baseName, key, () -> content, args);
    }

    public SupportI18n(String baseName, String key, Supplier<String> content, Object[] args) {
        this.baseName = baseName;
        this.key = key;
        this.content = content;
        this.args = args == null ? EMPTY_OBJECT_ARRAY : args;
    }

    /**
     * 当获取前{@link Locale}
     *
     * @return {@link Locale}
     */
    public static Locale current() {
        return CURRENT.get();
    }

    /**
     * 设置当前{@link Locale}
     *
     * @param locale {@link Locale}
     * @return 原来的{@link Locale}
     */
    public static Locale current(Locale locale) {
        Locale old = CURRENT.get();
        CURRENT.set(locale);
        return old;
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param throwable 异常信息
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofThrowable(Throwable throwable) {
        return SupportI18n.ofThrowable(throwable, null);
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param throwable 异常信息
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofThrowable(Throwable throwable, Object[] args) {
        SupportI18n supportI18n = SupportI18n.ofExpression(throwable.getMessage(), args);
        GeneralException.initExceptionMessage(throwable, supportI18n.content.get());
        return supportI18n;
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param expression 表达式
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofExpression(String expression, Object... args) {
        return SupportI18n.ofExpression(null, expression, args);
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param expression 表达式
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofExpression(String baseName, String expression, Object... args) {
        String name = null, key = null, content;
        if (expression != null && !expression.isEmpty()) {
            Matcher matcher = PATTERN_PREFIX.matcher(expression);
            List<String> list = new ArrayList<>();
            int last = 0;
            while (matcher.find()) {
                String gap = expression.substring(last, matcher.start()).trim();
                if (!gap.isEmpty()) break;
                last = matcher.end();
                String string = matcher.group().trim();
                string = string.substring(1, string.length() - 1).replaceAll("\\s", "");
                list.add(string);
            }
            if (baseName != null) {
                name = baseName;
            } else if (!list.isEmpty()) {
                name = list.remove(0);
            } else {
                name = Internationalization.MESSAGE_BASE_NAME;
            }
            key = String.join(".", list)
                    .replaceAll("\\.\\.", ".");
            content = expression.substring(last);
        } else {
            content = expression;
        }
        return new SupportI18n(name, key, () -> content, args);
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param baseName 国际化文件路径
     * @param key      国际化键值
     * @param content  默认内容
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofExpression(String baseName, String key, String content, Object... args) {
        return new SupportI18n(baseName, key, content, args);
    }

    /**
     * 构造枚举支持国家化语言
     *
     * @param e 枚举
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofEnumeration(Enum<?> e, String context) {
        return SupportI18n.ofEnumeration(e, context, null);
    }

    /**
     * 构造枚举支持国家化语言
     *
     * @param e 枚举
     * @return {@link SupportI18n}
     */
    public static SupportI18n ofEnumeration(Enum<?> e, String context, Object[] args) {
        return new SupportI18n(e.getClass().getSimpleName(), e.name(), context, args);
    }

    /**
     * 默认{@link Locale}
     *
     * @return 默认{@link Locale}
     */
    @Override
    public Locale locale() {
        return SupportI18n.current();
    }

    /**
     * 国际化基础名称
     *
     * @return 国际化基础名称
     */
    @Override
    public String baseName() {
        return baseName;
    }

    /**
     * 国际化键
     *
     * @return 国际化键
     */
    @Override
    public String key() {
        return key;
    }

    /**
     * 国际化默认内容
     *
     * @return 国际化默认内容
     */
    @Override
    public String content() {
        return content.get();
    }

    /**
     * 获取参数
     *
     * @return 返回参数
     */
    @Override
    public Object[] args() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportI18n that = (SupportI18n) o;
        return Objects.equals(baseName, that.baseName) && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseName, key);
    }

    @Override
    public String toString() {
        return getContentJsonString(Locale.CHINESE, Locale.ENGLISH);
    }
}
