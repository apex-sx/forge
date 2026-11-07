/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.time.Duration;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

/**
 * @author potatoxf
 */
public interface GeneralAssertion {
    static void checkState(boolean success) {
        GeneralAssertion.checkAndReturn(success, null);
    }

    static void checkState(boolean success, String messageTemplate, Object... args) {
        GeneralAssertion.checkAndReturn(success, null, messageTemplate, args);
    }

    static void checkState(boolean success, Supplier<String> messageSupplier) {
        GeneralAssertion.checkAndReturn(success, null, messageSupplier);
    }

    static <T> T checkStateAndReturn(boolean success, T input) {
        if (!success) {
            throw new IllegalStateException("The check result is error");
        }
        return input;
    }

    static <T> T checkStateAndReturn(boolean success, T input, String messageTemplate, Object... args) {
        if (!success) {
            throw new IllegalStateException(String.format(messageTemplate, args));
        }
        return input;
    }

    static <T> T checkStateAndReturn(boolean success, T input, Supplier<String> messageSupplier) {
        if (!success) {
            throw new IllegalStateException(messageSupplier.get());
        }
        return input;
    }

    static void check(boolean success) {
        GeneralAssertion.checkAndReturn(success, null);
    }

    static void check(boolean success, String messageTemplate, Object... args) {
        GeneralAssertion.checkAndReturn(success, null, messageTemplate, args);
    }

    static void check(boolean success, Supplier<String> messageSupplier) {
        GeneralAssertion.checkAndReturn(success, null, messageSupplier);
    }

    static <T> T checkAndReturn(boolean success, T input) {
        if (!success) {
            throw new IllegalArgumentException("The check result is error");
        }
        return input;
    }

    static <T> T checkAndReturn(boolean success, T input, String messageTemplate, Object... args) {
        if (!success) {
            throw new IllegalArgumentException(String.format(messageTemplate, args));
        }
        return input;
    }

    static <T> T checkAndReturn(boolean success, T input, Supplier<String> messageSupplier) {
        if (!success) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
        return input;
    }

