/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 指定拦截方法
 *
 * @author : Alfred X Fu
 * @date : 2020/4/20 13:58
 */
public abstract class AbstractSpecificMethodInterceptor implements MethodInterceptor {

  protected final Object origin;

  protected AbstractSpecificMethodInterceptor(Object origin) {
    this.origin = origin;
  }

  @Override
  public final Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    if (isMatch(method, args)) {
      return doIntercept(obj, method, args, proxy);
    }
    return proxy.invokeSuper(obj, args);
  }

  /**
   * 方法是否匹配
   *
   * @param method 方法
   * @param args   参数
   * @return true为匹配否则为false
   */
  protected final boolean isMatch(Method method, Object[] args) {
    return (oneMatch(method, args) && otherMatch(method, args)) || orMatch(method, args);
  }

  /**
   * 方法是否匹配
   *
   * @param method 方法
   * @param args   参数
   * @return true为匹配否则为false
   */
  protected boolean oneMatch(Method method, Object[] args) {
    return true;
  }

  /**
   * 方法是否匹配
   *
   * @param method 方法
   * @param args   参数
   * @return true为匹配否则为false
   */
  protected boolean otherMatch(Method method, Object[] args) {
    return true;
  }

  /**
   * 方法是否匹配
   *
   * @param method 方法
   * @param args   参数
   * @return true为匹配否则为false
   */
  protected boolean orMatch(Method method, Object[] args) {
    return false;
  }

  /**
   * 所有生成的代理方法都调用此方法，而不是原始方法。 原始方法可以通过使用方法对象的普通反射调用
   *
   * @param obj    增强的对象
   * @param method 被拦截的方法
   * @param args   参数
   * @param proxy  用于调用super(非截获方法)，可以根据需要多次调用
   * @return 与代理方法的签名兼容的任何值，返回void的方法将忽略此值
   * @throws Throwable 任何异常都可能被抛出，如果是超方法将不会被调用
   * @see MethodProxy
   */
  protected Object doIntercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    return proxy.invokeSuper(obj, args);
  }
}
