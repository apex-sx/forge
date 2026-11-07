package potatoxf.helper.crypto.digest.mac;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;

/**
 * SM4算法的MAC引擎实现
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class SM4MacEngine extends CBCBlockCipherMacEngine {

  private static final int MAC_SIZE = 128;

  /**
   * 构造
   *
   * @param params {@link CipherParameters}
   */
  public SM4MacEngine(CipherParameters params) {
    super(new SM4Engine(), MAC_SIZE, params);
  }
}
