package terraqo.arbiter;


import java.util.*;

/**
 * Content-Type 与文件类型映射工具类
 * 提供 MIME Type 与文件扩展名的双向映射，不区分大小写
 *
 * @author Terraqo
 */
public final class ResourceContentType {

    private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE = new TreeMap<>(String::compareToIgnoreCase);
    private static final Map<String, List<String>> CONTENT_TYPE_TO_EXTENSIONS = new TreeMap<>(String::compareToIgnoreCase);

    static {
        ResourceContentType.addMapping("txt", "text/plain");
        ResourceContentType.addMapping("html", "text/html");
        ResourceContentType.addMapping("htm", "text/html");
        ResourceContentType.addMapping("css", "text/css");
        ResourceContentType.addMapping("js", "application/javascript");
        ResourceContentType.addMapping("json", "application/json");
        ResourceContentType.addMapping("xml", "application/xml");
        ResourceContentType.addMapping("pdf", "application/pdf");
        ResourceContentType.addMapping("doc", "application/msword");
        ResourceContentType.addMapping("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        ResourceContentType.addMapping("xls", "application/vnd.ms-excel");
        ResourceContentType.addMapping("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ResourceContentType.addMapping("ppt", "application/vnd.ms-powerpoint");
        ResourceContentType.addMapping("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        ResourceContentType.addMapping("zip", "application/zip");
        ResourceContentType.addMapping("rar", "application/x-rar-compressed");
        ResourceContentType.addMapping("tar", "application/x-tar");
        ResourceContentType.addMapping("gz", "application/gzip");
        ResourceContentType.addMapping("7z", "application/x-7z-compressed");
        ResourceContentType.addMapping("jpg", "image/jpeg");
        ResourceContentType.addMapping("jpeg", "image/jpeg");
        ResourceContentType.addMapping("png", "image/png");
        ResourceContentType.addMapping("gif", "image/gif");
        ResourceContentType.addMapping("bmp", "image/bmp");
        ResourceContentType.addMapping("svg", "image/svg+xml");
        ResourceContentType.addMapping("webp", "image/webp");
        ResourceContentType.addMapping("ico", "image/x-icon");
        ResourceContentType.addMapping("mp3", "audio/mpeg");
        ResourceContentType.addMapping("wav", "audio/wav");
        ResourceContentType.addMapping("ogg", "audio/ogg");
        ResourceContentType.addMapping("flac", "audio/flac");
        ResourceContentType.addMapping("mp4", "video/mp4");
        ResourceContentType.addMapping("avi", "video/x-msvideo");
        ResourceContentType.addMapping("mov", "video/quicktime");
        ResourceContentType.addMapping("wmv", "video/x-ms-wmv");
        ResourceContentType.addMapping("flv", "video/x-flv");
        ResourceContentType.addMapping("webm", "video/webm");
        ResourceContentType.addMapping("csv", "text/csv");
        ResourceContentType.addMapping("rtf", "application/rtf");
        ResourceContentType.addMapping("yaml", "application/x-yaml");
        ResourceContentType.addMapping("yml", "application/x-yaml");
        ResourceContentType.addMapping("properties", "text/plain");
        ResourceContentType.addMapping("log", "text/plain");
        ResourceContentType.addMapping("md", "text/markdown");
        ResourceContentType.addMapping("markdown", "text/markdown");
        ResourceContentType.addMapping("java", "text/x-java-source");
        ResourceContentType.addMapping("py", "text/x-python");
        ResourceContentType.addMapping("c", "text/x-c");
        ResourceContentType.addMapping("cpp", "text/x-c++");
        ResourceContentType.addMapping("h", "text/x-c");
        ResourceContentType.addMapping("sql", "application/sql");
        ResourceContentType.addMapping("sh", "application/x-sh");
        ResourceContentType.addMapping("bat", "application/x-bat");
        ResourceContentType.addMapping("exe", "application/x-msdownload");
        ResourceContentType.addMapping("dll", "application/x-msdownload");
        ResourceContentType.addMapping("so", "application/x-sharedlib");
        ResourceContentType.addMapping("jar", "application/java-archive");
        ResourceContentType.addMapping("war", "application/java-archive");
        ResourceContentType.addMapping("ear", "application/java-archive");
        ResourceContentType.addMapping("ttf", "font/ttf");
        ResourceContentType.addMapping("otf", "font/otf");
        ResourceContentType.addMapping("woff", "font/woff");
        ResourceContentType.addMapping("woff2", "font/woff2");
        ResourceContentType.addMapping("eot", "application/vnd.ms-fontobject");
    }

    private static void addMapping(String extension, String contentType) {
        EXTENSION_TO_CONTENT_TYPE.put(extension, contentType);
        CONTENT_TYPE_TO_EXTENSIONS.computeIfAbsent(contentType, k -> new LinkedList<>()).add(extension);
    }

    /**
     * 动态添加自定义的映射关系
     *
     * @param extension   文件扩展名
     * @param contentType Content-Type 字符串
     */
    public static synchronized void registerMapping(String extension, String contentType) {
        if (extension == null || (extension = extension.trim()).isEmpty()) {
            throw new IllegalArgumentException("Invalid extension: " + extension);
        }
        if (contentType == null || (contentType = contentType.trim()).isEmpty()) {
            throw new IllegalArgumentException("Invalid content type: " + contentType);
        }
        ResourceContentType.addMapping(extension, contentType);
    }

    private ResourceContentType() {
    }

    /**
     * 根据文件扩展名获取对应的 Content-Type
     *
     * @param extension 文件扩展名（不区分大小写，可以带点或不带点）
     * @return Content-Type 字符串，如果未找到则返回 "application/octet-stream"
     */
    public static String getContentTypeByExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "application/octet-stream";
        }

        String ext = extension.toLowerCase();
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }

        return EXTENSION_TO_CONTENT_TYPE.getOrDefault(ext, "application/octet-stream");
    }

