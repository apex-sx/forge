/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.constants;

import java.math.BigDecimal;

/**
 * 比例
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
interface Scale<T extends Enum<T>> {

  /**
   * 将数字解析成对应比率
   *
   * @param num 比率字符串
   * @return 枚举类型
   */
  T parse(String num);

  /**
   * 获取比例
   *
   * @return 比例数字
   */
  BigDecimal scale();
}
