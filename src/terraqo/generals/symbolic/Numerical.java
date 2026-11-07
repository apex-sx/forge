/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author potatoxf
 */
public interface Numerical {

    Number value();

    default Byte byteValue() {
        Number number = value();
        return number != null ? number.byteValue() : null;
    }

    default byte byteValue(byte defaultValue) {
        Byte value = byteValue();
        return value != null ? value : defaultValue;
    }

    default Short shortValue() {
        Number number = value();
        return number != null ? number.shortValue() : null;
    }

    default short shortValue(short defaultValue) {
        Short value = shortValue();
        return value != null ? value : defaultValue;
    }

    default Integer intValue() {
        Number number = value();
        return number != null ? number.intValue() : null;
    }

    default int intValue(int defaultValue) {
        Integer value = intValue();
        return value != null ? value : defaultValue;
    }

    default Long longValue() {
        Number number = value();
        return number != null ? number.longValue() : null;
    }

    default long longValue(long defaultValue) {
        Long value = longValue();
        return value != null ? value : defaultValue;
    }

    default Float floatValue() {
        Number number = value();
        return number != null ? number.floatValue() : null;
    }

    default float floatValue(float defaultValue) {
        Float value = floatValue();
        return value != null ? value : defaultValue;
    }

    default Double doubleValue() {
        Number number = value();
        return number != null ? number.doubleValue() : null;
    }

    default double doubleValue(double defaultValue) {
        Double value = doubleValue();
        return value != null ? value : defaultValue;
    }

    default BigInteger bigIntegerValue() {
        Number number = value();
        if (number instanceof BigInteger) return (BigInteger) number;
        if (number instanceof BigDecimal) return ((BigDecimal) number).toBigIntegerExact();
        return number != null ? new BigInteger(number.toString()) : null;
    }

    default BigInteger bigIntegerValue(BigInteger defaultValue) {
        BigInteger value = bigIntegerValue();
        return value != null ? value : defaultValue;
    }

    default BigDecimal bigDecimalValue() {
        Number number = value();
        if (number instanceof BigInteger) return new BigDecimal((BigInteger) number);
        if (number instanceof BigDecimal) return (BigDecimal) number;
        return number != null ? new BigDecimal(number.toString()) : null;
    }

    default BigDecimal bigDecimalValue(BigDecimal defaultValue) {
        BigDecimal value = bigDecimalValue();
        return value != null ? value : defaultValue;
    }
}
