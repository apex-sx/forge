/*
 * Copyright (c) 2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Terraqo
 */
class ElementLookaheadLoaderTest {

    private ElementLookaheadLoader<String> loader;

    @BeforeEach
    void setUp() {
        loader = new ElementLookaheadLoaderByIterator<>(List.of("A", "B", "C", "D", "E"));
    }

    @Nested
    @DisplayName("基本功能测试")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("测试 hasNext 方法")
        void testHasNext() {
            assertTrue(loader.hasNext(), "初始状态应有下一个元素");

            // 消费所有元素
            while (loader.hasNext()) {
                loader.next();
            }

            assertFalse(loader.hasNext(), "所有元素消费完后不应有下一个元素");
        }

        @Test
        @DisplayName("测试 next 方法")
        void testNext() {
            assertEquals("A", loader.next(), "第一个元素应该是 A");
            assertEquals("B", loader.next(), "第二个元素应该是 B");
            assertEquals("C", loader.next(), "第三个元素应该是 C");
        }

        @Test
        @DisplayName("测试 next 方法抛出异常")
        void testNextThrowsExceptionWhenEmpty() {
            // 消费所有元素
            while (loader.hasNext()) {
                loader.next();
            }

            assertThrows(NoSuchElementException.class, () -> {
                loader.next();
            }, "当没有更多元素时 next 应该抛出异常");
        }
    }

    @Nested
    @DisplayName("前瞻功能测试")
    class LookaheadFunctionalityTests {

        @Test
        @DisplayName("测试 retrieveElement 不消费元素")
        void testRetrieveElementWithoutConsume() {
            // 前瞻查看元素但不消费
            assertEquals("A", loader.retrieveElement(0, false));
            assertEquals("B", loader.retrieveElement(1, false));
            assertEquals("C", loader.retrieveElement(2, false));

            // 使用 next 获取元素，验证元素未被消费
            assertEquals("A", loader.next());
            assertEquals("B", loader.next());
            assertEquals("C", loader.next());
        }

        @Test
        @DisplayName("测试 retrieveElement 消费元素")
        void testRetrieveElementWithConsume() {
            // 消费第1个元素（索引为0）
            assertEquals("A", loader.retrieveElement(0, true));

            // 验证元素已被消费
            assertEquals("B", loader.next());
        }

        @Test
        @DisplayName("测试 retrieveElement 消费多个元素")
        void testRetrieveElementConsumeMultiple() {
            // 消费到索引2的元素（A, B, C）
            assertEquals("C", loader.retrieveElement(2, true));

            // 下一个元素应该是 D
            assertEquals("D", loader.next());
        }

        @Test
        @DisplayName("测试 retrieveElement 负偏移量异常")
        void testRetrieveElementNegativeOffset() {
            assertThrows(IllegalArgumentException.class, () -> {
                loader.retrieveElement(-1, false);
            });
        }

        @Test
        @DisplayName("测试 retrieveElement 超出范围返回结束标识符")
        void testRetrieveElementOutOfRange() {
            // 创建只有一个元素的加载器
            ElementLookaheadLoader<String> singleElementLoader = new ElementLookaheadLoaderByIterator<>(List.of("X"));

            // 尝试获取超出范围的元素
            assertNull(singleElementLoader.retrieveElement(5, false));
        }
    }

    @Nested
    @DisplayName("预览功能测试")
    class PeekFunctionalityTests {

        @Test
        @DisplayName("测试 peekElement 方法")
        void testPeekElement() {
            assertEquals("A", loader.peekElement());
            assertEquals("A", loader.peekElement()); // 再次调用应该返回相同值
            assertEquals("A", loader.next()); // 消费后验证确实是A
        }

        @Test
        @DisplayName("测试 peekElementNext 方法")
        void testPeekElementNext() {
            assertEquals("B", loader.peekElementNext());
            assertEquals("A", loader.next()); // 消费第一个元素
            assertEquals("C", loader.peekElementNext()); // 下一个元素仍是B
        }

        @Test
        @DisplayName("测试 peekElementAt 方法")
        void testPeekElementAt() {
            assertEquals("A", loader.peekElementAt(0));
            assertEquals("B", loader.peekElementAt(1));
            assertEquals("C", loader.peekElementAt(2));
        }
    }

    @Nested
    @DisplayName("弹出功能测试")
    class PopFunctionalityTests {

        @Test
        @DisplayName("测试 popElement 方法")
        void testPopElement() {
            assertEquals("A", loader.popElement());
            assertEquals("B", loader.next()); // 下一个应该是B
        }

        @Test
        @DisplayName("测试 popElementNext 方法")
        void testPopElementNext() {
            assertEquals("B", loader.popElementNext());
            assertEquals("C", loader.next()); // 第一个元素未被消费，仍应是C
            assertEquals("D", loader.next()); // 第二个元素被跳过，应该是D
        }

        @Test
        @DisplayName("测试 popElementAt 方法")
        void testPopElementAt() {
            assertEquals("C", loader.popElementAt(2)); // 消费索引为2的元素C
            assertEquals("D", loader.next()); // 第一个元素
            assertEquals("E", loader.next()); // 第二个元素
        }
    }

    @Nested
    @DisplayName("空和结束状态测试")
    class EmptyAndEndStateTests {

        @Test
        @DisplayName("测试 isEmpty 方法")
        void testIsEmpty() {
            assertFalse(loader.isEmpty(), "初始状态不应该为空");

            // 消费所有元素
            while (loader.hasNext()) {
                loader.next();
            }

            assertTrue(loader.isEmpty(), "所有元素消费后应该为空");
        }

        @Test
        @DisplayName("测试 isEOF 方法")
        void testIsEOF() {
            assertFalse(loader.isEOF(), "初始状态不应该到达末尾");

            // 消费所有元素
            while (loader.hasNext()) {
                loader.next();
            }

            assertTrue(loader.isEOF(), "所有元素消费后应该到达末尾");
        }
    }

    @Nested
    @DisplayName("边界情况测试")
    class EdgeCaseTests {

        @Test
        @DisplayName("测试空数据源")
        void testEmptyDataSource() {
            ElementLookaheadLoader<String> emptyLoader =
                    new ElementLookaheadLoaderByIterator<>(new ArrayList<>());

            assertFalse(emptyLoader.hasNext());
            assertTrue(emptyLoader.isEmpty());
            assertTrue(emptyLoader.isEOF());
            assertNull(emptyLoader.endIndicator());
        }

        @Test
        @DisplayName("测试单元素数据源")
        void testSingleElementDataSource() {
            ElementLookaheadLoader<String> singleLoader =
                    new ElementLookaheadLoaderByIterator<>(List.of("SINGLE"));

            assertTrue(singleLoader.hasNext());
            assertEquals("SINGLE", singleLoader.next());
            assertFalse(singleLoader.hasNext());
        }

        @Test
        @DisplayName("测试重复调用 retrieveElement")
        void testRepeatedRetrieveElement() {
            // 多次调用 retrieveElement 应该返回相同的值（当不消费时）
            assertEquals("A", loader.retrieveElement(0, false));
            assertEquals("A", loader.retrieveElement(0, false));
            assertEquals("A", loader.retrieveElement(0, false));

            // 验证实际元素未被消费
            assertEquals("A", loader.next());
        }
    }
}
