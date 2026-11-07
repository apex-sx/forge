/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.attributes;

/**
 * 属性设置器接口，提供对对象属性的赋值能力。
 * <p>
 * 该接口继承自 {@link AttributeExisting}，允许在设置属性前检查属性是否存在，
 * 并支持原子性地更新属性值并获取旧值。
 *
 * @author Terraqo
 */
public interface AttributeSetter extends AttributeExisting {

    /**
     * 尝试设置指定键的属性值。
     * <p>
     * 如果属性已存在，则更新其值并返回旧值；
     * 如果属性不存在，则创建新属性并返回 {@code null}。
     *
     * @param key   属性键，不能为 {@code null}
     * @param value 要设置的属性值，可以为 {@code null}
     * @return 设置前的旧值；如果该属性之前不存在，则返回 {@code null}
     * @throws AttributeException 如果属性类型不匹配、权限不足或其他原因导致设置失败
     */
    Object trySetAttribute(Object key, Object value) throws AttributeException;
}
