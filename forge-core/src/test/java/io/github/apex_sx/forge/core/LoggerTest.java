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

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Logger}的单元测试
 * <p>
 * 覆盖日志ID归一化、调用者归属、级别短路、工厂默认实现回退以及全局日志器切换等行为。
 *
 * @author ApexSX
 */
class LoggerTest {
    /**
     * 测试用日志ID，大小写不敏感
     */
    private static final String RECORDER = "recorder";
    /**
     * 记录输出内容的日志工厂创建的日志记录，格式为"级别|类名#方法名|消息|异常"
     */
    private final List<String> records = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp() {
        Logger.openLogger();
        Logger.clearPrefix();
        Logger.minimumLevel(Logger.Level.TRACE);
        Logger.global("console");
        records.clear();
    }

    @AfterEach
    void tearDown() {
        Logger.openLogger();
        Logger.clearPrefix();
        Logger.minimumLevel(Logger.Level.TRACE);
        Logger.global("console");
    }

    /**
     * 捕获标准错误流中的输出
     *
     * @param action 待执行的操作
     * @return 捕获到的输出内容
     */
    private String captureError(Runnable action) {
        PrintStream original = System.err;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setErr(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setErr(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    /**
     * 记录型日志工厂，把输出内容收集到{@link #records}而不真正写控制台
     */
    private final class RecordingFactory extends Logger.Factory {
        /**
         * 是否启用TRACE级别
         */
        private final boolean traceEnabled;

        private RecordingFactory(boolean traceEnabled) {
            super("Recorder");
            this.traceEnabled = traceEnabled;
        }

        @Override
        protected boolean init() {
            return true;
        }

        @Override
        protected Object newJournal(String className) {
            return className;
        }

        @Override
        protected boolean isEnabledFor(Object logger, Logger.Level level) {
            return level != Logger.Level.TRACE || traceEnabled;
        }

        @Override
        protected void log(Object logger, Logger.Level level, StackTraceElement element, String message, Throwable throwable) {
            records.add(level + "|" + element.getClassName() + "#" + element.getMethodName() + "|" + message + "|" + throwable);
        }
    }

    @Nested
    @DisplayName("日志ID解析")
    class OfTest {
        @Test
        @DisplayName("日志ID统一归一化为小写")
        void shouldNormalizeIdToLowerCase() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            assertEquals(RECORDER, Logger.of("RECORDER").toString());
        }

        @Test
        @DisplayName("大小写不同的日志ID指向同一日志实现")
        void shouldResolveIdIgnoreCase() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            assertEquals(Logger.of(RECORDER), Logger.of("Recorder"));
        }

        @Test
        @DisplayName("日志ID为null时返回默认日志实例")
        void shouldFallbackToDefaultLogger() {
            assertEquals("console", Logger.of(null).toString());
        }

        @Test
        @DisplayName("日志ID不存在时抛出异常")
        void shouldThrowWhenIdMissing() {
            assertThrows(IllegalArgumentException.class, () -> Logger.of("missing-id"));
        }

        @Test
        @DisplayName("日志工厂不可用时抛出异常")
        void shouldThrowWhenFactoryUnsupported() {
            Logger.addFactory("unsupported", new Logger.Factory("Unsupported") {
                @Override
                protected boolean init() {
                    return false;
                }
            });
            assertThrows(IllegalArgumentException.class, () -> Logger.of("unsupported"));
        }

        @Test
        @DisplayName("内置JUL日志工厂的ID为jul")
        void shouldRegisterJulFactory() {
            assertEquals("jul", Logger.of("JUL").toString());
            assertTrue(Logger.global("jul"));
        }
    }

    @Nested
    @DisplayName("调用者归属")
    class CallerTest {
        @Test
        @DisplayName("实例方法输出日志归属到真正的调用者")
        void shouldAttributeInstanceCallToCaller() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.of(RECORDER).logInfo("hello %s", "world");
            assertEquals(1, records.size());
            assertTrue(records.getFirst().contains("LoggerTest$CallerTest#shouldAttributeInstanceCallToCaller"), records.getFirst());
            assertTrue(records.getFirst().contains("hello world"), records.getFirst());
        }

        @Test
        @DisplayName("静态方法输出日志归属到真正的调用者")
        void shouldAttributeStaticCallToCaller() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.global(RECORDER);
            Logger.info("static");
            assertEquals(1, records.size());
            assertTrue(records.getFirst().contains("LoggerTest$CallerTest#shouldAttributeStaticCallToCaller"), records.getFirst());
        }

