package terraqo.arbiter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 资源位置类，用于封装不同类型的资源路径（类路径、系统文件路径、JAR包内路径、ZIP包内路径）。
 * 该类提供了静态工厂方法来创建不同来源的资源位置实例，并统一管理资源的URI列表。
 *
 * @author Terraqo
 */
public final class ResourceLocation {

    /**
     * 资源的路径字符串，可能是相对路径或绝对路径，取决于资源类型。
     */
    private final String path;

    /**
     * 资源对应的URI列表。对于类路径资源，可能包含多个URI；对于文件、JAR或ZIP资源，通常只包含一个URI。
     */
    private final List<URI> locations;

    /**
     * 资源的类型，标识资源来源于类路径、系统文件、JAR包还是ZIP包。
     */
    private final ResourceType resourceType;

    /**
     * 创建文件对象，不指定编码。
     * 内部委托给 {@link #createFile(String, String)} 方法，使用默认处理逻辑。
     *
     * @param target 目标文件路径字符串
     * @return File 对象
     * @throws ResourceException 当路径为空或无效时抛出
     */
    public static File createFile(String target) throws ResourceException {
        return ResourceLocation.createFile(target, null);
    }

    /**
     * 根据目标路径和可选的编码创建文件对象。
     * 如果提供了编码，则尝试将目标字符串按照指定编码转换为字节，再使用默认字符集解码为新的字符串路径。
     * 若转换过程中发生异常，则回退使用原始路径。
     * 此方法主要用于处理路径字符串中可能存在的编码问题。
     *
     * @param target   目标文件路径字符串
     * @param encoding 可选的字符编码名称，用于重新编码路径字符串。若为 null，则直接使用原始路径
     * @return File 对象
     * @throws ResourceException 当目标路径为 null、空字符串或仅包含空白字符时抛出
     */
    public static File createFile(String target, String encoding) throws ResourceException {
        if (target == null || (target = target.trim()).isEmpty()) {
            throw new ResourceException("System target file cannot be null or empty");
        }
        String pathString = target;
        if (encoding != null) {
            try {
                // 尝试使用指定编码将字符串转为字节，再用默认字符集转回字符串，以处理可能的编码不一致问题
                pathString = new String(target.getBytes(encoding), Charset.defaultCharset());
            } catch (Throwable e) {
                // 如果编码转换失败，保留原始路径字符串
                pathString = target;
            }
        }
        return new File(pathString);
    }

    /**
     * 创建 Path 对象，不指定编码。
     * 内部委托给 {@link #createPath(String, String)} 方法，使用默认处理逻辑。
     *
     * @param target 目标路径字符串
     * @return Path 对象
     * @throws ResourceException 当路径为空或无效时抛出
     */
    public static Path createPath(String target) throws ResourceException {
        return ResourceLocation.createPath(target, null);
    }

    /**
     * 根据目标路径和可选的编码创建 Path 对象。
     * 如果提供了编码，则尝试将目标字符串按照指定编码转换为字节，再使用默认字符集解码为新的字符串路径。
     * 若转换过程中发生异常，则回退使用原始路径。
     * 此方法主要用于处理路径字符串中可能存在的编码问题。
     *
     * @param target   目标路径字符串
     * @param encoding 可选的字符编码名称，用于重新编码路径字符串。若为 null，则直接使用原始路径
     * @return Path 对象
     * @throws ResourceException 当目标路径为 null、空字符串或仅包含空白字符时抛出
     */
    public static Path createPath(String target, String encoding) throws ResourceException {
        if (target == null || (target = target.trim()).isEmpty()) {
            throw new ResourceException("System target path cannot be null or empty");
        }
        String pathString = target;
        if (encoding != null) {
            try {
                // 尝试使用指定编码将字符串转为字节，再用默认字符集转回字符串，以处理可能的编码不一致问题
                pathString = new String(target.getBytes(encoding), Charset.defaultCharset());
            } catch (Throwable e) {
                // 如果编码转换失败，保留原始路径字符串
                pathString = target;
            }
        }
        return Path.of(pathString);
    }

