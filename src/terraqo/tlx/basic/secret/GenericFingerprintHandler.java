package pxf.toolkit.basic.secret;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通用数字前面处理器
 *
 * @author potatoxf
 * @date 2021/03/12
 */
class GenericFingerprintHandler implements FingerprintHandler {

  private static final int STREAM_BUFFER_LENGTH = 8092;
  private final FingerprintCodec fingerprintCodec;

  public GenericFingerprintHandler(FingerprintCodec fingerprintCodec) {
    this.fingerprintCodec = fingerprintCodec;
  }

  @Override
  public byte[] sign(final byte[] data) {
    this.fingerprintCodec.reset();
    if (data.length == 0) {
      return new byte[0];
    }
    this.fingerprintCodec.update(data);
    return this.fingerprintCodec.finish();
  }

  @Override
  public byte[] sign(final ByteBuffer byteBuffer) {
    this.fingerprintCodec.reset();
    this.fingerprintCodec.update(byteBuffer);
    return this.fingerprintCodec.finish();
  }

  @Override
  public byte[] sign(final InputStream data) throws IOException {
    this.fingerprintCodec.reset();
    final byte[] results = new byte[STREAM_BUFFER_LENGTH];
    int readLen;
    while ((readLen = data.read(results)) > -1) {
      this.fingerprintCodec.update(results, 0, readLen);
    }
    return this.fingerprintCodec.finish();
  }

  @Override
  public byte[] sign(final FileChannel data) throws IOException {
    this.fingerprintCodec.reset();
    final ByteBuffer buffer = ByteBuffer.allocate(STREAM_BUFFER_LENGTH);
    while (data.read(buffer) > 0) {
      buffer.flip();
      this.fingerprintCodec.update(buffer);
      buffer.clear();
    }
    return this.fingerprintCodec.finish();
  }
}
