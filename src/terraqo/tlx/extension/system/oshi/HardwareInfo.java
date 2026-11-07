package pxf.toolkit.extension.system.oshi;

import java.util.LinkedList;
import java.util.List;

/**
 * 信息
 *
 * @author potatoxf
 * @date 2021/4/5
 */
public class HardwareInfo {

  private static final int OSHI_WAIT_SECOND = 1000;

  // --------------------------------------------------------------------------- CPU相关信息

  /** 核心数 */
  private int cpuCoreNum;

  /** CPU总的使用率 */
  private double cpuTotalUsedRatio;

  /** CPU系统使用率 */
  private double cpuSystemUsedRatio;

  /** CPU用户使用率 */
  private double cpuUserUsedRatio;

  /** CPU当前等待率 */
  private double cpuWaitRatio;

  /** CPU当前空闲率 */
  private double cpuFreeRatio;

  // --------------------------------------------------------------------------- 內存相关信息

  /** 内存总量 */
  private double ramTotal;

  /** 已用内存 */
  private double ramUsed;

  /** 剩余内存 */
  private double ramFree;

  // --------------------------------------------------------------------------- JVM相关信息

  /** 当前JVM占用的内存总数(M) */
  private double jvmRamTotal;

  /** JVM最大可用内存总数(M) */
  private double jvmRamLimit;

  /** JVM空闲内存(M) */
  private double jvmRamFree;

  /** JDK版本 */
  private String jvmVersion;

  /** JDK路径 */
  private String jvmHome;

  // --------------------------------------------------------------------------- 服务器相关信息

  /** 服务器名称 */
  private String serverName;

  /** 服务器Ip */
  private String serverIp;

  /** 项目路径 */
  private String serverPath;

  /** 操作系统 */
  private String serverOsName;

  /** 系统架构 */
  private String serverOsArch;

  /** 磁盘相关信息 */
  private List<SysFileInfo> sysFiles = new LinkedList<>();
}
