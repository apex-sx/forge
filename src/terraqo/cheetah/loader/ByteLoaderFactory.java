/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import terraqo.Constant;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * ByteLoaderFactory 是一个工厂类，用于创建不同类型的 ByteLoader 实例。
 * 该类提供了多种静态方法，支持从不同的数据源创建 ByteLoader 对象。
 * <p>
 *
 * @author Terraqo
 */
public final class ByteLoaderFactory {
    /**
     * 私有构造函数，防止实例化此类。
     * 抛出 AssertionError 防止通过反射等方式创建实例。
     */
    private ByteLoaderFactory() {
        throw new AssertionError("No instances.");
    }

    /**
     * 创建一个使用字符数组作为输入源的 ByteLoader 实例，默认缓冲区大小为 {@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的字符数组
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(char[] input) {
        return new ByteLoaderByCharArray(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字符数组作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的字符数组
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(char[] input, int bufferSize) {
        return new ByteLoaderByCharArray(input, bufferSize);
    }

    /**
     * 创建一个使用字节数组作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的字节数组
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(byte[] input) {
        return new ByteLoaderByByteArray(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字节数组作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的字节数组
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(byte[] input, int bufferSize) {
        return new ByteLoaderByByteArray(input, bufferSize);
    }

    /**
     * 创建一个使用短整型数组作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的短整型数组
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(short[] input) {
        return new ByteLoaderByShortArray(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用短整型数组作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的短整型数组
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(short[] input, int bufferSize) {
        return new ByteLoaderByShortArray(input, bufferSize);
    }

    /**
     * 创建一个使用整型数组作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的整型数组
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(int[] input) {
        return new ByteLoaderByIntArray(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用整型数组作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的整型数组
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(int[] input, int bufferSize) {
        return new ByteLoaderByIntArray(input, bufferSize);
    }

    /**
     * 创建一个使用长整型数组作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的长整型数组
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(long[] input) {
        return new ByteLoaderByLongArray(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用长整型数组作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的长整型数组
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(long[] input, int bufferSize) {
        return new ByteLoaderByLongArray(input, bufferSize);
    }

    /**
     * 创建一个使用 IntStream 作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的 IntStream 流
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(IntStream input) {
        return new ByteLoaderByIntStream(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用 IntStream 作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的 IntStream 流
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(IntStream input, int bufferSize) {
        return new ByteLoaderByIntStream(input, bufferSize);
    }

    /**
     * 创建一个使用 LongStream 作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的 LongStream 流
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(LongStream input) {
        return new ByteLoaderByLongStream(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用 LongStream 作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的 LongStream 流
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(LongStream input, int bufferSize) {
        return new ByteLoaderByLongStream(input, bufferSize);
    }

    /**
     * 创建一个使用字符缓冲区作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的字符缓冲区
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(CharBuffer input) {
        return new ByteLoaderByCharBuffer(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字符缓冲区作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的字符缓冲区
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(CharBuffer input, int bufferSize) {
        return new ByteLoaderByCharBuffer(input, bufferSize);
    }

    /**
     * 创建一个使用字符流作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的字符流
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(Reader input) {
        return new ByteLoaderByReader(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字符流作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的字符流
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(Reader input, int bufferSize) {
        return new ByteLoaderByReader(input, bufferSize);
    }

    /**
     * 创建一个使用字节缓冲区作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的字节缓冲区
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(ByteBuffer input) {
        return new ByteLoaderByByteBuffer(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字节缓冲区作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的字节缓冲区
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(ByteBuffer input, int bufferSize) {
        return new ByteLoaderByByteBuffer(input, bufferSize);
    }

    /**
     * 创建一个使用字节流作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的字节流
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(InputStream input) {
        return new ByteLoaderByInputStream(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字节流作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的字节流
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(InputStream input, int bufferSize) {
        return new ByteLoaderByInputStream(input, bufferSize);
    }

    /**
     * 创建一个使用可读字节通道作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的可读字节通道
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(ReadableByteChannel input) {
        return new ByteLoaderByReadableByteChannel(input, Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用可读字节通道作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的可读字节通道
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(ReadableByteChannel input, int bufferSize) {
        return new ByteLoaderByReadableByteChannel(input, bufferSize);
    }

    /**
     * 创建一个使用字符串作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     * 字符串会被转换为字符数组处理。
     *
     * @param input 输入的字符串
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(String input) {
        return new ByteLoaderByCharArray(input.toCharArray(), Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用字符串作为输入源的 ByteLoader 实例，指定缓冲区大小。
     * 字符串会被转换为字符数组处理。
     *
     * @param input      输入的字符串
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(String input, int bufferSize) {
        return new ByteLoaderByCharArray(input.toCharArray(), bufferSize);
    }

    /**
     * 创建一个使用字符串和指定字符集作为输入源的 ByteLoader 实例，指定缓冲区大小。
     * 字符串会被转换为字节数组处理。
     *
     * @param input      输入的字符串
     * @param charset    指定的字符集
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     */
    public static ByteLoader of(String input, Charset charset, int bufferSize) {
        return new ByteLoaderByByteArray(input.getBytes(charset), bufferSize);
    }

    /**
     * 创建一个使用文件作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的文件
     * @return 返回一个新的 ByteLoader 实例
     * @throws IOException 如果打开文件时发生错误
     */
    public static ByteLoader of(File input) throws IOException {
        return new ByteLoaderByInputStream(new FileInputStream(input), Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用文件作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的文件
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     * @throws IOException 如果打开文件时发生错误
     */
    public static ByteLoader of(File input, int bufferSize) throws IOException {
        return new ByteLoaderByInputStream(new FileInputStream(input), bufferSize);
    }

    /**
     * 创建一个使用路径作为输入源的 ByteLoader 实例，默认缓冲区大小为{@link Constant#BUFFER_SIZE}。
     *
     * @param input 输入的路径
     * @return 返回一个新的 ByteLoader 实例
     * @throws IOException 如果打开路径时发生错误
     */
    public static ByteLoader of(Path input) throws IOException {
        return new ByteLoaderByInputStream(Files.newInputStream(input), Constant.BUFFER_SIZE);
    }

    /**
     * 创建一个使用路径作为输入源的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param input      输入的路径
     * @param bufferSize 缓冲区大小
     * @return 返回一个新的 ByteLoader 实例
     * @throws IOException 如果打开路径时发生错误
     */
    public static ByteLoader of(Path input, int bufferSize) throws IOException {
        return new ByteLoaderByInputStream(Files.newInputStream(input), bufferSize);
    }
}
