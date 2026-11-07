/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.net.http;

import axf.java.common.utils.function.Constants;

/**
 * 响应常量
 *
 * @author : Alfred X Fu
 * @date : 2020/4/23 9:50
 */
public interface ResponseConstant<E extends Enum<E> & ResponseConstant<E>> extends Constants<E> {

  /**
   * 响应码
   *
   * @return 响应码
   */
  int responseCode();

  /**
   * 响应信息
   *
   * @return 响应信息
   */
  String responseMessage();
}
