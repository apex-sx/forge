/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math;

import java.util.UUID;

/**
 * UUID随机生成器
 *
 * @author : Alfred X Fu
 * @date : 2020/2/22 17:02
 */
public abstract class BaseUUIDGenerator {

  /**
   * 新建默认实例
   *
   * @return {@code UUIDGenerator}
   */
  public static BaseUUIDGenerator newDefaultInstance() {
    return new Default();
  }

  /**
   * 下一个UUID
   *
   * @return 全局唯一字符串
   */
  public abstract String next();

  private static final class Default extends BaseUUIDGenerator {

    /**
     * 下一个
     *
     * @return 全局唯一字符串
     */
    @Override
    public String next() {
      return UUID.randomUUID().toString();
    }
  }
}
