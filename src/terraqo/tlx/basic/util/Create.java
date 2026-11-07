package pxf.toolkit.basic.util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.IOFlowException;

/**
 * @author potatoxf
 * @date 2021/4/17
 */
public class Create {

  private static final AtomicBoolean NEW_FILE_LOCK = new AtomicBoolean(false);
  private static final Logger LOG = LoggerFactory.getLogger(Create.class);

  private Create() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 创建临时文件
   *
   * @param fileExtension 文件扩展
   * @return 返回 {@code File}
   */
  public static File temporaryFileAndDeleteOnExit(String fileExtension) {
    return Create.temporaryFileAndDeleteOnExit(Share.SNOWFLAKE.nextId(), fileExtension);
  }

  /**
   * 创建临时文件
   *
   * @param id ID
   * @param fileExtension 文件扩展
   * @return 返回 {@code File}
   */
  public static File temporaryFileAndDeleteOnExit(long id, String fileExtension) {
    File temporaryFile = Create.temporaryFile(id, fileExtension);
    temporaryFile.deleteOnExit();
    return temporaryFile;
  }

  /**
   * 创建临时文件
   *
   * @param fileExtension 文件扩展
   * @return 返回 {@code File}
   */
  public static File temporaryFile(String fileExtension) {
    return Create.temporaryFile(Share.SNOWFLAKE.nextId(), fileExtension);
  }

  /**
   * 创建临时文件
   *
   * @param id ID
   * @param fileExtension 文件扩展
   * @return 返回 {@code File}
   */
  public static File temporaryFile(long id, String fileExtension) {
    try {
      if (fileExtension != null && !fileExtension.startsWith(".")) {
        fileExtension = "." + fileExtension;
      }
      String filename = System.currentTimeMillis() + "-" + id;
      return File.createTempFile(filename, fileExtension);
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  @Nonnull
  public static File file(@Nonnull String file) {
    return new File(file);
  }

  @Nullable
  public static File shroudFileWithSilent(@Nonnull String file) {
    try {
      return shroudFile(file);
    } catch (IOException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(String.format("Failed to create file for [%s]", file), e);
      }
    }
    return null;
  }

  @Nonnull
  public static File shroudFile(@Nonnull String file) throws IOException {
    File result = new File(file);
    if (result.exists()) {
      return result;
    }
    directory(result.getParent());
    result.createNewFile();
    return result;
  }

  @Nullable
  public static File uniqueFileWithSilent(@Nonnull String file) {
    try {
      return uniqueFile(file);
    } catch (IOException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(String.format("Failed to create file for [%s]", file), e);
      }
    }
    return null;
  }

  @Nonnull
  public static File uniqueFile(@Nonnull String file) throws IOException {
    File result = new File(file);
    directory(result.getParent());
    NEW_FILE_LOCK.compareAndSet(false, true);
    if (!result.exists()) {
      boolean resultNewFile = result.createNewFile();
      if (resultNewFile) {
        return result;
      }
    } else {
      String filepath = result.toString();
      StringBuilder sb = new StringBuilder(filepath.length() + 10);
      sb.append(filepath);
      int dotI = Get.extensionIndex(sb);
      boolean hasSuffix = dotI != -1;
      int numI = 1;
      if (hasSuffix) {
        numI += dotI;
        sb.insert(dotI, "(0)");
      } else {
        numI += sb.length();
        sb.append("(0)");
      }
      int i = -1;
      do {
        i++;
        sb.replace(numI, numI + Get.len(i), String.valueOf(i + 1));
        result = new File(sb.toString());
        if (!result.exists()) {
          boolean resultNewFile = result.createNewFile();
          if (resultNewFile) {
            break;
          }
        }
      } while (true);
    }
    NEW_FILE_LOCK.compareAndSet(true, false);
    return result;
  }

  @Nonnull
  public static File directory(@Nonnull String directory) {
    File result = new File(directory);
    if (result.exists()) {
      return result;
    }
    if (result.mkdirs()) {
      return result;
    }
    if (!result.exists()) {
      throw new RuntimeException("Failed to create directory for [" + directory + "]");
    }
    return result;
  }
}
