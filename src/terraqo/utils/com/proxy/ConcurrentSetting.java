/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com.proxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * 同步设置
 * <p>
 * 在并发情况下同步设置有助于配置一致
 * <p>
 * 在某一个方法通过set方法设置参数时，其它线程get方法将处于阻塞状态 直到
 *
 * @author : Alfred X Fu
 * @date : 2020/4/20 14:27
 */
public interface ConcurrentSetting {

  /**
   * 生成代理
   *
   * @param origin 原类
   * @param <T>    原类型实现了{@code ConcurrentSetting}接口
   * @return 返回代理实例
   */
  @SuppressWarnings("unchecked")
  static <T extends ConcurrentSetting> T newProxy(T origin) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(origin.getClass());
    enhancer.setCallback(new ConcurrentSettingProxy(origin));
    return (T) enhancer.create();
  }

  /**
   * 完成设置，即将要结束设置关闭同步
   * <p>
   * 在未完成设置之前，无法获取其它设置
   * <p>
   * 这样做事统一设置，在未设置完全前无法获取任何设置信息
   * <p>
   * 不需要实现该方法，该方法会在代理类中实现
   */
  default void finishSetting() {
  }
}
