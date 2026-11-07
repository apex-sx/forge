/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils;

import axf.java.common.utils.exception.FailureException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.function.BiFunction;

/**
 * 文件工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 10:58
 */
public final class UtilsOfFile {

  public static Path createFileIfExistRenameAndSpecifySize(Path path, final long fileSize) {
    path = createFileIfExistRename(path);
    RandomAccessFile raf = null;
    try {
      raf = new RandomAccessFile(path.toFile(), "rw");
      raf.setLength(fileSize);
    } catch (IOException e) {
      throw new FailureException("Create file of size specified failure");
    } finally {
      UtilsOfResource.close(raf);
    }
    return path;
  }

  public static Path createFileIfExistRename(Path path) {
    File file = path.toFile();
    boolean b = file.exists();
    if (b) {
      String filename = path.getFileName().toString();
      int lastIndex = filename.lastIndexOf(UtilsOfConstant.DOT);
      String name;
      String ext;
      if (UtilsOfJudge.isEOF(lastIndex)) {
        name = filename;
        ext = "";
      } else {
        name = filename.substring(0, lastIndex);
        ext = filename.substring(lastIndex + 1);
      }
      int count = 0;
      BiFunction<String, Integer, String> nameSequence = (n, seq) -> n + UtilsOfConstant.SMALL_BRACES_LEFT + seq
          + UtilsOfConstant.SMALL_BRACES_RIGHT;
      while (b) {
        String newName = nameSequence.apply(name, count) + UtilsOfConstant.DOT + ext;
        path = path.resolveSibling(newName);
        file = path.toFile();
        b = file.exists();
        count++;
      }
    }
    try {
      b = file.createNewFile();
      if (!b) {
        throw new FailureException("Create file failure");
      }
    } catch (IOException e) {
      throw new FailureException("Create file failure", e);
    }
    return path;
  }

  public static Path createFileIfExistRecreate(Path path) {
    File file = path.toFile();
    boolean b = file.exists();
    if (b) {
      b = file.delete();
      if (!b) {
        throw new FailureException("Delete file failure");
      }
    }
    try {
      b = file.createNewFile();
      if (!b) {
        throw new FailureException("Create file failure");
      }
    } catch (IOException e) {
      throw new FailureException("Create file failure", e);
    }
    return path;
  }
}
