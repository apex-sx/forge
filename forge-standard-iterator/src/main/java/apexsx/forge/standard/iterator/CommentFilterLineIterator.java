package apexsx.forge.standard.iterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * 一个迭代器，用于跳过文件中的注释行，并读取有效数据。
 * 支持单行注释和块注释的处理。
 * <p>
 * 该类实现了 {@link Iterator} 接口，可以用于遍历文件中的非注释行。
 * 同时实现了 {@link AutoCloseable} 接口，确保在使用完毕后可以正确关闭资源。
 * </p>
 *
 * @author zhitron
 * @see Iterator
 * @see AutoCloseable
 */
public final class CommentFilterLineIterator implements Iterator<String>, AutoCloseable {
    /**
     * 用于读取文件内容的BufferedReader对象
     */
    private final BufferedReader reader;
    /**
     * 用于判断当前行是否为有效数据的函数接口
     */
    private final BooleanSupplier lineLoader;
    /**
     * 当前读取到的数据行
     */
    private String data = null;
    /**
     * 标识是否还有下一行数据的标志
     */
    private boolean hasNextLine = true;
    /**
     * 标识当前是否处于块注释内部的标志
     */
    private boolean inBlockComment = false;
    /**
     * 临时变量，用于存储查找注释符号时的索引位置
     */
    private int i = 0, lcs = -1, bcs = -1, bce;

    /**
     * 构造函数，初始化迭代器所需的资源和注释格式。
     *
     * @param reader            用于读取文件内容的BufferedReader。
     * @param lineCommentPrefix 单行注释的前缀字符串。
     * @param blockCommentStart 块注释开始的字符串。
     * @param blockCommentEnd   块注释结束的字符串。
     */
    private CommentFilterLineIterator(BufferedReader reader, String lineCommentPrefix, String blockCommentStart, String blockCommentEnd) {
        this.reader = Objects.requireNonNull(reader);
        // 如果没有指定任何注释格式，则所有行都是有效数据
        if (lineCommentPrefix == null && blockCommentStart == null && blockCommentEnd == null) {
            this.lineLoader = () -> true;
        }
        // 只处理单行注释的情况
        else if (lineCommentPrefix != null && blockCommentStart == null && blockCommentEnd == null) {
            this.lineLoader = () -> {
                // 查找单行注释的位置
                lcs = data.indexOf(lineCommentPrefix);
                // 如果单行注释在行首，则整行都是注释
                if (lcs == 0) return false;
                // 如果找到了单行注释，则截取注释之前的内容
                if (lcs > 0) {
                    data = data.substring(0, lcs);
                }
                return true;
            };
        }
        // 只处理块注释的情况
        else if (lineCommentPrefix == null && blockCommentStart != null && blockCommentEnd != null) {
            this.lineLoader = () -> {
                // 处理处于块注释内的行
                if (inBlockComment) {
                    // 查找块注释结束符的位置
                    bce = data.indexOf(blockCommentEnd);
                    // 如果未找到结束符，跳过整行
                    if (bce < 0) return false;
                    // 找到结束符，截取结束符之后的内容
                    data = data.substring(bce + blockCommentEnd.length());
                    // 退出块注释状态
                    inBlockComment = false;
                }
                // 查找块注释开始符的位置
                bcs = data.indexOf(blockCommentStart);
                // 处理块注释开始的情况
                if (bcs >= 0) {
                    // 进入块注释状态
                    inBlockComment = true;
                    // 在同一行内同时找到开始和结束符的情况
                    if ((bce = data.indexOf(blockCommentEnd)) > bcs) {
                        // 截取开始符之前和结束符之后的内容
                        data = data.substring(0, bcs) + data.substring(bce + blockCommentEnd.length());
                        // 退出块注释状态
                        inBlockComment = false;
                    } else {
                        // 截取注释开始符之前的内容
                        data = data.substring(0, bcs);
                    }
                }
                return true;
            };
        }
        // 同时处理单行注释和块注释的情况
        else if (lineCommentPrefix != null && blockCommentStart != null && blockCommentEnd != null) {
            this.lineLoader = () -> {
                // 处理处于块注释内的行
                if (inBlockComment) {
                    // 查找块注释结束符的位置
                    i = data.indexOf(blockCommentEnd);
                    // 如果未找到结束符，跳过整行
                    if (i < 0) return false;
                    // 找到结束符，截取结束符之后的内容
                    data = data.substring(i + blockCommentEnd.length());
                    // 退出块注释状态
                    inBlockComment = false;
                }
                // 查找单行注释的位置
                lcs = data.indexOf(lineCommentPrefix);
                // 查找块注释开始符的位置
                bcs = data.indexOf(blockCommentStart);
                // 如果同时存在单行注释和块注释符号，优先处理最近的有效符号
                if (lcs >= 0 && bcs >= 0) {
                    if (lcs < bcs) {
                        // 单行注释在块注释之前，忽略块注释
                        bcs = -1;
                    } else {
                        // 块注释在单行注释之前，忽略单行注释
                        lcs = -1;
                    }
                }
                // 处理单行注释
                if (lcs >= 0) {
                    if (lcs == 0) return false;
                    // 截取注释之前的内容
                    data = data.substring(0, lcs);
                }
                // 处理块注释开始的情况
                else if (bcs >= 0) {
                    // 进入块注释状态
                    inBlockComment = true;
                    // 在同一行内同时找到开始和结束符的情况
                    if ((bce = data.indexOf(blockCommentEnd)) > bcs) {
                        // 截取开始符之前和结束符之后的内容
                        data = data.substring(0, bcs) + data.substring(bce + blockCommentEnd.length());
                        // 退出块注释状态
                        inBlockComment = false;
                    } else {
                        if (bcs == 0) return false;
                        // 截取注释开始符之前的内容
                        data = data.substring(0, bcs);
                    }
                }
                return true;
            };
        }
        // 参数不合法的情况
        else {
            throw new IllegalArgumentException("The blockCommentStart and blockCommentEnd must be both null or both not null");
        }
    }

