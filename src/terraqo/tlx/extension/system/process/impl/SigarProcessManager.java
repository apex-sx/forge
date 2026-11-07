package pxf.toolkit.extension.system.process.impl;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessFinder;
import pxf.toolkit.extension.system.process.ProcessManager;

/**
 * {@link ProcessManager} implementation that uses the SIGAR library.
 *
 * <p>Requires the sigar.jar in the classpath and the appropriate system-specific native library
 * (e.g. <tt>libsigar-x86-linux.so</tt> on Linux x86) available in the <em>java.library.path</em>.
 *
 * <p>See the <a href="http://support.hyperic.com/display/SIGAR">SIGAR site</a> for documentation
 * and downloads.
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public class SigarProcessManager implements ProcessManager {

  @Override
  public long findProcessId(String command, String argument) throws Throwable {
    Sigar sigar = new Sigar();
    try {
      long[] pids = ProcessFinder.find(sigar, "State.Name.eq=" + command);
      for (long pid : pids) {
        String[] arguments = sigar.getProcArgs(pid);
        if (arguments != null && argumentMatches(arguments, argument)) {
          return pid;
        }
      }
      return PID_NOT_FOUND;
    } finally {
      sigar.close();
    }
  }

  @Override
  public void killProcess(Process process, long pid) throws Throwable {
    Sigar sigar = new Sigar();
    try {
      sigar.kill(pid, Sigar.getSigNum("KILL"));
    } finally {
      sigar.close();
    }
  }

  private boolean argumentMatches(String[] arguments, String expected) {
    for (String argument : arguments) {
      if (argument.contains(expected)) {
        return true;
      }
    }
    return false;
  }
}
