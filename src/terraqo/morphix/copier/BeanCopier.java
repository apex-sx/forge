package terraqo.morphix.copier;

import java.lang.invoke.*;
import java.util.Map;

/**
 * @author Terraqo
 */
@FunctionalInterface
public interface BeanCopier<S, T> {
    Map<Class<?>, Class<?>> WRAP_TYPE = Map.ofEntries(
            Map.entry(char.class, Character.class),
            Map.entry(boolean.class, Boolean.class),
            Map.entry(byte.class, Byte.class),
            Map.entry(short.class, Short.class),
            Map.entry(int.class, Integer.class),
            Map.entry(long.class, Long.class),
            Map.entry(float.class, Float.class),
            Map.entry(double.class, Double.class)
    );

    /**
     * 创建一个 BeanCopier 实例。
     * <p>
     * 该方法使用 {@link LambdaMetafactory} 动态生成一个高效的 Bean 拷贝器。
     * 生成的拷贝器将直接调用提供的 {@code implementation} MethodHandle，避免了反射调用的性能开销。
     *
     * @param sourceClass    源对象的类类型
     * @param targetClass    目标对象的类类型
     * @param lookup         方法查找上下文，用于访问私有成员或进行链接
     * @param implementation 实际执行拷贝逻辑的 MethodHandle，其签名必须为 (S, T)void
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 生成的 BeanCopier 实例
     * @throws ReflectiveOperationException 如果创建 BeanCopier 时发生错误
     */
    @SuppressWarnings("unchecked")
    static <S, T> BeanCopier<S, T> create(Class<S> sourceClass, Class<T> targetClass, MethodHandles.Lookup lookup, MethodHandle implementation) throws ReflectiveOperationException {
        try {
            // LambdaMetafactory 需要匹配函数式接口的方法签名
            // 接口方法: void copy(S source, T target) -> 类型擦除后为 void copy(Object, Object)

            // 1. factoryType: LambdaMetafactory 返回的目标类型，即函数式接口本身
            MethodType factoryType = MethodType.methodType(BeanCopier.class);

            // 2. interfaceMethodType: 函数式接口中抽象方法的签名（类型擦除后）
            // 对应 BeanCopier::copy，参数为两个 Object，返回 void
            MethodType interfaceMethodType = MethodType.methodType(void.class, Object.class, Object.class);

            // 3. dynamicMethodType: 实际实现方法的具体签名（泛型具体化后）
            // 对应 implementation 的实际参数类型 (S, T)，返回 void
            Class<?> sourceType = WRAP_TYPE.getOrDefault(sourceClass, sourceClass);
            Class<?> targetType = WRAP_TYPE.getOrDefault(targetClass, targetClass);
            MethodType dynamicMethodType = MethodType.methodType(void.class, sourceType, targetType);

            // 校验提供的 implementation MethodHandle 的类型是否与预期的动态方法类型精确匹配
            // 如果不匹配，LambdaMetafactory 将会抛出异常，这里提前检查以提供更清晰的错误信息
            if (!implementation.type().equals(dynamicMethodType)) {
                throw new ReflectiveOperationException(
                        String.format("Implementation method handle type [%s] does not match expected dynamic method type [%s]",
                                implementation.type(), dynamicMethodType));
            }

            // 调用 LambdaMetafactory 生成 CallSite
            // lookup: 查找上下文
            // "copy": 要实现的函数式接口方法名
            // factoryType: 返回类型
            // interfaceMethodType: 接口方法的签名（擦除后）
            // implementation: 实际调用的方法句柄
            // dynamicMethodType: 实际方法的具体签名
            CallSite callSite = LambdaMetafactory.metafactory(lookup, "copy", factoryType,
                    interfaceMethodType, implementation, dynamicMethodType);

            // 获取目标方法句柄并 invokeExact 以获取生成的 BeanCopier 实例
            return (BeanCopier<S, T>) callSite.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new ReflectiveOperationException("Failed to create bean copier for " +
                    sourceClass.getName() + " -> " + targetClass.getName(), e);
        }
    }


    void copy(S source, T target);
}
