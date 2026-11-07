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
 * @author : Alfred X Fu
 * @date : 2020/4/23 12:02
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableColConditional {

  boolean eq() default false;

  boolean ne() default false;

  boolean gt() default false;

  boolean ge() default false;

  boolean lt() default false;

  boolean le() default false;

  boolean like() default false;

  boolean notLike() default false;

  boolean likeLeft() default false;

  boolean likeRight() default false;

  boolean isNotNull() default false;

  boolean isNull() default false;

  /**
   * 就是between and
   * <p>
   * 在类中配置： start域名，end域名
   * <p>
   * 如：time：startTime,endTime
   * <p>
   * 如：gmtTime：startGmtTime,endGmtTime
   *
   * @return true开启
   */
  boolean range() default false;
}
