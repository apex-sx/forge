package potatoxf.helper.crypto;

import java.security.Provider;

/**
 * 全局单例的 org.bouncycastle.jce.provider.BouncyCastleProvider 对象
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public enum GlobalBouncyCastleProvider {
  INSTANCE;

  private static boolean useBouncyCastle = true;
  private Provider provider;

  GlobalBouncyCastleProvider() {
    try {
      this.provider = ProviderFactory.createBouncyCastleProvider();
    } catch (NoClassDefFoundError e) {
      // ignore
    }
  }

  /**
   * 设置是否使用Bouncy Castle库<br>
   * 如果设置为false，表示强制关闭Bouncy Castle而使用JDK
   *
   * @param isUseBouncyCastle 是否使用BouncyCastle库
   */
  public static void setUseBouncyCastle(boolean isUseBouncyCastle) {
    useBouncyCastle = isUseBouncyCastle;
  }

  /**
   * 获取{@link Provider}
   *
   * @return {@link Provider}
   */
  public Provider getProvider() {
    return useBouncyCastle ? this.provider : null;
  }
}
