package apexsx.forge.lang.meta;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.*;

/**
 * Lambda 工具类，提供将 Java 8+ 函数式接口（Lambda 表达式）绑定为 MethodHandle 的能力，
 * 以及构建顺序执行的方法调用流水线。
 *
 * @author Terraqo
 */
public class AssistLambda {
    public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    /**
     * 空的 MethodHandle，用于表示无操作或作为流水线的初始值。
     * 其方法类型为 void()，即无参数无返回值。
     */
    public static final MethodHandle EMPTY_METHOD_HANDLE = MethodHandles.empty(MethodType.methodType(void.class));

    /**
     * 私有构造函数，防止实例化。
     */
    private AssistLambda() {
    }

    /**
     * 构建一个顺序执行的流水线，其中所有提供的 MethodHandle 将被依次执行。
     * <p>
     * 注意：
     * 1. 所有传入的 MethodHandle 必须具有相同的参数签名。
     * 2. 每个 MethodHandle 的返回值将被丢弃（通过 dropReturn），因此它们通常用于产生副作用的操作。
     * 3. 执行顺序与传入数组的顺序一致。
     * 4. 如果传入 null 或空数组，则返回 EMPTY_METHOD_HANDLE。
     * 5. 如果数组中包含 null 元素，该元素将被跳过。
     *
     * @param methodHandles 要组合的 MethodHandle 数组
     * @return 组合后的 MethodHandle，按顺序执行所有非空输入句柄
     */
    public static MethodHandle createSequentialPipeline(MethodHandle... methodHandles) {
        if (methodHandles == null || methodHandles.length == 0) {
            return EMPTY_METHOD_HANDLE;
        }

        // 初始化结果为空句柄
        MethodHandle result = EMPTY_METHOD_HANDLE;

        // 遍历所有提供的句柄
        for (MethodHandle handle : methodHandles) {
            if (handle == null) {
                continue;
            }

            // 丢弃当前句柄的返回值，确保其返回类型为 void，以便与 foldArguments 兼容
            MethodHandle voidHandle = MethodHandles.dropReturn(handle);

            // 使用 foldArguments 将当前句柄添加到流水线中
            // foldArguments(target, combiner) 的行为是：先执行 combiner，再执行 target
            // 这里我们将新的 voidHandle 作为 target，之前的 result 作为 combiner
            // 这意味着新加入的句柄会在之前所有句柄执行完毕后执行
            result = MethodHandles.foldArguments(voidHandle, result);
        }

        return result;
    }

    /**
     * 通过方法名绑定静态方法为 MethodHandle。
     * <p>
     * 该方法用于将指定类的静态方法通过反射查找并转换为 MethodHandle，
     * 便于后续的方法调用、组合或流水线操作。
     *
     * @param lookup         方法查找上下文，用于执行反射查找操作
     * @param targetType     目标类的 Class 对象，该包含要查找的静态方法
     * @param name           要查找的静态方法名称
     * @param returnType     方法的返回类型
     * @param parameterTypes 方法的参数类型列表（可变参数）
     * @return 绑定后的 MethodHandle，指向指定的静态方法
     * @throws ReflectiveOperationException 当方法查找失败时抛出，例如方法不存在或签名不匹配
     */
    public static MethodHandle bindingStaticHandle(MethodHandles.Lookup lookup, Class<?> targetType, String name, Class<?> returnType, Class<?>... parameterTypes) throws ReflectiveOperationException {
        return lookup.findStatic(targetType, name, MethodType.methodType(returnType, parameterTypes));
    }

