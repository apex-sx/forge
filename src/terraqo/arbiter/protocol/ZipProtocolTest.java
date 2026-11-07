package terraqo.arbiter.protocol;


import terraqo.arbiter.protocol.URLHandlerFactory;
import terraqo.arbiter.protocol.zip.ZipURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * ZIP协议URL处理器的测试类
 *
 * @author Terraqo
 */
public class ZipProtocolTest {

    /**
     * 主测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== ZIP Protocol URL Handler Test ===\n");

        // 初始化URL处理器工厂
        URLHandlerFactory.initialize();
        System.out.println("✓ URLHandlerFactory initialized successfully\n");

        // 创建测试用的ZIP文件
        String testZipPath = createTestZipFile();
        
        if (testZipPath != null) {
            System.out.println("✓ Test ZIP file created: " + testZipPath + "\n");
            
            // 执行各种测试
            testZipUrlReading(testZipPath);
            testZipUrlContentType(testZipPath);
            testZipUrlContentLength(testZipPath);
            testInvalidZipEntry(testZipPath);
            testInvalidZipFile();
            
            // 清理测试文件
            //cleanupTestFile(testZipPath);
            System.out.println("\n✓ Test completed successfully!");
        } else {
            System.err.println("✗ Failed to create test ZIP file");
        }
    }

    /**
     * 创建测试用的ZIP文件
     */
    private static String createTestZipFile() {
        try {
            String zipPath = "test_sample.zip";
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            // 添加文本文件条目
            ZipEntry textEntry = new ZipEntry("readme.txt");
            zos.putNextEntry(textEntry);
            String textContent = "Hello from ZIP!\nThis is a test file.";
            zos.write(textContent.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            // 添加JSON文件条目
            ZipEntry jsonEntry = new ZipEntry("data.json");
            zos.putNextEntry(jsonEntry);
            String jsonContent = "{\"name\": \"test\", \"value\": 123}";
            zos.write(jsonContent.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            // 添加XML文件条目
            ZipEntry xmlEntry = new ZipEntry("config.xml");
            zos.putNextEntry(xmlEntry);
            String xmlContent = "<?xml version=\"1.0\"?><root><item>test</item></root>";
            zos.write(xmlContent.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            // 添加子目录中的文件
            ZipEntry subEntry = new ZipEntry("subdir/nested.txt");
            zos.putNextEntry(subEntry);
            String subContent = "Nested file content";
            zos.write(subContent.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            zos.close();
            fos.close();
            
            return zipPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 测试从ZIP文件中读取内容
     */
    private static void testZipUrlReading(String zipPath) {
        System.out.println("--- Test 1: Reading content from ZIP ---");
        
        String[] testEntries = {
            "readme.txt",
            "data.json",
            "config.xml",
            "subdir/nested.txt"
        };

        for (String entry : testEntries) {
            String zipUrl = "zip:file:///" + zipPath + "!/" + entry;
            try {
                URL url = new URL(zipUrl);
                System.out.println("URL: " + url);
                System.out.println(url.getHost());
                System.out.println(url.getPath());

                try (InputStream is = url.openStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    
                    System.out.println("  Entry: " + entry);
                    System.out.println("  Content: " + content.toString().trim());
                    System.out.println("  ✓ Success\n");
                }
            } catch (IOException e) {
                System.err.println("  ✗ Failed to read: " + entry);
                System.err.println("  Error: " + e.getMessage() + "\n");
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * 测试获取内容类型
     */
    private static void testZipUrlContentType(String zipPath) {
        System.out.println("--- Test 2: Content Type Detection ---");
        
        String[] testEntries = {
            "readme.txt",
            "data.json",
            "config.xml"
        };

        for (String entry : testEntries) {
            String zipUrl = "zip:///" + zipPath + "!/" + entry;
            try {
                URL url = new URL(zipUrl);
                ZipURLConnection connection = (ZipURLConnection) url.openConnection();
                connection.connect();
                
                String contentType = connection.getContentType();
                System.out.println("  Entry: " + entry);
                System.out.println("  Content-Type: " + contentType);
                
                connection.close();
                System.out.println("  ✓ Success\n");
            } catch (IOException e) {
                System.err.println("  ✗ Failed: " + entry);
                System.err.println("  Error: " + e.getMessage() + "\n");
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * 测试获取内容长度
     */
    private static void testZipUrlContentLength(String zipPath) {
        System.out.println("--- Test 3: Content Length ---");
        
        String zipUrl = "zip:///" + zipPath + "!/readme.txt";
        try {
            URL url = new URL(zipUrl);
            ZipURLConnection connection = (ZipURLConnection) url.openConnection();
            connection.connect();
            
            int contentLength = connection.getContentLength();
            System.out.println("  Entry: readme.txt");
            System.out.println("  Content-Length: " + contentLength + " bytes");
            
            connection.close();
            System.out.println("  ✓ Success\n");
        } catch (IOException e) {
            System.err.println("  ✗ Failed");
            System.err.println("  Error: " + e.getMessage() + "\n");
            e.printStackTrace(System.err);
        }
    }

    /**
     * 测试访问不存在的条目
     */
    private static void testInvalidZipEntry(String zipPath) {
        System.out.println("--- Test 4: Invalid ZIP Entry ---");
        
        String zipUrl = "zip:///" + zipPath + "!/nonexistent.txt";
        try {
            URL url = new URL(zipUrl);
            
            try (InputStream is = url.openStream()) {
                System.err.println("  ✗ Should have thrown IOException");
            }
        } catch (IOException e) {
            System.out.println("  Expected error caught: " + e.getMessage());
            System.out.println("  ✓ Correctly handled invalid entry\n");
            e.printStackTrace(System.err);
        }
    }

    /**
     * 测试访问不存在的ZIP文件
     */
    private static void testInvalidZipFile() {
        System.out.println("--- Test 5: Invalid ZIP File ---");
        
        String zipUrl = "zip:///nonexistent.zip!/file.txt";
        try {
            URL url = new URL(zipUrl);
            
            try (InputStream is = url.openStream()) {
                System.err.println("  ✗ Should have thrown IOException");
            }
        } catch (IOException e) {
            System.out.println("  Expected error caught: " + e.getMessage());
            System.out.println("  ✓ Correctly handled invalid ZIP file\n");
        }
    }

    /**
     * 清理测试文件
     */
    private static void cleanupTestFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                System.out.println("✓ Cleanup: Deleted test file " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to cleanup test file: " + e.getMessage());
        }
    }
}
