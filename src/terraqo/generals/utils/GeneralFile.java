/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件处理工具类
 *
 * @author potatoxf
 */
@SuppressWarnings("UnusedReturnValue")
public interface GeneralFile {

    static File openFileObject(Object fileObject) {
        File file;
        if (fileObject instanceof String) {
            file = new File(fileObject.toString());
        } else if (fileObject instanceof File) {
            file = (File) fileObject;
        } else if (fileObject instanceof Path) {
            file = ((Path) fileObject).toFile();
        } else {
            throw new IllegalArgumentException("The input file object is not String,File or Path");
        }
        return file;
    }

    static Path openPathObject(Object fileObject) {
        Path file;
        if (fileObject instanceof String) {
            file = Paths.get(fileObject.toString());
        } else if (fileObject instanceof File) {
            file = ((File) fileObject).toPath();
        } else if (fileObject instanceof Path) {
            file = (Path) fileObject;
        } else {
            throw new IllegalArgumentException("The input file object is not String,File or Path");
        }
        return file;
    }

    /**
     * 删除文件
     *
     * @param fileObject 文件
     * @return 返回是否删除
     */
    static boolean deleteFile(Object fileObject) {
        return GeneralFile.deleteFile(fileObject, false);
    }

    /**
     * 删除文件
     *
     * @param fileObject 文件
     * @param thorough   是否彻底删除文件
     * @return 返回是否删除
     */
    static boolean deleteFile(Object fileObject, boolean thorough) {
        File file = GeneralFile.openFileObject(fileObject);
        if (!file.exists()) return true;
        if (file.isFile()) {
            if (thorough) {
                long length = file.length();
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    GeneralIO.write(outputStream, length, true, (byte) 0);
                } catch (IOException ignored) {
                }
            }
            return file.delete();
        }
        return false;
    }

    /**
     * 读取文件到输出文件
     *
     * @param inputFileObject  输入文件对象
     * @param outputFileObject 输出文件对象
     */
    static void readFileToOutputFile(Object inputFileObject, Object outputFileObject) throws IOException {
        File file = GeneralFile.openFileObject(outputFileObject);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            GeneralFile.readFileToOutputStream(inputFileObject, outputStream);
        }
    }

    /**
     * 读取文件到输出流
     *
     * @param fileObject   文件对象
     * @param outputStream 输出流
     */
    static void readFileToOutputStream(Object fileObject, OutputStream outputStream) throws IOException {
        File file = GeneralFile.openFileObject(fileObject);
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        try (FileInputStream inputStream = new FileInputStream(file)) {
            GeneralIO.copy(inputStream, outputStream);
        }
    }
}