    /**
     * 将 Supplier&lt;R&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param returnType 期望的返回类型
     * @param lambda     Supplier Lambda 表达式
     * @param <R>        返回类型泛型
     * @return 绑定后的 MethodHandle
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <R> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<R> returnType, Supplier<R> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, Supplier.class, "get", lambda, returnType);
    }

    /**
     * 将 BooleanSupplier Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda BooleanSupplier Lambda 表达式
     * @return 绑定后的 MethodHandle，返回类型为 boolean
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, BooleanSupplier lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, BooleanSupplier.class, "getAsBoolean", lambda, boolean.class);
    }

    /**
     * 将 IntSupplier Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda IntSupplier Lambda 表达式
     * @return 绑定后的 MethodHandle，返回类型为 int
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, IntSupplier lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, IntSupplier.class, "getAsInt", lambda, int.class);
    }

    /**
     * 将 LongSupplier Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda LongSupplier Lambda 表达式
     * @return 绑定后的 MethodHandle，返回类型为 long
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, LongSupplier lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, LongSupplier.class, "getAsLong", lambda, long.class);
    }

    /**
     * 将 DoubleSupplier Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda DoubleSupplier Lambda 表达式
     * @return 绑定后的 MethodHandle，返回类型为 double
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, DoubleSupplier lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, DoubleSupplier.class, "getAsDouble", lambda, double.class);
    }

    /**
     * 将 Function&lt;T, R&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 输入参数类型
     * @param returnType 返回类型
     * @param lambda     Function Lambda 表达式
     * @param <T>        输入类型泛型
     * @param <R>        返回类型泛型
     * @return 绑定后的 MethodHandle
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T, R> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, Class<R> returnType, Function<T, R> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, Function.class, "apply", lambda, returnType, sourceType);
    }

    /**
     * 将 BiFunction&lt;T, U, R&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 第一个输入参数类型
     * @param secondType 第二个输入参数类型
     * @param returnType 返回类型
     * @param lambda     BiFunction Lambda 表达式
     * @param <T>        第一个输入类型泛型
     * @param <U>        第二个输入类型泛型
     * @param <R>        返回类型泛型
     * @return 绑定后的 MethodHandle
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T, U, R> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, Class<U> secondType, Class<R> returnType, BiFunction<T, U, R> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, BiFunction.class, "apply", lambda, returnType, sourceType, secondType);
    }

    /**
     * 将 IntFunction&lt;R&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param returnType 返回类型
     * @param lambda     IntFunction Lambda 表达式
     * @param <R>        返回类型泛型
     * @return 绑定后的 MethodHandle，输入类型为 int
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <R> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<R> returnType, IntFunction<R> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, IntFunction.class, "apply", lambda, returnType, int.class);
    }

    /**
     * 将 LongFunction&lt;R&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param returnType 返回类型
     * @param lambda     LongFunction Lambda 表达式
     * @param <R>        返回类型泛型
     * @return 绑定后的 MethodHandle，输入类型为 long
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <R> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<R> returnType, LongFunction<R> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, LongFunction.class, "apply", lambda, returnType, long.class);
    }

    /**
     * 将 DoubleFunction&lt;R&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param returnType 返回类型
     * @param lambda     DoubleFunction Lambda 表达式
     * @param <R>        返回类型泛型
     * @return 绑定后的 MethodHandle，输入类型为 double
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <R> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<R> returnType, DoubleFunction<R> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, DoubleFunction.class, "apply", lambda, returnType, double.class);
    }

    /**
     * 将 ToIntFunction&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda ToIntFunction Lambda 表达式
     * @param <T>    输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 int，输入类型为 Object（泛型擦除后）
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, ToIntFunction<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, ToIntFunction.class, "applyAsInt", lambda, int.class, Object.class);
    }

    /**
     * 将 ToLongFunction&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda ToLongFunction Lambda 表达式
     * @param <T>    输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 long，输入类型为 Object（泛型擦除后）
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, ToLongFunction<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, ToLongFunction.class, "applyAsLong", lambda, long.class, Object.class);
    }

    /**
     * 将 ToDoubleFunction&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda ToDoubleFunction Lambda 表达式
     * @param <T>    输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 double，输入类型为 Object（泛型擦除后）
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, ToDoubleFunction<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, ToDoubleFunction.class, "applyAsDouble", lambda, double.class, Object.class);
    }

    /**
     * 将 Consumer&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 输入参数类型
     * @param lambda     Consumer Lambda 表达式
     * @param <T>        输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, Consumer<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, Consumer.class, "accept", lambda, void.class, sourceType);
    }

    /**
     * 将 BiConsumer&lt;T, U&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param firstType  第一个输入参数类型
     * @param secondType 第二个输入参数类型
     * @param lambda     BiConsumer Lambda 表达式
     * @param <T>        第一个输入类型泛型
     * @param <U>        第二个输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T, U> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> firstType, Class<U> secondType, BiConsumer<T, U> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, BiConsumer.class, "accept", lambda, void.class, firstType, secondType);
    }

    /**
     * 将 IntConsumer Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda IntConsumer Lambda 表达式
     * @return 绑定后的 MethodHandle，输入类型为 int，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, IntConsumer lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, IntConsumer.class, "accept", lambda, void.class, int.class);
    }

    /**
     * 将 LongConsumer Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda LongConsumer Lambda 表达式
     * @return 绑定后的 MethodHandle，输入类型为 long，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, LongConsumer lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, LongConsumer.class, "accept", lambda, void.class, long.class);
    }

    /**
     * 将 DoubleConsumer Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda DoubleConsumer Lambda 表达式
     * @return 绑定后的 MethodHandle，输入类型为 double，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, DoubleConsumer lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, DoubleConsumer.class, "accept", lambda, void.class, double.class);
    }

    /**
     * 将 ObjIntConsumer&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 对象输入参数类型
     * @param lambda     ObjIntConsumer Lambda 表达式
     * @param <T>        对象输入类型泛型
     * @return 绑定后的 MethodHandle，输入类型为 T 和 int，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, ObjIntConsumer<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, ObjIntConsumer.class, "accept", lambda, void.class, sourceType, int.class);
    }

    /**
     * 将 ObjLongConsumer&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 对象输入参数类型
     * @param lambda     ObjLongConsumer Lambda 表达式
     * @param <T>        对象输入类型泛型
     * @return 绑定后的 MethodHandle，输入类型为 T 和 long，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, ObjLongConsumer<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, ObjLongConsumer.class, "accept", lambda, void.class, sourceType, long.class);
    }

    /**
     * 将 ObjDoubleConsumer&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 对象输入参数类型
     * @param lambda     ObjDoubleConsumer Lambda 表达式
     * @param <T>        对象输入类型泛型
     * @return 绑定后的 MethodHandle，输入类型为 T 和 double，返回类型为 void
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, ObjDoubleConsumer<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, ObjDoubleConsumer.class, "accept", lambda, void.class, sourceType, double.class);
    }

    /**
     * 将 Predicate&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 输入参数类型
     * @param lambda     Predicate Lambda 表达式
     * @param <T>        输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 boolean
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, Predicate<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, Predicate.class, "test", lambda, boolean.class, sourceType);
    }

    /**
     * 将 BiPredicate&lt;T, U&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param firstType  第一个输入参数类型
     * @param secondType 第二个输入参数类型
     * @param lambda     BiPredicate Lambda 表达式
     * @param <T>        第一个输入类型泛型
     * @param <U>        第二个输入类型泛型
     * @return 绑定后的 MethodHandle，返回类型为 boolean
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T, U> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> firstType, Class<U> secondType, BiPredicate<T, U> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, BiPredicate.class, "test", lambda, boolean.class, firstType, secondType);
    }

    /**
     * 将 IntPredicate Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda IntPredicate Lambda 表达式
     * @return 绑定后的 MethodHandle，输入类型为 int，返回类型为 boolean
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, IntPredicate lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, IntPredicate.class, "test", lambda, boolean.class, int.class);
    }

    /**
     * 将 LongPredicate Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda LongPredicate Lambda 表达式
     * @return 绑定后的 MethodHandle，输入类型为 long，返回类型为 boolean
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, LongPredicate lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, LongPredicate.class, "test", lambda, boolean.class, long.class);
    }

    /**
     * 将 DoublePredicate Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda DoublePredicate Lambda 表达式
     * @return 绑定后的 MethodHandle，输入类型为 double，返回类型为 boolean
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, DoublePredicate lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, DoublePredicate.class, "test", lambda, boolean.class, double.class);
    }

    /**
     * 将 UnaryOperator&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 输入/输出类型
     * @param lambda     UnaryOperator Lambda 表达式
     * @param <T>        类型泛型
     * @return 绑定后的 MethodHandle，输入和返回类型相同
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, UnaryOperator<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, UnaryOperator.class, "apply", lambda, sourceType, sourceType);
    }

    /**
     * 将 IntUnaryOperator Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda IntUnaryOperator Lambda 表达式
     * @return 绑定后的 MethodHandle，输入和返回类型均为 int
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, IntUnaryOperator lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, IntUnaryOperator.class, "applyAsInt", lambda, int.class, int.class);
    }

    /**
     * 将 LongUnaryOperator Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda LongUnaryOperator Lambda 表达式
     * @return 绑定后的 MethodHandle，输入和返回类型均为 long
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, LongUnaryOperator lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, LongUnaryOperator.class, "applyAsLong", lambda, long.class, long.class);
    }

    /**
     * 将 DoubleUnaryOperator Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda DoubleUnaryOperator Lambda 表达式
     * @return 绑定后的 MethodHandle，输入和返回类型均为 double
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, DoubleUnaryOperator lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, DoubleUnaryOperator.class, "applyAsDouble", lambda, double.class, double.class);
    }

    /**
     * 将 BinaryOperator&lt;T&gt; Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup     方法查找上下文
     * @param sourceType 输入/输出类型
     * @param lambda     BinaryOperator Lambda 表达式
     * @param <T>        类型泛型
     * @return 绑定后的 MethodHandle，两个输入参数和返回类型相同
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static <T> MethodHandle bindingHandle(MethodHandles.Lookup lookup, Class<T> sourceType, BinaryOperator<T> lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, BinaryOperator.class, "apply", lambda, sourceType, sourceType, sourceType);
    }

    /**
     * 将 IntBinaryOperator Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda IntBinaryOperator Lambda 表达式
     * @return 绑定后的 MethodHandle，两个输入参数和返回类型均为 int
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, IntBinaryOperator lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, IntBinaryOperator.class, "applyAsInt", lambda, int.class, int.class, int.class);
    }

    /**
     * 将 LongBinaryOperator Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda LongBinaryOperator Lambda 表达式
     * @return 绑定后的 MethodHandle，两个输入参数和返回类型均为 long
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, LongBinaryOperator lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, LongBinaryOperator.class, "applyAsLong", lambda, long.class, long.class, long.class);
    }

    /**
     * 将 DoubleBinaryOperator Lambda 表达式绑定为 MethodHandle。
     *
     * @param lookup 方法查找上下文
     * @param lambda DoubleBinaryOperator Lambda 表达式
     * @return 绑定后的 MethodHandle，两个输入参数和返回类型均为 double
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    public static MethodHandle bindingHandle(MethodHandles.Lookup lookup, DoubleBinaryOperator lambda) throws ReflectiveOperationException {
        return AssistLambda.bindingLambdaHandle(lookup, DoubleBinaryOperator.class, "applyAsDouble", lambda, double.class, double.class, double.class);
    }

    /**
     * 核心方法：将任意 Lambda 表达式绑定为 MethodHandle。
     * <p>
     * 该方法通过反射找到 Lambda 接口中指定方法的句柄，并将 Lambda 实例绑定到该句柄上，
     * 最后进行类型转换以匹配期望的方法签名。
     *
     * @param lookup           方法查找上下文
     * @param lambdaClass      Lambda 接口的 Class 对象
     * @param lambdaMethodName Lambda 接口中要调用的方法名
     * @param lambda           Lambda 实例
     * @param returnType       期望的返回类型
     * @param parameterTypes   期望的参数类型列表
     * @param <Lambda>         Lambda 接口类型泛型
     * @return 绑定并转换后的 MethodHandle
     * @throws ReflectiveOperationException 当反射操作失败时抛出
     */
    private static <Lambda> MethodHandle bindingLambdaHandle(MethodHandles.Lookup lookup,
                                                             Class<Lambda> lambdaClass, String lambdaMethodName, Lambda lambda,
                                                             Class<?> returnType, Class<?>... parameterTypes) throws ReflectiveOperationException {
        try {
            // 确定原始返回类型：如果是基本类型则直接使用，否则使用 Object.class
            Class<?> rawReturnType = returnType.isPrimitive() ? returnType : Object.class;

            // 确定原始参数类型：基本类型保持不变，引用类型统一转换为 Object.class
            Class<?>[] rawParameterTypes = new Class[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                rawParameterTypes[i] = parameterTypes[i] != null && parameterTypes[i].isPrimitive() ? parameterTypes[i] : Object.class;
            }
            // 构建用于查找虚拟方法的 MethodType
            MethodType methodType = MethodType.methodType(rawReturnType, rawParameterTypes);

            // 查找 Lambda 接口中指定方法的虚拟句柄
            MethodHandle operatorHandle = lookup.findVirtual(lambdaClass, lambdaMethodName, methodType);

            // 将 Lambda 实例作为第一个参数插入到句柄中，实现绑定
            MethodHandle boundHandle = MethodHandles.insertArguments(operatorHandle, 0, lambda);

            // 显式转换句柄的参数和返回类型以匹配期望的签名
            methodType = MethodType.methodType(returnType, parameterTypes);

            return MethodHandles.explicitCastArguments(boundHandle, methodType);
        } catch (Throwable e) {
            throw new ReflectiveOperationException("Failed to bind handle for lambda: " + lambdaClass.getSimpleName() + "." + lambdaMethodName, e);
        }
    }
}
