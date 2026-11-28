/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author ApexSX
 */
package io.github.apex_sx.forge.core;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 通用日志处理工具类
 * <p>
 * 提供统一的日志接口，支持多种日志框架的集成，包括：
 * - 内置控制台日志（默认）
 * - Java Util Logging (JUL)
 * - Log4j
 * - Log4j2
 * - Slf4j
 * <p>
 * 内置注册的日志ID依次为{@code console}、{@code jul}、{@code log4j}、{@code log4j2}、{@code slf4j}，
 * 也可以通过{@link #addFactory(String, Factory)}注册自定义实现。
 * <p>
 * 日志对象通过{@link #of(String)}按日志ID创建，使用实例方法（如{@link #logError(String, Object...)}）输出日志，
 * 并通过{@link #isEnabled(Level)}判断指定级别是否启用。
 * <p>
 * 静态方法使用全局日志器输出日志，可通过{@link #global(String)}设置全局日志器的ID：
 * 如果{@link #isEnabledError()}返回true则{@link #error(String, Object...)}会输出日志
 * 如果{@link #isEnabledWarn()}返回true则{@link #warn(String, Object...)}会输出日志
 * 如果{@link #isEnabledInfo()}返回true则{@link #info(String, Object...)}会输出日志
 * 如果{@link #isEnabledDebug()}返回true则{@link #debug(String, Object...)}会输出日志
 * 如果{@link #isEnabledTrace()}返回true则{@link #trace(String, Object...)}会输出日志
 * <p>
 * 全局最低级别通过{@link #minimumLevel(Level)}设置，低于该级别的日志在所有日志实现上都不会输出；
 * 日志总开关通过{@link #closeLogger()}与{@link #openLogger()}控制；
 * 需要根据条件"抛异常或记日志"时使用{@link #throwIfError(boolean, Level, Throwable, String, Object...)}等throwIf族方法。
 *
 * @author ApexSX
 */
public final class Logger {
    /**
     * 日志类的完全限定类名，用于在堆栈跟踪中识别调用者
     * 通过获取Logger类的名称来初始化，避免硬编码类名
     */
    private static final String LOGGER_CLASS_NAME = Logger.class.getName();
    /**
     * 堆栈遍历器，保留类引用以便过滤Logger及其嵌套类的栈帧
     * 与Thread.getStackTrace相比，只会按需展开到找到调用者为止，避免一次性捕获完整堆栈
     */
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    /**
     * 控制台日志的时间戳格式
     * 复用单个formatter实例，避免每次输出重复构建；输出结果与OffsetDateTime的ISO格式一致
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    /**
     * 全局日志开关，使用原子布尔值保证线程安全
     * 默认值为true，表示日志功能处于开启状态
     */
    private static final AtomicBoolean LOGGER_ENABLED = new AtomicBoolean(true);
    /**
     * 线程本地变量，用于存储每个线程的日志前缀
     */
    private static final ThreadLocal<String> LOGGER_PREFIX = new ThreadLocal<>();
    /**
     * 日志工厂注册表，键为归一化（小写）后的日志ID
     * 仅用于键值查找，无需排序，因此使用ConcurrentHashMap
     */
    private static final Map<String, Factory> FACTORY_MAP = new ConcurrentHashMap<>();
    /**
     * 控制台日志工厂实现，直接使用System.out和System.err输出日志
     */
    private static final Factory COMMON_FACTORY = new CommonFactory();
    /**
     * 默认日志实例
     */
    private static final Logger DEFAULT_LOGGER = new Logger("Console");
    /**
     * 全局日志引用，使用原子引用保证线程安全
     */
    private static final AtomicReference<Logger> GLOBAL_LOG = new AtomicReference<>(DEFAULT_LOGGER);
    /**
     * 全局最低日志级别，低于该级别的日志不会输出
     * 默认值为TRACE，表示不额外限制；各日志实现的自身级别配置仍需单独配置
     */
    private static volatile Level minimumLevel = Level.TRACE;

    /*
     * 静态初始化块，用于注册内置的日志工厂实现
     */
    static {
        // 注册内置的日志工厂实现，与控制台回退使用的工厂保持同一实例，避免重复缓存
        FACTORY_MAP.put("console", COMMON_FACTORY);
        // Java Util Logging (JUL) 日志工厂实现
        FACTORY_MAP.put("jul", new JulFactory());
        // Log4j 日志工厂实现
        FACTORY_MAP.put("log4j", new CommonFactory("Log4j", "org.apache.log4j.LogManager", "org.apache.log4j.Logger"));
        // Log4j2 日志工厂实现
        FACTORY_MAP.put("log4j2", new CommonFactory("Log4j2", "org.apache.logging.log4j.LogManager", "org.apache.logging.log4j.Logger"));
        // SLF4J 日志工厂实现
        FACTORY_MAP.put("slf4j", new CommonFactory("Slf4j", "org.slf4j.LoggerFactory", "org.slf4j.Logger"));
    }

    /**
     * 注册自定义日志工厂
     * <p>
     * 注册后即可通过{@link #of(String)}或{@link #global(String)}使用该日志ID；
     * 同名ID会被覆盖，日志ID大小写不敏感。
     *
     * @param id      日志ID，大小写不敏感
     * @param factory 日志工厂
     * @return 如果设置成功返回true，否则返回false
     */
    public static boolean addFactory(String id, Factory factory) {
        if (id != null && factory != null) {
            FACTORY_MAP.put(factoryKey(id), factory);
            return true;
        }
        return false;
    }

    /**
     * 设置当前线程的日志前缀
     * <p>
     * 前缀会拼接到该线程每条日志消息之前，仅在当前线程生效并覆盖原有前缀，
     * 适合在请求处理、任务执行等场景中标记上下文信息。
     *
     * @param prefix 日志前缀，为null时表示不拼接前缀
     */
    public static void addPrefix(String prefix) {
        LOGGER_PREFIX.set(prefix);
    }

    /**
     * 清除当前线程的日志前缀
     */
    public static void clearPrefix() {
        LOGGER_PREFIX.remove();
    }

    /**
     * 关闭日志功能
     * <p>
     * 将全局日志开关设置为关闭状态，禁止所有日志输出。
     * 该方法是线程安全的。
     */
    public static void closeLogger() {
        LOGGER_ENABLED.set(false);
    }

    /**
     * 输出DEBUG级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public static void debug(String messageTemplate, Object... args) {
        Logger.global().logDebug(messageTemplate, args);
    }

    /**
     * 输出ERROR级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public static void error(String messageTemplate, Object... args) {
        Logger.global().logError(messageTemplate, args);
    }

    /**
     * 设置全局日志实现
     * <p>
     * 设置成功后，所有静态日志方法都会改用该日志实现；日志ID未知或对应工厂不可用时不做任何修改。
     *
     * @param id 日志ID，大小写不敏感
     * @return 如果设置成功返回true，否则返回false
     */
    public static boolean global(String id) {
        if (check(id, false)) {
            if (!Objects.equals(global().id, factoryKey(id))) {
                GLOBAL_LOG.set(new Logger(id));
            }
            return true;
        }
        return false;
    }

    /**
     * 获取全局日志实例
     *
     * @return 全局日志实例
     */
    public static Logger global() {
        return GLOBAL_LOG.get();
    }

    /**
     * 输出INFO级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public static void info(String messageTemplate, Object... args) {
        Logger.global().logInfo(messageTemplate, args);
    }

    /**
     * 检查是否启用DEBUG级别日志
     *
     * @return 如果启用返回true，否则返回false
     */
    public static boolean isEnabledDebug() {
        return Logger.global().isEnabled(Level.DEBUG);
    }

    /**
     * 检查是否启用ERROR级别日志
     *
     * @return 如果启用返回true，否则返回false
     */
    public static boolean isEnabledError() {
        return Logger.global().isEnabled(Level.ERROR);
    }

    /**
     * 检查是否启用INFO级别日志
     *
     * @return 如果启用返回true，否则返回false
     */
    public static boolean isEnabledInfo() {
        return Logger.global().isEnabled(Level.INFO);
    }

    /**
     * 检查是否启用TRACE级别日志
     *
     * @return 如果启用返回true，否则返回false
     */
    public static boolean isEnabledTrace() {
        return Logger.global().isEnabled(Level.TRACE);
    }

    /**
     * 检查是否启用WARN级别日志
     *
     * @return 如果启用返回true，否则返回false
     */
    public static boolean isEnabledWarn() {
        return Logger.global().isEnabled(Level.WARN);
    }

    /**
     * 设置全局最低日志级别
     * <p>
     * 低于该级别的日志在所有日志实现上都不会输出，作为全局统一的下限；
     * 默认值为{@link Level#TRACE}，即不额外限制。
     *
     * @param level 最低日志级别，为null时忽略本次设置
     */
    public static void minimumLevel(Level level) {
        if (level != null) {
            minimumLevel = level;
        }
    }

    /**
     * 获取全局最低日志级别
     *
     * @return 全局最低日志级别
     */
    public static Level minimumLevel() {
        return minimumLevel;
    }

    /**
     * 创建指定ID的日志实例
     * <p>
     * 每次调用都会返回新的实例，实例在首次输出日志时会绑定并缓存对应的日志实现对象，
     * 因此建议按类持有为静态常量（如{@code private static final Logger LOGGER = Logger.of("slf4j");}），
     * 这样同一实例的日志实现只需解析一次。
     *
     * @param id 日志ID，大小写不敏感
     * @return {@link Logger} 日志实例
     * @throws IllegalArgumentException 当日志ID没有对应的工厂或该工厂不可用时
     */
    public static Logger of(String id) {
        if (id == null) {
            return DEFAULT_LOGGER;
        }
        check(id, true);
        return new Logger(id);
    }

    /**
     * 开启日志功能
     * <p>
     * 将全局日志开关设置为开启状态，允许日志输出。
     * 该方法是线程安全的。
     */
    public static void openLogger() {
        LOGGER_ENABLED.set(true);
    }

    /**
     * 根据条件选择抛出{@link Error}或记录指定级别的日志
     * <p>
     * 当{@code isThrows}为true时，用格式化后的消息与给定异常构造{@link Error}抛出；
     * 否则按{@code level}输出一条带异常堆栈的日志并返回null。
     *
     * @param isThrows        是否抛出错误
     * @param level           日志级别，为null时按ERROR处理
     * @param throwable       异常对象，可为null
     * @param messageTemplate 消息模板
     * @param args            参数列表
     * @param <T>             返回类型
     * @return 不抛出错误时返回null
     */
    public static <T> T throwIfError(boolean isThrows, Level level, Throwable throwable, String messageTemplate, Object... args) {
        if (isThrows) {
            throw new Error(String.format(messageTemplate, args), throwable);
        }
        return logAndReturnNull(level, throwable, messageTemplate, args);
    }

    /**
     * 根据条件选择抛出{@link Exception}或记录指定级别的日志
     * <p>
     * 当{@code isThrows}为true时，用格式化后的消息与给定异常构造{@link Exception}抛出；
     * 否则按{@code level}输出一条带异常堆栈的日志并返回null。
     *
     * @param isThrows        是否抛出异常
     * @param level           日志级别，为null时按ERROR处理
     * @param throwable       异常对象，可为null
     * @param messageTemplate 消息模板
     * @param args            参数列表
     * @param <T>             返回类型
     * @return 不抛出异常时返回null
     * @throws Exception 当isThrows为true时抛出
     */
    public static <T> T throwIfException(boolean isThrows, Level level, Throwable throwable, String messageTemplate, Object... args) throws Exception {
        if (isThrows) {
            throw new Exception(String.format(messageTemplate, args), throwable);
        }
        return logAndReturnNull(level, throwable, messageTemplate, args);
    }

    /**
     * 根据条件选择抛出{@link RuntimeException}或记录指定级别的日志
     * <p>
     * 当{@code isThrows}为true时，用格式化后的消息与给定异常构造{@link RuntimeException}抛出；
     * 否则按{@code level}输出一条带异常堆栈的日志并返回null。
     *
     * @param isThrows        是否抛出运行时异常
     * @param level           日志级别，为null时按ERROR处理
     * @param throwable       异常对象，可为null
     * @param messageTemplate 消息模板
     * @param args            参数列表
     * @param <T>             返回类型
     * @return 不抛出运行时异常时返回null
     */
    public static <T> T throwIfRuntimeException(boolean isThrows, Level level, Throwable throwable, String messageTemplate, Object... args) {
        if (isThrows) {
            throw new RuntimeException(String.format(messageTemplate, args), throwable);
        }
        return logAndReturnNull(level, throwable, messageTemplate, args);
    }

    /**
     * 输出TRACE级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public static void trace(String messageTemplate, Object... args) {
        Logger.global().logTrace(messageTemplate, args);
    }

    /**
     * 输出WARN级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public static void warn(String messageTemplate, Object... args) {
        Logger.global().logWarn(messageTemplate, args);
    }

    /**
     * 检查指定ID的日志工厂是否存在且受支持
     *
     * @param id       日志ID
     * @param isThrows 是否抛出异常
     * @return 如果存在且受支持返回true，否则返回false
     */
    private static boolean check(String id, boolean isThrows) {
        if (id == null) {
            if (!isThrows) {
                return false;
            }
            throw new IllegalArgumentException("The log implementation ID can not be null.");
        }
        Factory factory = FACTORY_MAP.get(factoryKey(id));
        if (factory == null) {
            if (!isThrows) {
                return false;
            }
            throw new IllegalArgumentException("There is no log implementation factory for [" + id + "].");
        }
        if (factory.isSupport()) {
            return true;
        }
        if (!isThrows) {
            return false;
        }
        throw new IllegalArgumentException("The log implementation factory that does not implement [" + id + "].");
    }

    /**
     * 将日志ID归一化为工厂注册表中使用的键
     *
     * @param id 日志ID
     * @return 小写形式的日志ID，入参为null时返回null
     */
    private static String factoryKey(String id) {
        return id == null ? null : id.toLowerCase(Locale.ROOT);
    }

    /**
     * 获取调用日志方法的堆栈跟踪元素
     * <p>
     * 通过跳过{@link Logger}自身及其所有嵌套类的栈帧来定位真正的调用者，
     * 因此不受日志调用层级变化的影响；若无法定位则返回占位元素，避免在输出日志时抛出异常。
     *
     * @return 调用者的堆栈跟踪元素
     */
    private static StackTraceElement getCallStackTraceElement() {
        return STACK_WALKER
                .walk(frames -> frames
                        .filter(frame -> !Logger.class.isNestmateOf(frame.getDeclaringClass()))
                        .map(StackWalker.StackFrame::toStackTraceElement)
                        .findFirst())
                .orElseGet(() -> new StackTraceElement(LOGGER_CLASS_NAME, "<unknown>", null, -1));
    }

    /**
     * 输出一条带异常的日志并返回null
     *
     * @param level           日志级别，为null时按ERROR处理
     * @param throwable       异常对象，可为null
     * @param messageTemplate 消息模板
     * @param args            参数列表
     * @param <T>             返回类型
     * @return 总是返回null
     */
    private static <T> T logAndReturnNull(Level level, Throwable throwable, String messageTemplate, Object... args) {
        Logger.global().write(level, throwable, messageTemplate, args);
        return null;
    }

    /**
     * 日志ID，用于标识不同的日志实现，统一归一化为小写
     */
    private final String id;
    /**
     * 当前日志实例绑定的日志实现容器
     * 首次解析后缓存，后续输出日志无需再查注册表、加锁；调用者类名变化时会重新绑定
     */
    private volatile Journal journal;

    /**
     * 构造函数，创建指定ID的日志实例
     *
     * @param id 日志ID，大小写不敏感，会被归一化为小写
     */
    private Logger(String id) {
        this.id = factoryKey(id);
    }

    /**
     * 记录TRACE级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public void logTrace(String messageTemplate, Object... args) {
        log(Level.TRACE, messageTemplate, args);
    }

    /**
     * 记录DEBUG级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public void logDebug(String messageTemplate, Object... args) {
        log(Level.DEBUG, messageTemplate, args);
    }

    /**
     * 记录INFO级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public void logInfo(String messageTemplate, Object... args) {
        log(Level.INFO, messageTemplate, args);
    }

    /**
     * 记录WARN级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public void logWarn(String messageTemplate, Object... args) {
        log(Level.WARN, messageTemplate, args);
    }

    /**
     * 记录ERROR级别日志
     *
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    public void logError(String messageTemplate, Object... args) {
        log(Level.ERROR, messageTemplate, args);
    }

    /**
     * 获取当前日志ID对应的日志工厂
     *
     * @return 日志工厂，未注册的日志ID返回控制台工厂
     */
    private Factory factory() {
        return FACTORY_MAP.getOrDefault(id, COMMON_FACTORY);
    }

    /**
     * 检查当前日志实例是否启用指定级别
     * <p>
     * 判断结果同时受全局日志开关、全局最低级别以及日志实现自身的级别配置约束，
     * 与{@link #log(Level, String, Object...)}的实际输出条件保持一致。
     *
     * @param level 日志级别
     * @return 如果启用返回true，否则返回false
     */
    public boolean isEnabled(Level level) {
        Journal journal = this.journal;
        if (journal == null) {
            journal = journalOf(getCallStackTraceElement().getClassName());
        }
        return journal.isEnabledFor(level);
    }

    /**
     * 输出指定级别的日志
     * <p>
     * 处理顺序为：全局开关 -> 定位调用者并绑定日志实现 -> 级别判断 -> 参数处理与消息格式化 -> 委托输出，
     * 级别未启用时会在格式化之前返回，避免无谓的字符串拼接开销。
     *
     * @param level           日志级别
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    private void log(Level level, String messageTemplate, Object... args) {
        write(level, null, messageTemplate, args);
    }

    /**
     * 输出指定级别且可携带异常的日志
     * <p>
     * 处理顺序为：全局开关 -> 定位调用者并绑定日志实现 -> 级别判断 -> 参数处理与消息格式化 -> 委托输出，
     * 级别未启用时会在格式化之前返回，避免无谓的字符串拼接开销。
     * 未显式给出异常时，按约定把最后一个{@link Throwable}参数当作待输出的异常。
     *
     * @param level           日志级别，为null时按ERROR处理
     * @param throwable       异常对象，可为null
     * @param messageTemplate 消息模板
     * @param args            参数列表
     */
    private void write(Level level, Throwable throwable, String messageTemplate, Object... args) {
        if (!LOGGER_ENABLED.get()) {
            return;
        }
        Level effectiveLevel = level == null ? Level.ERROR : level;
        // 定位调用者，其类名既决定日志归属的日志实现，也用于控制台输出
        StackTraceElement callStackTraceElement = getCallStackTraceElement();
        Journal journal = journalOf(callStackTraceElement.getClassName());
        // 级别未启用时不再格式化消息
        if (!journal.isEnabledFor(effectiveLevel)) {
            return;
        }
        // 未显式传入异常时，允许把异常作为最后一个参数传入
        if (throwable == null && args != null && args.length > 0 && args[args.length - 1] instanceof Throwable) {
            throwable = (Throwable) args[args.length - 1];
            args = Arrays.copyOf(args, args.length - 1);
        }
        journal.printLog(callStackTraceElement, effectiveLevel, throwable, messageTemplate, args);
    }

    /**
     * 获取指定类名对应的日志实现对象，并缓存到当前日志实例
     * <p>
     * 类名一致时直接复用缓存；不一致时按类名重新解析，保证日志归属到真正输出日志的类。
     *
     * @param className 调用者类名
     * @return 日志实现对象
     */
    private Journal journalOf(String className) {
        Journal journal = this.journal;
        if (journal != null && journal.className().equals(className)) {
            return journal;
        }
        journal = factory().getOrCreateJournal(className);
        this.journal = journal;
        return journal;
    }

    /**
     * 计算当前Logger对象的哈希值
     *
     * @return 基于id属性计算的哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * 判断当前Logger对象与另一个对象是否相等
     *
     * @param obj 要比较的对象
     * @return 如果对象相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Logger other = (Logger) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * 返回当前Logger对象的字符串表示形式
     *
     * @return Logger的id值
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * Java Util Logging (JUL) 日志工厂实现
     * 该类适配了Java内置的java.util.logging框架，将通用日志接口转换为JUL的具体调用
     */
    private static class JulFactory extends Factory {
        /**
         * 构造函数，初始化工厂名称为"Java Util Logging"
         */
        private JulFactory() {
            super("Java Util Logging");
        }

        /**
         * 初始化日志工厂
         * java.util.logging是JDK内置的日志实现，无需反射检测即可直接使用
         *
         * @return 总是返回true
         */
        @Override
        protected boolean init() {
            return true;
        }

        /**
         * 创建新的日志记录器实例
         * 使用Java Util Logging的Logger.getLogger方法获取指定类名的日志记录器
         *
         * @param className 日志记录器关联的类名
         * @return java.util.logging.Logger实例
         */
        @Override
        protected Object newJournal(String className) {
            return java.util.logging.Logger.getLogger(className);
        }

        /**
         * 检查ERROR级别日志是否启用
         * 映射到java.util.logging的SEVERE级别
         *
         * @param logger 日志记录器对象
         * @return 如果启用返回true，否则返回false
         */
        @Override
        protected boolean isEnabledError(Object logger) {
            return ((java.util.logging.Logger) logger).isLoggable(java.util.logging.Level.SEVERE);
        }

        /**
         * 检查WARN级别日志是否启用
         * 映射到java.util.logging的WARNING级别
         *
         * @param logger 日志记录器对象
         * @return 如果启用返回true，否则返回false
         */
        @Override
        protected boolean isEnabledWarn(Object logger) {
            return ((java.util.logging.Logger) logger).isLoggable(java.util.logging.Level.WARNING);
        }

        /**
         * 检查INFO级别日志是否启用
         * 映射到java.util.logging的INFO级别
         *
         * @param logger 日志记录器对象
         * @return 如果启用返回true，否则返回false
         */
        @Override
        protected boolean isEnabledInfo(Object logger) {
            return ((java.util.logging.Logger) logger).isLoggable(java.util.logging.Level.INFO);
        }

        /**
         * 检查DEBUG级别日志是否启用
         * 映射到java.util.logging的CONFIG级别
         *
         * @param logger 日志记录器对象
         * @return 如果启用返回true，否则返回false
         */
        @Override
        protected boolean isEnabledDebug(Object logger) {
            return ((java.util.logging.Logger) logger).isLoggable(java.util.logging.Level.CONFIG);
        }

        /**
         * 检查TRACE级别日志是否启用
         * 映射到java.util.logging的FINE级别
         *
         * @param logger 日志记录器对象
         * @return 如果启用返回true，否则返回false
         */
        @Override
        protected boolean isEnabledTrace(Object logger) {
            return ((java.util.logging.Logger) logger).isLoggable(java.util.logging.Level.FINE);
        }

        /**
         * 输出ERROR级别日志
         * 使用logp方法记录日志，显式指定调用的类名和方法名
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素，包含类名和方法名
         * @param message               日志消息内容
         * @param throwable             异常对象，可为null
         */
        @Override
        protected void error(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) {
            ((java.util.logging.Logger) logger).logp(java.util.logging.Level.SEVERE, callStackTraceElement.getClassName(), callStackTraceElement.getMethodName(), message, throwable);
        }

        /**
         * 输出WARN级别日志
         * 使用logp方法记录日志，显式指定调用的类名和方法名
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素，包含类名和方法名
         * @param message               日志消息内容
         * @param throwable             异常对象，可为null
         */
        @Override
        protected void warn(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) {
            ((java.util.logging.Logger) logger).logp(java.util.logging.Level.WARNING, callStackTraceElement.getClassName(), callStackTraceElement.getMethodName(), message, throwable);
        }

        /**
         * 输出INFO级别日志
         * 使用logp方法记录日志，显式指定调用的类名和方法名
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素，包含类名和方法名
         * @param message               日志消息内容
         * @param throwable             异常对象，可为null
         */
        @Override
        protected void info(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) {
            ((java.util.logging.Logger) logger).logp(java.util.logging.Level.INFO, callStackTraceElement.getClassName(), callStackTraceElement.getMethodName(), message, throwable);
        }

        /**
         * 输出DEBUG级别日志
         * 使用logp方法记录日志，显式指定调用的类名和方法名
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素，包含类名和方法名
         * @param message               日志消息内容
         * @param throwable             异常对象，可为null
         */
        @Override
        protected void debug(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) {
            ((java.util.logging.Logger) logger).logp(java.util.logging.Level.CONFIG, callStackTraceElement.getClassName(), callStackTraceElement.getMethodName(), message, throwable);
        }

        /**
         * 输出TRACE级别日志
         * 使用logp方法记录日志，显式指定调用的类名和方法名
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素，包含类名和方法名
         * @param message               日志消息内容
         * @param throwable             异常对象，可为null
         */
        @Override
        protected void trace(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) {
            ((java.util.logging.Logger) logger).logp(java.util.logging.Level.FINE, callStackTraceElement.getClassName(), callStackTraceElement.getMethodName(), message, throwable);
        }
    }

    /**
     * 通用日志工厂实现，用于支持Log4j、Log4j2和Slf4j等日志框架
     * 通过反射机制动态加载和调用目标日志框架的API
     */
    @SuppressWarnings("DuplicatedCode")
    private static class CommonFactory extends Factory {
        /**
         * 日志工厂类和日志记录器类的全限定类名
         */
        private final String loggerFactoryClassName, loggerClassName;
        /**
         * 获取日志记录器实例的方法
         * 使用volatile保证初始化结果对其它线程可见
         */
        private volatile Method getLogger;
        /**
         * 各日志级别的检查方法，与Level枚举一一对应
         */
        private volatile LevelChecker traceLevel, debugLevel, infoLevel, warnLevel, errorLevel;
        /**
         * 各日志级别的输出方法，与Level枚举一一对应
         */
        private volatile MessageHandler traceLogger, debugLogger, infoLogger, warnLogger, errorLogger;

        /**
         * 构造函数，初始化通用日志工厂
         */
        private CommonFactory() {
            super("Console");
            this.loggerFactoryClassName = null;
            this.loggerClassName = null;
        }

        /**
         * 构造函数，初始化通用日志工厂
         *
         * @param name                   工厂名称
         * @param loggerFactoryClassName 日志工厂类的全限定类名
         * @param loggerClassName        日志记录器类的全限定类名
         */
        private CommonFactory(String name, String loggerFactoryClassName, String loggerClassName) {
            super(name);
            this.loggerFactoryClassName = loggerFactoryClassName;
            this.loggerClassName = loggerClassName;
        }

        /**
         * 初始化日志工厂，通过反射加载相关类和方法
         * 此方法会在首次使用日志工厂时被调用，用于动态加载目标日志框架的类和方法
         *
         * @return 初始化成功返回true，表示支持该日志框架
         * @throws Throwable 反射操作可能抛出的异常，如ClassNotFoundException、NoSuchMethodException等
         */
        @Override
        protected boolean init() throws Throwable {
            // 加载日志工厂类和日志记录器类
            Class<?> loggerFactoryClass = Class.forName(loggerFactoryClassName);
            Class<?> loggerClass = Class.forName(loggerClassName);
            // 获取获取日志记录器的方法，使用getMethod以便沿继承体系查找公开方法
            getLogger = loggerFactoryClass.getMethod("getLogger", String.class);
            Method traceLoggerMethod, debugLoggerMethod, infoLoggerMethod, warnLoggerMethod, errorLoggerMethod;
            // 根据工厂名称判断是否为Log4j框架
            if ("log4j".equalsIgnoreCase(name())) {
                // 加载Log4j相关的类
                Class<?> levelClass = Class.forName("org.apache.log4j.Level");
                Class<?> priorityClass = Class.forName("org.apache.log4j.Priority");
                Method isEnabledFor = loggerClass.getMethod("isEnabledFor", priorityClass);
                // 获取各个日志级别的常量对象
                // Log4j的TRACE级别在较早版本中不存在，因此需要进行异常处理
                Object traceObject;
                try {
                    traceObject = levelClass.getField("TRACE").get(null);
                } catch (Throwable e) {
                    // 如果TRACE级别不存在，则使用DEBUG级别作为替代
                    traceObject = levelClass.getField("DEBUG").get(null);
                }
                Object trace = traceObject;
                Object debug = levelClass.getField("DEBUG").get(null);
                Object info = levelClass.getField("INFO").get(null);
                Object warn = levelClass.getField("WARN").get(null);
                Object error = levelClass.getField("ERROR").get(null);
                // 初始化各日志级别的检查方法
                traceLevel = (logger) -> (boolean) isEnabledFor.invoke(logger, trace);
                debugLevel = (logger) -> (boolean) isEnabledFor.invoke(logger, debug);
                infoLevel = (logger) -> (boolean) isEnabledFor.invoke(logger, info);
                warnLevel = (logger) -> (boolean) isEnabledFor.invoke(logger, warn);
                errorLevel = (logger) -> (boolean) isEnabledFor.invoke(logger, error);
                // 初始化各日志级别的输出方法
                // 注意：此处两个trace方法获取代码相同，可能是为了容错处理
                try {
                    traceLoggerMethod = loggerClass.getMethod("trace", Object.class, Throwable.class);
                } catch (Throwable e) {
                    traceLoggerMethod = loggerClass.getMethod("debug", Object.class, Throwable.class);
                }
                debugLoggerMethod = loggerClass.getMethod("debug", Object.class, Throwable.class);
                infoLoggerMethod = loggerClass.getMethod("info", Object.class, Throwable.class);
                warnLoggerMethod = loggerClass.getMethod("warn", Object.class, Throwable.class);
                errorLoggerMethod = loggerClass.getMethod("error", Object.class, Throwable.class);
            } else {
                // 处理其他日志框架（如Log4j2、SLF4J等）
                // 初始化各日志级别的检查方法
                Method isTraceEnabled = loggerClass.getMethod("isTraceEnabled");
                Method isDebugEnabled = loggerClass.getMethod("isDebugEnabled");
                Method isInfoEnabled = loggerClass.getMethod("isInfoEnabled");
                Method isWarnEnabled = loggerClass.getMethod("isWarnEnabled");
                Method isErrorEnabled = loggerClass.getMethod("isErrorEnabled");
                traceLevel = (logger) -> (boolean) isTraceEnabled.invoke(logger);
                debugLevel = (logger) -> (boolean) isDebugEnabled.invoke(logger);
                infoLevel = (logger) -> (boolean) isInfoEnabled.invoke(logger);
                warnLevel = (logger) -> (boolean) isWarnEnabled.invoke(logger);
                errorLevel = (logger) -> (boolean) isErrorEnabled.invoke(logger);
                // 初始化各日志级别的输出方法
                traceLoggerMethod = loggerClass.getMethod("trace", String.class, Throwable.class);
                debugLoggerMethod = loggerClass.getMethod("debug", String.class, Throwable.class);
                infoLoggerMethod = loggerClass.getMethod("info", String.class, Throwable.class);
                warnLoggerMethod = loggerClass.getMethod("warn", String.class, Throwable.class);
                errorLoggerMethod = loggerClass.getMethod("error", String.class, Throwable.class);
            }
            // 将方法引用赋值给对应的函数式接口
            traceLogger = traceLoggerMethod::invoke;
            debugLogger = debugLoggerMethod::invoke;
            infoLogger = infoLoggerMethod::invoke;
            warnLogger = warnLoggerMethod::invoke;
            errorLogger = errorLoggerMethod::invoke;
            return true;
        }

        /**
         * 创建指定类名的日志记录器实例
         *
         * @param className 类名
         * @return 日志记录器实例
         * @throws Throwable 反射操作可能抛出的异常
         */
        @Override
        protected Object newJournal(String className) throws Throwable {
            return getLogger.invoke(null, className);
        }

        /**
         * 检查指定日志级别是否启用
         *
         * @param logger 日志记录器实例
         * @param level  日志级别
         * @return 启用返回true，否则返回false
         * @throws Throwable 反射操作可能抛出的异常
         */
        @Override
        protected boolean isEnabledFor(Object logger, Level level) throws Throwable {
            if (isConsole()) {
                return super.isEnabledFor(logger, level);
            } else {
                return switch (level) {
                    case TRACE -> traceLevel.test(logger);
                    case DEBUG -> debugLevel.test(logger);
                    case INFO -> infoLevel.test(logger);
                    case WARN -> warnLevel.test(logger);
                    case ERROR -> errorLevel.test(logger);
                };
            }
        }

        /**
         * 输出日志信息
         * 控制台实现或无级别时委托给默认实现，其余情况按级别调用对应的输出方法
         *
         * @param logger                日志记录器实例
         * @param level                 日志级别
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 反射操作可能抛出的异常
         */
        @Override
        protected void log(Object logger, Level level, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            if (isConsole() || level == null) {
                super.log(logger, level, callStackTraceElement, message, throwable);
            } else {
                switch (level) {
                    case TRACE -> traceLogger.accept(logger, message, throwable);
                    case DEBUG -> debugLogger.accept(logger, message, throwable);
                    case INFO -> infoLogger.accept(logger, message, throwable);
                    case WARN -> warnLogger.accept(logger, message, throwable);
                    case ERROR -> errorLogger.accept(logger, message, throwable);
                }
            }
        }
    }

    /**
     * 日志实例容器，封装了具体的日志实现对象
     * <p>
     * Journal类是日志系统的核心组件之一，负责管理特定类的日志实例。
     * 它持有工厂对象、底层日志框架的具体日志对象以及关联的类名，
     * 并提供了日志级别检查和日志输出的功能。
     *
     * @param factory   创建此日志实例的工厂对象
     * @param logger    底层日志框架的具体日志对象（如Log4j的Logger实例）
     * @param className 此日志实例关联的类名
     */
    private record Journal(Factory factory, Object logger, String className) {
        /**
         * 日志实例容器不可变，构造时无需额外校验
         */
        private Journal {
        }

        /**
         * 检查指定的日志级别是否启用
         * <p>
         * 该方法首先检查全局日志开关是否打开，如果关闭则直接返回false。
         * 然后委托给对应的日志工厂检查指定级别是否启用。
         * 如果在检查过程中发生异常，则打印异常堆栈并返回false。
         *
         * @param level 要检查的日志级别
         * @return 如果启用了该级别返回true，否则返回false
         */
        private boolean isEnabledFor(Level level) {
            // 检查全局日志开关是否打开，以及该级别是否低于全局最低级别
            if (!LOGGER_ENABLED.get() || level == null || level.severity() < minimumLevel.severity()) {
                return false;
            }
            try {
                // 委托给工厂检查指定级别是否启用
                return factory.isEnabledFor(logger, level);
            } catch (Throwable e) {
                // 发生异常时打印堆栈跟踪并返回false
                e.printStackTrace(System.err);
                return false;
            }
        }

        /**
         * 输出日志信息
         * <p>
         * 调用者栈帧、级别是否启用以及待输出的异常均已由{@link Logger#write(Level, Throwable, String, Object...)}判定，
         * 此处只负责处理消息参数、格式化日志消息、添加线程本地前缀，最后委托给工厂执行实际的日志输出。
         * <p>
         * 消息按{@link String#format(String, Object...)}的语义处理：{@code args}中的每个元素对应一个占位符，
         * 数组参数不会被隐式展开，需要逐项填充占位符时应直接传入各元素；{@code messageTemplate}为null时按空串处理。
         * 如果在处理过程中发生异常，则打印异常堆栈。
         *
         * @param callStackTraceElement 调用堆栈元素
         * @param level                 日志级别
         * @param throwable             异常对象，可为null
         * @param messageTemplate       消息模板
         * @param args                  消息参数，可为null
         */
        private void printLog(StackTraceElement callStackTraceElement, Level level, Throwable throwable, String messageTemplate, Object... args) {
            try {
                // 格式化日志消息，模板为空时按空串处理，避免String.format抛空指针
                String message = messageTemplate == null ? "" : messageTemplate;
                if (args != null && args.length != 0) {
                    message = String.format(message, args);
                }
                // 添加线程本地前缀（如果存在）
                String prefix = LOGGER_PREFIX.get();
                if (prefix != null && !prefix.isEmpty()) {
                    message = prefix + message;
                }
                // 委托给工厂执行实际的日志输出
                factory.log(logger, level, callStackTraceElement, message, throwable);
            } catch (Throwable e) {
                e.printStackTrace(System.err);
            }
        }

        /**
         * 返回此日志实例的字符串表示形式
         *
         * @return 包含类名的日志描述字符串
         */
        @Override
        public String toString() {
            return "Logger: " + className;
        }
    }

    /**
     * 日志工厂抽象类，定义了日志工厂的基本结构和行为
     * <p>
     * Factory类是日志系统的基础设施，负责创建和管理特定日志框架的适配器。
     * 每个具体的日志实现（如Console、JUL、Log4j、Log4j2、Slf4j等）都需要继承此类并按需覆写相应的方法。
     * 该类提供了日志实例的缓存机制、线程安全访问控制以及日志框架可用性检测等功能。
     */
    public static abstract class Factory {
        /**
         * 工厂名称，用于标识不同的日志实现
         */
        private final String name;
        /**
         * 日志实例缓存，键为调用者类名
         * 在读锁/写锁保护下访问，避免同一类名重复创建Journal对象
         */
        private final Map<String, Journal> cache = new LinkedHashMap<>();
        /**
         * 读写锁，保证多线程环境下对缓存的安全访问
         * 读锁用于查询缓存，写锁用于更新缓存
         */
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        /**
         * 标识是否为控制台日志实现
         * 控制台日志具有特殊的处理逻辑
         */
        private final boolean isConsole;
        /**
         * 标识工厂是否已完成初始化
         * 使用volatile确保多线程环境下的可见性
         */
        private volatile boolean isInit = false;
        /**
         * 标识当前日志框架是否受支持
         * 通过初始化过程检测日志框架的可用性
         */
        private volatile boolean isSupport = false;

        /**
         * 默认构造函数，创建名为"console"的工厂实例
         */
        protected Factory() {
            this("Console");
        }

        /**
         * 带参构造函数，创建指定名称的工厂实例
         *
         * @param name 工厂名称
         */
        protected Factory(String name) {
            this.name = name;
            this.isConsole = "Console".equalsIgnoreCase(name);
            if (this.isConsole) {
                this.isInit = true;
                this.isSupport = true;
            }
        }

        /**
         * 获取工厂名称
         *
         * @return 工厂名称
         */
        public final String name() {
            return name;
        }

        /**
         * 检查当前日志框架是否受支持
         * 首次调用时会触发初始化过程，初始化过程加锁保证只执行一次
         *
         * @return 如果受支持返回true，否则返回false
         */
        public final synchronized boolean isSupport() {
            if (!isInit) {
                try {
                    isSupport = init();
                } catch (Throwable e) {
                    System.err.printf("The '%s' logs are not supported due to:%n", name());
                    e.printStackTrace(System.err);
                } finally {
                    isInit = true;
                }
            }
            return isSupport;
        }

        /**
         * 判断当前工厂是否为控制台日志实现
         *
         * @return 如果是控制台日志实现返回true，否则返回false
         */
        public final boolean isConsole() {
            return isConsole;
        }

        /**
         * 初始化日志工厂并声明当前日志实现是否可用
         * 默认实现仅在控制台工厂下返回true，因此接入第三方日志框架的子类必须覆写此方法
         * （直接返回true，或通过反射探测依赖后返回探测结果），否则该工厂会被判定为不可用。
         *
         * @return 初始化成功且日志实现可用返回true，否则返回false
         * @throws Throwable 初始化过程中可能抛出的异常
         */
        protected boolean init() throws Throwable {
            return isConsole;
        }

        /**
         * 创建新的日志记录器实例
         * 对于控制台日志，这里简单地返回Logger类本身作为标记
         *
         * @param className 日志记录器关联的类名
         * @return Logger类对象
         * @throws Throwable 创建过程中可能抛出的异常
         */
        protected Object newJournal(String className) throws Throwable {
            return Logger.class;
        }

        /**
         * 检查指定级别的日志是否启用
         * 控制台日志在此处不做级别过滤，全局最低级别由{@link Journal#isEnabledFor(Level)}统一把关
         *
         * @param logger 日志记录器对象
         * @param level  日志级别
         * @return 控制台始终返回true，其他实现委托给各级别判断方法
         * @throws Throwable 反射操作可能抛出的异常
         */
        protected boolean isEnabledFor(Object logger, Level level) throws Throwable {
            if (isConsole || level == null) {
                return true;
            } else {
                return switch (level) {
                    case TRACE -> this.isEnabledTrace(logger);
                    case DEBUG -> this.isEnabledDebug(logger);
                    case INFO -> this.isEnabledInfo(logger);
                    case WARN -> this.isEnabledWarn(logger);
                    case ERROR -> this.isEnabledError(logger);
                };
            }
        }

        /**
         * 检查ERROR级别日志是否启用
         * 默认返回true，具体日志框架可通过重写此方法或{@link #isEnabledFor(Object, Level)}决定
         *
         * @param logger 日志记录器对象
         * @return 默认返回true，表示无法确定时按启用处理
         * @throws Throwable 检查过程中可能抛出的异常
         */
        protected boolean isEnabledError(Object logger) throws Throwable {
            return true;
        }

        /**
         * 检查WARN级别日志是否启用
         * 默认返回true，具体日志框架可通过重写此方法或{@link #isEnabledFor(Object, Level)}决定
         *
         * @param logger 日志记录器对象
         * @return 默认返回true，表示无法确定时按启用处理
         * @throws Throwable 检查过程中可能抛出的异常
         */
        protected boolean isEnabledWarn(Object logger) throws Throwable {
            return true;
        }

        /**
         * 检查INFO级别日志是否启用
         * 默认返回true，具体日志框架可通过重写此方法或{@link #isEnabledFor(Object, Level)}决定
         *
         * @param logger 日志记录器对象
         * @return 默认返回true，表示无法确定时按启用处理
         * @throws Throwable 检查过程中可能抛出的异常
         */
        protected boolean isEnabledInfo(Object logger) throws Throwable {
            return true;
        }

        /**
         * 检查DEBUG级别日志是否启用
         * 默认返回true，具体日志框架可通过重写此方法或{@link #isEnabledFor(Object, Level)}决定
         *
         * @param logger 日志记录器对象
         * @return 默认返回true，表示无法确定时按启用处理
         * @throws Throwable 检查过程中可能抛出的异常
         */
        protected boolean isEnabledDebug(Object logger) throws Throwable {
            return true;
        }

        /**
         * 检查TRACE级别日志是否启用
         * 默认返回true，具体日志框架可通过重写此方法或{@link #isEnabledFor(Object, Level)}决定
         *
         * @param logger 日志记录器对象
         * @return 默认返回true，表示无法确定时按启用处理
         * @throws Throwable 检查过程中可能抛出的异常
         */
        protected boolean isEnabledTrace(Object logger) throws Throwable {
            return true;
        }

        /**
         * 输出日志信息
         * 根据日志级别和工厂类型选择适当的输出方式；控制台实现或未指定级别时直接输出到控制台
         *
         * @param logger                日志记录器对象
         * @param level                 日志级别，为null时按控制台输出
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 输出过程中可能抛出的异常
         */
        protected void log(Object logger, Level level, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            if (isConsole || level == null) {
                this.console(logger, level, callStackTraceElement, message, throwable);
            } else {
                switch (level) {
                    case TRACE -> this.trace(logger, callStackTraceElement, message, throwable);
                    case DEBUG -> this.debug(logger, callStackTraceElement, message, throwable);
                    case INFO -> this.info(logger, callStackTraceElement, message, throwable);
                    case WARN -> this.warn(logger, callStackTraceElement, message, throwable);
                    case ERROR -> this.error(logger, callStackTraceElement, message, throwable);
                }
            }
        }

        /**
         * 输出ERROR级别日志
         * 默认实现直接输出到控制台，子类可重写此方法接入具体日志框架，
         * 且不会再回调{@link #log}，避免与{@link #log}的默认实现形成递归
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 输出过程中可能抛出的异常
         */
        protected void error(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            this.console(logger, Level.ERROR, callStackTraceElement, message, throwable);
        }

        /**
         * 输出WARN级别日志
         * 默认实现直接输出到控制台，子类可重写此方法接入具体日志框架
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 输出过程中可能抛出的异常
         */
        protected void warn(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            this.console(logger, Level.WARN, callStackTraceElement, message, throwable);
        }

        /**
         * 输出INFO级别日志
         * 默认实现直接输出到控制台，子类可重写此方法接入具体日志框架
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 输出过程中可能抛出的异常
         */
        protected void info(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            this.console(logger, Level.INFO, callStackTraceElement, message, throwable);
        }

        /**
         * 输出DEBUG级别日志
         * 默认实现直接输出到控制台，子类可重写此方法接入具体日志框架
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 输出过程中可能抛出的异常
         */
        protected void debug(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            this.console(logger, Level.DEBUG, callStackTraceElement, message, throwable);
        }

        /**
         * 输出TRACE级别日志
         * 默认实现直接输出到控制台，子类可重写此方法接入具体日志框架
         *
         * @param logger                日志记录器对象
         * @param callStackTraceElement 调用堆栈元素
         * @param message               日志消息
         * @param throwable             异常信息，可为null
         * @throws Throwable 输出过程中可能抛出的异常
         */
        protected void trace(Object logger, StackTraceElement callStackTraceElement, String message, Throwable throwable) throws Throwable {
            this.console(logger, Level.TRACE, callStackTraceElement, message, throwable);
        }

        /**
         * 执行实际的日志输出操作
         * 拼接日志消息并根据日志级别决定输出到System.out还是System.err，异常堆栈会输出到相同的流
         *
         * @param logger                日志记录器对象
         * @param level                 日志级别
         * @param callStackTraceElement 调用堆栈元素，包含类名、方法名和行号等信息
         * @param message               日志消息内容
         * @param throwable             异常对象，可为null
         */
        protected void console(Object logger, Level level, StackTraceElement callStackTraceElement, String message, Throwable throwable) {
            // 拼接日志内容，包含时间戳、日志级别、调用位置和消息内容
            StringBuilder builder = new StringBuilder(64 + (message == null ? 0 : message.length()));
            builder.append('[').append(TIME_FORMATTER.format(OffsetDateTime.now())).append("] [")
                    .append(level).append("] ").append(callStackTraceElement).append(": ").append(message);
            // 根据日志级别决定输出流，ERROR级别输出到错误流，其他级别输出到标准流
            if (level == Level.ERROR) {
                System.err.println(builder);
            } else {
                System.out.println(builder);
            }
            // 如果有异常信息，则输出异常堆栈跟踪
            if (throwable != null) {
                if (level == Level.ERROR) {
                    throwable.printStackTrace(System.err);
                } else {
                    throwable.printStackTrace(System.out);
                }
            }
        }

        /**
         * 获取或创建指定类名对应的日志实例
         * <p>
         * 使用读写锁实现双重检查：读锁下快速返回已缓存的实例，未命中时再在写锁内二次检查，
         * 避免并发场景下重复创建并覆盖缓存；创建失败时抛出{@link Error}。
         *
         * @param className 日志记录器关联的类名
         * @return 日志实例
         * @throws Error 日志实例创建失败（如工厂的newJournal返回null）时抛出
         */
        private Journal getOrCreateJournal(String className) {
            // 检查当前日志工厂是否支持
            if (!isSupport()) {
                throw new IllegalArgumentException(String.format("The '%s' logs are not supported due to:", name()));
            }
            Journal journal;
            // 尝试从缓存中获取现有的日志实例（使用读锁）
            try {
                lock.readLock().lock();
                journal = cache.get(className);
            } finally {
                lock.readLock().unlock();
            }
            if (journal != null) {
                return journal;
            }
            // 缓存中没有找到，则创建新的日志实例（使用写锁）
            try {
                lock.writeLock().lock();
                // 写锁内二次检查，避免并发场景下重复创建并覆盖缓存
                journal = cache.get(className);
                if (journal == null) {
                    Throwable cause = null;
                    try {
                        // 创建新的日志对象
                        Object journalObject = createJournal(className);
                        if (journalObject != null) {
                            // 封装成Journal对象
                            journal = new Journal(this, journalObject, className);
                        }
                    } catch (Throwable e) {
                        // 记录创建过程中出现的异常
                        cause = e;
                    }
                    // 如果成功创建了日志实例，则将其放入缓存
                    if (journal != null) {
                        cache.put(className, journal);
                    } else {
                        // 如果创建失败，则抛出错误
                        throw new Error("Error implement method 'createJournal',due to the return value being null", cause);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
            // 返回获取或创建的日志实例
            return journal;
        }

        /**
         * 根据类名创建日志对象
         *
         * @param className 类名
         * @return 日志对象
         * @throws Throwable 创建过程中可能出现的异常
         */
        private Object createJournal(String className) throws Throwable {
            if (isConsole()) {
                // 控制台日志直接返回Logger类对象
                return Logger.class;
            } else {
                // 其他日志框架调用具体的newJournal方法创建日志对象
                return newJournal(className);
            }
        }
    }

    /**
     * 日志等级枚举
     * <p>
     * 枚举按严重程度从低到高排列，并通过{@link #severity()}提供可比较的权重。
     */
    public enum Level {
        /**
         * 跟踪级别 - 最详细的日志信息，通常只在开发阶段使用
         */
        TRACE(0),
        /**
         * 调试级别 - 用于调试应用程序的详细信息
         */
        DEBUG(1),
        /**
         * 信息级别 - 一般性的运行状态信息
         */
        INFO(2),
        /**
         * 警告级别 - 潜在的问题，但不影响程序继续运行
         */
        WARN(3),
        /**
         * 错误级别 - 错误事件，可能导致功能异常
         */
        ERROR(4);
        /**
         * 级别权重，数值越大越严重，用于比较日志级别阈值
         */
        private final int severity;

        /**
         * 构造指定权重的日志级别
         *
         * @param severity 级别权重，数值越大越严重
         */
        Level(int severity) {
            this.severity = severity;
        }

        /**
         * 获取级别权重
         *
         * @return 级别权重，数值越大越严重
         */
        public int severity() {
            return severity;
        }
    }

    /**
     * 日志级别检查器函数式接口
     * 用于检查指定日志记录器是否启用了特定日志级别
     */
    @FunctionalInterface
    private interface LevelChecker {
        /**
         * 检查日志记录器是否启用对应级别
         *
         * @param logger 日志记录器对象
         * @return 如果启用该级别返回true，否则返回false
         * @throws Exception 检查过程中可能抛出的异常
         */
        boolean test(Object logger) throws Exception;
    }

    /**
     * 消息处理器函数式接口
     * 用于将日志消息委托给具体的日志框架输出
     */
    @FunctionalInterface
    private interface MessageHandler {
        /**
         * 输出一条日志消息
         *
         * @param logger    日志记录器对象
         * @param message   日志消息内容
         * @param throwable 可能存在的异常对象
         * @throws Exception 处理过程中可能抛出的异常
         */
        void accept(Object logger, String message, Throwable throwable) throws Exception;
    }
}
