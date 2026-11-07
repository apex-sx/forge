package apexsx.forge.lang.meta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClassMetadata 测试")
class ClassMetadataTest {

    private TestClass testInstance;
    private ClassMetadata cheetah;

    // 测试用的内部类
    static class TestClass {
        public static String STATIC_FIELD = "staticValue";
        private static int staticInt = 42;

        public String publicField = "public";
        protected String protectedField = "protected";
        private String privateField = "private";
        String packageField = "package";

        public static void staticMethod() {
            // 静态方法
        }

        public void publicMethod() {
            // 公共方法
        }

        protected void protectedMethod() {
            // 受保护方法
        }

        private void privateMethod() {
            // 私有方法
        }

        void packageMethod() {
            // 包私有方法
        }

        public void methodWithParams(String param1, int param2) {
            // 带参数的方法
        }

        public TestClass() {
            // 默认构造器
        }

        public TestClass(String param) {
            // 带参数构造器
        }

        private TestClass(int param) {
            // 私有构造器
        }
    }

    @BeforeEach
    void setUp() {
        testInstance = new TestClass();
        cheetah = ClassMetadata.of(TestClass.class);
    }

    @Test
    @DisplayName("测试获取类元数据实例")
    void testOfClasscheetah() {
        ClassMetadata cheetah1 = ClassMetadata.of(TestClass.class);
        ClassMetadata cheetah2 = ClassMetadata.of(TestClass.class);

        // 验证缓存机制：相同类应该返回同一个实例
        assertSame(cheetah1, cheetah2, "相同类的元数据应该被缓存");

        // 验证不同类返回不同实例
        ClassMetadata stringcheetah = ClassMetadata.of(String.class);
        assertNotSame(cheetah1, stringcheetah, "不同类的元数据应该是不同实例");
    }

