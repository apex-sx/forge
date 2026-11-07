/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.net.ssl;

import javax.net.ssl.SSLSession;

/**
 * 默认主机名验证
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public class HostnameVerifier implements javax.net.ssl.HostnameVerifier {

  @Override
  public boolean verify(final String s, final SSLSession sslSession) {
    return true;
  }
}
