/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import terraqo.Constant;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 工厂类，用于创建不同类型的 CodePointLoader 实例。
 * <p>
 *
 * @author Terraqo
 */
public final class CodePointLoaderFactory {

    private CodePointLoaderFactory() {
    }

    /**
     * 从字符串创建 CodePointLoader。
     *
     * @param input 字符串
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(String input) {
        return new CodePointLoaderByCharArray(input.toCharArray(), Constant.BUFFER_SIZE);
    }

    /**
     * 从字符串创建 CodePointLoader，并指定缓冲区大小。
     *
     * @param input      字符串
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(String input, int bufferSize) {
        return new CodePointLoaderByCharArray(input.toCharArray(), bufferSize);
    }

    /**
     * 从字符数组创建 CodePointLoader。
     *
     * @param input 字符数组
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(char[] input) {
        return new CodePointLoaderByCharArray(input, Constant.BUFFER_SIZE);
    }

    /**
     * 从字符数组创建 CodePointLoader，并指定缓冲区大小。
     *
     * @param input      字符数组
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(char[] input, int bufferSize) {
        return new CodePointLoaderByCharArray(input, bufferSize);
    }

    /**
     * 从 CharBuffer 创建 CodePointLoader。
     *
     * @param input CharBuffer
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(CharBuffer input) {
        return new CodePointLoaderByCharBuffer(input, Constant.BUFFER_SIZE);
    }

    /**
     * 从 CharBuffer 创建 CodePointLoader，并指定缓冲区大小。
     *
     * @param input      CharBuffer
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(CharBuffer input, int bufferSize) {
        return new CodePointLoaderByCharBuffer(input, bufferSize);
    }

    /**
     * 从 Reader 创建 CodePointLoader。
     *
     * @param input Reader
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(Reader input) {
        return new CodePointLoaderByReader(input, Constant.BUFFER_SIZE);
    }

    /**
     * 从 Reader 创建 CodePointLoader，并指定缓冲区大小。
     *
     * @param input      Reader
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(Reader input, int bufferSize) {
        return new CodePointLoaderByReader(input, bufferSize);
    }

    /**
     * 从字节数组创建 CodePointSequenceLoader。
     *
     * @param input 字节数组
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(byte[] input) {
        return new CodePointSequenceLoaderByByteArray(input, StandardCharsets.UTF_8, Constant.BUFFER_SIZE);
    }

    /**
     * 从字节数组创建 CodePointSequenceLoader。
     *
     * @param input   字节数组
     * @param charset 字符集
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(byte[] input, Charset charset) {
        return new CodePointSequenceLoaderByByteArray(input, charset, Constant.BUFFER_SIZE);
    }

    /**
     * 从字节数组创建 CodePointSequenceLoader，并指定缓冲区大小。
     *
     * @param input      字节数组
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(byte[] input, Charset charset, int bufferSize) {
        return new CodePointSequenceLoaderByByteArray(input, charset, bufferSize);
    }

    /**
     * 从 InputStream 创建 CodePointSequenceLoader。
     *
     * @param input 输入流
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(InputStream input) {
        return new CodePointSequenceLoaderByInputStream(input, StandardCharsets.UTF_8, Constant.BUFFER_SIZE);
    }

    /**
     * 从 InputStream 创建 CodePointSequenceLoader。
     *
     * @param input   输入流
     * @param charset 字符集
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(InputStream input, Charset charset) {
        return new CodePointSequenceLoaderByInputStream(input, charset, Constant.BUFFER_SIZE);
    }

    /**
     * 从 InputStream 创建 CodePointSequenceLoader，并指定缓冲区大小。
     *
     * @param input      输入流
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(InputStream input, Charset charset, int bufferSize) {
        return new CodePointSequenceLoaderByInputStream(input, charset, bufferSize);
    }

    /**
     * 从 ReadableByteChannel 创建 CodePointSequenceLoader。
     *
     * @param input 可读通道
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(ReadableByteChannel input) {
        return new CodePointSequenceLoaderByReadableByteChannel(input, StandardCharsets.UTF_8, Constant.BUFFER_SIZE);
    }

    /**
     * 从 ReadableByteChannel 创建 CodePointSequenceLoader。
     *
     * @param input   可读通道
     * @param charset 字符集
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(ReadableByteChannel input, Charset charset) {
        return new CodePointSequenceLoaderByReadableByteChannel(input, charset, Constant.BUFFER_SIZE);
    }

    /**
     * 从 ReadableByteChannel 创建 CodePointSequenceLoader，并指定缓冲区大小。
     *
     * @param input      可读通道
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(ReadableByteChannel input, Charset charset, int bufferSize) {
        return new CodePointSequenceLoaderByReadableByteChannel(input, charset, bufferSize);
    }

    /**
     * 从字符串创建 CodePointLoader，并指定缓冲区大小。
     *
     * @param input      字符串
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     */
    public static CodePointLoader of(String input, Charset charset, int bufferSize) {
        return new CodePointSequenceLoaderByByteArray(input.getBytes(charset), charset, bufferSize);
    }

    /**
     * 从文件路径创建 CodePointLoader（使用 UTF-8 编码）。
     *
     * @param input 文件路径
     * @return CodePointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodePointLoader of(File input) throws IOException {
        return new CodePointSequenceLoaderByInputStream(new FileInputStream(input), StandardCharsets.UTF_8, Constant.BUFFER_SIZE);
    }

    /**
     * 从文件路径创建 CodePointLoader，并指定编码和缓冲区大小。
     *
     * @param input      文件路径
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodePointLoader of(File input, Charset charset, int bufferSize) throws IOException {
        return new CodePointSequenceLoaderByInputStream(new FileInputStream(input), charset, bufferSize);
    }

    /**
     * 从文件路径创建 CodePointLoader（使用 UTF-8 编码）。
     *
     * @param input 文件路径
     * @return CodePointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodePointLoader of(Path input) throws IOException {
        return new CodePointSequenceLoaderByInputStream(Files.newInputStream(input), StandardCharsets.UTF_8, Constant.BUFFER_SIZE);
    }

    /**
     * 从文件路径创建 CodePointLoader，并指定编码和缓冲区大小。
     *
     * @param input      文件路径
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodePointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodePointLoader of(Path input, Charset charset, int bufferSize) throws IOException {
        return new CodePointSequenceLoaderByInputStream(Files.newInputStream(input), charset, bufferSize);
    }
}
