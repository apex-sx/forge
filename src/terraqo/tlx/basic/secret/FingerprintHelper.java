package pxf.toolkit.basic.secret;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import pxf.toolkit.basic.secret.coder.EncoderException;

/**
 * 指纹助手
 *
 * @author potatoxf
 * @date 2021/5/5
 */
public final class FingerprintHelper {

  private static volatile DigestAlgorithm digestAlgorithmInstance = DigestAlgorithm.MD5;

  private FingerprintHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 设置数字加密算法
   *
   * @param digestAlgorithmInstance {@code DigestAlgorithm}
   */
  public static void setDigestAlgorithmInstance(
      DigestAlgorithm digestAlgorithmInstance) {
    if (digestAlgorithmInstance == null) {
      return;
    }
    synchronized (FingerprintHelper.class) {
      FingerprintHelper.digestAlgorithmInstance = digestAlgorithmInstance;
    }
  }

  private synchronized static FingerprintHandler getFingerprintHandler() {
    DigestAlgorithm digestAlgorithmInstance;
    synchronized (FingerprintHelper.class) {
      digestAlgorithmInstance = FingerprintHelper.digestAlgorithmInstance;
    }
    return digestAlgorithmInstance.createHandler();
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(byte[] data) {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(ByteBuffer data) {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(InputStream data) throws IOException {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(FileChannel data) throws IOException {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(Path data) throws IOException {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(File data) throws IOException {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(RandomAccessFile data) throws IOException {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static byte[] sign(CharSequence data) {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data    数据
   * @param charset 字符集
   * @return 返回指纹数据
   */
  public static byte[] sign(CharSequence data, Charset charset) {
    return getFingerprintHandler().sign(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(byte[] data) throws EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(ByteBuffer data) throws EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(InputStream data) throws IOException, EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(FileChannel data) throws IOException, EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(Path data) throws IOException, EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(File data) throws IOException, EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(RandomAccessFile data) throws IOException, EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  public static String signHex(CharSequence data) throws EncoderException {
    return getFingerprintHandler().signHex(data);
  }

  /**
   * 生存指纹
   *
   * @param data    数据
   * @param charset 字符集
   * @return 返回指纹数据
   */
  public static String signHex(CharSequence data, Charset charset) throws EncoderException {
    return getFingerprintHandler().signHex(data);
  }
}
