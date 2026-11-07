package potatoxf.helper.basic.db;

import org.apache.ibatis.reflection.MetaObject;
import potatoxf.helper.basic.common.entity.TableSupportDeleteFlag;

import java.util.Map;

/**
 * @author potatoxf
 * @date 2022/6/29
 */
public class FieldValueSetterForDeleteFlag implements FieldValueSetter {

  /**
   * 是否匹配要设置对象
   *
   * @param metaObject 对象信息
   * @return 如果匹配返回true，否则false
   */
  @Override
  public boolean isMatch(MetaObject metaObject) {
    return metaObject.getOriginalObject() instanceof TableSupportDeleteFlag;
  }

  /**
   * 初始化插入值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  @Override
  public void initInsertValueMapping(Map<String, Object> container, MetaObject metaObject) {
    TableSupportDeleteFlag<?, ?> originalObject =
        (TableSupportDeleteFlag<?, ?>) metaObject.getOriginalObject();
    container.put(originalObject.getDeleteFlagFieldName(), 0);
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
