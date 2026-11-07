package com.github.zhitron.primitive_extension;


import com.github.zhitron.byte_loader.ByteLoaderFactory;
import com.github.zhitron.byte_loader.ByteSequenceIterable;
import com.github.zhitron.byte_loader.ByteSequenceIterator;
import com.github.zhitron.effortless.func.*;
import com.github.zhitron.lambda.consumer.TripleConsumerInt;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Byte元组
 *
 * @author zhitron
 */
public class ByteTuple implements Serializable, Iterable<Byte>{
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 存储布尔值的数组
     */
    protected byte[] elements;

    public ByteTuple(byte[] elements) {
        this.elements = Objects.requireNonNull(elements);
    }

    /**
     * 获取当前列表中的元素数量。
     *
     * @return 当前元素数量
     */
    public int size() {
        return elements.length;
    }

    /**
     * 检查列表是否为空。
     *
     * @return 如果列表为空则返回 true，否则返回 false
     */
    public final boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 检查列表是否包含指定元素。
     *
     * @param value 要查找的元素
     * @return 如果包含则返回 true，否则返回 false
     */
    public final boolean contains(byte value) {
        int length = size();
        for (int i = 0; i < length; i++) {
            if (elements[i] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找指定元素的第一个匹配项的索引。
     *
     * @param value 要查找的元素
     * @return 元素的索引，如果未找到则返回 -1
     */
    public final int indexOf(byte value) {
        int length = size();
        for (int i = 0; i < length; i++) {
            if (elements[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找指定数组在当前列表中的第一次出现的位置。
     * 该函数使用KMP算法进行匹配，以提高查找效率。
     *
     * @param values 要查找的数组，不能为 null 或空数组
     * @return 如果找到指定数组，则返回其在当前列表中的起始索引；否则返回 -1
     */
    public final int indexOf(byte[] values) {
        // 检查输入数组是否为空或 null，如果是则直接返回 -1
        if (values == null || values.length == 0) {
            return -1;
        }
        // 检查输入数组长度是否大于当前列表长度，如果是则直接返回 -1
        int length = size();
        if (values.length > length) {
            return -1;
        }

        // 使用KMP算法生成next数组，用于优化匹配过程
        int[] next = Helper.kmpNextArray(values.length, (i, j) -> values[i] == values[j]);

        // 初始化指针，i 用于遍历当前列表，j 用于遍历输入数组
        int i = 0, j = 0;
        while (i < length && j < values.length) {
            // 如果字符匹配，则继续比较下一个字符
            if (j == -1 || elements[i] == values[j]) {
                i++;
                j++;
            } else {
                // 如果字符不匹配，则根据next数组调整 j 的位置
                j = next[j];
            }
        }

        // 如果 j 等于输入数组的长度，说明找到了匹配的数组，返回起始索引
        if (j == values.length) {
            return i - j;
        } else {
            // 否则返回 -1，表示未找到匹配的数组
            return -1;
        }
    }

    /**
     * 查找指定元素的最后一个匹配项的索引。
     *
     * @param value 要查找的元素
     * @return 元素的索引，如果未找到则返回 -1
     */
    public final int lastIndexOf(byte value) {
        int length = size();
        for (int i = length - 1; i >= 0; i--) {
            if (elements[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在列表中查找指定数组最后一次出现的位置。
     * 使用KMP算法进行匹配，以提高查找效率。
     *
     * @param values 要查找的数组，不能为null且长度不能大于当前列表的长度
     * @return 如果找到匹配的数组，返回其在列表中的起始索引；否则返回-1
     */
    public final int lastIndexOf(byte[] values) {
        // 处理特殊情况：数组为空或长度大于列表长度
        if (values == null || values.length == 0) {
            return -1;
        }
        int length = size();
        if (values.length > length) {
            return -1;
        }

        // 使用KMP算法生成next数组，用于优化匹配过程
        int[] next = Helper.kmpNextArray(values.length, (i, j) -> values[i] == values[j]);

        // 从列表末尾开始匹配，i为列表中的起始位置，j为模式串的匹配位置
        int i = length - values.length;
        int j = 0;

        // 使用KMP算法进行匹配
        while (i >= 0 && j < values.length) {
            if (j == -1 || elements[i + j] == values[j]) {
                j++;
            } else {
                // 根据next数组移动i，并更新j的值
                i -= (j - next[j]);
                j = next[j];
            }
        }

        // 判断是否找到匹配的数组
        if (j == values.length) {
            return i;
        } else {
            return -1;
        }
    }

    /**
     * 获取指定位置的元素。
     *
     * @param index 元素位置
     * @return 指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public final byte get(int index) {
        return elements[Helper.idx(size(), index)];
    }

    /**
     * 将当前对象中的布尔数组值复制到目标数组中，并返回当前对象。
     *
     * @param target 目标布尔数组，用于接收当前对象中的布尔值。目标数组的长度必须与当前对象中的布尔数组长度一致，否则将抛出异常。
     * @param start  从目标数组的哪个位置开始复制
     * @throws IllegalArgumentException 如果目标数组的长度与当前对象中的布尔数组长度不一致，则抛出此异常。
     */
    public final void get(byte[] target, int start) {
        if (target != null) {
            if (size() < start + target.length) {
                throw new IllegalArgumentException("The start index plus the target length exceeds the size of the tuple. Expected: size() >= " + (start + target.length) + ", actual: " + size());
            }
            System.arraycopy(elements, start, target, 0, target.length);
        }
    }

    /**
     * 获取字符串内容，默认使用UTF-8字符集进行解码。
     * <p>
     * 该函数是getString(Charset charset)的重载版本，默认使用StandardCharsets.UTF_8作为字符集。
     *
     * @return 返回解码后的字符串内容。如果原始数据为空或无法解码，则返回空字符串或null（具体取决于实现）。
     */
    public final String getString() {
        return getString(StandardCharsets.UTF_8);
    }

    /**
     * 将字节数组转换为指定字符集的字符串。
     *
     * @param charset 用于解码字节数组的字符集。不能为null。
     * @return 返回根据指定字符集解码后的字符串。如果字节数组为空或字符集无效，返回的字符串可能为空。
     */
    public final String getString(Charset charset) {
        // 使用指定的字符集将字节数组解码为字符串
        return new String(elements, 0, size(), charset);
    }

    /**
     * 将当前对象中的数组复制并返回一个新的数组。
     *
     * @return 一个新的布尔值数组，包含与 {@code elements} 数组相同的元素。
     */
    public final byte[] toArray() {
        return Arrays.copyOf(elements, size());
    }

    /**
     * 创建一个当前 {@code ByteTuple} 对象的副本。
     * <p>
     * 该方法通过调用 {@code toArray()} 方法获取当前元组的所有元素，并使用这些元素创建一个新的 {@code ByteTuple} 对象。
     *
     * @return 返回一个新的 {@code ByteTuple} 对象，该对象包含与当前元组相同的元素。
     */
    public final ByteTuple toTupleObject() {
        return new ByteTuple(toArray());
    }

    /**
     * 返回一个迭代器，用于遍历布尔数组中的元素。
     * 该迭代器是基于ByteArrayIterator实现的，它允许逐个访问布尔数组中的值。
     *
     * @return 返回一个Iterator<Byte>对象，用于遍历布尔数组中的元素。
     */
    @Override
    public Iterator<Byte> iterator() {
        return new ByteArrayIterator(elements);
    }

    /**
     * 计算当前元组中所有元素经过指定函数处理后的累加和。
     * <p>
     * 该方法会遍历元组中的每个元素，并将每个元素传递给 `function` 进行处理，然后将处理结果累加到总和中。
     *
     * @param function 一个接受元素并返回处理结果的函数，处理结果将被累加到总和中。
     * @return 返回所有元素经过处理后的累加和。
     */
    public final long sum(ByteDataSiOperator function) {
        int length = size();
        long sum = 0;

        // 遍历所有元素，将每个元素经过函数处理后的结果累加到sum中
        for (int i = 0; i < length; i++) {
            sum += function.apply(elements[i]);
        }

        return sum;
    }

    /**
     * 计算当前元组与另一个元组中对应元素经过指定二元函数处理后的累加和。
     * <p>
     * 该方法会遍历两个元组中的每个元素，并将对应位置的元素传递给 `function` 进行处理，然后将处理结果累加到总和中。
     * 如果两个元组的长度不一致，将抛出 `IllegalArgumentException` 异常。
     *
     * @param value    另一个元组，其元素将与当前元组的元素进行配对处理。
     * @param function 一个接受两个元素并返回处理结果的二元函数，处理结果将被累加到总和中。
     * @return 返回两个元组中对应元素经过处理后的累加和。
     * @throws IllegalArgumentException 如果两个元组的长度不一致，则抛出此异常。
     */
    public final long sum(ByteTuple value, ByteDataBiOperator function) {
        int length = size();
        // 检查两个ByteTuple对象的长度是否一致
        if (length != value.size()) {
            throw new IllegalArgumentException("The length is inconsistent");
        }

        long sum = 0;
        // 遍历每个元素，应用指定的二元操作并累加结果
        for (int i = 0; i < length; i++) {
            sum += function.apply(elements[i], value.elements[i]);
        }
        return sum;
    }

    /**
     * 计算当前元组与另外两个元组中对应元素经过指定三元函数处理后的累加和。
     * <p>
     * 该方法会遍历三个元组中的每个元素，并将对应位置的元素传递给 `function` 进行处理，然后将处理结果累加到总和中。
     * 如果三个元组的长度不一致，将抛出 `IllegalArgumentException` 异常。
     *
     * @param value    第一个元组，其元素将与当前元组的元素进行配对处理。
     * @param other    第二个元组，其元素将与当前元组的元素进行配对处理。
     * @param function 一个接受三个元素并返回处理结果的三元函数，处理结果将被累加到总和中。
     * @return 返回三个元组中对应元素经过处理后的累加和。
     * @throws IllegalArgumentException 如果三个元组的长度不一致，则抛出此异常。
     */
    public final long sum(ByteTuple value, ByteTuple other, ByteDataTrOperator function) {
        int length = size();
        // 检查长度是否一致，如果不一致则抛出异常
        if (length != value.size() || length != other.size()) {
            throw new IllegalArgumentException("The length is inconsistent");
        }
        long sum = 0;
        // 遍历每个元素，使用function对三个元素进行计算并累加到sum中
        for (int i = 0; i < length; i++) {
            sum += function.apply(elements[i], value.elements[i], other.elements[i]);
        }
        return sum;
    }

    /**
     * 将原始字节数据转换为十六进制编码的字节元组。
     * <p>
     * 该函数通过调用 `ACoder.hexEncode` 方法，将 `byteSequenceIterator()` 返回的字节数据
     * 转换为十六进制编码的字节数组，并根据 `upper` 参数决定是否使用大写字母表示十六进制字符。
     * 如果转换过程中发生 I/O 异常，则将其包装为 `RuntimeException` 抛出。
     *
     * @param upper 一个布尔值，指示是否使用大写字母表示十六进制字符。如果为 `true`，则使用大写字母；否则使用小写字母。
     * @return 返回一个包含十六进制编码字节的 [ByteTuple](file://F:\zhitron-bpb\zhitron-buep\buep-functional\src\main\java\zhitron\buep\functional\ByteTuple.java#L11-L130) 对象。
     * @throws RuntimeException 如果在编码过程中发生 I/O 异常，则抛出此异常。
     */
    public final ByteTuple hexEncode(boolean upper) {
        try {
            // 调用 ACoder.hexEncode 方法进行十六进制编码，并返回结果
            return new ByteTuple(UsedCodec.hexEncode(byteSequenceIterator(), upper));
        } catch (IOException e) {
            // 捕获 I/O 异常并包装为 RuntimeException 抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 将当前对象的原始字节数据解码为十六进制格式，并返回一个包含解码结果的ByteTuple对象。
     * <p>
     * 该函数首先通过调用byteSequenceIterator()方法获取原始字节数据的迭代器，然后使用ACoder.hexDecode方法
     * 将字节数据解码为十六进制格式。如果解码过程中发生IOException，则将其包装为RuntimeException并抛出。
     *
     * @return 返回一个包含解码后十六进制数据的ByteTuple对象。
     * @throws RuntimeException 如果解码过程中发生IOException，则抛出此异常。
     */
    public final ByteTuple hexDecode() {
        try {
            // 调用ACoder.hexDecode方法对原始字节数据进行十六进制解码，并返回解码结果
            return new ByteTuple(UsedCodec.hexDecode(byteSequenceIterator()));
        } catch (IOException e) {
            // 如果解码过程中发生IOException，则将其包装为RuntimeException并抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 对原始字节数据进行Base64编码，并返回编码后的字节元组。
     *
     * @param urlType 如果为true，则使用URL安全的Base64编码方案；否则使用标准Base64编码方案。
     * @return 包含Base64编码结果的ByteTuple对象。
     * @throws RuntimeException 如果在编码过程中发生IO异常，则抛出RuntimeException。
     */
    public final ByteTuple base64Encode(boolean urlType) {
        try {
            // 调用ACoder.base64Encode方法对原始字节数据进行Base64编码，并返回编码后的字节元组
            return new ByteTuple(UsedCodec.base64Encode(byteSequenceIterator(), urlType));
        } catch (IOException e) {
            // 捕获IO异常并包装为RuntimeException抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 对当前对象的原始字节数据进行Base64解码，并返回解码后的字节元组。
     * <p>
     * 该函数首先通过调用`byteSequenceIterator()`方法获取原始字节数据的迭代器，
     * 然后使用`ACoder.base64Decode`方法对字节数据进行Base64解码。
     * 解码后的字节数据将被封装在一个`ByteTuple`对象中返回。
     * <p>
     * 如果在解码过程中发生IO异常，该异常将被捕获并包装为`RuntimeException`抛出。
     *
     * @return 返回一个包含解码后字节数据的ByteTuple对象。
     * @throws RuntimeException 如果解码过程中发生IO异常，则抛出此异常。
     */
    public final ByteTuple base64Decode() {
        try {
            // 对原始字节数据进行Base64解码，并返回解码后的字节元组
            return new ByteTuple(UsedCodec.base64Decode(byteSequenceIterator()));
        } catch (IOException e) {
            // 捕获IO异常并包装为RuntimeException抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取一个用于遍历字节数据的迭代器。
     *
     * @return 返回一个实现 {@link ByteSequenceIterator} 的实例
     */
    @Override
    public ByteSequenceIterator byteSequenceIterator() {
        return ByteLoaderFactory.of(elements);
    }

    /**
     * 遍历当前元组中的元素，并对每个元素执行指定的操作。
     * <p>
     * 该方法会遍历从索引 0 到 `size() - 1` 的元素，并将每个元素及其索引传递给 `consumer` 进行处理。
     * 该方法不会修改元组的内容，仅用于执行某些操作（如打印、计算等）。
     *
     * @param consumer 一个接受元素和索引的双参数消费者函数，用于对每个元素执行操作。
     *                 consumer 的第一个参数是元素值，第二个参数是元素的索引。
     * @throws NullPointerException 如果 `consumer` 为 `null`，则抛出此异常。
     */
    public final void each(TripleConsumerInt consumer) {
        int length = size();
        for (int i = 0; i < length; i++) {
            consumer.accept(elements[i], i, length);
        }
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ByteTuple other)) return false;
        int length = size();
        if (length != other.size()) return false;
        for (int i = 0; i < length; i++) {
            if (!Objects.equals(elements[i], other.elements[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        int length = size();
        int result = 1;
        for (int i = 0; i < length; i++) {
            result = 31 * result + Objects.hashCode(elements[i]);
        }
        return result;
    }

    @Override
    public final String toString() {
        int length = size();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (length > 0) {
            sb.append(elements[0]);
        }
        for (int i = 1; i < length; i++) {
            sb.append(", ").append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
