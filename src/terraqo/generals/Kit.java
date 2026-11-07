/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals;

import com.github.potatoxf.generals.lang.ReferenceThreadLocal;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 全局常量接口
 *
 * @author potatoxf
 */
@SuppressWarnings({"unused"})
public interface Kit {
    //------------------------------------------------------------------------------------------------------------------
    //类型
    //------------------------------------------------------------------------------------------------------------------

    Class<?> T_Z = boolean.class, T_WZ = Boolean.class, T_AZ = boolean[].class,
            T_C = char.class, T_WC = Character.class, T_AC = char[].class,
            T_B = byte.class, T_WB = Byte.class, T_AB = byte[].class,
            T_S = short.class, T_WS = Short.class, T_AS = short[].class,
            T_I = int.class, T_WI = Integer.class, T_AI = int[].class,
            T_J = long.class, T_WJ = Long.class, T_AJ = long[].class,
            T_F = float.class, T_WF = Float.class, T_AF = float[].class,
            T_D = double.class, T_WD = Double.class, T_AD = double[].class,
            T_O = Object.class, T_AO = Object[].class;
    Map<Class<?>, Class<?>> PRIMITIVE_MAPPING = Kit.unmodifiableMap(
            T_Z, T_WZ, T_C, T_WC, T_B, T_WB, T_S, T_WS, T_I, T_WI, T_J, T_WJ, T_F, T_WF, T_D, T_WD,
            T_WZ, T_Z, T_WC, T_C, T_WB, T_B, T_WS, T_S, T_WI, T_I, T_WJ, T_J, T_WF, T_F, T_WD, T_D
    );

    //------------------------------------------------------------------------------------------------------------------
    //常量
    //------------------------------------------------------------------------------------------------------------------

    int BUFFER_SIZE = Math.max(Integer.getInteger("BUFFER_SIZE", 4096), 4096);
    Path TEMP_PATH = Paths.get(System.getProperty("java.io.tmpdir"));
    Path RUNTIME_PATH = Paths.get("").toAbsolutePath();
    Path RUNTIME_CACHE_PATH = RUNTIME_PATH.resolve("cache");

    char C__B = 'b', C_F = 'f', C_T = 't', C_R = 'r', C_N = 'n';
    char C_BACKSPACE = '\b', C_FLIP = '\f', C_TAB = '\t', C_CR = '\r', C_LF = '\n';
    String S_BACKSPACE = "\b", S_FLIP = "\f", S_TAB = "\t", S_CR = "\r", S_LF = "\n", S_CRLF = "\r\n";
    char C_BRACE_START = '{', C_BRACE_END = '}', C_BRACKET_START = '[', C_BRACKET_END = ']';
    String S_BRACE_START = "{", S_BRACE_END = "}", S_BRACKET_START = "[", S_BRACKET_END = "]";
    char C_SPACE = ' ', C_DOT = '.', C_SLASH = '/', C_BACKSLASH = '\\', C_SINGLE_QUOTE = '\'', C_DOUBLE_QUOTES = '"';
    String S_SPACE = " ", S_DOT = ".", S_LASH = "/", S_BACKSLASH = "\\", S_SINGLE_QUOTE = "'", S_DOUBLE_QUOTES = "\"";
    char C_DASHED = '-', C_UNDERLINE = '_', C_COMMA = ',', C_AMP = '&', C_COLON = ':', C_AT = '@', C_HASHTAG = '#', C_PIPE = '|';
    String S_DASHED = "-", S_UNDERLINE = "_", S_COMMA = ",", S_AMP = "&", S_COLON = ":", S_AT = "@", S_HASHTAG = "#", S_PIPE = "|";

    String S_EMPTY = "", S_NULL = "null", S_UNDEFINED = "undefined";
    String S_XML_NBSP = "&nbsp;", S_XML_AMP = "&amp;", S_XML_QUOTE = "&quot;", S_XML_APOS = "&apos", S_XML_LT = "&lt;", S_XML_GT = "&gt;";
    String S_EMPTY_OBJECT = "{}", S_EMPTY_ARRAY = "[]", S_INVALID_BLANK = " \t\n\r\f";
    String S_DIGIT = "0123456789", S_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ", S_LOWER = S_UPPER.toLowerCase(), S_UPPER_HEX = S_DIGIT + S_UPPER.substring(0, 6), S_LOWER_HEX = S_UPPER_HEX.toLowerCase();
    String SYSTEM_LINE_SEPARATOR = ((Supplier<String>) () -> {
        // avoid security issues
        StringWriter buf = new StringWriter(2);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        return buf.toString();
    }).get();
    /**
     * Ascii 小写字母编码下界
     * Ascii 小写字母编码上界
     * Ascii 大写字母编码下界
     * Ascii 大写字母编码上界
     * Ascii 数字编码下界
     * Ascii 数字编码上界
     */
    int EOF = -1, INITIAL_HASH = 7, MULTIPLIER = 31, BYTE = 8, WORD = 16, DWORD = 32, QWORD = 64,
            LETTER_LLO = 97, LETTER_LHI = 122, LETTER_ULO = 65, LETTER_UHI = 90, NUMBER_LO = 48, NUMBER_HI = 57;

