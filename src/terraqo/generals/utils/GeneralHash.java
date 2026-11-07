/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * hash工具类
 *
 * @author potatoxf
 */
public interface GeneralHash {
    static String md5(String string) {
        return GeneralHash.md5(string, StandardCharsets.UTF_8);
    }

    static String md5(String string, Charset charset) {
        if (charset == null) charset = StandardCharsets.UTF_8;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(string.getBytes(charset));
            byte[] bytes = algorithm.digest();
            StringBuilder buf = new StringBuilder(bytes.length * 2);
            for (byte v : bytes) {
                if (v < 0x10) buf.append("0");
                buf.append(Integer.toString(v, 16));
            }
            return buf.toString();
        } catch (Throwable e) {
            Log.error("MD5 Error...", e);
        }
        return string;
    }
}
