package com.github.zhitron.primitive_extension;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Arrays;

/**
 * ShortList 类是一个用于存储字符的动态数组实现。
 * 它提供了添加、删除、查找、修改等操作，并支持序列化和反序列化。
 *
 * @author zhitron
 */
public class ShortList extends ShortArray {
    @Serial
    private static final long serialVersionUID = 1L;
    private int size;

    /**
     * 默认构造函数，使用默认初始容量初始化 ShortList。
     */
    public ShortList() {
        this(16);
    }

    /**
     * 构造函数，使用指定的初始容量初始化 ShortList。
     *
     * @param initialCapacity 初始容量
     * @throws IllegalArgumentException 如果初始容量为负数
     */
    public ShortList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.elements = new short[initialCapacity];
        this.size = 0;
    }

    /**
     * 构造函数，使用指定的数组初始化 ShortList。
     *
     * @param values 初始化数组
     * @throws IllegalArgumentException 如果初始容量为负数
     */
    public ShortList(short[] values) {
        if (values == null || values.length == 0) {
            this.elements = new short[16];
            this.size = 0;
        } else {
            this.elements = Arrays.copyOf(values, values.length + 16);
            this.size = values.length;
        }
    }

    /**
     * 获取当前列表中的元素数量。
     *
     * @return 当前元素数量
     */
    public int size() {
        return size;
    }

    /**
     * 在列表末尾添加一个元素。
     *
     * @param value 要添加的元素
     */
    public void add(short value) {
        ensureCapacity(size + 1, true);
        elements[size++] = value;
    }

    /**
     * 在列表末尾添加一个数组。
     *
     * @param values 要添加的数组
     */
    public void add(short[] values) {
        add(values, 0, values.length);
    }

    /**
     * 在列表末尾添加一个数组的一部分。
     *
     * @param values 要添加的数组
     * @param start  数组的起始位置（包含）
     * @param count  要添加的元素数量
     * @throws IllegalArgumentException 如果 start 或 count 不合法
     */
    public void add(short[] values, int start, int count) {
        if (values == null || values.length == 0) {
            return; // 如果数组为空或 null，直接返回
        }
        if (start < 0 || count < 0 || start + count > values.length) {
            throw new IllegalArgumentException("Invalid start or count");
        }
        ensureCapacity(size + count, true);
        System.arraycopy(values, start, elements, size, count);
        size += count;
    }

    /**
     * 在指定位置插入一个元素。
     *
     * @param index 插入位置
     * @param value 要插入的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public void add(int index, short value) {
        Helper.idxVerify(size, index, true);
        ensureCapacity(size + 1, true);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        size++;
    }

    /**
     * 在指定位置插入一个数组。
     *
     * @param index  插入位置
     * @param values 要插入的数组
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public void add(int index, short[] values) {
        // 调用重载方法，插入整个数组
        add(index, values, 0, values.length);
    }

    /**
     * 在指定位置插入一个数组的指定部分。
     *
     * @param index  插入位置
     * @param values 要插入的数组
     * @param start  要插入的数组的起始位置（包含）
     * @param count  要插入的元素数量
     * @throws IndexOutOfBoundsException 如果索引超出范围
     * @throws IllegalArgumentException  如果 start 或 count 不合法
     */
    public void add(int index, short[] values, int start, int count) {
        if (values == null || values.length == 0) {
            return; // 如果数组为空或 null，直接返回
        }

        // 检查 start 和 count 是否合法
        if (start < 0 || count < 0 || start + count > values.length) {
            throw new IllegalArgumentException("Invalid start or count");
        }

        // 检查索引是否合法
        Helper.idxVerify(size, index, true);

        // 确保容量足够，并返回一个新的空数组
        short[] newElements = ensureCapacity(size + count, false);

        // 将原数组中从 0 到 index 的部分复制到新数组
        System.arraycopy(elements, 0, newElements, 0, index);

        // 将 values 数组中从 start 开始的 count 个元素插入到新数组的 index 位置
        System.arraycopy(values, start, newElements, index, count);

        // 将原数组中从 index 到末尾的部分复制到新数组的 index + count 位置
        System.arraycopy(elements, index, newElements, index + count, size - index);

        // 将新数组赋值给 elements
        elements = newElements;

        // 更新列表大小
        size += count;
    }

    /**
     * 删除指定位置的元素，并返回被删除的元素。
     *
     * @param index 元素位置
     * @return 被删除的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public short remove(int index) {
        Helper.idxVerify(size, index, false);
        short removedValue = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        return removedValue;
    }

    /**
     * 删除指定值的第一个匹配项。
     *
     * @param value 要删除的元素
     * @return 如果删除成功则返回 true，否则返回 false
     */
    public boolean delete(short value) {
        int index = indexOf(value);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * 替换指定范围内的元素为给定的值数组。
     *
     * @param start  替换范围的起始索引（包含）。
     * @param end    替换范围的结束索引（不包含）。
     * @param values 用于替换的新值数组。如果为null或空数组，则相当于删除指定范围内的元素。
     * @throws IllegalArgumentException 如果start或end不合法（start < 0, end > size, 或 start > end）。
     */
    public void replace(int start, int end, short[] values) {
        int[] idx = Helper.idx(size, start, end);

        // 如果values不为null且不为空数组，则进行替换操作
        if (values != null && values.length != 0) {
            // 计算新数组的大小
            int newSize = size - (idx[1] - idx[0]) + values.length;
            // 确保容量足够，并创建新数组
            short[] newElements = ensureCapacity(newSize, false);

            // 将原数组中idx[0]之前的部分复制到新数组
            System.arraycopy(elements, 0, newElements, 0, idx[0]);
            // 将values数组复制到新数组的idx[0]位置
            System.arraycopy(values, 0, newElements, idx[0], values.length);
            // 将原数组中idx[1]之后的部分复制到新数组的idx[0] + values.length位置
            System.arraycopy(elements, idx[1], newElements, idx[0] + values.length, size - idx[1]);

            // 更新elements和size
            elements = newElements;
            size = newSize;
        } else {
            // 如果values为null或空数组，则相当于删除idx[0]到idx[1]之间的元素
            System.arraycopy(elements, idx[1], elements, idx[0], size - idx[1]);
            size -= idx[1] - idx[0];
        }
    }

    /**
     * 返回从指定起始位置到结束位置的子列表。
     *
     * @param start 起始位置（包含）
     * @param end   结束位置（不包含）
     * @return 包含指定范围内元素的子列表
     * @throws IllegalArgumentException 如果起始位置或结束位置不合法
     */
    public short[] subList(int start, int end) {
        int[] idx = Helper.idx(size, start, end);
        if (idx[0] == idx[1]) return new short[0];
        short[] temp = new short[idx[1] - idx[0]];
        System.arraycopy(elements, idx[0], temp, 0, idx[1] - idx[0]);
        return temp;
    }

    /**
     * 返回从指定起始位置到列表末尾的子列表。
     *
     * @param start 起始位置（包含）
     * @return 包含从起始位置到列表末尾元素的子列表
     * @throws IllegalArgumentException 如果起始位置不合法
     */
    public short[] subList(int start) {
        return subList(start, size);
    }

    /**
     * 设置列表的长度。如果指定的长度小于当前长度，则截断列表；如果大于当前长度，则不做任何操作。
     *
     * @param length 新的列表长度
     * @throws IllegalArgumentException 如果指定的长度为负数
     */
    public void setLength(int length) {
        if (length >= 0 && length <= size) {
            size = length;
        }
    }

    /**
     * 清空列表中的所有元素。
     */
    public void clear() {
        size = 0;
    }

    /**
     * 确保列表容量足够。
     *
     * @param minCapacity 所需的最小容量
     */
    private short[] ensureCapacity(int minCapacity, boolean coverage) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            short[] array = new short[newCapacity];
            if (coverage) {
                System.arraycopy(elements, 0, array, 0, size);
                elements = array;
            }
            return array;
        } else {
            return coverage ? elements : new short[elements.length];
        }
    }

    /**
     * 自定义序列化逻辑。
     *
     * @param out 输出流
     * @throws IOException 如果发生 I/O 错误
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeShort(elements[i]);
        }
    }

    /**
     * 自定义反序列化逻辑。
     *
     * @param in 输入流
     * @throws IOException            如果发生 I/O 错误
     * @throws ClassNotFoundException 如果类未找到
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        size = in.readInt();
        elements = new short[size];
        for (int i = 0; i < size; i++) {
            elements[i] = in.readShort();
        }
    }
}
