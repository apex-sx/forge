/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.lang.ReferenceType;

import java.lang.ref.Reference;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 实例提供器
 *
 * @author potatoxf
 */
public final class InstanceSupplier<T> implements Supplier<T> {
    /**
     * 实例提供器
     */
    private final SupplierThrow<Reference<T>, Throwable> instanceReferenceSupplier;
    /**
     * 默认实例提供器
     */
    private final SupplierThrow<Reference<T>, Throwable> defaultReferenceSupplier;
    /**
     * 实例存储容器
     */
    private final AtomicReference<Reference<T>> instanceContainer = new AtomicReference<>();

    public InstanceSupplier(Reference<T> instance,
                            SupplierThrow<Reference<T>, Throwable> instanceSupplier,
                            SupplierThrow<Reference<T>, Throwable> defaultSupplier) {
        this.instanceContainer.set(instance);
        this.instanceReferenceSupplier = instanceSupplier;
        this.defaultReferenceSupplier = defaultSupplier;
    }

    public static <K, V> InstanceSupplier<Map<K, V>> ofSynchronizedMap(SupplierThrow<Map<K, V>, Throwable> instanceSupplier) {
        return InstanceSupplier.of(null, () -> Collections.synchronizedMap(instanceSupplier.getThrow()), null);
    }

    public static <T> InstanceSupplier<T> of(SupplierThrow<T, Throwable> instanceSupplier) {
        return InstanceSupplier.of(null, instanceSupplier, null);
    }

    public static <T> InstanceSupplier<T> of(SupplierThrow<T, Throwable> instanceSupplier,
                                             SupplierThrow<T, Throwable> defaultSupplier) {
        return InstanceSupplier.of(null, instanceSupplier, defaultSupplier);
    }

    public static <T> InstanceSupplier<T> of(T instance,
                                             SupplierThrow<T, Throwable> instanceSupplier,
                                             SupplierThrow<T, Throwable> defaultSupplier) {
        Reference<T> instanceReference = instance == null ? null : ReferenceType.create(ReferenceType.WEAK, instance);
        SupplierThrow<Reference<T>, Throwable> instanceReferenceSupplier = instanceSupplier == null ? null
                : () -> ReferenceType.create(ReferenceType.WEAK, instanceSupplier.getThrow());
        SupplierThrow<Reference<T>, Throwable> defaultReferenceSupplier = defaultSupplier == null ? null
                : () -> ReferenceType.create(ReferenceType.WEAK, defaultSupplier.getThrow());
        return new InstanceSupplier<>(instanceReference, instanceReferenceSupplier, defaultReferenceSupplier);
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public T get() {
        T instance = null;
        Reference<T> instanceReference = instanceContainer.get();
        if (instanceReference != null) instance = instanceReference.get();
        if (instance == null) {
            instanceContainer.compareAndSet(instanceReference, null);
            try {
                synchronized (instanceContainer) {
                    instanceReference = instanceContainer.get();
                    if (instanceReference == null) {
                        if (instanceReferenceSupplier != null) {
                            try {
                                instanceReference = instanceReferenceSupplier.get();
                            } catch (Throwable e) {
                                if (Log.isEnabledWarn()) {
                                    Log.warn("Error getting value from instance provider", e);
                                }
                            }
                        }
                        if (instanceReference == null && defaultReferenceSupplier != null) {
                            try {
                                instanceReference = defaultReferenceSupplier.get();
                            } catch (Throwable e) {
                                if (Log.isEnabledWarn()) {
                                    Log.warn("Error getting value from default provider", e);
                                }
                            }
                        }
                        this.instanceContainer.compareAndSet(null, instanceReference);
                    }
                }
            } catch (Throwable e) {
                if (Log.isEnabledWarn()) {
                    Log.warn("Error to create instance Reference", e);
                }
            }
            if (instanceReference == null) {
                if (Log.isEnabledWarn()) {
                    Log.warn("Error to create instance Reference of null");
                }
            }
            if (instanceReference != null) {
                instance = instanceReference.get();
            }
            if (instance == null) {
                if (Log.isEnabledWarn()) {
                    Log.warn("Error to create instance of null");
                }
            }
            instanceContainer.compareAndSet(null, instanceReference);
        }
        return instance;
    }

    @Override
    public String toString() {
        T instance = get();
        if (instance == null) return "";
        else return instance.toString();
    }
}
