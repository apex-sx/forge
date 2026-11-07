package apexsx.forge.loader.unicode;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 工厂类，用于创建不同类型的 UnicodeLoader 实例。
 *
 * @author zhitron
 */
public final class UnicodeLoaderFactory {

    private UnicodeLoaderFactory() {
    }

    /**
     * 从字符串创建 UnicodeLoader。
     *
     * @param input 字符串
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(String input) {
        return new UnicodeLoaderByCharArray(input.toCharArray(), 1024);
    }

    /**
     * 从字符串创建 UnicodeLoader，并指定缓冲区大小。
     *
     * @param input      字符串
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(String input, int bufferSize) {
        return new UnicodeLoaderByCharArray(input.toCharArray(), bufferSize);
    }

    /**
     * 从字符数组创建 UnicodeLoader。
     *
     * @param input 字符数组
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(char[] input) {
        return new UnicodeLoaderByCharArray(input, 1024);
    }

    /**
     * 从字符数组创建 UnicodeLoader，并指定缓冲区大小。
     *
     * @param input      字符数组
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(char[] input, int bufferSize) {
        return new UnicodeLoaderByCharArray(input, bufferSize);
    }

    /**
     * 从 CharBuffer 创建 UnicodeLoader。
     *
     * @param input CharBuffer
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(CharBuffer input) {
        return new UnicodeLoaderByCharBuffer(input, 1024);
    }

    /**
     * 从 CharBuffer 创建 UnicodeLoader，并指定缓冲区大小。
     *
     * @param input      CharBuffer
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(CharBuffer input, int bufferSize) {
        return new UnicodeLoaderByCharBuffer(input, bufferSize);
    }

    /**
     * 从 Reader 创建 UnicodeLoader。
     *
     * @param input Reader
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(Reader input) {
        return new UnicodeLoaderByReader(input, 1024);
    }

    /**
     * 从 Reader 创建 UnicodeLoader，并指定缓冲区大小。
     *
     * @param input      Reader
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(Reader input, int bufferSize) {
        return new UnicodeLoaderByReader(input, bufferSize);
    }

    /**
     * 从字节数组创建 UnicodeSequenceLoader。
     *
     * @param input 字节数组
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(byte[] input) {
        return new UnicodeSequenceLoaderByByteArray(input, StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从字节数组创建 UnicodeSequenceLoader。
     *
     * @param input   字节数组
     * @param charset 字符集
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(byte[] input, Charset charset) {
        return new UnicodeSequenceLoaderByByteArray(input, charset, 1024);
    }

    /**
     * 从字节数组创建 UnicodeSequenceLoader，并指定缓冲区大小。
     *
     * @param input      字节数组
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(byte[] input, Charset charset, int bufferSize) {
        return new UnicodeSequenceLoaderByByteArray(input, charset, bufferSize);
    }

    /**
     * 从 InputStream 创建 UnicodeSequenceLoader。
     *
     * @param input 输入流
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(InputStream input) {
        return new UnicodeSequenceLoaderByInputStream(input, StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从 InputStream 创建 UnicodeSequenceLoader。
     *
     * @param input   输入流
     * @param charset 字符集
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(InputStream input, Charset charset) {
        return new UnicodeSequenceLoaderByInputStream(input, charset, 1024);
    }

    /**
     * 从 InputStream 创建 UnicodeSequenceLoader，并指定缓冲区大小。
     *
     * @param input      输入流
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(InputStream input, Charset charset, int bufferSize) {
        return new UnicodeSequenceLoaderByInputStream(input, charset, bufferSize);
    }

    /**
     * 从 ReadableByteChannel 创建 UnicodeSequenceLoader。
     *
     * @param input 可读通道
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(ReadableByteChannel input) {
        return new UnicodeSequenceLoaderByReadableByteChannel(input, StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从 ReadableByteChannel 创建 UnicodeSequenceLoader。
     *
     * @param input   可读通道
     * @param charset 字符集
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(ReadableByteChannel input, Charset charset) {
        return new UnicodeSequenceLoaderByReadableByteChannel(input, charset, 1024);
    }

    /**
     * 从 ReadableByteChannel 创建 UnicodeSequenceLoader，并指定缓冲区大小。
     *
     * @param input      可读通道
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(ReadableByteChannel input, Charset charset, int bufferSize) {
        return new UnicodeSequenceLoaderByReadableByteChannel(input, charset, bufferSize);
    }

    /**
     * 从字符串创建 UnicodeLoader，并指定缓冲区大小。
     *
     * @param input      字符串
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     */
    public static UnicodeLoader of(String input, Charset charset, int bufferSize) {
        return new UnicodeSequenceLoaderByByteArray(input.getBytes(charset), charset, bufferSize);
    }

    /**
     * 从文件路径创建 UnicodeLoader（使用 UTF-8 编码）。
     *
     * @param input 文件路径
     * @return UnicodeLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static UnicodeLoader of(File input) throws IOException {
        return new UnicodeSequenceLoaderByInputStream(new FileInputStream(input), StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从文件路径创建 UnicodeLoader，并指定编码和缓冲区大小。
     *
     * @param input      文件路径
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static UnicodeLoader of(File input, Charset charset, int bufferSize) throws IOException {
        return new UnicodeSequenceLoaderByInputStream(new FileInputStream(input), charset, bufferSize);
    }

    /**
     * 从文件路径创建 UnicodeLoader（使用 UTF-8 编码）。
     *
     * @param input 文件路径
     * @return UnicodeLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static UnicodeLoader of(Path input) throws IOException {
        return new UnicodeSequenceLoaderByInputStream(Files.newInputStream(input), StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从文件路径创建 UnicodeLoader，并指定编码和缓冲区大小。
     *
     * @param input      文件路径
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return UnicodeLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static UnicodeLoader of(Path input, Charset charset, int bufferSize) throws IOException {
        return new UnicodeSequenceLoaderByInputStream(Files.newInputStream(input), charset, bufferSize);
    }
}
