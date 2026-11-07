/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.utils.GeneralChar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * 单词Tokens
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public final class WordTokens {
    /**
     * 全局格式 - UpperUnderLine
     */
    public static final AtomicReference<FormatType> GLOBAL_FORMAT_TYPE_UPPER_UNDER_LINE = new AtomicReference<>(FormatType.UpperUnderLine);
    /**
     * 全局格式 - LowerCamelCase
     */
    public static final AtomicReference<FormatType> GLOBAL_FORMAT_TYPE_LOWER_CAMEL_CASE = new AtomicReference<>(FormatType.LowerCamelCase);
    /**
     * 全局格式 - LowerKebabCase
     */
    public static final AtomicReference<FormatType> GLOBAL_FORMAT_TYPE_LOWER_KEBAB_CASE = new AtomicReference<>(FormatType.LowerKebabCase);
    private static final BitSet LETTER_UPPER = new BitSet(), LETTER_LOWER = new BitSet(), DIGIT = new BitSet(), SPLIT = new BitSet(), HEAD = new BitSet();

    static {
        HEAD.set('$');
        SPLIT.set('-');
        SPLIT.set('_');
        SPLIT.set('&');
        for (int i = '0'; i < '9'; i++) DIGIT.set(i);
        for (int i = 'a'; i < 'z'; i++) LETTER_LOWER.set(i);
        for (int i = 'A'; i < 'Z'; i++) LETTER_UPPER.set(i);
    }

    private static final WordTokens EMPTY = new WordTokens(new String[0], 0, null);
    private static final Map<Integer, WordTokens> CACHE = new ConcurrentHashMap<>();

    private final String[] parts;
    private final int hash;
    private final AtomicReference<FormatType> formatType;

    public static WordTokens ofJava(String string) {
        return WordTokens.of(string, WordTokens.GLOBAL_FORMAT_TYPE_LOWER_CAMEL_CASE);
    }

    public static WordTokens ofDatabase(String string) {
        return WordTokens.of(string, WordTokens.GLOBAL_FORMAT_TYPE_UPPER_UNDER_LINE);
    }

    public static WordTokens ofHTML(String string) {
        return WordTokens.of(string, WordTokens.GLOBAL_FORMAT_TYPE_LOWER_KEBAB_CASE);
    }

    public static WordTokens of(String string) {
        return WordTokens.of(string, null);
    }

    public static WordTokens of(String string, AtomicReference<FormatType> formatType) {
        if (string == null || string.isEmpty()) return WordTokens.EMPTY;
        int[] codePoints = string.codePoints().toArray();
        int hash = hash(codePoints);
        if (CACHE.containsKey(hash)) return CACHE.get(hash);
        return CACHE.computeIfAbsent(hash, h -> new WordTokens(parts(codePoints), h, formatType));
    }

    private static int hash(int[] codePoints) {
        return Arrays.stream(codePoints).filter(value -> !SPLIT.get(value)).reduce(1, (r, v) -> 31 * r + v + (LETTER_UPPER.get(v) ? 32 : 0));
    }

    private static String[] parts(int[] codePoints) {
        ArrayList<String> list = new ArrayList<>(6);
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < codePoints.length; i++) {
            int codePoint = codePoints[i];
            if (SPLIT.get(codePoint)) {
                if (sb.length() != 0) {
                    list.add(sb.toString().intern());
                    sb.setLength(0);
                }
            } else if (LETTER_LOWER.get(codePoint) || DIGIT.get(codePoint)) {
                sb.append(GeneralChar.checkGetSingletonChar(codePoint));
            } else if (LETTER_UPPER.get(codePoint) || HEAD.get(codePoint)) {
                if (sb.length() != 0) {
                    list.add(sb.toString().intern());
                    sb.setLength(0);
                }
                while (HEAD.get(codePoints[i])) sb.append(GeneralChar.checkGetSingletonChar(codePoints[i++]));
                int l = i + 1;
                while (l < codePoints.length && (DIGIT.get(codePoints[l]) || LETTER_UPPER.get(codePoints[l]))) l++;

                if (l - i > 1) {
                    if (LETTER_LOWER.get(codePoints[l])) l--;
                    for (int j = i; j < l; j++) sb.append(GeneralChar.checkGetSingletonChar(codePoints[j]));
                    i = l - 1;
                } else {
                    char c = GeneralChar.checkGetSingletonChar(codePoints[i]);
                    sb.append(Character.toLowerCase(c));
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (sb.length() != 0) {
            list.add(sb.toString().intern());
            sb.setLength(0);
        }
        return list.toArray(new String[0]);
    }

    private WordTokens(String[] parts, int hash, AtomicReference<FormatType> formatType) {
        this.parts = parts;
        this.hash = hash;
        this.formatType = formatType;
    }

    public FormatType formatType() {
        return formatType == null ? null : formatType.get();
    }

    public String toUpperKebabCase() {
        return this.toKebabCase(true);
    }

    public String toUpperUnderLine() {
        return this.toUnderLine(true);
    }

    public String toUpperCamelCase() {
        return this.toCamelCase(true);
    }

    public String toLowerKebabCase() {
        return this.toKebabCase(false);
    }

    public String toLowerUnderLine() {
        return this.toUnderLine(false);
    }

    public String toLowerCamelCase() {
        return this.toCamelCase(false);
    }

    private String toKebabCase(boolean upper) {
        return marge("-", upper);
    }

    private String toUnderLine(boolean upper) {
        return marge("_", upper);
    }

    private String toCamelCase(boolean upper) {
        return marge(null, upper);
    }

    private String marge(String delimiter, boolean upper) {
        boolean isCamelCase = delimiter == null || delimiter.isEmpty();
        StringBuilder sb = new StringBuilder(parts.length * (parts[0].length() + 3));

        BiConsumer<String, Boolean> camelCase = (string, b) -> {
            char[] chars = string.toCharArray();
            int u = 0;
            for (char c : chars) {
                if (LETTER_UPPER.get(c)) u++;
                if (u >= 2) break;
            }
            for (char c : chars) {
                if (u == 2) {
                    sb.append(b ? Character.toUpperCase(c) : Character.toLowerCase(c));
                } else {
                    if (u != -1) {
                        if (LETTER_UPPER.get(c) || LETTER_LOWER.get(c)) {
                            sb.append(b ? Character.toUpperCase(c) : Character.toLowerCase(c));
                            u = -1;
                        } else {
                            sb.append(c);
                        }
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                }
            }
        };
        if (isCamelCase) {
            camelCase.accept(parts[0], upper);
        } else {
            sb.append(upper ? parts[0].toUpperCase() : parts[0].toLowerCase());
        }

        for (int x = 1; x < parts.length; x++) {
            if (delimiter != null) sb.append(delimiter);

            if (isCamelCase) {
                camelCase.accept(parts[x], true);
            } else {
                sb.append(upper ? parts[x].toUpperCase() : parts[x].toLowerCase());
            }
        }
        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        String[] otherParts = null;
        int otherHash = -1;
        if (other instanceof String) {
            int[] codePoints = ((String) other).codePoints().toArray();
            int hash = hash(codePoints);
            WordTokens wordTokens = CACHE.get(hash);
            otherParts = wordTokens != null ? wordTokens.parts : parts(codePoints);
            otherHash = hash;
        } else if (other instanceof WordTokens) {
            otherParts = ((WordTokens) other).parts;
            otherHash = ((WordTokens) other).hash;
        }
        if (otherParts != null && hash == otherHash && parts.length == otherParts.length) {
            for (int i = 0; i < parts.length; i++) {
                if (!parts[i].equalsIgnoreCase(otherParts[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        FormatType formatType = formatType();
        if (formatType != null) {
            switch (formatType) {
                case UpperCamelCase:
                    return toUpperCamelCase();
                case LowerCamelCase:
                    return toLowerCamelCase();
                case UpperUnderLine:
                    return toUpperUnderLine();
                case LowerUnderLine:
                    return toLowerUnderLine();
                case UpperKebabCase:
                    return toUpperKebabCase();
                case LowerKebabCase:
                    return toLowerKebabCase();
            }
        }
        return marge(null, false);
    }

    public enum FormatType {
        UpperCamelCase,
        LowerCamelCase,
        UpperUnderLine,
        LowerUnderLine,
        UpperKebabCase,
        LowerKebabCase,
    }
}
