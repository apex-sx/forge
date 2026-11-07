/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com.proxy;

import axf.java.common.utils.exception.ImpossibleOccurException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步设置Bean
 *
 * @author : Alfred X Fu
 * @date : 2020/4/20 14:22
 */
public class ConcurrentSettingProxy extends AbstractSpecificNameMethodInterceptor.GetSet {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentSettingProxy.class);
  /***
   * 目标方法
   */
  private static final String SPECIFIC_METHOD = "finishSetting";
  /**
   * 设置锁
   */
  private final ReentrantReadWriteLock settingLock = new ReentrantReadWriteLock();

  public <T extends ConcurrentSetting> ConcurrentSettingProxy(T origin) {
    super(origin);
  }

  @Override
  protected boolean orMatch(Method method, Object[] args) {
    return isSpecifyMethod(method);
  }

  @Override
  protected Object doIntercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

    if (isSpecifyMethod(method)) {
      LOGGER.trace("write unlock");
      settingLock.writeLock().unlock();
      return null;
    }
    if (isSet(method)) {
      //是否拥有写锁
      if (settingLock.isWriteLocked()) {
        //是当前前程拥有写锁
        if (settingLock.writeLock().isHeldByCurrentThread()) {
          return proxy.invokeSuper(obj, args);
        }
      }
      LOGGER.trace("write lock");
      settingLock.writeLock().lock();
      return proxy.invokeSuper(obj, args);
    }
    if (isGet(method)) {
      try {
        LOGGER.trace("read lock");
        settingLock.readLock().lock();
        return proxy.invokeSuper(obj, args);
      } finally {
        LOGGER.trace("read unlock");
        settingLock.readLock().unlock();
      }
    }
    throw new ImpossibleOccurException();
  }

  private boolean isSpecifyMethod(Method method) {
    if (SPECIFIC_METHOD.equals(method.getName())) {
      return method.getParameterCount() == 0;
    }
    return false;
  }

}
