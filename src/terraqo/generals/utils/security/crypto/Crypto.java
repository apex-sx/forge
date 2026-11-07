/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.crypto;

import com.github.potatoxf.generals.utils.security.Secure;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Crypto
 *
 * @author potatoxf
 */
public abstract class Crypto<I extends Crypto<I>> extends Secure<I> {
    /**
     * 加解密计算算法
     */
    protected final String cipherAlgorithm;
    /**
     * 符合以下
     * [指定keySize=[0]]
     * [keySize>大于等于[0],keySize小于等于[1]]
     * [keySize>大于等于[0],keySize小于等于[1],指定keySize=[2]]
     */
    protected final int[][] keySizeRange;
    /**
     * KeySpec可能大小
     */
    protected final int[] keySpecSize;
    /**
     * 对keySize是否支持分段，如果存在则支持加解密进行分段处理
     * {@code int keySize->new int[]{encryptBlock,decryptBlock}}
     */
    protected final Map<Integer, int[]> keySizeCryptPart;

    public Crypto(Builder<I> builder, int[] keySpecSize, int[][] keySizeRange) {
        super(builder);
        this.cipherAlgorithm = builder.getCipherAlgorithm();
        this.keySizeCryptPart = builder.getKeySizeCryptPart();
        this.keySizeRange = keySizeRange;
        this.keySpecSize = keySpecSize;
    }

    /**
     * 对称Crypto构建器
     *
     * @author potatoxf
     */
    public static Builder<Symmetric> ofSymmetric() {
        return new Builder<Symmetric>() {
            @Override
            protected Symmetric create(int[] keySpecSize, int[][] keySizeRange) {
                return new Symmetric(this, keySpecSize, keySizeRange);
            }
        };
    }

    /**
     * 非对称Crypto构建器
     *
     * @author potatoxf
     */
    public static Builder<Asymmetric> ofAsymmetric() {
        return new Builder<Asymmetric>() {
            @Override
            protected Asymmetric create(int[] keySpecSize, int[][] keySizeRange) {
                return new Asymmetric(this, keySpecSize, keySizeRange);
            }
        };
    }

    /**
     * 获取{@code Cipher}
     *
     * @param mode {@link Cipher#ENCRYPT_MODE},{@link Cipher#DECRYPT_MODE},{@link Cipher#WRAP_MODE},{@link Cipher#UNWRAP_MODE}
     * @param key  键
     * @return {@code Cipher}
     * @throws GeneralSecurityException 如果发生异常
     */
    public final Cipher getInitCipher(int mode, Key key) throws GeneralSecurityException {
        return getInitCipher(mode, key, null, null);
    }

