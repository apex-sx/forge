package apexsx.forge.standard.iterator;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * NumberingStringIterator 是一个迭代器类，用于生成带有序号的字符串。
 * 它支持自定义格式、起始值、步长和数字位数，能够生成如 "item_001", "item_002" 等格式的字符串序列。
 * 支持多线程环境下的安全使用，通过 AtomicLong 实现线程安全的计数。
 *
 * @author zhitron
 */
public class NumberingStringIterator implements Iterator<String> {
    /**
     * 存储格式化字符串，其中 "{numbering}" 部分会替换成递增的数值。
     */
    private final String format;
    /**
     * 表示编号的最大限制值，当超过该值时会直接返回原始索引。
     * 它是根据数字位数计算出来的上限（例如：10^n）。
     */
    private final long limit;
    /**
     * 原子化的计数器，用于保存当前的编号值，确保多线程环境下安全使用。
     */
    private final AtomicLong count;
    /**
     * 每次递增的步长，默认为 1。
     */
    private final int interval;

    /**
     * 构造函数，调用四个参数的构造方法，并设置 numberingOfDigits = 10 和 interval = 1 和 start = 1。
     *
     * @param format 格式字符串，包含 "{numbering}" 占位符
     */
    public NumberingStringIterator(String format) {
        this(format, 1);
    }

    /**
     * 构造函数，调用四个参数的构造方法，并设置 numberingOfDigits = 10 和 interval = 1。
     *
     * @param format 格式字符串，包含 "{numbering}" 占位符
     * @param start  起始编号
     */
    public NumberingStringIterator(String format, int start) {
        this(format, start, 1);
    }

    /**
     * 构造函数，调用四个参数的构造方法，并设置 numberingOfDigits = 10。
     *
     * @param format   格式字符串，包含 "{numbering}" 占位符
     * @param start    起始编号
     * @param interval 每次递增的步长
     */
    public NumberingStringIterator(String format, int start, int interval) {
        this(format, start, interval, 1);
    }

    /**
     * 全参构造函数，初始化所有字段。
     *
     * @param format            格式字符串，包含 "{numbering}" 占位符
     * @param start             起始编号
     * @param interval          每次递增的步长
     * @param numberingOfDigits 数字位数，决定编号的上限，控制前导零的数量
     */
    public NumberingStringIterator(String format, int start, int interval, int numberingOfDigits) {
        this.format = format;
        // 计算编号上限，如果输入的位数小于 0，则默认为 1；最多不超过 18 位。
        this.limit = BigInteger.valueOf(10).pow(numberingOfDigits < 0 ? 1 : Math.min(numberingOfDigits, 18)).longValueExact();
        this.count = new AtomicLong(start);
        // 确保步长最小为 1
        this.interval = Math.max(interval, 1);
    }

    /**
     * 判断是否还有下一个元素。
     * 因为这个迭代器没有终止条件，所以总是返回 true。
     *
     * @return 始终返回 true
     */
    public boolean hasNext() {
        return true;
    }

    /**
     * 返回下一个元素，即按照格式生成的字符串。
     *
     * @return 格式化后的字符串，"{numbering}" 替换为当前编号
     */
    public String next() {
        long index;
        if (interval != 1) {
            index = count.getAndAdd(interval);
        } else {
            index = count.getAndIncrement();
        }
        String value;
        if (index >= 0 && limit > index) {
            // 正常情况下，在前面加上 limit 并去掉第一位，实现前导零的效果
            value = String.valueOf(index + limit).substring(1);
        } else if (index < 0 && limit > -index) {
            // 如果是负数且未超出范围，处理负号并去前导零
            value = "-" + String.valueOf(-index + limit).substring(1);
        } else {
            // 超出范围的情况下直接返回原始索引
            value = String.valueOf(index);
        }
        return format.replace("{numbering}", value);
    }
}