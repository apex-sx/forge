/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.net.http;

import axf.java.common.utils.UtilsOfJudge;
import axf.java.common.utils.components.unit.ByteUnits;
import axf.java.common.utils.function.Constants;
import axf.java.common.utils.function.Pair;
import java.time.Instant;
import java.util.Objects;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/19 22:46
 */
public class HttpHeader implements Comparable<HttpHeader> {

  private final String name;
  private final int maxArgCount;

  protected HttpHeader(String name) {
    this(name, 1);
  }

  protected HttpHeader(String name, int maxArgCount) {
    this.name = name;
    this.maxArgCount = maxArgCount;
  }

  private static String parseName(Class<?> clz) {
    StringBuilder sb = new StringBuilder();
    String name = clz.getSimpleName();
    int len = name.length();
    boolean lastUpper = false;
    for (int i = 0; i < len; i++) {
      char c = name.charAt(i);
      if (Character.isUpperCase(c)) {
        if (lastUpper) {
          sb.append(c);
        } else {
          sb.append("-").append(c);
        }
        lastUpper = true;
      } else if (Character.isLowerCase(c)) {
        sb.append(c);
        lastUpper = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public final String parse(Object... values) {
    if (values.length > maxArgCount) {
      throw new IllegalArgumentException("Arg count incorrect");
    }
    String result = parseValue(values);
    if (UtilsOfJudge.isNull(result)) {
      throw new IllegalArgumentException("Arg error");
    }
    return result;
  }

  protected Object parse(String input) {
    return parseValue(input);
  }

  /**
   * 解析值
   *
   * @param values 参数
   * @return 对应标准的字符串
   */
  protected String parseValue(Object... values) {
    return values[0].toString();
  }

  /**
   * 解析值
   *
   * @param input 输入的字符串
   * @return 返回对应的类型
   */
  protected Object parseValue(String input) {
    return input;
  }

  @Override
  public final int compareTo(HttpHeader o) {
    return name.compareTo(o.name);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HttpHeader)) {
      return false;
    }
    HttpHeader that = (HttpHeader) o;
    return name.equals(that.name);
  }

  @Override
  public final String toString() {
    return name;
  }

  /**
   * 可以请求网页实体的一个或者多个子范围字段 Accept-Ranges: bytes
   */
  public static class AcceptRanges extends HttpHeader {

    protected AcceptRanges() {
      super("Accept-Ranges", 1);
    }

    @Override
    public String parseValue(Object... values) {
      return "bytes";
    }

    @Override
    public ByteUnits parseValue(String input) {
      return Constants.parse(ByteUnits.class, input);
    }
  }

  /**
   * 指定请求和响应遵循的缓存机制 Cache-Control: no-cache
   */
  public static class CacheControl extends HttpHeader {

    protected CacheControl() {
      super("Cache-Control", 2);
    }

    @Override
    public String parseValue(Object... values) {
      String result;
      if (values.length <= 2) {
        if (values[0] instanceof String) {
          String stringValue = (String) values[0];
          if (values.length == 2) {
            long second = Long.parseLong(values[1].toString());
            result = stringValue + "=" + second;
          } else {
            result = stringValue;
          }
        } else if (values[0] instanceof CacheControlValue) {
          CacheControlValue cacheControlValue = (CacheControlValue) values[0];
          if (values.length == 1 && cacheControlValue.isMustHasArg()) {
            throw new IllegalArgumentException("Must need second arg");
          }
          String stringValue = cacheControlValue.aliases()[0];
          if (values.length == 2) {
            long second = Long.parseLong(values[1].toString());
            result = stringValue + "=" + second;
          } else {
            result = stringValue;
          }
        } else {
          throw new IllegalArgumentException("Arg of first incorrect,must String or " + CacheControlValue.class);
        }
      } else {
        throw new IllegalArgumentException("Length of arg must lesser than 2 and equal");
      }
      return result;
    }

    @Override
    public CacheControlValue parseValue(String input) {
      return Constants.parse(CacheControlValue.class, input);
    }
  }

  /**
   * 请求的与实体对应的MIME信息 Content-Type: application/x-www-form-urlencoded
   */
  public static class ContentType extends HttpHeader {

    protected ContentType() {
      super("Content-Type", 1);
    }

    @Override
    public String parseValue(Object... values) {
      return (String) values[0];
    }

    @Override
    public String parseValue(String input) {
      return input;
    }
  }

  /**
   * 请求的内容长度 Content-Length: 348
   */
  public static class ContentLength extends HttpHeader {

    protected ContentLength() {
      super("Content-Length", 1);
    }

    @Override
    public String parseValue(Object... values) {
      long value;
      if (values[0] instanceof Number) {
        Number number = (Number) values[0];
        value = number.longValue();
      } else if (values[0] instanceof String) {
        value = Long.parseLong((String) values[0]);
      } else {
        throw new IllegalArgumentException("Arg class error,must be Number or String type");
      }
      return String.valueOf(value);
    }

    @Override
    public Long parseValue(String input) {
      return Long.parseLong(input);
    }
  }

  /**
   * 请求发送的日期和时间 Date: Tue, 15 Nov 2010 08:12:31 GMT
   */
  public static class Date extends HttpHeader {

    protected Date() {
      super("Date", 1);
    }

    @Override
    public String parseValue(Object... values) {
      if (values[0] instanceof java.util.Date) {
        return values[0].toString();
      }
      if (values[0] instanceof Instant) {
        return java.util.Date.from((Instant) values[0]).toString();
      }
      return null;
    }

    @Override
    public Date parseValue(String input) {
      return null;
    }
  }

  /**
   * 用来包含实现特定的指令 Pragma: no-cache
   */
  public static class Pragma extends HttpHeader {

    protected Pragma() {
      super("Via", 1);
    }
  }

  /**
   * 通知中间网关或代理服务器地址，通信协议 Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)
   */
  public static class Via extends HttpHeader {

    protected Via() {
      super("Via", 1);
    }
  }

  /**
   * 关于消息实体的警告信息 Warn: 199 Miscellaneous warning
   */
  public static class Warning extends HttpHeader {

    protected Warning() {
      super("Warning", 2);
    }

    @Override
    protected String parseValue(Object... values) {
      return super.parseValue(values);
    }

    @Override
    protected Pair<Integer, String> parseValue(String input) {
      int index = input.indexOf(" ");
      if (index == -1) {
        return null;
      }
      String stringCode = input.substring(0, index);
      String message = input.substring(index + 1);
      int code = Integer.parseInt(stringCode);
      return Pair.of(code, message);
    }
  }
}