    /**
     * 获取{@code Cipher}
     *
     * @param mode   {@link Cipher#ENCRYPT_MODE},{@link Cipher#DECRYPT_MODE},{@link Cipher#WRAP_MODE},{@link Cipher#UNWRAP_MODE}
     * @param key    键
     * @param params {@code AlgorithmParameterSpec}
     * @param random {@code SecureRandom}
     * @return {@code Cipher}
     * @throws GeneralSecurityException 如果发生异常
     */
    public final Cipher getInitCipher(int mode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws GeneralSecurityException {
        Cipher cipher = createCipher();
        if (null != params) {
            if (null != random) {
                cipher.init(mode, key, params, random);
            } else {
                cipher.init(mode, key, params);
            }
        } else {
            if (null != random) {
                cipher.init(mode, key, random);
            } else {
                cipher.init(mode, key);
            }
        }
        return cipher;
    }

    /**
     * 是否支持分段处理
     *
     * @return 如果是返回true，否者返回false
     */
    public final boolean isSupportSegment(int keySize) {
        return keySizeCryptPart != null && !keySizeCryptPart.isEmpty() && keySizeCryptPart.containsKey(keySize);
    }

    /**
     * 获取加密块大小
     *
     * @param keySize 键大小
     * @return 返回解密块大小
     * @throws GeneralSecurityException 如果不支持，则抛出异常
     */
    public final int getEncryptSegmentSize(int keySize) throws GeneralSecurityException {
        if (!isSupportSegment(keySize)) {
            throw new GeneralSecurityException("No support segment with key size [" + keySize + "]");
        }
        return keySizeCryptPart.get(keySize)[0];
    }

    /**
     * 获取解密块大小
     *
     * @param keySize 键大小
     * @return 返回解密块大小
     * @throws GeneralSecurityException 如果不支持，则抛出异常
     */
    public final int getDecryptSegmentSize(int keySize) throws GeneralSecurityException {
        if (!isSupportSegment(keySize)) {
            throw new GeneralSecurityException("No support segment with key size [" + keySize + "]");
        }
        return keySizeCryptPart.get(keySize)[1];
    }

    /**
     * 检查KeySize大小
     *
     * @param keySize KeySize大小
     * @return KeySize大小
     * @throws GeneralSecurityException 如果不支持KeySize则抛出该异常
     */
    public final int checkKeySize(int keySize) throws GeneralSecurityException {
        if (keySize > 0 && keySizeRange != null && keySizeRange.length != 0) {
            for (int[] ints : keySizeRange) {
                if (ints.length == 1 && ints[0] == keySize) {
                    return keySize;
                } else if (ints.length == 2 && keySize >= ints[0] && keySize <= ints[1]) {
                    return keySize;
                } else if (ints.length == 3 && keySize >= ints[0] && keySize <= ints[1]) {
                    return ints[2];
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("The encryption algorithm [").append(algorithm)
                    .append("] does not support key size [").append(keySize)
                    .append("],The support list in [");
            for (int[] ints : keySizeRange) {
                if (ints.length == 1) {
                    sb.append("keySize=").append(ints[0]).append(",");
                } else if (ints.length == 2) {
                    sb.append("keySize>=").append(ints[0]).append("keySize<=").append(ints[1]).append(",");
                } else if (ints.length == 3) {
                    sb.append("keySize>=").append(ints[0]).append("keySize<=").append(ints[1]).append("=").append(ints[2]).append(",");
                }
            }
            sb.setLength(sb.length() - 1);
            sb.append("]");
            throw new GeneralSecurityException(sb.toString());
        }
        return keySize;
    }

    /**
     * 是否是PBE算法
     *
     * @return 如果是返回true，否则返回false
     */
    public boolean isPBE() {
        return algorithm.toUpperCase().contains("PBE");
    }

    /**
     * 生成{@code KeySpec}
     *
     * @param key 键
     * @return {@code KeySpec}
     */
    public KeySpec generateKeySpec(byte[] key) throws InvalidKeyException {
        if (isPBE()) {
            char[] chars = new char[key.length];
            for (int i = 0; i < key.length; i++) {
                chars[i] = (char) key[i];
            }
            return new PBEKeySpec(chars);
        } else if (algorithm.regionMatches(true, 0, "DES", 0, 3)) {
            KeySpec keySpec;
            if (algorithm.regionMatches(true, 0, "DESede", 0, 6)) {
                keySpec = new DESedeKeySpec(key);
            } else {
                keySpec = new DESKeySpec(key);
            }
            return keySpec;
        }
        return new SecretKeySpec(key, algorithm);
    }

    /**
     * 生成空的{@code SecretKeySpec}
     *
     * @return {@code SecretKeySpec}
     */
    public SecretKeySpec generateEmptySecretKeySpec() {
        SecretKeySpec secretKeySpec;
        if (keySpecSize == null || keySpecSize.length == 0) {
            secretKeySpec = new SecretKeySpec(new byte[16], algorithm);
        } else {
            secretKeySpec = new SecretKeySpec(new byte[keySpecSize[keySpecSize.length / 2]], algorithm);
        }
        return secretKeySpec;
    }

    /**
     * Crypto构建器
     */
    public abstract static class Builder<T extends Crypto<T>> extends Secure.Builder<T, Builder<T>> {
        protected String cipherAlgorithm;
        protected Map<Integer, int[]> keySizeMappingSegment;
        protected List<int[]> keySizeRangeList;
        protected IntStream.Builder keySpecSizeBuilder;
        protected int[] keySpecSizes;

        private Builder() {
        }

        /**
         * 构建{@code T extends Crypto<T>}
         *
         * @return {@code T extends Crypto<T>}
         */
        @Override
        public T build() {
            int[] keySpecSize;
            if (keySpecSizeBuilder != null) {
                keySpecSize = keySpecSizeBuilder.build().sorted().toArray();
                keySpecSizeBuilder = null;
            } else {
                keySpecSize = keySpecSizes;
            }
            int[][] keySizeRange = null;
            if (keySizeRangeList != null) {
                keySizeRangeList.sort((o1, o2) -> {
                    int compare = Integer.compare(o1.length, o2.length);
                    if (compare == 0 && o1.length >= 1) {
                        compare = Integer.compare(o1[0], o2[0]);
                        if (compare == 0 && o1.length >= 2) {
                            compare = Integer.compare(o1[1], o2[1]);
                            if (compare == 0 && o1.length >= 3) {
                                compare = Integer.compare(o1[2], o2[2]);
                            }
                        }
                    }
                    return compare;
                });
                keySizeRange = new int[keySizeRangeList.size()][];
                for (int i = 0; i < keySizeRangeList.size(); i++) {
                    keySizeRange[i] = keySizeRangeList.get(i);
                }
            }

            return create(keySpecSize, keySizeRange);
        }

        /**
         * 创建Crypto
         *
         * @param keySpecSize  keySpec
         * @param keySizeRange keySize
         * @return 返回Crypto
         */
        protected abstract T create(int[] keySpecSize, int[][] keySizeRange);

        public String getCipherAlgorithm() {
            return cipherAlgorithm;
        }

        public Builder<T> setCipherAlgorithm(String cipherAlgorithm) {
            this.cipherAlgorithm = cipherAlgorithm;
            return this;
        }

        public int[] getKeySpecSizes() {
            return keySpecSizes;
        }

        public Builder<T> addKeySizeRange(int value) {
            getKeySizeRangeList().add(new int[]{value});
            return this$();
        }

        public Builder<T> addKeySizeRange(int lo, int hi) {
            getKeySizeRangeList().add(new int[]{lo, hi});
            return this$();
        }

        public Builder<T> addKeySizeRange(int lo, int hi, int defaultValue) {
            getKeySizeRangeList().add(new int[]{lo, hi, defaultValue});
            return this$();
        }

        public Builder<T> addKeySpecSize(int value) {
            getKeySpecSizeBuilder().add(value);
            return this$();
        }

        public Builder<T> addKeySizeCryptPart(int keySize, int encryptBlock, int decryptBlock) {
            getKeySizeMappingSegment().put(keySize, new int[]{encryptBlock, decryptBlock});
            addKeySizeRange(keySize);
            return this$();
        }

        public Map<Integer, int[]> getKeySizeCryptPart() {
            return keySizeMappingSegment;
        }

        private Map<Integer, int[]> getKeySizeMappingSegment() {
            return keySizeMappingSegment == null ? (keySizeMappingSegment = new LinkedHashMap<>()) : keySizeMappingSegment;
        }

        private List<int[]> getKeySizeRangeList() {
            return keySizeRangeList == null ? (keySizeRangeList = new ArrayList<>()) : keySizeRangeList;
        }

        private IntStream.Builder getKeySpecSizeBuilder() {
            return keySpecSizeBuilder == null ? (keySpecSizeBuilder = IntStream.builder()) : keySpecSizeBuilder;
        }
    }
}
