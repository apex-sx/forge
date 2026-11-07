package potatoxf.helper.api;

import potatoxf.helper.api.map.CaseInsensitiveMap;
import potatoxf.helper.api.map.UnModificationMap;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author potatoxf
 * @date 2021/9/26
 */
public enum CharsetToken {
  /** 字符集 */
  UTF8("UTF-8", "UTF-8"),
  UTF16("UTF-16", "UTF-16");
  private static final Map<String, CharsetToken> CACHE =
      HelperOnCollection.ofMapFromEnum(
          CharsetToken.class,
          new UnModificationMap<>(new CaseInsensitiveMap<>()),
          charsetToken -> charsetToken.alias);
  private final String name;
  private final String[] alias;

  CharsetToken(String name, String... alias) {
    this.name = name;
    this.alias = alias;
  }

  /**
   * 通过字符串解析{@code CharsetToken}
   *
   * @param charset charset字符串
   * @return {@code CharsetToken}
   * @throws NoSuchConstantException 如果未能解析成功返回次异常
   */
  public static CharsetToken parse(String charset) throws NoSuchConstantException {
    CharsetToken charsetToken = CACHE.get(charset);
    if (charsetToken == null) {
      throw new NoSuchConstantException("Charset not found [" + charset + "]");
    }
    return charsetToken;
  }

  public Charset get() {
    return Charset.forName(this.name);
  }

  @Override
  public String toString() {
    return name;
  }
}
