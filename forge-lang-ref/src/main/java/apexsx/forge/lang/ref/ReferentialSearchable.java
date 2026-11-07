/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.lang.ref;

/**
 * 可搜索的强引用实现类。
 * <p>
 * 该类继承自 {@link ReferentialType.StrongReference}，并实现了 {@link Referential} 接口，
 * 提供了一种支持搜索操作的强引用机制。与普通的强引用不同，此类不仅持有对目标对象的强引用，
 * 还允许通过特定的哈希码进行快速检索和匹配，适用于需要高效查找和引用管理的场景。
 * </p>
 *
 * @param <T> 引用对象的类型
 * @author Terraqo
 * @see ReferentialType.StrongReference
 * @see Referential
 */
public final class ReferentialSearchable<T> extends ReferentialType.StrongReference<T> implements Referential<T> {
    /**
     * 默认构造函数。
     * <p>
     * 创建一个空的强引用实例，初始引用值为 {@code null}，哈希码为 {@code 0}。
     * 此构造函数通常用于后续通过 {@link #set(Object, int)} 方法动态设置引用值和哈希码的场景。
     * </p>
     */
    public ReferentialSearchable() {
        super(null, 0);
    }

    /**
     * 带参构造函数。
     * <p>
     * 使用指定的引用值和对应的哈希码创建一个强引用实例。
     * 该构造函数将直接初始化父类 {@link ReferentialType.StrongReference} 的状态。
     * </p>
     *
     * @param referent 被引用的目标对象值，可以为 {@code null}
     * @param hashCode 目标对象的哈希码，用于快速搜索和匹配
     */
    public ReferentialSearchable(T referent, int hashCode) {
        super(referent, hashCode);
    }

    /**
     * 设置当前引用的目标对象值及其对应的哈希码。
     * <p>
     * 此方法会更新内部持有的引用值和哈希码状态。调用此方法后，
     * 该强引用将指向新的目标对象，并使用新的哈希码进行搜索和匹配。
     * </p>
     *
     * @param referent 新的被引用目标对象值，可以为 {@code null}
     * @param hashCode 新的目标对象哈希码
     */
    @Override
    public void set(T referent, int hashCode) {
        this.referent = referent;
        this.hashCode = hashCode;
    }
}