package potatoxf.helper.basic.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.ibatis.reflection.MetaObject;
import potatoxf.helper.api.HelperOnClass;
import potatoxf.helper.basic.common.entity.TableSupportPrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author potatoxf
 * @date 2022/6/29
 */
public class FieldValueSetterForPrimaryKey implements FieldValueSetter {

  /**
   * 是否匹配要设置对象
   *
   * @param metaObject 对象信息
   * @return 如果匹配返回true，否则false
   */
  @Override
  public boolean isMatch(MetaObject metaObject) {
    Object originalObject = metaObject.getOriginalObject();
    if (originalObject instanceof TableSupportPrimaryKey) {
      TableSupportPrimaryKey<?, ?> tableSupportPrimaryKey =
          (TableSupportPrimaryKey<?, ?>) originalObject;

      String primaryKeyFieldName = tableSupportPrimaryKey.getPrimaryKeyFieldName();

      if (primaryKeyFieldName == null) {
        throw new IllegalArgumentException("Primary key property is not allowed to be null");
      }
      Field field = HelperOnClass.lookupField(originalObject, primaryKeyFieldName, null, Modifier.STATIC);

      if (field != null) {
        TableId tableId = field.getDeclaredAnnotation(TableId.class);
        if (tableId != null) {
          return tableId.type() == IdType.NONE;
        }
      }
    }
    return false;
  }

  /**
   * 初始化插入值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  @Override
  public void initInsertValueMapping(Map<String, Object> container, MetaObject metaObject) {
    TableSupportPrimaryKey<?, ?> originalObject =
        (TableSupportPrimaryKey<?, ?>) metaObject.getOriginalObject();
    container.put(originalObject.getPrimaryKeyFieldName(), 0);
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
