/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import static com.github.potatoxf.generals.Kit.P_FILE_ILLEGAL;
import static com.github.potatoxf.generals.Kit.P_PATH_DOT;

/**
 * 安全功能接口，该功能用于处理问题
 *
 * @author potatoxf
 */
public interface Safe {

    /**
     * 安全创建路径字符串，排除一切攻击可能
     *
     * @param root  根路径
     * @param paths 路径
     * @return 返回值
     */
    static String newPathString(Object root, Object... paths) {
        boolean isAbsolute = false, isDirectory = false;
        String string = root.toString();
        Character driver = string.charAt(0);
        if (driver == '/' || driver == '\\') {
            isAbsolute = true;
            driver = null;
        } else if (Character.isLetter(driver) && string.charAt(1) == ':') {
            isAbsolute = true;
            driver = Character.toUpperCase(driver);
            string = string.substring(2);
        } else {
            driver = null;
        }
        List<String> strings = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(string, "/\\", false);
        while (st.hasMoreTokens()) {
            string = st.nextToken().trim();
            string = P_PATH_DOT.matcher(string).replaceAll("");
            string = P_FILE_ILLEGAL.matcher(string).replaceAll("");
            if (string.isEmpty()) continue;
            strings.add(string);
        }
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i].toString();
            if (i + 1 == paths.length) isDirectory = path.endsWith("/") || path.endsWith("\\");
            st = new StringTokenizer(path, "/\\", false);
            while (st.hasMoreTokens()) {
                string = st.nextToken().trim();
                string = P_PATH_DOT.matcher(string).replaceAll("");
                string = P_FILE_ILLEGAL.matcher(string).replaceAll("");
                if (string.isEmpty()) continue;
                strings.add(string);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (isAbsolute) (driver != null ? sb.append(driver).append(':') : sb).append('/');
        for (String s : strings) sb.append(s).append('/');
        if (!isDirectory) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 安全创建路径字符串，排除一切攻击可能
     *
     * @param root  根路径
     * @param paths 路径
     * @return 返回值
     */
    static Path newPath(Object root, Object... paths) {
        return Paths.get(Safe.newPathString(root, paths));
    }

    /**
     * 安全创建路径字符串，排除一切攻击可能
     *
     * @param root  根路径
     * @param paths 路径
     * @return 返回值
     */
    static File newFile(Object root, Object... paths) {
        return new File(Safe.newPathString(root, paths));
    }

    /**
     * Returns the length of the given array in a null-safe manner.
     *
     * @param array an array or null
     * @return the array length, or 0 if the given array is null.
     * @author potatoxf
     */
    static int length(byte[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * Returns the length of the given array in a null-safe manner.
     *
     * @param array an array or null
     * @return the array length, or 0 if the given array is null.
     * @author potatoxf
     */
    static int length(char[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * Returns the length of the given CharSequence in a null-safe manner.
     *
     * @param csq a CharSequence or null
     * @return the CharSequence length, or 0 if the given CharSequence is null.
     * @author potatoxf
     */
    static int length(CharSequence csq) {
        return csq == null ? 0 : csq.length();
    }

    /**
     * Returns the length of the given array in a null-safe manner.
     *
     * @param array an array or null
     * @return the array length, or 0 if the given array is null.
     * @author potatoxf
     */
    static int length(Object[] array) {
        return array == null ? 0 : array.length;
    }
}

