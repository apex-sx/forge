/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.core;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 基于字符流的行处理器，支持任意字符编码、反斜杠续行与转义解码。
 *
 * <p>每个逻辑行依次经过以下处理阶段：
 * <ol>
 *     <li><b>物理行读取</b>：由 {@link BufferedReader#readLine()} 按 {@code \n}、{@code \r}、{@code \r\n} 切分，
 *     行尾分隔符不会出现在返回内容中</li>
 *     <li><b>续行合并</b>：行尾存在奇数个反斜杠时，去掉该续行符后与下一物理行拼接；
 *     若下一行以空白开头则跳过其行首空白，遵循 {@link java.util.Properties} 规范</li>
 *     <li><b>转义解码</b>：解码 {@code \t}、{@code \r}、{@code \n}、{@code \f}、{@code \\}、{@code \\uXXXX}；
 *     不认识的转义序列、非法或不完整的 {@code \\u} 序列一律原样保留，不抛异常</li>
 *     <li><b>内容过滤</b>：调用 {@link #filterLineBefore(String, int)}，返回 {@code false} 表示跳过该行</li>
 *     <li><b>内容改写</b>：调用 {@link #mapLine(String, int, StringBuilder)}，返回 {@code null} 表示丢弃该行</li>
 *     <li><b>内容过滤</b>：调用 {@link #filterLineAfter(String, int)}，返回 {@code false} 表示跳过该行</li>
 * </ol>
 *
 * <p>字符编码完全由调用方显式指定（UTF-8、GBK、GB18030、Big5 等），本类不做任何探测；
 * 若文件带 BOM，需由调用方在包装流时自行剥离，否则 BOM 会成为首个行内容的一部分。
 *
 * <p>推荐用法：
 * <pre>{@code
 * try (LineIterator it = new LineIterator(Files.newInputStream(path), StandardCharsets.UTF_8)) {
 *     while (it.hasNext()) {
 *         String line = it.next();
 *     }
 * }
 * }</pre>
 *
 * <p>注意：
 * <ul>
 *     <li>由于行尾反斜杠被当作续行符，需要在行尾表达字面反斜杠时必须写成 {@code \\}</li>
 *     <li>解码产生的 {@code \r}、{@code \n} 会出现在返回的行内容中，此时“行”不再是物理行</li>
 *     <li>本类非线程安全，同一实例不可被多个线程共享；{@link #close()} 可重复调用，关闭后不再返回任何行</li>
 *     <li>读取过程中发生 {@link IOException} 时，会先关闭底层流，再以 {@link UncheckedIOException} 抛出</li>
 * </ul>
 *
 * @author Terraqo
 */
public class LineProcessor implements Iterator<String>, Closeable {
    /**
     * 字符缓冲区大小，与 {@link BufferedReader} 的默认缓冲区保持一致，
     * 避免使用默认值以外的隐式约定；需要调优时只需修改此常量
     */
    private static final int BUFFER_SIZE = 8192;
    /**
     * 复用缓冲区的初始容量，可覆盖绝大多数 properties 行，减少扩容次数
     */
    private static final int INITIAL_CAPACITY = 256;
    /**
     * 复用缓冲区保留容量的上限，超过该阈值时丢弃当前 {@link StringBuilder} 并重新分配，
     * 避免超长行导致缓冲区被永久撑大，进而长期占用堆内存
     */
    private static final int MAX_CAPACITY = 4 * 1024;
    /**
     * 底层行读取器，负责按物理行切分并屏蔽 {@code \n}、{@code \r}、{@code \r\n} 的差异
     */
    private final BufferedReader bufferedReader;
    /**
     * 复用缓冲区保留容量的上限，超过该阈值时丢弃当前 {@link StringBuilder} 并重新分配，
     * 避免超长行导致缓冲区被永久撑大，进而长期占用堆内存
     */
    private final int maxCapacity;
    /**
     * 复用缓冲区的初始容量，可覆盖绝大多数 properties 行，减少扩容次数
     */
    private final int initialCapacity;
    /**
     * 复用的字符拼接缓冲区，供 {@link #readLine(StringBuilder)} 续行合并与 {@link #unescapeLine(String, int, StringBuilder)} 转义解码共享，
     * 避免每行都创建新的 {@link StringBuilder}；
     * 使用前需调用 {@code setLength(0)} 清空，使用后由 {@link #resetBuilder()} 统一回收，
     * 因此本类在同一时刻仅允许一个方法占用该缓冲区，与类的非线程安全约束一致
     */
    private StringBuilder builder;
    /**
     * 预读行，内容是已完成转义解码，非 null 表示 {@link #hasNext()} 已确认存在下一行，
     * {@link #next()} 直接取用即可，避免重复读取与重复解码
     */
    private String cachedLine;
    /**
     * 当前物理行号，从 {@code 1} 开始计数；每次底层 {@link BufferedReader#readLine()} 成功读取后递增，
     * 发生续行合并时记录的是最后一个被合并的物理行行号
     */
    private int row;
    /**
     * 是否已到达流末尾。置为 true 后 {@link #hasNext()} 直接返回 false，不再访问底层流
     */
    private boolean finished;
    /**
     * 是否已关闭。用于保证底层流最多被关闭一次，使 {@link #close()} 具备幂等性
     */
    private boolean closed;

    /**
     * 使用字节流构造处理器，按指定编码解码。
     * <p>编码必须由调用方显式给出，本类不做 BOM 探测，也不回退到平台默认编码
     *
     * @param inputStream     字节流，不能为空
     * @param charset         字符编码，不能为空
     * @param initialCapacity 初始容量
     * @param maxCapacity     最大容量
     * @throws IllegalArgumentException 任一参数为 null 时抛出
     */
    public LineProcessor(InputStream inputStream, Charset charset, int initialCapacity, int maxCapacity) {
        this(new InputStreamReader(inputStream, charset), maxCapacity, initialCapacity);
    }

    /**
     * 使用已解码的 {@link Reader} 构造处理器。
     * <p>若传入的已是 {@link BufferedReader} 则直接复用（避免重复缓冲），
     * 否则内部包装为 {@link #BUFFER_SIZE} 缓冲的 {@link BufferedReader}
     *
     * @param reader          字符流，不能为空
     * @param initialCapacity 初始容量
     * @param maxCapacity     最大容量
     * @throws IllegalArgumentException 参数不合法时抛出
     */
    public LineProcessor(Reader reader, int initialCapacity, int maxCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("The initialCapacity must be greater than 0");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("The maxCapacity must be greater than 0");
        }
        if (maxCapacity < initialCapacity) {
            throw new IllegalArgumentException("The maxCapacity must be greater than the initialCapacity");
        }
        this.initialCapacity = initialCapacity;
        this.maxCapacity = maxCapacity;
        // 参数非空校验
        Objects.requireNonNull(reader, "The reader must be not null");
        // 判断是否为BufferedReader实例，以避免重复包装缓冲
        if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader) reader;
        } else {
            // 包装为指定缓冲区大小的BufferedReader
            this.bufferedReader = new BufferedReader(reader, BUFFER_SIZE);
        }
    }

    /**
     * 从字节流加载属性键值对并填充到指定的 Map 中，按指定编码解码字符。
     *
     * <p>该方法为 {@link #parseKeyValuePairs(InputStream, Charset, Map, String)} 的便捷重载，
     * 使用默认的 {@code "="} 作为键值分隔符。内部将字节流包装为 {@link InputStreamReader} 后委托执行。
     *
     * <p>解析规则：
     * <ul>
     *     <li>每行按第一个未被转义的分隔符拆分为键和值</li>
     *     <li>分隔符前的反斜杠会被识别为转义，被转义的分隔符不作为拆分依据</li>
     *     <li>不含分隔符的行整体作为键，值为空字符串</li>
     *     <li>分隔符位于行首时，键为空字符串，整行内容作为值</li>
     * </ul>
     *
     * @param inputStream 字节流，不能为空
     * @param charset     字符编码，不能为空
     * @param properties  用于接收解析结果的 Map，不能为空；解析结果会直接追加到该 Map 中
     * @return 填充后的 {@code properties} 引用，便于链式调用
     * @throws IOException 读取过程中发生 I/O 错误时抛出
     */
    public static Map<String, String> loadProperties(InputStream inputStream, Charset charset, Map<String, String> properties) throws IOException {
        return parseKeyValuePairs(inputStream, charset, properties, null);
    }

    /**
     * 从字符流加载属性键值对并填充到指定的 Map 中，使用默认的键值分隔符。
     *
     * <p>该方法为 {@link #parseKeyValuePairs(Reader, Map, String)} 的便捷重载，
     * 使用默认的 {@code "="} 作为键值分隔符。逐行读取内容（自动过滤注释行与块注释），
     * 按分隔符将每行拆分为键值对。
     *
     * <p>解析规则：
     * <ul>
     *     <li>每行按第一个未被转义的分隔符拆分为键和值</li>
     *     <li>分隔符前的反斜杠会被识别为转义，被转义的分隔符不作为拆分依据</li>
     *     <li>不含分隔符的行整体作为键，值为空字符串</li>
     *     <li>分隔符位于行首（索引为 0）时，键为空字符串，整行内容作为值</li>
     * </ul>
     *
     * <p>典型用法：
     * <pre>{@code
     * Map<String, String> props = new LinkedHashMap<>();
     * try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
     *     processor.loadProperties(reader, props);
     * }
     * }</pre>
     *
     * @param reader     字符流，不能为空
     * @param properties 用于接收解析结果的 Map，不能为空；解析结果会直接追加到该 Map 中
     * @return 填充后的 {@code properties} 引用，便于链式调用
     * @throws IOException 读取过程中发生 I/O 错误时抛出
     */
    public static Map<String, String> loadProperties(Reader reader, Map<String, String> properties) throws IOException {
        return parseKeyValuePairs(reader, properties, null);
    }

    /**
     * 从字节流解析键值对并填充到指定的 Map 中，按指定编码解码字符。
     *
     * <p>该方法为 {@link #parseKeyValuePairs(Reader, Map, String)} 的便捷重载，
     * 内部将字节流包装为 {@link InputStreamReader} 后委托执行。
     *
     * <p>解析规则：
     * <ul>
     *     <li>每行按第一个未被转义的分隔符拆分为键和值</li>
     *     <li>分隔符前的反斜杠会被识别为转义，被转义的分隔符不作为拆分依据</li>
     *     <li>不含分隔符的行整体作为键，值为空字符串</li>
     *     <li>分隔符位于行首时，键为空字符串，整行内容作为值</li>
     * </ul>
     *
     * @param inputStream 字节流，不能为空
     * @param charset     字符编码，不能为空
     * @param properties  用于接收解析结果的 Map，不能为空；解析结果会直接追加到该 Map 中
     * @param separator   键值分隔符；为 {@code null} 或空白时回退到默认值 {@code "="}
     * @return 填充后的 {@code properties} 引用，便于链式调用
     * @throws IOException 读取过程中发生 I/O 错误时抛出
     */
    public static Map<String, String> parseKeyValuePairs(InputStream inputStream, Charset charset, Map<String, String> properties, String separator) throws IOException {
        return parseKeyValuePairs(new InputStreamReader(inputStream, charset), properties, separator);
    }

    /**
     * 从字符流解析键值对并填充到指定的 Map 中，支持自定义分隔符与转义处理。
     *
     * <p>该方法逐行读取内容（自动过滤注释行与块注释），按分隔符将每行拆分为键值对。
     * 分隔符的识别会跳过被反斜杠转义的位置，确保像 {@code "key\=name=value"} 这样的内容
     * 能被正确解析为键 {@code "key=name"}、值 {@code "value"}。
     *
     * <p>解析规则：
     * <ul>
     *     <li>每行按第一个未被转义的分隔符拆分为键和值</li>
     *     <li>分隔符前的反斜杠会被识别为转义，被转义的分隔符不作为拆分依据</li>
     *     <li>不含分隔符的行整体作为键，值为空字符串</li>
     *     <li>分隔符位于行首（索引为 0）时，键为空字符串，整行内容作为值</li>
     *     <li>分隔符为 {@code null} 或空白时，回退到默认值 {@code "="}</li>
     * </ul>
     *
     * <p>典型用法：
     * <pre>{@code
     * Map<String, String> props = new LinkedHashMap<>();
     * try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
     *     processor.parseKeyValuePairs(reader, props, "=");
     * }
     * }</pre>
     *
     * @param reader     字符流，不能为空
     * @param properties 用于接收解析结果的 Map，不能为空；解析结果会直接追加到该 Map 中
     * @param separator  键值分隔符；为 {@code null} 或空白时回退到默认值 {@code "="}
     * @return 填充后的 {@code properties} 引用，便于链式调用
     * @throws IOException 读取过程中发生 I/O 错误时抛出
     */
    public static Map<String, String> parseKeyValuePairs(Reader reader, Map<String, String> properties, String separator) throws IOException {
        separator = separator == null || separator.isBlank() ? "=" : separator;
        try (LineProcessor lineProcessor = createRemarkFilter(reader)) {
            while (lineProcessor.hasNext()) {
                String line = lineProcessor.next();
                int l = line.indexOf(separator);
                if (l <= 0) {
                    if (l < 0) {
                        properties.put(line, "");
                    } else {
                        properties.put("", line);
                    }
                    continue;
                }
                while (isEscapeEnded(line, l - 1)) {
                    l = line.indexOf(separator, l + separator.length());
                    if (l < 0) {
                        break;
                    }
                }
                if (l > 0) {
                    String key = line.substring(0, l);
                    String value = line.substring(l + separator.length());
                    properties.put(key, value);
                } else {
                    properties.put(line, "");
                }
            }
        }
        return properties;
    }

    /**
     * 创建启用注释过滤的处理器，按指定编码解码字节流，使用默认容量与默认注释标记。
     *
     * @param inputStream 字节流，不能为空
     * @param charset     字符编码，不能为空
     * @return 带注释过滤功能的处理器
     */
    public static LineProcessor createRemarkFilter(InputStream inputStream, Charset charset) {
        return createRemarkFilter(new InputStreamReader(inputStream, charset));
    }

    /**
     * 创建启用注释过滤的处理器，使用默认容量与默认注释标记（{@code #}、{@code /*}、{@code *}{@code /}）。
     *
     * <p>等效于 {@code Builder().reader(reader).build()}，适用于不需要自定义注释标记的场景。
     *
     * @param reader 字符流，不能为空
     * @return 带注释过滤功能的处理器
     */
    public static LineProcessor createRemarkFilter(Reader reader) {
        return new RemarkFilter(reader, INITIAL_CAPACITY, MAX_CAPACITY, "#", "/*", "*/");
    }

    /**
     * 创建启用注释过滤的处理器，按指定编码解码字节流，使用自定义注释标记与默认容量。
     * 标记为 {@code null} 或空白时回退到默认值。
     *
     * @param inputStream       字节流，不能为空
     * @param charset           字符编码，不能为空
     * @param lineCommentPrefix 单行注释前缀，为 null 或空白时使用默认值 {@code #}
     * @param blockCommentStart 块注释起始标记，为 null 或空白时使用默认值 {@code /*}
     * @param blockCommentEnd   块注释结束标记，为 null 或空白时使用默认值 {@code *}{@code /}
     * @return 带注释过滤功能的处理器
     */
    public static LineProcessor createCustomRemarkFilter(InputStream inputStream, Charset charset,
                                                         String lineCommentPrefix, String blockCommentStart, String blockCommentEnd) {
        return createCustomRemarkFilter(new InputStreamReader(inputStream, charset), lineCommentPrefix, blockCommentStart, blockCommentEnd);
    }

    /**
     * 创建启用注释过滤的处理器，使用自定义注释标记与默认容量。
     * 标记为 {@code null} 或空白时回退到默认值（{@code #}、{@code /*}、{@code *}{@code /}）。
     *
     * @param reader            字符流，不能为空
     * @param lineCommentPrefix 单行注释前缀，为 null 或空白时使用默认值 {@code #}
     * @param blockCommentStart 块注释起始标记，为 null 或空白时使用默认值 {@code /*}
     * @param blockCommentEnd   块注释结束标记，为 null 或空白时使用默认值 {@code *}{@code /}
     * @return 带注释过滤功能的处理器
     */
    public static LineProcessor createCustomRemarkFilter(Reader reader,
                                                         String lineCommentPrefix, String blockCommentStart, String blockCommentEnd) {
        // 若传入的注释标记为null或trim后为空串，则赋予默认值
        lineCommentPrefix = lineCommentPrefix == null || lineCommentPrefix.isBlank() ? "#" : lineCommentPrefix;
        blockCommentStart = blockCommentStart == null || blockCommentStart.isBlank() ? "/*" : blockCommentStart;
        blockCommentEnd = blockCommentEnd == null || blockCommentEnd.isBlank() ? "*/" : blockCommentEnd;
        return new RemarkFilter(reader, INITIAL_CAPACITY, MAX_CAPACITY, lineCommentPrefix, blockCommentStart, blockCommentEnd);
    }

    /**
     * 判断行尾是否存在续行符（奇数个反斜杠）
     *
     * @param string 行内容
     * @param end    行尾位置
     * @return 存在续行符时返回 true
     */
    private static boolean isEscapeEnded(String string, int end) {
        // 从行末最后一个字符开始检查
        int i = end;
        // 如果行空或者最后一个字符不是反斜杠，则不续行
        if (i < 0 || string.charAt(i) != '\\') {
            return false;
        }
        // 找到一个反斜杠
        boolean continued = true;
        i--;
        // 向前连续查找反斜杠，奇数个为续行，偶数个为非续行（转义后的反斜杠）
        while (i >= 0 && string.charAt(i) == '\\') {
            // 每找到一个反斜杠，反转续行状态
            continued = !continued;
            i--;
        }
        return continued;
    }

    /**
     * 判断底层流是否还有可返回的行。
     * 若读取过程中发生 IO 异常，会先关闭底层流再抛出 {@link IllegalStateException}
     *
     * @return 存在下一行时返回 true
     */
    @Override
    public final boolean hasNext() {
        // 快速路径1：如果存在缓存的预读行，说明还有下一个元素
        if (cachedLine != null) {
            return true;
        }
        // 快速路径2：如果已经标记为结束，直接返回false
        if (finished) {
            return false;
        }
        try {
            String line;
            // 循环读取并处理行，直到找到符合过滤条件的行或到达文件末尾
            while (true) {
                // 阶段1: 续行合并读取。前后重置缓冲区，保证读取前后状态干净
                resetBuilder();
                line = readLine(builder);
                resetBuilder();
                if (line == null) {
                    break;
                }
                // 阶段2: 前置过滤判断。返回false则跳过后续解码与改写，直接读取下一行
                if (!filterLineBefore(line, row)) {
                    continue;
                }
                // 阶段3: 转义解码。前后重置缓冲区，供unescapeLine内部复用
                resetBuilder();
                line = unescapeLine(line, row, builder);
                resetBuilder();
                // 阶段4: 映射改写。前后重置缓冲区，供子类(如CommentFilterLineIterator)复用
                resetBuilder();
                line = mapLine(line, row, builder);
                resetBuilder();
                // 如果改写后为null，表示丢弃该行，继续读取下一行
                if (line == null) {
                    continue;
                }
                // 阶段5: 后置过滤判断。对改写后的内容做最终筛查
                if (!filterLineAfter(line, row)) {
                    continue;
                }
                // 全部阶段通过，缓存该行并返回true
                cachedLine = line;
                return true;
            }
            // 读取到文件末尾，标记为结束
            finished = true;
            return false;
        } catch (IOException e) {
            // 发生IO异常时，尝试关闭底层流，防止资源泄漏
            try {
                close();
            } catch (IOException ignored) {
                // 关闭失败直接忽略
            }
            // 将IOException包装为IllegalStateException抛出，附带当前坐标便于定位问题行
            throw new IllegalStateException("I/O error occurred on line " + row, e);
        }
    }

    /**
     * 返回下一行
     *
     * @return 下一行内容
     * @throws NoSuchElementException 没有更多行时抛出
     */
    @Override
    public final String next() {
        // 检查是否有下一行，如果没有则抛出异常
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        // 取出缓存的行
        String currentLine = cachedLine;
        // 清空缓存，以便后续hasNext()继续预读
        cachedLine = null;
        return currentLine;
    }

    /**
     * 关闭底层 {@link Reader}，可重复调用
     *
     * @throws IOException 关闭失败
     */
    @Override
    public final void close() throws IOException {
        // 幂等性检查：如果已经关闭，直接返回
        if (this.closed) {
            return;
        }
        // 标记为已关闭状态
        this.closed = true;
        // 标记为已结束状态，防止后续操作继续读取
        this.finished = true;
        // 清空缓存行
        this.cachedLine = null;
        // 关闭底层的BufferedReader
        this.bufferedReader.close();
    }

    /**
     * 判断处理器是否已关闭
     *
     * @return 已关闭时返回 true
     */
    public final boolean closed() {
        return closed;
    }

    /**
     * 对已完成续行合并的行执行反斜杠转义解码。
     *
     * <p>支持的转义序列：{@code \t}、{@code \r}、{@code \n}、{@code \\} 以及 {@code \\uXXXX}；
     * 其他不认识的转义（如 {@code \f}、{@code \x}）以及非法或不完整的 {@code \\u} 序列一律原样保留，不抛异常，
     * 以便调用方能感知源文件中的错误内容。
     *
     * @param line          已合并续行的原始行内容，不会为 null
     * @param lineNumber    当前行号
     * @param stringBuilder 复用的字符缓冲区
     * @return 解码后的行内容；若原行不含反斜杠则返回入参本身
     */
    protected String unescapeLine(String line, int lineNumber, StringBuilder stringBuilder) {
        int length = line.length();
        // 查找行内第一个反斜杠的位置
        int start = line.indexOf('\\');
        // 快速路径：如果不存在反斜杠，无需解码，直接返回原字符串避免拷贝
        if (start >= 0) {
            // 将首个反斜杠之前的普通字符直接追加到缓冲区
            stringBuilder.append(line, 0, start);
            // 从第一个反斜杠位置开始遍历处理
            for (int i = start; i < length; ) {
                char c = line.charAt(i);
                // 如果当前字符不是反斜杠，说明是普通字符段
                if (c != '\\') {
                    // 查找当前段中下一个反斜杠的位置
                    int nextBackslash = line.indexOf('\\', i + 1);
                    // 如果后续没有反斜杠，将剩余字符全部追加并结束循环
                    if (nextBackslash < 0) {
                        stringBuilder.append(line, i, length);
                        break;
                    }
                    // 追加当前普通字符段
                    stringBuilder.append(line, i, nextBackslash);
                    // 移动指针到下一个反斜杠位置继续处理
                    i = nextBackslash;
                    continue;
                }
                // 边界条件：反斜杠是行末最后一个字符，无法构成转义序列，原样追加并结束
                if (i + 1 >= length) {
                    stringBuilder.append('\\');
                    break;
                }
                // 获取反斜杠后面的字符
                char next = line.charAt(i + 1);
                // 根据转义字符类型进行相应的解码处理
                switch (next) {
                    case 't' -> {
                        // 水平制表符转义
                        stringBuilder.append('\t');
                        i += 2;
                    }
                    case 'r' -> {
                        // 回车转义
                        stringBuilder.append('\r');
                        i += 2;
                    }
                    case 'n' -> {
                        // 换行转义
                        stringBuilder.append('\n');
                        i += 2;
                    }
                    case '\\' -> {
                        // 反斜杠自身转义
                        stringBuilder.append('\\');
                        i += 2;
                    }
                    case 'u' -> {
                        // Unicode转义序列 \\uXXXX 处理
                        // 检查后续是否有足够的字符（至少需要4位十六进制）
                        if (i + 5 < length) {
                            int value = 0;
                            // 跳过 '\\u'，指向第一位十六进制字符
                            int k = i + 2;
                            // 限制范围至4位十六进制字符结束
                            int limit = i + 6;
                            // 循环解析4位十六进制字符
                            while (k < limit) {
                                char hex = line.charAt(k);
                                int digit;
                                // 优先使用手写位运算解析数字，性能优于Character.digit
                                if (hex >= '0' && hex <= '9') {
                                    digit = hex - '0';
                                    // 解析小写字母 a-f
                                } else if (hex >= 'a' && hex <= 'f') {
                                    digit = hex - 'a' + 10;
                                    // 解析大写字母 A-F
                                } else if (hex >= 'A' && hex <= 'F') {
                                    digit = hex - 'A' + 10;
                                } else {
                                    // 非常规十六进制字符，回退到Character.digit解析
                                    digit = Character.digit(hex, 16);
                                }
                                // 如果不是合法的十六进制字符，中断解析
                                if (digit < 0) {
                                    break;
                                }
                                // 累加十六进制数值
                                value = (value << 4) | digit;
                                k++;
                            }
                            // 如果成功解析了4位十六进制字符
                            if (k == limit) {
                                // 将解析出的Unicode码点转为字符追加
                                stringBuilder.append((char) value);
                                // 移动指针跳过整个 \\uXXXX 序列
                                i += 6;
                                continue;
                            }
                        }
                        // 非法或不完整的 \\u 序列原样保留，后续字符继续解析
                        stringBuilder.append('\\').append('u');
                        i += 2;
                    }
                    default -> {
                        // 不支持的转义序列原样保留
                        stringBuilder.append('\\').append(next);
                        i += 2;
                    }
                }
            }
            // 取出缓冲区中的解码结果并重置缓冲区
            line = resetBuilder();
        }
        // 返回解码后的行内容
        return line;
    }

    /**
     * 可重写方法，用于改写行内容，默认原样返回。
     * 入参为已完成续行合并与转义解码的行内容，返回值会作为最终行内容参与
     * {@link #filterLineAfter(String, int)} 过滤；返回 {@code null} 表示丢弃该行。
     *
     * @param line          待改写的行内容，不会为 null
     * @param lineNumber    当前行号
     * @param stringBuilder 复用的字符缓冲区
     * @return 改写后的行内容，返回 null 表示跳过该行
     */
    protected String mapLine(String line, int lineNumber, StringBuilder stringBuilder) {
        // 默认实现：原样返回行内容
        return line;
    }

    /**
     * 可重写方法，用于前置过滤返回的行，返回 false 的行会被跳过。
     * 入参为已完成续行合并但未转义解码的行内容，默认全部返回
     *
     * @param line       待校验的行内容
     * @param lineNumber 当前行号
     * @return 返回 true 保留该行，false 跳过
     */
    protected boolean filterLineBefore(String line, int lineNumber) {
        // 默认实现：保留所有行
        return true;
    }

    /**
     * 可重写方法，用于后置过滤返回的行，返回 false 的行会被跳过。
     * 入参为已完成续行合并与转义解码及改写的行内容，默认全部返回
     *
     * @param line       待校验的行内容
     * @param lineNumber 当前行号
     * @return 返回 true 保留该行，false 跳过
     */
    protected boolean filterLineAfter(String line, int lineNumber) {
        // 默认实现：保留所有行
        return true;
    }

    /**
     * 读取一个逻辑行，行尾奇数个反斜杠表示续行，会与下一行合并。
     * 续行时忽略下一行的行首空白，与 properties 规范保持一致
     *
     * @param stringBuilder 复用的字符缓冲区
     * @return 合并后的逻辑行内容，到达流末尾时返回 null
     * @throws IOException 读取失败
     */
    protected final String readLine(StringBuilder stringBuilder) throws IOException {
        // 读取第一行物理行
        String line = bufferedReader.readLine();
        // 如果到达文件末尾，返回null
        if (line == null) {
            return null;
        }
        int length = line.length();
        // 递增物理行号，记录当前读取到的行位置
        row++;
        // 快速路径：如果第一行没有续行符，直接返回该行，避免缓冲区操作
        if (!isEscapeEnded(line, length - 1)) {
            return line;
        }
        // 追加第一行内容，去掉行尾的续行反斜杠（所以是 length - 1）
        stringBuilder.append(line, 0, length - 1);
        // 循环读取后续物理行进行合并
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            length = line.length();
            // 每读取一个物理行，行号递增
            row++;
            int start = 0;
            // 跳过续行的行首空白字符
            while (start < length && Character.isWhitespace(line.charAt(start))) {
                start++;
            }
            // 如果跳过空白后到达行尾（空行或全空白行），结束合并
            if (start == length) {
                break;
            }
            if (!filterLineBefore(line, row)) {
                continue;
            }
            // 判断当前行是否也是续行
            boolean continued = isEscapeEnded(line, length - 1);
            if (continued) {
                // 如果也是续行，追加内容并去掉行尾的反斜杠
                stringBuilder.append(line, start, length - 1);
            } else {
                // 如果不是续行，追加全部内容并结束合并
                stringBuilder.append(line, start, length);
                break;
            }
        }
        // 取出合并后的逻辑行并重置缓冲区
        return resetBuilder();
    }

    /**
     * 取出 {@link #builder} 中已拼接的内容并回收缓冲区，供下一次 {@link #readLine(StringBuilder)} 或
     * {@link #unescapeLine(String, int, StringBuilder)} 复用。
     *
     * <p>回收策略：
     * <ul>
     *     <li>当前容量未超过 {@link #maxCapacity} 时，仅 {@code setLength(0)} 清空内容，
     *     保留已扩容的字符数组，避免下一行再次触发扩容</li>
     *     <li>已超过 {@link #maxCapacity} 时，丢弃当前 {@link StringBuilder} 并按上限重新分配，
     *     防止偶发的超长行导致缓冲区永久驻留大数组</li>
     * </ul>
     *
     * @return 缓冲区当前内容对应的字符串快照，与后续 {@link #builder} 的修改相互独立
     */
    protected final String resetBuilder() {
        String string = "";
        if (builder == null) {
            builder = new StringBuilder(initialCapacity);
        } else {
            // 将缓冲区内容转为字符串
            string = builder.toString();
            // 检查缓冲区长度是否超过最大容量限制
            if (builder.length() > maxCapacity) {
                // 超过限制，重新分配一个以上限为初始容量的StringBuilder，释放原有大数组
                builder = new StringBuilder(initialCapacity);
            } else {
                // 未超过限制，仅清空内容，保留当前底层数组以供复用
                builder.setLength(0);
            }
        }
        return string;
    }

    /**
     * 支持注释剥离的行处理器，在 {@link LineProcessor} 的续行合并与转义解码之后逐行去除注释。
     *
     * <p>可识别三类注释，标记均由构造参数指定，为空时回退到 {@code #}、{@code /*}、{@code *}{@code /} 默认值：
     * <ul>
     *     <li><b>整行单行注释</b>：行首（忽略前置内容后首个标记）出现 {@code lineCommentPrefix} 时，从前缀到行尾全部视为注释；若整行都是注释则该行被丢弃</li>
     *     <li><b>行尾单行注释</b>：有效内容之后出现 {@code lineCommentPrefix} 时，仅截断该行剩余部分，并去除因截断残留的行尾空白</li>
     *     <li><b>任意位置块注释</b>：{@code blockCommentStart} 与 {@code blockCommentEnd} 之间的内容视为注释，可出现在行内任意位置，也可跨越多行；跨行时未闭合的行整体作为注释丢弃，直至遇到结束标记</li>
     * </ul>
     *
     * <p>注释识别遵循「谁先出现谁生效」的顺序：处于块注释中时只查找结束标记，
     * 因此块注释内的 {@code lineCommentPrefix} 不会截断内容；反之单行注释内的 {@code blockCommentStart}也不会开启块注释。
     *
     * <p>块注释的跨行状态由实例字段保存，故本类继承 {@link LineProcessor} 的非线程安全约束，
     * 同一实例不可被多个线程共享，也不可交叉遍历多个数据源。
     */
    private static final class RemarkFilter extends LineProcessor {
        /**
         * 单行注释前缀，从该前缀出现处到行尾的内容都被视为注释，默认 {@code #}
         */
        private final String lineCommentPrefix;
        /**
         * 块注释起始标记，默认 {@code /*}
         */
        private final String blockCommentStart;
        /**
         * 块注释结束标记，默认 {@code *}{@code /}
         */
        private final String blockCommentEnd;
        /**
         * 是否正处于未闭合的块注释中。为 true 时，后续行在遇到 {@link #blockCommentEnd} 之前的内容全部作为注释处理，
         * 用于支持块注释跨行；该状态在 {@link #mapLine(String, int, StringBuilder)} 中随扫描推进而更新
         */
        private boolean inBlockComment;

        /**
         * 基于字符读取器的核心构造方法。
         * 对传入的注释标记进行 trim 处理，若处理后为空则使用默认值。
         *
         * @param reader            字符读取器
         * @param initialCapacity   初始容量
         * @param maxCapacity       最大容量
         * @param lineCommentPrefix 单行注释前缀
         * @param blockCommentStart 块注释起始标记
         * @param blockCommentEnd   块注释结束标记
         */
        private RemarkFilter(Reader reader, int initialCapacity, int maxCapacity,
                             String lineCommentPrefix, String blockCommentStart, String blockCommentEnd) {
            super(reader, initialCapacity, maxCapacity);
            // 若传入的注释标记为null或trim后为空串，则赋予默认值
            this.lineCommentPrefix = lineCommentPrefix;
            this.blockCommentStart = blockCommentStart;
            this.blockCommentEnd = blockCommentEnd;
        }

        /**
         * 剥离当前逻辑行中的注释内容（块注释与行尾单行注释）。
         *
         * <p>处理规则：
         * <ul>
         *     <li>不在块注释中且行内不含 {@link #blockCommentStart} 与 {@link #lineCommentPrefix} 时，
         *     直接原样返回，避免无谓的扫描与对象分配</li>
         *     <li>处于块注释中时，只查找 {@link #blockCommentEnd} 结束标记，其间内容全部丢弃；
         *     未闭合时本行剩余内容全部丢弃，块注释状态延续到下一行</li>
         *     <li>遇到 {@link #blockCommentStart} 进入块注释状态，直到匹配结束标记才恢复</li>
         *     <li>非块注释状态下遇到 {@link #lineCommentPrefix}，从该处到行尾全部截断（行尾单行注释）</li>
         *     <li>本行发生过注释剥离时，去除残留的行尾空白；若剥离后为空（处于跨行块注释中或纯注释行），
         *     返回 {@code null} 表示丢弃该行</li>
         * </ul>
         *
         * <p>注释识别遵循「谁先出现谁生效」的顺序：处于块注释中时 {@link #lineCommentPrefix} 不生效，
         * 反之单行注释截断后 {@link #blockCommentStart} 也不再扫描。
         *
         * <p>整行单行注释（行首即为 {@link #lineCommentPrefix}）的过滤仍由 {@link #filterLineBefore} 与
         * {@link #filterLineAfter} 负责，本方法主要处理行尾附加的单行注释。
         *
         * @param line          已完成续行合并与转义解码的行内容，不会为 null
         * @param lineNumber    当前行号
         * @param stringBuilder 用于拼接非注释内容的可复用 StringBuilder
         * @return 去除注释后的行内容；整行均为注释或处于跨行块注释中时返回 {@code null}
         */
        @Override
        protected String mapLine(String line, int lineNumber, StringBuilder stringBuilder) {
            // 快速路径：不在块注释中且行内不含任何注释标记，原样返回
            if (!inBlockComment && !line.contains(blockCommentStart) && !line.contains(lineCommentPrefix)) {
                return line;
            }
            int length = line.length();
            // 标记本行是否发生过注释剥离，仅在剥离后才做行尾裁剪与空行丢弃判断
            boolean stripped = false;
            int i = 0;
            while (i < length) {
                if (inBlockComment) {
                    // 处于未闭合块注释中：只查找结束标记，其间内容全部作为注释
                    int end = line.indexOf(blockCommentEnd, i);
                    if (end < 0) {
                        // 本行内未找到结束标记，剩余内容全部属于块注释，状态延续到下一行
                        stripped = true;
                        break;
                    }
                    // 找到结束标记，跳出块注释并从标记之后继续扫描
                    inBlockComment = false;
                    stripped = true;
                    i = end + blockCommentEnd.length();
                } else if (line.startsWith(blockCommentStart, i)) {
                    // 遇到块注释起始标记，进入块注释状态
                    inBlockComment = true;
                    stripped = true;
                    i += blockCommentStart.length();
                } else if (line.startsWith(lineCommentPrefix, i)) {
                    // 遇到单行注释前缀，从该处到行尾全部截断
                    stripped = true;
                    break;
                } else {
                    // 普通字符，保留并追加至缓冲区
                    stringBuilder.append(line.charAt(i));
                    i++;
                }
            }
            // 未发生任何剥离，返回原始行内容
            if (!stripped) {
                return line;
            }
            // 取出缓冲区内容并重置
            String content = resetBuilder();
            if (content.isEmpty()) {
                return null;
            }
            return content;
        }

        /**
         * 在续行合并与转义解码前，判断当前行是否应被过滤（丢弃）。
         * 若当前行跳过行首空白后以单行注释前缀开头，则视为整行注释予以丢弃。
         *
         * @param line       原始行内容
         * @param lineNumber 当前行号
         * @return 如果行应保留则返回 true，如果行应被过滤则返回 false
         */
        @Override
        protected boolean filterLineBefore(String line, int lineNumber) {
            // 只有不在块注释中时，才进行单行注释前缀的判定
            if (!inBlockComment) {
                int start = 0, length = line.length();
                // 跳过行首空白字符，以支持缩进的单行注释
                while (start < length && Character.isWhitespace(line.charAt(start))) {
                    start++;
                }
                // 如果跳过空白后以单行注释前缀开头，则过滤该行（返回 false）
                return !line.startsWith(lineCommentPrefix, start);
            }
            // 处于块注释中时，保留该行交由 mapLine 处理块注释逻辑
            return true;
        }

        /**
         * 在续行合并与转义解码后，判断当前行是否应被过滤（丢弃）。
         * 逻辑与 {@link #filterLineBefore} 一致，确保在行内容转换后仍能正确过滤整行单行注释。
         *
         * @param line       已处理的行内容
         * @param lineNumber 当前行号
         * @return 如果行应保留则返回 true，如果行应被过滤则返回 false
         */
        @Override
        protected boolean filterLineAfter(String line, int lineNumber) {
            return filterLineBefore(line, lineNumber);
        }
    }
}
