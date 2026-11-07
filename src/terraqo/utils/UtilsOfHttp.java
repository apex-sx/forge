/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import com.utils.components.unit.ByteUnits;
import axf.java.common.utils.exception.FailureException;
import axf.java.common.utils.exception.NoSupportException;
import axf.java.common.utils.function.Configurator;
import axf.java.common.utils.function.Constants;
import axf.java.common.utils.function.Triple;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

/**
 * Http工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 19:41
 */
public class UtilsOfHttp {

  /**
   * HTTP连接延迟设置键
   */
  public static final String HTTP_CONNECT_TIMEOUT_KEY = "HTTP_CONNECT_TIMEOUT_KEY";
  /**
   * HTTP读取延迟设置键
   */
  public static final String HTTP_READ_TIMEOUT_KEY = "HTTP_READ_TIMEOUT_KEY";
  /**
   * 正常号码手段
   */
  private static final int NORMAL_HTTP_CODE_HEADER = 2;

  /**
   * 获取输入流
   *
   * @param url URL地址
   * @return {@code InputStream}
   * @throws IOException 如果发生异常抛出
   */
  public static InputStream issueInputStream(URL url) throws IOException {
    HttpURLConnection connection = getHttpStreamAndConfig(url, null);
    connection.connect();
    int statusCode = connection.getResponseCode();
    if (statusCode / 100 != NORMAL_HTTP_CODE_HEADER) {
      connection.disconnect();
      throw new IOException("Server exception,status code:" + statusCode);
    }
    return new BufferedInputStream(connection.getInputStream()) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          connection.disconnect();
        }
      }
    };
  }

  /**
   * 获取分段输入流
   *
   * @param url        URL地址
   * @param lowerBound 下届
   * @param upperBound 上届
   * @return {@code InputStream}
   * @throws IOException 如果发生异常抛出
   */
  public static InputStream issueInputStream(URL url, long lowerBound, long upperBound) throws IOException {
    HttpURLConnection connection = getHttpStreamAndConfig(url,
        urlConnection -> urlConnection.setRequestProperty("Range", Header.parseRange(lowerBound, upperBound)));
    connection.connect();
    int statusCode = connection.getResponseCode();
    if (HttpURLConnection.HTTP_PARTIAL != statusCode) {
      connection.disconnect();
      throw new IOException("Server exception,status code:" + statusCode);
    }
    return new BufferedInputStream(connection.getInputStream()) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          connection.disconnect();
        }
      }
    };
  }

  private static HttpURLConnection getHttpStreamAndConfig(URL url, Configurator<HttpURLConnection> extraHandler)
      throws IOException {
    final HttpURLConnection connection = UtilsOfAssert.checkIsInstance(url.openConnection(),
        HttpURLConnection.class, "No an http connect");
    int connTimeout = UtilsOfJudge.ifInvalid(System.getProperty(HTTP_CONNECT_TIMEOUT_KEY), 60000);
    int readTimeout = UtilsOfJudge.ifInvalid(System.getProperty(HTTP_READ_TIMEOUT_KEY), 60000);
    connection.setConnectTimeout(connTimeout);
    connection.setReadTimeout(readTimeout);
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Connection", "Keep-alive");
    connection.setDoInput(true);
    if (extraHandler != null) {
      extraHandler.accept(connection);
    }
    return connection;
  }

  /**
   * 根据指定的URL获取相应文件的大小
   *
   * @param url url
   * @return 返回文件字节大小，如果失败返回-1
   */
  public static long resolveFileSize(URL url) {
    if (UtilsOfJudge.isNoHumanEqual(UtilsOfConstant.HTTP, url.getProtocol())) {
      throw new NoSupportException(url.getProtocol());
    }
    HttpURLConnection conn = null;
    try {
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod(UtilsOfConstant.HEAD);
      conn.setRequestProperty("Connection", "Keep-alive");
      conn.connect();
      int statusCode = conn.getResponseCode();
      if (HttpURLConnection.HTTP_OK != statusCode) {
        throw new FailureException("Server exception,status code:" + statusCode);
      }
      return UtilsOfJudge.ifInvalid(conn.getHeaderField("Content-Length"), UtilsOfConstant.EOF);
    } catch (IOException e) {
      throw new FailureException(e);
    } finally {
      if (UtilsOfJudge.isNoNull(conn)) {
        conn.disconnect();
      }
    }
  }

  /**
   * 获取HTTP请求的IP地址
   *
   * @param request {@code HttpServletRequest}
   * @return IP地址
   */
  public static String getIpAddressByRequest(final HttpServletRequest request) {
    if (request == null) {
      return UtilsOfConstant.U_UNKNOWN;
    }
    String ip = request.getHeader("x-forwarded-for");
    if (UtilsOfJudge.isInvalid(ip) && UtilsOfConstant.UNKNOWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (UtilsOfJudge.isInvalid(ip) || UtilsOfConstant.UNKNOWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Forwarded-For");
    }
    if (UtilsOfJudge.isInvalid(ip) || UtilsOfConstant.UNKNOWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (UtilsOfJudge.isInvalid(ip) || UtilsOfConstant.UNKNOWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");
    }
    if (UtilsOfJudge.isInvalid(ip) || UtilsOfConstant.UNKNOWN.equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
  }

  public static final class Header {

    /**
     * Range格式长度
     */
    private static final int RANGE_FORMAT_LENGTH = 4;
    /**
     * Range分隔表达式
     */
    private static final String RANGE_SPLIT_REGEXP = "[ =/\\-\\\\]";

    /**
     * 解析Range
     *
     * @param header 头部信息
     * @return {@code Triple<Long, Long, Long>}起始范围，结束范围，总计数
     */
    public static Triple<Long, Long, Long> parseContentRange(String header) {
      UtilsOfAssert.checkValid(header, "Header is invalid");
      String[] strings = header.split(RANGE_SPLIT_REGEXP);
      UtilsOfAssert.checkEqual(strings.length, RANGE_FORMAT_LENGTH, "Header part format is incorrect");
      ByteUnits byteUnits = Constants.parse(ByteUnits.class, strings[0]);
      long s = Long.parseLong(strings[1]);
      long e = Long.parseLong(strings[2]);
      long t = Long.parseLong(strings[3]);
      if (!ByteUnits.B.equals(byteUnits)) {
        s = byteUnits.exactConvert(s, ByteUnits.B);
        e = byteUnits.exactConvert(e, ByteUnits.B);
        t = byteUnits.exactConvert(t, ByteUnits.B);
      }
      return Triple.of(s, e, t);
    }

    /**
     * 解析Range头部，默认字节单位
     * <p>
     * Range:(unit=first byte pos)-[last byte pos]
     *
     * @param start 起始位置
     * @param end   结束位置
     * @return 返回Range格式的字符串
     */
    public static String parseRange(long start, long end) {
      return parseRange(ByteUnits.B, start, end);
    }

    /**
     * 解析Range头部
     * <p>
     * Range:(unit=first byte pos)-[last byte pos]
     *
     * @param byteUnits 字节单位
     * @param start     起始位置
     * @param end       结束位置
     * @return 返回Range格式的字符串
     */
    public static String parseRange(ByteUnits byteUnits, long start, long end) {
      if (!ByteUnits.B.equals(byteUnits)) {
        start = byteUnits.exactConvert(start, ByteUnits.B);
        end = byteUnits.exactConvert(end, ByteUnits.B);
      }
      return ByteUnits.B.aliases()[1] + "=" + UtilsOfString.rangeString(start, end);
    }

  }
}