    /**
     * 从类路径中加载资源。
     * 使用指定的ClassLoader查找给定路径下的所有资源，并将它们转换为URI列表。
     *
     * @param classLoader 用于加载资源的ClassLoader，不能为null
     * @param path        资源在类路径中的相对路径，不能为null或空
     * @return ResourceLocation 实例，包含找到的资源URI列表
     * @throws ResourceException 当ClassLoader为空、路径无效、未找到资源或URI转换失败时抛出
     */
    public static ResourceLocation ofClassPath(ClassLoader classLoader, String path) throws ResourceException {
        if (classLoader == null) {
            throw new ResourceException("ClassLoader cannot be null for path: " + path);
        }
        if (path == null || path.isEmpty()) {
            throw new ResourceException("Resource path cannot be null or empty");
        }

        Enumeration<URL> resources;
        try {
            // 通过ClassLoader获取所有匹配路径的资源URL
            resources = classLoader.getResources(path);
        } catch (Throwable e) {
            throw new ResourceException("Failed to load resources from classpath: " + path, e);
        }

        if (!resources.hasMoreElements()) {
            throw new ResourceException("Resource not found in classpath: " + path);
        }

        List<URI> list = new LinkedList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            URI uri = null;
            try {
                // 将URL转换为URI，忽略无法转换的无效URI
                uri = url.toURI();
            } catch (Throwable ignored) {
                // Ignore invalid URIs
            }
            if (uri != null) {
                list.add(uri);
            }
        }

        if (list.isEmpty()) {
            throw new ResourceException("No valid URIs found for resource: " + path);
        }

