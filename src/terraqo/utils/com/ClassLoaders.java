/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com;

import com.utils.UtilsOfAssert;
import com.utils.UtilsOfConstant;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * 类加载器
 *
 * @author : Alfred X Fu
 * @date : 2020/2/12 15:45
 */
public final class ClassLoaders {

  private static final transient Supplier<?>[] APPLICATION_CLASS_LOADER_SUPPLIERS = new Supplier[]{
      () -> {
        try {
          return Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        return null;
      },
      () -> {
        try {
          return ClassLoaders.class.getClassLoader();
        } catch (Throwable ignored) {
        }
        return null;
      },
      () -> {
        try {
          return ClassLoader.getSystemClassLoader();
        } catch (Throwable ignored) {
        }
        return null;
      }

  };

  public static InputStream getClassLoaderResource(final String path) {
    UtilsOfAssert.checkValid(path, "Path is valid");
    UtilsOfAssert.checkIsTrue(!path.startsWith(UtilsOfConstant.SLASH), "Path does not start with a slash '/'");
    return ClassLoaders.getApplicationClassLoader().getResourceAsStream(path);
  }

  /**
   * 获取 Application ClassLoader
   *
   * @return {@link ClassLoader}
   */
  public static ClassLoader getApplicationClassLoader() {
    for (final Supplier<?> classLoaderSupplier : ClassLoaders.APPLICATION_CLASS_LOADER_SUPPLIERS) {
      final ClassLoader classLoader = (ClassLoader) classLoaderSupplier.get();
      if (classLoader != null) {
        return classLoader;
      }
    }
    throw new RuntimeException("Don't find classloader");
  }

  /**
   * 获取 Extension ClassLoader
   *
   * @return {@link ClassLoader}
   */
  public static ClassLoader getExtensionClassLoader() {
    final ClassLoader classLoader = ClassLoaders.getApplicationClassLoader();
    return classLoader.getParent();
  }

  public static InputStream getRootResource(String path) {
    UtilsOfAssert.checkValid(path, "Path is valid");
    if (!path.startsWith(UtilsOfConstant.SLASH)) {
      path = UtilsOfConstant.SLASH + path;
    }
    return ClassLoaders.class.getResourceAsStream(path);
  }

  //get方法

}
