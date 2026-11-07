package terraqo.arbiter;


import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;

/**
 * 测试 jar:file: URL 的不同格式
 */
public class JarUrlTest {

    public static void main(String[] args) {
        try {
            // 1. 创建测试用的 JAR 文件
            String testJarPath = createTestJar();
            System.out.println("测试 JAR 文件创建成功: " + testJarPath);
            System.out.println("=" .repeat(60));

            // 2. 测试三种不同的 URL 格式
            //testJarUrlFormat1(testJarPath);  // jar:file:/path!/entry
            //testJarUrlFormat2(testJarPath);  // jar:file:///path!/entry (标准格式)
            testJarUrlFormat3(testJarPath);  // jar:file://localhost/path!/entry (网络格式)

            // 清理测试文件
            new File(testJarPath).delete();
            System.out.println("\n测试完成，临时文件已清理");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式1: jar:file:/path!/entry (两个斜杠 + 绝对路径)
     */
    private static void testJarUrlFormat1(String jarPath) {
        System.out.println("\n【测试格式1】jar:file:/path!/entry");
        System.out.println("-".repeat(60));
        
        // Windows 路径转换：C:/xxx -> /C:/xxx
        String urlPath = jarPath.replace("\\", "/");
        if (urlPath.startsWith("/")) {
            urlPath = urlPath.substring(1);
        }
        
        String urlString = "jar:file:/" + urlPath + "!/test.txt";
        System.out.println("URL: " + urlString);
        
        try {
            URL url = new URL(urlString);
            testUrlConnection(url);
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
    }

    /**
     * 格式2: jar:file:///path!/entry (三个斜杠 - 标准格式)
     */
    private static void testJarUrlFormat2(String jarPath) {
        System.out.println("\n【测试格式2】jar:file:///path!/entry (推荐)");
        System.out.println("-".repeat(60));
        
        String urlPath = jarPath.replace("\\", "/");
        String urlString = "jar:file:///" + urlPath + "!/test.txt";
        System.out.println("URL: " + urlString);
        
        try {
            URL url = new URL(urlString);
            testUrlConnection(url);
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
    }

    /**
     * 格式3: jar:file://localhost/path!/entry (四个斜杠 - 网络格式)
     */
    private static void testJarUrlFormat3(String jarPath) {
        System.out.println("\n【测试格式3】jar:file://localhost/path!/entry");
        System.out.println("-".repeat(60));
        
        String urlPath = jarPath.replace("\\", "/");
        String urlString = "jar:file://localhost/" + urlPath + "!/test.txt";
        System.out.println("URL: " + urlString);
        
        try {
            URL url = new URL(urlString);
            System.out.println(url.getHost());
            System.out.println(url.getPath());
            System.out.println(url.getFile());
            testUrlConnection(url);
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
    }

    /**
     * 测试 URL 连接并读取内容
     */
    private static void testUrlConnection(URL url) {
        try (InputStream is = url.openStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead = is.read(buffer);
            String content = new String(buffer, 0, bytesRead);
            System.out.println("✅ 成功读取内容: " + content.trim());
            System.out.println("   Protocol: " + url.getProtocol());
            System.out.println("   File: " + url.getFile());
        } catch (Exception e) {
            System.out.println("❌ 读取失败: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    /**
     * 创建测试用的 JAR 文件
     */
    private static String createTestJar() throws Exception {
        File tempJar = File.createTempFile("test", ".jar");
        tempJar.deleteOnExit();
        
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempJar))) {
            // 添加一个测试文件
            ZipEntry entry = new ZipEntry("test.txt");
            jos.putNextEntry(entry);
            jos.write("Hello from JAR!".getBytes());
            jos.closeEntry();
            
            // 添加另一个测试文件
            ZipEntry entry2 = new ZipEntry("config.properties");
            jos.putNextEntry(entry2);
            jos.write("key=value\nname=test".getBytes());
            jos.closeEntry();
        }
        
        return tempJar.getAbsolutePath();
    }
}
