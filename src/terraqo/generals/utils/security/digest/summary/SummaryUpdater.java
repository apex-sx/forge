/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.summary;

import com.github.potatoxf.generals.utils.security.CryptoUpdater;

/**
 * 指纹编码
 *
 * @author potatoxf
 */
public interface SummaryUpdater extends CryptoUpdater {
    /**
     * 完成
     *
     * @return 返回指纹结果
     */
    byte[] finish();

    /**
     * 获取散列长度，0表示不支持此方法
     *
     * @return 散列长度，0表示不支持此方法
     */
    int getDigestLength();
}
