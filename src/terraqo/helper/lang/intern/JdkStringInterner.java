package potatoxf.helper.lang.intern;

/**
 * JDK中默认的字符串规范化实现
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class JdkStringInterner implements Interner<String> {
  @Override
  public String intern(String sample) {
    if (null == sample) {
      return null;
    }
    return sample.intern();
  }
}
