package apexsx.forge.core;

import java.io.Serial;
import java.io.Serializable;

/**
 * 只读 int‑>int rank映射表，底层采用开放寻址线性探测哈希表实现。
 * <p>功能：为输入int类型key分配连续自增rank编号，一个key对应唯一rank。</p>
 * <p><b>重要约束：</b>业务key不允许传入 {@link Integer#MIN_VALUE}，该常量被用作哈希表空槽标记。</p>
 * <p>实例构建完成后只读，不支持增删改；所有写入操作通过内部{@link IntMap}完成。</p>
 */
public class IntRankTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 哈希表存储key数组。
     * 槽位值为{@link AnTool#EMPTY_INT}代表该位置为空。
     */
    private final int[] keys;
    /**
     * key对应的rank编号数组，与values数组下标一一对应
     */
    private final int[] values;
    /**
     * 当前存储的有效key数量（rank总数）
     */
    private final int size;

    /**
     * 私有构造，外部只能通过Builder构建实例
     *
     * @param keys key哈希数组
     * @param values  rank编号数组
     * @param size   有效元素个数
     */
    IntRankTable(int[] keys, int[] values, int size) {
        this.keys = keys;
        this.values = values;
        this.size = size;
    }

    public static boolean isEmptyValue(int value) {
        return value == AnTool.EMPTY_INT;
    }

    /**
     * 获取构建器，使用默认初始容量16
     *
     * @return Builder实例
     */
    public static IntMap of() {
        return new IntMap(16);
    }

    /**
     * 获取构建器，指定预估初始容量
     *
     * @param initialCapacity 预估存储元素数量
     * @return Builder实例
     * @throws IllegalArgumentException initialCapacity小于0抛出异常
     */
    public static IntMap of(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must >= 0");
        }
        return new IntMap(initialCapacity);
    }

    /**
     * 获取value对应的rank编号。
     *
     * @param value 查询key
     * @return 存在返回rank；不存在返回{@link AnTool#EMPTY_INT}(Integer.MIN_VALUE)
     */
    public int get(int value) {
        int idx = AnTool.probeIndex(keys, value);
        if (idx == AnTool.EMPTY_INT) {
            return AnTool.EMPTY_INT;
        }
        return values[idx];
    }

    /**
     * 判断key是否存在于映射表
     *
     * @param value 待检测key
     * @return true存在；false不存在
     */
    public boolean has(int value) {
        return AnTool.probeIndex(keys, value) != AnTool.EMPTY_INT;
    }

    /**
     * 获取有效元素总数量
     *
     * @return 元素数目
     */
    public int size() {
        return size;
    }

    /**
     * 返回所有key的拷贝数组，顺序不保证哈希顺序
     *
     * @return key拷贝数组
     */
    public int[] keys() {
        int[] res = new int[size];
        int ptr = 0;
        for (int v : keys) {
            if (v != AnTool.EMPTY_INT) {
                res[ptr++] = v;
            }
        }
        return res;
    }

    /**
     * 格式化输出，格式示例：[1->0, 10->1, 99->2]
     *
     * @return 字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (int i = 0; i < keys.length; i++) {
            int v = keys[i];
            if (v != AnTool.EMPTY_INT) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(v).append("->").append(values[i]);
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
