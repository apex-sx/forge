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
 * @author : Alfred X Fu
 * @date : 2020/4/20 16:37
 */
public enum CacheControlValue implements Constants<CacheControlValue> {
  MUST_REVALIDATE,
  NO_CACHE,
  NO_STORE,
  NO_TRANSFORM,
  _PUBLIC,
  _PRIVATE,
  PROXY_REVALIDATE,
  MAX_AGE(1, true),
  S_MAXAGE(1, true),
  MAX_STALE(1, false),
  MIN_FRESH(1, true),
  ONLY_IF_CACHED,
  //扩展
  IMMUTABLE,
  STALE_WHILE_REVALIDATE(1, true),
  STALE_IF_ERROR(1, true);
  private final int possibleArgCount;
  private final boolean isMustHasArg;

  CacheControlValue() {
    this(0, false);
  }

  CacheControlValue(int possibleArgCount, boolean isMustHasArg) {
    this.possibleArgCount = possibleArgCount;
    this.isMustHasArg = isMustHasArg;
  }

  public int getPossibleArgCount() {
    return possibleArgCount;
  }

  public boolean isMustHasArg() {
    return isMustHasArg;
  }

  @Override
  public CacheControlValue self() {
    return this;
  }

  @Override
  public String[] aliases() {
    String name = this.name();
    if (name.startsWith("_")) {
      name = name.substring(1);
    }
    name.replace('_', '-');
    return new String[]{name};
  }
}
