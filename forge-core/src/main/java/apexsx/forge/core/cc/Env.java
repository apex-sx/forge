package apexsx.forge.core.cc;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author Terraqo
 */
public class Env {

    /**
     * 跨环境获取项目根路径（绝对路径）
     * 非容器：项目根目录
     * Tomcat容器：webapp/你的项目 根目录
     */
    public static String getProjectRoot() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 1. 获取 classpath 根（classes目录）
        URL url = classLoader.getResource("");
        while (url == null && (classLoader = classLoader.getParent()) != null) {
            url = classLoader.getResource("");
        }
        if (url == null) {
            throw new RuntimeException("无法获取项目根目录");
        }
        String path;
        try {
            path = new File(url.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("", e);
        }
        // 2. 反向推导根目录（自动适配 容器/非容器）
        File root;
        if (path.contains("WEB-INF")) {
            // Tomcat容器：classes -> WEB-INF -> 项目根
            root = new File(path).getParentFile().getParentFile();
        } else {
            // 本地非容器：target/classes -> 项目根
            root = new File(path);
        }
        return root.getAbsolutePath();

    }

    public static void main(String[] args) {
        System.out.println(getProjectRoot());
    }
}
