/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.core.cc;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 通用常量类，提供各种基本类型常量、空对象实例和集合创建工具方法
 *
 * @author Terraqo
 */
public final class Constant {

    /**
     * 缓冲区大小，默认值为 4096 字节，可以通过系统属性 "BUFFER_SIZE" 进行配置。
     */
    public static final int BUFFER_SIZE = Math.max(Integer.getInteger("BUFFER_SIZE", 4096), 4096);

    /**
     * 系统临时目录的路径。
     */
    public static final Path PATH_TEMPORARY = Paths.get(System.getProperty("java.io.tmpdir"));

    /**
     * 运行时路径，表示当前工作目录的绝对路径
     */
    public static final Path PATH_RUNTIME = Paths.get("").toAbsolutePath();

    /**
     * 运行时缓存路径，位于运行时路径下的cache目录
     */
    public static final Path PATH_RUNTIME_CACHE = PATH_RUNTIME.resolve("cache");

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final boolean BOOLEAN_FALSE = false, BOOLEAN_TRUE = true;
    public static final boolean BOOLEAN_ZERO = false;
    public static final boolean[] BOOLEAN_EMPTY_ARRAY = {};
    public static final Boolean[] BOOLEAN_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> BOOLEAN_TYPE = boolean.class, BOOLEAN_ARRAY_TYPE = boolean[].class, BOOLEAN_OBJECT_TYPE = Boolean.class, BOOLEAN_OBJECT_ARRAY_TYPE = Boolean[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final char CHAR_ZERO = '\0';
    public static final char[] CHAR_EMPTY_ARRAY = {};
    public static final Character[] CHAR_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> CHAR_TYPE = char.class, CHAR_ARRAY_TYPE = char[].class, CHAR_OBJECT_TYPE = Character.class, CHAR_OBJECT_ARRAY_TYPE = Character[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final byte BYTE_ZERO = 0;
    public static final byte[] BYTE_EMPTY_ARRAY = {};
    public static final Byte[] BYTE_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> BYTE_TYPE = byte.class, BYTE_ARRAY_TYPE = byte[].class, BYTE_OBJECT_TYPE = Byte.class, BYTE_OBJECT_ARRAY_TYPE = Byte[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final short SHORT_ZERO = 0;
    public static final short[] SHORT_EMPTY_ARRAY = {};
    public static final Short[] SHORT_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> SHORT_TYPE = short.class, SHORT_ARRAY_TYPE = short[].class, SHORT_OBJECT_TYPE = Short.class, SHORT_OBJECT_ARRAY_TYPE = Short[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final int INT_ZERO = 0;
    public static final int[] INT_EMPTY_ARRAY = {};
    public static final Integer[] INT_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> INT_TYPE = int.class, INT_ARRAY_TYPE = int[].class, INT_OBJECT_TYPE = Integer.class, INT_OBJECT_ARRAY_TYPE = Integer[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final long LONG_ZERO = 0L;
    public static final long[] LONG_EMPTY_ARRAY = {};
    public static final Long[] LONG_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> LONG_TYPE = long.class, LONG_ARRAY_TYPE = long[].class, LONG_OBJECT_TYPE = Long.class, LONG_OBJECT_ARRAY_TYPE = Long[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final float FLOAT_ZERO = 0.0f;
    public static final float[] FLOAT_EMPTY_ARRAY = {};
    public static final Float[] FLOAT_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> FLOAT_TYPE = float.class, FLOAT_ARRAY_TYPE = float[].class, FLOAT_OBJECT_TYPE = Float.class, FLOAT_OBJECT_ARRAY_TYPE = Float[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final double DOUBLE_ZERO = 0.0d;
    public static final double[] DOUBLE_EMPTY_ARRAY = {};
    public static final Double[] DOUBLE_OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> DOUBLE_TYPE = double.class, DOUBLE_ARRAY_TYPE = double[].class, DOUBLE_OBJECT_TYPE = Double.class, DOUBLE_OBJECT_ARRAY_TYPE = Double[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final Object[] OBJECT_EMPTY_ARRAY = {};
    public static final Class<?> OBJECT_TYPE = Object.class, OBJECT_ARRAY_TYPE = Object[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final Class<?>[] CLASS_EMPTY_ARRAY = {};
    public static final Class<?> CLASS_TYPE = Class.class, CLASS_ARRAY_TYPE = Class[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static final String STRING_EMPTY = "";
    public static final String[] STRING_EMPTY_ARRAY = {};
    public static final Class<?> STRING_TYPE = String.class, STRING_ARRAY_TYPE = String[].class;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------

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

    /**
     * 系统行分隔符，用于表示当前操作系统的换行符
     * 通过动态获取系统换行符来避免安全问题
     */
    public static final String SYSTEM_LINE_SEPARATOR;

    static {
        // 通过动态生成来避免安全问题
        StringWriter buf = new StringWriter(2);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        SYSTEM_LINE_SEPARATOR = buf.toString();
    }

    /**
     * 私有构造函数，防止实例化
     */
    private Constant() {
        throw new AssertionError("No instances.");
    }

    /**
     * 构建内置资源文件的路径
     *
     * @param clz      要获取路径的类对象，用于确定包名和类名
     * @param fileName 要获取的文件名
     * @return 构建完成的资源文件路径字符串，使用正斜杠分隔符
     */
    public static String getBuiltInPath(Class<?> clz, String fileName) {
        // 构建以 META-INF 开头的路径，然后依次添加包名、类名和文件名
        Path path = Path.of("META-INF")
                .resolve(clz.getPackage().getName().replace('.', '/'))
                .resolve(clz.getSimpleName());
        if (fileName != null) {
            path = path.resolve(fileName);
        }
        return path.toString().replace('\\', '/');
    }

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
     * 创建不可变动List
     *
     * @param elements 值列表
     * @param <E>      值类型
     * @return 返回 {@code List<E>}
     */
    @SafeVarargs
    public static <E> List<E> ofUnmodifiableList(E... elements) {
        List<E> r = new ArrayList<>(elements.length);
        for (E element : elements) if (element != null) r.add(element);
        return Collections.unmodifiableList(r);
    }

    /**
     * 创建不可变动Set
     *
     * @param elements 值列表
     * @param <E>      值类型
     * @return 返回 {@code Set<E>}
     */
    @SafeVarargs
    public static <E> Set<E> ofUnmodifiableSet(E... elements) {
        Set<E> r = new LinkedHashSet<>(elements.length);
        for (E element : elements) if (element != null) r.add(element);
        return Collections.unmodifiableSet(r);
    }

    /**
     * 创建不可变动Map
     *
     * @param elements 键值列表
     * @param <E>      值类型
     * @return 返回 {@code Map<E, E>}
     */
    @SafeVarargs
    public static <E> Map<E, E> ofUnmodifiableMap(E... elements) {
        if (elements.length % 2 != 0) throw new IllegalArgumentException();
        HashMap<E, E> r = new HashMap<>();
        for (int i = 0; i < elements.length; i += 2) r.put(elements[i], elements[i + 1]);
        return Collections.unmodifiableMap(r);
    }

    /**
     * 创建不可变动Map
     *
     * @param keys   键列表
     * @param values 值列表
     * @param <K>    键类型
     * @param <V>    值类型
     * @return 返回 {@code Map<K, V>}
     */
    public static <K, V> Map<K, V> ofUnmodifiableMap(K[] keys, V[] values) {
        if (keys.length != values.length) throw new IllegalArgumentException();
        HashMap<K, V> r = new HashMap<>();
        for (int i = 0; i < keys.length; i++) r.put(keys[i], values[i]);
        return Collections.unmodifiableMap(r);
    }

    /**
     * 创建不可变动SortedMap
     *
     * @param elements 键值列表
     * @param <E>      值类型
     * @return 返回 {@code Map<E, E>}
     */
    @SafeVarargs
    public static <K, E> SortedMap<K, E> ofUnmodifiableSortedMap(Function<E, K> keyGetter, E... elements) {
        return ofUnmodifiableSortedMap(keyGetter, null, elements);
    }

    /**
     * 创建不可变动SortedMap
     *
     * @param elements 键值列表
     * @param <E>      值类型
     * @return 返回 {@code Map<E, E>}
     */
    @SafeVarargs
    public static <K, E> SortedMap<K, E> ofUnmodifiableSortedMap(Function<E, K> keyGetter, Comparator<? super K> comparator, E... elements) {
        if (comparator == null) comparator = Comparator.comparing(Objects::hashCode);
        TreeMap<K, E> r = new TreeMap<>(comparator);
        for (E element : elements) r.put(keyGetter.apply(element), element);
        return Collections.unmodifiableSortedMap(r);
    }

    @SafeVarargs
    public static <E> List<E> initOrUpdateList(List<E> input, E... es) {
        if (input == null) input = new ArrayList<>();
        Collections.addAll(input, es);
        return input;
    }

    @SafeVarargs
    public static <E> Set<E> initOrUpdateSet(Set<E> input, E... es) {
        if (input == null) input = new HashSet<>();
        Collections.addAll(input, es);
        return input;
    }

    /**
     * 初始化或更新Map
     *
     * @param cacheInstance  缓存实例
     * @param override       覆盖函数
     * @param initialization 初始化函数
     * @param <K>            键类型
     * @param <V>            值类型
     * @return 返回Map
     */
    public static <K, V> Map<K, V> initOrUpdateMap(Map<K, V> cacheInstance, Consumer<Map<K, V>> override, Consumer<Map<K, V>> initialization) {
        if (cacheInstance != null) {
            if (override == null) return cacheInstance;
            Map<K, V> map = new HashMap<>(cacheInstance);
            override.accept(map);
            return map;
        }
        Map<K, V> map = new HashMap<>();
        if (initialization != null) initialization.accept(map);
        return map;
    }

    /**
     * 初始化或更新SortedMap
     *
     * @param cacheInstance  缓存实例
     * @param comparator     比较器
     * @param override       覆盖函数
     * @param initialization 初始化函数
     * @param <K>            键类型
     * @param <V>            值类型
     * @return 返回SortedMap
     */
    public static <K, V> SortedMap<K, V> initOrUpdateSortedMap(SortedMap<K, V> cacheInstance, Consumer<SortedMap<K, V>> override, Consumer<SortedMap<K, V>> initialization, Comparator<K> comparator) {
        if (cacheInstance != null) {
            if (override == null) return cacheInstance;
            SortedMap<K, V> map = new TreeMap<>(comparator);
            map.putAll(cacheInstance);
            override.accept(map);
            return map;
        }
        SortedMap<K, V> map = new TreeMap<>(comparator);
        if (initialization != null) initialization.accept(map);
        return map;
    }
}
