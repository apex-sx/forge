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
public class RequestHeader extends HttpHeader {

  protected RequestHeader(String name) {
    super(name);
  }

  protected RequestHeader(String name, int maxArgCount) {
    super(name, maxArgCount);
  }

  /**
   * 指定客户端能够接收的内容类型 Accept: text/plain, text/html
   */
  public static class Accept extends RequestHeader {

    protected Accept() {
      super("Accept");
    }
  }

  /**
   * 浏览器可以接受的字符编码集 Accept-Charset: iso-8859-5
   */
  public static class AcceptCharset extends RequestHeader {

    protected AcceptCharset() {
      super("Accept-Charset");
    }
  }

  /**
   * 指定浏览器可以支持的web服务器返回内容压缩编码类型 Accept-Encoding: compress, gzip
   */
  public static class AcceptEncoding extends RequestHeader {

    protected AcceptEncoding() {
      super("Accept-Encoding");
    }
  }

  /**
   * 浏览器可接受的语言 Accept-Language: en,zh
   */
  public static class AcceptLanguage extends RequestHeader {

    protected AcceptLanguage() {
      super("Accept-Language");
    }
  }

  /**
   * http授权的授权证书 Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
   */
  public static class Authorization extends RequestHeader {

    protected Authorization() {
      super("Authorization");
    }
  }

  /**
   * 表示是否需要持久连接，HTTP 1.1默认进行持久连接 Connection: close
   */
  public static class Connection extends RequestHeader {

    protected Connection() {
      super("Authorization");
    }
  }

  /**
   * HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器 Cookie: $Version=1; Skin=new;
   */
  public static class Cookie extends RequestHeader {

    protected Cookie() {
      super("Authorization");
    }
  }

  /**
   * 请求的特定的服务器行为 Expect: 100-continue
   */
  public static class Expect extends RequestHeader {

    protected Expect() {
      super("Expect");
    }
  }

  /**
   * 发出请求的用户的Email From: user@email.com
   */
  public static class From extends RequestHeader {

    protected From() {
      super("From");
    }
  }

  /**
   * 指定请求的服务器的域名和端口号 Host: www.zcmhi.com
   */
  public static class Host extends RequestHeader {

    protected Host() {
      super("Host");
    }
  }

  /**
   * 只有请求内容与实体相匹配才有效 If-Match: “737060cd8c284d8af7ad3082f209582d”
   */
  public static class IfMatch extends RequestHeader {

    protected IfMatch() {
      super("If-Match");
    }
  }

  /**
   * 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码 If-Modified-Since: Sat, 29 Oct 2010 19:43:31 GMT
   */
  public static class IfModifiedSince extends RequestHeader {

    protected IfModifiedSince() {
      super("If-Modified-Since");
    }
  }

  /**
   * 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变 If-None-Match: “737060cd8c284d8af7ad3082f209582d”
   */
  public static class IfNoneMatch extends RequestHeader {

    protected IfNoneMatch() {
      super("If-NoneMatch");
    }
  }

  /**
   * 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。参数也为Etag If-Range: “737060cd8c284d8af7ad3082f209582d”
   */
  public static class IfRange extends RequestHeader {

    protected IfRange() {
      super("If-Range");
    }
  }

  /**
   * 只在实体在指定时间之后未被修改才请求成功 If-Unmodified-Since: Sat, 29 Oct 2010 19:43:31 GMT
   */
  public static class IfUnmodifiedSince extends RequestHeader {

    protected IfUnmodifiedSince() {
      super("If-Unmodified-Since");
    }
  }

  /**
   * 限制信息通过代理和网关传送的时间 Max-Forwards: 10
   */
  public static class MaxForwards extends RequestHeader {

    protected MaxForwards() {
      super("Max-Forwards");
    }
  }

  /**
   * 用来包含实现特定的指令 Pragma: no-cache
   */
  public static class Pragma extends RequestHeader {

    protected Pragma() {
      super("Pragma");
    }
  }

  /**
   * 连接到代理的授权证书 Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
   */
  public static class ProxyAuthorization extends RequestHeader {

    protected ProxyAuthorization() {
      super("Proxy-Authorization");
    }
  }

  /**
   * 只请求实体的一部分，指定范围 Range: bytes=500-999
   */
  public static class Range extends RequestHeader {

    Range() {
      super("Range");
    }
  }

  /**
   * 先前网页的地址，当前请求网页紧随其后,即来路 Referer: http://www.xxxxxx.com/archives/71.html
   */
  public static class Referer extends RequestHeader {

    protected Referer() {
      super("Referer");
    }
  }

  /**
   * 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息 TE: trailers,deflate;q=0.5
   */
  public static class TE extends RequestHeader {

    protected TE() {
      super("TE");
    }
  }

  /**
   * 向服务器指定某种传输协议以便服务器进行转换（如果支持） Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
   */
  public static class Upgrade extends RequestHeader {

    protected Upgrade() {
      super("Upgrade");
    }
  }

  /**
   * User-Agent的内容包含发出请求的用户信息 User-Agent: Mozilla/5.0 (Linux; X11)
   */
  public static class UserAgent extends RequestHeader {

    protected UserAgent() {
      super("User-Agent");
    }
  }

  public static class XRequestedWith extends RequestHeader {

    protected XRequestedWith() {
      super("X-Requested-With");
    }
  }
}
