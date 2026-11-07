package potatoxf.helper.api.lang;

import potatoxf.helper.api.HelperOnStream;
import potatoxf.helper.api.Tools;
import potatoxf.helper.api.base.ImmutablePair;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带有缓存文件夹映射
 *
 * <p>映射类路径下的文件和外部路径下的文件 当外部路径不存在文件时，则从类路径下文件复制到外部路径下的文件夹
 *
 * @author potatoxf
 * @date 2022/01/22
 */
public class MappingFolderCached extends MappingFolder {
  private static final int MAX_CACHE_SIZE = 1024 * 16;
  private final Map<String, ImmutablePair<Long, byte[]>> cache = new ConcurrentHashMap<>();
  private final int maxCacheSize;

  /**
   * @param builtRootDirPath 根类路径
   * @param outerRootDirPath 根文件路径
   */
  public MappingFolderCached(String builtRootDirPath, String outerRootDirPath) {
    this(builtRootDirPath, outerRootDirPath, MAX_CACHE_SIZE);
  }

  /**
   * @param builtRootDirPath 根类路径
   * @param outerRootDirPath 根文件路径
   * @param maxCacheSize 最大缓存大小
   */
  public MappingFolderCached(String builtRootDirPath, String outerRootDirPath, int maxCacheSize) {
    super(builtRootDirPath, outerRootDirPath);
    this.maxCacheSize = Tools.safeGtEq(maxCacheSize, maxCacheSize, MAX_CACHE_SIZE);
  }

  /**
   * 读取文件输入流，如果外部流不存在则从内置类复制过去
   *
   * @param resourcePath 资源路径
   * @return {@code InputStream}
   * @throws IOException 如果读写文件发生异常
   */
  @Nonnull
  @Override
  public InputStream getInputStream(String resourcePath) throws IOException {
    byte[] data;
    File outerFile = getOuterFile(resourcePath);
    ImmutablePair<Long, byte[]> cacheBlock = cache.get(resourcePath);
    if (cacheBlock == null) {
      data = cached(resourcePath, outerFile);
      load(resourcePath, data);
    } else {
      if (outerFile.lastModified() == cacheBlock.getKey()) {
        data = cacheBlock.getValue();
      } else {
        data = cached(resourcePath, outerFile);
        load(resourcePath, data);
      }
    }
    return new ByteArrayInputStream(data);
  }

  /**
   * @param resourcePath
   * @return
   * @throws IOException
   */
  public boolean isChange(String resourcePath) throws IOException {
    ImmutablePair<Long, byte[]> cacheBlock = cache.get(resourcePath);
    if (cacheBlock == null) {
      return true;
    } else {
      return getOuterFile(resourcePath).lastModified() != cacheBlock.getKey();
    }
  }

  /**
   * @param resourcePath
   * @param data
   */
  protected void load(String resourcePath, byte[] data) {}

  /**
   * @param resourcePath
   * @param outerFile
   * @return
   * @throws IOException
   */
  private byte[] cached(String resourcePath, File outerFile) throws IOException {
    byte[] data = HelperOnStream.readAllBytes(outerFile);
    if (outerFile.length() < maxCacheSize) {
      cache.put(resourcePath, new ImmutablePair<>(outerFile.lastModified(), data));
    } else {
      cache.remove(resourcePath);
    }
    return data;
  }
}
