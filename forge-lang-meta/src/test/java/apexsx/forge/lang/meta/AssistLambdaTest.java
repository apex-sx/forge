package apexsx.forge.lang.meta;

import org.junit.jupiter.api.Test;
import terraqo.arbiter.assist.AssistLambda;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

class AssistLambdaTest {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    @Test
    void testCreateSequentialPipelineWithNullInput() throws Throwable {
        MethodHandle pipeline = AssistLambda.createSequentialPipeline(null);
        assertNotNull(pipeline);
        assertEquals(AssistLambda.EMPTY_METHOD_HANDLE, pipeline);
    }

    @Test
    void testCreateSequentialPipelineWithEmptyInput() throws Throwable {
        MethodHandle pipeline = AssistLambda.createSequentialPipeline();
        assertNotNull(pipeline);
        assertEquals(AssistLambda.EMPTY_METHOD_HANDLE, pipeline);
    }

    @Test
    void testCreateSequentialPipelineWithSingleMethod() throws Throwable {
        AtomicInteger counter = new AtomicInteger(0);
        Consumer<String> consumer = s -> counter.incrementAndGet();

        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, consumer);
        MethodHandle pipeline = AssistLambda.createSequentialPipeline(handle);

        pipeline.invoke("test");
        assertEquals(1, counter.get());
    }

    @Test
    void testCreateSequentialPipelineWithMultipleMethods() throws Throwable {
        List<String> executionOrder = new ArrayList<>();

        Consumer<String> first = s -> executionOrder.add("first");
        Consumer<String> second = s -> executionOrder.add("second");
        Consumer<String> third = s -> executionOrder.add("third");

        MethodHandle h1 = AssistLambda.bindingHandle(LOOKUP, String.class, first);
        MethodHandle h2 = AssistLambda.bindingHandle(LOOKUP, String.class, second);
        MethodHandle h3 = AssistLambda.bindingHandle(LOOKUP, String.class, third);

        MethodHandle pipeline = AssistLambda.createSequentialPipeline(h1, h2, h3);
        pipeline.invoke("test");

        assertEquals(3, executionOrder.size());
        assertEquals("first", executionOrder.get(0));
        assertEquals("second", executionOrder.get(1));
        assertEquals("third", executionOrder.get(2));
    }

    @Test
    void testCreateSequentialPipelineWithNullElements() throws Throwable {
        List<String> executionOrder = new ArrayList<>();

        Consumer<String> first = s -> executionOrder.add("first");
        Consumer<String> third = s -> executionOrder.add("third");

        MethodHandle h1 = AssistLambda.bindingHandle(LOOKUP, String.class, first);
        MethodHandle h3 = AssistLambda.bindingHandle(LOOKUP, String.class, third);

        MethodHandle pipeline = AssistLambda.createSequentialPipeline(h1, null, h3);
        pipeline.invoke("test");

        assertEquals(2, executionOrder.size());
        assertEquals("first", executionOrder.get(0));
        assertEquals("third", executionOrder.get(1));
    }

    @Test
    void testBindingHandleFunction() throws Throwable {
        Function<String, Integer> function = String::length;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, Integer.class, function);

        Object result = handle.invoke("hello");
        assertEquals(5, result);
    }

    @Test
    void testBindingHandlePredicate() throws Throwable {
        Predicate<String> predicate = s -> s.length() > 3;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, predicate);

        Object result = handle.invoke("hello");
        assertTrue((boolean) result);

        result = handle.invoke("hi");
        assertFalse((boolean) result);
    }

    @Test
    void testBindingHandleConsumer() throws Throwable {
        List<String> list = new ArrayList<>();
        Consumer<String> consumer = list::add;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, consumer);

        handle.invoke("test");
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));
    }

    @Test
    void testBindingHandleSupplier() throws Throwable {
        Supplier<String> supplier = () -> "hello";
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, supplier);

        Object result = handle.invoke();
        assertEquals("hello", result);
    }

    @Test
    void testBindingHandleBiFunction() throws Throwable {
        BiFunction<String, String, String> biFunction = String::concat;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, String.class, String.class, biFunction);

        Object result = handle.invoke("hello", " world");
        assertEquals("hello world", result);
    }

    @Test
    void testBindingHandleBiPredicate() throws Throwable {
        BiPredicate<String, String> biPredicate = String::equals;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, String.class, biPredicate);

        Object result = handle.invoke("hello", "hello");
        assertTrue((boolean) result);

        result = handle.invoke("hello", "world");
        assertFalse((boolean) result);
    }

    @Test
    void testBindingHandleBiConsumer() throws Throwable {
        List<String> list = new ArrayList<>();
        BiConsumer<List, String> biConsumer = List::add;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, List.class, String.class, biConsumer);

        handle.invoke(list, "test");
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));
    }

    @Test
    void testBindingHandleUnaryOperator() throws Throwable {
        UnaryOperator<String> operator = s -> s.toUpperCase();
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, operator);

        Object result = handle.invoke("hello");
        assertEquals("HELLO", result);
    }

    @Test
    void testBindingHandleBinaryOperator() throws Throwable {
        BinaryOperator<String> operator = String::concat;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, operator);

        Object result = handle.invoke("hello", " world");
        assertEquals("hello world", result);
    }

    @Test
    void testBindingHandleIntFunction() throws Throwable {
        IntFunction<String> function = i -> "value:" + i;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, function);

        Object result = handle.invoke(42);
        assertEquals("value:42", result);
    }

    @Test
    void testBindingHandleLongFunction() throws Throwable {
        LongFunction<String> function = l -> "value:" + l;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, function);

        Object result = handle.invoke(42L);
        assertEquals("value:42", result);
    }

    @Test
    void testBindingHandleDoubleFunction() throws Throwable {
        DoubleFunction<String> function = d -> "value:" + d;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, function);

        Object result = handle.invoke(3.14);
        assertEquals("value:3.14", result);
    }

    @Test
    void testBindingHandleIntPredicate() throws Throwable {
        IntPredicate predicate = i -> i > 10;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, predicate);

        Object result = handle.invoke(15);
        assertTrue((boolean) result);

        result = handle.invoke(5);
        assertFalse((boolean) result);
    }

    @Test
    void testBindingHandleLongPredicate() throws Throwable {
        LongPredicate predicate = l -> l > 100;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, predicate);

        Object result = handle.invoke(150L);
        assertTrue((boolean) result);

        result = handle.invoke(50L);
        assertFalse((boolean) result);
    }

    @Test
    void testBindingHandleDoublePredicate() throws Throwable {
        DoublePredicate predicate = d -> d > 1.0;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, predicate);

        Object result = handle.invoke(2.5);
        assertTrue((boolean) result);

        result = handle.invoke(0.5);
        assertFalse((boolean) result);
    }

    @Test
    void testBindingHandleIntConsumer() throws Throwable {
        AtomicInteger sum = new AtomicInteger(0);
        IntConsumer consumer = sum::addAndGet;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, consumer);

        handle.invoke(10);
        assertEquals(10, sum.get());

        handle.invoke(20);
        assertEquals(30, sum.get());
    }

    @Test
    void testBindingHandleLongConsumer() throws Throwable {
        java.util.concurrent.atomic.AtomicLong sum = new java.util.concurrent.atomic.AtomicLong(0);
        LongConsumer consumer = sum::addAndGet;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, consumer);

        handle.invoke(10L);
        assertEquals(10L, sum.get());

        handle.invoke(20L);
        assertEquals(30L, sum.get());
    }

    @Test
    void testBindingHandleDoubleConsumer() throws Throwable {
        double[] sum = {0.0};
        DoubleConsumer consumer = d -> sum[0] += d;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, consumer);

        handle.invoke(1.5);
        assertEquals(1.5, sum[0]);

        handle.invoke(2.5);
        assertEquals(4.0, sum[0]);
    }

    @Test
    void testBindingHandleIntSupplier() throws Throwable {
        AtomicInteger counter = new AtomicInteger(0);
        IntSupplier supplier = counter::incrementAndGet;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, supplier);

        Object result = handle.invoke();
        assertEquals(1, result);

        result = handle.invoke();
        assertEquals(2, result);
    }

    @Test
    void testBindingHandleLongSupplier() throws Throwable {
        java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong(0);
        LongSupplier supplier = counter::incrementAndGet;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, supplier);

        Object result = handle.invoke();
        assertEquals(1L, result);

        result = handle.invoke();
        assertEquals(2L, result);
    }

    @Test
    void testBindingHandleDoubleSupplier() throws Throwable {
        DoubleSupplier supplier = () -> 3.14;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, supplier);

        Object result = handle.invoke();
        assertEquals(3.14, result);
    }

    @Test
    void testBindingHandleBooleanSupplier() throws Throwable {
        BooleanSupplier supplier = () -> true;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, supplier);

        Object result = handle.invoke();
        assertTrue((boolean) result);
    }

    @Test
    void testBindingHandleToIntFunction() throws Throwable {
        ToIntFunction<String> function = String::length;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, function);

        Object result = handle.invoke("hello");
        assertEquals(5, result);
    }

    @Test
    void testBindingHandleToLongFunction() throws Throwable {
        ToLongFunction<String> function = s -> (long) s.length();
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, function);

        Object result = handle.invoke("hello");
        assertEquals(5L, result);
    }

    @Test
    void testBindingHandleToDoubleFunction() throws Throwable {
        ToDoubleFunction<String> function = s -> s.length() * 1.5;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, function);

        Object result = handle.invoke("hello");
        assertEquals(7.5, result);
    }

    @Test
    void testBindingHandleIntUnaryOperator() throws Throwable {
        IntUnaryOperator operator = i -> i * 2;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, operator);

        Object result = handle.invoke(21);
        assertEquals(42, result);
    }

    @Test
    void testBindingHandleLongUnaryOperator() throws Throwable {
        LongUnaryOperator operator = l -> l * 2;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, operator);

        Object result = handle.invoke(21L);
        assertEquals(42L, result);
    }

    @Test
    void testBindingHandleDoubleUnaryOperator() throws Throwable {
        DoubleUnaryOperator operator = d -> d * 2;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, operator);

        Object result = handle.invoke(3.14);
        assertEquals(6.28, result);
    }

    @Test
    void testBindingHandleIntBinaryOperator() throws Throwable {
        IntBinaryOperator operator = Integer::sum;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, operator);

        Object result = handle.invoke(20, 22);
        assertEquals(42, result);
    }

    @Test
    void testBindingHandleLongBinaryOperator() throws Throwable {
        LongBinaryOperator operator = Long::sum;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, operator);

        Object result = handle.invoke(20L, 22L);
        assertEquals(42L, result);
    }

    @Test
    void testBindingHandleDoubleBinaryOperator() throws Throwable {
        DoubleBinaryOperator operator = Double::sum;
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, operator);

        double result = (double) handle.invokeExact(3.14, 1.0);
        assertEquals(4.14, result, 0.0001);
    }

    @Test
    void testBindingHandleObjIntConsumer() throws Throwable {
        List<String> list = new ArrayList<>();
        ObjIntConsumer<String> consumer = (s, i) -> list.add(s + ":" + i);
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, consumer);

        handle.invoke("test", 42);
        assertEquals(1, list.size());
        assertEquals("test:42", list.get(0));
    }

    @Test
    void testBindingHandleObjLongConsumer() throws Throwable {
        List<String> list = new ArrayList<>();
        ObjLongConsumer<String> consumer = (s, l) -> list.add(s + ":" + l);
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, consumer);

        handle.invoke("test", 100L);
        assertEquals(1, list.size());
        assertEquals("test:100", list.get(0));
    }

    @Test
    void testBindingHandleObjDoubleConsumer() throws Throwable {
        List<String> list = new ArrayList<>();
        ObjDoubleConsumer<String> consumer = (s, d) -> list.add(s + ":" + d);
        MethodHandle handle = AssistLambda.bindingHandle(LOOKUP, String.class, consumer);

        handle.invoke("test", 3.14);
        assertEquals(1, list.size());
        assertEquals("test:3.14", list.get(0));
    }

    @Test
    void testPipelineWithDifferentTypes() throws Throwable {
        AtomicInteger counter = new AtomicInteger(0);

        Consumer<Integer> increment = i -> counter.addAndGet(i);
        Consumer<Integer> doubleValue = i -> counter.addAndGet(i * 2);

        MethodHandle h1 = AssistLambda.bindingHandle(LOOKUP, Integer.class, increment);
        MethodHandle h2 = AssistLambda.bindingHandle(LOOKUP, Integer.class, doubleValue);

        MethodHandle pipeline = AssistLambda.createSequentialPipeline(h1, h2);
        pipeline.invoke(10);

        assertEquals(30, counter.get());
    }

    @Test
    void testComplexPipeline() throws Throwable {
        List<String> log = new ArrayList<>();

        Consumer<String> step1 = s -> log.add("step1:" + s);
        Consumer<String> step2 = s -> log.add("step2:" + s);
        Consumer<String> step3 = s -> log.add("step3:" + s);
        Consumer<String> step4 = s -> log.add("step4:" + s);

        MethodHandle h1 = AssistLambda.bindingHandle(LOOKUP, String.class, step1);
        MethodHandle h2 = AssistLambda.bindingHandle(LOOKUP, String.class, step2);
        MethodHandle h3 = AssistLambda.bindingHandle(LOOKUP, String.class, step3);
        MethodHandle h4 = AssistLambda.bindingHandle(LOOKUP, String.class, step4);

        MethodHandle pipeline = AssistLambda.createSequentialPipeline(h1, h2, h3, h4);
        pipeline.invoke("data");

        assertEquals(4, log.size());
        assertEquals("step1:data", log.get(0));
        assertEquals("step2:data", log.get(1));
        assertEquals("step3:data", log.get(2));
        assertEquals("step4:data", log.get(3));
    }
}
