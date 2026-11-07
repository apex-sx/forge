/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.summary;

import com.github.potatoxf.generals.Kit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.GeneralSecurityException;

/**
 * 通用数字前面处理器
 *
 * @author potatoxf
 */
class SummaryServiceImpl implements SummaryService {
    private final SummaryUpdater summaryUpdater;
    /**
     * 盐值
     */
    private final byte[] salt;
    /**
     * 加盐位置，即将盐值字符串放置在数据的index数，默认0
     */
    private final int saltPosition;
    /**
     * 散列次数
     */
    private final int digestCount;

    SummaryServiceImpl(SummaryUpdater summaryUpdater, byte[] salt, int saltPosition, int digestCount) {
        this.summaryUpdater = summaryUpdater;
        this.salt = salt;
        this.saltPosition = saltPosition;
        this.digestCount = digestCount;
    }

    /**
     * 生成摘要，考虑加盐和重复摘要次数
     *
     * @param data 数据bytes
     * @return 摘要bytes
     */
    @Override
    public byte[] sign(final byte[] data) throws GeneralSecurityException {
        summaryUpdater.reset();
        if (data == null || data.length == 0) {
            return Kit.EMPTY_ARRAY_B;
        }
        byte[] result;
        if (salt == null || salt.length == 0) {
            // 无加盐
            summaryUpdater.update(data);
        } else {
            if (saltPosition <= 0) {
                // 加盐在开头，自动忽略空盐值
                summaryUpdater.update(salt);
                summaryUpdater.update(data);
            } else if (saltPosition >= data.length) {
                // 加盐在末尾，自动忽略空盐值
                summaryUpdater.update(data);
                summaryUpdater.update(salt);
            } else {
                // 加盐在中间
                summaryUpdater.update(data, 0, saltPosition);
                summaryUpdater.update(salt);
                summaryUpdater.update(data, saltPosition, data.length - saltPosition);
            }
        }
        result = summaryUpdater.finish();
        result = repeatSign(result);
        return result;
    }

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    @Override
    public byte[] sign(final ByteBuffer data) throws GeneralSecurityException {
        summaryUpdater.reset();
        if (data == null || data.limit() == 0) {
            return Kit.EMPTY_ARRAY_B;
        }
        if (data.position() > 0) {
            data.flip();
        }
        byte[] result;
        if (salt == null || salt.length == 0) {
            // 无加盐
            summaryUpdater.update(data);
        } else {
            int limit = data.limit();
            if (saltPosition <= 0) {
                // 加盐在开头，自动忽略空盐值
                summaryUpdater.update(salt);
                summaryUpdater.update(data);
            } else if (saltPosition >= limit) {
                // 加盐在末尾，自动忽略空盐值
                summaryUpdater.update(data);
                summaryUpdater.update(salt);
            } else {
                // 加盐在中间
                summaryUpdater.update((ByteBuffer) data.limit(saltPosition));
                summaryUpdater.update(salt);
                summaryUpdater.update((ByteBuffer) data.limit(limit));
            }
        }
        result = summaryUpdater.finish();
        result = repeatSign(result);
        return result;
    }

    /**
     * 生成摘要
     *
     * @param data {@link InputStream} 数据流
     * @return 摘要bytes
     * @throws IOException IO异常
     */
    @Override
    public byte[] sign(final InputStream data) throws IOException, GeneralSecurityException {
        summaryUpdater.reset();
        byte[] result;
        int readLen;
        byte[] buffer = new byte[Kit.BUFFER_SIZE];
        if (salt == null || salt.length == 0) {
            // 无加盐
            while ((readLen = data.read(buffer)) > -1) {
                summaryUpdater.update(buffer, 0, readLen);
            }
        } else {
            if (saltPosition <= 0) {
                // 加盐在开头
                summaryUpdater.update(salt);
            }
            int total = 0;
            while ((readLen = data.read(buffer)) > -1) {
                total += readLen;
                if (saltPosition > 0 && total >= saltPosition) {
                    int len = total - saltPosition;
                    if (len != 0) {
                        summaryUpdater.update(buffer, 0, len);
                    }
                    // 加盐在中间
                    summaryUpdater.update(salt);
                    summaryUpdater.update(buffer, len, readLen - len);
                } else {
                    summaryUpdater.update(buffer, 0, readLen);
                }
            }
            if (total < saltPosition) {
                // 加盐在末尾
                summaryUpdater.update(salt);
            }
        }
        result = summaryUpdater.finish();
        result = repeatSign(result);
        return result;
    }

    /**
     * 生存指纹
     *
     * @param data 数据
     * @return 返回指纹数据
     */
    @Override
    public byte[] sign(final FileChannel data) throws IOException, GeneralSecurityException {
        summaryUpdater.reset();
        byte[] result;
        int readLen;
        ByteBuffer byteBuffer = ByteBuffer.allocate(Kit.BUFFER_SIZE);
        if (salt == null || salt.length == 0) {
            // 无加盐
            while (data.read(byteBuffer) > 0) {
                byteBuffer.flip();
                summaryUpdater.update(byteBuffer);
                byteBuffer.clear();
            }
        } else {
            if (saltPosition <= 0) {
                // 加盐在开头
                summaryUpdater.update(salt);
            }
            int total = 0;
            while ((readLen = data.read(byteBuffer)) > -1) {
                total += readLen;
                byteBuffer.flip();
                if (saltPosition > 0 && total >= saltPosition) {
                    int len = total - saltPosition;
                    if (len != 0) {
                        byteBuffer.limit(len);
                        summaryUpdater.update(byteBuffer);
                    }
                    // 加盐在中间
                    summaryUpdater.update(salt);
                    summaryUpdater.update((ByteBuffer) byteBuffer.limit(readLen));
                } else {
                    summaryUpdater.update(byteBuffer);
                }
                byteBuffer.clear();
            }
            if (total < saltPosition) {
                // 加盐在末尾
                summaryUpdater.update(salt);
            }
        }
        result = summaryUpdater.finish();
        result = repeatSign(result);
        return result;
    }

    private byte[] repeatSign(byte[] result) throws GeneralSecurityException {
        if (salt != null && salt.length != 0) {
            for (int i = Math.max(0, digestCount); i > 0; i--) {
                result = this.sign(result);
            }
        }
        return result;
    }
}
