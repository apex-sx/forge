/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * X509TrustManager
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public class X509TrustManager implements javax.net.ssl.X509TrustManager {

  @Override
  public void checkClientTrusted(final X509Certificate[] x509Certificates,
      final String s) throws CertificateException {
  }

  @Override
  public void checkServerTrusted(final X509Certificate[] x509Certificates,
      final String s) throws CertificateException {
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }
}
