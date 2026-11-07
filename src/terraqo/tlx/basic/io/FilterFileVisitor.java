package pxf.toolkit.basic.io;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.function.Cleanable;
import pxf.toolkit.basic.function.Recoverable;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.Extract;

/**
 * 筛选文件访问
 *
 * @author potatoxf
 * @date 2021/3/6
 */
public class FilterFileVisitor
    implements FileVisitor<Path>, Recoverable, Cleanable, Serializable, Cloneable {

  private static final Logger LOG = LoggerFactory.getLogger(FilterFileVisitor.class);
  /** 是否开启统计 */
  private final boolean isOpenStatics;
  /** 目录白名单 */
  private Set<Path> directoryWhitelist;
  /** 文件白名单 */
  private Set<Path> fileWhitelist;
  /** 忽略目录路径 */
  private Set<Path> ignoredDirectoryPaths;
  /** 忽略文件路径 */
  private Set<Path> ignoredFilePaths;
  /** 忽略目录正则表达式 */
  private Set<String> ignoredDirectoryPatterns;
  /** 忽略文件正则表达式 */
  private Set<String> ignoredFilePatterns;
  /** 层级 */
  private int currentLevel = 0;
  /** 层级名 */
  private LinkedList<String> levelName;
  /** 文件类型数量 */
  private Map<String, Integer> catalogAmount;
  /** 总共目录数 */
  private int totalDirectory = 0;
  /** 总共文件数 */
  private int totalFile = 0;
  /** 总共失败文件数 */
  private int totalFailureFile = 0;

  public FilterFileVisitor(boolean isOpenStatics) {
    this.isOpenStatics = isOpenStatics;
    if (isOpenStatics) {
      levelName = new LinkedList<>();
      catalogAmount = new HashMap<>();
    }
  }

  /**
   * Invoked for a directory before entries in the directory are visited.
   *
   * <p>If this method returns {@link FileVisitResult#CONTINUE CONTINUE}, then entries in the
   * directory are visited. If this method returns {@link FileVisitResult#SKIP_SUBTREE SKIP_SUBTREE}
   * or {@link FileVisitResult#SKIP_SIBLINGS SKIP_SIBLINGS} then entries in the directory (and any
   * descendants) will not be visited.
   *
   * @param dir a reference to the directory
   * @param attrs the directory's basic attributes
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  @Override
  public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
      throws IOException {
    if (isSkipDirectory(dir)) {
      return FileVisitResult.SKIP_SUBTREE;
    }
    currentLevel++;
    levelName.add(dir.getFileName().toString());
    if (isOpenStatics()) {
      totalDirectory++;
    }
    return doPreVisitDirectory(dir, attrs);
  }

  /**
   * 处理没有被拦截的目录
   *
   * @param dir a reference to the directory
   * @param attrs the directory's basic attributes
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  protected FileVisitResult doPreVisitDirectory(Path dir, BasicFileAttributes attrs)
      throws IOException {
    return FileVisitResult.CONTINUE;
  }

  /**
   * Invoked for a file in a directory.
   *
   * @param file a reference to the file
   * @param attrs the file's basic attributes
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  @Override
  public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (isSkipFile(file)) {
      return FileVisitResult.CONTINUE;
    }
    if (isOpenStatics()) {
      totalFile++;
      String fileExtension = Extract.fileExtensionFormPath(file.toString());
      if (catalogAmount.containsKey(fileExtension)) {
        catalogAmount.put(fileExtension, catalogAmount.get(fileExtension) + 1);
      } else {
        catalogAmount.put(fileExtension, 1);
      }
    }
    return doVisitFile(file, attrs);
  }
  /**
   * 处理没有被拦截的文件
   *
   * @param file a reference to the file
   * @param attrs the file's basic attributes
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  protected FileVisitResult doVisitFile(Path file, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  /**
   * Invoked for a file that could not be visited. This method is invoked if the file's attributes
   * could not be read, the file is a directory that could not be opened, and other reasons.
   *
   * @param file a reference to the file
   * @param exc the I/O exception that prevented the file from being visited
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  @Override
  public final FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    if (LOG.isErrorEnabled()) {
      LOG.error(String.format("Failed to access file [%s]", file), exc);
    }
    totalFailureFile++;
    return doVisitFileFailed(file, exc);
  }
  /**
   * Invoked for a file that could not be visited. This method is invoked if the file's attributes
   * could not be read, the file is a directory that could not be opened, and other reasons.
   *
   * @param file a reference to the file
   * @param exc the I/O exception that prevented the file from being visited
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  protected FileVisitResult doVisitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  /**
   * Invoked for a directory after entries in the directory, and all of their descendants, have been
   * visited. This method is also invoked when iteration of the directory completes prematurely (by
   * a {@link #visitFile visitFile} method returning {@link FileVisitResult#SKIP_SIBLINGS
   * SKIP_SIBLINGS}, or an I/O error when iterating over the directory).
   *
   * @param dir a reference to the directory
   * @param exc {@code null} if the iteration of the directory completes without an error; otherwise
   *     the I/O exception that caused the iteration of the directory to complete prematurely
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  @Override
  public final FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    currentLevel--;
    levelName.pop();
    return doPostVisitDirectory(dir, exc);
  }
  /**
   * Invoked for a directory after entries in the directory, and all of their descendants, have been
   * visited. This method is also invoked when iteration of the directory completes prematurely (by
   * a {@link #visitFile visitFile} method returning {@link FileVisitResult#SKIP_SIBLINGS
   * SKIP_SIBLINGS}, or an I/O error when iterating over the directory).
   *
   * @param dir a reference to the directory
   * @param exc {@code null} if the iteration of the directory completes without an error; otherwise
   *     the I/O exception that caused the iteration of the directory to complete prematurely
   * @return the visit result
   * @throws IOException if an I/O error occurs
   */
  protected FileVisitResult doPostVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  /**
   * 当前文件统计数
   *
   * @return 当前文件统计数
   */
  public int currentFileCount() {
    return totalFile;
  }
  /**
   * 当前失败文件统计数
   *
   * @return 当前失败文件统计数
   */
  public int currentFailureFileCount() {
    return totalFailureFile;
  }
  /**
   * 当前目录统计数
   *
   * @return 当前目录统计数
   */
  public int currentDirectoryCount() {
    return totalDirectory;
  }
  /**
   * 当前层级
   *
   * @return 当前层级
   */
  public int currentLevel() {
    return currentLevel;
  }
  /**
   * 当前层级名
   *
   * @return 当前层级名
   */
  public String currentLevelName() {
    if (currentLevel() <= 0) {
      return Empty.STRING;
    }
    return levelName.peek();
  }
  /**
   * 获取层级名
   *
   * @param level 层级，必须大于0且小于当前层级
   * @return 获取层级名
   */
  public String getLevelName(int level) {
    if (level < 0 || level > currentLevel()) {
      throw new IllegalArgumentException("Input level exceeds all levels");
    }
    return levelName.get(level - 1);
  }

  /**
   * 是否跳过目录
   *
   * @param path 目录路径
   * @return 如果 {@code true}为跳过，否则 {@code false}
   */
  protected boolean isSkipDirectory(Path path) {
    return isSkip(path, directoryWhitelist, ignoredDirectoryPaths, ignoredDirectoryPatterns);
  }

  /**
   * 是否跳过文件
   *
   * @param path 文件路径
   * @return 如果 {@code true}为跳过，否则 {@code false}
   */
  protected boolean isSkipFile(Path path) {
    return isSkip(path, fileWhitelist, ignoredFilePaths, ignoredFilePatterns);
  }

  /**
   * 是否跳过
   *
   * @param path 路径
   * @param whitelist 百名单
   * @param ignoredPaths 忽略路径
   * @param ignoredPatterns 忽略路径匹配模式
   * @return 如果 {@code true}为跳过，否则 {@code false}
   */
  protected boolean isSkip(
      Path path, Set<Path> whitelist, Set<Path> ignoredPaths, Set<String> ignoredPatterns) {
    if (whitelist != null) {
      if (whitelist.contains(path)) {
        return false;
      }
    }
    if (ignoredPaths != null) {
      if (ignoredPaths.contains(path)) {
        return true;
      }
    }
    if (ignoredPatterns != null) {
      String pathString = path.toString();
      for (String ignoredDirectoryPattern : ignoredPatterns) {
        if (pathString.matches(ignoredDirectoryPattern)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void reset() {
    currentLevel = 0;
    totalFile = 0;
  }

  @Override
  public void clear() {
    directoryWhitelist = null;
    fileWhitelist = null;
    ignoredDirectoryPaths = null;
    ignoredFilePaths = null;
    ignoredDirectoryPatterns = null;
    ignoredFilePatterns = null;
  }

  public boolean isOpenStatics() {
    return isOpenStatics;
  }

  public Map<String, Integer> getCatalogAmount() {
    return catalogAmount;
  }

  public int getTotalDirectory() {
    return totalDirectory;
  }

  public int getTotalFile() {
    return totalFile;
  }

  public int getTotalFailureFile() {
    return totalFailureFile;
  }

  // ---------------------------------------------------------------------------

  public Set<Path> getDirectoryWhitelist() {
    return directoryWhitelist;
  }

  public void setDirectoryWhitelist(Set<Path> directoryWhitelist) {
    this.directoryWhitelist = directoryWhitelist;
  }

  public Set<Path> getFileWhitelist() {
    return fileWhitelist;
  }

  public void setFileWhitelist(Set<Path> fileWhitelist) {
    this.fileWhitelist = fileWhitelist;
  }

  public Set<Path> getIgnoredDirectoryPaths() {
    return ignoredDirectoryPaths;
  }

  public void setIgnoredDirectoryPaths(Set<Path> ignoredDirectoryPaths) {
    this.ignoredDirectoryPaths = ignoredDirectoryPaths;
  }

  public Set<Path> getIgnoredFilePaths() {
    return ignoredFilePaths;
  }

  public void setIgnoredFilePaths(Set<Path> ignoredFilePaths) {
    this.ignoredFilePaths = ignoredFilePaths;
  }

  public Set<String> getIgnoredDirectoryPatterns() {
    return ignoredDirectoryPatterns;
  }

  public void setIgnoredDirectoryPatterns(Set<String> ignoredDirectoryPatterns) {
    this.ignoredDirectoryPatterns = ignoredDirectoryPatterns;
  }

  public Set<String> getIgnoredFilePatterns() {
    return ignoredFilePatterns;
  }

  public void setIgnoredFilePatterns(Set<String> ignoredFilePatterns) {
    this.ignoredFilePatterns = ignoredFilePatterns;
  }
}
