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

import java.lang.ref.ReferenceQueue;

/**
 * 一对引用（key,value）的回收队列，如果是强引用则不存在回收队列即为null
 * 该类用于管理键值对的引用队列，支持弱引用和软引用的垃圾回收
 *
 * @author Terraqo
 */
public final class PariReferenceQueue<K, V> {
    // 键的引用队列，如果为null表示使用强引用
    private final ReferenceQueue<K> keyReferenceQueue;
    // 值的引用队列，如果为null表示使用强引用
    private final ReferenceQueue<V> valueReferenceQueue;

    /**
     * 构造函数，从PairReference创建引用队列
     * 根据PairReference的引用类型决定是否创建回收队列
     *
     * @param ReferentialType PairReference实例，用于确定键值对的引用类型
     */
    public PariReferenceQueue(PairReference<?, ?> ReferentialType) {
        this(ReferentialType.type(), ReferentialType.type());
    }

    /**
     * 复制构造函数，从现有的PariReferenceQueue创建新的实例
     * 该构造函数会根据原实例的引用队列状态创建新的引用队列
     *
     * @param pariReferenceQueue 现有的PariReferenceQueue实例，用于复制其引用队列状态
     */
    public PariReferenceQueue(PariReferenceQueue<?, ?> pariReferenceQueue) {
        this.keyReferenceQueue = pariReferenceQueue.keyReferenceQueue == null ? null : new ReferenceQueue<>();
        this.valueReferenceQueue = pariReferenceQueue.valueReferenceQueue == null ? null : new ReferenceQueue<>();
    }

    /**
     * 构造函数，使用指定的引用类型创建引用队列
     * 如果引用类型为null（强引用），则不创建对应的回收队列
     *
     * @param keyReferentialType   键的引用类型，null表示强引用
     * @param valueReferentialType 值的引用类型，null表示强引用
     */
    public PariReferenceQueue(ReferentialType keyReferentialType, ReferentialType valueReferentialType) {
        this.keyReferenceQueue = keyReferentialType == null ? null : new ReferenceQueue<>();
        this.valueReferenceQueue = valueReferentialType == null ? null : new ReferenceQueue<>();
    }

    /**
     * key引用队列
     */
    public ReferenceQueue<K> keyReferenceQueue() {
        return this.keyReferenceQueue;
    }

    /**
     * value引用队列
     */
    public ReferenceQueue<V> valueReferenceQueue() {
        return this.valueReferenceQueue;
    }

    /**
     * 如果密钥是弱引用垃圾回收，则返回。
     */
    public boolean isCollectKeys() {
        return this.keyReferenceQueue != null;
    }

    /**
     * 如果值是弱值或软引用垃圾回收，则返回。
     */
    public boolean isCollectValues() {
        return this.valueReferenceQueue != null;
    }
}
