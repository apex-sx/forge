package apexsx.forge.core;

import java.io.*;
import java.nio.CharBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 通用常量类，提供各种基本类型常量、空对象实例和集合创建工具方法
 *
 * @author zhitron
 */
public final class UniversalConstant {
    /**
     * 布尔常量
     */
    public static final boolean FALSE = false, TRUE = true;

    /**
     * 文件结束标识
     */
    public static final int EOF = -1;

    /**
     * 哈希计算初始值
     */
    public static final int INITIAL_HASH = 7;

    /**
     * 哈希计算乘数
     */
    public static final int MULTIPLIER = 31;

    /**
     * 位数常量
     */
    public static final int BYTE = 8, WORD = 16, DWORD = 32, QWORD = 64;

    /*
     * 小写字母ASCII码范围
     */
    public static final int LOWERCASE_LETTER_ASCII_LOW = 97;
    public static final int LOWERCASE_LETTER_ASCII_HIGH = 122;

    /*
     * 大写字母ASCII码范围
     */
    public static final int UPPERCASE_LETTER_ASCII_LOW = 65;
    public static final int UPPERCASE_LETTER_ASCII_HIGH = 90;

    /*
     * 数字ASCII码范围
     */
    public static final int DIGIT_ASCII_LOW = 48;
    public static final int DIGIT_ASCII_HIGH = 57;

    /*
     * 各种基本类型的空值常量
     */
    public static final char EMPTY_CHAR = 0;
    public static final byte EMPTY_BYTE = 0;
    public static final short EMPTY_SHORT = 0;
    public static final int EMPTY_INT = 0;
    public static final long EMPTY_LONG = 0;
    public static final float EMPTY_FLOAT = 0;
    public static final double EMPTY_DOUBLE = 0;

    /*
     * 各种基本类型数组的空数组常量
     */
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = {};
    public static final Boolean[] EMPTY_BOOLEAN_WRAPPER_ARRAY = {};
    public static final char[] EMPTY_CHAR_ARRAY = {};
    public static final Character[] EMPTY_CHAR_WRAPPER_ARRAY = {};
    public static final byte[] EMPTY_BYTE_ARRAY = {};
    public static final Byte[] EMPTY_BYTE_WRAPPER_ARRAY = {};
    public static final short[] EMPTY_SHORT_ARRAY = {};
    public static final Short[] EMPTY_SHORT_WRAPPER_ARRAY = {};
    public static final int[] EMPTY_INT_ARRAY = {};
    public static final Integer[] EMPTY_INT_WRAPPER_ARRAY = {};
    public static final long[] EMPTY_LONG_ARRAY = {};
    public static final Long[] EMPTY_LONG_WRAPPER_ARRAY = {};
    public static final float[] EMPTY_FLOAT_ARRAY = {};
    public static final Float[] EMPTY_FLOAT_WRAPPER_ARRAY = {};
    public static final double[] EMPTY_DOUBLE_ARRAY = {};
    public static final Double[] EMPTY_DOUBLE_WRAPPER_ARRAY = {};
    public static final Object[] EMPTY_OBJECT_ARRAY = {};
    public static final Class<?>[] EMPTY_CLASS_ARRAY = {};
    public static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * 空输入流实例
     */
    public static final InputStream EMPTY_INPUT_STREAM = new InputStream() {
        @Override
        public int read() {
            return EOF;
        }

        @Override
        public int read(byte[] b) {
            return EOF;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return EOF;
        }

        @Override
        public long skip(long n) {
            return 0L;
        }

        @Override
        public int available() {
            return 0;
        }

        @Override
        public void close() {
        }

        @Override
        public synchronized void mark(int readLimit) {
        }

        @Override
        public synchronized void reset() {
        }

        @Override
        public boolean markSupported() {
            return false;
        }
    };

    /**
     * 空输出流实例
     */
    public static final OutputStream EMPTY_OUTPUT_STREAM = new OutputStream() {
        @Override
        public void write(int b) {
        }

        @Override
        public void write(byte[] b) {
        }

        @Override
        public void write(byte[] b, int off, int len) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    };

