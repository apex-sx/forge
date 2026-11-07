/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.net.http;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/20 18:21
 */
public class ResponseHeader extends HttpHeader {

  protected ResponseHeader(String name) {
    super(name);
  }

  protected ResponseHeader(String name, int maxArgCount) {
    super(name, maxArgCount);
  }

  /**
   * 从原始服务器到代理缓存形成的估算时间（以秒计，非负） Age: 12
   */
  public static class Age extends ResponseHeader {

    protected Age() {
      super("Age");
    }
  }

  /**
   * 对某网络资源的有效的请求行为，不允许则返回405 Allow: GET, HEAD
   */
  public static class Allow extends ResponseHeader {

    protected Allow() {
      super("Allow");
    }
  }

  /**
   * web服务器支持的返回内容压缩编码类型。 Content-Encoding: gzip
   */
  public static class ContentEncoding extends ResponseHeader {

    protected ContentEncoding() {
      super("Content-Encoding");
    }
  }

  /**
   * 响应体的语言 Content-Language: en,zh
   */
  public static class ContentLanguage extends ResponseHeader {

    protected ContentLanguage() {
      super("Content-Language");
    }
  }

  /**
   * 请求资源可替代的备用的另一地址 Content-Location: /index.htm
   */
  public static class ContentLocation extends ResponseHeader {

    protected ContentLocation() {
      super("Content-Location");
    }
  }

  /**
   * 返回资源的MD5校验值 Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==
   */
  public static class ContentMD5 extends ResponseHeader {

    protected ContentMD5() {
      super("Content-MD5");
    }
  }

  /**
   * 在整个返回体中本部分的字节位置 Content-Range: bytes 21010-47021/47022
   */
  public static class ContentRange extends ResponseHeader {

    protected ContentRange() {
      super("Content-Range");
    }
  }

  /**
   * 请求变量的实体标签的当前值 ETag: “737060cd8c284d8af7ad3082f209582d”
   */
  public static class ETag extends ResponseHeader {

    protected ETag() {
      super("ETag");
    }
  }

  /**
   * 响应过期的日期和时间 Expires: Thu, 01 Dec 2010 16:00:00 GMT
   */
  public static class Expires extends ResponseHeader {

    protected Expires() {
      super("Expires");
    }
  }

  /**
   * 请求资源的最后修改时间 Last-Modified: Tue5 Nov 2010 12:45:26 GMT
   */
  public static class LastModified extends ResponseHeader {

    protected LastModified() {
      super("Last-Modified");
    }
  }

  /**
   * 用来重定向接收方到非请求URL的位置来完成请求或标识新的资源 Location: http://www.xxxxxx.com/archives/94.html
   */
  public static class Location extends ResponseHeader {

    protected Location() {
      super("Location");
    }
  }

  /**
   * 它指出认证方案和可应用到代理的该URL上的参数 Proxy-Authenticate: Basic
   */
  public static class ProxyAuthenticate extends ResponseHeader {

    protected ProxyAuthenticate() {
      super("Proxy-Authenticate");
    }
  }

  /**
   * 应用于重定向或一个新的资源被创造，在5秒之后重定向（由网景提出，被大部分浏览器支持） Refresh: 5; url=http://www.zcmhi.com/archives/94.html
   */
  public static class Refresh extends ResponseHeader {

    protected Refresh() {
      super("Refresh");
    }
  }

  /**
   * 如果实体暂时不可取，通知客户端在指定时间之后再次尝试 Retry-After: 120
   */
  public static class RetryAfter extends ResponseHeader {

    protected RetryAfter() {
      super("Retry-After");
    }
  }

  /**
   * web服务器软件名称 Server: Apache/1.3.27 (Unix) (Red-Hat/Linux)
   */
  public static class Server extends ResponseHeader {

    protected Server() {
      super("Server");
    }
  }

  /**
   * 设置Http Cookie Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1
   */
  public static class SetCookie extends ResponseHeader {

    protected SetCookie() {
      super("Set-Cookie");
    }
  }

  /**
   * 指出头域在分块传输编码的尾部存在 Trailer: Max-Forwards
   */
  public static class Trailer extends ResponseHeader {

    protected Trailer() {
      super("Trailer");
    }
  }

  /**
   * 文件传输编码 Transfer-Encoding:chunked
   */
  public static class TransferEncoding extends ResponseHeader {

    protected TransferEncoding() {
      super("Transfer-Encoding");
    }
  }

  /**
   * 告诉下游代理是使用缓存响应还是从原始服务器请求 Vary: *
   */
  public static class Vary extends ResponseHeader {

    protected Vary() {
      super("Vary");
    }
  }

  /**
   * 表明客户端请求实体应该使用的授权方案 WWW-Authenticate: Basic
   */
  public static class WWWAuthenticate extends ResponseHeader {

    protected WWWAuthenticate() {
      super("WWW-Authenticate");
    }
  }
}
