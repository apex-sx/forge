package potatoxf.helper.api.lang;

import potatoxf.helper.api.HelperOnCollection;
import potatoxf.helper.api.HelperOnNumber;
import potatoxf.helper.api.HelperOnStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @author potatoxf
 * @date 2022/7/16
 */
public class SecretFactory {
  @Nonnull private final AlgorithmType algorithmType;
  private final int keySize;
  private final boolean isSegment;
  @Nullable private final Provider provider;
  @Nullable private final String providerName;
  @Nonnull private final SecretService[] secretServices;

  public SecretFactory(AlgorithmType algorithmType, int keySize, boolean isSegment) {
    this(algorithmType, keySize, isSegment, null, null);
  }

  public SecretFactory(
      @Nonnull AlgorithmType algorithmType,
      int keySize,
      boolean isSegment,
      @Nullable Provider provider) {
    this(algorithmType, keySize, isSegment, provider, null);
  }

  public SecretFactory(
      @Nonnull AlgorithmType algorithmType,
      int keySize,
      boolean isSegment,
      @Nullable String providerName) {
    this(algorithmType, keySize, isSegment, null, providerName);
  }

  private SecretFactory(
      @Nonnull AlgorithmType algorithmType,
      int keySize,
      boolean isSegment,
      @Nullable Provider provider,
      @Nullable String providerName) {
    if (!algorithmType.lengthBlockMap.containsKey(keySize)) {
      throw new IllegalArgumentException(
          "The encryption algorithm ["
              + algorithmType
              + "] does not support key size ["
              + keySize
              + "],The support list in "
              + algorithmType.lengthBlockMap.keySet());
    }
    this.algorithmType = algorithmType;
    this.keySize = keySize;
    this.isSegment = algorithmType.isAsymmetric || isSegment;
    this.provider = provider;
    this.providerName = providerName;
    if (algorithmType.isAsymmetric) {
      secretServices =
          new SecretService[] {
            new SecretService() {

              @Override
              public byte[] encrypt(byte[] data, byte[] key) throws SecretException {
                return handle(data, Cipher.ENCRYPT_MODE, getPublicKey(key));
              }

              @Override
              public byte[] decrypt(byte[] data, byte[] key) throws SecretException {
                return handle(data, Cipher.DECRYPT_MODE, getPublicKey(key));
              }

              @Override
              public InputStream encrypt(InputStream data, byte[] key) throws SecretException {
                return handle(data, Cipher.ENCRYPT_MODE, getPublicKey(key));
              }

              @Override
              public InputStream decrypt(InputStream data, byte[] key) throws SecretException {
                return handle(data, Cipher.DECRYPT_MODE, getPublicKey(key));
              }
            },
            new SecretService() {

              @Override
              public byte[] encrypt(byte[] data, byte[] key) throws SecretException {
                return handle(data, Cipher.ENCRYPT_MODE, getPrivateKey(key));
              }

              @Override
              public byte[] decrypt(byte[] data, byte[] key) throws SecretException {
                return handle(data, Cipher.DECRYPT_MODE, getPrivateKey(key));
              }

              @Override
              public InputStream encrypt(InputStream data, byte[] key) throws SecretException {

                return handle(data, Cipher.ENCRYPT_MODE, getPrivateKey(key));
              }

              @Override
              public InputStream decrypt(InputStream data, byte[] key) throws SecretException {
                return handle(data, Cipher.DECRYPT_MODE, getPrivateKey(key));
              }
            }
          };
    } else {
      secretServices =
          new SecretService[] {
            new SecretService() {

              @Override
              public byte[] encrypt(byte[] data, byte[] key) throws SecretException {
                return handle(data, Cipher.ENCRYPT_MODE, getSecretKey(key));
              }

              @Override
              public byte[] decrypt(byte[] data, byte[] key) throws SecretException {
                return handle(data, Cipher.DECRYPT_MODE, getSecretKey(key));
              }

              @Override
              public InputStream encrypt(InputStream data, byte[] key) throws SecretException {
                return handle(data, Cipher.ENCRYPT_MODE, getSecretKey(key));
              }

              @Override
              public InputStream decrypt(InputStream data, byte[] key) throws SecretException {
                return handle(data, Cipher.DECRYPT_MODE, getSecretKey(key));
              }
            }
          };
    }
  }

