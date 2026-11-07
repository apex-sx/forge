package pxf.toolkit.extension.system.process;

import pxf.toolkit.extension.system.process.impl.LinuxProcessManager;
import pxf.toolkit.extension.system.process.impl.PureJavaProcessManager;
import pxf.toolkit.extension.system.process.impl.SigarProcessManager;

/**
 * 程序管理接口
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public interface ProcessManager {

  /** 没有找到 */
  long PID_NOT_FOUND = -2;
  /** 未知 */
  long PID_UNKNOWN = -1;

  /**
   * 构造进程处理
   *
   * @return {@code ProcessManager}
   */
  static ProcessManager of() {
    return new SigarProcessManager();
  }

  /**
   * 构造纯Java进程处理
   *
   * @return {@code ProcessManager}
   */
  static ProcessManager ofPureJava() {
    return new PureJavaProcessManager();
  }

  /**
   * 构造Linux进程处理
   *
   * @param runAsArgs 参数
   * @return {@code ProcessManager}
   */
  static ProcessManager ofLinux(String... runAsArgs) {
    return new LinuxProcessManager(runAsArgs);
  }

  /**
   * 杀死进程
   *
   * @param process 程序
   * @param pid 程序ID
   * @throws Throwable 如果发生异常
   */
  void killProcess(Process process, long pid) throws Throwable;

  /**
   * 查找进程ID
   *
   * @param command 命令
   * @param argument 参数
   * @return the pid if found, {@link #PID_NOT_FOUND} if not, or {@link #PID_UNKNOWN} if this
   *     implementation is unable to find out
   * @throws Throwable 如果发生异常
   */
  long findProcessId(String command, String argument) throws Throwable;
}
