/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import com.github.potatoxf.generals.utils.coder.SupportCoderWithBase64;
import com.github.potatoxf.generals.utils.GeneralIO;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * 签名服务
 *
 * @author potatoxf
 */
public interface SignatureService extends SupportCoderWithBase64 {
    byte[] sign(byte[] data) throws GeneralSecurityException;

    boolean verify(byte[] data, byte[] signature) throws GeneralSecurityException;

    default byte[] sign(InputStream data) throws GeneralSecurityException, IOException {
        return this.sign(GeneralIO.read(data));
    }

    default String signToBase64(byte[] data) throws GeneralSecurityException {
        return encodeWithBase64(this.sign(data));
    }

    default String signToBase64(InputStream data) throws GeneralSecurityException, IOException {
        return encodeWithBase64(this.sign(data));
    }

    default boolean verify(InputStream data, byte[] signature) throws GeneralSecurityException, IOException {
        return this.verify(GeneralIO.read(data), signature);
    }

    default boolean verifyFromBase64(byte[] data, String signature) throws GeneralSecurityException {
        return this.verify(data, decodeWithBase64(signature));
    }

    default boolean verifyFromBase64(InputStream data, String signature) throws GeneralSecurityException, IOException {
        return this.verify(data, decodeWithBase64(signature));
    }
}
