package potatoxf.helper.basic.db;

import org.apache.ibatis.reflection.MetaObject;
import potatoxf.helper.basic.common.entity.TableSupportMultiStatus;

import java.util.Map;

/**
 * @author potatoxf
 * @date 2022/6/29
 */
public class FieldValueSetterForMultiStatus implements FieldValueSetter {

  /**
   * 是否匹配要设置对象
   *
   * @param metaObject 对象信息
   * @return 如果匹配返回true，否则false
   */
  @Override
  public boolean isMatch(MetaObject metaObject) {
    return metaObject.getOriginalObject() instanceof TableSupportMultiStatus;
  }

  /**
   * 初始化插入值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  @Override
  public void initInsertValueMapping(Map<String, Object> container, MetaObject metaObject) {
    TableSupportMultiStatus<?, ?> originalObject =
        (TableSupportMultiStatus<?, ?>) metaObject.getOriginalObject();
    container.put(originalObject.getMultiStatusFieldName(), 0);
  }

  /**
   * 初始化更新值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  @Override
  public void initUpdateValueMapping(Map<String, Object> container, MetaObject metaObject) {}
}
