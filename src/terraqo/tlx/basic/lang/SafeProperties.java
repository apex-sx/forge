package pxf.toolkit.basic.lang;

import java.util.Map;
import java.util.Properties;

/**
 * 安全屬性，对于 {@code null}值进行检查
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public class SafeProperties extends Properties {

  public SafeProperties() {}

  public SafeProperties(int initialCapacity) {
    super(initialCapacity);
  }

  public SafeProperties(Properties defaults) {
    super(defaults);
  }

  @Override
  public synchronized Object put(Object key, Object value) {
    if (key == null || value == null) {
      return null;
    }
    return super.put(key, value);
  }

  @Override
  public synchronized void putAll(Map<?, ?> t) {
    if (t == null || t.size() == 0) {
      return;
    }
    super.putAll(t);
  }

  @Override
  public synchronized Object setProperty(String key, String value) {
    if (key == null || value == null) {
      return null;
    }
    return super.setProperty(key, value);
  }
}
