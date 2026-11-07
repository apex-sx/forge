/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.utils.GeneralArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")
public enum ChinesePhonetic {
    /**
     * 该选项表示汉语拼音输出带有音调标记
     */
    WITH_TONE_MARK {
        @Override
        public String format(String pinyin) {
            pinyin = pinyin.replaceAll("v", "ü");
            int len = pinyin.length(), vi;
            char vc = '\0';
            if ((vi = pinyin.indexOf('a')) != -1) {
                vc = 'a';
            } else if ((vi = pinyin.indexOf('e')) != -1) {
                vc = 'e';
            } else if ((vi = pinyin.indexOf("ou")) != -1) {
                vc = 'o';
            } else {
                for (int i = len - 1; i >= 0; i--) {
                    char c = pinyin.charAt(i);
                    if (UNMARKED.indexOf(c) >= 0) {
                        vi = i;
                        vc = c;
                        break;
                    }
                }
            }
            if (vc != '\0') {
                int tuneNumber = Character.getNumericValue(pinyin.charAt(len - 1));
                char markedVowel = MARKED.charAt(UNMARKED.indexOf(vc) * 5 + (tuneNumber - 1));
                pinyin = pinyin.substring(0, vi) + markedVowel + pinyin.substring(vi + 1, len - 1);
            }
            return pinyin;
        }
    },
    /**
     * 不带音调，字符“v”
     */
    WITHOUT_TONE_CHAR_V_ASCII {
        @Override
        public String format(String pinyin) {
            return pinyin.replaceAll("[1-5]", "");
        }
    },
    /**
     * 不带音调，字符“ü”
     */
    WITHOUT_TONE_CHAR_U_UNICODE {
        @Override
        public String format(String pinyin) {
            return pinyin.replaceAll("[1-5]", "").replaceAll("v", "ü");
        }
    };

    private static final int START_UNICODE = 0x4E00, END_UNICODE = 0x9FA6;
    private static final String[][] CHINESE_PHONETIC = new String[END_UNICODE - START_UNICODE][];
    private static final String UNMARKED = "aeiouü", MARKED = "āáăàaēéĕèeīíĭìiōóŏòoūúŭùuǖǘǚǜü";

