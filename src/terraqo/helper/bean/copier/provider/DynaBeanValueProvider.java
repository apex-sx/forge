package potatoxf.helper.bean.copier.provider;

import potatoxf.helper.bean.DynaBean;
import potatoxf.helper.bean.copier.ValueProvider;
import potatoxf.helper.convert.Convert;

import java.lang.reflect.Type;

/**
 * DynaBean值提供者
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class DynaBeanValueProvider implements ValueProvider<String> {

  private final DynaBean dynaBean;
  private final boolean ignoreError;

  /**
   * 构造
   *
   * @param dynaBean DynaBean
   * @param ignoreError 是否忽略错误
   */
  public DynaBeanValueProvider(DynaBean dynaBean, boolean ignoreError) {
    this.dynaBean = dynaBean;
    this.ignoreError = ignoreError;
  }

  @Override
  public Object value(String key, Type valueType) {
    final Object value = dynaBean.get(key);
    return Convert.convertWithCheck(valueType, value, null, this.ignoreError);
  }

  @Override
  public boolean containsKey(String key) {
    return dynaBean.containsProp(key);
  }
}
