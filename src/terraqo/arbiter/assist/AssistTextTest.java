package terraqo.arbiter.assist;

import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class AssistTextTest {

    @Test
    void testRepeat() {
        // 测试场景1: repeat <= 0，container为null
        String result1 = AssistText.repeat(null, "test", 0, null, null, null, null, null);
        assertEquals("", result1, "repeat为0时应返回空字符串");

        String result2 = AssistText.repeat(null, "test", -1, null, null, null, null, null);
        assertEquals("", result2, "repeat为负数时应返回空字符串");

        // 测试场景2: repeat <= 0，container不为null
        StringWriter writer1 = new StringWriter();
        String result3 = AssistText.repeat(writer1, "test", 0, null, null, null, null, null);
        assertNull(result3, "repeat为0且container不为null时应返回null");
        assertEquals("", writer1.toString(), "repeat为0时container应为空");

        // 测试场景3: 基本重复，无额外参数
        String result4 = AssistText.repeat(null, "a", 3, null, null, null, null, null);
        assertEquals("aaa", result4, "基本重复应正确拼接");

        // 测试场景4: 带分隔符的重复
        String result5 = AssistText.repeat(null, "a", 3, ",", null, null, null, null);
        assertEquals("a,a,a", result5, "带分隔符的重复应正确拼接");

        // 测试场景5: 带元素前缀的重复
        String result6 = AssistText.repeat(null, "a", 2, null, "[", null, null, null);
        assertEquals("[a[a", result6, "带元素前缀的重复应正确拼接");

        // 测试场景6: 带元素后缀的重复
        String result7 = AssistText.repeat(null, "a", 2, null, null, "]", null, null);
        assertEquals("a]a]", result7, "带元素后缀的重复应正确拼接");

        // 测试场景7: 带整体前缀的重复
        String result8 = AssistText.repeat(null, "a", 2, null, null, null, "(", null);
        assertEquals("(a]a", result8, "带整体前缀的重复应正确拼接");

        // 测试场景8: 带整体后缀的重复
        String result9 = AssistText.repeat(null, "a", 2, null, null, null, null, ")");
        assertEquals("aa)", result9, "带整体后缀的重复应正确拼接");

        // 测试场景9: 所有参数都有的情况
        String result10 = AssistText.repeat(null, "x", 3, "|", "<", ">", "(", ")");
        assertEquals("(<x>|<x>|<x>)", result10, "所有参数都存在时应正确拼接");

        // 测试场景10: repeat=1时的情况
        String result11 = AssistText.repeat(null, "a", 1, ",", "[", "]", "(", ")");
        assertEquals("([a])", result11, "repeat为1时不应添加分隔符");

        // 测试场景11: 使用非String类型的输入
        String result12 = AssistText.repeat(null, 123, 2, "-", null, null, null, null);
        assertEquals("123-123", result12, "数字类型输入应转换为字符串");

        // 测试场景12: container不为null时写入内容但不返回
        StringWriter writer2 = new StringWriter();
        String result13 = AssistText.repeat(writer2, "test", 2, ",", "[", "]", "(", ")");
        assertNull(result13, "container不为null时应返回null");
        assertEquals("([test],[test])", writer2.toString(), "container不为null时应写入内容");

        // 测试场景13: 复杂Appendable对象测试
        StringBuilder builder = new StringBuilder();
        String result14 = AssistText.repeat(builder, "hello", 2, " ", null, null, null, null);
        assertNull(result14, "使用StringBuilder时应返回null");
        assertEquals("hello hello", builder.toString(), "StringBuilder应包含正确的内容");

        // 测试场景14: 空字符串作为输入
        String result15 = AssistText.repeat(null, "", 3, ",", null, null, null, null);
        assertEquals(",,", result15, "空字符串重复时应只保留分隔符");

        // 测试场景15: 只有prefix和suffix
        String result16 = AssistText.repeat(null, "test", 1, null, null, null, "START-", "-END");
        assertEquals("START-test-END", result16, "只有前后缀时应正确添加");
    }
}
