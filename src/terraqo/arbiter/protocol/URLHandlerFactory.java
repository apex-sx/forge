package terraqo.arbiter.protocol;


import terraqo.arbiter.protocol.zip.ZipURLStreamHandler;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * ZIP协议的URL流处理器工厂，用于注册和管理自定义协议处理器
 *
 * @author Terraqo
 */
public final class URLHandlerFactory implements URLStreamHandlerFactory {
    private static final URLHandlerFactory INSTANCE = new URLHandlerFactory();
    private static final Map<String, URLStreamHandler> handlers = new HashMap<>();

    static {
        handlers.put("zip", ZipURLStreamHandler.INSTANCE);
    }

    private URLHandlerFactory() {
    }

    /**
     * 注册自定义的协议处理器
     *
     * @param protocol 协议名称
     * @param handler  协议处理器
     */
    public static void register(String protocol, URLStreamHandler handler) {
        if (handler != null) {
            handlers.put(protocol.toLowerCase(), handler);
        }
    }

    /**
     * 初始化ZIP协议支持
     * 注意：URL.setURLStreamHandlerFactory只能调用一次
     */
    public static void initialize() {
        try {
            URL.setURLStreamHandlerFactory(URLHandlerFactory.INSTANCE);
        } catch (Throwable e) {
            System.err.println("Warning: URLStreamHandlerFactory already set. ZIP protocol may still work if previously registered.");
        }
    }

    /**
     * 根据协议名称获取对应的流处理器
     *
     * @param protocol 协议名称（如 "zip"）
     * @return 对应的URL流处理器，如果不支持则返回null
     */
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol != null) {
            return handlers.get(protocol.toLowerCase());
        }
        return null;
    }
}
