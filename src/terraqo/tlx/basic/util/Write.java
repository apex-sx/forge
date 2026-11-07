package pxf.toolkit.basic.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 写操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Write {

  private Write() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 写入多文件
   *
   * @param data 数据
   * @param directory 目录
   * @param charset 字符集
   * @throws IOException 如果写入发送了异常
   */
  public static void multiFile(Map<String, List<String>> data, Path directory, Charset charset)
      throws IOException {
    multiFile(data, directory, charset, path -> path);
  }

  /**
   * 写入多文件
   *
   * @param data 数据
   * @param directory 目录
   * @param charset 字符集
   * @throws IOException 如果写入发送了异常
   */
  public static void multiFile(Map<String, List<String>> data, File directory, Charset charset)
      throws IOException {
    multiFile(data, directory, charset, File::toPath);
  }

  /**
   * 写入多文件
   *
   * @param data 数据
   * @param directory 目录
   * @param charset 字符集
   * @throws IOException 如果写入发送了异常
   */
  public static void multiFile(Map<String, List<String>> data, String directory, Charset charset)
      throws IOException {
    multiFile(data, directory, charset, Path::of);
  }

  private static <T> void multiFile(
      Map<String, List<String>> data, T directory, Charset charset, Function<T, Path> function)
      throws IOException {
    Set<String> keys = data.keySet();
    for (String key : keys) {
      File file = function.apply(directory).resolve(key).toFile();
      FileWriter fileWriter = new FileWriter(file, charset);
      List<String> list = data.get(key);
      for (String ll : list) {
        fileWriter.write(ll);
        fileWriter.write("\n");
      }
      Close.closeable(fileWriter);
    }
  }
}
