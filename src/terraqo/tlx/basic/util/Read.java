package pxf.toolkit.basic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * 读取操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Read {

  private Read() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 读取属性文件从环境变量
   *
   * @param file 文件
   * @return {@code Properties}
   * @throws IOException 当读取发生错误时
   */
  public static Properties propertiesFromFile(String file) throws IOException {
    return propertiesFromFile(new File(file));
  }

  /**
   * 读取属性文件从环境变量
   *
   * @param file 文件
   * @param charset 文件字符集
   * @return {@code Properties}
   * @throws IOException 当读取发生错误时
   */
  public static Properties propertiesFromFile(String file, Charset charset) throws IOException {
    return propertiesFromFile(new File(file), charset);
  }

  /**
   * 读取属性文件从环境变量
   *
   * @param file 文件
   * @return {@code Properties}
   * @throws IOException 当读取发生错误时
   */
  public static Properties propertiesFromFile(Path file) throws IOException {
    return propertiesFromFile(file.toFile());
  }

  /**
   * 读取属性文件从环境变量
   *
   * @param file 文件
   * @param charset 文件字符集
   * @return {@code Properties}
   * @throws IOException 当读取发生错误时
   */
  public static Properties propertiesFromFile(Path file, Charset charset) throws IOException {
    return propertiesFromFile(file.toFile(), charset);
  }

  /**
   * 读取属性文件从环境变量
   *
   * @param file 文件
   * @return {@code Properties}
   * @throws IOException 当读取发生错误时
   */
  public static Properties propertiesFromFile(File file) throws IOException {
    return propertiesFromFile(file, StandardCharsets.UTF_8);
  }

  /**
   * 读取属性文件从环境变量
   *
   * @param file 文件
   * @param charset 文件字符集
   * @return {@code Properties}
   * @throws IOException 当读取发生错误时
   */
  public static Properties propertiesFromFile(File file, Charset charset) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, charset))) {
      Properties properties = new Properties();
      properties.load(bufferedReader);
      for (Entry<Object, Object> entry : properties.entrySet()) {
        String key = entry.getKey().toString();
        String value = entry.getValue().toString().trim();
        if (value.startsWith("${") && value.endsWith("}")) {
          int beginIndex = value.indexOf(":");
          if (beginIndex < 0) {
            beginIndex = value.length() - 1;
          }
          int endIndex = value.length() - 1;
          String envKey = value.substring(2, beginIndex);
          String envValue = System.getenv(envKey);
          if (envValue == null || "".equals(envValue.trim())) {
            value = value.substring(beginIndex + 1, endIndex);
          } else {
            value = envValue;
          }
          properties.setProperty(key, value);
        }
      }
      return properties;
    }
  }

  /**
   * 读取所有行
   *
   * @param filePath 文件路径
   * @param charset 字符集
   * @param collectionSupplier 集合提供器
   * @param <C> 集合类型 {@code <C extends Collection<String>>}
   * @return {@code C}
   * @throws IOException 如果读取文件发送异常
   */
  public static <C extends Collection<String>> C allLine(
      String filePath, Charset charset, Supplier<C> collectionSupplier) throws IOException {
    return allLine(new File(filePath), charset, collectionSupplier);
  }

  /**
   * 读取所有行
   *
   * @param filePath 文件路径
   * @param charset 字符集
   * @param collectionSupplier 集合提供器
   * @param <C> 集合类型 {@code <C extends Collection<String>>}
   * @return {@code C}
   * @throws IOException 如果读取文件发送异常
   */
  public static <C extends Collection<String>> C allLine(
      File filePath, Charset charset, Supplier<C> collectionSupplier) throws IOException {
    BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
    C result = collectionSupplier.get();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      result.add(line);
    }
    Close.closeableSilently(bufferedReader);
    return result;
  }
}
