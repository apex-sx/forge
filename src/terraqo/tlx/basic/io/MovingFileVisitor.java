package pxf.toolkit.basic.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 移动文件
 *
 * @author potatoxf
 * @date 2021/3/6
 */
public class MovingFileVisitor extends FilterFileVisitor {

  private static final Logger LOG = LoggerFactory.getLogger(MovingFileVisitor.class);
  /** 移动到的根路径 */
  private final Path rootDirectory;
  /** 是否编号 */
  private boolean isSerialNumber = false;
  /** 是否移动后删除 */
  private boolean isMovingAfterDelete = false;

  private MovingFileVisitor(Path rootDirectory) {
    super(true);
    this.rootDirectory = rootDirectory;
  }

  public static MovingFileVisitor of(String rootDirectory) {
    return MovingFileVisitor.of(Path.of(rootDirectory));
  }

  public static MovingFileVisitor of(Path rootDirectory) {
    return new MovingFileVisitor(rootDirectory);
  }

  @Override
  protected FileVisitResult doPreVisitDirectory(Path dir, BasicFileAttributes attrs)
      throws IOException {
    return super.doPreVisitDirectory(dir, attrs);
  }

  @Override
  protected FileVisitResult doVisitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String fileName = (isSerialNumber ? currentFileCount() + "." : "") + file.getFileName();
    Path newFile = rootDirectory.resolve(fileName);
    if (isMovingAfterDelete) {
      Files.move(file, newFile);
      if (LOG.isDebugEnabled()) {
        LOG.debug(String.format("Moving file from [%s] to [%s]", file, newFile));
      }
    } else {
      Files.copy(file, newFile);
      if (LOG.isDebugEnabled()) {
        LOG.debug(String.format("Copying file from [%s] to [%s]", file, newFile));
      }
    }
    return super.doVisitFile(file, attrs);
  }

  public MovingFileVisitor setSerialNumber(boolean serialNumber) {
    isSerialNumber = serialNumber;
    return this;
  }

  public MovingFileVisitor setMovingAfterDelete(boolean movingAfterDelete) {
    isMovingAfterDelete = movingAfterDelete;
    return this;
  }
}
