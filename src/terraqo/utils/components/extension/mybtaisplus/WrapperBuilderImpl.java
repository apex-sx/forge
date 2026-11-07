/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.extension.mybtaisplus;

import com.utils.UtilsOfJudge;
import axf.java.common.utils.UtilsOfString;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/23 13:13
 */
public class WrapperBuilderImpl<T> implements WrapperBuilder<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(WrapperBuilderImpl.class);
  private static final String RANGE_BEGIN_PREFIX = "begin";
  private static final String RANGE_END_PREFIX = "end";

  protected WrapperBuilderImpl() {
  }

  /**
   * 构建{@code QueryWrapper<T>}
   *
   * @param clz    类的Class
   * @param entity 实体
   * @return {@code QueryWrapper<T>}
   */
  @Override
  public QueryWrapper<T> buildQueryWrapper(Class<T> clz, Object entity) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    if (UtilsOfJudge.isNull(entity)) {
      return queryWrapper;
    }
    buildCondition(queryWrapper, clz, entity);
    return queryWrapper;
  }

  /**
   * 构建{@code UpdateWrapper<T>}
   *
   * @param clz    类的Class
   * @param entity 实体
   * @return {@code UpdateWrapper<T>}
   */
  @Override
  public UpdateWrapper<T> buildUpdateWrapper(Class<T> clz, Object entity) {
    UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
    if (UtilsOfJudge.isNull(entity)) {
      return updateWrapper;
    }
    buildCondition(updateWrapper, clz, entity);
    return updateWrapper;
  }

  protected void buildCondition(AbstractWrapper<T, String, ? extends AbstractWrapper<T, String, ?>> wrapper, Class<T> clz, Object entity) {
    Field[] fields = clz.getDeclaredFields();
    List<Field> collect = Arrays.stream(fields).filter(field -> UtilsOfJudge.isNoNull(field.getAnnotation(TableCol.class)))
        .collect(Collectors.toList());
    List<String> groupByList = new LinkedList<>();
    for (Field field : collect) {
      TableCol tableCol = field.getAnnotation(TableCol.class);
      String colName = tableCol.colName();
      if (UtilsOfJudge.isInvalid(colName)) {
        colName = UtilsOfString.convertUnderScoreCase(field.getName(), tableCol.isUpper());
      }
      if (tableCol.groupBy()) {
        groupByList.add(colName);
      }
      TableColConditionals conditions = tableCol.conditions();
      TableColConditional[] tableColConditionals = conditions.value();
      if (tableColConditionals.length == 1) {
        handle(wrapper, entity, colName, field, tableColConditionals[0]);
        continue;
      }
      handle(wrapper, entity, colName, field, tableColConditionals[0]);
      for (int i = 1; i < tableColConditionals.length; i++) {
        TableColConditional tableColConditional = tableColConditionals[i];
        String finalColName = colName;
        wrapper.or(aw -> handle(aw, entity, finalColName, field, tableColConditional));
      }
    }
    wrapper.groupBy(groupByList.toArray(new String[0]));
  }

  protected void handle(AbstractWrapper<T, String, ? extends AbstractWrapper<T, String, ?>> abstractWrapper, Object entity, String colName,
      Field field, TableColConditional tableColConditional) {
    Supplier<Object> valueSupplier = () -> {
      try {
        field.setAccessible(true);
        return field.get(entity);
      } catch (IllegalAccessException e) {
        LOGGER.debug(e.getMessage());
        return null;
      }
    };
    if (tableColConditional.isNotNull()) {
      handleNotNull(abstractWrapper, field, colName);
    } else if (tableColConditional.isNull()) {
      handleNull(abstractWrapper, field, colName);
    } else if (tableColConditional.eq()) {
      handleGt(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.ne()) {
      handleNoEq(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.gt()) {
      handleGt(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.ge()) {
      handleGtEq(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.lt()) {
      handleLt(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.le()) {
      handleLtEq(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.likeRight()) {
      handleLikeRight(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.likeLeft()) {
      handleLikeLeft(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.like()) {
      handleLike(abstractWrapper, field, colName, valueSupplier);
    } else if (tableColConditional.range()) {
      handleRange(abstractWrapper, field, colName, entity);
    }

  }

  protected void handleEq(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    Object value = valueSupplier.get();
    if (UtilsOfJudge.isNoNull(value)) {
      abstractWrapper.eq(colName, value);
    }
  }

  protected void handleNoEq(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    Object value = valueSupplier.get();
    if (UtilsOfJudge.isNoNull(value)) {
      abstractWrapper.ne(colName, value);
    }
  }

  protected void handleGt(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (isCompare(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value)) {
        abstractWrapper.gt(colName, value);
      }
    }
  }

  protected void handleGtEq(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (isCompare(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value)) {
        abstractWrapper.ge(colName, value);
      }
    }
  }

  protected void handleLt(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (isCompare(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value) && UtilsOfJudge.isBasic(value)) {
        abstractWrapper.lt(colName, value);
      }
    }
  }

  protected void handleLtEq(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (isCompare(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value)) {
        abstractWrapper.le(colName, value);
      }
    }
  }

  protected void handleLike(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (UtilsOfJudge.isString(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value)) {
        abstractWrapper.like(colName, value);
      }
    }
  }

  protected void handleLikeLeft(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (UtilsOfJudge.isString(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value)) {
        abstractWrapper.likeLeft(colName, value);
      }
    }
  }

  protected void handleLikeRight(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName,
      Supplier<Object> valueSupplier) {
    if (UtilsOfJudge.isString(field.getType())) {
      Object value = valueSupplier.get();
      if (UtilsOfJudge.isNoNull(value)) {
        abstractWrapper.likeRight(colName, value);
      }
    }
  }

  protected void handleNull(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName) {
    abstractWrapper.isNull(colName);
  }

  protected void handleNotNull(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName) {
    abstractWrapper.isNotNull(colName);
  }

  protected boolean isCompare(Class<?> clz) {
    if (UtilsOfJudge.isExBasic(clz)) {
      return !UtilsOfJudge.isString(clz) && !UtilsOfJudge.isBoolean(clz) && !UtilsOfJudge.isCharacter(clz);
    }
    return false;
  }

  protected void handleRange(AbstractWrapper<T, String, ?> abstractWrapper, Field field, String colName, Object entity) {
    Class<?> fieldClz = field.getType();
    if (UtilsOfJudge.isExBasic(fieldClz)) {
      try {
        String startFieldName = RANGE_BEGIN_PREFIX + UtilsOfString.convertCamelCase(field.getName(), true);
        String endFieldName = RANGE_END_PREFIX + UtilsOfString.convertCamelCase(field.getName(), true);
        try {
          Class<?> parent = entity.getClass();
          Field startField = parent.getDeclaredField(startFieldName);
          Field endField = parent.getDeclaredField(endFieldName);
          startField.setAccessible(true);
          endField.setAccessible(true);
          Object startValue = startField.get(entity);
          Object endValue = startField.get(entity);
          abstractWrapper.between(colName, startValue, endValue);
        } catch (NoSuchFieldException ignored) {
          LOGGER.debug("Don't set between and,because [" + startFieldName + "] or [" + endFieldName + "] don't exist");
        }
      } catch (IllegalAccessException e) {
        LOGGER.debug(e.getMessage());
      }
    }
  }
}
