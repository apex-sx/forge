package pxf.toolkit.basic.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除操作
 *
 * @author potatoxf
 * @date 2021/4/16
 */
public final class Delete {

  private static final Logger LOG = LoggerFactory.getLogger(DeleteFileVisitor.class);

  private Delete() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 安全地删除文件
   *
   * <p>只有存在并且是文件时才删除
   *
   * @param filePath 文件路径
   * @return 删除成功返回 {@code true}，否则 {@code false}
   */
  public static boolean fileSafely(String filePath) {
    return Delete.fileSafely(Path.of(filePath));
  }

  /**
   * 安全地删除文件
   *
   * <p>只有存在并且是文件时才删除
   *
   * @param filePath 文件路径
   * @return 删除成功返回 {@code true}，否则 {@code false}
   */
  public static boolean fileSafely(File filePath) {
    return Delete.fileSafely(filePath.toPath());
  }

  /**
   * 安全地删除文件
   *
   * <p>只有存在并且是文件时才删除
   *
   * @param filePath 文件路径
   * @return 删除成功返回 {@code true}，否则 {@code false}
   */
  public static boolean fileSafely(Path filePath) {
    try {
      Delete.fileOrDirectorySafely(filePath.toFile(), filePath, true);
      return true;
    } catch (IOException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("Failed to delete file for [" + filePath + "]", e);
      }
    }
    return false;
  }

  /**
   * 安全地删除目录
   *
   * <p>只有存在并且是目录时才删除
   *
   * @param directoryPath 目录路径
   * @return 删除成功返回 {@code true}，否则 {@code false}
   */
  public static boolean directorySafely(String directoryPath) {
    return directorySafely(Path.of(directoryPath));
  }

  /**
   * 安全地删除目录
   *
   * <p>只有存在并且是目录时才删除
   *
   * @param directoryPath 目录路径
   * @return 删除成功返回 {@code true}，否则 {@code false}
   */
  public static boolean directorySafely(File directoryPath) {
    return directorySafely(directoryPath.toPath());
  }

  /**
   * 安全地删除目录
   *
   * <p>只有存在并且是目录时才删除
   *
   * @param directoryPath 目录路径
   * @return 删除成功返回 {@code true}，否则 {@code false}
   */
  public static boolean directorySafely(Path directoryPath) {
    try {
      Delete.fileOrDirectorySafely(directoryPath.toFile(), directoryPath, false);
      return true;
    } catch (IOException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("Failed to delete directory for [" + directoryPath + "]", e);
      }
    }
    return false;
  }

  private static void fileOrDirectorySafely(File file, Path path, boolean isDeleteFile)
      throws IOException {
    if (file.exists()) {
      if (isDeleteFile) {
        if (file.isFile()) {
          Files.deleteIfExists(path);
        }
      } else {
        if (file.isDirectory()) {
          Files.walkFileTree(path, new DeleteFileVisitor());
        }
      }
    }
  }

  private static class DeleteFileVisitor implements FileVisitor<Path> {

    private boolean isException;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException {
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      Files.deleteIfExists(file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
      if (LOG.isErrorEnabled()) {
        LOG.error("Failed to delete file for [" + file + "]", e);
      }
      isException = true;
      Files.deleteIfExists(file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      if (!isException) {
        Files.deleteIfExists(dir);
      }
      return FileVisitResult.CONTINUE;
    }
  }
}
