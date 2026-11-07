/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest;

import com.github.potatoxf.generals.utils.security.Secure;
import com.github.potatoxf.generals.utils.security.digest.signature.CryptoSignatureEllipticCurve;
import com.github.potatoxf.generals.utils.security.digest.signature.CryptoSignatureMain;
import com.github.potatoxf.generals.utils.security.digest.summary.CryptoSummaryMac;
import com.github.potatoxf.generals.utils.security.digest.summary.CryptoSummaryMessageDigest;

import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * 加密摘要
 *
 * @author potatoxf
 */
public abstract class CryptoDigest<I extends CryptoDigest<I>> extends Secure<I> {
    protected final int digestLength;
    protected final int minKeyLength;

    protected CryptoDigest(Builder<I> builder) {
        super(builder);
        this.digestLength = builder.digestLength;
        this.minKeyLength = builder.minKeyLength;
    }

    public static CryptoDigest.Builder<CryptoSignatureMain> ofCryptoSignatureMain() {
        return new Builder<CryptoSignatureMain>() {
            @Override
            public CryptoSignatureMain build() {
                return new CryptoSignatureMain(this);
            }
        };
    }

    public static CryptoDigest.Builder<CryptoSignatureEllipticCurve> ofCryptoSignatureEllipticCurve(int requiredByteLength, String paramSpecCurveName) {
        return new Builder<CryptoSignatureEllipticCurve>() {
            @Override
            public CryptoSignatureEllipticCurve build() {
                return new CryptoSignatureEllipticCurve(this, requiredByteLength, paramSpecCurveName);
            }
        };
    }

    public static CryptoDigest.Builder<CryptoSummaryMac> ofCryptoSummaryMac() {
        return new Builder<CryptoSummaryMac>() {
            @Override
            public CryptoSummaryMac build() {
                return new CryptoSummaryMac(this);
            }
        };
    }

    public static CryptoDigest.Builder<CryptoSummaryMessageDigest> ofMessageDigest() {
        return new Builder<CryptoSummaryMessageDigest>() {
            @Override
            public CryptoSummaryMessageDigest build() {
                return new CryptoSummaryMessageDigest(this);
            }
        };
    }

    public final int digestLength() {
        return digestLength;
    }

    public final int minKeyLength() {
        return minKeyLength;
    }

    /**
     * to support assertValid(Key, boolean)
     */
    protected String keyType(boolean signing) {
        return signing ? "signing" : "verification";
    }

    public void assertValid(Key key, boolean signing) throws GeneralSecurityException {
        String algorithm = value();
        if (algorithm == null) algorithm = algorithm();
        if (algorithm == null) throw new GeneralSecurityException("The value and algorithm invalid.");
    }

    /**
     * CryptoDigest构建器
     */
    public abstract static class Builder<T extends CryptoDigest<T>> extends Secure.Builder<T, CryptoDigest.Builder<T>> {
        private int digestLength;
        private int minKeyLength;

        protected Builder() {
        }

        public int getDigestLength() {
            return digestLength;
        }

        public Builder<T> setDigestLength(int digestLength) {
            this.digestLength = digestLength;
            return this;
        }

        public int getMinKeyLength() {
            return minKeyLength;
        }

        public Builder<T> setMinKeyLength(int minKeyLength) {
            this.minKeyLength = minKeyLength;
            return this;
        }
    }
}