  @Nonnull
  public static AlgorithmType findAlgorithmType(@Nonnull String algorithm) {
    Optional<AlgorithmType> first =
        Arrays.stream(AlgorithmType.values())
            .filter(algorithmType -> algorithmType.algorithm.equalsIgnoreCase(algorithm))
            .findFirst();

    if (first.isPresent()) {
      return first.get();
    }
    throw new IllegalArgumentException();
  }

  public boolean isSegment() {
    return isSegment;
  }

  @Nonnull
  public <T extends AlgorithmParameterSpec> KeyPair generatePairKey(
      int keySize, @Nonnull Class<T> paramSpec) throws SecretException {
    checkAsymmetric();
    AlgorithmParameterGenerator algorithmParameterGenerator = getAlgorithmParameterGenerator();
    // 初始化参数生成器
    algorithmParameterGenerator.init(keySize);
    // 生成算法参数
    AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
    // 实例化密钥生成器
    KeyPairGenerator keyPairGenerator = getKeyPairGenerator();
    try {
      // 初始化密钥生成器
      keyPairGenerator.initialize(
          algorithmParameters.getParameterSpec(paramSpec), getSecureRandom(null));
      // 生成密钥对
      return keyPairGenerator.generateKeyPair();
    } catch (InvalidParameterSpecException | InvalidAlgorithmParameterException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  public KeyPair generatePairKey() throws SecretException {
    return generatePairKey(null);
  }

  @Nonnull
  public KeyPair generatePairKey(@Nullable byte[] seeds) throws SecretException {
    checkAsymmetric();
    // 实例化密钥生成器
    KeyPairGenerator keyPairGenerator = getKeyPairGenerator();
    // 初始化密钥生成器
    keyPairGenerator.initialize(keySize, getSecureRandom(seeds));
    // 生成密钥对
    return keyPairGenerator.generateKeyPair();
  }

  @Nonnull
  public SecretKey generateSecretKey() throws SecretException {
    return generateSecretKey(null);
  }

  @Nonnull
  public SecretKey generateSecretKey(@Nullable byte[] seeds) throws SecretException {
    checkSymmetric();
    KeyGenerator keyGenerator = getKeyGenerator();
    keyGenerator.init(keySize, getSecureRandom(seeds));
    return keyGenerator.generateKey();
  }

  @Nonnull
  public byte[] wrap(@Nonnull Key wrappingKey, @Nonnull Key key) throws SecretException {
    Cipher cipher = initCipher(Cipher.WRAP_MODE, key);
    try {
      return cipher.wrap(wrappingKey);
    } catch (IllegalBlockSizeException | InvalidKeyException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  public Key unwrap(@Nonnull byte[] wrappedKey, @Nonnull Key key) throws SecretException {
    Cipher cipher = initCipher(Cipher.UNWRAP_MODE, key);
    try {
      return cipher.unwrap(wrappedKey, getAlgorithm(), Cipher.SECRET_KEY);
    } catch (InvalidKeyException | NoSuchAlgorithmException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  public PublicKey getPublicKey(@Nonnull byte[] key) throws SecretException {
    checkAsymmetric();
    // 密钥材料转换
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
    // 实例化密钥工厂
    KeyFactory keyFactory = getKeyFactory();
    // 产生公钥
    try {
      return keyFactory.generatePublic(x509KeySpec);
    } catch (InvalidKeySpecException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  public PrivateKey getPrivateKey(@Nonnull byte[] key) throws SecretException {
    checkAsymmetric();
    // 取得私钥
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = getKeyFactory();
    // 生成私钥
    try {
      return keyFactory.generatePrivate(pkcs8KeySpec);
    } catch (InvalidKeySpecException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  public SecretKey getSecretKey(@Nonnull byte[] key) throws SecretException {
    checkSymmetric();
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, getAlgorithm());
    SecretKeyFactory keySecretKeyFactory = getSecretKeyFactory();
    try {
      return keySecretKeyFactory.generateSecret(secretKeySpec);
    } catch (InvalidKeySpecException e) {
      return secretKeySpec;
    }
  }

  @Nonnull
  public String getAlgorithm() {
    return algorithmType.algorithm;
  }

  public int getMaxEncryptBlock() {
    return algorithmType.lengthBlockMap.get(keySize)[0];
  }

  public int getMaxDecryptBlock() {
    return algorithmType.lengthBlockMap.get(keySize)[1];
  }

  @Nonnull
  public SecretService getPublicAsymmetricEncryptionService() {
    checkAsymmetric();
    return secretServices[0];
  }

  @Nonnull
  public SecretService getPrivateAsymmetricEncryptionService() {
    checkAsymmetric();
    return secretServices[1];
  }

  @Nonnull
  public SecretService getSymmetricEncryptionService() {
    checkSymmetric();
    return secretServices[0];
  }

  @Nonnull
  protected AlgorithmParameterGenerator getAlgorithmParameterGenerator() throws SecretException {
    checkAsymmetric();
    try {
      if (this.provider != null) {
        return AlgorithmParameterGenerator.getInstance(getAlgorithm(), provider);
      }
      if (this.providerName != null) {
        return AlgorithmParameterGenerator.getInstance(getAlgorithm(), providerName);
      }
      return AlgorithmParameterGenerator.getInstance(getAlgorithm());
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  protected KeyPairGenerator getKeyPairGenerator() throws SecretException {
    checkAsymmetric();
    try {
      if (this.provider != null) {
        return KeyPairGenerator.getInstance(getAlgorithm(), provider);
      }
      if (this.providerName != null) {
        return KeyPairGenerator.getInstance(getAlgorithm(), providerName);
      }
      return KeyPairGenerator.getInstance(getAlgorithm());
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  protected KeyGenerator getKeyGenerator() throws SecretException {
    checkSymmetric();
    try {
      if (this.provider != null) {
        return KeyGenerator.getInstance(getAlgorithm(), provider);
      }
      if (this.providerName != null) {
        return KeyGenerator.getInstance(getAlgorithm(), providerName);
      }
      return KeyGenerator.getInstance(getAlgorithm());
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  protected KeyFactory getKeyFactory() throws SecretException {
    checkAsymmetric();
    try {
      if (this.provider != null) {
        return KeyFactory.getInstance(getAlgorithm(), provider);
      }
      if (this.providerName != null) {
        return KeyFactory.getInstance(getAlgorithm(), providerName);
      }
      return KeyFactory.getInstance(getAlgorithm());
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  protected SecretKeyFactory getSecretKeyFactory() throws SecretException {
    checkSymmetric();
    try {
      if (this.provider != null) {
        return SecretKeyFactory.getInstance(getAlgorithm(), provider);
      }
      if (this.providerName != null) {
        return SecretKeyFactory.getInstance(getAlgorithm(), providerName);
      }
      return SecretKeyFactory.getInstance(getAlgorithm());
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  protected Cipher getCipher() throws SecretException {
    try {
      if (this.provider != null) {
        return Cipher.getInstance(getAlgorithm(), provider);
      }
      if (this.providerName != null) {
        return Cipher.getInstance(getAlgorithm(), providerName);
      }
      return Cipher.getInstance(getAlgorithm());
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  protected Cipher initCipher(int mode, @Nonnull Key key) throws SecretException {
    Cipher cipher = getCipher();
    try {
      cipher.init(mode, key);
    } catch (InvalidKeyException e) {
      throw new SecretException(e);
    }
    return cipher;
  }

  @Nullable
  protected Provider getProvider() {
    return provider;
  }

  @Nullable
  protected String getProviderName() {
    return providerName;
  }

  @Nonnull
  private SecureRandom getSecureRandom(@Nullable byte[] seeds) {
    if (seeds == null) {
      return new SecureRandom();
    }
    return new SecureRandom(seeds);
  }

  @Nonnull
  private byte[] handle(@Nonnull byte[] data, int mode, @Nonnull Key key) throws SecretException {
    try {
      if (isSegment) {
        return computeSegment(data, mode, key);
      } else {
        return computeFull(data, mode, key);
      }
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  private InputStream handle(@Nonnull InputStream data, int mode, @Nonnull Key key)
      throws SecretException {
    try {
      if (isSegment) {
        return computeSegment(data, mode, key);
      } else {
        return computeFull(data, mode, key);
      }
    } catch (BadPaddingException | IOException | IllegalBlockSizeException e) {
      throw new SecretException(e);
    }
  }

  @Nonnull
  private byte[] computeSegment(@Nonnull byte[] data, int mode, @Nonnull Key key)
      throws BadPaddingException, IllegalBlockSizeException, SecretException {
    Cipher cipher = initCipher(mode, key);
    final int block = getBlock(mode);
    ByteArrayOutputStream out =
        new ByteArrayOutputStream(HelperOnNumber.extendBinaryMultiple(data.length, block));
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
  }

  @Nonnull
  private InputStream computeSegment(@Nonnull InputStream data, int mode, @Nonnull Key key)
      throws BadPaddingException, IllegalBlockSizeException, SecretException, IOException {
    Cipher cipher = initCipher(mode, key);
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
  }

  @Nonnull
  private byte[] computeFull(@Nonnull byte[] data, int mode, @Nonnull Key key)
      throws BadPaddingException, IllegalBlockSizeException, SecretException {
    Cipher cipher = initCipher(mode, key);
    return cipher.doFinal(data);
  }

  @Nonnull
  private InputStream computeFull(@Nonnull InputStream data, int mode, @Nonnull Key key)
      throws SecretException, IOException {
    Cipher cipher = initCipher(mode, key);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    if (mode == Cipher.DECRYPT_MODE) {
      CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
      HelperOnStream.write(cipherOutputStream, data);
      cipherOutputStream.close();
      return new ByteArrayInputStream(out.toByteArray());
    } else if (mode == Cipher.ENCRYPT_MODE) {
      CipherInputStream cipherInputStream = new CipherInputStream(data, cipher);
      HelperOnStream.write(out, cipherInputStream);
      cipherInputStream.close();
      return new ByteArrayInputStream(out.toByteArray());
    } else {
      throw new SecretException();
    }
  }

  private int getBlock(int mode) {
    if (Cipher.ENCRYPT_MODE == mode || Cipher.WRAP_MODE == mode) {
      return getMaxEncryptBlock();
    } else if (Cipher.DECRYPT_MODE == mode || Cipher.UNWRAP_MODE == mode) {
      return getMaxDecryptBlock();
    }
    throw new IllegalArgumentException();
  }

  private void checkAsymmetric() {
    if (!algorithmType.isAsymmetric) {
      throw new IllegalArgumentException(
          "The encryption algorithm ["
              + algorithmType
              + "] does not support asymmetric encryption");
    }
  }

  private void checkSymmetric() {
    if (algorithmType.isAsymmetric) {
      throw new IllegalArgumentException(
          "The encryption algorithm [" + algorithmType + "] does not support symmetric encryption");
    }
  }

  public enum AlgorithmType {
    RSA(
        "RSA",
        true,
        HelperOnCollection.ofUnmodifiableMap(
            512, new int[] {53, 64},
            1024, new int[] {117, 128})),
    DES("DES", false, HelperOnCollection.ofUnmodifiableMap(56, new int[] {56, 64})),
    ;

    private final String algorithm;
    private final boolean isAsymmetric;
    private final Map<Integer, int[]> lengthBlockMap;

    AlgorithmType(String algorithm, boolean isAsymmetric, Map<Integer, int[]> lengthBlockMap) {
      this.algorithm = algorithm;
      this.isAsymmetric = isAsymmetric;
      this.lengthBlockMap = lengthBlockMap;
    }

    @Override
    public String toString() {
      return algorithm;
    }
  }
}
