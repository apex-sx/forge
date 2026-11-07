/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import java.security.GeneralSecurityException;

/**
 * @author potatoxf
 */
final class SignatureServiceImpl implements SignatureService {
    private final SignatureSigner signatureSigner;
    private final SignatureVerifier signatureVerifier;

    SignatureServiceImpl(SignatureSigner signatureSigner, SignatureVerifier signatureVerifier) {
        this.signatureSigner = signatureSigner;
        this.signatureVerifier = signatureVerifier;
    }

    @Override
    public byte[] sign(byte[] data) throws GeneralSecurityException {
        signatureSigner.reset();
        signatureSigner.update(data);
        return signatureSigner.sign();
    }

    @Override
    public boolean verify(byte[] data, byte[] signature) throws GeneralSecurityException {
        signatureVerifier.reset();
        signatureVerifier.update(data);
        return signatureVerifier.verify(signature);
    }
}
