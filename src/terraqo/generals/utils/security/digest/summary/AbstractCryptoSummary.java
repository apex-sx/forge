/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.summary;

import com.github.potatoxf.generals.utils.security.digest.CryptoDigest;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;

/**
 * 信息摘要
 *
 * @author potatoxf
 */
abstract class AbstractCryptoSummary<I extends AbstractCryptoSummary<I>> extends CryptoDigest<I> {
    public AbstractCryptoSummary(Builder<I> builder) {
        super(builder);
    }

    public SummaryService getCryptoSummaryService() throws GeneralSecurityException {
        return new SummaryServiceImpl(getCryptoSummaryCoder(), null, -1, 0);
    }

    public SummaryService getCryptoSummaryService(int digestCount) throws GeneralSecurityException {
        return new SummaryServiceImpl(getCryptoSummaryCoder(), null, -1, digestCount);
    }

    public SummaryService getCryptoSummaryService(byte[] salt, int digestCount) throws GeneralSecurityException {
        return new SummaryServiceImpl(getCryptoSummaryCoder(), salt, -1, digestCount);
    }

    public SummaryService getCryptoSummaryService(byte[] salt, int saltPosition, int digestCount) throws GeneralSecurityException {
        return new SummaryServiceImpl(getCryptoSummaryCoder(), salt, saltPosition, digestCount);
    }

    /**
     * 如果枚举实例表示 HMAC 签名算法，则返回 {@code true}，否则返回 {@code false}
     */
    public boolean isHmac() {
        return "HMAC".equalsIgnoreCase(family());
    }

    @Override
    public void assertValid(Key key, boolean signing) throws GeneralSecurityException {
        super.assertValid(key, signing);
        if (isHmac()) {
            String family = family();
            String name = value();
            if (!(key instanceof SecretKey)) {
                throw new InvalidKeyException(family + " " + keyType(signing)
                        + " keys must be SecretKey instances.");
            }
            SecretKey secretKey = (SecretKey) key;
            byte[] encoded = secretKey.getEncoded();
            if (encoded == null) {
                throw new InvalidKeyException("The " + keyType(signing) + " key's encoded bytes cannot be null.");
            }
            String alg = secretKey.getAlgorithm();
            if (alg == null) {
                throw new InvalidKeyException("The " + keyType(signing) + " key's algorithm cannot be null.");
            }
            // These next checks use equalsIgnoreCase per https://github.com/jwtk/jjwt/issues/381#issuecomment-412912272
            if (!alg.equalsIgnoreCase(this.value) &&
                    !alg.equalsIgnoreCase(algorithm)) {
                throw new InvalidKeyException("The " + keyType(signing) + " key's algorithm '" + alg +
                        "' does not equal a valid HmacSHA* algorithm name and cannot be used with " + name + ".");
            }
            int size = encoded.length * 8; //size in bits
            int minKeyLength = minKeyLength();
            if (size < minKeyLength) {
                throw new GeneralSecurityException("The " + keyType(signing) + " key's size is " + size + " bits which " +
                        "is not secure enough for the " + name + " algorithm.  The JWT " +
                        "JWA Specification (RFC 7518, Section 3.2) states that keys used with " + name + " MUST have a " +
                        "size >= " + minKeyLength + " bits (the key size must be greater than or equal to the hash " +
                        "output size).");
            }
        }
    }

    /**
     * 获取{@code CryptoSummaryCoder}
     *
     * @return {@code CryptoSummaryCoder}
     * @throws GeneralSecurityException 如果发生异常
     */
    public abstract SummaryUpdater getCryptoSummaryCoder() throws GeneralSecurityException;

}