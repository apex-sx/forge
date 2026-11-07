/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.digest.summary;

import java.security.GeneralSecurityException;

/**
 * MessageDigest信息摘要
 *
 * @author potatoxf
 */
public final class CryptoSummaryMessageDigest extends AbstractCryptoSummary<CryptoSummaryMessageDigest> {
    public CryptoSummaryMessageDigest(Builder<CryptoSummaryMessageDigest> builder) {
        super(builder);
    }

    @Override
    public SummaryUpdater getCryptoSummaryCoder() throws GeneralSecurityException {
        return new SummaryUpdaterMessageDigest(createMessageDigest());
    }
}
