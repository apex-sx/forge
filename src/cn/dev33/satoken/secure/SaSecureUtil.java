/*
 * Copyright 2020-2099 sa-token.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.dev33.satoken.secure;

import cn.dev33.satoken.error.SaErrorCode;
import cn.dev33.satoken.exception.SaTokenException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * Sa-Token 常见加密算法工具类
 *
 * @author click33
 * @since 1.14.0
 */
public class SaSecureUtil {

	private SaSecureUtil() {
	}

	/**
	 * Base64编码
	 */
	private static final Base64.Encoder encoder = Base64.getEncoder();

	/**
	 * Base64解码
	 */
	private static final Base64.Decoder decoder = Base64.getDecoder();

	// ----------------------- 摘要加密 -----------------------

	/**
	 * md5加密
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String md5(String str) {
		str = (str == null ? "" : str);
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] strA = new char[j * 2];
			int k = 0;
			for (byte byte0 : md) {
				strA[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strA[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strA);
		} catch (Exception e) {
			throw new SaTokenException(e).setCode(SaErrorCode.CODE_12111);
		}
	}

	/**
	 * sha1加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha1(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new SaTokenException(e).setCode(SaErrorCode.CODE_12112);
		}
	}

	/**
	 * sha256加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha256(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new SaTokenException(e).setCode(SaErrorCode.CODE_12113);
		}
	}

	/**
	 * sha384加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha384(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new SaTokenException(e).setCode(SaErrorCode.CODE_121131);
		}
	}

	/**
	 * sha512加密
	 *
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	public static String sha512(String str) {
		try {
			str = (str == null ? "" : str);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			return getShaHexString(str, messageDigest);
		} catch (Exception e) {
			throw new SaTokenException(e).setCode(SaErrorCode.CODE_121132);
		}
	}

	/**
	 * sha (Secure Hash Algorithm)加密 公共方法
	 *
	 * @param str 指定字符串
	 * @param messageDigest 消息摘要
	 * @return 加密后的字符串
	 */
	private static String getShaHexString(String str, MessageDigest messageDigest) {
		messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = messageDigest.digest();
		StringBuilder builder = new StringBuilder();
		String temp;
		for (byte aByte : bytes) {
			temp = Integer.toHexString(aByte & 0xFF); // 获取无符号整数十六进制字符串
			if (temp.length() == 1) {
				builder.append("0"); // 确保每个字节都用两个字符表示
			}
			builder.append(temp);
		}

		return builder.toString();
	}

	// ----------------------- 对称加密 AES -----------------------

    /**
     * 默认密码算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES加密
     *
     * @param key 加密的密钥
     * @param text 需要加密的字符串
     * @return 返回Base64转码后的加密数据
     */
    public static String aesEncrypt(String key, String text) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = text.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
            byte[] result = cipher.doFinal(byteContent);
            return encoder.encodeToString(result);
 		} catch (Exception e) {
 			throw new SaTokenException(e).setCode(SaErrorCode.CODE_12114);
 		}
    }

    /**
     * AES解密
     * @param key 加密的密钥
     * @param text 已加密的密文
     * @return 返回解密后的数据
     */
    public static String aesDecrypt(String key, String text) {
       try {
    	   Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
           cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
           byte[] result = cipher.doFinal(decoder.decode(text));
           return new String(result, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new SaTokenException(e).setCode(SaErrorCode.CODE_12115);
		}
    }

    /**
     * 生成加密秘钥
     * @param password 秘钥
     * @return SecretKeySpec
	 */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kg.init(128, random);
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

}
