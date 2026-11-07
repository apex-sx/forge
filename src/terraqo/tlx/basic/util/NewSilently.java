package pxf.toolkit.basic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import pxf.toolkit.basic.exception.IOFlowException;
import pxf.toolkit.basic.exception.ReflectException;

/**
 * 静默新建操作
 *
 * @author potatoxf
 * @date 2021/3/12
 */
public final class NewSilently {

  private NewSilently() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static <T> T instance(Class<?> clz) {
    return New.instance(clz);
  }

  public static <T> T instance(Class<?> clz, Object... args) {
    try {
      return New.instance(clz, args);
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException
        | InstantiationException e) {
      throw new ReflectException(e);
    }
  }

  /**
   * 获取输入流
   *
   * @param file 文件
   * @return {@code FileInputStream}
   */
  public static FileInputStream fileInputStream(String file) {
    try {
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 获取输入流
   *
   * @param file 文件
   * @return {@code FileInputStream}
   */
  public static FileInputStream fileInputStream(File file) {
    try {
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 获取输出流
   *
   * @param file 文件
   * @return {@code FileOutputStream}
   */
  public static FileOutputStream fileOutputStream(String file) {
    try {
      return new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 获取输出流
   *
   * @param file 文件
   * @return {@code FileOutputStream}
   */
  public static FileOutputStream fileOutputStream(File file) {
    try {
      return new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      throw new IOFlowException(e);
    }
  }
}
