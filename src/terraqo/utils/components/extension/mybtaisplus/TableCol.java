/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.extension.mybtaisplus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表列
 *
 * @author : Alfred X Fu
 * @date : 2020/4/23 11:06
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableCol {

  /**
   * 列名，当没有配置时，按约定命名方式转换
   *
   * @return 列名
   */
  String colName() default "";

  /**
   * 是否大小，当{@link this#colName()}没有配置时生效
   *
   * @return true大写否则false
   */
  boolean isUpper() default false;

  /**
   * 是否分组
   *
   * @return true分组否则false
   */
  boolean groupBy() default false;

  /**
   * 表格列条件
   *
   * @return {@code TableColConditionals}
   */
  TableColConditionals conditions() default @TableColConditionals();
}
