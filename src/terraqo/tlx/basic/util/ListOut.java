package pxf.toolkit.basic.util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * 列出操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class ListOut {

  private ListOut() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 列出路径
   *
   * @param rootDirectory 根路径
   * @param filenames 文件名
   * @return 返回路径列表
   */
  public static List<Path> pathsExisting(Path rootDirectory, String... filenames) {
    return filesExisting(rootDirectory, Path::resolve, path -> path.toFile().exists(), filenames);
  }

  /**
   * 列出路径
   *
   * @param rootDirectory 根路径
   * @param filenames 文件名
   * @return 返回路径列表
   */
  public static List<Path> pathsExisting(String rootDirectory, String... filenames) {
    return filesExisting(rootDirectory, Path::of, path -> path.toFile().exists(), filenames);
  }

  /**
   * 列出文件
   *
   * @param rootDirectory 根路径
   * @param filenames 文件名
   * @return 返回路径列表
   */
  public static List<File> filesExisting(Path rootDirectory, String... filenames) {
    return filesExisting(
        rootDirectory, (rd, filename) -> rd.resolve(filename).toFile(), File::exists, filenames);
  }

  /**
   * 列出文件
   *
   * @param rootDirectory 根路径
   * @param filenames 文件名
   * @return 返回路径列表
   */
  public static List<File> filesExisting(String rootDirectory, String... filenames) {
    return filesExisting(
        rootDirectory, (rd, filename) -> Path.of(rd, filename).toFile(), File::exists, filenames);
  }

  /**
   * 列出文件
   *
   * @param rootDirectory 根路径
   * @param filenames 文件名
   * @return 返回路径列表
   */
  private static <T, R> List<R> filesExisting(
      T rootDirectory,
      BiFunction<T, String, R> function,
      Predicate<R> predicate,
      String... filenames) {
    if (rootDirectory == null) {
      throw new IllegalArgumentException();
    }
    List<R> paths = new ArrayList<>(filenames.length);
    for (String filename : filenames) {
      if (filename == null) {
        continue;
      }
      R result = function.apply(rootDirectory, filename);
      if (predicate.test(result)) {
        paths.add(result);
      }
    }
    return paths;
  }
}
