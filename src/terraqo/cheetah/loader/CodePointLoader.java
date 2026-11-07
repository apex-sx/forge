/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.nio.CharBuffer;

/**
 * 该类定义了用于加载 Unicode Code Point 的基本操作。
 * <p>
 *
 * @author Terraqo
 */
public abstract class CodePointLoader extends ElementLookaheadLoaderBase<Integer> implements AutoCloseable {
    /**
     * 存储字符数据的缓冲区，用于读取 Unicode Code Point。
     * 该缓冲区为 final 类型，确保其在对象生命周期内不可变。
     */
    private final CharBuffer charBuffer;

    /**
     * 记录当前处理的行号，从1开始计数
     */
    private int currentLine = 1;

    /**
     * 记录当前处理的列号，从0开始计数
     */
    private int currentColumn = 1;

    /**
     * 构造函数，初始化具有指定大小的字符缓冲区。
     *
     * @param bufferSize 缓冲区的大小
     */
    public CodePointLoader(int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must be greater than 0");
        }
        this.charBuffer = CharBuffer.allocate(bufferSize);
        this.charBuffer.flip();
    }

    /**
     * 默认的关闭方法，用于释放资源或完成清理工作
     *
     * @throws Exception 如果关闭过程中发生错误，抛出此异常
     */
    @Override
    public void close() throws Exception {
    }

    /**
     * 获取当前行号
     *
     * @return 当前行号，从1开始
     */
    public final int currentLine() {
        return currentLine;
    }

    /**
     * 获取当前列号
     *
     * @return 当前列号，从1开始
     */
    public final int currentColumn() {
        return currentColumn;
    }

    /**
     * 检查是否还有下一个 Unicode Code Point 可读取。
     *
     * @return 如果有下一个 Code Point 返回 true，否则返回 false。
     */
    public final boolean hasNextCodePoint() {
        return hasNext();
    }

    /**
     * 获取下一个 Unicode Code Point，并移动读取位置。
     *
     * @return 下一个 Unicode Code Point。
     */
    public final int nextCodePoint() {
        return next();
    }

    /**
     * 预览当前的 Unicode Code Point，不会移动读取位置。
     *
     * @return 当前的 Unicode Code Point。
     */
    public final int peekCodePoint() {
        return this.peekCodePoint(0);
    }

    /**
     * 预览指定偏移位置的 Unicode Code Point，不会移动读取位置。
     *
     * @param offset 相对于当前位置的偏移量。
     * @return 指定偏移位置的 Unicode Code Point。
     */
    public final int peekCodePoint(int offset) {
        return this.retrieveElement(offset, false);
    }

    /**
     * 弹出代码点
     * 该方法用于从数据结构中弹出一个代码点（Unicode字符）默认情况下，不指定任何标志
     * 具体行为和返回值取决于底层数据结构和popCodePoint方法的实现
     *
     * @return int 弹出的代码点如果数据结构为空或无法弹出代码点，则返回值将反映这一点
     */
    public final int popCodePoint() {
        return this.popCodePoint(0);
    }

    /**
     * 从数据结构中弹出一个代码点（codePoint）并返回它
     * 此方法用于需要处理字符数据的高级文本处理场景
     * 它确保以正确的方式处理字符边界，避免数据损坏或不一致
     *
     * @param offset 相对于当前位置的偏移量。
     * @return 返回弹出的代码点的整数值如果栈中没有足够的代码点，则行为未定义
     */
    public final int popCodePoint(int offset) {
        return this.retrieveElement(offset, true);
    }

    /**
     * 将当前上下文转换为字符串内容
     * 此方法通过遍历当前上下文中的所有代码点，并将它们拼接成一个字符串来实现
     * 它利用了StringBuilder来高效地构建字符串
     *
     * @return 包含当前上下文所有代码点的字符串
     */
    public final String toContent() {
        StringBuilder sb = new StringBuilder();
        while (hasNextCodePoint()) {
            int codePoint = nextCodePoint();
            sb.appendCodePoint(codePoint);
        }
        return sb.toString();
    }

    /**
     * 获取结束指示器的值
     *
     * @return Integer 返回结束指示器的值，固定返回-1表示结束
     */
    @Override
    public Integer eof() {
        return -1;
    }

    /**
     * 检索指定偏移量处的元素，并根据需要决定是否消费该元素及之前的元素
     *
     * <p>此方法允许向前查看流中的元素而不消费它们（当 consume=false 时），
     * 或者检索并消费指定偏移量处的元素以及所有在其之前的元素（当 consume=true 时）。
     * 如果缓存中没有足够的元素满足请求的偏移量，会尝试从数据源中获取更多元素。
     *
     * @param offset  要检索的元素相对于当前位置的偏移量（非负整数）
     * @param consume 如果为 true，则消费从索引 0 到 offset 的所有元素（包括 offset 处的元素）；
     *                如果为 false，则仅返回指定偏移量处的元素而不消费任何元素
     * @return 在指定偏移量处的元素，如果已达到数据源末尾且该位置没有元素，则返回 endIndicator()
     * @throws IllegalArgumentException 如果偏移量为负数
     */
    @Override
    public Integer retrieveElement(int offset, boolean consume) {
        if (offset < 0 || offset >= charBuffer.capacity()) {
            throw new IllegalArgumentException("offset out of range at [0," + charBuffer.capacity() + ")");
        }
        if (!consume) {
            charBuffer.mark();
        }
        char h, l;
        int codePoint = -1, charCount = 0, count = 0;
        try {
            while (count <= offset) {
                // 判断是否有剩余字符可读
                if (!ensureBufferData(charCount, consume)) {
                    return endIndicator();
                }

                h = charBuffer.get();
                charCount++;
                // 判断是否是高代理字符
                if (Character.isHighSurrogate(h)) {
                    // 获取低代理字符
                    if (!ensureBufferData(charCount, consume)) {
                        throw new IllegalStateException("Incomplete surrogate pair: high surrogate without low surrogate.");
                    }
                    l = charBuffer.get();
                    charCount++;
                    if (count == offset) {
                        codePoint = Character.toCodePoint(h, l);
                    }
                } else {
                    if (count == offset) {
                        codePoint = h;
                    }
                }
                if (count == offset) {
                    if (codePoint == '\n') {
                        currentLine++;
                        currentColumn = 0;
                    } else {
                        currentColumn++;
                    }
                    return codePoint;
                }
                count += 1;
            }
            return endIndicator();
        } catch (IllegalStateException e) {
            throw e; // 重新抛出特定异常
        } catch (Exception e) {
            throw new IllegalStateException("Error to load char data", e);
        } finally {
            // 操作完成后恢复位置并根据 consume 决定是否消费字符
            if (!consume) {
                charBuffer.reset();
            }
        }
    }

    /**
     * 确保缓冲区中有可用数据
     *
     * @return 如果成功获取到数据返回true，否则返回false
     */
    private boolean ensureBufferData(int count, boolean consume) throws Exception {
        if (!charBuffer.hasRemaining()) {
            if (!consume) {
                charBuffer.reset();
            }
            // 缓冲区无足够空间，重新填充数据
            charBuffer.compact();
            loadCharBuffer(charBuffer);
            charBuffer.flip();
            if (!consume) {
                charBuffer.mark();
            }
            charBuffer.position(count);
            return charBuffer.hasRemaining();
        }
        return true;
    }

    /**
     * 抽象方法，由子类实现以提供具体的字符加载逻辑。
     *
     * @param charBuffer 要加载数据的目标 CharBuffer
     */
    protected abstract void loadCharBuffer(CharBuffer charBuffer) throws Exception;

    /**
     * 更新当前行号和列号
     *
     * @param codePoint 当前处理的代码点
     */
    private void updatePosition(int codePoint) {
    }
}