package pxf.toolkit.basic.lang.collection;

import java.util.Map;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

/**
 * @author potatoxf
 * @date 2021/5/12
 */
public class Prop extends CaseInsensitiveMap<String, Object> {

  public Prop() {}

  public Prop(int initialCapacity) {
    super(initialCapacity);
  }

  public Prop(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public Prop(Map<? extends String, ?> map) {
    super(map);
  }
}
