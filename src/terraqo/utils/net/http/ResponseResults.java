/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.net.http;

import axf.java.common.utils.UtilsOfConstant;
import axf.java.common.utils.UtilsOfJudge;
import axf.java.common.utils.function.Initiator;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * 响应结果
 *
 * @author : Alfred X Fu
 * @date : 2020/4/23 9:49
 */
@Data
public class ResponseResults {

  /**
   * 响应码
   */
  private final int code;
  /**
   * 响应信息
   */
  private final String msg;
  /**
   * 数据
   */
  private Map<String, Object> data;

  protected ResponseResults(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static ResponseResults build(int code) {
    return build(code, UtilsOfConstant.SUCCESS, null);
  }

  public static ResponseResults build(int code, Initiator<Map<String, Object>> mapInitiator) {
    return build(code, UtilsOfConstant.SUCCESS, mapInitiator);
  }

  public static ResponseResults build(int code, String msg, Initiator<Map<String, Object>> mapInitiator) {
    ResponseResults responseResults = new ResponseResults(code, msg);
    if (UtilsOfJudge.isNoNull(mapInitiator)) {
      mapInitiator.accept(responseResults.getData());
    }
    return responseResults;
  }

  public static ResponseResults build(ResponseConstant<?> responseConstant) {
    return build(responseConstant.responseCode(), responseConstant.responseMessage(), null);
  }

  public static ResponseResults build(ResponseConstant<?> responseConstant, Initiator<Map<String, Object>> mapInitiator) {
    return build(responseConstant.responseCode(), responseConstant.responseMessage(), mapInitiator);
  }

  public final Map<String, Object> getData() {
    if (UtilsOfJudge.isNull(data)) {
      data = new HashMap<>(4, 1);
    }
    return data;
  }

  public final void setData(Map<String, Object> data) {
    this.data = data;
  }
}
