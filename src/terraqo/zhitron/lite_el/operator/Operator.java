package com.github.zhitron.lite_el.operator;

@FunctionalInterface
public interface Operator {
    Object apply(Object... values);
}