    static <T> T checkNoNull(T input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoNull(input), input, "The input value must be not null");
    }

    static <T> T checkNoNull(T input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoNull(input), input, messageTemplate, args);
    }

    static <T> T checkNoNull(T input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoNull(input), input, messageSupplier);
    }

    static void checkesNoNull(Object input, Object other) {
        GeneralAssertion.check(GeneralIF.isNoNull(input, other), null, "The input value must be not null");
    }

    static void checkesNoNull(Object input, Object other, String messageTemplate, Object... args) {
        GeneralAssertion.checkAndReturn(GeneralIF.isNoNull(input, other), null, messageTemplate, args);
    }

    static void checkesNoNull(Object input, Object other, Supplier<String> messageSupplier) {
        GeneralAssertion.checkAndReturn(GeneralIF.isNoNull(input, other), null, messageSupplier);
    }

    static <T extends CharSequence> T checkNoEmpty(T input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static <T extends CharSequence> T checkNoEmpty(T input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static <T extends CharSequence> T checkNoEmpty(T input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static <E, T extends Collection<E>> T checkNoEmpty(T input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static <E, T extends Collection<E>> T checkNoEmpty(T input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static <E, T extends Collection<E>> T checkNoEmpty(T input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static <T> T[] checkNoEmpty(T[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static <T> T[] checkNoEmpty(T[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static <T> T[] checkNoEmpty(T[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static boolean[] checkNoEmpty(boolean[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static boolean[] checkNoEmpty(boolean[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static boolean[] checkNoEmpty(boolean[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static char[] checkNoEmpty(char[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static char[] checkNoEmpty(char[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static char[] checkNoEmpty(char[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static byte[] checkNoEmpty(byte[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static byte[] checkNoEmpty(byte[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static byte[] checkNoEmpty(byte[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static short[] checkNoEmpty(short[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static short[] checkNoEmpty(short[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static short[] checkNoEmpty(short[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static int[] checkNoEmpty(int[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static int[] checkNoEmpty(int[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static int[] checkNoEmpty(int[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static long[] checkNoEmpty(long[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static long[] checkNoEmpty(long[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static long[] checkNoEmpty(long[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static float[] checkNoEmpty(float[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static float[] checkNoEmpty(float[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static float[] checkNoEmpty(float[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static double[] checkNoEmpty(double[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, "The input value must be not empty");
    }

    static double[] checkNoEmpty(double[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageTemplate, args);
    }

    static double[] checkNoEmpty(double[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNoEmpty(input), input, messageSupplier);
    }

    static <E, T extends Collection<E>> T checkElementsNoNull(T input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoNull(input), input, "The input collection value must be not null");
    }

    static <E, T extends Collection<E>> T checkElementsNoNull(T input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoNull(input), input, messageTemplate, args);
    }

    static <E, T extends Collection<E>> T checkElementsNoNull(T input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoNull(input), input, messageSupplier);
    }

    static <E> E[] checkElementsNoNull(E[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoNull(input), input, "The input array value must be not null");
    }

    static <E> E[] checkElementsNoNull(E[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoNull(input), input, messageTemplate, args);
    }

    static <E> E[] checkElementsNoNull(E[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoNull(input), input, messageSupplier);
    }

    static <T extends Collection<? extends CharSequence>> T checkElementsNoEmpty(T input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoEmpty(input), input, "The input array value must be not empty");
    }

    static <T extends Collection<? extends CharSequence>> T checkElementsNoEmpty(T input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoEmpty(input), input, messageTemplate, args);
    }

    static <T extends Collection<? extends CharSequence>> T checkElementsNoEmpty(T input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoEmpty(input), input, messageSupplier);
    }

    static <E extends CharSequence> E[] checkElementsNoEmpty(E[] input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoEmpty(input), input, "The input array value must be not empty");
    }

    static <E extends CharSequence> E[] checkElementsNoEmpty(E[] input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoEmpty(input), input, messageTemplate, args);
    }

    static <E extends CharSequence> E[] checkElementsNoEmpty(E[] input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isElementsNoEmpty(input), input, messageSupplier);
    }

    static void checkContain(String input, String ele) {
        GeneralAssertion.check(GeneralIF.isContain(input, ele), "The substring '%s' is not in the string '%s'", ele, input);
    }

    static void checkContain(String input, String ele, String messageTemplate, Object... args) {
        GeneralAssertion.checkAndReturn(GeneralIF.isContain(input, ele), null, messageTemplate, args);
    }

    static void checkContain(String input, String ele, Supplier<String> messageSupplier) {
        GeneralAssertion.checkAndReturn(GeneralIF.isContain(input, ele), null, messageSupplier);
    }

    static <T extends CharSequence> T checkMatch(T input, IntPredicate charMatch) {
        return GeneralAssertion.checkMatch(input, charMatch, value -> "The '" + input + "' string contains invalid characters '" + ((char) value) + "'");
    }

    static <T extends CharSequence> T checkMatch(T input, IntPredicate charMatch, IntFunction<String> messageSupplier) {
        int[] array = input.codePoints().toArray();
        for (int c : array) {
            if (!charMatch.test(c)) {
                throw new IllegalArgumentException(messageSupplier.apply(c));
            }
        }
        return input;
    }

    static void checkElementIndex(int index, int size) {
        GeneralAssertion.check(index >= 0 && index < size, "The index must be 0<=index(%d)x<size(%d)", index, size);
    }

    static void checkElementIndex(int start, int end, int size) {
        GeneralAssertion.check(start <= end && start >= 0 && end < size, "The index must be 0<=start(%d)<=end(%d)<size(%d)", start, end, size);

    }

    static Duration checkNoNegative(Duration input) {
        return GeneralAssertion.checkAndReturn(input != null && !input.isNegative(), input, "The value '%s', but the duration cannot be negative", input);
    }

    static long checkNoNegative(long input) {
        return GeneralAssertion.checkAndReturn(input >= 0, input, "The value '%s', but it must be no negative", input);
    }

    static long checkNoNegative(long input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageTemplate, args);
    }

    static long checkNoNegative(long input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageSupplier);
    }

    static int checkNoNegative(int input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, "The value '%s', but it must be no negative", input);
    }

    static int checkNoNegative(int input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageTemplate, args);
    }

    static int checkNoNegative(int input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageSupplier);
    }

    static short checkNoNegative(short input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, "The value '%s', but it must be no negative", input);
    }

    static short checkNoNegative(short input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageTemplate, args);
    }

    static short checkNoNegative(short input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageSupplier);
    }

    static byte checkNoNegative(byte input) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, "The value '%s', but it must be no negative", input);
    }

    static byte checkNoNegative(byte input, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageTemplate, args);
    }

    static byte checkNoNegative(byte input, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input), input, messageSupplier);
    }

    static long checkNatural(long input, long limit) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    static long checkNatural(long input, long limit, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageTemplate, args);
    }

    static long checkNatural(long input, long limit, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageSupplier);
    }

    static int checkNatural(int input, int limit) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    static int checkNatural(int input, int limit, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageTemplate, args);
    }

    static int checkNatural(int input, int limit, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageSupplier);
    }

    static short checkNatural(short input, short limit) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    static short checkNatural(short input, short limit, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageTemplate, args);
    }

    static short checkNatural(short input, short limit, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageSupplier);
    }

    static byte checkNatural(byte input, byte limit) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    static byte checkNatural(byte input, byte limit, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageTemplate, args);
    }

    static byte checkNatural(byte input, byte limit, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isNatural(input, limit), input, messageSupplier);
    }

    static long checkRange(long input, long lo, long hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    static long checkRange(long input, long lo, long hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    static long checkRange(long input, long lo, long hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageSupplier);
    }

    static int checkRange(int input, int lo, int hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    static int checkRange(int input, int lo, int hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    static int checkRange(int input, int lo, int hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageSupplier);
    }

    static short checkRange(short input, short lo, short hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    static short checkRange(short input, short lo, short hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    static short checkRange(short input, short lo, short hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageSupplier);
    }

    static byte checkRange(byte input, byte lo, byte hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    static byte checkRange(byte input, byte lo, byte hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    static byte checkRange(byte input, byte lo, byte hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isInRange(input, lo, hi), input, messageSupplier);
    }

    static long checkExcludeRange(long input, long lo, long hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    static long checkExcludeRange(long input, long lo, long hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    static long checkExcludeRange(long input, long lo, long hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageSupplier);
    }

    static int checkExcludeRange(int input, int lo, int hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    static int checkExcludeRange(int input, int lo, int hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    static int checkExcludeRange(int input, int lo, int hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageSupplier);
    }

    static short checkExcludeRange(short input, short lo, short hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    static short checkExcludeRange(short input, short lo, short hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    static short checkExcludeRange(short input, short lo, short hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageSupplier);
    }

    static byte checkExcludeRange(byte input, byte lo, byte hi) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    static byte checkExcludeRange(byte input, byte lo, byte hi, String messageTemplate, Object... args) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    static byte checkExcludeRange(byte input, byte lo, byte hi, Supplier<String> messageSupplier) {
        return GeneralAssertion.checkAndReturn(GeneralIF.isOutRange(input, lo, hi), input, messageSupplier);
    }

    static void checkCompatibilityFrom(Class<?> inputClass, Class<?> matchClass) {
        GeneralAssertion.check(GeneralIF.isCompatibilityFrom(inputClass, matchClass), null, "This match type '%s' is not compatible with input type '%s'", matchClass, inputClass);
    }

    static void checkCompatibilityFrom(Class<?> inputClass, Class<?> matchClass, String messageTemplate, Object... args) {
        GeneralAssertion.checkAndReturn(GeneralIF.isCompatibilityFrom(inputClass, matchClass), null, messageTemplate, args);
    }

    static void checkCompatibilityFrom(Class<?> inputClass, Class<?> matchClass, Supplier<String> messageSupplier) {
        GeneralAssertion.checkAndReturn(GeneralIF.isCompatibilityFrom(inputClass, matchClass), null, messageSupplier);
    }


}
