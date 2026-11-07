package pxf.toolkit.basic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Classify {

  private Classify() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 将路径按扩展名分组
   *
   * @param paths 路径
   * @return {@code Prop<String, List<String>>}
   */
  public static Map<String, List<String>> pathByExtension(
      String defaultExtension, String... paths) {
    return Classify.pathByExtension(defaultExtension, List.of(paths));
  }

  /**
   * 将路径按扩展名分组
   *
   * @param paths 路径
   * @return {@code Prop<String, List<String>>}
   */
  public static Map<String, List<String>> pathByExtension(
      String defaultExtension, Collection<String> paths) {
    Map<String, List<String>> result = new HashMap<>(paths.size() / 10 + 10);
    for (String path : paths) {
      String ext = Extract.fileExtensionFormPath(path, defaultExtension);
      result.computeIfAbsent(ext, k -> new ArrayList<>(10)).add(path);
    }
    return result;
  }
}
