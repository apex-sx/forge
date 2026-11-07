package pxf.toolkit.extension.system.process.impl;

/**
 * @author potatoxf
 * @date 2021/4/29
 */
public class ProcessHelper {

  private ProcessHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * Sigar 是否有效
   *
   * @return 如果有效返回 {@code true}，否则 {@code false}
   */
  public static boolean isSigarAvailable() {
    try {
      Class.forName("org.hyperic.sigar.Sigar", false, ProcessHelper.class.getClassLoader());
      return true;
    } catch (ClassNotFoundException classNotFoundException) {
      return false;
    }
  }
}
