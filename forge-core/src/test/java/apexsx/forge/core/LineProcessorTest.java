/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LineProcessor 与 RemarkFilter 的单元测试
 */
@DisplayName("LineProcessor 单元测试")
class LineProcessorTest {

    // ========== LineProcessor 基础功能测试 ==========

    @Test
    @DisplayName("基础功能：读取单行")
    void testSingleLine() {
        String content = "Hello World";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello World", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("基础功能：读取多行")
    void testMultipleLines() {
        String content = "Line 1\nLine 2\nLine 3";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(3, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    @Test
    @DisplayName("基础功能：空行处理")
    void testEmptyLines() {
        String content = "Line 1\n\nLine 3\n\n";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(4, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("", lines.get(1));
        assertEquals("Line 3", lines.get(2));
        assertEquals("", lines.get(3));
    }

    @Test
    @DisplayName("基础功能：不同换行符")
    void testDifferentLineEndings() {
        String content = "Line 1\nLine 2\rLine 3\r\nLine 4";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(4, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
        assertEquals("Line 3", lines.get(2));
        assertEquals("Line 4", lines.get(3));
    }

    @Test
    @DisplayName("基础功能：空输入")
    void testEmptyInput() {
        String content = "";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertFalse(processor.hasNext());
            assertThrows(NoSuchElementException.class, processor::next);
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    // ========== 续行合并测试 ==========

    @Test
    @DisplayName("续行合并：单行续行")
    void testLineContinuation() {
        String content = "Hello \\\nWorld";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello World", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("续行合并：多行续行")
    void testMultipleLineContinuation() {
        String content = "Line 1 \\\nLine 2 \\\nLine 3";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Line 1 Line 2 Line 3", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("续行合并：续行跳过行首空白")
    void testLineContinuationSkipWhitespace() {
        String content = "Hello \\\n    World";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello World", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("续行合并：偶数个反斜杠不续行")
    void testDoubleBackslashNoContinuation() {
        String content = "Hello\\\\\nWorld";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Hello\\", lines.get(0));
        assertEquals("World", lines.get(1));
    }

    @Test
    @DisplayName("续行合并：续行后跟空行")
    void testLineContinuationFollowedByEmptyLine() {
        String content = "Hello \\\n\nWorld";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Hello ", lines.get(0));
        assertEquals("World", lines.get(1));
    }

    // ========== 转义解码测试 ==========

    @Test
    @DisplayName("转义解码：制表符")
    void testEscapeTab() {
        String content = "Hello\\tWorld";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello\tWorld", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：换行符")
    void testEscapeNewline() {
        String content = "Hello\\nWorld";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello\nWorld", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：回车符")
    void testEscapeCarriageReturn() {
        String content = "Hello\\rWorld";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello\rWorld", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：反斜杠")
    void testEscapeBackslash() {
        String content = "Hello\\\\World";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello\\World", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：Unicode 转义")
    void testEscapeUnicode() {
        String content = "Hello\\u0057World";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            String next = processor.next();
            assertEquals("HelloWWorld", next);
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：不完整的 Unicode 转义")
    void testIncompleteUnicodeEscape() {
        String content = "Hello\\u00World";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello\\u00World", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：未知的转义序列")
    void testUnknownEscapeSequence() {
        String content = "Hello\\xWorld";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello\\xWorld", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：行末单个反斜杠")
    void testTrailingBackslash() {
        String content = "Hello\\";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertEquals("Hello", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("转义解码：混合转义")
    void testMixedEscapes() {
        String content = "\\t\\n\\r\\\\Hello\\u0020World";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            String next = processor.next();
            assertEquals("\t\n\r\\Hello World", next);
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    // ========== 构造函数参数校验测试 ==========

    @Test
    @DisplayName("参数校验：null Reader")
    void testNullReader() {
        assertThrows(NullPointerException.class, () -> {
            try (LineProcessor processor = new LineProcessor(null, 256, 4096)) {
            }
        });
    }

    @Test
    @DisplayName("参数校验：无效的 initialCapacity")
    void testInvalidInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            try (LineProcessor processor = new LineProcessor(new StringReader(""), 0, 4096)) {
            }
        });
        assertThrows(IllegalArgumentException.class, () -> {
            try (LineProcessor processor = new LineProcessor(new StringReader(""), -1, 4096)) {
            }
        });
    }

    @Test
    @DisplayName("参数校验：无效的 maxCapacity")
    void testInvalidMaxCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            try (LineProcessor processor = new LineProcessor(new StringReader(""), 256, 0)) {
            }
        });
        assertThrows(IllegalArgumentException.class, () -> {
            try (LineProcessor processor = new LineProcessor(new StringReader(""), 256, -1)) {
            }
        });
    }

    @Test
    @DisplayName("参数校验：maxCapacity 小于 initialCapacity")
    void testMaxCapacityLessThanInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            try (LineProcessor processor = new LineProcessor(new StringReader(""), 512, 256)) {
            }
        });
    }

    // ========== close 方法测试 ==========

    @Test
    @DisplayName("关闭功能：关闭后 hasNext 返回 false")
    void testCloseHasNext() throws IOException {
        String content = "Line 1\nLine 2";
        LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096);
        assertTrue(processor.hasNext());
        processor.close();
        assertFalse(processor.hasNext());
        assertTrue(processor.closed());
    }

    @Test
    @DisplayName("关闭功能：重复关闭")
    void testMultipleClose() throws IOException {
        String content = "Line 1";
        LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096);
        processor.close();
        assertDoesNotThrow(processor::close);
        assertTrue(processor.closed());
    }

    // ========== createRemarkFilter 工厂方法测试 ==========

    @Test
    @DisplayName("RemarkFilter：使用 Reader 创建")
    void testCreateRemarkFilterWithReader() {
        String content = "Line 1\n# Comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：使用 InputStream 创建")
    void testCreateRemarkFilterWithInputStream() {
        String content = "Line 1\n# Comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：使用自定义容量创建")
    void testCreateRemarkFilterWithCapacity() {
        String content = "Line 1\n# Comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
    }

    // ========== RemarkFilter 单行注释测试 ==========

    @Test
    @DisplayName("RemarkFilter：过滤整行单行注释")
    void testRemarkFilterWholeLineComment() {
        String content = "Line 1\n# This is a comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：过滤行首带空白的单行注释")
    void testRemarkFilterCommentWithLeadingWhitespace() {
        String content = "Line 1\n   # This is a comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：去除行尾注释")
    void testRemarkFilterKeepHashInContent() {
        String content = "Key=Value#Comment";
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            assertTrue(processor.hasNext());
            assertEquals("Key=Value", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    // ========== RemarkFilter 块注释测试 ==========

    @Test
    @DisplayName("RemarkFilter：过滤单行块注释")
    void testRemarkFilterSingleLineBlockComment() {
        String content = "Line 1\n/* This is a block comment */\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：过滤行内块注释")
    void testRemarkFilterInlineBlockComment() {
        String content = "Key /* comment */ = Value";
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            assertTrue(processor.hasNext());
            assertEquals("Key  = Value", processor.next());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("RemarkFilter：过滤跨行块注释")
    void testRemarkFilterMultiLineBlockComment() {
        String content = "Line 1\n/* This is\na multi-line\ncomment */\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：未闭合的块注释")
    void testRemarkFilterUnclosedBlockComment() {
        String content = "Line 1\n/* This is\nan unclosed\ncomment";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(1, lines.size());
        assertEquals("Line 1", lines.get(0));
    }

    @Test
    @DisplayName("RemarkFilter：多个块注释")
    void testRemarkFilterMultipleBlockComments() {
        String content = "/* comment1 */ Line 1 /* comment2 */\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals(" Line 1 ", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    // ========== createCustomRemarkFilter 自定义注释标记测试 ==========

    @Test
    @DisplayName("CustomRemarkFilter：自定义单行注释前缀")
    void testCustomLineCommentPrefix() {
        String content = "Line 1\n// This is a comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createCustomRemarkFilter(
                new StringReader(content), "//", "/*", "*/")) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("CustomRemarkFilter：自定义块注释标记")
    void testCustomBlockCommentMarkers() {
        String content = "Line 1\n<!-- This is a comment -->\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createCustomRemarkFilter(
                new StringReader(content), "#", "<!--", "-->")) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("CustomRemarkFilter：null 标记使用默认值")
    void testCustomRemarkFilterNullMarkers() {
        String content = "Line 1\n# Comment\n/* Block */\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createCustomRemarkFilter(
                new StringReader(content), null, null, null)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("CustomRemarkFilter：空白标记使用默认值")
    void testCustomRemarkFilterBlankMarkers() {
        String content = "Line 1\n# Comment\n/* Block */\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createCustomRemarkFilter(
                new StringReader(content), "  ", "  ", "  ")) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    // ========== RemarkFilter 综合场景测试 ==========

    @Test
    @DisplayName("RemarkFilter：单行注释与块注释混合")
    void testRemarkFilterMixedComments() {
        String content = """
                Line 1
                # Single line comment
                Line 2 /* inline block */
                /* Multi-line
                   block comment */
                Line 3
                """;
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(3, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2 ", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    @Test
    @DisplayName("RemarkFilter：块注释内的单行注释符号")
    void testRemarkFilterLineCommentInBlockComment() {
        String content = "Line 1\n/* # This is not a comment */\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    @Test
    @DisplayName("RemarkFilter：续行与注释混合")
    void testRemarkFilterContinuationWithComments() {
        String content = "Hello \\\n# Comment\nWorld";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(1, lines.size());
        assertEquals("Hello World", lines.get(0));
    }

    @Test
    @DisplayName("RemarkFilter：转义字符与注释混合")
    void testRemarkFilterEscapeWithComments() {
        String content = "Hello\\tWorld /* comment */\n# comment\nLine 2";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = LineProcessor.createRemarkFilter(new StringReader(content))) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Hello\tWorld ", lines.get(0));
        assertEquals("Line 2", lines.get(1));
    }

    // ========== 缓冲区管理测试 ==========

    @Test
    @DisplayName("缓冲区管理：超过最大容量时重建")
    void testBufferRebuildOnExceedingMaxCapacity() {
        StringBuilder longLine = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            longLine.append('a');
        }
        String content = longLine.toString();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 1024)) {
            assertTrue(processor.hasNext());
            String result = processor.next();
            assertEquals(5000, result.length());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("缓冲区管理：使用 BufferedReader")
    void testWithBufferedReader() {
        String content = "Line 1\nLine 2";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(bufferedReader, 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
    }

    // ========== 边界条件测试 ==========

    @Test
    @DisplayName("边界条件：只有空白字符的行")
    void testWhitespaceOnlyLines() {
        String content = "   \n\t\t\nLine 1";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(3, lines.size());
        assertEquals("   ", lines.get(0));
        assertEquals("\t\t", lines.get(1));
        assertEquals("Line 1", lines.get(2));
    }

    @Test
    @DisplayName("边界条件：特殊字符")
    void testSpecialCharacters() {
        String content = "Hello 世界\nこんにちは";
        List<String> lines = new ArrayList<>();
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            while (processor.hasNext()) {
                lines.add(processor.next());
            }
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
        assertEquals(2, lines.size());
        assertEquals("Hello 世界", lines.get(0));
        assertEquals("こんにちは", lines.get(1));
    }

    @Test
    @DisplayName("边界条件：连续调用 hasNext")
    void testConsecutiveHasNextCalls() {
        String content = "Line 1";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertTrue(processor.hasNext());
            assertTrue(processor.hasNext());
            assertTrue(processor.hasNext());
            assertEquals("Line 1", processor.next());
            assertFalse(processor.hasNext());
            assertFalse(processor.hasNext());
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }

    @Test
    @DisplayName("边界条件：连续调用 next")
    void testConsecutiveNextCalls() {
        String content = "Line 1\nLine 2";
        try (LineProcessor processor = new LineProcessor(new StringReader(content), 256, 4096)) {
            assertEquals("Line 1", processor.next());
            assertEquals("Line 2", processor.next());
            assertThrows(NoSuchElementException.class, processor::next);
        } catch (IOException e) {
            fail("不应该抛出异常", e);
        }
    }
}