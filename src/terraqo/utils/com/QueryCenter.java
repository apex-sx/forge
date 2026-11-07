/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils Interface in Java Web Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com;

import com.utils.UtilsOfCollection;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询中心
 *
 * @author : Alfred X Fu
 */
public class QueryCenter {

  private static final Map<Class<?>, Class<?>> BASIC_TO_WRAPPER_TYPE = new HashMap<>();
  private static final Map<Class<?>, Class<?>> WRAPPER_TO_BASIC_TYPE = new HashMap<>();
  /**
   * 数据库类型转换java类型
   */
  private static final Map<String, Class<?>> DB_TYPE_TO_JAVA_TYPE = new HashMap<>();
  private static final Map<String, String> DB_TYPE_TO_JAVA_TYPE_NAME = new HashMap<>();

  static {
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(boolean.class, Boolean.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(byte.class, Byte.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(short.class, Short.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(char.class, Character.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(int.class, Integer.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(long.class, Long.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(float.class, Float.class);
    QueryCenter.BASIC_TO_WRAPPER_TYPE.put(double.class, Double.class);
    UtilsOfCollection.reserveMap(QueryCenter.BASIC_TO_WRAPPER_TYPE, QueryCenter.WRAPPER_TO_BASIC_TYPE);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("tinyint", Integer.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("smallint", Integer.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("mediumint", Integer.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("int", Integer.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("number", Integer.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("integer", Integer.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("bigint", Long.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("float", Float.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("double", Double.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("decimal", BigDecimal.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("bit", Boolean.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("char", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("varchar", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("varchar2", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("tinytext", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("text", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("mediumtext", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("longtext", String.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("time", Date.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("date", Date.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("datetime", Date.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.put("timestamp", Date.class);
    QueryCenter.DB_TYPE_TO_JAVA_TYPE.forEach(
        (k, v) -> QueryCenter.DB_TYPE_TO_JAVA_TYPE_NAME.put(k, v.getSimpleName()));
  }

  public static Iterable<Class<?>> wrapBasicTypes() {
    return Collections.unmodifiableSet(WRAPPER_TO_BASIC_TYPE.keySet());
  }
}
