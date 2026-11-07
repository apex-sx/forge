package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * StringTokenizerIterable 是一个适配器类，它将 StringTokenizer 转换为可迭代对象。
 *
 * @author zhitron
 * @see StringTokenizer
 * @see StringTokenizerIterator
 */
public final class StringTokenizerIterable implements Iterable<String> {
    /**
     * 内部持有的 StringTokenizer 实例
     */
    private final StringTokenizer input;

    /**
     * 构造函数
     *
     * @param input StringTokenizer 实例，如果为 null，则使用空字符串的 StringTokenizer
     */
    public StringTokenizerIterable(StringTokenizer input) {
        // 如果输入的 StringTokenizer 为 null，则使用 StringTokenizerIterator 中预定义的空字符串 StringTokenizer
        this.input = input != null ? input : StringTokenizerIterator.EMPTY_STRING_TOKENIZER;
    }

    /**
     * 返回一个迭代器，用于遍历 StringTokenizer 中的 token
     *
     * @return StringTokenizerIterator 实例
     */
    @Override
    public Iterator<String> iterator() {
        // 返回一个新的 StringTokenizerIterator 实例，传入内部持有的 StringTokenizer
        return new StringTokenizerIterator(input);
    }
}
