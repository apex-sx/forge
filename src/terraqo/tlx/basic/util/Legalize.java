package pxf.toolkit.basic.util;

/**
 * 合法化
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public final class Legalize {

  private Legalize() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 合法化目录路径
   *
   * @param filePath 目录路径
   * @return 返回合法化的目录路径
   */
  public static String directoryPath(String filePath) {
    return Legalize.path(filePath, true);
  }

  /**
   * 合法化文件路径
   *
   * @param filePath 文件路径
   * @return 返回合法化的文件路径
   */
  public static String filePath(String filePath) {
    return Legalize.path(filePath, false);
  }

  /**
   * 合法化路径
   *
   * @param path 路径
   * @param isDirectory 是否为目录
   * @return 返回合法化的路径
   */
  public static String path(String path, boolean isDirectory) {
    String p = path;
    p = p.replace('\\', '/');
    p = p.replace("\\\\", "/");
    if (!p.startsWith("/")) {
      p = "/" + p;
    }
    if (!p.endsWith("/") && isDirectory) {
      p = p + "/";
    }
    return p;
  }

  /**
   * 转换正确索引
   *
   * @param index 索引
   * @param len 长度
   * @return 正确索引
   */
  public static int index(int index, int len) {
    while (index < 0) {
      index += len;
    }
    while (index >= len) {
      index -= len;
    }
    return index;
  }
}
