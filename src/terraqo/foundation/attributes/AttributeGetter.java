/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.attributes;

/**
 * 属性获取器接口，定义了尝试获取指定属性值的行为。
 * <p>
 * 该接口继承自 {@link AttributeExisting}，通常用于在不确定属性是否存在或获取可能失败的场景下，
 * 安全地尝试检索属性值。
 *
 * @author Terraqo
 */
public interface AttributeGetter extends AttributeExisting {

    /**
     * 尝试获取指定键对应的属性值。
     * <p>
     * 如果属性不存在、类型不匹配或在获取过程中发生其他错误，将抛出 {@link AttributeException}。
     * 具体的行为（如是否返回 null 或直接抛异常）取决于实现类的具体逻辑。
     *
     * @param key 属性的键，不能为 null
     * @return 属性值
     * @throws AttributeException 当属性不存在、获取失败或发生其他错误时抛出
     */
    Object tryGetAttribute(Object key) throws AttributeException;
}
