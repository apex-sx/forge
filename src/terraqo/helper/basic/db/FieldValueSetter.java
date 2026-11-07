package potatoxf.helper.basic.db;

import org.apache.ibatis.reflection.MetaObject;
import potatoxf.helper.api.Sorted;

import java.util.Map;

/**
 * 域值设置器
 *
 * @author potatoxf
 * @date 2022/6/29
 */
public interface FieldValueSetter extends Sorted {

  /**
   * 是否匹配要设置对象
   *
   * @param metaObject 对象信息
   * @return 如果匹配返回true，否则false
   */
  default boolean isMatch(MetaObject metaObject) {
    return false;
  }

  /**
   * 初始化插入值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  void initInsertValueMapping(Map<String, Object> container, MetaObject metaObject);

  /**
   * 初始化更新值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  void initUpdateValueMapping(Map<String, Object> container, MetaObject metaObject);
}
