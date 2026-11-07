package pxf.toolkit.basic.secret;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import pxf.toolkit.basic.secret.coder.EncoderException;

/**
 * 指纹处理器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public interface FingerprintHandler {

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  byte[] sign(byte[] data);

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  byte[] sign(ByteBuffer data);

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  byte[] sign(InputStream data) throws IOException;

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  byte[] sign(FileChannel data) throws IOException;

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default byte[] sign(CharSequence data) {
    return sign(data, Charset.defaultCharset());
  }

  /**
   * 生存指纹
   *
   * @param data    数据
   * @param charset 字符集
   * @return 返回指纹数据
   */
  default byte[] sign(CharSequence data, Charset charset) {
    return sign(data.toString().getBytes(charset));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(ByteBuffer data) throws EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(byte[] data) throws EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(InputStream data) throws IOException, EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(FileChannel data) throws IOException, EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(Path data) throws IOException, EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default byte[] sign(Path data) throws IOException {
    return sign(data.toFile());
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default byte[] sign(File data) throws IOException {
    return sign(new FileInputStream(data).getChannel());
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(File data) throws IOException, EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(RandomAccessFile data) throws IOException, EncoderException {
    return CodecHelper.encodeHexString(sign(data));
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default byte[] sign(RandomAccessFile data) throws IOException {
    return sign(data.getChannel());
  }

  /**
   * 生存指纹
   *
   * @param data 数据
   * @return 返回指纹数据
   */
  default String signHex(CharSequence data) throws EncoderException {
    return signHex(data, Charset.defaultCharset());
  }

  /**
   * 生存指纹
   *
   * @param data    数据
   * @param charset 字符集
   * @return 返回指纹数据
   */
  default String signHex(CharSequence data, Charset charset) throws EncoderException {
    return CodecHelper.encodeHexString(sign(data, charset));
  }
}
