/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.regularamount;

/**
 * 合格金额
 *
 * @author : Alfred X Fu
 * @date : 2020/4/24 11:36
 */
public interface RegularAmount {

  /**
   * 解析合格金额
   *
   * @param amount 阿拉伯数字金额
   *
   * @return 返回正规的金额数字
   */
  String parse(String amount);

  /**
   * 解析合格金额
   *
   * @param amount 阿拉伯数字金额
   *
   * @return 返回正规的金额数字
   */
  default String parse(double amount) {
    return parse(String.valueOf(amount));
  }
}
