package pxf.toolkit.extension.system.process.impl;

import pxf.toolkit.extension.system.process.ProcessManager;

/**
 * 程序管理接口
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public class PureJavaProcessManager implements ProcessManager {

  @Override
  public long findProcessId(String command, String argument) {
    return PID_UNKNOWN;
  }

  @Override
  public void killProcess(Process process, long pid) {
    process.destroy();
  }
}
