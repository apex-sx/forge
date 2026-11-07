/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils Context
 * <p>
 * 工具类常量
 *
 * @author : Alfred X Fu
 */
public final class UtilsOfConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(UtilsOfConstant.class);
  /**
   * 初始hash值
   */
  public static final int INITIAL_HASH = 7;
  /**
   * java的乘数 1<<5-1
   */
  public static final int MULTIPLIER = 31;
  /**
   * 空的对象数组
   */
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  /**
   * 基础类路径
   */
  public static final String BASIC_CLASS_PATH = UtilsOfResource.class.getResource("/").getPath();
  /**
   * 临时目录
   */
  public static final String TEMP_DIR_PATH = getTempDirectoryPath();
  /**
   * 用户目录
   */
  public static final String USER_DIR_PATH = getUserDirectoryPath();
  public static final String EMPTY = "";
  public static final String[] EMPTY_STRING_ARRAY = {};
  public static final String NULL = "null";
  public static final String S_GET = "get";
  public static final String S_SET = "set";
  public static final String S_IS = "is";
  public static final String UNKNOWN = "UNKNOWN";
  public static final String U_UNKNOWN = "Unknown";
  public static final String L_UNKNOWN = "unknown";
  public static final String SUCCESS = "SUCCESS";
  public static final String U_SUCCESS = "Success";
  public static final String L_SUCCESS = "success";
  public static final String FAILURE = "FAILURE";
  public static final String U_FAILURE = "Failure";
  public static final String L_FAILURE = "failure";
  public static final String CORRECT = "CORRECT";
  public static final String U_CORRECT = "Correct";
  public static final String L_CORRECT = "correct";
  public static final String ERROR = "ERROR";
  public static final String U_ERROR = "Error";
  public static final String L_ERROR = "error";
  public static final String FILE_PREFIX = "file:";
  public static final String CLASS_PATH_PREFIX = "classpath:";
  public static final String CLASS_PATHS_PREFIX = "classpath*:";
  public static final String DESCEND = "desc";
  public static final String ASCEND = "asc";
  public static final String HTTP = "HTTP";
  public static final String HTTPS = "HTTPS";
  /**
   * 请求指定的页面信息，并返回实体主体。
   */
  public static final String GET = "GET";
  /**
   * 类似于 GET 请求，只不过返回的响应中没有具体的内容，用于获取报头
   */
  public static final String HEAD = "HEAD";
  /**
   * 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST 请求可能会导致新的资源的建立和/或已有资源的修改。
   */
  public static final String POST = "POST";
  /**
   * 从客户端向服务器传送的数据取代指定的文档的内容。
   */
  public static final String PUT = "PUT";
  /**
   * 请求服务器删除指定的页面。
   */
  public static final String DELETE = "DELETE";
  /**
   * HTTP/1.1 协议中预留给能够将连接改为管道方式的代理服务器。
   */
  public static final String CONNECT = "CONNECT";
  /**
   * 允许客户端查看服务器的性能。
   */
  public static final String OPTIONS = "OPTIONS";
  /**
   * 回显服务器收到的请求，主要用于测试或诊断。
   */
  public static final String TRACE = "TRACE";
  /**
   * 是对 PUT 方法的补充，用来对已知资源进行局部更新 。
   */
  public static final String PATCH = "PATCH";
  public static final int EOF = -1;
  public static final int ZERO = 0;
  public static final int ONE = 1;
  public static final int TWO = 2;
  public static final int THREE = 3;
  public static final int FOUR = 4;
  public static final int FIVE = 5;
  public static final int SIX = 6;
  public static final int SEVEN = 7;
  public static final int EIGHT = 8;
  public static final int NINE = 8;
  public static final int TAN = 10;
  public static final int DECIMAL_PLACES_2 = TWO;
  public static final int DECIMAL_PLACES_4 = FOUR;
  public static final int DECIMAL_PLACES_6 = SIX;
  public static final int DECIMAL_PLACES_8 = EIGHT;
  public static final int DECIMAL_PLACES_16 = 16;
  public static final int HUNDRED = 100;
  public static final int THOUSAND = 1000;
  public static final long L_ZERO = 0L;
  public static final long L_ONE = 1L;
  public static final long L_TAN = 10L;
  public static final long L_HUNDRED = 100L;
  public static final long L_THOUSAND = 1000L;
  public static final float F_ZERO = 0f;
  public static final float F_ONE = 1f;
  public static final float F_TAN = 10f;
  public static final float F_HUNDRED = 100f;
  public static final float F_THOUSAND = 1000f;
  public static final double D_ZERO = 0d;
  public static final double D_ONE = 1d;
  public static final double D_TAN = 10d;
  public static final double D_HUNDRED = 100d;
  public static final double D_THOUSAND = 1000d;
  public static final int HIGH_4_BIT = 0xF0;
  public static final int LOW_4_BIT = 0x0F;
  public static final long FULL_1_BIT = 0xFF;
  public static final long FULL_2_BIT = 0xFF_FF;
  public static final long FULL_3_BIT = 0xFF_FF_FF;
  public static final long FULL_4_BIT = 0xFF_FF_FF_FF;
  public static final long FULL_5_BIT = 0xFF_FF_FF_FF_FFL;
  public static final long FULL_6_BIT = 0xFF_FF_FF_FF_FF_FFL;
  public static final long FULL_7_BIT = 0xFF_FF_FF_FF_FF_FF_FFL;
  public static final long FULL_8_BIT = 0xFF_FF_FF_FF_FF_FF_FF_FFL;
  public static final long ON_1_BIT = 1L;
  public static final long ON_2_BIT = 1L << 1;
  public static final long ON_3_BIT = 1L << 2;
  public static final long ON_4_BIT = 1L << 3;
  public static final long ON_5_BIT = 1L << 4;
  public static final long ON_6_BIT = 1L << 5;
  public static final long ON_7_BIT = 1L << 6;
  public static final long ON_8_BIT = 1L << 7;
  public static final long ON_9_BIT = 1L << 8;
  public static final long ON_10_BIT = 1L << 9;
  public static final long ON_11_BIT = 1L << 10;
  public static final long ON_12_BIT = 1L << 11;
  public static final long ON_13_BIT = 1L << 12;
  public static final long ON_14_BIT = 1L << 13;
  public static final long ON_15_BIT = 1L << 14;
  public static final long ON_16_BIT = 1L << 15;
  public static final long ON_17_BIT = 1L << 16;
  public static final long ON_18_BIT = 1L << 17;
  public static final long ON_19_BIT = 1L << 18;
  public static final long ON_20_BIT = 1L << 19;
  public static final long ON_21_BIT = 1L << 20;
  public static final long ON_22_BIT = 1L << 21;
  public static final long ON_23_BIT = 1L << 22;
  public static final long ON_24_BIT = 1L << 23;
  public static final long ON_25_BIT = 1L << 24;
  public static final long ON_26_BIT = 1L << 25;
  public static final long ON_27_BIT = 1L << 26;
  public static final long ON_28_BIT = 1L << 27;
  public static final long ON_29_BIT = 1L << 28;
  public static final long ON_30_BIT = 1L << 29;
  public static final long ON_31_BIT = 1L << 30;
  public static final long ON_32_BIT = 1L << 31;
  public static final long ON_33_BIT = 1L << 32;
  public static final long ON_34_BIT = 1L << 33;
  public static final long ON_35_BIT = 1L << 34;
  public static final long ON_36_BIT = 1L << 35;
  public static final long ON_37_BIT = 1L << 36;
  public static final long ON_38_BIT = 1L << 37;
  public static final long ON_39_BIT = 1L << 38;
  public static final long ON_40_BIT = 1L << 39;
  public static final long ON_41_BIT = 1L << 40;
  public static final long ON_42_BIT = 1L << 41;
  public static final long ON_43_BIT = 1L << 42;
  public static final long ON_44_BIT = 1L << 43;
  public static final long ON_45_BIT = 1L << 44;
  public static final long ON_46_BIT = 1L << 45;
  public static final long ON_47_BIT = 1L << 46;
  public static final long ON_48_BIT = 1L << 47;
  public static final long ON_49_BIT = 1L << 48;
  public static final long ON_50_BIT = 1L << 49;
  public static final long ON_51_BIT = 1L << 50;
  public static final long ON_52_BIT = 1L << 51;
  public static final long ON_53_BIT = 1L << 52;
  public static final long ON_54_BIT = 1L << 53;
  public static final long ON_55_BIT = 1L << 54;
  public static final long ON_56_BIT = 1L << 55;
  public static final long ON_57_BIT = 1L << 56;
  public static final long ON_58_BIT = 1L << 57;
  public static final long ON_59_BIT = 1L << 58;
  public static final long ON_60_BIT = 1L << 59;
  public static final long ON_61_BIT = 1L << 60;
  public static final long ON_62_BIT = 1L << 61;
  public static final long ON_63_BIT = 1L << 62;
  public static final long ON_64_BIT = 1L << 63;
  /**
   * unicode基本片面的最大范围
   */
  public static final int UNICODE_BMP_MAX = 0xFFFF;
  /**
   * unicode扩展片面的最小范围
   */
  public static final int UNICODE_SMP_MIN = 0x1_0000;
  /**
   * unicode扩展片面的最大范围
   */
  public static final int UNICODE_SMP_MAX = 0x10_FFFF;
  public static final String UPPER_TRIANGLE = "^";
  public static final String DOLLAR = "$";
  public static final String INNER_CLASS_SYMBOL = DOLLAR;
  public static final String PROXY_CLASS_SYMBOL = "$$";
  public static final String PATH_SEPARATOR = File.separator;
  public static final String UNDERLINE = "_";
  public static final String BACKSLASH = "\\";
  public static final String BI_BACKSLASH = "\\\\";
  public static final String SLASH = "/";
  public static final String BI_SLASH = "//";
  public static final String BIG_BRACES_LEFT = "{";
  public static final String BIG_BRACES_RIGHT = "}";
  public static final String MIDDLE_BRACES_LEFT = "[";
  public static final String MIDDLE_BRACES_RIGHT = "]";
  public static final String SMALL_BRACES_LEFT = "(";
  public static final String SMALL_BRACES_RIGHT = ")";
  public static final String PS = File.separator;
  /**
   * 当前目录
   */
  public static final String CURRENT_DIR_PATH = getCurrentDirectoryPath();
  public static final String REPEAT_PS = PS + PS;
  public static final String DOT = ".";
  public static final String BI_DOT = "..";
  public static final String COMMA = ",";
  public static final String QUOTE = "\"";
  public static final String COLON = ":";
  public static final String SEMICOLON = ";";
  public static final String LS = System.lineSeparator();
  public static final String ROOT_DIRECTORY = "/";
  /**
   * 根目录
   */
  public static final String ROOT_DIR_PATH = getRootDirectoryPath();
  public static final String CURRENT_DIRECTORY = "./";
  public static final String UP_DIRECTORY = "../";
  public static final char C_PATH_SEPARATOR = File.separatorChar;
  public static final char C_UNDERLINE = '_';
  public static final char C_ESCAPE = '\\';
  public static final char C_BIG_BRACES_LEFT = '{';
  public static final char C_BIG_BRACES_RIGHT = '}';
  public static final char C_MIDDLE_BRACES_LEFT = '[';
  public static final char C_MIDDLE_BRACES_RIGHT = ']';
  public static final char C_SMALL_BRACES_LEFT = '(';
  public static final char C_SMALL_BRACES_RIGHT = ')';
  public static final char C_SLASH = '/';
  public static final char C_PS = File.separatorChar;
  public static final char C_DOT = '.';
  public static final char C_COMMA = ',';
  public static final char C_QUOTE = '"';
  public static final char C_COLON = ':';
  public static final char C_SEMICOLON = ';';
  public static final char[] C_INVISIBLE_LIST = {
      '\r',
      '\n',
      '\t',
      '\b',
      '\f'
  };
  public static final String[] INVISIBLE_LIST = {
      "\r",
      "\n",
      "\t",
      "\b",
      "\f"
  };
  /**
   * 已经排序不允许出现在文件名或目录名的字符串
   */
  public static final char[] NO_ALLOW_IN_FILE_OR_DIR_NAME_CHAR_SORTED_LIST = {
      '"',
      '*',
      '/',
      ':',
      '<',
      '>',
      '?',
      '\\',
      '|'
  };
  /**
   * 全局编码环境
   */
  public static transient final Charset GLOBAL_CHARSET = StandardCharsets.UTF_8;

  /**
   * 获取当前项目目录
   *
   * @return 返回项目目录
   */
  private static String getRootDirectoryPath() {
    return new File(ROOT_DIRECTORY).getAbsolutePath();
  }

  /**
   * 获取当前项目目录
   *
   * @return 返回项目目录
   */
  private static String getCurrentDirectoryPath() {
    return new File(EMPTY).getAbsolutePath() + PS;
  }

  /**
   * 获取临时目录路径
   *
   * @return 返回临时目录路径
   */
  private static String getTempDirectoryPath() {
    return System.getProperty("java.io.tmpdir");
  }

  /**
   * 获取用户目录路径
   *
   * @return 返回用户目录路径
   */
  private static String getUserDirectoryPath() {
    return System.getProperty("user.home");
  }
}
