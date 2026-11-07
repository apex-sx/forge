/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.function.InstanceSupplier;
import com.github.potatoxf.generals.lang.Initializer;
import com.github.potatoxf.generals.utils.security.crypto.Asymmetric;
import com.github.potatoxf.generals.utils.security.crypto.Crypto;
import com.github.potatoxf.generals.utils.security.crypto.CryptoService;
import com.github.potatoxf.generals.utils.security.crypto.Symmetric;
import com.github.potatoxf.generals.utils.security.digest.CryptoDigest;
import com.github.potatoxf.generals.utils.security.digest.signature.CryptoSignatureEllipticCurve;
import com.github.potatoxf.generals.utils.security.digest.signature.CryptoSignatureMain;
import com.github.potatoxf.generals.utils.security.digest.summary.CryptoSummaryMac;
import com.github.potatoxf.generals.utils.security.digest.summary.CryptoSummaryMessageDigest;

import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;

/**
 * 安全类
 *
 * @author potatoxf
 */
public final class GeneralSecurity {
    /**
     * JWA algorithm name for {@code RSASSA-PKCS-v1_5 using SHA-256}
     */
    public static final InstanceSupplier<CryptoSignatureMain> RS256 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureMain()
                    .setValue("RS256").setAlgorithm("SHA256withRSA").setFamily("RSA")
                    .setAlgorithm("RSASSA-PKCS-v1_5 using SHA-256").setJdkStandard().setDigestLength(256).setMinKeyLength(2048).build());
    /**
     * JWA algorithm name for {@code RSASSA-PKCS-v1_5 using SHA-384}
     */
    public static final InstanceSupplier<CryptoSignatureMain> RS384 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureMain()
                    .setValue("RS384").setAlgorithm("SHA384withRSA").setFamily("RSA")
                    .setAlgorithm("RSASSA-PKCS-v1_5 using SHA-384").setJdkStandard().setDigestLength(384).setMinKeyLength(2048).build());
    /**
     * JWA algorithm name for {@code RSASSA-PKCS-v1_5 using SHA-512}
     */
    public static final InstanceSupplier<CryptoSignatureMain> RS512 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureMain()
                    .setValue("RS512").setAlgorithm("SHA512withRSA").setFamily("RSA")
                    .setAlgorithm("RSASSA-PKCS-v1_5 using SHA-512").setJdkStandard().setDigestLength(512).setMinKeyLength(2048).build());
    /**
     * JWA algorithm name for {@code RSASSA-PSS using SHA-256 and MGF1 with SHA-256}.  <b>This algorithm requires
     * Java 11 or later or a JCA provider like BouncyCastle to be in the runtime classpath.</b>  If on Java 10 or
     * earlier, BouncyCastle will be used automatically if found in the runtime classpath.
     */
    public static final InstanceSupplier<CryptoSignatureMain> PS256 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureMain()
                    .setValue("PS256").setAlgorithm("RSASSA-PSS").setFamily("RSA")
                    .setAlgorithm("RSASSA-PSS using SHA-256 and MGF1 with SHA-256").setDigestLength(256).setMinKeyLength(2048).build());
    /**
     * JWA algorithm name for {@code RSASSA-PSS using SHA-384 and MGF1 with SHA-384}.  <b>This algorithm requires
     * Java 11 or later or a JCA provider like BouncyCastle to be in the runtime classpath.</b>  If on Java 10 or
     * earlier, BouncyCastle will be used automatically if found in the runtime classpath.
     */
    public static final InstanceSupplier<CryptoSignatureMain> PS384 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureMain()
                    .setValue("PS384").setAlgorithm("RSASSA-PSS").setFamily("RSA")
                    .setAlgorithm("RSASSA-PSS using SHA-384 and MGF1 with SHA-384").setDigestLength(384).setMinKeyLength(2048).build());
    /**
     * JWA algorithm name for {@code RSASSA-PSS using SHA-512 and MGF1 with SHA-512}. <b>This algorithm requires
     * Java 11 or later or a JCA provider like BouncyCastle to be in the runtime classpath.</b>  If on Java 10 or
     * earlier, BouncyCastle will be used automatically if found in the runtime classpath.
     */
    public static final InstanceSupplier<CryptoSignatureMain> PS512 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureMain()
                    .setValue("PS512").setAlgorithm("RSASSA-PSS").setFamily("RSA")
                    .setAlgorithm("RSASSA-PSS using SHA-512 and MGF1 with SHA-512").setDigestLength(512).setMinKeyLength(2048).build());
    /**
     * JWA algorithm name for {@code ECDSA using P-256 and SHA-256}
     */
    public static final InstanceSupplier<CryptoSignatureEllipticCurve> ES256 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureEllipticCurve(64, "secp256r1")
                    .setValue("ES256").setAlgorithm("SHA256withECDSA").setFamily("ECDSA")
                    .setDescription("ECDSA using P-256 and SHA-256")
                    .setJdkStandard().setDigestLength(256).setMinKeyLength(256).build());
    /**
     * JWA algorithm name for {@code ECDSA using P-384 and SHA-384}
     */
    public static final InstanceSupplier<CryptoSignatureEllipticCurve> ES384 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureEllipticCurve(96, "secp384r1")
                    .setValue("ES384").setAlgorithm("SHA384withECDSA").setFamily("ECDSA")
                    .setDescription("ECDSA using P-384 and SHA-384")
                    .setJdkStandard().setDigestLength(384).setMinKeyLength(384).build());
    /**
     * JWA algorithm name for {@code ECDSA using P-521 and SHA-512}
     */
    public static final InstanceSupplier<CryptoSignatureEllipticCurve> ES512 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSignatureEllipticCurve(132, "secp521r1")
                    .setValue("ES512").setAlgorithm("SHA512withECDSA").setFamily("ECDSA")
                    .setDescription("ECDSA using P-521 and SHA-512")
                    .setJdkStandard().setDigestLength(512).setMinKeyLength(512).build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> MD2 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("MD2").build());

    //------------------------------------------------------------------------------------------------------------------
    //信息摘要
    //------------------------------------------------------------------------------------------------------------------

    public static final InstanceSupplier<CryptoSummaryMessageDigest> MD5 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("MD5").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_1 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-1").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_224 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-224").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_256 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-256").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_384 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-384").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_512 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-512").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_512_224 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-512/224").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA_512_256 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA-512/256").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA3_224 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA3-224").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA3_256 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA3-256").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA3_384 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA3-384").build());
    public static final InstanceSupplier<CryptoSummaryMessageDigest> SHA3_512 =
            InstanceSupplier.of(() -> CryptoDigest.ofMessageDigest().setAlgorithm("SHA3-512").build());

    //------------------------------------------------------------------------------------------------------------------
    //HMAC信息摘要
    //------------------------------------------------------------------------------------------------------------------

    public static final InstanceSupplier<CryptoSummaryMac> HMAC_MD5 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HMD5").setAlgorithm("HmacMD5").setFamily("HMAC")
                    .setDescription("").setJdkStandard().setDigestLength(-1).setMinKeyLength(-1).build());
    public static final InstanceSupplier<CryptoSummaryMac> HMAC_SHA1 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HSHA1").setAlgorithm("HmacSHA1").setFamily("HMAC")
                    .setDescription("").setJdkStandard().setDigestLength(-1).setMinKeyLength(-1).build());
    public static final InstanceSupplier<CryptoSummaryMac> HMAC_SHA224 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HS244").setAlgorithm("HmacSHA224").setFamily("HMAC")
                    .setDescription("").setJdkStandard().setDigestLength(244).setMinKeyLength(244).build());
    public static final InstanceSupplier<CryptoSummaryMac> HMAC_SHA256 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HS256").setAlgorithm("HmacSHA256").setFamily("HMAC")
                    .setDescription("").setJdkStandard().setDigestLength(256).setMinKeyLength(256).build());
    public static final InstanceSupplier<CryptoSummaryMac> HMAC_SHA384 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HS384").setAlgorithm("HmacSHA384").setFamily("HMAC")
                    .setDescription("").setJdkStandard().setDigestLength(384).setMinKeyLength(384).build());
    public static final InstanceSupplier<CryptoSummaryMac> HMAC_SHA512 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HS512").setAlgorithm("HmacSHA512").setFamily("HMAC")
                    .setDescription("").setJdkStandard().setDigestLength(512).setMinKeyLength(512).build());
    /**
     * HmacSM3算法实现，需要BouncyCastle库支持
     */
    public static final InstanceSupplier<CryptoSummaryMac> HMAC_SM3 =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("HSM3").setAlgorithm("HmacSM3").setFamily("HMAC")
                    .setDescription("").setDigestLength(-1).setMinKeyLength(-1).build());
    /**
     * SM4 CMAC模式实现，需要BouncyCastle库支持
     */
    public static final InstanceSupplier<CryptoSummaryMac> SM4CMAC =
            InstanceSupplier.of(() -> CryptoDigest.ofCryptoSummaryMac().setValue("SM4CMAC").setAlgorithm("SM4CMAC").setFamily("HMAC")
                    .setDescription("").setDigestLength(-1).setMinKeyLength(-1).build());

    //------------------------------------------------------------------------------------------------------------------
    //非对称加密
    //------------------------------------------------------------------------------------------------------------------

    public static final InstanceSupplier<Asymmetric> DSA = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("DSA").setJdkStandard()
                    .addKeySizeRange(512).addKeySizeRange(1024).addKeySizeRange(2048).addKeySizeRange(4096).build());
    public static final InstanceSupplier<Asymmetric> EC = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("EC").setJdkStandard()
                    .addKeySizeRange(128).addKeySizeRange(256).build());
    public static final InstanceSupplier<Asymmetric> ECIES = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("ECIES").setJdkStandard()
                    .addKeySizeRange(128).addKeySizeRange(256).build());
    public static final InstanceSupplier<Asymmetric> SM2 = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("SM2").setJdkStandard().build());
    public static final InstanceSupplier<Asymmetric> RSA_ECB_PKCS1 = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("RSA").setCipherAlgorithm("RSA/ECB/PKCS1Padding").setJdkStandard()
                    .addKeySizeRange(512).addKeySizeRange(1024).addKeySizeRange(2048).addKeySizeRange(4096).addKeySizeRange(8192)
                    .addKeySizeCryptPart(512, 53, 64)
                    .addKeySizeCryptPart(1024, 117, 128).build());
    public static final InstanceSupplier<Asymmetric> RSA_ECB = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("RSA").setCipherAlgorithm("RSA/ECB/NoPadding").setJdkStandard().build());
    public static final InstanceSupplier<Asymmetric> RSA_NONE = InstanceSupplier.of(() ->
            Crypto.ofAsymmetric().setAlgorithm("RSA").setCipherAlgorithm("RSA/None/NoPadding").setJdkStandard().build());

    //------------------------------------------------------------------------------------------------------------------
    //对称加密
    //------------------------------------------------------------------------------------------------------------------

    public static final InstanceSupplier<Symmetric> BLOWFISH = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("Blowfish").setJdkStandard().addKeySizeRange(1, 56).build());
    public static final InstanceSupplier<Symmetric> RC2 = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("RC2").setJdkStandard().addKeySizeRange(1, 128).build());
    public static final InstanceSupplier<Symmetric> PBE_WITH_MD5_AND_DES = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("PBEWithMD5AndDES").setJdkStandard().build());
    public static final InstanceSupplier<Symmetric> PBE_WITH_SHA1_AND_DES_EDE = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("PBEWithSHA1AndDESede").setJdkStandard().build());
    public static final InstanceSupplier<Symmetric> PBE_WITH_SHA1_AND_RC2 = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("PBEWithSHA1AndRC2").setJdkStandard().build());
    public static final InstanceSupplier<Symmetric> AES = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("AES").setJdkStandard().addKeySizeRange(128)
                    .addKeySizeRange(192).addKeySizeRange(256).addKeySpecSize(16)
                    .addKeySpecSize(24).addKeySpecSize(32).build());
    public static final InstanceSupplier<Symmetric> DES = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("DES").setJdkStandard().addKeySizeRange(56)
                    .addKeySizeCryptPart(56, 56, 64).build());
    public static final InstanceSupplier<Symmetric> DES_EDE = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("DESede").setJdkStandard().addKeySizeRange(112)
                    .addKeySizeRange(168).addKeySpecSize(16).addKeySpecSize(24).build());
    public static final InstanceSupplier<Symmetric> ARC_FOUR = InstanceSupplier.of(() ->
            Crypto.ofSymmetric().setAlgorithm("ARCFOUR").setJdkStandard().build());

    private static final String BC_PROVIDER_CLASS_NAME = "org.bouncycastle.jce.provider.BouncyCastleProvider";
    /**
     * {@link #defaultRSA(boolean)}
     */
    private static final byte[] RSA_DEFAULT_PUBLIC_KEY = new byte[]{48, -126, 2, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 2, 15, 0, 48, -126, 2, 10, 2, -126, 2, 1, 0, -126, -128, -20, -8, -28, -31, 28, 64, -113, -63, 120, -34, -81, 41, -93, 85, 39, 101, -1, -95, -16, -68, -63, 26, 13, -111, -119, 43, 85, -68, -2, 92, 72, -92, -44, 86, 93, -112, 60, -32, 114, -34, 22, 29, -114, 41, 1, -126, -12, 125, 99, 94, -41, -65, -86, -71, -53, 43, 67, -14, 54, 27, -40, 95, -24, -85, 89, -126, 40, -3, 5, -106, -55, -101, 91, -62, -18, -106, -21, 74, -1, -127, 114, 126, 95, 94, 123, -59, -71, -111, 64, 61, 13, 87, 28, -24, 36, 16, 95, -38, -55, 32, 79, 57, 93, 67, 37, -118, -27, 98, -90, -19, -64, 61, 98, -48, 40, 92, -59, -116, 44, -126, -33, -77, 108, 25, -44, 8, -112, 56, -103, -63, 108, 118, -79, -123, 108, -82, -113, -45, 5, -34, -103, -6, -59, -111, 27, -80, -98, -68, 62, -95, 2, -73, 53, -37, -76, -40, 41, -79, 25, -123, -127, 55, -74, 35, 112, 82, -6, -80, -90, -9, 22, 103, 62, -59, -56, -51, -78, 33, 85, -90, 114, 49, -45, 16, -46, -116, -28, -30, -31, 81, -18, -125, -127, -107, 118, -110, -119, -79, 10, 126, -52, -14, 114, -83, -88, -4, 24, 34, -16, 115, -24, 76, -104, 119, -10, 44, 114, -78, -30, -102, -106, -62, -42, 125, 122, -122, -67, 54, 100, -22, -51, 113, -27, 89, 101, -26, -3, 62, -40, 85, 36, 51, 62, -32, 17, 109, -33, 62, 113, -38, 109, -94, 19, -24, 125, 67, -67, 40, 41, -8, -111, -10, -86, -59, 51, 90, -88, -111, -113, 44, -127, 14, 117, 92, 94, -114, -1, -18, -16, 109, 112, 71, -95, 124, 40, -75, 12, 111, 18, -87, -60, -24, -31, -29, -125, 10, 97, -8, -71, 47, -35, 20, -124, 1, 40, 127, -100, -87, -30, 124, -47, -110, -41, -76, 16, 21, 9, 87, 124, -5, 78, -105, 74, -24, -50, 13, -64, 117, 95, -105, 49, -79, 73, 93, 79, 10, -74, 58, -84, -90, 23, -60, -37, 1, 117, 104, 68, 117, 127, 1, 95, 45, -20, -35, -81, -25, -40, -57, -116, 59, -128, 106, 83, -29, 21, 55, -77, 97, -77, -112, -105, 36, -69, 81, 48, -91, -126, 68, -10, 121, 71, -48, 93, -59, 13, 53, -116, 36, -70, -66, -96, -25, -55, 15, -106, -57, 44, -18, 54, 26, -97, -80, -51, 12, 65, 39, 14, 43, 50, -114, 106, -105, 10, 59, 83, -42, 107, -124, -25, 69, 27, -30, -107, 64, 91, 11, -80, 100, -112, 69, 53, -62, -31, 9, -124, 26, 21, 3, 2, 92, 124, 34, 23, -93, -73, 108, 10, 80, 107, -35, 40, 115, 127, 4, -83, 56, 125, -11, 43, 122, -34, -91, -10, 88, 65, -112, -102, 9, -58, 123, 21, 46, -34, 87, -52, -75, 118, 0, 116, 41, -87, -127, 10, -1, 81, 25, 114, 107, 22, 1, 112, -21, 13, 113, 121, 97, 95, -54, -51, 46, -120, -78, -127, -28, 19, -42, -68, -126, 30, -97, 2, 3, 1, 0, 1};
    /**
     * {@link #defaultRSA(boolean)}
     */
    private static final byte[] RSA_DEFAULT_PRIVATE_KEY = new byte[]{48, -126, 9, 65, 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 4, -126, 9, 43, 48, -126, 9, 39, 2, 1, 0, 2, -126, 2, 1, 0, -126, -128, -20, -8, -28, -31, 28, 64, -113, -63, 120, -34, -81, 41, -93, 85, 39, 101, -1, -95, -16, -68, -63, 26, 13, -111, -119, 43, 85, -68, -2, 92, 72, -92, -44, 86, 93, -112, 60, -32, 114, -34, 22, 29, -114, 41, 1, -126, -12, 125, 99, 94, -41, -65, -86, -71, -53, 43, 67, -14, 54, 27, -40, 95, -24, -85, 89, -126, 40, -3, 5, -106, -55, -101, 91, -62, -18, -106, -21, 74, -1, -127, 114, 126, 95, 94, 123, -59, -71, -111, 64, 61, 13, 87, 28, -24, 36, 16, 95, -38, -55, 32, 79, 57, 93, 67, 37, -118, -27, 98, -90, -19, -64, 61, 98, -48, 40, 92, -59, -116, 44, -126, -33, -77, 108, 25, -44, 8, -112, 56, -103, -63, 108, 118, -79, -123, 108, -82, -113, -45, 5, -34, -103, -6, -59, -111, 27, -80, -98, -68, 62, -95, 2, -73, 53, -37, -76, -40, 41, -79, 25, -123, -127, 55, -74, 35, 112, 82, -6, -80, -90, -9, 22, 103, 62, -59, -56, -51, -78, 33, 85, -90, 114, 49, -45, 16, -46, -116, -28, -30, -31, 81, -18, -125, -127, -107, 118, -110, -119, -79, 10, 126, -52, -14, 114, -83, -88, -4, 24, 34, -16, 115, -24, 76, -104, 119, -10, 44, 114, -78, -30, -102, -106, -62, -42, 125, 122, -122, -67, 54, 100, -22, -51, 113, -27, 89, 101, -26, -3, 62, -40, 85, 36, 51, 62, -32, 17, 109, -33, 62, 113, -38, 109, -94, 19, -24, 125, 67, -67, 40, 41, -8, -111, -10, -86, -59, 51, 90, -88, -111, -113, 44, -127, 14, 117, 92, 94, -114, -1, -18, -16, 109, 112, 71, -95, 124, 40, -75, 12, 111, 18, -87, -60, -24, -31, -29, -125, 10, 97, -8, -71, 47, -35, 20, -124, 1, 40, 127, -100, -87, -30, 124, -47, -110, -41, -76, 16, 21, 9, 87, 124, -5, 78, -105, 74, -24, -50, 13, -64, 117, 95, -105, 49, -79, 73, 93, 79, 10, -74, 58, -84, -90, 23, -60, -37, 1, 117, 104, 68, 117, 127, 1, 95, 45, -20, -35, -81, -25, -40, -57, -116, 59, -128, 106, 83, -29, 21, 55, -77, 97, -77, -112, -105, 36, -69, 81, 48, -91, -126, 68, -10, 121, 71, -48, 93, -59, 13, 53, -116, 36, -70, -66, -96, -25, -55, 15, -106, -57, 44, -18, 54, 26, -97, -80, -51, 12, 65, 39, 14, 43, 50, -114, 106, -105, 10, 59, 83, -42, 107, -124, -25, 69, 27, -30, -107, 64, 91, 11, -80, 100, -112, 69, 53, -62, -31, 9, -124, 26, 21, 3, 2, 92, 124, 34, 23, -93, -73, 108, 10, 80, 107, -35, 40, 115, 127, 4, -83, 56, 125, -11, 43, 122, -34, -91, -10, 88, 65, -112, -102, 9, -58, 123, 21, 46, -34, 87, -52, -75, 118, 0, 116, 41, -87, -127, 10, -1, 81, 25, 114, 107, 22, 1, 112, -21, 13, 113, 121, 97, 95, -54, -51, 46, -120, -78, -127, -28, 19, -42, -68, -126, 30, -97, 2, 3, 1, 0, 1, 2, -126, 2, 0, 122, -56, 7, -84, 7, -46, -49, 60, -30, -61, 121, 15, -79, -89, 48, 23, 94, -80, -11, 56, -116, 1, 90, -128, 67, -127, -56, -105, 47, -36, 87, 71, -59, -60, -26, 77, -73, 75, -116, 42, -13, -121, -123, 49, 27, 93, -13, 33, 29, 27, -105, 81, 85, 20, -21, -16, 79, 121, 14, 61, 36, -79, 63, -6, -60, 49, -22, 77, -82, 120, -105, 56, -115, -57, -112, 38, -81, 72, -24, 104, -80, 73, 62, -54, 44, -71, -3, 35, 66, -6, -88, -128, -120, 59, -116, 99, -48, 2, 99, -36, -72, -37, -96, 13, -19, 115, -50, 47, -122, -81, 49, 52, -128, 104, -128, -66, 45, 43, 83, 101, 21, 63, -80, 109, -29, 103, -51, 120, -99, 15, -45, -127, 10, -90, 5, 80, -19, -74, -78, 116, 44, -110, -119, 54, 73, -120, -61, -113, -127, 88, -60, 103, -63, -31, 83, 87, -71, 38, -94, -37, 15, -110, -18, -58, -42, -93, 50, -5, -81, 6, 98, -96, 23, -64, -35, 107, 66, 8, 92, -18, 69, -30, 10, -112, 91, -18, -8, -39, 60, 67, 69, 16, 75, -94, 8, 89, -15, -85, -34, -59, -27, -124, -11, 23, -87, -45, 63, 40, -89, 32, -64, 47, -100, -65, -21, -74, -7, -78, 73, -37, -119, -99, 61, -8, 99, -54, -68, 107, 102, 59, 117, -24, -1, -113, -121, -29, 123, 31, 62, -26, 80, -84, -110, 25, 112, -81, 105, 86, -128, -14, 4, 54, 40, 109, -109, -30, 17, 107, 22, -32, 113, 31, 124, 2, -48, -41, -20, -36, -4, -82, 94, -19, -93, 97, 107, -127, 109, 126, -76, -85, 15, -13, 81, 86, 60, -97, -67, -127, -40, -45, -121, 127, -15, 101, -96, -48, -45, 85, -99, 15, -50, 49, 84, -75, 64, -118, 77, 62, -40, 100, -24, -87, -61, 12, 112, 46, -127, 3, 91, -80, 2, -112, 46, -74, -53, 47, 36, -123, -99, 18, 6, -26, -58, 35, 38, -5, 54, -9, 108, -36, 43, -121, 55, 69, 44, 49, -39, 1, 38, 92, -78, -73, -52, -89, -86, 49, 84, 108, -50, 127, -124, 1, 107, -89, -64, -96, 30, 127, -99, -36, -118, 115, 103, -8, -12, 105, -110, -5, 118, 23, 83, -109, 43, 112, -117, 85, -32, 82, 14, 126, -88, -77, -17, 34, 102, 15, 26, 14, 51, 68, 39, -11, 111, 70, 48, 3, 117, 107, -112, 117, 43, -79, -16, 116, 5, 34, -15, -57, 25, 51, 42, -30, -109, -14, 26, 83, 41, -104, 85, -6, -96, 47, -41, -8, 88, 121, 25, -74, -56, -26, -109, 15, -115, 72, 114, 102, 1, -127, -92, 7, 0, 96, -19, -33, 7, -35, -127, -109, -17, 30, 90, -116, -43, -27, -26, 125, -39, -122, 4, 125, -102, 8, -102, 93, 4, -8, 64, -76, 20, -87, 72, -2, 97, -65, -37, -19, 127, -64, 110, 64, 67, 33, 89, -58, 52, 19, 19, -39, -38, 79, 57, -51, 91, -12, -125, -97, 1, -69, -99, -94, -91, -103, 2, -126, 1, 1, 0, -70, -26, 77, 66, -51, 84, -48, 71, -5, 39, -124, 112, 118, 92, 27, 15, 19, 110, 64, -16, 89, -30, -92, 93, 64, 57, -120, 21, -77, 126, -75, 58, -69, 110, -38, -76, 7, 112, 114, -44, -70, 84, -98, -106, 50, 35, -45, 70, 74, 55, -51, 108, -55, -120, 24, -35, 100, 109, 5, 13, -61, 95, -101, 96, -62, -61, -97, -98, 23, 52, -53, 77, -21, -112, -26, -86, 12, -114, -123, 115, -29, 64, 12, -29, -119, 96, -126, -10, 82, -100, -125, 56, 45, -25, -61, 57, 58, -84, 72, 99, 98, -90, -28, -57, -14, -21, 31, -4, 51, -76, 0, -24, -126, 12, -115, -51, 54, 101, 53, -61, -56, -114, 13, 104, -11, -7, -33, -118, -7, -9, 104, 78, 95, 120, -119, -98, 48, 29, -64, 103, -38, 118, -27, -9, -41, 82, -101, -24, 123, -78, 65, -93, -2, -102, -123, 20, -80, 50, 14, 35, -120, -74, -74, 26, 107, 36, -91, 105, 117, -87, -7, -7, 56, 10, 118, -126, 75, -38, 10, -65, 72, -120, 26, -71, -30, -109, -11, 69, 92, 51, 55, -44, -85, -78, 36, 78, 120, -96, 88, 19, -78, 15, -94, 74, 13, 89, 87, 23, 46, -83, -119, -12, -9, -119, 49, -35, -8, -80, 12, -17, -25, -41, 60, 48, -9, -60, 98, 70, -65, -69, 2, 71, -65, 42, 12, -78, -88, -55, 100, -83, -50, -98, -24, 119, 118, 49, 103, -10, 69, 89, -61, 22, 14, 96, 33, -45, 2, -126, 1, 1, 0, -78, -64, -39, 34, -95, -120, 43, -57, 39, 79, -85, -65, 80, 57, -82, -118, 61, 52, -37, -44, 124, -49, -89, -4, 52, -66, -46, -70, 66, -78, 80, 111, 0, 77, -72, 91, -50, 79, -20, 84, -123, -74, -78, 28, -126, 102, -120, 102, -30, 124, -79, 65, -28, 77, 12, -64, 99, -92, -82, -91, 21, 20, 18, -106, -125, -32, -121, -50, 9, 17, 98, 32, -122, -79, -117, -79, -112, -53, 69, -63, 122, -11, -68, -30, -120, -3, -81, 41, 69, 25, -53, -31, -6, 61, 17, 9, -48, -73, 52, -44, 115, -18, -108, -108, 49, 47, 118, 75, 14, -47, 50, 96, -42, 46, -115, -6, -68, -74, -13, 92, -24, 49, 117, -106, 59, 47, -71, 127, -41, 61, 117, -93, 113, 19, 105, 32, -7, 2, -75, -52, 104, -104, 21, 24, -45, 94, 17, 3, 12, -124, -21, 21, -83, 27, -34, 77, 14, -69, 69, 66, -18, -124, -78, -30, -24, -48, -63, -124, -5, -15, 73, -17, -107, 112, -86, 83, -44, 112, 126, 76, 46, -20, -110, -79, -17, 30, 102, 1, 29, -57, 88, -7, -22, -122, 118, 117, 8, 9, 26, 66, 97, 121, -11, 48, 82, -52, -1, -14, 70, 16, 46, 52, -29, 113, 118, 30, 13, -32, -45, 90, 24, 53, 107, -3, 10, -87, 38, 94, 107, -74, 81, -54, -53, -81, 86, 98, 118, 70, 109, -59, -1, 29, 8, -57, 16, 120, -107, -84, 85, 26, -49, -72, -12, -55, -60, -123, 2, -126, 1, 0, 83, 8, -106, 50, -93, 126, -106, -70, 35, -2, -66, 124, 40, 89, 107, 60, 27, 105, -44, 55, -13, -9, 25, 6, -60, -51, -54, -125, -71, -89, 5, 100, -2, 120, -107, 121, 126, 4, -101, 63, -97, 56, -112, -20, 49, -126, 113, 14, -67, 82, 101, 85, 123, -125, -105, 104, 100, -127, 122, -55, 78, -113, 95, 60, 113, -54, -17, 83, 17, 4, 71, 126, 106, -75, -119, -124, -22, -25, 54, -86, -15, -65, 54, -6, -81, -63, -52, -19, 26, 110, -90, 109, -125, 23, -128, 72, 32, -91, 1, -44, 5, -76, 23, -40, 45, 56, 0, -111, -49, 118, -99, 32, -81, 6, 92, -55, 90, -19, 68, 33, -93, 44, 89, 16, -44, -105, 26, -107, 103, -48, 94, -22, -46, -57, 126, 124, 100, -20, 18, -37, 16, -41, -39, -5, 110, 18, 41, 105, -13, -22, -111, -9, 5, -38, 40, 93, -76, -103, -56, 102, 73, 10, -61, -124, -95, 45, 58, -65, -53, -94, -92, -121, -16, 57, -43, -60, -75, 102, 61, 120, 22, 5, 65, -70, 107, -53, 72, 32, 116, -127, -36, -64, 3, 55, -75, -95, -105, 61, -59, 99, -102, -84, 15, 113, -50, -44, 110, 56, -62, 104, 30, -18, 82, 59, 5, -120, -108, 63, -62, -65, -94, -60, -96, -35, 48, 118, 115, 34, -13, 115, 101, -29, -14, 46, 0, 78, 47, 102, -87, -17, -3, -25, -29, 103, -113, 127, 11, -83, 1, -54, -117, 61, 117, 77, -120, 57, 2, -126, 1, 0, 36, -27, -39, -20, 100, 27, -113, -83, -44, 87, -123, -60, 63, -13, 40, 44, 48, -26, 48, 90, 116, 73, 9, 11, -18, 119, 85, 88, 77, 92, 49, 110, 56, 73, -46, -68, 96, -95, -18, -50, -93, -85, 74, -88, -57, -30, 18, 85, -22, 122, -126, -68, 111, 59, -40, 93, -2, 51, -120, 57, 91, 88, -127, 93, -64, 107, 32, -99, 79, 17, -33, -121, -121, 70, 46, -25, -64, 83, -75, 90, 114, -33, 54, -42, 44, 32, -15, 127, 59, 92, 79, -47, -99, -83, -19, 78, 113, 94, -33, -16, -38, -35, 108, -98, 10, 105, 25, -87, -12, -83, 10, 17, 35, 37, 8, -7, -80, -22, -78, -31, -126, -84, 114, -15, 71, -42, 90, 28, 33, 121, -10, 70, 126, -104, -37, 34, -117, 68, 27, -14, -51, -60, 86, -42, -127, 10, -15, -56, 7, 85, -120, -111, 56, -30, 127, -56, -72, 57, -65, -38, 73, 17, -21, 59, 27, -125, -69, -18, -113, 115, 7, 40, 5, 88, -12, 79, -55, -110, 78, -1, -70, -49, -22, -104, 0, 27, -18, 66, 92, -69, 65, -118, -108, -10, -100, -115, 27, -45, -123, 48, 30, -98, -26, 21, -53, -127, -75, 115, 117, 50, -15, -42, -56, -4, 42, -67, -103, 111, -57, 48, -39, -66, 109, 68, 22, 103, 68, 28, 73, 111, -95, 2, -109, 120, -26, 3, -3, 22, 28, -94, -70, 62, 108, 9, 107, 68, -8, -32, 81, -48, -18, -1, -26, -57, -97, 97, 2, -126, 1, 0, 62, -76, -66, 120, 20, -78, 31, 72, -97, -70, 120, -49, 93, 10, 40, -101, -97, -78, 111, 1, 98, -102, -127, 52, -66, -62, -86, 43, -23, -8, -101, -93, -39, -92, 1, 114, 36, -58, -121, -49, -111, 89, 31, -106, 47, -76, 85, 118, -125, -45, -64, 13, -87, 98, 67, -74, 108, -88, -49, 37, 88, 106, -125, 80, 123, 121, -56, -119, 47, 23, -12, 55, -32, 114, 23, -73, -42, -37, -33, -34, -48, 77, -108, -89, 24, 75, -81, 25, 32, 37, 18, -38, 47, 56, 122, 19, -25, 61, 91, 11, -51, 66, 118, 64, 68, -64, -67, -127, -33, 122, -93, 96, -21, 74, -67, -30, 102, 81, 10, -45, -46, 28, -112, 78, 17, -89, -8, 14, -7, 75, -111, -46, -40, -96, 101, -2, -95, -11, -37, 116, -58, -8, -52, 29, -4, 109, 122, 72, 65, -38, 81, -117, 99, -123, 14, -55, 0, 102, 72, -99, -55, -61, -70, 102, -105, -60, -101, 86, 89, 57, -55, -102, -45, 120, -40, 45, 8, 106, 81, 15, -1, 107, -124, -111, 83, -47, 90, -73, 26, 34, 65, -8, -71, 101, -25, -41, -50, -75, -112, -2, 29, -80, -27, -9, 0, -70, 85, 28, 100, 62, -63, -92, -40, 89, 78, -50, 83, -36, -105, 59, -36, 72, 1, 39, -8, 118, -25, -112, 60, 49, 108, -39, 113, -103, 2, 47, -4, 112, 88, -67, 83, -66, 86, 60, 40, -124, -76, 70, 55, -43, -67, -57, 78, -119, 20, -36};


    public static void enableBouncyCastleIfPossible() {
        if (Initializer.isHandled(BC_PROVIDER_CLASS_NAME)) return;
        Initializer.enable(new Initializer(BC_PROVIDER_CLASS_NAME) {
            @Override
            protected void initialize() throws Throwable {
                try {
                    Class<?> clazz = Class.forName(BC_PROVIDER_CLASS_NAME);
                    //check to see if the user has already registered the BC provider:
                    Provider[] providers = Security.getProviders();

                    for (Provider provider : providers) {
                        if (clazz.isInstance(provider)) {
                            return;
                        }
                    }
                    //bc provider not enabled - add it:
                    Security.addProvider((Provider) GeneralClass.newInstance(clazz));
                } catch (Throwable e) {
                    Log.warn("Error to load '%s'", BC_PROVIDER_CLASS_NAME);
                    throw e;
                }
            }
        });
    }

    public static boolean isBouncyCastleAvailable() {
        return Initializer.isSuccess(BC_PROVIDER_CLASS_NAME);
    }

    /**
     * 获取一个默认RSA服务
     *
     * @param isPublicEncrypt 是否是公匙加密
     * @return {@code CryptoService}
     * @throws GeneralSecurityException 如果发生异常
     * @see #RSA_DEFAULT_PUBLIC_KEY
     * @see #RSA_DEFAULT_PRIVATE_KEY
     */
    public static CryptoService defaultRSA(boolean isPublicEncrypt) throws GeneralSecurityException {
        return RSA_ECB_PKCS1.get().getCryptoService(RSA_DEFAULT_PUBLIC_KEY, RSA_DEFAULT_PRIVATE_KEY, isPublicEncrypt);
    }

    /**
     * 返回默认RSA的公钥
     *
     * @return {@code String}
     * @see #RSA_DEFAULT_PUBLIC_KEY
     */
    public static String defaultRSAPublicKey() {
        return Base64.getEncoder().encodeToString(RSA_DEFAULT_PUBLIC_KEY);
    }

    /**
     * 返回默认RSA的私钥
     *
     * @return {@code CryptoService}
     * @see #RSA_DEFAULT_PRIVATE_KEY
     */
    public static String defaultRSAPrivateKey() {
        return Base64.getEncoder().encodeToString(RSA_DEFAULT_PRIVATE_KEY);
    }

    public static CryptoDigest.Builder<CryptoSignatureMain> ofCryptoSignatureMain() {
        return new CryptoDigest.Builder<CryptoSignatureMain>() {
            @Override
            public CryptoSignatureMain build() {
                return new CryptoSignatureMain(this);
            }
        };
    }

    public static CryptoDigest.Builder<CryptoSignatureEllipticCurve> ofCryptoSignatureEllipticCurve(int requiredByteLength, String paramSpecCurveName) {
        return new CryptoDigest.Builder<CryptoSignatureEllipticCurve>() {
            @Override
            public CryptoSignatureEllipticCurve build() {
                return new CryptoSignatureEllipticCurve(this, requiredByteLength, paramSpecCurveName);
            }
        };
    }

    public static CryptoDigest.Builder<CryptoSummaryMac> ofCryptoSummaryMac() {
        return new CryptoDigest.Builder<CryptoSummaryMac>() {
            @Override
            public CryptoSummaryMac build() {
                return new CryptoSummaryMac(this);
            }
        };
    }

    public static CryptoDigest.Builder<CryptoSummaryMessageDigest> ofCryptoSummaryMessageDigest() {
        return new CryptoDigest.Builder<CryptoSummaryMessageDigest>() {
            @Override
            public CryptoSummaryMessageDigest build() {
                return new CryptoSummaryMessageDigest(this);
            }
        };
    }
}
