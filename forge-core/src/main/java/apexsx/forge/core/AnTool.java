package apexsx.forge.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BiFunction;

public class AnTool {

    public static void main(String[] args) {

        Path path = Paths.get("").toAbsolutePath();
        System.out.println(path);
    }
    /**
     * 空槽标记值，禁止作为业务key。
     * 使用{@link Integer#MIN_VALUE}标记数组槽位为空。
     */
    public static final int EMPTY_INT = Integer.MIN_VALUE;
    /**
     * 默认负载因子，达到该阈值触发哈希表扩容
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * int哈希扰动函数，降低hash冲突
     *
     * @param x 输入key
     * @return 扰动后的hash值（未做掩码取模）
     */
    public static int hash(int x) {
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        return x;
    }

    /**
     * 线性探测，在哈希表中查找key对应的数组下标。
     * <p>遍历规则：从hash起始位置向后循环查找，遇到空槽直接终止。</p>
     *
     * @param table 哈希key数组
     * @param key   待查找key
     * @return 找到返回数组下标；找不到返回{@link #EMPTY_INT}
     */
    public static int probeIndex(int[] table, int key) {
        int mask = table.length - 1;
        int idx = hash(key) & mask;
        for (; ; ) {
            int k = table[idx];
            // 遇到空槽，代表key不存在
            if (k == EMPTY_INT) {
                return EMPTY_INT;
            }
            // key匹配，返回下标
            if (k == key) {
                return idx;
            }
            // 线性探测，移动到下一个槽位
            idx = (idx + 1) & mask;
        }
    }

    /**
     * 根据容量执行rehash重哈希，把旧表数据迁移到新表。
     * <p>如果目标容量与旧数组长度相等，直接复用旧数组，不做拷贝。</p>
     *
     * @param capacity  新哈希表容量，必须是2的幂
     * @param oldValues 旧key数组
     * @param oldRanks  旧rank数组
     * @param set       回调函数，接收新values、新ranks，返回业务对象
     * @param <T>       返回对象泛型
     * @return 回调函数返回的结果对象
     */
    public static <T> T rehash(int capacity, int[] oldValues, int[] oldRanks, BiFunction<int[], int[], T> set) {
        final int[] newValues;
        final int[] newRanks;
        if (capacity == oldValues.length) {
            // 容量不变，直接复用原数组，无需拷贝
            newValues = oldValues;
            newRanks = oldRanks;
        } else {
            newValues = new int[capacity];
            newRanks = new int[capacity];
            Arrays.fill(newValues, EMPTY_INT);
            int mask = capacity - 1;
            // 遍历旧表，迁移所有有效元素
            for (int i = 0; i < oldValues.length; i++) {
                int value = oldValues[i];
                if (value != EMPTY_INT) {
                    int rank = oldRanks[i];
                    int idx = hash(value) & mask;
                    // 线性探测寻找空槽写入
                    while (true) {
                        if (newValues[idx] == EMPTY_INT) {
                            newValues[idx] = value;
                            newRanks[idx] = rank;
                            break;
                        }
                        idx = (idx + 1) & mask;
                    }
                }
            }
        }
        return set.apply(newValues, newRanks);
    }

    /**
     * 将输入数字向上取最近的2的幂。
     * <pre>
     * example:
     * roundUpPower2(5) → 8
     * roundUpPower2(8) → 8
     * roundUpPower2(0) → 1
     * </pre>
     *
     * @param v 输入数值
     * @return 大于等于v的最小2的幂；v<=0返回1
     */
    public static int roundUpPower2(int v) {
        if (v <= 0) {
            return 1;
        }
        v--;
        v |= v >>> 1;
        v |= v >>> 2;
        v |= v >>> 4;
        v |= v >>> 8;
        v |= v >>> 16;
        return v + 1;
    }
}
