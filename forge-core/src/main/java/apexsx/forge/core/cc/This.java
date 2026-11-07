/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.core.cc;

/**
 * @author Terraqo
 */
@SuppressWarnings("unchecked")
public interface This<T extends This<T>> {

    /**
     * 返回本的的Class
     *
     * @return {@code this}
     */
    default Class<T> thisClass$() {
        return (Class<T>) this$().getClass();
    }

    /**
     * 返回this
     *
     * @return {@code this}
     */
    default T this$() {
        return (T) this;
    }

    /**
     * 从另一个相同类复制属性到this
     *
     * @param other 另一个对象
     * @return {@code this}
     */
    default T assign(T other) {
        return this$();
    }
}