    static {
        InputStream in = ClassLoader.getSystemResourceAsStream("META-INF/ChinesePinYin");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            String line;
            for (int i = 0; (line = reader.readLine()) != null; i++) {
                String[] array = line.toLowerCase().split(",");
                CHINESE_PHONETIC[i] = new String[array.length];
                for (int j = 0; j < array.length; j++) {
                    CHINESE_PHONETIC[i][j] = array[j].trim();
                }
            }
        } catch (IOException e) {
            Log.error("Error to init ChinesePhonetic", e);
        }
    }

    /**
     * 是否有中文拼音
     *
     * @param c 中文字符
     * @return 如果有返回 {@code true}，否则 {@code false}
     */
    public static boolean checkSupport(char c, boolean throwException) {
        boolean result = c >= START_UNICODE && c <= END_UNICODE;
        if (throwException && !result) {
            throw new IllegalArgumentException("The [" + c + "] Chinese Pin Yin does not exist");
        }
        return result;
    }

    /**
     * 获取拼音个数，如果没有默认为1
     *
     * @param c 字符
     * @return 返回拼音个数
     * @throws IllegalArgumentException 如果不是中文字符串则抛出异常
     */
    public static int getPinYinCount(char c) {
        ChinesePhonetic.checkSupport(c, true);
        return ChinesePhonetic.getPhoneticListForChar(c, null).length;
    }

    /**
     * 组合字符串所有拼音的首字母
     *
     * @param string 输入字符串
     * @return 返回组合字符串所有拼音的首字母
     */
    public static String[] combinePhoneticHeadListForString(String string) {
        return ChinesePhonetic.combinePhoneticHeadListForString(string, false);
    }

    /**
     * 组合字符串所有拼音的首字母
     *
     * @param input 输入字符串
     * @param upper  是否大写
     * @return 返回组合字符串所有拼音的首字母
     */
    public static String[] combinePhoneticHeadListForString(String input, boolean upper) {
        char[][] data = ChinesePhonetic.getPhoneticHeadListForString(input, WITH_TONE_MARK);
        int[] array = Arrays.stream(data).mapToInt(v -> v.length).toArray();
        System.out.println(Arrays.toString(array));
        int[][] combines = GeneralArray.combine(array);
        LinkedHashSet<String> set = new LinkedHashSet<>();
        StringBuilder sb = new StringBuilder();
        for (int[] combine : combines) {
            sb.setLength(0);
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i][combine[i]]);
            }
            String s = sb.toString();
            if (upper) {
                s = s.toUpperCase();
            }
            set.add(s);
        }
        return set.toArray(new String[0]);
    }


    /**
     * 组合字符串所有拼音
     *
     * @param input           输入字符串
     * @param chinesePhonetic 中文拼音类型
     * @return 返回组合字符串所有拼音
     */
    public static String[] combinePhoneticListForString(String input, ChinesePhonetic chinesePhonetic) {
        return ChinesePhonetic.combinePhoneticListForString(input, chinesePhonetic, false);
    }

    /**
     * 组合字符串所有拼音
     *
     * @param input           输入字符串
     * @param chinesePhonetic 中文拼音类型
     * @param upper           是否大写
     * @return 返回组合字符串所有拼音
     */
    public static String[] combinePhoneticListForString(String input, ChinesePhonetic chinesePhonetic, boolean upper) {
        String[][] data = ChinesePhonetic.getPhoneticListForString(input, chinesePhonetic);
        int[] array = Arrays.stream(data).mapToInt(v -> v.length).toArray();
        int[][] combines = GeneralArray.combine(array);
        LinkedHashSet<String> set = new LinkedHashSet<>();
        StringBuilder sb = new StringBuilder();
        for (int[] combine : combines) {
            sb.setLength(0);
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i][combine[i]]);
            }
            String s = sb.toString();
            if (upper) {
                s = s.toUpperCase();
            }
            set.add(s);
        }
        return set.toArray(new String[0]);
    }

    /**
     * 获取字符串所有拼音
     *
     * @param input           输入字符串
     * @param chinesePhonetic 中文拼音类型
     * @return 返回字符串所有拼音
     */
    public static char[][] getPhoneticHeadListForString(String input, ChinesePhonetic chinesePhonetic) {
        char[] chars = input.toCharArray();
        char[][] results = new char[chars.length][];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (checkSupport(c, false)) {
                results[i] = ChinesePhonetic.getPhoneticHeadListForChar(c, chinesePhonetic);
            } else {
                results[i] = new char[]{c};
            }
        }
        return results;
    }

    /**
     * 获取中文所有拼音的首字母
     *
     * @param input           中文字符
     * @param chinesePhonetic 中文拼音类型
     * @return 返回中文所有拼音的首字母
     * @throws IllegalArgumentException 如果不是中文字符串则抛出异常
     */
    public static char[] getPhoneticHeadListForChar(char input, ChinesePhonetic chinesePhonetic) {
        String[] charPhoneticList = ChinesePhonetic.getPhoneticListForChar(input, chinesePhonetic);
        int[] intPhoneticHeadList = Arrays.stream(charPhoneticList).mapToInt(s -> s.codePointAt(0)).distinct().toArray();
        char[] charPhoneticHeadList = new char[intPhoneticHeadList.length];
        for (int i = 0; i < intPhoneticHeadList.length; i++) {
            charPhoneticHeadList[i] = (char) intPhoneticHeadList[i];
        }
        return charPhoneticHeadList;
    }

    /**
     * 获取中文默认拼音
     *
     * @param input           中文字符
     * @param chinesePhonetic 中文拼音类型
     * @return 返回中文拼音
     * @throws IllegalArgumentException 如果不是中文字符串则抛出异常
     */
    public static String getPhoneticForChar(char input, ChinesePhonetic chinesePhonetic) {
        return ChinesePhonetic.getPhoneticForChar(input, chinesePhonetic, 0);
    }

    /**
     * 获取中文默认拼音
     *
     * @param input           中文字符
     * @param chinesePhonetic 中文拼音类型
     * @return 返回中文拼音
     * @throws IllegalArgumentException 如果不是中文字符串则抛出异常
     */
    public static String getPhoneticForChar(char input, ChinesePhonetic chinesePhonetic, int index) {
        String[] strings = ChinesePhonetic.getPhoneticListForChar(input, chinesePhonetic);
        return index < 0 || index >= strings.length ? strings[0] : strings[index];
    }

    /**
     * 获取字符串所有拼音
     *
     * @param input 输入字符串
     * @return 返回字符串所有拼音
     */
    public static String[][] getPhoneticListForString(String input, ChinesePhonetic chinesePhonetic) {
        char[] chars = input.toCharArray();
        String[][] results = new String[chars.length][];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (checkSupport(c, false)) {
                results[i] = ChinesePhonetic.getPhoneticListForChar(c, chinesePhonetic);
            } else {
                results[i] = new String[]{String.valueOf(c)};
            }
        }
        return results;
    }

    /**
     * 获取中文所有拼音
     *
     * @param input           中文字符
     * @param chinesePhonetic 中文拼音类型
     * @return 返回中文拼音列表
     * @throws IllegalArgumentException 如果不是中文字符串则抛出异常
     */
    public static String[] getPhoneticListForChar(char input, ChinesePhonetic chinesePhonetic) {
        ChinesePhonetic.checkSupport(input, true);
        String[] array = CHINESE_PHONETIC[input - START_UNICODE];
        if (chinesePhonetic == null) return array;
        return Arrays.stream(array).map(chinesePhonetic::format).toArray(String[]::new);
    }

    /**
     * 格式化拼音
     *
     * @param pinyin 拼音字符粗
     * @return 格式化后的拼音字符串
     */
    public abstract String format(String pinyin);
}