    /**
     * 根据 Content-Type 获取对应的文件扩展名列表
     *
     * @param contentType Content-Type 字符串（不区分大小写）
     * @return 文件扩展名列表，如果未找到则返回空列表
     */
    public static List<String> getExtensionsByContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return Collections.emptyList();
        }

        String lowerContentType = contentType.toLowerCase();
        List<String> extensions = CONTENT_TYPE_TO_EXTENSIONS.get(lowerContentType);

        return extensions != null ? Collections.unmodifiableList(extensions) : Collections.emptyList();
    }

    /**
     * 根据 Content-Type 获取首选的文件扩展名
     *
     * @param contentType Content-Type 字符串（不区分大小写）
     * @return 首选文件扩展名，如果未找到则返回 null
     */
    public static String getPreferredExtensionByContentType(String contentType) {
        List<String> extensions = getExtensionsByContentType(contentType);
        return extensions.isEmpty() ? null : extensions.getFirst();
    }

    /**
     * 检查是否支持指定的文件扩展名
     *
     * @param extension 文件扩展名（不区分大小写）
     * @return 如果支持返回 true，否则返回 false
     */
    public static boolean isSupportedExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }

        String ext = extension.toLowerCase();
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }

        return EXTENSION_TO_CONTENT_TYPE.containsKey(ext);
    }

    /**
     * 检查是否支持指定的 Content-Type
     *
     * @param contentType Content-Type 字符串（不区分大小写）
     * @return 如果支持返回 true，否则返回 false
     */
    public static boolean isSupportedContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return false;
        }

        return CONTENT_TYPE_TO_EXTENSIONS.containsKey(contentType.toLowerCase());
    }

    /**
     * 获取所有支持的扩展名与 Content-Type 的映射关系
     *
     * @return 不可修改的映射表
     */
    public static Map<String, String> getAllMappings() {
        return Collections.unmodifiableMap(EXTENSION_TO_CONTENT_TYPE);
    }
}

