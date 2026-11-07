/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.extension.mybtaisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import java.util.Objects;

/**
 * Wrapper构建器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/23 13:08
 */
public interface WrapperBuilder<T> {

  /**
   * 返回默认实例
   *
   * @param <T> 参数类型
   * @return {@code WrapperBuilder<T>}
   */
  static <T> WrapperBuilder<T> of() {
    return new WrapperBuilderImpl<>();
  }

  /**
   * 构建{@code QueryWrapper<T>}
   *
   * @param clz    类的Class
   * @param entity 实体
   * @return {@code QueryWrapper<T>}
   */
  QueryWrapper<T> buildQueryWrapper(Class<T> clz, Object entity);

  /**
   * 构建{@code UpdateWrapper<T>}
   *
   * @param clz    类的Class
   * @param entity 实体
   * @return {@code UpdateWrapper<T>}
   */
  UpdateWrapper<T> buildUpdateWrapper(Class<T> clz, Object entity);

  /**
   * 构建{@code QueryWrapper<T>}
   *
   * @param entity 实体
   * @return {@code QueryWrapper<T>}
   */
  @SuppressWarnings("unchecked")
  default QueryWrapper<T> buildQueryWrapper(Object entity) {
    return buildQueryWrapper((Class<T>) Objects.requireNonNull(entity).getClass(), entity);
  }

  /**
   * 构建{@code UpdateWrapper<T>}
   *
   * @param entity 实体
   * @return {@code UpdateWrapper<T>}
   */
  @SuppressWarnings("unchecked")
  default UpdateWrapper<T> buildUpdateWrapper(Object entity) {
    return buildUpdateWrapper((Class<T>) Objects.requireNonNull(entity).getClass(), entity);
  }
}