    /**
     * 创建一个DataLineIterator对象，用于迭代读取数据行
     * 此方法通过指定注释符号，初始化一个DataLineIterator实例，以便在解析数据行时排除注释
     *
     * @param reader BufferedReader对象，用于读取数据源
     * @return 返回一个DataLineIterator对象，用于遍历处理数据源中的每一行数据
     */
    public static CommentFilterLineIterator ofComment(BufferedReader reader) {
        // 初始化DataLineIterator对象，使用指定的注释符号，以便在解析时排除注释
        return new CommentFilterLineIterator(reader, "#", "/*", "*/");
    }

    /**
     * 创建一个处理原始文本数据的DataLineIterator实例
     * 该方法用于初始化一个DataLineIterator对象，对象将使用给定的BufferedReader来读取数据
     * 由于该方法是静态的，因此可以通过类名直接调用
     *
     * @param reader BufferedReader实例，用于读取原始文本数据
     * @return 返回一个DataLineIterator实例，用于迭代读取数据
     * <p>
     * 注意：该方法的实现较为直接，没有进行参数校验，如reader是否为null，调用者需要确保传入有效的参数
     */
    public static CommentFilterLineIterator ofLineComment(BufferedReader reader) {
        return new CommentFilterLineIterator(reader, "#", null, null);
    }

    /**
     * 创建一个处理原始文本数据的DataLineIterator实例
     * 该方法用于初始化一个DataLineIterator对象，对象将使用给定的BufferedReader来读取数据
     * 由于该方法是静态的，因此可以通过类名直接调用
     *
     * @param reader BufferedReader实例，用于读取原始文本数据
     * @return 返回一个DataLineIterator实例，用于迭代读取数据
     * <p>
     * 注意：该方法的实现较为直接，没有进行参数校验，如reader是否为null，调用者需要确保传入有效的参数
     */
    public static CommentFilterLineIterator ofDirect(BufferedReader reader) {
        return CommentFilterLineIterator.of(reader, null, null, null);
    }

    /**
     * 创建一个处理原始文本数据的DataLineIterator实例
     * 该方法用于初始化一个DataLineIterator对象，对象将使用给定的BufferedReader来读取数据
     * 由于该方法是静态的，因此可以通过类名直接调用
     *
     * @param reader            BufferedReader实例，用于读取原始文本数据
     * @param lineCommentPrefix 行注释前缀，用于识别和跳过行注释
     * @param blockCommentStart 块注释开始标记，用于识别块注释的开始
     * @param blockCommentEnd   块注释结束标记，用于识别块注释的结束
     * @return 返回一个DataLineIterator实例，用于迭代读取数据
     * <p>
     * 注意：该方法的实现较为直接，没有进行参数校验，如reader是否为null，调用者需要确保传入有效的参数
     */
    public static CommentFilterLineIterator of(BufferedReader reader, String lineCommentPrefix, String blockCommentStart, String blockCommentEnd) {
        return new CommentFilterLineIterator(reader, lineCommentPrefix, blockCommentStart, blockCommentEnd);
    }

    /**
     * 检查是否还有下一行有效数据。
     *
     * @return 如果有有效数据返回true，否则返回false。
     */
    @Override
    public boolean hasNext() {
        // 如果已经没有更多行，则直接返回false
        if (!hasNextLine) return false;
        // 加载下一行数据
        loadNextLine();
        // 返回是否有下一行的标志
        return hasNextLine;
    }

    /**
     * 获取下一行有效数据。
     *
     * @return 返回一行有效字符串。
     * @throws NoSuchElementException 如果没有更多有效数据则抛出异常。
     */
    @Override
    public String next() {
        // 加载下一行数据
        loadNextLine();
        // 如果没有更多行，则抛出异常
        if (!hasNextLine) {
            throw new NoSuchElementException("Error reading next line");
        }
        // 保存当前行数据并清空缓存
        String result = data;
        data = null;
        // 返回当前行数据
        return result;
    }

    /**
     * 关闭底层资源（如文件流）。
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }

    /**
     * 加载下一行有效数据，跳过所有类型的注释。
     * 该方法会循环读取文件行，直到找到一个不被注释的内容或者到达文件末尾。
     * 它处理单行注释和块注释，并且能够正确识别注释符号的位置来截取有效数据。
     */
    private void loadNextLine() {
        // 只有当当前数据为空时才尝试加载下一行
        if (data == null) {
            // 默认假设没有更多行
            hasNextLine = false;
            try {
                // 循环读取文件行，直到找到有效数据或到达文件末尾
                for (data = reader.readLine(); data != null; data = reader.readLine()) {
                    // 跳过空行
                    if (data.isEmpty()) continue;
                    // 使用lineLoader判断当前行是否为有效数据
                    hasNextLine = lineLoader.getAsBoolean();
                    // 如果是有效数据，则跳出循环
                    if (hasNextLine) break;
                }
            } catch (IOException e) {
                // 如果读取过程中发生IO异常，则包装为UncheckedIOException抛出
                throw new UncheckedIOException("Error reading data line", e);
            }
        }
    }
}