    boolean FALSE = false, TRUE = true;
    char ZERO_CHAR = 0;
    byte ZERO_BYTE = 0;
    short ZERO_SHORT = 0;
    int ZERO_INT = 0;
    long ZERO_LONG = 0;
    float ZERO_FLOAT = 0;
    double ZERO_DOUBLE = 0;
    boolean[] EMPTY_ARRAY_Z = {};
    Boolean[] EMPTY_ARRAY_WZ = {};
    char[] EMPTY_ARRAY_C = {};
    Character[] EMPTY_ARRAY_WC = {};
    byte[] EMPTY_ARRAY_B = {};
    Byte[] EMPTY_ARRAY_WB = {};
    short[] EMPTY_ARRAY_S = {};
    Short[] EMPTY_ARRAY_WS = {};
    int[] EMPTY_ARRAY_I = {};
    Integer[] EMPTY_ARRAY_WI = {};
    long[] EMPTY_ARRAY_L = {};
    Long[] EMPTY_ARRAY_WL = {};
    float[] EMPTY_ARRAY_F = {};
    Float[] EMPTY_ARRAY_WF = {};
    double[] EMPTY_ARRAY_D = {};
    Double[] EMPTY_ARRAY_WD = {};
    Object[] EMPTY_EMPTY_OBJECT = {};
    Class<?>[] EMPTY_ARRAY_CLASS = {};
    String[] EMPTY_ARRAY_STRING = {};

