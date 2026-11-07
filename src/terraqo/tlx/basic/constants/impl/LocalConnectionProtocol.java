package pxf.toolkit.basic.constants.impl;

import pxf.toolkit.basic.constants.FindableEnumConstant;

/**
 * 本地连接协议
 *
 * @author potatoxf
 * @date 2021/4/20
 */
public enum LocalConnectionProtocol implements FindableEnumConstant<LocalConnectionProtocol> {
  /** 管道连接 */
  PIPE,
  /** 套字节连接 */
  SOCKET
}
