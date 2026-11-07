package terraqo.arbiter.protocol.zip;


import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * ZIP协议的URL流处理器，负责创建ZIP协议的URL连接
 *
 * @author Terraqo
 */
public class ZipURLStreamHandler extends URLStreamHandler {
    public static final ZipURLStreamHandler INSTANCE = new ZipURLStreamHandler();

    protected ZipURLStreamHandler() {
    }

    /**
     * 打开ZIP协议的URL连接
     *
     * @param u ZIP协议的URL
     * @return ZIP URL连接对象
     * @throws IOException 如果无法打开连接
     */
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new ZipURLConnection(u);
    }
    
    /**
     * 解析ZIP协议的URL路径
     *
     * @param u 要解析的URL
     * @param spec 规范字符串
     * @param start 起始位置
     * @param limit 限制位置
     */
    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        super.parseURL(u, spec, start, limit);
    }
}
