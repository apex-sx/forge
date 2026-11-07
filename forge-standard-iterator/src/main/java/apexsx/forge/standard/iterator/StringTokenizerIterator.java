package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * StringTokenizerIterator 是一个适配器类，它将 StringTokenizer 转换为 Iterator 对象。
 *
 * @author zhitron
 */
public final class StringTokenizerIterator implements Iterator<String> {
    /**
     * 空字符串的 StringTokenizer 实例，用于当传入的 StringTokenizer 为 null 时使用
     */
    static final StringTokenizer EMPTY_STRING_TOKENIZER = new StringTokenizer("");
    /**
     * 内部持有的 StringTokenizer 实例
     */
    private final StringTokenizer input;

    /**
     * 构造函数
     *
     * @param input StringTokenizer 实例，如果为 null，则使用空字符串的 StringTokenizer
     */
    public StringTokenizerIterator(StringTokenizer input) {
        // 如果输入的 StringTokenizer 为 null，则使用预定义的空字符串 StringTokenizer
        this.input = input != null ? input : EMPTY_STRING_TOKENIZER;
    }

    /**
     * 检查是否还有更多的 token 可用
     *
     * @return 如果还有更多 token 返回 true，否则返回 false
     */
    @Override
    public boolean hasNext() {
        // 委托给内部 StringTokenizer 的 hasMoreTokens 方法
        return input.hasMoreTokens();
    }

    /**
     * 获取下一个 token
     *
     * @return 下一个 token 字符串
     */
    @Override
    public String next() {
        // 委托给内部 StringTokenizer 的 nextToken 方法
        return input.nextToken();
    }
}
