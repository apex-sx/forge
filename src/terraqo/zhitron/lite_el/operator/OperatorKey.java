package com.github.zhitron.lite_el.operator;

import java.util.Arrays;

/**
 * @author zhitron
 */
public class OperatorKey {
    private final Class<?>[] valueTypeClasses;

    public OperatorKey(Class<?>...valueTypeClasses) {
        this.valueTypeClasses = valueTypeClasses;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof OperatorKey)) return false;
        OperatorKey that = (OperatorKey) object;
        return Arrays.equals(valueTypeClasses, that.valueTypeClasses);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(valueTypeClasses);
    }
}
