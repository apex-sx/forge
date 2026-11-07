/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.lang.SnowFlakeTemplate;

import java.util.UUID;
import java.util.function.LongFunction;

/**
 * @author potatoxf
 */
public interface GeneralID {
    SnowFlakeTemplate LC_0_0 = new SnowFlakeTemplate(1, 1, 1, 0, 0);
    SnowFlakeTemplate LC_0_1 = new SnowFlakeTemplate(1, 1, 1, 0, 1);
    SnowFlakeTemplate LC_1_0 = new SnowFlakeTemplate(1, 1, 1, 1, 0);
    SnowFlakeTemplate LC_1_1 = new SnowFlakeTemplate(1, 1, 1, 1, 1);

    /**
     * 下一个随机UUID
     *
     * @return 返回随机ID字符串
     */
    static String nextRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 下一个32位随机ID字符串
     *
     * @return 返回随机ID字符串
     */
    static String next32DigitRandomID() {
        return nextRandomUUID().replaceAll("-", "").trim();
    }

    /**
     * 下一个22位随机ID字符串
     *
     * @return 返回随机ID字符串
     */
    static String next22DigitRandomID() {
        UUID uuid = UUID.randomUUID();
        long highBit = uuid.getMostSignificantBits();
        long lowBit = uuid.getLeastSignificantBits();
        LongFunction<Character> radix64 = value -> {
            int i = (int) (value & 0x3F);
            if (i < 10) return (char) ('0' + i);
            else if (i < 36) return (char) ('A' + i);
            else if (i < 62) return (char) ('a' + i);
            else if (i == 62) return '-';
            else return '_';
        };
        char[] chars = new char[22];
        int c = 0;
        chars[c++] = radix64.apply((highBit & 0xC000000000000000L) >>> 62);
        for (int i = 0; i < 10; i++) {
            chars[c++] = radix64.apply((highBit << (2 + i * 6) & 0xFC00000000000000L) >>> 58);
        }
        chars[c++] = radix64.apply((highBit << 62 ^ lowBit >>> 2 & 0xFC00000000000000L) >>> 58);
        for (int i = 0; i < 10; i++) {
            chars[c++] = radix64.apply(((lowBit << (4 + i * 6)) & 0xFC00000000000000L) >>> 58);
        }
        return new String(chars);
    }
}
