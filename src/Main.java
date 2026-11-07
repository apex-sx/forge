import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

/**
 *
 * @author ApexSX
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("I:\\personal\\新建文本文档.txt");
        Path out = Paths.get("I:\\personal\\新建文本文档111.7z");
        String s = Files.readString(path);
        // Deleted:String hex = s.replace(" ", "")
        // Deleted:        .replace("!", "")
        // Deleted:        .replace("Q", "0")
        // Deleted:        .replace("Y", "1")
        // Deleted:        .replace("B", "R");
        String hex = s.replaceAll("\\s+", "")
                .replace("!", "")
                .replace("Q", "0")
                .replace("Y", "1")
                .replace("R", "B");

        if ((hex.length() & 1) != 0) {
            throw new IllegalArgumentException("hex 字符串长度必须为偶数，当前为 " + hex.length());
        }

        byte[] bytes = HexFormat.of().parseHex(hex);
        Files.write(out, bytes);

        System.out.println("已写入 " + bytes.length + " 字节 -> " + out);
    }
}
