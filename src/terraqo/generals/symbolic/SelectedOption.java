/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

import com.github.potatoxf.generals.lang.SupportI18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Selected Option
 *
 * @author potatoxf
 */
public interface SelectedOption<K, V> {

    static List<SelectedOption<Integer, String>> list(String... values) {
        List<SelectedOption<Integer, String>> list = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            list.add(new Pair<>(i, values[i]));
        }
        return Collections.unmodifiableList(list);
    }

    static List<SelectedOption<Integer, String>> list(int[] keys, String[] values) {
        List<SelectedOption<Integer, String>> list = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            list.add(new Pair<>(keys[i], values[i]));
        }
        return Collections.unmodifiableList(list);
    }

    @SuppressWarnings("unchecked")
    static <E extends Enum<E> & Enumeration<E>> List<SelectedOption<Integer, String>> list(E e) {
        return SelectedOption.list((Class<E>) e.getClass());
    }

    static <E extends Enum<E> & Enumeration<E>> List<SelectedOption<Integer, String>> list(Class<E> enumClass) {
        Enumeration<E>[] enumConstants = enumClass.getEnumConstants();
        List<SelectedOption<Integer, String>> list = new ArrayList<>(enumConstants.length);
        for (Enumeration<E> enumConstant : enumConstants) {
            list.add(new Pair<>(enumConstant.getKey(), enumConstant.getValue()));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 获取键
     *
     * @return {@code K}
     */
    K getKey();

    /**
     * 获取值
     *
     * @return {@code V}
     */
    V getValue();

    /**
     * Enumeration Selected Option
     */
    interface Enumeration<E extends Enum<E> & Enumeration<E>> extends SelectedOption<Integer, String>, This<E> {

        /**
         * 获取I19n支持类
         *
         * @return {@code SupportI18n}
         */
        default SupportI18n getSupportI18n() {
            return null;
        }

        /**
         * 获取键
         *
         * @return {@code K}
         */
        @Override
        default Integer getKey() {
            return this$().ordinal();
        }

        /**
         * 获取值
         *
         * @return {@code V}
         */
        @Override
        default String getValue() {
            SupportI18n supportI18n = getSupportI18n();
            if (supportI18n == null) {
                return this$().name();
            } else {
                try {
                    return supportI18n.getContent();
                } catch (Throwable e) {
                    return this$().name();
                }
            }
        }

        /**
         * 尽可能相等
         *
         * @param other 另一个对象
         * @return 如果相等返回true，否则返回false
         */
        default boolean equalsAsPossible(Object other) {
            if (other instanceof Number) {
                int key = ((Number) other).intValue();
                return Objects.equals(getKey(), key);
            } else if (other instanceof String) {
                try {
                    int key = Integer.parseInt((String) other);
                    return Objects.equals(getKey(), key);
                } catch (NumberFormatException ignored) {
                    return Objects.equals(this$().name(), other) || Objects.equals(getValue(), other);
                }
            } else {
                if (thisClass$().isInstance(other)) {
                    return Objects.equals(this, other);
                } else {
                    return false;
                }
            }
        }
    }
}