        @Test
        @DisplayName("同一日志实例被不同类使用时按调用者重新绑定")
        void shouldRebindWhenCallerChanges() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.global(RECORDER);
            Logger.info("from test");
            Delegate.log();
            assertEquals(2, records.size());
            assertTrue(records.getFirst().contains("LoggerTest$CallerTest#shouldRebindWhenCallerChanges"), records.getFirst());
            assertTrue(records.get(1).contains("LoggerTest$Delegate#log"), records.get(1));
        }
    }

    @Nested
    @DisplayName("级别过滤")
    class LevelTest {
        @Test
        @DisplayName("级别未启用时跳过消息格式化")
        void shouldSkipFormattingWhenLevelDisabled() {
            Logger.addFactory(RECORDER, new RecordingFactory(false));
            AtomicInteger formatted = new AtomicInteger();
            Object arg = new Object() {
                @Override
                public String toString() {
                    formatted.incrementAndGet();
                    return "formatted";
                }
            };
            Logger.of(RECORDER).logTrace("value=%s", arg);
            assertEquals(0, formatted.get());
            assertTrue(records.isEmpty());
        }

        @Test
        @DisplayName("级别启用时完成消息格式化")
        void shouldFormatWhenLevelEnabled() {
            Logger.addFactory(RECORDER, new RecordingFactory(false));
            AtomicInteger formatted = new AtomicInteger();
            Object arg = new Object() {
                @Override
                public String toString() {
                    formatted.incrementAndGet();
                    return "formatted";
                }
            };
            Logger.of(RECORDER).logInfo("value=%s", arg);
            assertEquals(1, formatted.get());
            assertEquals(1, records.size());
        }

        @Test
        @DisplayName("全局开关关闭时不输出日志")
        void shouldSuppressWhenLoggerClosed() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.closeLogger();
            Logger.of(RECORDER).logError("closed");
            assertTrue(records.isEmpty());
        }
    }

    @Nested
    @DisplayName("工厂默认实现")
    class FactoryDefaultTest {
        @Test
        @DisplayName("只覆写级别判断时回退到控制台且不递归")
        void shouldFallbackToConsoleWithoutRecursion() {
            Logger.addFactory("fallback", new Logger.Factory("Fallback") {
                @Override
                protected boolean init() {
                    return true;
                }

                @Override
                protected boolean isEnabledFor(Object logger, Logger.Level level) {
                    return true;
                }
            });
            String output = captureError(() -> Logger.of("fallback").logError("boom %s", "now"));
            assertTrue(output.contains("[ERROR]"), output);
            assertTrue(output.contains("boom now"), output);
            assertFalse(output.contains("StackOverflowError"), output);
        }

        @Test
        @DisplayName("日志实例创建失败时抛出错误")
        void shouldThrowWhenJournalNotCreated() {
            Logger.addFactory("empty-journal", new Logger.Factory("EmptyJournal") {
                @Override
                protected boolean init() {
                    return true;
                }

                @Override
                protected Object newJournal(String className) {
                    return null;
                }
            });
            assertThrows(Error.class, () -> Logger.of("empty-journal").logInfo("x"));
        }
    }

    @Nested
    @DisplayName("全局日志器")
    class GlobalTest {
        @Test
        @DisplayName("切换全局日志器后静态方法走新的日志实现")
        void shouldRouteStaticApiToGlobalLogger() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            assertTrue(Logger.global(RECORDER));
            Logger.warn("via global");
            assertEquals(1, records.size());
            assertTrue(records.getFirst().startsWith("WARN|"), records.getFirst());
        }

        @Test
        @DisplayName("切换到未知日志ID时返回false")
        void shouldRejectUnknownGlobalId() {
            assertFalse(Logger.global("missing-id"));
            assertFalse(Logger.global(null));
        }

        @Test
        @DisplayName("级别判断走与输出相同的日志实现")
        void shouldReportEnabledStateOfGlobalLogger() {
            Logger.addFactory(RECORDER, new RecordingFactory(false));
            Logger.global(RECORDER);
            assertTrue(Logger.isEnabledInfo());
            assertFalse(Logger.isEnabledTrace());
        }
    }

    @Nested
    @DisplayName("线程日志前缀")
    class PrefixTest {
        @Test
        @DisplayName("输出日志时拼接线程本地前缀")
        void shouldPrependThreadLocalPrefix() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.addPrefix("[req-1] ");
            Logger.of(RECORDER).logInfo("body");
            assertTrue(records.getFirst().contains("[req-1] body"), records.getFirst());
        }
    }

    @Nested
    @DisplayName("全局最低级别")
    class MinimumLevelTest {
        @Test
        @DisplayName("默认最低级别为TRACE")
        void shouldDefaultToTrace() {
            assertEquals(Logger.Level.TRACE, Logger.minimumLevel());
        }

        @Test
        @DisplayName("低于阈值的日志不输出")
        void shouldSuppressBelowThreshold() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.global(RECORDER);
            Logger.minimumLevel(Logger.Level.WARN);
            Logger logger = Logger.global();
            logger.logDebug("debug");
            logger.logInfo("info");
            logger.logWarn("warn");
            assertEquals(1, records.size());
            assertTrue(records.getFirst().startsWith("WARN|"), records.getFirst());
            assertFalse(logger.isEnabled(Logger.Level.INFO));
            assertTrue(logger.isEnabled(Logger.Level.WARN));
        }

        @Test
        @DisplayName("设置null时保持原有阈值")
        void shouldIgnoreNullLevel() {
            Logger.minimumLevel(Logger.Level.ERROR);
            Logger.minimumLevel(null);
            assertEquals(Logger.Level.ERROR, Logger.minimumLevel());
        }

        @Test
        @DisplayName("阈值不影响异常抛出")
        void shouldNotAffectThrowing() {
            Logger.minimumLevel(Logger.Level.ERROR);
            IllegalStateException cause = new IllegalStateException("boom");
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> Logger.throwIfRuntimeException(true, Logger.Level.TRACE, cause, "fail"));
            assertEquals("fail", exception.getMessage());
            assertSame(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("条件抛异常")
    class ThrowIfTest {
        @Test
        @DisplayName("条件成立时抛出Error并保留格式化消息与原异常")
        void shouldThrowError() {
            IllegalStateException cause = new IllegalStateException("boom");
            Error error = assertThrows(Error.class,
                    () -> Logger.throwIfError(true, Logger.Level.ERROR, cause, "fail %s", "now"));
            assertEquals("fail now", error.getMessage());
            assertSame(cause, error.getCause());
        }

        @Test
        @DisplayName("条件成立时抛出受检异常")
        void shouldThrowCheckedException() {
            Exception exception = assertThrows(Exception.class,
                    () -> Logger.throwIfException(true, Logger.Level.ERROR, null, "fail"));
            assertEquals("fail", exception.getMessage());
        }

        @Test
        @DisplayName("条件成立时抛出运行时异常")
        void shouldThrowRuntimeException() {
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> Logger.throwIfRuntimeException(true, Logger.Level.WARN, null, "fail %d", 1));
            assertEquals("fail 1", exception.getMessage());
        }

        @Test
        @DisplayName("条件不成立时按指定级别记录日志并返回null")
        void shouldLogAndReturnNull() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.global(RECORDER);
            IllegalStateException cause = new IllegalStateException("boom");
            Object result = Logger.throwIfRuntimeException(false, Logger.Level.WARN, cause, "fail %s", "now");
            assertNull(result);
            assertEquals(1, records.size());
            assertTrue(records.getFirst().startsWith("WARN|"), records.getFirst());
            assertTrue(records.getFirst().contains("fail now"), records.getFirst());
            assertTrue(records.getFirst().contains("boom"), records.getFirst());
        }

        @Test
        @DisplayName("级别为null时按ERROR记录")
        void shouldTreatNullLevelAsError() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.global(RECORDER);
            Logger.throwIfError(false, null, null, "no level");
            assertEquals(1, records.size());
            assertTrue(records.getFirst().startsWith("ERROR|"), records.getFirst());
        }
    }

    @Nested
    @DisplayName("消息参数")
    class ArgumentTest {
        @Test
        @DisplayName("最后一个参数为异常时自动作为异常输出")
        void shouldExtractTrailingThrowable() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            IllegalStateException cause = new IllegalStateException("boom");
            Logger.of(RECORDER).logError("failed %s", "now", cause);
            assertEquals(1, records.size());
            assertTrue(records.getFirst().contains("failed now"), records.getFirst());
            assertTrue(records.getFirst().contains("boom"), records.getFirst());
        }

        @Test
        @DisplayName("数组参数不会被隐式展开")
        void shouldNotUnwrapNestedArray() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.of(RECORDER).logInfo("%s", (Object) new Object[]{"a", "b"});
            assertEquals(1, records.size());
            assertFalse(records.getFirst().contains("a b"), records.getFirst());
            assertTrue(records.getFirst().contains("[Ljava.lang.Object;"), records.getFirst());
        }

        @Test
        @DisplayName("消息模板为null时按空串输出")
        void shouldTreatNullTemplateAsEmpty() {
            Logger.addFactory(RECORDER, new RecordingFactory(true));
            Logger.of(RECORDER).logInfo(null, "ignored");
            assertEquals(1, records.size());
            assertTrue(records.getFirst().endsWith("||null"), records.getFirst());
        }
    }

    /**
     * 模拟另一个类使用全局日志器
     */
    private static class Delegate {
        /**
         * 输出一条日志，用于验证调用者重绑定
         */
        private static void log() {
            Logger.info("from delegate");
        }
    }
}
