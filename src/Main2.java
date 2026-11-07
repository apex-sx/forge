import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.CRC32;
import java.io.IOException;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) throws Throwable {
        method1();
    }

    private static void method1() {
        Path sourceDir = Paths.get("D:\\DCIM");
        Path outDir = Paths.get("D:\\pppppp");

        if (!Files.isDirectory(sourceDir)) {
            System.out.println("目录不存在: " + sourceDir);
            return;
        }

        try {
            if (!Files.exists(outDir)) {
                Files.createDirectories(outDir);
            }

            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString();

                    int dotIdx = fileName.lastIndexOf('.');
                    if (dotIdx <= 0) {
                        return FileVisitResult.CONTINUE;
                    }
                    String ext = fileName.substring(dotIdx + 1).toLowerCase();

                    BasicFileAttributes fileAttrs = Files.readAttributes(file, BasicFileAttributes.class);
                    LocalDateTime creationTime = LocalDateTime.ofInstant(
                            fileAttrs.lastModifiedTime().toInstant(),
                            java.time.ZoneId.systemDefault());

                    String dateStr = creationTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                    CRC32 crc32Calc = new CRC32();
                    byte[] buffer = new byte[8192];
                    try (InputStream is = Files.newInputStream(file)) {
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            crc32Calc.update(buffer, 0, bytesRead);
                        }
                    }
                    String hexHash = String.format("%08X", crc32Calc.getValue());

                    String newFileName = dateStr + "_" + hexHash + "." + ext;
                    String yearMonth = dateStr.substring(0, 6);
                    String targetDirName = yearMonth + "__";

                    Path targetDir = outDir.resolve(targetDirName);
                    if (!Files.exists(targetDir)) {
                        Files.createDirectories(targetDir);
                        System.out.println("创建目录: " + targetDir);
                    }

                    Path targetPath = targetDir.resolve(newFileName);
                    if (Files.exists(targetPath)) {
                        System.out.println("目标文件已存在，跳过: " + newFileName);
                        return FileVisitResult.CONTINUE;
                    }

                    Files.move(file, targetPath, StandardCopyOption.ATOMIC_MOVE);
                    System.out.println("移动: " + file + " -> " + targetDirName + "/" + newFileName);

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("完成！");
    }
}
