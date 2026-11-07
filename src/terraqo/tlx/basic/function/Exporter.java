package pxf.toolkit.basic.function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Properties;
import pxf.toolkit.basic.exception.IOFlowException;

/**
 * 导入器
 *
 * @author potatoxf
 * @date 2021/4/4
 */
public interface Exporter {

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(String path) {
    try {
      execute(path);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @param parameter 参数
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(String path, Properties parameter) {
    try {
      execute(path, parameter);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(Path path) {
    try {
      execute(path);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @param parameter 参数
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(Path path, Properties parameter) {
    try {
      execute(path, parameter);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定文件
   *
   * @param file 路径
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(File file) {
    try {
      execute(file);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定文件
   *
   * @param file 文件
   * @param parameter 参数
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(File file, Properties parameter) {
    try {
      execute(file, parameter);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定输出流
   *
   * @param outputStream 输出流
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(OutputStream outputStream) {
    try {
      execute(outputStream);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定输出流
   *
   * @param outputStream 输出流
   * @param parameter 参数
   * @throws IOFlowException 如果导入发生错误
   */
  default void executeSilently(OutputStream outputStream, Properties parameter) {
    try {
      execute(outputStream, parameter);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @throws IOException 如果导入发生错误
   */
  default void execute(String path) throws IOException {
    execute(new File(path), null);
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @param parameter 参数
   * @throws IOException 如果导入发生错误
   */
  default void execute(String path, Properties parameter) throws IOException {
    execute(new File(path), parameter);
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @throws IOException 如果导入发生错误
   */
  default void execute(Path path) throws IOException {
    execute(path.toFile(), null);
  }

  /**
   * 执行导入，导入到指定路径
   *
   * @param path 路径
   * @param parameter 参数
   * @throws IOException 如果导入发生错误
   */
  default void execute(Path path, Properties parameter) throws IOException {
    execute(path.toFile(), parameter);
  }

  /**
   * 执行导入，导入到指定文件
   *
   * @param file 路径
   * @throws IOException 如果导入发生错误
   */
  default void execute(File file) throws IOException {
    execute(new FileOutputStream(file), null);
  }

  /**
   * 执行导入，导入到指定文件
   *
   * @param file 文件
   * @param parameter 参数
   * @throws IOException 如果导入发生错误
   */
  default void execute(File file, Properties parameter) throws IOException {
    execute(new FileOutputStream(file), parameter);
  }

  /**
   * 执行导入，导入到指定输出流
   *
   * @param outputStream 输出流
   * @throws IOException 如果导入发生错误
   */
  default void execute(OutputStream outputStream) throws IOException {
    execute(outputStream, null);
  }

  /**
   * 执行导入，导入到指定输出流
   *
   * @param outputStream 输出流
   * @param parameter 参数
   * @throws IOException 如果导入发生错误
   */
  void execute(OutputStream outputStream, Properties parameter) throws IOException;
}
