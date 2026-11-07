package potatoxf.helper.basic.db;

import org.apache.ibatis.reflection.MetaObject;
import potatoxf.helper.api.CurrentClock;
import potatoxf.helper.api.CurrentOperator;
import potatoxf.helper.basic.common.entity.TableSupportCommon;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author potatoxf
 * @date 2022/6/29
 */
public class FieldValueSetterForCommon implements FieldValueSetter {

  private final CurrentClock currentClock;
  private final CurrentOperator currentOperator;

  public FieldValueSetterForCommon() {
    this(CurrentClock.DEFAULT_INSTANCE, CurrentOperator.DEFAULT_INSTANCE);
  }

  public FieldValueSetterForCommon(CurrentClock currentClock, CurrentOperator currentOperator) {
    this.currentClock =
        Objects.requireNonNull(currentClock, "The DateTimeEnvironment must no null");
    this.currentOperator =
        Objects.requireNonNull(currentOperator, "The OperatorEnvironment must no null");
  }

  /**
   * 是否匹配要设置对象
   *
   * @param metaObject 对象信息
   * @return 如果匹配返回true，否则false
   */
  @Override
  public boolean isMatch(MetaObject metaObject) {
    return metaObject.getOriginalObject() instanceof TableSupportCommon;
  }

  /**
   * 初始化插入值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  @Override
  public void initInsertValueMapping(Map<String, Object> container, MetaObject metaObject) {
    TableSupportCommon<?, ?> originalObject =
        (TableSupportCommon<?, ?>) metaObject.getOriginalObject();
    container.put(originalObject.getRevisionFieldName(), 0);
    container.put(
        originalObject.getCreatedByFieldName(), (Supplier<String>) currentOperator::getUsername);
    container.put(
        originalObject.getCreatedTimeFieldName(), (Supplier<Date>) currentClock::getCurrentDate);
    container.put(
        originalObject.getUpdatedByFieldName(), (Supplier<String>) currentOperator::getUsername);
    container.put(
        originalObject.getUpdatedTimeFieldName(), (Supplier<Date>) currentClock::getCurrentDate);
  }

  /**
   * 初始化更新值映射
   *
   * @param container {@code Map<String, Object>}设置值容器
   * @param metaObject 对象信息
   */
  @Override
  public void initUpdateValueMapping(Map<String, Object> container, MetaObject metaObject) {
    TableSupportCommon<?, ?> originalObject =
        (TableSupportCommon<?, ?>) metaObject.getOriginalObject();
    container.put(
        originalObject.getUpdatedByFieldName(), (Supplier<String>) currentOperator::getUsername);
    container.put(
        originalObject.getUpdatedTimeFieldName(), (Supplier<Date>) currentClock::getCurrentDate);
  }
}