        return new ResourceLocation(path, list, ResourceType.CLASS_PATH);
    }

    /**
     * 从系统文件路径加载资源。
     * 验证指定路径的文件是否存在且为普通文件，然后将其转换为URI。
     *
     * @param path 系统文件的绝对或相对路径，不能为null或空
     * @return ResourceLocation 实例，包含该文件的URI
     * @throws ResourceException 当路径为空、文件不存在或不是普通文件时抛出
     */
    public static ResourceLocation ofSystemPath(String path, String encoding) throws ResourceException {
        File file = ResourceLocation.createFile(path, encoding);
        if (!file.exists()) {
            throw new ResourceException("File does not exist: " + path);
        }
        if (!file.isFile()) {
            throw new ResourceException("Path is not a file: " + path);
        }

        // 将文件转换为URI并封装
        URI uri = file.toURI();
        List<URI> list = Collections.singletonList(uri);
        return new ResourceLocation(path, list, ResourceType.SYSTEM_PATH);
    }

    /**
     * 从JAR包中加载指定条目资源。
     * 构建JAR协议的URI，并验证条目是否存在于JAR文件中。
     *
     * @param path  JAR文件的路径，不能为null或空
     * @param entry JAR包内的条目路径（如 /com/example/MyClass.class），不能为null或空
     * @return ResourceLocation 实例，包含指向JAR条目的URI
     * @throws ResourceException 当路径或条目为空、JAR文件不存在、条目不存在或URI格式错误时抛出
     */
    public static ResourceLocation ofJarPath(String path, String entry) throws ResourceException {
        File target = ResourceLocation.createFile(path, null);
        if (entry == null || entry.isEmpty()) {
            throw new ResourceException("Entry location cannot be null or empty");
        }
        if (!target.exists()) {
            throw new ResourceException("JAR file does not exist: " + path);
        }
        if (!target.isFile()) {
            throw new ResourceException("Path is not a file: " + path);
        }

        // 规范化条目路径，确保以 '/' 开头
        String normalizedLocation = entry.startsWith("/") ? entry : "/" + entry;
        // 构建JAR协议URI: jar:file:/path/to/file.jar!/entry/path
        String uriString = "jar:" + target.toURI() + "!" + normalizedLocation;

        URI uri;
        try {
            uri = URI.create(uriString);
        } catch (Exception e) {
            throw new ResourceException("Invalid JAR URI format: " + uriString, e);
        }

        Path entryPath;
        // 打开JAR文件系统以验证条目是否存在
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            entryPath = fs.getPath(normalizedLocation);
            if (!Files.exists(entryPath)) {
                throw new ResourceException("Entry does not exist in JAR: " + normalizedLocation);
            }
        } catch (Throwable e) {
            throw new ResourceException("Failed to access JAR entry: " + normalizedLocation, e);
        }

        List<URI> list = Collections.singletonList(uri);
        return new ResourceLocation(uriString, list, ResourceType.JAR_PATH);
    }

    /**
     * 从ZIP包中加载指定条目资源。
     * 构建JAR协议URI（ZIP也使用jar:协议），并验证条目是否存在于ZIP文件中。
     *
     * @param path  ZIP文件的路径，不能为null或空
     * @param entry ZIP包内的条目路径，不能为null或空
     * @return ResourceLocation 实例，包含指向ZIP条目的URI
     * @throws ResourceException 当路径或条目为空、ZIP文件不存在、条目不存在或URI格式错误时抛出
     */
    public static ResourceLocation ofZipPath(String path, String entry) throws ResourceException {
        File target = ResourceLocation.createFile(path, null);
        if (entry == null || entry.isEmpty()) {
            throw new ResourceException("Entry location cannot be null or empty");
        }
        if (!target.exists()) {
            throw new ResourceException("ZIP file does not exist: " + path);
        }
        if (!target.isFile()) {
            throw new ResourceException("Path is not a file: " + path);
        }

        // 规范化条目路径，确保以 '/' 开头
        String normalizedLocation = entry.startsWith("/") ? entry : "/" + entry;
        // 构建JAR协议URI: jar:file:/path/to/file.zip!/entry/path
        String uriString = "jar:" + target.toURI() + "!" + normalizedLocation;

        URI uri;
        try {
            uri = URI.create(uriString);
        } catch (Exception e) {
            throw new ResourceException("Invalid ZIP URI format: " + uriString, e);
        }

        Path entryPath;
        // 打开ZIP文件系统以验证条目是否存在
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            entryPath = fs.getPath(normalizedLocation);
            if (!Files.exists(entryPath)) {
                throw new ResourceException("Entry does not exist in ZIP: " + normalizedLocation);
            }
        } catch (Throwable e) {
            throw new ResourceException("Failed to access ZIP entry: " + normalizedLocation, e);
        }

        List<URI> list = Collections.singletonList(uri);
        return new ResourceLocation(uri.toString(), list, ResourceType.ZIP_PATH);
    }

    /**
     * 私有构造函数，用于创建ResourceLocation实例。
     *
     * @param path         资源路径字符串
     * @param locations    资源URI列表，将被包装为不可变列表
     * @param resourceType 资源类型
     */
    private ResourceLocation(String path, List<URI> locations, ResourceType resourceType) {
        this.path = Objects.requireNonNull(path);
        this.locations = Collections.unmodifiableList(Objects.requireNonNull(locations));
        this.resourceType = Objects.requireNonNull(resourceType);
    }

    /**
     * 获取资源的路径字符串。
     *
     * @return 资源路径
     */
    public String path() {
        return path;
    }

    /**
     * 获取资源的第一个URI。
     * 对于类路径资源，返回找到的第一个匹配资源的URI；对于文件、JAR或ZIP资源，返回其对应的唯一URI。
     *
     * @return 资源的URI
     * @throws NoSuchElementException 如果资源位置列表为空
     */
    public URI uri() {
        return locations.getFirst();
    }

    /**
     * 将资源的第一个URI转换为URL对象。
     *
     * @return 资源的URL
     * @throws MalformedURLException  如果URI格式不正确或无法转换为URL
     * @throws NoSuchElementException 如果资源位置列表为空
     */
    public URL url() throws MalformedURLException {
        return locations.getFirst().toURL();
    }

    /**
     * 获取资源的所有URI位置。
     *
     * @return 不可变的URI列表
     */
    public List<URI> locations() {
        return locations;
    }

    /**
     * 获取资源的类型。
     *
     * @return 资源类型枚举值
     */
    public ResourceType resourceType() {
        return resourceType;
    }

    /**
     * 返回资源的字符串表示，包含所有URI位置，每个URI占一行。
     *
     * @return 资源的字符串描述
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<URI> iterator = locations.iterator();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
        }
        while (iterator.hasNext()) {
            sb.append('\n').append(iterator.next());
        }
        return sb.toString();
    }
}