    OutputStream EMPTY_OUTPUT_STREAM = new OutputStream() {

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
    PrintStream EMPTY_PRINT_STREAM = new PrintStream(EMPTY_OUTPUT_STREAM);
    Writer EMPTY_WRITER = new Writer() {

        @Override
        public void write(int c) {
        }

        @Override
        public void write(char[] chars) {
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
        public void write(char[] chars, int off, int len) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() {

        }
    };
    Appendable EMPTY_APPENDABLE = EMPTY_WRITER;

    //------------------------------------------------------------------------------------------------------------------
    //正则表达式
    //------------------------------------------------------------------------------------------------------------------

    Pattern P_HTTP_OR_HTTPS_URL = Pattern.compile("(https?)://" +
            "([\\w\\-]+\\.)*[\\w\\-]+" +
            "(:[0-9]+)?" +
            "((/[\\w-]*)*" +
            "(\\?[\\w-%:;&@#+=]*)?)");
    /**
     * ascii 可见字符
     */
    Pattern P_VALID_CHAR = Pattern.compile("[0-9A-Za-z`~!@#%&-_=;:'\",<>/$()*+.\\[\\]?^{}\\\\]+");
    /**
     * TOKEN标识
     */
    Pattern P_CODE_SYMBOL_NAME = Pattern.compile("^[a-zA-Z_$][a-zA-Z0-9_$]*"),
            P_FILE_NAME = Pattern.compile("[^\":*?<>^|/\\\\]+"),
            P_FILE_ILLEGAL = Pattern.compile("[\":*?<>^|/\\\\]+");
    /**
     * 非法路径字符
     */
    Pattern P_PATH_DOT = Pattern.compile("^\\.*$");
    /**
     * 正则表达式特殊字符
     */
    Pattern P_ESCAPE_REGEX = Pattern.compile("[\\\\^$|?.+*(){}\\[\\]]+"), P_PLACEHOLDER = Pattern.compile("\\$?\\{[^{}$]+}");
    /**
     * 英文字母 、数字和下划线
     */
    Pattern P_LINE = Pattern.compile("[\r\n]"),
            P_LETTER = Pattern.compile("[a-zA-Z]+"),
            P_GENERAL = Pattern.compile("[a-zA-Z_]+"),
            P_INTEGER = Pattern.compile("[+-]?\\d+"),
            P_DECIMAL = Pattern.compile("[+-]?\\d+(\\.\\d+)?"),
            P_NUMBERS = Pattern.compile("[+-]?([1-9](,?\\d*)*|0)(.\\d+)?"),
            P_OCT = Pattern.compile("0[0-7]+"),
            P_HEX = Pattern.compile("0[xX][\\dA-Fa-f]+");

    static BitSet getLetterNumberBitSet() {
        BitSet safeChars = new BitSet(256);
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            safeChars.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            safeChars.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            safeChars.set(i);
        }
        return safeChars;
    }

    static Object[] emptyArguments(int length) {
        Object[] args = new Object[length];
        Arrays.fill(args, "");
        return args;
    }

    /**
     * 创建不可变动List
     *
     * @param elements 值列表
     * @param <E>      值类型
     * @return 返回 {@code List<E>}
     */
    @SafeVarargs
    static <E> List<E> ofUnmodifiableList(E... elements) {
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
    static <E> Set<E> ofUnmodifiableSet(E... elements) {
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
    static <E> Map<E, E> unmodifiableMap(E... elements) {
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
    static <K, V> Map<K, V> unmodifiableMap(K[] keys, V[] values) {
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
    static <K, E> SortedMap<K, E> unmodifiableSortedMap(Function<E, K> keyGetter, E... elements) {
        return Kit.unmodifiableSortedMap(keyGetter, null, elements);
    }

    /**
     * 创建不可变动SortedMap
     *
     * @param elements 键值列表
     * @param <E>      值类型
     * @return 返回 {@code Map<E, E>}
     */
    @SafeVarargs
    static <K, E> SortedMap<K, E> unmodifiableSortedMap(Function<E, K> keyGetter, Comparator<? super K> comparator, E... elements) {
        if (comparator == null) comparator = Comparator.comparing(Objects::hashCode);
        TreeMap<K, E> r = new TreeMap<>(comparator);
        for (E element : elements) r.put(keyGetter.apply(element), element);
        return Collections.unmodifiableSortedMap(r);
    }

    @SafeVarargs
    static <E> List<E> initOrUpdateList(List<E> input, E... es) {
        if (input == null) input = new ArrayList<>();
        Collections.addAll(input, es);
        return input;
    }

    @SafeVarargs
    static <E> Set<E> initOrUpdateSet(Set<E> input, E... es) {
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
    static <K, V> Map<K, V> initOrUpdateMap(Map<K, V> cacheInstance, Consumer<Map<K, V>> override, Consumer<Map<K, V>> initialization) {
        if (cacheInstance != null) {
            if (override == null) return cacheInstance;
            Map<K, V> map = new HashMap<>(cacheInstance);
            override.accept(map);
            return map;
        }
        Map<K, V> map = new HashMap<>();
        if (initialization != null) initialization.accept(map);
        return Collections.unmodifiableMap(map);
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
    static <K, V> SortedMap<K, V> initOrUpdateSortedMap(SortedMap<K, V> cacheInstance, Consumer<SortedMap<K, V>> override, Consumer<SortedMap<K, V>> initialization, Comparator<K> comparator) {
        if (cacheInstance != null) {
            if (override == null) return cacheInstance;
            SortedMap<K, V> map = new TreeMap<>(comparator);
            map.putAll(cacheInstance);
            override.accept(map);
            return map;
        }
        SortedMap<K, V> map = new TreeMap<>(comparator);
        if (initialization != null) initialization.accept(map);
        return Collections.unmodifiableSortedMap(map);
    }

    static byte[] getScratchByteArray() {
        byte[] array = Inner.THREAD_LOCAL_BYTE_ARRAY.get();
        if (array == null) {
            array = new byte[BUFFER_SIZE];
            Inner.THREAD_LOCAL_BYTE_ARRAY.set(array);
        }
        Arrays.fill(array, (byte) 0);
        return array;
    }

    static char[] getScratchCharArray() {
        char[] array = Inner.THREAD_LOCAL_CHAR_ARRAY.get();
        if (array == null) {
            array = new char[BUFFER_SIZE];
            Inner.THREAD_LOCAL_CHAR_ARRAY.set(array);
        }
        Arrays.fill(array, (char) 0);
        return array;
    }

    class Inner {
        private static final ReferenceThreadLocal<byte[]> THREAD_LOCAL_BYTE_ARRAY = ReferenceThreadLocal.weakWithInitial(() -> new byte[BUFFER_SIZE]);
        private static final ReferenceThreadLocal<char[]> THREAD_LOCAL_CHAR_ARRAY = ReferenceThreadLocal.weakWithInitial(() -> new char[BUFFER_SIZE]);

        private Inner() {
        }
    }
}
