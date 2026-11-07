package com.github.zhitron.effortless.lang;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 线程安全的延迟验证字符串缓存实现。
 * <p>
 * 该类通过双重检查锁定模式确保多线程环境下的安全性，采用AtomicReference实现高效的原子更新操作，
 * 避免传统synchronized关键字带来的性能损耗。支持使用Supplier定义延迟加载逻辑，并通过Predicate
 * 对生成结果进行格式校验，保证缓存数据的合法性。
 *
 * @author zhitron
 */
public final class LazyValidatedStringCache implements Supplier<String> {

    /**
     * 原子引用类型的缓存值存储器。
     * 使用CAS操作保证多线程环境下值更新的原子性和可见性
     */
    private final AtomicReference<String> cachedValue = new AtomicReference<>(null);

    /**
     * 字符串生成策略提供者。
     * 当缓存为空时，通过此Supplier获取新值进行填充
     */
    private final Supplier<String> stringSupplier;

    /**
     * 格式校验规则执行器。
     * 每次生成新字符串后会自动触发test方法进行合规性验证
     */
    private final Predicate<String> validationRule;

    /**
     * 工厂构造方法
     *
     * @param supplier 必须的字符串生成器
     * @return 构建完成的缓存实例
     */
    public static LazyValidatedStringCache of(Supplier<String> supplier) {
        return new LazyValidatedStringCache(supplier, s -> true);
    }

    /**
     * 工厂构造方法
     *
     * @param supplier  必须的字符串生成器
     * @param validator 必须的格式验证器
     * @return 构建完成的缓存实例
     */
    public static LazyValidatedStringCache of(Supplier<String> supplier, Predicate<String> validator) {
        return new LazyValidatedStringCache(supplier, validator);
    }

    /**
     * 双参构造函数
     *
     * @param stringSupplier 非空的字符串供应源
     * @param validationRule 非空的验证规则定义
     */
    public LazyValidatedStringCache(Supplier<String> stringSupplier, Predicate<String> validationRule) {
        this.stringSupplier = Objects.requireNonNull(stringSupplier);
        this.validationRule = Objects.requireNonNull(validationRule);
    }

    /**
     * 获取缓存字符串（线程安全）
     * <p>
     * 实现延迟初始化模式，首次调用时会：
     * 1. 通过Supplier生成新值
     * 2. 使用Predicate进行格式校验
     * 3. 通过原子操作写入缓存
     * 后续调用直接返回已缓存的结果
     *
     * @return 经过验证的缓存字符串
     * @throws IllegalArgumentException 当生成内容不符合校验规则时抛出
     */
    @Override
    public String get() {
        String result = cachedValue.get();
        if (result == null) {
            synchronized (this) {
                result = cachedValue.get();
                if (result == null) {
                    String newValue = stringSupplier.get();
                    if (validationRule.test(newValue)) {
                        cachedValue.set(newValue);
                        result = newValue;
                    } else {
                        throw new IllegalArgumentException("Generated string does not meet the format requirements: " + newValue);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 清除当前缓存值
     * 下次调用get()方法时将重新生成新值
     */
    public void clear() {
        cachedValue.set(null);
    }

    /**
     * 返回缓存字符串的字符串表示形式
     * <p>
     * 该方法线程安全，首次调用时会触发值的延迟初始化过程
     *
     * @return 缓存的字符串值
     * @throws IllegalArgumentException 当生成内容不符合校验规则时抛出
     */
    @Override
    public String toString() {
        return this.get();
    }
}
