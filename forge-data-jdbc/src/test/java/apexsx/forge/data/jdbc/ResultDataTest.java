package apexsx.forge.data.jdbc;



import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ResultDataTest {
    
    private ResultMap resultMap = new ResultMap();

    
    // 测试基本构造和初始化
    @Test
    public void testInitialization() {
        assertEquals(0, resultMap.size());
        assertTrue(resultMap.isEmpty());
    }
    
    // 测试 put 和 get 方法
    @Test
    public void testPutAndGet() {
        resultMap.put("key", "value");
        assertEquals("value", resultMap.get("key"));
        assertEquals("value", resultMap.get("KEY")); // 大小写不敏感
        assertEquals("value", resultMap.get("Key")); // 大小写不敏感
        assertEquals(1, resultMap.size());
    }
    
    // 测试大小写不敏感的键替换
    @Test
    public void testCaseInsensitiveKeyReplacement() {
        resultMap.put("KeyName", "originalValue");
        resultMap.put("keyname", "updatedValue");
        
        assertEquals("updatedValue", resultMap.get("keyname"));
        assertEquals("updatedValue", resultMap.get("KeyName"));
        assertEquals("updatedValue", resultMap.get("KEYNAME"));
        assertEquals(1, resultMap.size()); // 确保没有创建新条目
    }
    
    // 测试 containsKey 方法
    @Test
    public void testContainsKey() {
        resultMap.put("TestKey", "value");
        assertTrue(resultMap.containsKey("TestKey"));
        assertTrue(resultMap.containsKey("testkey")); // 大小写不敏感
        assertFalse(resultMap.containsKey("NonExistentKey"));
    }
    
    // 测试 containsValue 方法
    @Test
    public void testContainsValue() {
        resultMap.put("key", "testValue");
        assertTrue(resultMap.containsValue("testValue"));
        assertFalse(resultMap.containsValue("nonExistentValue"));
    }
    
    // 测试 remove 方法
    @Test
    public void testRemove() {
        resultMap.put("Key", "value");
        Object removed = resultMap.remove("key"); // 大小写不敏感删除
        
        assertEquals("value", removed);
        assertEquals(0, resultMap.size());
        assertNull(resultMap.get("Key"));
    }
    
    // 测试 clear 方法
    @Test
    public void testClear() {
        resultMap.put("key1", "value1");
        resultMap.put("key2", "value2");
        
        resultMap.clear();
        
        assertEquals(0, resultMap.size());
        assertTrue(resultMap.isEmpty());
    }
    
    // 测试 keySet, values, entrySet 视图
    @Test
    public void testCollectionView() {
        resultMap.put("Key1", "Value1");
        resultMap.put("Key2", "Value2");
        
        Set<String> keys = resultMap.keySet();
        Collection<Object> values = resultMap.values();
        Set<Map.Entry<String, Object>> entries = resultMap.entrySet();
        
        assertEquals(2, keys.size());
        assertEquals(2, values.size());
        assertEquals(2, entries.size());
        
        assertTrue(keys.contains("Key1"));
        assertTrue(keys.contains("Key2"));
    }
    
    // 测试 putAll 方法
    @Test
    public void testPutAll() {
        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("key1", "value1");
        sourceMap.put("key2", "value2");
        
        resultMap.putAll(sourceMap);
        
        assertEquals("value1", resultMap.get("KEY1"));
        assertEquals("value2", resultMap.get("KEY2"));
        assertEquals(2, resultMap.size());
    }
    
    // 测试 getOrDefault 方法
    @Test
    public void testGetOrDefault() {
        resultMap.put("ExistingKey", "existingValue");
        
        assertEquals("existingValue", resultMap.getOrDefault("existingkey", "default"));
        assertEquals("defaultValue", resultMap.getOrDefault("NonExistentKey", "defaultValue"));
    }
    
    // 测试 putIfAbsent 方法
    @Test
    public void testPutIfAbsent() {
        // 测试添加不存在的键
        assertNull(resultMap.putIfAbsent("newKey", "newValue"));
        assertEquals("newValue", resultMap.get("NewKey"));
        
        // 测试添加已存在的键（大小写不敏感）
        assertNotNull(resultMap.putIfAbsent("NEWKEY", "anotherValue"));
        assertEquals("newValue", resultMap.get("newKey")); // 值不应该改变
    }
    
    // 测试 remove(key, value) 方法
    @Test
    public void testConditionalRemove() {
        resultMap.put("Key", "Value");
        
        // 测试条件匹配删除
        assertTrue(resultMap.remove("key", "Value")); // 大小写不敏感
        assertEquals(0, resultMap.size());
        
        // 重新添加
        resultMap.put("Key", "Value");
        
        // 测试条件不匹配
        assertFalse(resultMap.remove("key", "WrongValue"));
        assertEquals(1, resultMap.size());
    }
    
    // 测试 replace(key, oldValue, newValue) 方法
    @Test
    public void testReplace() {
        resultMap.put("Key", "OldValue");
        
        // 测试正确旧值替换
        assertTrue(resultMap.replace("key", "OldValue", "NewValue")); // 大小写不敏感
        assertEquals("NewValue", resultMap.get("Key"));
        
        // 测试错误旧值
        assertFalse(resultMap.replace("key", "WrongValue", "AnotherValue"));
        assertEquals("NewValue", resultMap.get("Key")); // 值不应改变
    }
    
    // 测试 replaceAll 方法
    @Test
    public void testReplaceAll() {
        resultMap.put("Key1", "Value1");
        resultMap.put("Key2", "Value2");
        
        resultMap.replaceAll((key, value) -> value.toString() + "_modified");
        
        assertEquals("Value1_modified", resultMap.get("key1"));
        assertEquals("Value2_modified", resultMap.get("key2"));
        assertEquals(2, resultMap.size());
    }
    
    // 测试 computeIfAbsent 方法
    @Test
    public void testComputeIfAbsent() {
        // 测试键不存在的情况
        String result = (String) resultMap.computeIfAbsent("newKey", k -> "computed_" + k);
        assertEquals("computed_newKey", result);
        assertEquals("computed_newKey", resultMap.get("NewKey"));
        
        // 测试键已存在的情况
        String result2 = (String) resultMap.computeIfAbsent("NEWKEY", k -> "another_value");
        assertEquals("computed_newKey", result2); // 应该返回原始值而不是计算新值
    }
    
    // 测试 computeIfPresent 方法
    @Test
    public void testComputeIfPresent() {
        resultMap.put("Key", "OriginalValue");
        
        // 测试键存在的情况
        String result = (String) resultMap.computeIfPresent("key", (k, v) -> v + "_updated");
        assertEquals("OriginalValue_updated", result);
        assertEquals("OriginalValue_updated", resultMap.get("Key"));
        
        // 测试键不存在的情况
        resultMap.remove("Key");
        String result2 = (String) resultMap.computeIfPresent("key", (k, v) -> v + "_updated");
        assertNull(result2);
        assertFalse(resultMap.containsKey("Key"));
    }
    
    // 测试 compute 方法
    @Test
    public void testCompute() {
        // 测试键不存在的情况
        String result = (String) resultMap.compute("newKey", (k, v) -> "computed");
        assertEquals("computed", result);
        assertEquals("computed", resultMap.get("NewKey"));
        
        // 测试键存在且返回非空值的情况
        String result2 = (String) resultMap.compute("NEWKEY", (k, v) -> v + "_updated");
        assertEquals("computed_updated", result2);
        assertEquals("computed_updated", resultMap.get("NewKey"));
        
        // 测试返回null导致删除条目
        String result3 = (String) resultMap.compute("NewKey", (k, v) -> null);
        assertNull(result3);
        assertFalse(resultMap.containsKey("NewKey"));
    }
    
    // 测试 merge 方法
    @Test
    public void testMerge() {
        // 测试键不存在的情况
        String result = (String) resultMap.merge("newKey", "value", (v1, v2) -> v1 + "_" + v2);
        assertEquals("value", result);
        assertEquals("value", resultMap.get("NewKey"));
        
        // 测试键存在的情况
        String result2 = (String) resultMap.merge("NEWKEY", "merged", (v1, v2) -> v1 + "_" + v2);
        assertEquals("value_merged", result2);
        assertEquals("value_merged", resultMap.get("NewKey"));
        
        // 测试合并函数返回null导致删除条目
        String result3 = (String) resultMap.merge("NewKey", "ignored", (v1, v2) -> null);
        assertNull(result3);
        assertFalse(resultMap.containsKey("NewKey"));
    }
    
    // 测试 clone 方法
    @Test
    public void testClone() throws CloneNotSupportedException {
        resultMap.put("Key1", "Value1");
        resultMap.put("Key2", "Value2");
        
        ResultMap cloned = (ResultMap) resultMap.clone();
        
        assertEquals(resultMap.size(), cloned.size());
        assertEquals(resultMap.get("key1"), cloned.get("KEY1"));
        assertEquals(resultMap.get("key2"), cloned.get("KEY2"));
        
        // 验证是深拷贝
        cloned.put("Key3", "Value3");
        assertNotEquals(resultMap.size(), cloned.size());
        assertTrue(resultMap.containsKey("Key1"));
        assertFalse(resultMap.containsKey("Key3"));
    }
    
    // 测试插入顺序保持
    @Test
    public void testInsertionOrder() {
        resultMap.put("First", "1st");
        resultMap.put("Second", "2nd");
        resultMap.put("Third", "3rd");
        
        Iterator<String> keyIterator = resultMap.keySet().iterator();
        assertEquals("First", keyIterator.next());
        assertEquals("Second", keyIterator.next());
        assertEquals("Third", keyIterator.next());
    }
}
