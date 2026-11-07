/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com.proxy;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 指定拦截方法
 *
 * @author : Alfred X Fu
 * @date : 2020/4/20 13:58
 */
public abstract class AbstractSpecificNameMethodInterceptor extends AbstractSpecificMethodInterceptor {

  /**
   * 匹配模式
   */
  private final Pattern matchPattern;

  public AbstractSpecificNameMethodInterceptor(Object origin, String matchRegExp) {
    super(origin);
    this.matchPattern = Pattern.compile(matchRegExp);
  }

  public AbstractSpecificNameMethodInterceptor(Object origin, Pattern matchPattern) {
    super(origin);
    this.matchPattern = matchPattern;
  }

  @Override
  protected final boolean oneMatch(Method method, Object[] args) {
    final String methodName = method.getName();
    return matchPattern.matcher(methodName).matches();
  }

  /**
   * Get Set方法拦截
   */
  public static class GetSet extends AbstractSpecificNameMethodInterceptor {

    private static final String GET_SET_REG_EXP = "^(get|set).*$";

    public GetSet(Object origin) {
      super(origin, GET_SET_REG_EXP);
    }

    protected boolean isGet(Method method) {
      return method.getName().startsWith("get");
    }

    protected boolean isSet(Method method) {
      return method.getName().startsWith("set");
    }

    protected Object isBuilderSet(Method method) {
      Class<?> returnClass = method.getReturnType();
      if (void.class.equals(returnClass)) {
        return false;
      }
      return origin;
    }
  }
}
