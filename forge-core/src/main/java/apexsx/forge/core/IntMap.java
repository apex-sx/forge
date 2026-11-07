package apexsx.forge.core;

import java.util.Arrays;

/**
 * 内部构建器，用于增量添加key，分配rank，最终生成只读{@link IntRankTable}。
 * <p>rank从0开始，每新增一个不重复key rank自增+1。</p>
 */
public class IntMap {
    private int[] keys;
    private int[] values;
    private int size;
    private int mask;
    private int threshold;

    /**
     * 指定初始容量构造Builder，内部自动向上对齐到2的幂。
     *
     * @param initialCapacity 预估初始存储数量
     */
    IntMap(int initialCapacity) {
        int cap = AnTool.roundUpPower2(Math.max(initialCapacity, 1));
        keys = new int[cap];
        values = new int[cap];
        Arrays.fill(keys, AnTool.EMPTY_INT);
        size = 0;
        mask = cap - 1;
        threshold = (int) (cap * AnTool.DEFAULT_LOAD_FACTOR);
    }

    /**
     * 添加key；key已存在则直接返回builder，不会重复分配rank。
     * <p>新key分配下一个连续自增rank编号，rank从0开始。</p>
     *
     * @param key 需要加入映射的key，禁止为{@link Integer#MIN_VALUE}
     * @return this builder，支持链式调用
     * @throws IllegalArgumentException value等于EMPTY_VALUE时抛出
     */
    public IntMap add(int key) {
        if (key == AnTool.EMPTY_INT) {
            throw new IllegalArgumentException("key cannot be Integer.MIN_VALUE");
        }
        // 检查key是否已经存在
        int idx = AnTool.probeIndex(keys, key);
        if (idx != AnTool.EMPTY_INT) {
            return this;
        }
        int newSize = size + 1;
        // 判断是否需要扩容
        if (newSize > threshold) {
            int newCap = keys.length << 1;
            AnTool.rehash(newCap, this.keys, this.values, (nv, nr) -> {
                this.keys = nv;
                this.values = nr;
                this.mask = nv.length - 1;
                this.threshold = (int) (nv.length * AnTool.DEFAULT_LOAD_FACTOR);
                return this;
            });
        }
        // 扩容与否，统一寻找空槽
        idx = AnTool.hash(key) & mask;
        while (keys[idx] != AnTool.EMPTY_INT) {
            idx = (idx + 1) & mask;
        }
        keys[idx] = key;
        values[idx] = size;
        size = newSize;
        return this;
    }

    /**
     * 构建只读IntKeyRankTable实例；会做一次rehash，压缩哈希表到合适容量。
     * <p>会根据当前元素数量+负载因子，重新计算最小容量，减少内存占用。</p>
     *
     * @return 只读IntKeyRankTable
     */
    public IntRankTable build() {
        int minCapacity;
        int count = this.size;
        if (count <= 1) {
            minCapacity = 2;
        } else {
            // 根据元素数量/负载因子向上取2次幂，作为最终容量
            minCapacity = AnTool.roundUpPower2((int) Math.ceil(count / AnTool.DEFAULT_LOAD_FACTOR));
        }
        return AnTool.rehash(minCapacity, this.keys, this.values, (nv, nr) -> new IntRankTable(nv, nr, count));
    }
}
