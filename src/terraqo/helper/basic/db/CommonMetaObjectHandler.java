package potatoxf.helper.basic.db;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.api.comparator.SortedComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 实体处理器
 *
 * <p>抽象实现用于自动化处理实体的公共字段
 *
 * @author potatoxf
 * @date 2021/5/12
 */
@SuppressWarnings("unchecked")
public class CommonMetaObjectHandler implements MetaObjectHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CommonMetaObjectHandler.class);

  private final List<FieldValueSetter> fieldValueSetterList;

  public CommonMetaObjectHandler() {
    List<FieldValueSetter> listRegistrar = new ArrayList<>();
    registerFieldValueSetter(listRegistrar);
    listRegistrar.sort(SortedComparator.RESERVE_INSTANCE);
    fieldValueSetterList = listRegistrar;
  }

  @Override
  public final void insertFill(MetaObject metaObject) {
    Map<String, Object> container = new HashMap<>(4, 1);
    for (FieldValueSetter fieldValueSetter : fieldValueSetterList) {
      if (fieldValueSetter.isMatch(metaObject)) {
        fieldValueSetter.initInsertValueMapping(container, metaObject);
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Automatic insert value: " + container);
    }
    container.forEach(
        (k, v) -> {
          if (v instanceof Supplier) {
            Object value = ((Supplier<?>) v).get();
            strictInsertFill(metaObject, k, (Class<Object>) value.getClass(), value);
          } else {
            strictInsertFill(metaObject, k, (Class<Object>) v.getClass(), v);
          }
        });
  }

  @Override
  public final void updateFill(MetaObject metaObject) {
    Map<String, Object> container = new HashMap<>();
    for (FieldValueSetter fieldValueSetter : fieldValueSetterList) {
      if (fieldValueSetter.isMatch(metaObject)) {
        fieldValueSetter.initUpdateValueMapping(container, metaObject);
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Automatic update value: " + container);
    }
    container.forEach(
        (k, v) -> {
          if (v instanceof Supplier) {
            Object value = ((Supplier<?>) v).get();
            strictUpdateFill(metaObject, k, (Class<Object>) value.getClass(), value);
          } else {
            strictUpdateFill(metaObject, k, (Class<Object>) v.getClass(), v);
          }
        });
  }

  protected void registerFieldValueSetter(List<FieldValueSetter> listRegistrar) {
    listRegistrar.add(new FieldValueSetterForPrimaryKey());
    listRegistrar.add(new FieldValueSetterForCommon());
    listRegistrar.add(new FieldValueSetterForDeleteFlag());
    listRegistrar.add(new FieldValueSetterForStatusFlag());
    listRegistrar.add(new FieldValueSetterForMultiStatus());
  }
}
