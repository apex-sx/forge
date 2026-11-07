/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * 字节码工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 17:00
 */
public final class UtilsOfClass {

  public static Collection<Class<?>> searchClass(final Object object, final boolean isUpFound) {
    final Collection<Class<?>> classes;
    if (isUpFound) {
      classes = getSelfClasses(object);
    } else {
      classes = Collections.singletonList(getSelfClass(object));
    }
    return classes;
  }

  public static Collection<Class<?>> getSelfClasses(final Object oc) {
    final Collection<Class<?>> classes = new LinkedHashSet<>();
    if (oc == null) {
      return classes;
    }
    for (Class<?> clz = getSelfClass(oc); clz != null && !Object.class.equals(clz);
        clz = clz.getSuperclass()) {
      classes.add(clz);
    }
    classes.addAll(Arrays.asList(getSelfClass(oc).getInterfaces()));
    return classes;
  }

  public static Class<?> getSelfClass(final Object oc) {
    if (oc == null) {
      return null;
    }
    if (oc instanceof Class) {
      return (Class<?>) oc;
    }
    return oc.getClass();
  }
}
