/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import java.nio.ByteBuffer;
import java.security.*;

/**
 * @author potatoxf
 */
class SignatureMain implements SignatureSigner, SignatureVerifier {
    final AbstractCryptoSignature<?> cryptoSignature;
    final PrivateKey privateKey;
    final PublicKey publicKey;

    SignatureMain(AbstractCryptoSignature<?> cryptoSignature, PrivateKey privateKey) throws GeneralSecurityException {
        this.cryptoSignature = cryptoSignature;
        this.privateKey = privateKey;
        this.publicKey = null;
        this.reset();
    }

    SignatureMain(AbstractCryptoSignature<?> cryptoSignature, PublicKey publicKey) throws GeneralSecurityException {
        this.cryptoSignature = cryptoSignature;
        this.privateKey = null;
        this.publicKey = publicKey;
        this.reset();
    }

    @Override
    public void reset() throws GeneralSecurityException {
        if (privateKey != null) {
            getInstance().initSign(privateKey);
        }
        if (publicKey != null) {
            getInstance().initVerify(publicKey);
        }
    }

    @Override
    public byte[] sign() throws GeneralSecurityException {
        return getInstance().sign();
    }

    @Override
    public boolean verify(byte[] signature) throws GeneralSecurityException {
        Signature instance = getInstance();
        if (publicKey != null) {
            byte[] signatureNew = handleSignature(signature);
            if (signatureNew != null) {
                signature = signatureNew;
            }
            return instance.verify(signature);
        }
        if (privateKey != null) {
            return MessageDigest.isEqual(instance.sign(), signature);
        }
        return false;
    }

    @Override
    public void update(byte[] input, int offset, int len) throws GeneralSecurityException {
        getInstance().update(input, offset, len);
    }

    @Override
    public void update(ByteBuffer input) throws GeneralSecurityException {
        getInstance().update(input);
    }

    private Signature getInstance() throws GeneralSecurityException {
        return cryptoSignature.getInitSignature(null);
    }

    byte[] handleSignature(byte[] signature) {
        return signature;
    }
}
