package potatoxf.helper.api.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.api.Tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 可缓存映射文件
 *
 * @author potatoxf
 * @date 2022/7/13
 */
public abstract class MappingFileCached<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MappingFileCached.class);
  /** 缓存实例 */
  private final AtomicReference<T> cache = new AtomicReference<>();
  /** 可缓存映射文件夹 */
  private final MappingFolderCached cachedFolderMapping;
  /** 资源名称 */
  private final String resourceName;

  /**
   * @param cachedFolderMapping 可缓存映射文件夹
   * @param resourceName 资源名称
   */
  public MappingFileCached(
      @Nonnull MappingFolderCached cachedFolderMapping, @Nonnull String resourceName) {
    this.cachedFolderMapping = Objects.requireNonNull(cachedFolderMapping);
    this.resourceName = Tools.safePath(false, false, Objects.requireNonNull(resourceName));
  }

  /**
   * 资源名称
   *
   * @return 返回资源名称
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * 获取外部文件路径
   *
   * @return 返回外部文件路径
   */
  public String getOuterFilePath() {
    return cachedFolderMapping.getOuterPath(resourceName);
  }

  /**
   * 获取内置文件路径
   *
   * @return 返回内置文件路径
   */
  public String getBuiltFilePath() {
    return cachedFolderMapping.getBuiltPath(resourceName);
  }

  /**
   * 获取数据
   *
   * @return {@code T}实例对象
   */
  public T getData() {
    try {
      if (cachedFolderMapping.isChange(resourceName)) {
        T t = resolveFileData(cachedFolderMapping.getInputStream(resourceName));
        if (t != null) {
          cache.set(t);
        }
        return t;
      }
    } catch (Throwable e) {
      // 解析错误时还时使用上一次数据
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn(
            "The Error occurs when parsing the resource file ["
                + getOuterFilePath()
                + "] , or use the last parsing result",
            e);
      }
    }
    return cache.get();
  }

  /**
   * 解析文件数据实体对象
   *
   * @param inputStream 文件输入流
   * @return 返回解析后的对象
   * @throws IOException 读取文件发生了异常
   */
  @Nullable
  protected abstract T resolveFileData(InputStream inputStream) throws IOException;
}
