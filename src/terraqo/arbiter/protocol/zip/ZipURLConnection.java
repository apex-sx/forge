package terraqo.arbiter.protocol.zip;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZIP协议的URL连接实现，支持通过zip://协议访问ZIP文件中的条目
 * <p>
 * URL格式: zip:///path/to/file.zip!/entry/path
 * 或者: jar:file:/path/to/file.zip!/entry/path
 *
 * @author Terraqo
 */
public class ZipURLConnection extends URLConnection implements AutoCloseable {

    private URL jarFileURL;
    private String entryName;

    /**
     * The connection to the JAR file URL, if the connection has been
     * initiated. This should be set by connect.
     */
    protected URLConnection jarFileURLConnection;
    private final String zipPath;
    private final String entryPath;
    private ZipFile zipFile;
    private ZipEntry zipEntry;

    /**
     * 构造ZIP URL连接
     *
     * @param url ZIP协议的URL
     */
    protected ZipURLConnection(URL url) {
        super(url);
        String filePart = url.getFile();

        int separatorIndex = filePart.indexOf("!/");
        if (separatorIndex == -1) {
            throw new IllegalArgumentException("Invalid ZIP URL format, missing '!/' separator: " + url);
        }

        this.zipPath = filePart.substring(0, separatorIndex);
        this.entryPath = filePart.substring(separatorIndex + 2);
    }

    /**
     * 连接到ZIP文件并定位条目
     *
     * @throws IOException 如果连接失败或条目不存在
     */
    @Override
    public void connect() throws IOException {
        if (connected) {
            return;
        }

        String actualZipPath = zipPath;
        // 处理 file: 前缀
        if (actualZipPath.startsWith("file:")) {
            actualZipPath = actualZipPath.substring(5);
        }

        // 标准化路径分隔符，确保在Windows和Unix下都能正确解析
        // ZipFile 通常期望系统兼容的路径，但去除前导斜杠有助于避免绝对路径问题
        if (actualZipPath.startsWith("/") || actualZipPath.startsWith("\\")) {
            // 简单去除前导分隔符，视具体文件系统需求而定，这里保持原有逻辑但更健壮
            actualZipPath = actualZipPath.substring(1);
        }

        try {
            zipFile = new ZipFile(actualZipPath);
        } catch (IOException e) {
            throw new IOException("Failed to open ZIP file: " + actualZipPath, e);
        }

        String normalizedEntry = entryPath;
        // 标准化条目路径，去除前导斜杠
        if (normalizedEntry.startsWith("/") || normalizedEntry.startsWith("\\")) {
            normalizedEntry = normalizedEntry.substring(1);
        }

        zipEntry = zipFile.getEntry(normalizedEntry);

        if (zipEntry == null) {
            // 关闭已打开的流以避免资源泄露
            try {
                zipFile.close();
            } catch (IOException ex) {
                // 忽略关闭异常，抛出主要的未找到异常
            }
            throw new IOException("ZIP entry not found: " + entryPath + " in " + actualZipPath);
        }

        connected = true;
    }

    /**
     * 获取ZIP条目的输入流
     *
     * @return ZIP条目的输入流
     * @throws IOException 如果无法获取输入流
     */
    @Override
    public InputStream getInputStream() throws IOException {
        if (!connected) {
            connect();
        }
        if (zipFile == null || zipEntry == null) {
            throw new IOException("Connection is not properly established");
        }
        return zipFile.getInputStream(zipEntry);
    }

    /**
     * 获取内容长度
     *
     * @return 内容长度，未知返回-1
     */
    @Override
    public int getContentLength() {
        try {
            if (!connected) {
                connect();
            }
            if (zipEntry != null) {
                long size = zipEntry.getSize();
                // ZipEntry.getSize() 返回 -1 如果大小未知
                if (size >= 0 && size <= Integer.MAX_VALUE) {
                    return (int) size;
                }
            }
            return -1;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 获取内容类型
     *
     * @return 内容类型
     */
    @Override
    public String getContentType() {
        if (entryPath == null) {
            return "application/octet-stream";
        }

        String name = entryPath.toLowerCase();
        if (name.endsWith(".txt")) {
            return "text/plain";
        } else if (name.endsWith(".xml")) {
            return "application/xml";
        } else if (name.endsWith(".json")) {
            return "application/json";
        } else if (name.endsWith(".properties")) {
            return "text/plain";
        } else if (name.endsWith(".html") || name.endsWith(".htm")) {
            return "text/html";
        } else if (name.endsWith(".css")) {
            return "text/css";
        } else if (name.endsWith(".js")) {
            return "application/javascript";
        }
        return "application/octet-stream";
    }

    /**
     * 关闭连接，释放资源
     *
     * @throws IOException 如果关闭失败
     */
    @Override
    public void close() throws IOException {
        if (zipFile != null) {
            zipFile.close();
            zipFile = null; // 帮助垃圾回收
            zipEntry = null;
        }
    }
}
