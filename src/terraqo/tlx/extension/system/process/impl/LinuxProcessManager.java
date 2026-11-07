package pxf.toolkit.extension.system.process.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hyperic.sigar.ptql.ProcessQuery;
import pxf.toolkit.basic.lang.iterators.LineIterable;
import pxf.toolkit.extension.system.process.ProcessManager;

/**
 * {@link ProcessManager} implementation for Linux. Uses the <tt>ps</tt> and <tt>kill</tt> commands.
 *
 * <p>Should Work on Solaris too, except that the command line string returned by <tt>ps</tt> there
 * is limited to 80 characters and this affects {@link this##findProcessId(ProcessQuery)})}.
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public class LinuxProcessManager implements ProcessManager {

  private static final Pattern PS_OUTPUT_LINE = Pattern.compile("^\\s*(\\d+)\\s+(.*)$");

  private final String[] runAsArgs;

  public LinuxProcessManager(String[] runAsArgs) {
    this.runAsArgs = runAsArgs;
  }

  protected String[] psCommand() {
    return new String[] {"/bin/ps", "-e", "-o", "pid,args"};
  }

  @Override
  public long findProcessId(String command, String argument) throws Throwable {
    String regex = Pattern.quote(command) + ".*" + Pattern.quote(argument);
    Pattern commandPattern = Pattern.compile(regex);
    LineIterable lineIterable = execute(psCommand());
    for (String line : lineIterable) {
      Matcher lineMatcher = PS_OUTPUT_LINE.matcher(line);
      if (lineMatcher.matches()) {
        String pc = lineMatcher.group(2);
        Matcher commandMatcher = commandPattern.matcher(pc);
        if (commandMatcher.find()) {
          return Long.parseLong(lineMatcher.group(1));
        }
      }
    }
    return PID_NOT_FOUND;
  }

  @Override
  public void killProcess(Process process, long pid) throws Throwable {
    if (pid <= 0) {
      throw new IllegalArgumentException("invalid pid: " + pid);
    }
    execute("/bin/kill", "-KILL", Long.toString(pid));
  }

  private LineIterable execute(String... args) throws Throwable {
    String[] command;
    if (runAsArgs != null) {
      command = new String[runAsArgs.length + args.length];
      System.arraycopy(runAsArgs, 0, command, 0, runAsArgs.length);
      System.arraycopy(args, 0, command, runAsArgs.length, args.length);
    } else {
      command = args;
    }
    Process process = new ProcessBuilder(command).start();
    return new LineIterable(process.getInputStream());
  }
}
