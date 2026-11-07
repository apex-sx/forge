package potatoxf.helper.api;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author potatoxf
 * @date 2021/06/19
 */
public final class HelperOnCodec {
  private HelperOnCodec() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static byte[] hexEncodeToBytes(byte[] source) {
    int len = source.length;
    byte[] out = new byte[len << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < len; i++) {
      char[] chars = HelperOnNumber.digitToHex(source[i]);
      out[j++] = (byte) chars[0];
      out[j++] = (byte) chars[1];
    }
    return out;
  }

  public static String hexEncodeToString(byte[] source) {
    return new String(hexEncodeToBytes(source));
  }

  public static String hexEncodeToString(String source, Charset charset) {
    return new String(hexEncodeToBytes(source.getBytes(charset)));
  }

  public static byte[] hexDecodeToBytes(byte[] source) {
    int len = source.length;
    if (len % 2 != 0) {
      throw new IllegalArgumentException("Odd number of characters.");
    }
    byte[] out = new byte[len >> 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; j < len; i++) {
      out[i] = HelperOnNumber.hexToDigit(source[j++], source[j++]);
    }
    return out;
  }

  public static byte[] hexDecodeToBytes(String source) {
    return hexDecodeToBytes(source.getBytes());
  }

  public static String hexDecodeToString(String source, Charset charset) {
    return new String(hexDecodeToBytes(source.getBytes()), charset);
  }

  public static byte[] base64EncodeData(byte[] source) {
    return base64EncodeData(source, null);
  }

  public static byte[] base64EncodeData(byte[] source, Base64Type base64Type) {
    return getBase64Encoder(base64Type).encode(source);
  }

  public static String base64EncodeDataToString(byte[] source) {
    return base64EncodeDataToString(source, null);
  }

  public static String base64EncodeDataToString(byte[] source, Base64Type base64Type) {
    return getBase64Encoder(base64Type).encodeToString(source);
  }

  public static String base64EncodeText(String source, Charset charset) {
    return base64EncodeText(source, charset, null);
  }

  public static String base64EncodeText(String source, Charset charset, Base64Type base64Type) {
    return getBase64Encoder(base64Type).encodeToString(source.getBytes(charset));
  }

  public static byte[] base64DecodeToData(byte[] source) {
    return base64DecodeToData(source, null);
  }

  public static byte[] base64DecodeToData(byte[] source, Base64Type base64Type) {
    return getBase64Decoder(base64Type).decode(source);
  }

  public static byte[] base64DecodeDataToBytes(String source) {
    return base64DecodeDataToBytes(source, null);
  }

  public static byte[] base64DecodeDataToBytes(String source, Base64Type base64Type) {
    return getBase64Decoder(base64Type).decode(source);
  }

  public static String base64DecodeText(String source, Charset charset) {
    return base64DecodeText(source, charset, null);
  }

  public static String base64DecodeText(String source, Charset charset, Base64Type base64Type) {
    return new String(getBase64Decoder(base64Type).decode(source.getBytes()), charset);
  }

  private static Base64.Encoder getBase64Encoder(Base64Type base64Type) {
    if (base64Type == null) {
      return Base64.getEncoder();
    }
    switch (base64Type) {
      case URL_SAFE:
        return Base64.getUrlEncoder();
      case MIME_SAFE:
        return Base64.getMimeEncoder();
      default:
        return Base64.getEncoder();
    }
  }

  private static Base64.Decoder getBase64Decoder(Base64Type base64Type) {
    if (base64Type == null) {
      return Base64.getDecoder();
    }
    switch (base64Type) {
      case URL_SAFE:
        return Base64.getUrlDecoder();
      case MIME_SAFE:
        return Base64.getMimeDecoder();
      default:
        return Base64.getDecoder();
    }
  }

  /** Base64类型 */
  public enum Base64Type {
    /** Base64 */
    STANDARD,
    /** Base64WithUrl */
    URL_SAFE,
    /** Base64WithMime */
    MIME_SAFE
  }
}
