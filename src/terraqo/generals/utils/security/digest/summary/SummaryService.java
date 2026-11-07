/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.summary;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;

/**
 * 指纹处理器
 *
 * @author potatoxf
 */
public interface SummaryService {

    /**
     * 生成一次性密码
     * <p>
     * HMAC-based one-time passwords (HOTP) 基于HMAC算法一次性密码生成器， 规范见：<a
     * href="https://tools.ietf.org/html/rfc4226">RFC&nbsp;4226</a>.
     *
     * <p>基于事件同步，通过某一特定的事件次序及相同的种子值作为输入，通过HASH算法运算出一致的密码
     *
     * <p>参考：https://github.com/jchambers/java-otp
     *
     * @param counter 事件计数的值，8 字节的整数，称为移动因子（moving factor）， 可以是基于计次的动移动因子，也可以是计时移动因子
     * @return 一次性密码的int值
     */
    default long generateHOTP(long counter, int passwordLength) throws GeneralSecurityException {
        // C 的整数值需要用二进制的字符串表达，比如某个事件计数为 3，
        // 则C是 "11"（此处省略了前面的二进制的数字0）
        byte[] buffer = new byte[8];
        buffer[0] = (byte) ((counter & 0xff00000000000000L) >>> 56);
        buffer[1] = (byte) ((counter & 0x00ff000000000000L) >>> 48);
        buffer[2] = (byte) ((counter & 0x0000ff0000000000L) >>> 40);
        buffer[3] = (byte) ((counter & 0x000000ff00000000L) >>> 32);
        buffer[4] = (byte) ((counter & 0x00000000ff000000L) >>> 24);
        buffer[5] = (byte) ((counter & 0x0000000000ff0000L) >>> 16);
        buffer[6] = (byte) ((counter & 0x000000000000ff00L) >>> 8);
        buffer[7] = (byte) ((counter & 0x00000000000000ffL) >>> 0);
        final byte[] digest = sign(buffer);


        final int count = 64 / 8, maxOffset = (count - 1) * 8;
        final int endIndex = digest.length - 1, offset = digest[endIndex] & endIndex;
        long result = (digest[offset] & 0x7FL) << maxOffset;
        for (int i = 1; i < count; i++) {
            result |= (digest[(offset + i) % digest.length] & 0xFFL) << (maxOffset - i * 8);
        }
        return result % (int) Math.pow(10, Math.max(passwordLength, 4));
    }

    /**
     * 使用给定的时间戳生成一次性密码.
     * <p>
     * time-based one-time passwords (TOTP) 基于时间戳算法的一次性密码生成器， 规范见：<a
     * href="https://tools.ietf.org/html/rfc6238">RFC&nbsp;6238</a>.
     *
     * <p>时间同步，基于客户端的动态口令和动态口令验证服务器的时间比对，一般每30秒产生一个新口令， 要求客户端和服务器能够十分精确的保持正确的时钟，客户端和服务端基于时间计算的动态口令才能一致
     *
     * <p>参考：https://github.com/jchambers/java-otp
     *
     * @param timestamp      用于生成密码的时间戳
     * @param passwordLength 密码长度
     * @param timeStep       步进
     * @return 一次性密码的int形式
     */
    default long generateTOTP(Instant timestamp, int passwordLength, long timeStep) throws GeneralSecurityException {
        return generateHOTP(timestamp.toEpochMilli() / timeStep, passwordLength);
    }

    /**
     * 用于验证code是否正确
     *
     * @param timestamp      验证时间戳
     * @param passwordLength 密码长度
     * @param timeStep       步进
     * @param code           code
     * @return 是否通过
     */
    default boolean validate(Instant timestamp, int passwordLength, long timeStep, long code) throws GeneralSecurityException {
        return generateTOTP(timestamp, passwordLength, timeStep) == code;
    }

    /**
     * 用于验证code是否正确
     *
     * @param timestamp      验证时间戳
     * @param passwordLength 密码长度
     * @param timeStep       步进
     * @param code           code
     * @param errorRange     误差范围
     * @return 是否通过
     */
    default boolean validate(Instant timestamp, int passwordLength, long timeStep, long code, int errorRange) throws GeneralSecurityException {
        if (errorRange == 0) {
            return generateTOTP(timestamp, passwordLength, timeStep) == code;
        }
        errorRange = Math.abs(errorRange);
        Duration duration = Duration.ofMillis(timeStep);
        for (int i = -errorRange; i <= errorRange; i++) {
            Instant instant = timestamp.plus(duration.multipliedBy(i));
            if (generateTOTP(instant, passwordLength, timeStep) == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    byte[] sign(byte[] data) throws GeneralSecurityException;

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    byte[] sign(ByteBuffer data) throws GeneralSecurityException;

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    byte[] sign(InputStream data) throws IOException, GeneralSecurityException;

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    byte[] sign(FileChannel data) throws IOException, GeneralSecurityException;

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    default byte[] sign(CharSequence data) throws GeneralSecurityException {
        return this.sign(data, Charset.defaultCharset());
    }

    /**
     * 生存指纹
     *
     * @param data    数据
     * @param charset 字符集
     * @return 返回指纹数据
     */
    default byte[] sign(CharSequence data, Charset charset) throws GeneralSecurityException {
        String string = data.toString();
        if (charset == null)
            return this.sign(string.getBytes());
        else {
            return this.sign(string.getBytes(charset));
        }
    }

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    default byte[] sign(Path data) throws IOException, GeneralSecurityException {
        return this.sign(data.toFile());
    }

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    default byte[] sign(File data) throws IOException, GeneralSecurityException {
        return this.sign(new FileInputStream(data).getChannel());
    }

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    default byte[] sign(RandomAccessFile data) throws IOException, GeneralSecurityException {
        return this.sign(data.getChannel());
    }
}
