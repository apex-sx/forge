package terraqo.arbiter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 资源优先级获取器
 * 负责根据预定义的优先级顺序查找并返回资源
 */
public class PriorityResourceLocator {

    public static void main(String[] args) {

    }
    /**
     * 定义资源查找的优先级枚举
     */
    public enum PriorityLevel {
        // 1. 类路径 META-INF (最低优先级，通常是库的默认值)
        CLASSPATH_META_INF,
        
        // 2. 类路径根目录 (中等优先级，通常是应用的默认打包值)
        CLASSPATH_ROOT,
        
        // 3. 项目根目录 (高优先级，通常是部署时的覆盖值)
        FILESYSTEM_PROJECT_ROOT,
        
        // 4. 用户自定义绝对路径 (最高优先级)
        FILESYSTEM_CUSTOM
    }

    /**
     * 基础相对路径 (相对于项目启动路径)
     */
    private static final String DEFAULT_RELATIVE_PATH = "config";

    /**
     * 获取所有存在的资源 URL 列表
     * 返回顺序即为优先级顺序（从低到高），方便调用者进行流式合并/覆盖
     * 
     * @param resourcePath 资源相对路径 (例如 "app-config.properties")
     * @param customPath 用户自定义的绝对路径 (可选)
     * @return 按优先级排序的 URL 列表
     */
    public List<URL> getAllResources(String resourcePath, String customPath) {
        List<URL> urls = new ArrayList<>();

        // 1. 查找 classpath:/META-INF/{resourcePath}
        findClasspathResource(urls, "META-INF/" + resourcePath);

        // 2. 查找 classpath:/{resourcePath}
        findClasspathResource(urls, resourcePath);

        // 3. 查找 项目根目录/{resourcePath}
        findFileSystemResource(urls, Paths.get(resourcePath));
        
        // 额外：通常也会在 项目根目录/config/{resourcePath} 找一下
        findFileSystemResource(urls, Paths.get(DEFAULT_RELATIVE_PATH, resourcePath));

        // 4. 查找 用户自定义路径
        if (customPath != null && !customPath.trim().isEmpty()) {
            findFileSystemResource(urls, Paths.get(customPath));
        }

        return urls;
    }

    /**
     * 获取最高优先级的资源
     * 一旦找到高优先级的资源，立即返回，忽略低优先级的
     * 
     * @param resourcePath 资源相对路径
     * @param customPath 用户自定义绝对路径
     * @return 优先级最高的资源 Optional
     */
    public Optional<URL> getHighestPriorityResource(String resourcePath, String customPath) {
        // 倒序查找，从最高优先级开始
        
        // 1. 检查自定义路径
        if (customPath != null && !customPath.trim().isEmpty()) {
            Optional<URL> url = findSingleFileSystemResource(Paths.get(customPath));
            if (url.isPresent()) return url;
        }

        // 2. 检查项目根目录
        Optional<URL> url = findSingleFileSystemResource(Paths.get(resourcePath));
        if (url.isPresent()) return url;
        url = findSingleFileSystemResource(Paths.get(DEFAULT_RELATIVE_PATH, resourcePath));
        if (url.isPresent()) return url;

        // 3. 检查类路径根目录
        url = findSingleClasspathResource(resourcePath);
        if (url.isPresent()) return url;

        // 4. 检查 META-INF
        return findSingleClasspathResource("META-INF/" + resourcePath);
    }

    // --- 内部辅助查找方法 ---

    private void findClasspathResource(List<URL> list, String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(path);
        if (url != null) {
            list.add(url);
        }
    }

    private Optional<URL> findSingleClasspathResource(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return Optional.ofNullable(cl.getResource(path));
    }

    private void findFileSystemResource(List<URL> list, Path path) {
        if (Files.exists(path) && Files.isRegularFile(path)) {
            try {
                list.add(path.toUri().toURL());
            } catch (IOException e) {
                // 忽略转换错误
            }
        }
    }

    private Optional<URL> findSingleFileSystemResource(Path path) {
        if (Files.exists(path) && Files.isRegularFile(path)) {
            try {
                return Optional.of(path.toUri().toURL());
            } catch (IOException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}