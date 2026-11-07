/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils Interface in Java Web Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.constants;

import com.utils.UtilsOfAssert;
import axf.java.common.utils.exception.ParseException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 字符集类型
 *
 * @author : Alfred X Fu
 * @date : 2020/2/29 16:30
 */
public enum CharsetType {
  US_ASCII(StandardCharsets.US_ASCII, "ascii"),
  ISO_8859_1(StandardCharsets.ISO_8859_1, "ios_8859_1", "ios-8859-1"),
  UTF_8(StandardCharsets.UTF_8, "utf-8", "utf_8", "utf8"),
  UTF_16BE(StandardCharsets.UTF_16BE, "utf-16be", "utf-16-be", "utf_16_be", "utf_16_be"),
  UTF_16LE(StandardCharsets.UTF_16LE, "utf-16le", "utf-16-le", "utf_16_le", "utf_16_le"),
  UTF_16(StandardCharsets.UTF_16, "uff-16", "utf_16", "utf16");

  private final Charset charset;
  private final String[] alias;

  CharsetType(final Charset charset, final String... alias) {
    this.charset = charset;
    this.alias = alias;
  }

  public static Charset parseCharset(final String name) {
    return CharsetType.parseCharsetType(name).charset;
  }

  public static CharsetType parseCharsetType(final String name) {
    UtilsOfAssert.checkValid(name);
    for (final CharsetType value : CharsetType.values()) {
      final long count = Arrays.stream(value.alias).filter(s -> s.equalsIgnoreCase(name)).count();
      if (count >= 1) {
        return value;
      }
    }
    throw new ParseException("Don't find charset type");
  }

  //get方法

  /**
   * Gets the value of charset.
   *
   * @return the value of charset ,may be null
   */
  public Charset getCharset() {
    return this.charset;
  }
}
