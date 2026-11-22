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

import java.util.List;
import java.util.ServiceConfigurationError;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Extension} 单元测试
 * <p>
 * 通过{@code src/test/resources/META-INF/services}下的SPI声明验证扩展的扫描、排序、查询、
 * 元数据解析与实例化策略。测试类必须是public，否则嵌套的public实现类无法被ServiceLoader实例化。
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("Extension SPI扩展注册中心")
public class ExtensionTest {
    /**
     * 每个用例执行前后清空全局缓存，避免用例之间以及与其他测试类之间互相影响
     */
    @BeforeEach
    @AfterEach
    void clearCache() {
        Extension.clearCache();
    }

    @Nested
    @DisplayName("加载与排序")
    class LoadAllTest {
        @Test
        @DisplayName("按order升序加载全部扩展")
        void loadAllSortsByOrder() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            List<String> names = registry.loadAll().stream().map(DemoService::name).toList();
            assertEquals(List.of("plain", "first", "second", "third", "blank-classify", "fifth", "another-fifth"), names);
        }

        @Test
        @DisplayName("order相同时保持SPI声明顺序")
        void sameOrderKeepsDeclarationOrder() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            List<String> names = registry.loadAll().stream().map(DemoService::name).toList();
            assertEquals(List.of("fifth", "another-fifth"), names.subList(names.size() - 2, names.size()));
        }

        @Test
        @DisplayName("同一SPI接口复用缓存的注册器")
        void ofReturnsCachedRegistry() {
            assertSame(Extension.of(DemoService.class), Extension.of(DemoService.class));
        }

        @Test
        @DisplayName("清除指定接口的缓存后重新扫描")
        void clearCacheByInterface() {
            Extension<DemoService> before = Extension.of(DemoService.class);
            Extension.clearCache(DemoService.class);
            Extension<DemoService> after = Extension.of(DemoService.class);
            assertNotSame(before, after);
            assertEquals(before.loadAll().size(), after.loadAll().size());
        }
    }

    @Nested
    @DisplayName("按id与类型查询")
    class QueryTest {
        @Test
        @DisplayName("按id命中扩展")
        void loadById() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertInstanceOf(FirstService.class, registry.loadById("first").orElseThrow());
        }

        @Test
        @DisplayName("未命中或id为null时返回Optional.empty()")
        void loadByIdMiss() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertTrue(registry.loadById("missing").isEmpty());
            assertTrue(registry.loadById(null).isEmpty());
        }

        @Test
        @DisplayName("按实现类Class命中扩展")
        void loadByType() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertInstanceOf(SecondService.class, registry.loadByType(SecondService.class).orElseThrow());
        }

        @Test
        @DisplayName("未命中或类型为null时返回Optional.empty()")
        void loadByTypeMiss() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertTrue(registry.loadByType(String.class).isEmpty());
            assertTrue(registry.loadByType(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("按分类查询")
    class ClassifyTest {
        @Test
        @DisplayName("按分类加载并保持order顺序")
        void loadByClassify() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            List<String> names = registry.loadByClassify("default").stream().map(DemoService::name).toList();
            assertEquals(List.of("plain", "blank-classify"), names);
        }

        @Test
        @DisplayName("空白分类归入default")
        void blankClassifyFallsBackToDefault() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertTrue(registry.loadByClassify(" ").isEmpty());
            assertEquals(List.of("plain", "blank-classify"),
                    registry.loadByClassify("default").stream().map(DemoService::name).toList());
        }

        @Test
        @DisplayName("附加过滤器时只返回满足条件的扩展")
        void loadByClassifyWithPredicate() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            List<String> names = registry.loadByClassify("alpha", entry -> entry.order() >= 2)
                    .stream().map(DemoService::name).toList();
            assertEquals(List.of("second"), names);
        }

        @Test
        @DisplayName("过滤器为null时等同于不过滤")
        void nullPredicateMeansNoFilter() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertEquals(2, registry.loadByClassify("alpha", null).size());
        }

        @Test
        @DisplayName("未命中分类或分类为null时返回空集合")
        void loadByClassifyMiss() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertTrue(registry.loadByClassify("missing").isEmpty());
            assertTrue(registry.loadByClassify(null).isEmpty());
            assertTrue(registry.loadByClassify(null, entry -> true).isEmpty());
        }
    }

    @Nested
    @DisplayName("元数据解析")
    class MetadataTest {
        @Test
        @DisplayName("未标注Tag时使用默认元数据")
        void defaultMetadataWithoutTag() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            assertEquals(1, registry.loadByClassify("default", entry -> entry.type() == PlainService.class
                    && entry.id() == null
                    && "default".equals(entry.classify())
                    && entry.order() == 0
                    && entry.singleton()).size());
        }

        @Test
        @DisplayName("标注Tag时按注解解析元数据")
        void metadataFromTag() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            DemoService service = registry.loadByClassify("beta", entry -> "third".equals(entry.id())
                            && "beta".equals(entry.classify())
                            && entry.order() == 3
                            && !entry.singleton())
                    .stream().findFirst().orElseThrow();
            assertInstanceOf(ThirdService.class, service);
        }
    }

    @Nested
    @DisplayName("实例化策略")
    class SingletonTest {
        @Test
        @DisplayName("单例模式多次加载返回同一实例")
        void singletonReturnsSameInstance() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            DemoService first = registry.loadById("first").orElseThrow();
            assertSame(first, registry.loadById("first").orElseThrow());
            assertSame(first, registry.loadByType(FirstService.class).orElseThrow());
        }

        @Test
        @DisplayName("多例模式每次加载返回新实例")
        void prototypeReturnsNewInstance() {
            Extension<DemoService> registry = Extension.of(DemoService.class);
            DemoService first = registry.loadById("third").orElseThrow();
            DemoService second = registry.loadById("third").orElseThrow();
            assertNotSame(first, second);
        }
    }

    @Nested
    @DisplayName("配置校验")
    class ValidationTest {
        @Test
        @DisplayName("SPI接口为null时抛出NullPointerException")
        void nullInterface() {
            assertThrows(NullPointerException.class, () -> Extension.of(null));
        }

        @Test
        @DisplayName("id重复时抛出IllegalStateException")
        void duplicateId() {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> Extension.of(DuplicateIdService.class));
            assertTrue(exception.getMessage().contains("Duplicate extension id"));
        }

        @Test
        @DisplayName("id为空白时抛出IllegalStateException")
        void blankId() {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> Extension.of(BlankIdService.class));
            assertTrue(exception.getMessage().contains("can not be blank"));
        }

        @Test
        @DisplayName("实现类缺少public无参构造时加载失败")
        void missingPublicNoArgConstructor() {
            ServiceConfigurationError error = assertThrows(ServiceConfigurationError.class, () -> Extension.of(NoCtorService.class));
            assertTrue(error.getMessage().contains("no-arg constructor"));
        }

        @Test
        @DisplayName("实现类构造失败时由ServiceLoader抛出ServiceConfigurationError")
        void constructorFailure() {
            Extension<BrokenCtorService> registry = Extension.of(BrokenCtorService.class);
            ServiceConfigurationError error = assertThrows(ServiceConfigurationError.class, () -> registry.loadAll());
            assertTrue(error.getMessage().contains("could not be instantiated"));
        }
    }

    /**
     * 无注解实现，order=0、classify=default、单例
     */
    public static class PlainService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public PlainService() {
        }

        @Override
        public String name() {
            return "plain";
        }
    }

    /**
     * alpha分类、order=1的单例实现
     */
    @Extension.Tag(id = "first", classify = "alpha", order = 1)
    public static class FirstService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public FirstService() {
        }

        @Override
        public String name() {
            return "first";
        }
    }

    /**
     * alpha分类、order=2的单例实现
     */
    @Extension.Tag(id = "second", classify = "alpha", order = 2)
    public static class SecondService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public SecondService() {
        }

        @Override
        public String name() {
            return "second";
        }
    }

    /**
     * beta分类、order=3的多例实现
     */
    @Extension.Tag(id = "third", classify = "beta", order = 3, singleton = false)
    public static class ThirdService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public ThirdService() {
        }

        @Override
        public String name() {
            return "third";
        }
    }

    /**
     * 分类为空白字符串，应归入default
     */
    @Extension.Tag(id = "blank-classify", classify = "  ", order = 4)
    public static class BlankClassifyService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public BlankClassifyService() {
        }

        @Override
        public String name() {
            return "blank-classify";
        }
    }

    /**
     * beta分类、order=5的实现，声明顺序在{@link AnotherFifthService}之前
     */
    @Extension.Tag(id = "fifth", classify = "beta", order = 5)
    public static class FifthService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public FifthService() {
        }

        @Override
        public String name() {
            return "fifth";
        }
    }

    /**
     * beta分类、order=5的实现，与{@link FifthService}的order相同，用于验证稳定排序
     */
    @Extension.Tag(id = "another-fifth", classify = "beta", order = 5)
    public static class AnotherFifthService implements DemoService {
        /**
         * SPI要求public无参构造
         */
        public AnotherFifthService() {
        }

        @Override
        public String name() {
            return "another-fifth";
        }
    }

    /**
     * 与{@link SameIdTwo}使用同一个id
     */
    @Extension.Tag(id = "dup")
    public static class SameIdOne implements DuplicateIdService {
        /**
         * SPI要求public无参构造
         */
        public SameIdOne() {
        }
    }

    /**
     * 与{@link SameIdOne}使用同一个id
     */
    @Extension.Tag(id = "dup")
    public static class SameIdTwo implements DuplicateIdService {
        /**
         * SPI要求public无参构造
         */
        public SameIdTwo() {
        }
    }

    /**
     * id为空白字符串的实现
     */
    @Extension.Tag(id = "  ")
    public static class BlankIdImpl implements BlankIdService {
        /**
         * SPI要求public无参构造
         */
        public BlankIdImpl() {
        }
    }

    /**
     * 只有private无参构造的实现，ServiceLoader无法实例化
     */
    @Extension.Tag(id = "no-ctor")
    public static class NoCtorImpl implements NoCtorService {
        private NoCtorImpl() {
        }
    }

    /**
     * 构造器抛出异常的实现，用于验证ServiceLoader的实例化异常语义
     */
    @Extension.Tag(id = "broken-ctor")
    public static class BrokenCtorImpl implements BrokenCtorService {
        /**
         * 故意抛出异常，模拟实现类实例化失败
         */
        public BrokenCtorImpl() {
            throw new IllegalStateException("boom-ctor");
        }
    }

    /**
     * 常规测试用SPI接口
     */
    public interface DemoService {
        /**
         * 扩展名称
         *
         * @return 名称
         */
        String name();
    }

    /**
     * id重复校验用SPI接口
     */
    public interface DuplicateIdService {
    }

    /**
     * 空白id校验用SPI接口
     */
    public interface BlankIdService {
    }

    /**
     * 缺少public无参构造校验用SPI接口
     */
    public interface NoCtorService {
    }

    /**
     * 构造失败校验用SPI接口
     */
    public interface BrokenCtorService {
    }
}