    /**
     * 空读取器实例
     */
    public static final Reader EMPTY_READER = new Reader() {
        @Override
        public int read(CharBuffer target) {
            return EOF;
        }

        @Override
        public int read() {
            return EOF;
        }

        @Override
        public int read(char[] cbuf) {
            return EOF;
        }

        @Override
        public long skip(long n) {
            return 0L;
        }

        @Override
        public boolean ready() {
            return false;
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public void mark(int readAheadLimit) {
        }

        @Override
        public void reset() {
        }

        @Override
        public int read(char[] cbuf, int off, int len) {
            return EOF;
        }

        @Override
        public void close() {
        }
    };

    /**
     * 空写入器实例
     */
    public static final Writer EMPTY_WRITER = new Writer() {
        @Override
        public void write(int c) {
        }

        @Override
        public void write(char[] cbuf) {
        }

        @Override
        public void write(String str) {
        }

        @Override
        public void write(String str, int off, int len) {
        }

        @Override
        public Writer append(CharSequence csq) {
            return this;
        }

        @Override
        public Writer append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public Writer append(char c) {
            return this;
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    };

    /**
     * 空可追加对象实例
     */
    public static final Appendable EMPTY_APPENDABLE = new Appendable() {
        @Override
        public Appendable append(CharSequence csq) {
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public Appendable append(char c) {
            return this;
        }
    };

    /**
     * 空打印流实例
     */
    public static final PrintStream EMPTY_PRINT_STREAM = new PrintStream(EMPTY_OUTPUT_STREAM);

    /*
     * 基本类型信息常量
     */
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_DOUBLE = new BasicTypeInformation(double.class, Double.class, 0, 8, Double::parseDouble, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_FLOAT = new BasicTypeInformation(float.class, Float.class, 1, 4, Float::parseFloat, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_LONG = new BasicTypeInformation(long.class, Long.class, 2, 8, Long::parseLong, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_INTEGER = new BasicTypeInformation(int.class, Integer.class, 3, 4, Integer::parseInt, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_SHORT = new BasicTypeInformation(short.class, Short.class, 4, 2, Short::parseShort, byte.class, Byte.class, short.class, Short.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_BYTE = new BasicTypeInformation(byte.class, Byte.class, 5, 1, Byte::parseByte, byte.class, Byte.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_CHARACTER = new BasicTypeInformation(char.class, Character.class, -1, 2, s -> s.isEmpty() ? '\0' : s.charAt(0), char.class, Character.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_BOOLEAN = new BasicTypeInformation(boolean.class, Boolean.class, -1, 1, Boolean::parseBoolean, boolean.class, Boolean.class);

    /*
     * 基本类型信息映射表
     */
    public static final Map<Class<?>, BasicTypeInformation> BASIC_TYPE_INFORMATION = UniversalConstant.asHashMap(true, map -> {
        map.put(BASIC_TYPE_INFORMATION_DOUBLE.primitiveType, BASIC_TYPE_INFORMATION_DOUBLE);
        map.put(BASIC_TYPE_INFORMATION_DOUBLE.wrapperType, BASIC_TYPE_INFORMATION_DOUBLE);
        map.put(BASIC_TYPE_INFORMATION_FLOAT.primitiveType, BASIC_TYPE_INFORMATION_FLOAT);
        map.put(BASIC_TYPE_INFORMATION_FLOAT.wrapperType, BASIC_TYPE_INFORMATION_FLOAT);
        map.put(BASIC_TYPE_INFORMATION_LONG.primitiveType, BASIC_TYPE_INFORMATION_LONG);
        map.put(BASIC_TYPE_INFORMATION_LONG.wrapperType, BASIC_TYPE_INFORMATION_LONG);
        map.put(BASIC_TYPE_INFORMATION_INTEGER.primitiveType, BASIC_TYPE_INFORMATION_INTEGER);
        map.put(BASIC_TYPE_INFORMATION_INTEGER.wrapperType, BASIC_TYPE_INFORMATION_INTEGER);
        map.put(BASIC_TYPE_INFORMATION_SHORT.primitiveType, BASIC_TYPE_INFORMATION_SHORT);
        map.put(BASIC_TYPE_INFORMATION_SHORT.wrapperType, BASIC_TYPE_INFORMATION_SHORT);
        map.put(BASIC_TYPE_INFORMATION_BYTE.primitiveType, BASIC_TYPE_INFORMATION_BYTE);
        map.put(BASIC_TYPE_INFORMATION_BYTE.wrapperType, BASIC_TYPE_INFORMATION_BYTE);
        map.put(BASIC_TYPE_INFORMATION_CHARACTER.primitiveType, BASIC_TYPE_INFORMATION_CHARACTER);
        map.put(BASIC_TYPE_INFORMATION_CHARACTER.wrapperType, BASIC_TYPE_INFORMATION_CHARACTER);
        map.put(BASIC_TYPE_INFORMATION_BOOLEAN.primitiveType, BASIC_TYPE_INFORMATION_BOOLEAN);
        map.put(BASIC_TYPE_INFORMATION_BOOLEAN.wrapperType, BASIC_TYPE_INFORMATION_BOOLEAN);
    });

    /**
     * 创建HashSet实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return HashSet实例
     */
    @SafeVarargs
    public static <E> Set<E> asHashSet(boolean unmodifiable, E... es) {
        Set<E> result = new HashSet<>((int) (es.length / 0.75f) + 1);
        Collections.addAll(result, es);
        result.removeIf(Objects::isNull);
        return unmodifiable ? Collections.unmodifiableSet(result) : result;
    }

    /**
     * 创建LinkedHashSet实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return LinkedHashSet实例
     */
    @SafeVarargs
    public static <E> Set<E> asLinkedHashSet(boolean unmodifiable, E... es) {
        Set<E> result = new LinkedHashSet<>((int) (es.length / 0.75f) + 1);
        Collections.addAll(result, es);
        result.removeIf(Objects::isNull);
        return unmodifiable ? Collections.unmodifiableSet(result) : result;
    }

    /**
     * 创建TreeSet实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param comparator   比较器
     * @param es           元素数组
     * @param <E>          元素类型
     * @return TreeSet实例
     */
    @SafeVarargs
    public static <E> Set<E> asTreeSet(boolean unmodifiable, Comparator<? super E> comparator, E... es) {
        Set<E> result = new TreeSet<>(comparator);
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableSet(result) : result;
    }

    /**
     * 创建CopyOnWriteArraySet实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param comparator   比较器
     * @param es           元素数组
     * @param <E>          元素类型
     * @return CopyOnWriteArraySet实例
     */
    @SafeVarargs
    public static <E> Set<E> asCopyOnWriteArraySet(boolean unmodifiable, Comparator<? super E> comparator, E... es) {
        Set<E> result = new CopyOnWriteArraySet<>();
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableSet(result) : result;
    }

    /**
     * 创建ArrayList实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return ArrayList实例
     */
    @SafeVarargs
    public static <E> List<E> asArrayList(boolean unmodifiable, E... es) {
        List<E> result = new ArrayList<>(es.length + 16);
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableList(result) : result;
    }

    /**
     * 创建LinkedList实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return LinkedList实例
     */
    @SafeVarargs
    public static <E> List<E> asLinkedList(boolean unmodifiable, E... es) {
        List<E> result = new LinkedList<>();
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableList(result) : result;
    }

    /**
     * 创建Vector实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return Vector实例
     */
    @SafeVarargs
    public static <E> List<E> asVector(boolean unmodifiable, E... es) {
        List<E> result = new Vector<>(es.length + 16);
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableList(result) : result;
    }

    /**
     * 创建Stack实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return Stack实例
     */
    @SafeVarargs
    public static <E> List<E> asStack(boolean unmodifiable, E... es) {
        List<E> result = new Stack<>();
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableList(result) : result;
    }

    /**
     * 创建CopyOnWriteArrayList实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           元素数组
     * @param <E>          元素类型
     * @return CopyOnWriteArrayList实例
     */
    @SafeVarargs
    public static <E> List<E> asCopyOnWriteArrayList(boolean unmodifiable, E... es) {
        List<E> result = new CopyOnWriteArrayList<>();
        for (E e : es) {
            if (e != null) {
                result.add(e);
            }
        }
        return unmodifiable ? Collections.unmodifiableList(result) : result;
    }

    /**
     * 根据键值数组创建HashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return HashMap实例
     */
    public static <K, V> Map<K, V> asHashMap(boolean unmodifiable, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new HashMap<>((int) (length / 0.75f) + 1);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值数组创建LinkedHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return LinkedHashMap实例
     */
    public static <K, V> Map<K, V> asLinkedHashMap(boolean unmodifiable, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new LinkedHashMap<>((int) (length / 0.75f) + 1);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值数组创建TreeMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param comparator   比较器
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return TreeMap实例
     */
    public static <K, V> Map<K, V> asTreeMap(boolean unmodifiable, Comparator<? super K> comparator, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new TreeMap<>(comparator);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值数组创建Hashtable实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return Hashtable实例
     */
    public static <K, V> Map<K, V> asHashtable(boolean unmodifiable, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new Hashtable<>((int) (length / 0.75f) + 1);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值数组创建ConcurrentHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return ConcurrentHashMap实例
     */
    public static <K, V> Map<K, V> asConcurrentHashMap(boolean unmodifiable, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new ConcurrentHashMap<>((int) (length / 0.75f) + 1);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值数组创建WeakHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return WeakHashMap实例
     */
    public static <K, V> Map<K, V> asWeakHashMap(boolean unmodifiable, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new WeakHashMap<>((int) (length / 0.75f) + 1);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值数组创建IdentityHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param ks           键数组
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return IdentityHashMap实例
     */
    public static <K, V> Map<K, V> asIdentityHashMap(boolean unmodifiable, K[] ks, V[] vs) {
        if (ks == null || vs == null) {
            throw new IllegalArgumentException("Key or value array cannot be null");
        }
        int length = ks.length;
        if (length != vs.length) {
            throw new IllegalArgumentException("The length of key array must be equal to the length of value array");
        }
        Map<K, V> result = new IdentityHashMap<>((int) (length / 0.75f) + 1);
        for (int i = 0; i < length; i++) {
            if (ks[i] != null) {
                result.put(ks[i], vs[i]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建HashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return HashMap实例
     */
    public static <K, V> Map<K, V> asHashMap(boolean unmodifiable, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        int length = vs.length;
        Map<K, V> result = new HashMap<>((int) (length / 0.75f) + 1);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建LinkedHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return LinkedHashMap实例
     */
    public static <K, V> Map<K, V> asLinkedHashMap(boolean unmodifiable, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        int length = vs.length;
        Map<K, V> result = new LinkedHashMap<>((int) (length / 0.75f) + 1);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建TreeMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param comparator   比较器
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return TreeMap实例
     */
    public static <K, V> Map<K, V> asTreeMap(boolean unmodifiable, Comparator<? super K> comparator, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        Map<K, V> result = new TreeMap<>(comparator);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建Hashtable实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return Hashtable实例
     */
    public static <K, V> Map<K, V> asHashtable(boolean unmodifiable, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        int length = vs.length;
        Map<K, V> result = new Hashtable<>((int) (length / 0.75f) + 1);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建ConcurrentHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return ConcurrentHashMap实例
     */
    public static <K, V> Map<K, V> asConcurrentHashMap(boolean unmodifiable, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        int length = vs.length;
        Map<K, V> result = new ConcurrentHashMap<>((int) (length / 0.75f) + 1);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建WeakHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return WeakHashMap实例
     */
    public static <K, V> Map<K, V> asWeakHashMap(boolean unmodifiable, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        int length = vs.length;
        Map<K, V> result = new WeakHashMap<>((int) (length / 0.75f) + 1);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据值数组和键获取函数创建IdentityHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param keyGetter    键获取函数
     * @param vs           值数组
     * @param <K>          键类型
     * @param <V>          值类型
     * @return IdentityHashMap实例
     */
    public static <K, V> Map<K, V> asIdentityHashMap(boolean unmodifiable, Function<V, K> keyGetter, V[] vs) {
        if (vs == null) {
            throw new IllegalArgumentException("Value array cannot be null");
        }
        int length = vs.length;
        Map<K, V> result = new IdentityHashMap<>((int) (length / 0.75f) + 1);
        for (V v : vs) {
            K key = keyGetter.apply(v);
            if (key != null) {
                result.put(key, v);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建HashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return HashMap实例
     */
    public static <K, V> Map<K, V> asHashMap(boolean unmodifiable, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new HashMap<>();
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建LinkedHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return LinkedHashMap实例
     */
    public static <K, V> Map<K, V> asLinkedHashMap(boolean unmodifiable, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new LinkedHashMap<>();
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建TreeMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param comparator   比较器
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return TreeMap实例
     */
    public static <K, V> Map<K, V> asTreeMap(boolean unmodifiable, Comparator<? super K> comparator, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new TreeMap<>(comparator);
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建Hashtable实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return Hashtable实例
     */
    public static <K, V> Map<K, V> asHashtable(boolean unmodifiable, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new Hashtable<>();
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建ConcurrentHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return ConcurrentHashMap实例
     */
    public static <K, V> Map<K, V> asConcurrentHashMap(boolean unmodifiable, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new ConcurrentHashMap<>();
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建WeakHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return WeakHashMap实例
     */
    public static <K, V> Map<K, V> asWeakHashMap(boolean unmodifiable, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new WeakHashMap<>();
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据配置函数创建IdentityHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param setting      配置函数
     * @param <K>          键类型
     * @param <V>          值类型
     * @return IdentityHashMap实例
     */
    public static <K, V> Map<K, V> asIdentityHashMap(boolean unmodifiable, Consumer<Map<K, V>> setting) {
        Map<K, V> result = new IdentityHashMap<>();
        if (setting != null) {
            setting.accept(result);
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 根据键值对数组创建HashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return HashMap实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asHashMap(boolean unmodifiable, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new HashMap<>(computeMapCapacity(length)), es);
    }

    /**
     * 根据键值对数组创建LinkedHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return LinkedHashMap实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asLinkedHashMap(boolean unmodifiable, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new LinkedHashMap<>(computeMapCapacity(length)), es);
    }

    /**
     * 根据键值对数组创建TreeMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param comparator   比较器
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return TreeMap实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asTreeMap(boolean unmodifiable, Comparator<? super E> comparator, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new TreeMap<>(comparator), es);
    }

    /**
     * 根据键值对数组创建Hashtable实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return Hashtable实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asHashtable(boolean unmodifiable, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new Hashtable<>(computeMapCapacity(length)), es);
    }

    /**
     * 根据键值对数组创建ConcurrentHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return ConcurrentHashMap实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asConcurrentHashMap(boolean unmodifiable, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new ConcurrentHashMap<>(computeMapCapacity(length)), es);
    }

    /**
     * 根据键值对数组创建WeakHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return WeakHashMap实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asWeakHashMap(boolean unmodifiable, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new WeakHashMap<>(computeMapCapacity(length)), es);
    }

    /**
     * 根据键值对数组创建IdentityHashMap实例
     *
     * @param unmodifiable 是否返回不可变集合
     * @param es           键值对数组（交替排列）
     * @param <E>          键值类型
     * @return IdentityHashMap实例
     */
    @SafeVarargs
    public static <E> Map<E, E> asIdentityHashMap(boolean unmodifiable, E... es) {
        int length = validateEvenLengthArrayAndReturnLength(es) / 2;
        return populateMapWithPairs(unmodifiable, new IdentityHashMap<>(computeMapCapacity(length)), es);
    }

    /**
     * 计算Map容量
     *
     * @param length 元素数量
     * @return 计算后的容量
     */
    private static int computeMapCapacity(int length) {
        return (int) (length / 0.75f) + 1;
    }

    /**
     * 验证数组长度是否为偶数
     *
     * @param es  元素数组
     * @param <E> 元素类型
     * @return 数组长度
     */
    @SafeVarargs
    private static <E> int validateEvenLengthArrayAndReturnLength(E... es) {
        if (es == null) {
            throw new IllegalArgumentException("Elements array cannot be null");
        }
        int length = es.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Elements array length must be even, but got: " + length);
        }
        return length;
    }

    /**
     * 用键值对填充Map
     *
     * @param unmodifiable 是否返回不可变集合
     * @param result       目标Map
     * @param es           键值对数组
     * @param <E>          键值类型
     * @return 填充后的Map
     */
    @SafeVarargs
    private static <E> Map<E, E> populateMapWithPairs(boolean unmodifiable, Map<E, E> result, E... es) {
        for (int i = 0; i < es.length; i += 2) {
            E key = es[i];
            if (key != null) {
                result.put(key, es[i + 1]);
            }
        }
        return unmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    /**
     * 私有构造函数，防止实例化
     */
    private UniversalConstant() {
        throw new AssertionError("No instances.");
    }

}