    @Test
    @DisplayName("测试字段唯一键生成")
    void testGetFieldUniqueKey() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("publicField");
        String key = ClassMetadata.getFieldUniqueKey(field);
        assertEquals("publicField", key, "字段唯一键应该是字段名");
    }

    @Test
    @DisplayName("测试方法唯一键生成")
    void testGetMethodUniqueKey() throws NoSuchMethodException {
        Method method = TestClass.class.getDeclaredMethod("methodWithParams", String.class, int.class);
        String key = ClassMetadata.getMethodUniqueKey(method);
        System.out.println(key);
        assertEquals("methodWithParams(java.lang.String,int)", key, "方法唯一键格式应该正确");
    }

    @Test
    @DisplayName("测试构造器唯一键生成")
    void testGetConstructorUniqueKey() throws NoSuchMethodException {
        Constructor<TestClass> constructor = TestClass.class.getDeclaredConstructor(String.class);
        String key = ClassMetadata.getConstructorUniqueKey(constructor);
        System.out.println(key);
        assertEquals("terraqo.cheetah.ClassMetadataTest$TestClass(java.lang.String)", key,
                "构造器唯一键格式应该正确");
    }

    @Test
    @DisplayName("测试基本唯一键生成")
    void testGetUniqueKey() {
        // 无参数情况
        String key1 = ClassMetadata.getUniqueKey("testMethod", null);
        assertEquals("testMethod", key1, "无参数时应该直接返回方法名");

        String key2 = ClassMetadata.getUniqueKey("testMethod", new Class[0]);
        assertEquals("testMethod", key2, "空参数数组时应该直接返回方法名");

        // 有参数情况
        String key3 = ClassMetadata.getUniqueKey("testMethod", new Class[]{String.class, Integer.class});
        assertEquals("testMethod(java.lang.String,java.lang.Integer)", key3, "有参数时格式应该正确");
    }

    @Test
    @DisplayName("测试字段存在性检查")
    void testContainsField() {
        assertTrue(cheetah.containsField("publicField"), "应该包含公共字段");
        assertTrue(cheetah.containsField("protectedField"), "应该包含受保护字段");
        assertTrue(cheetah.containsField("privateField"), "应该包含私有字段");
        assertTrue(cheetah.containsField("packageField"), "应该包含包私有字段");
        assertTrue(cheetah.containsField("STATIC_FIELD"), "应该包含静态字段");
        assertFalse(cheetah.containsField("nonExistentField"), "不应该包含不存在的字段");
    }

    @Test
    @DisplayName("测试方法存在性检查")
    void testContainsMethod() {
        assertTrue(cheetah.containsMethod("publicMethod"), "应该包含公共方法");
        assertTrue(cheetah.containsMethod("protectedMethod"), "应该包含受保护方法");
        assertTrue(cheetah.containsMethod("privateMethod"), "应该包含私有方法");
        assertTrue(cheetah.containsMethod("packageMethod"), "应该包含包私有方法");
        assertTrue(cheetah.containsMethod("staticMethod"), "应该包含静态方法");
        assertTrue(cheetah.containsMethod("methodWithParams", String.class, int.class), "应该包含带参数的方法");
        assertFalse(cheetah.containsMethod("nonExistentMethod"), "不应该包含不存在的方法");
        assertFalse(cheetah.containsMethod("methodWithParams", String.class), "参数不匹配时不应该包含方法");
    }

    @Test
    @DisplayName("测试构造器存在性检查")
    void testContainsConstructor() {
        assertTrue(cheetah.containsConstructor(), "应该包含默认构造器");
        assertTrue(cheetah.containsConstructor(String.class), "应该包含带String参数的构造器");
        assertTrue(cheetah.containsConstructor(int.class), "应该包含带int参数的构造器");
        assertFalse(cheetah.containsConstructor(Double.class), "不应该包含带Double参数的构造器");
    }

    @Test
    @DisplayName("测试获取字段对象")
    void testGetField() throws NoSuchFieldException {
        Field publicField = cheetah.getField("publicField");
        assertEquals("publicField", publicField.getName(), "应该正确获取公共字段");

        Field privateField = cheetah.getField("privateField");
        assertEquals("privateField", privateField.getName(), "应该正确获取私有字段");

        Field staticField = cheetah.getField("STATIC_FIELD");
        assertEquals("STATIC_FIELD", staticField.getName(), "应该正确获取静态字段");

        assertThrows(NoSuchFieldException.class, () -> cheetah.getField("nonExistentField"),
                "获取不存在的字段应该抛出异常");
    }

    @Test
    @DisplayName("测试获取方法对象")
    void testGetMethod() throws NoSuchMethodException {
        Method publicMethod = cheetah.getMethod("publicMethod");
        assertEquals("publicMethod", publicMethod.getName(), "应该正确获取公共方法");

        Method paramMethod = cheetah.getMethod("methodWithParams", String.class, int.class);
        assertEquals("methodWithParams", paramMethod.getName(), "应该正确获取带参数的方法");
        assertEquals(2, paramMethod.getParameterCount(), "参数数量应该正确");

        assertThrows(NoSuchMethodException.class, () -> cheetah.getMethod("nonExistentMethod"),
                "获取不存在的方法应该抛出异常");
    }

    @Test
    @DisplayName("测试获取构造器对象")
    void testGetConstructor() throws NoSuchMethodException {
        Constructor<?> defaultConstructor = cheetah.getConstructor();
        assertNotNull(defaultConstructor, "应该正确获取默认构造器");

        Constructor<?> paramConstructor = cheetah.getConstructor(String.class);
        assertNotNull(paramConstructor, "应该正确获取带参数的构造器");
        assertEquals(1, paramConstructor.getParameterCount(), "参数数量应该正确");

        assertThrows(NoSuchMethodException.class, () -> cheetah.getConstructor(Double.class),
                "获取不存在的构造器应该抛出异常");
    }

    @Test
    @DisplayName("测试获取字段值")
    void testGetFieldValue() throws Throwable {
        Field publicField = cheetah.getField("publicField");
        Object value = cheetah.getFieldValue(publicField, testInstance);
        assertEquals("public", value, "应该正确获取字段值");

        Field staticField = cheetah.getField("STATIC_FIELD");
        Object staticValue = cheetah.getFieldValue(staticField, null);
        assertEquals("staticValue", staticValue, "应该正确获取静态字段值");
    }

    @Test
    @DisplayName("测试设置字段值")
    void testSetFieldValue() throws Throwable {
        Field publicField = cheetah.getField("publicField");

        // 设置新值
        cheetah.getFieldValue(publicField, testInstance, "newValue");
        Object newValue = cheetah.getFieldValue(publicField, testInstance);
        assertEquals("newValue", newValue, "应该正确设置字段值");

        Field staticField = cheetah.getField("STATIC_FIELD");
        cheetah.getFieldValue(staticField, null, "newStaticValue");
        Object newStaticValue = cheetah.getFieldValue(staticField, null);
        assertEquals("newStaticValue", newStaticValue, "应该正确设置静态字段值");
    }

    @Test
    @DisplayName("测试调用方法")
    void testInvokeMethod() throws Throwable {
        // 创建一个有返回值和副作用的测试方法类
        MethodInvocationTest testObj = new MethodInvocationTest();
        ClassMetadata methodcheetah = ClassMetadata.of(MethodInvocationTest.class);

        Method method = methodcheetah.getMethod("testMethod", String.class);
        Object result = methodcheetah.invokeMethod(method, testObj, "testParam");

        assertEquals("testParam_processed", result, "方法调用应该返回正确的结果");
        assertEquals("testParam", testObj.getLastParam(), "方法应该正确修改对象状态");
    }

    static class MethodInvocationTest {
        private String lastParam;

        public String testMethod(String param) {
            this.lastParam = param;
            return param + "_processed";
        }

        public String getLastParam() {
            return lastParam;
        }
    }

    @Test
    @DisplayName("测试静态字段迭代器")
    void testGetStaticFieldIterator() {
        Iterator<Field> iterator = cheetah.getStaticFieldIterator(null);

        int count = 0;
        while (iterator.hasNext()) {
            Field field = iterator.next();
            assertTrue(field.getName().equals("STATIC_FIELD") || field.getName().equals("staticInt"),
                    "静态字段迭代器应该只包含静态字段");
            count++;
        }

        assertEquals(2, count, "应该有两个静态字段");
    }

    @Test
    @DisplayName("测试非静态字段迭代器")
    void testGetNonStaticFieldIterator() {
        Iterator<Field> iterator = cheetah.getNonStaticFieldIterator(null);

        int count = 0;
        while (iterator.hasNext()) {
            Field field = iterator.next();
            assertFalse(java.lang.reflect.Modifier.isStatic(field.getModifiers()),
                    "非静态字段迭代器不应该包含静态字段");
            count++;
        }

        assertEquals(4, count, "应该有四个非静态字段");
    }

    @Test
    @DisplayName("测试带过滤条件的字段迭代器")
    void testFieldIteratorWithFilter() {
        Predicate<Field> publicFilter = field -> java.lang.reflect.Modifier.isPublic(field.getModifiers());
        Iterator<Field> iterator = cheetah.getNonStaticFieldIterator(publicFilter);

        int count = 0;
        while (iterator.hasNext()) {
            Field field = iterator.next();
            assertTrue(java.lang.reflect.Modifier.isPublic(field.getModifiers()),
                    "过滤后的迭代器应该只包含公共字段");
            assertEquals("publicField", field.getName(), "应该只找到publicField");
            count++;
        }

        assertEquals(1, count, "应该只有一个公共非静态字段");
    }

    @Test
    @DisplayName("测试静态方法迭代器")
    void testGetStaticMethodIterators() {
        Iterator<Method> iterator = cheetah.getStaticMethodIterators(null);

        int count = 0;
        while (iterator.hasNext()) {
            Method method = iterator.next();
            assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()),
                    "静态方法迭代器应该只包含静态方法");
            assertEquals("staticMethod", method.getName(), "应该找到staticMethod");
            count++;
        }

        assertEquals(1, count, "应该有一个静态方法");
    }

    @Test
    @DisplayName("测试非静态方法迭代器")
    void testGetNonStaticMethodIterator() {
        Iterator<Method> iterator = cheetah.getNonStaticMethodIterator(null);

        int count = 0;
        while (iterator.hasNext()) {
            Method method = iterator.next();
            assertFalse(java.lang.reflect.Modifier.isStatic(method.getModifiers()),
                    "非静态方法迭代器不应该包含静态方法");
            count++;
        }

        assertEquals(5, count, "应该有五个非静态方法");
    }

    @Test
    @DisplayName("测试公共构造器迭代器")
    void testGetPublicConstructorIterator() {
        Iterator<Constructor<?>> iterator = cheetah.getPublicConstructorIterator(null);

        int count = 0;
        while (iterator.hasNext()) {
            Constructor<?> constructor = iterator.next();
            assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()),
                    "公共构造器迭代器应该只包含公共构造器");
            count++;
        }

        assertEquals(2, count, "应该有两个公共构造器");
    }

    @Test
    @DisplayName("测试非公共构造器迭代器")
    void testGetNonPublicConstructorIterator() {
        Iterator<Constructor<?>> iterator = cheetah.getNonPublicConstructorIterator(null);

        int count = 0;
        while (iterator.hasNext()) {
            Constructor<?> constructor = iterator.next();
            assertFalse(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()),
                    "非公共构造器迭代器不应该包含公共构造器");
            count++;
        }

        assertEquals(1, count, "应该有一个非公共构造器");
    }

    @Test
    @DisplayName("测试迭代器的hasNext和next方法")
    void testIteratorHasNextAndNext() {
        Iterator<Field> iterator = cheetah.getStaticFieldIterator(null);

        // 第一次调用hasNext
        assertTrue(iterator.hasNext(), "应该有下一个元素");

        // 调用next获取第一个元素
        Field firstField = iterator.next();
        assertNotNull(firstField, "第一个元素不应该为null");

        // 再次调用hasNext
        assertTrue(iterator.hasNext(), "应该还有下一个元素");

        // 调用next获取第二个元素
        Field secondField = iterator.next();
        assertNotNull(secondField, "第二个元素不应该为null");

        // 没有更多元素
        assertFalse(iterator.hasNext(), "不应该还有更多元素");

        // 再次调用next应该抛出异常
        assertThrows(NoSuchElementException.class, iterator::next,
                "没有更多元素时调用next应该抛出异常");
    }

    @Test
    @DisplayName("测试空迭代器")
    void testEmptyIterator() {
        // 创建一个没有静态字段的类进行测试
        ClassMetadata emptycheetah = ClassMetadata.of(EmptyClass.class);
        Iterator<Field> iterator = emptycheetah.getStaticFieldIterator(null);

        assertFalse(iterator.hasNext(), "空迭代器应该没有下一个元素");
        assertThrows(NoSuchElementException.class, iterator::next,
                "空迭代器调用next应该抛出异常");
    }

    static class EmptyClass {
        // 没有静态字段
        private String instanceField = "test";
    }

    @ParameterizedTest
    @ValueSource(strings = {"publicField", "protectedField", "privateField", "packageField"})
    @DisplayName("参数化测试：各种访问级别的字段")
    void testDifferentAccessLevelFields(String fieldName) throws Throwable {
        Field field = cheetah.getField(fieldName);
        assertNotNull(field, "应该能够获取到字段: " + fieldName);

        // 获取字段值
        Object value = cheetah.getFieldValue(field, testInstance);
        assertNotNull(value, "字段值不应该为null: " + fieldName);
    }

    @Test
    @DisplayName("测试继承关系的元数据")
    void testInheritancecheetah() {
        ClassMetadata childcheetah = ClassMetadata.of(ChildClass.class);
        ClassMetadata parentcheetah = ClassMetadata.of(ParentClass.class);

        // 验证子类包含父类的字段和方法
        assertFalse(childcheetah.containsField("parentField"), "子类不应该包含父类的字段");
        assertFalse(childcheetah.containsMethod("parentMethod"), "子类不应该包含父类的方法");

        // 验证父类元数据独立存在
        assertTrue(parentcheetah.containsField("parentField"), "父类应该包含自己的字段");
    }

    static class ParentClass {
        protected String parentField = "parentValue";

        public void parentMethod() {
            // 父类方法
        }
    }

    static class ChildClass extends ParentClass {
        private String childField = "childValue";

        public void childMethod() {
            // 子类方法
        }
    }
}
