/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.crypto;

import com.github.potatoxf.generals.function.Function1Throw;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * Crypto编码
 *
 * @author potatoxf
 */
final class CryptoCoderImpl implements CryptoCoder {
    private final Crypto<?> crypto;
    private final int keySize;
    private final Function1Throw<byte[], Key, GeneralSecurityException> keyFunction;

    CryptoCoderImpl(Crypto<?> crypto, int keySize, Function1Throw<byte[], Key, GeneralSecurityException> keyFunction) {
        this.crypto = crypto;
        this.keySize = keySize;
        this.keyFunction = keyFunction;
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        return handle(data, Cipher.ENCRYPT_MODE, keyFunction.apply(key));
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        return handle(data, Cipher.DECRYPT_MODE, keyFunction.apply(key));
    }

    @Override
    public InputStream encrypt(InputStream data, byte[] key) throws GeneralSecurityException, IOException {
        return handle(data, Cipher.ENCRYPT_MODE, keyFunction.apply(key));
    }

    @Override
    public InputStream decrypt(InputStream data, byte[] key) throws GeneralSecurityException, IOException {
        return handle(data, Cipher.DECRYPT_MODE, keyFunction.apply(key));
    }

    private byte[] handle(byte[] data, int mode, Key key) throws GeneralSecurityException {
        if (crypto.isSupportSegment(keySize) && data.length >= 1024 * 1024) {
            Cipher cipher = crypto.getInitCipher(mode, key);
            final int block = getBlock(mode);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] temp = new byte[block];
            byte[] doFinal;
            // 传入数据并返回解密结果, 采用分段解密
            for (int i = 0; i < data.length; i += block) {
                // 判断是否超出 解密/加密的最大长度
                int dataLength = Math.min(data.length - i, block);
                System.arraycopy(data, i, temp, 0, dataLength);
                doFinal = cipher.doFinal(temp, 0, dataLength);
                out.write(doFinal, 0, doFinal.length);
            }
            return out.toByteArray();
        } else {
            return crypto.getInitCipher(mode, key).doFinal(data);
        }
    }

    private InputStream handle(InputStream data, int mode, Key key) throws GeneralSecurityException, IOException {
        if (crypto.isSupportSegment(keySize)) {
            Cipher cipher = crypto.getInitCipher(mode, key);
            final int block = getBlock(mode);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] temp = new byte[block];
            byte[] doFinal;
            int dataLength;
            while ((dataLength = data.read(temp)) >= 0) {
                doFinal = cipher.doFinal(temp, 0, dataLength);
                out.write(doFinal, 0, doFinal.length);
            }
            return new ByteArrayInputStream(out.toByteArray());
        } else {
            Cipher cipher = crypto.getInitCipher(mode, key);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] cache = new byte[4096];
            if (mode == Cipher.DECRYPT_MODE) {
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
                int len;
                while ((len = data.read(cache)) > 0) {
                    cipherOutputStream.write(cache, 0, len);
                }
                cipherOutputStream.close();
                return new ByteArrayInputStream(out.toByteArray());
            } else if (mode == Cipher.ENCRYPT_MODE) {
                CipherInputStream in = new CipherInputStream(data, cipher);
                int len;
                while ((len = in.read(cache)) > 0) {
                    out.write(cache, 0, len);
                }
                in.close();
                return new ByteArrayInputStream(out.toByteArray());
            } else {
                throw new GeneralSecurityException("");
            }
        }
    }

    private int getBlock(int mode) throws GeneralSecurityException {
        if (Cipher.ENCRYPT_MODE == mode || Cipher.WRAP_MODE == mode) {
            return crypto.getEncryptSegmentSize(keySize);
        } else if (Cipher.DECRYPT_MODE == mode || Cipher.UNWRAP_MODE == mode) {
            return crypto.getDecryptSegmentSize(keySize);
        }
        throw new IllegalArgumentException();
    }
}
