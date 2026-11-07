/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import com.github.potatoxf.generals.utils.security.digest.CryptoDigest;

import java.security.*;
import java.security.interfaces.ECKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;

/**
 * Crypto
 *
 * @author potatoxf
 */
public class CryptoSignatureEllipticCurve extends AbstractCryptoSignature<CryptoSignatureEllipticCurve> {

    protected final int requiredByteLength;
    protected final int fieldByteLength;
    protected final String paramSpecCurveName;

    public CryptoSignatureEllipticCurve(CryptoDigest.Builder<CryptoSignatureEllipticCurve> builder, int requiredByteLength, String paramSpecCurveName) {
        super(builder);
        this.requiredByteLength = requiredByteLength;
        this.fieldByteLength = requiredByteLength / 2;
        this.paramSpecCurveName = paramSpecCurveName;
    }

    /**
     * Transcodes the ECDSA JWS signature into ASN.1/DER format for use by
     * the JCA verifier.
     *
     * @param signature The JWS signature, consisting of the
     *                  concatenated R and S values. Must not be
     *                  {@code null}.
     * @return The ASN.1/DER encoded signature.
     * @throws RuntimeException If the ECDSA JWS signature format is invalid.
     */
    public static byte[] transcodeConcatToDER(byte[] signature) {
        try {
            int rawLen = signature.length / 2;
            int i = rawLen;
            while ((i > 0) && (signature[rawLen - i] == 0)) {
                i--;
            }
            int j = i;
            if (signature[rawLen - i] < 0) {
                j += 1;
            }
            int k = rawLen;
            while ((k > 0) && (signature[2 * rawLen - k] == 0)) {
                k--;
            }
            int l = k;
            if (signature[2 * rawLen - k] < 0) {
                l += 1;
            }
            int len = 2 + j + 2 + l;
            if (len > 255) {
                throw new RuntimeException("Invalid ECDSA signature format");
            }
            int offset;
            final byte[] derSignature;
            if (len < 128) {
                derSignature = new byte[2 + 2 + j + 2 + l];
                offset = 1;
            } else {
                derSignature = new byte[3 + 2 + j + 2 + l];
                derSignature[1] = (byte) 0x81;
                offset = 2;
            }
            derSignature[0] = 48;
            derSignature[offset++] = (byte) len;
            derSignature[offset++] = 2;
            derSignature[offset++] = (byte) j;
            System.arraycopy(signature, rawLen - i, derSignature, (offset + j) - i, i);
            offset += j;
            derSignature[offset++] = 2;
            derSignature[offset++] = (byte) l;
            System.arraycopy(signature, 2 * rawLen - k, derSignature, (offset + l) - k, k);
            return derSignature;
        } catch (Exception e) { // CVE-2022-21449 guard
            String msg = "Invalid ECDSA signature format.";
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Transcodes the JCA ASN.1/DER-encoded signature into the concatenated
     * R + S format expected by ECDSA JWS.
     *
     * @param derSignature The ASN1./DER-encoded. Must not be {@code null}.
     * @param outputLength The expected length of the ECDSA JWS signature.
     * @return The ECDSA JWS encoded signature.
     * @throws RuntimeException If the ASN.1/DER signature format is invalid.
     */
    public static byte[] transcodeDERToConcat(final byte[] derSignature, int outputLength) {
        if (derSignature.length < 8 || derSignature[0] != 48) {
            throw new RuntimeException("Invalid ECDSA signature format");
        }
        int offset;
        if (derSignature[1] > 0) {
            offset = 2;
        } else if (derSignature[1] == (byte) 0x81) {
            offset = 3;
        } else {
            throw new RuntimeException("Invalid ECDSA signature format");
        }
        byte rLength = derSignature[offset + 1];
        int i = rLength;
        while ((i > 0) && (derSignature[(offset + 2 + rLength) - i] == 0)) {
            i--;
        }
        byte sLength = derSignature[offset + 2 + rLength + 1];
        int j = sLength;
        while ((j > 0) && (derSignature[(offset + 2 + rLength + 2 + sLength) - j] == 0)) {
            j--;
        }
        int rawLen = Math.max(i, j);
        rawLen = Math.max(rawLen, outputLength / 2);
        if ((derSignature[offset - 1] & 0xff) != derSignature.length - offset
                || (derSignature[offset - 1] & 0xff) != 2 + rLength + 2 + sLength
                || derSignature[offset] != 2
                || derSignature[offset + 2 + rLength] != 2) {
            throw new RuntimeException("Invalid ECDSA signature format");
        }
        final byte[] concatSignature = new byte[2 * rawLen];
        System.arraycopy(derSignature, (offset + 2 + rLength) - i, concatSignature, rawLen - i, i);
        System.arraycopy(derSignature, (offset + 2 + rLength + 2 + sLength) - j, concatSignature, 2 * rawLen - j, j);
        return concatSignature;
    }

    /**
     * 如果枚举实例表示椭圆曲线 ECDSA 签名算法，则返回{@code true}，否则{@code false}
     */
    public boolean isEllipticCurve() {
        return "ECDSA".equalsIgnoreCase(family());
    }

    @Override
    public void assertValid(Key key, boolean signing) throws GeneralSecurityException {
        super.assertValid(key, signing);
        if (isEllipticCurve()) { //EC or RSA
            if (signing && !(key instanceof PrivateKey)) {
                throw new InvalidKeyException(" signing keys must be PrivateKey instances.");
            }
            if (isEllipticCurve()) {
                String familyName = family();
                if (!(key instanceof ECKey)) {
                    throw new InvalidKeyException(familyName + " " + keyType(signing) + " keys must be ECKey instances.");
                }
                ECKey ecKey = (ECKey) key;
                int size = ecKey.getParams().getOrder().bitLength();
                int minKeyLength = minKeyLength();
                if (size < minKeyLength) {
                    String name = value();
                    throw new GeneralSecurityException("The " + keyType(signing) + " key's size (ECParameterSpec order) is " + size +
                            " bits which is not secure enough for the " + name + " algorithm.  The JWT " +
                            "JWA Specification (RFC 7518, Section 3.4) states that keys used with " +
                            name + " MUST have a size >= " + minKeyLength + " bits.");
                }
            }
        }
    }

    @Override
    public AlgorithmParameterSpec createAlgorithmParameterSpec() throws GeneralSecurityException {
        return new ECGenParameterSpec(paramSpecCurveName);
    }

    @Override
    public SignatureVerifier getCryptoSignatureVerifier(PublicKey publicKey) throws GeneralSecurityException {
        return new SignatureEllipticCurve(this, publicKey);
    }

    @Override
    public SignatureVerifier getCryptoSignatureVerifier(PrivateKey privateKey) throws GeneralSecurityException {
        return new SignatureEllipticCurve(this, privateKey);
    }

    @Override
    public SignatureSigner getCryptoSignatureSigner(PrivateKey privateKey) throws GeneralSecurityException {
        return new SignatureEllipticCurve(this, privateKey);
    }
}
