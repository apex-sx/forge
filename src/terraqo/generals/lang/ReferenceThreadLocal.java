/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import java.lang.ref.Reference;
import java.util.function.Supplier;

/**
 * This class provides thread-local reference variables.
 *
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public final class ReferenceThreadLocal<T> extends ThreadLocal<T> {
    private final ReferenceType referenceType;
    private final Supplier<? extends T> initialValue;

    /**
     * Creates a thread local variable. The initial value of the variable is
     * determined by invoking the {@code get} method on the {@code Supplier}.
     *
     * @param <S>      the type of the thread local's value
     * @param supplier the supplier to be used to determine the initial value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     */
    public static <S> ReferenceThreadLocal<S> weakWithInitial(Supplier<? extends S> supplier) {
        return new ReferenceThreadLocal<>(ReferenceType.WEAK, supplier);
    }

    /**
     * Creates a thread local variable. The initial value of the variable is
     * determined by invoking the {@code get} method on the {@code Supplier}.
     *
     * @param <S> the type of the thread local's value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     */
    public static <S> ReferenceThreadLocal<S> weak() {
        return new ReferenceThreadLocal<>(ReferenceType.WEAK, null);
    }

    /**
     * Creates a thread local variable. The initial value of the variable is
     * determined by invoking the {@code get} method on the {@code Supplier}.
     *
     * @param <S>      the type of the thread local's value
     * @param supplier the supplier to be used to determine the initial value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     */
    public static <S> ReferenceThreadLocal<S> softWithInitial(Supplier<? extends S> supplier) {
        return new ReferenceThreadLocal<>(ReferenceType.SOFT, supplier);
    }

    /**
     * Creates a thread local variable. The initial value of the variable is
     * determined by invoking the {@code get} method on the {@code Supplier}.
     *
     * @param <S> the type of the thread local's value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     */
    public static <S> ReferenceThreadLocal<S> soft() {
        return new ReferenceThreadLocal<>(ReferenceType.SOFT, null);
    }

    private ReferenceThreadLocal(ReferenceType referenceType, Supplier<? extends T> initialValue) {
        this.referenceType = referenceType;
        this.initialValue = initialValue;
    }

    /**
     * Returns the current thread's "initial value" for this
     * thread-local variable.  This method will be invoked the first
     * time a thread accesses the variable with the {@link #get}
     * method, unless the thread previously invoked the {@link #set}
     * method, in which case the {@code initialValue} method will not
     * be invoked for the thread.  Normally, this method is invoked at
     * most once per thread, but it may be invoked again in case of
     * subsequent invocations of {@link #remove} followed by {@link #get}.
     *
     * <p>This implementation simply returns {@code null}; if the
     * programmer desires thread-local variables to have an initial
     * value other than {@code null}, {@code ThreadLocal} must be
     * subclassed, and this method overridden.  Typically, an
     * anonymous inner class will be used.
     *
     * @return the initial value for this thread-local
     */
    @Override
    protected T initialValue() {
        if (this.initialValue == null) {
            return null;
        } else {
            return (T) ReferenceType.create(this.referenceType, this.initialValue.get());
        }
    }

    /**
     * Returns the value in the current thread's copy of this
     * thread-local variable.  If the variable has no value for the
     * current thread, it is first initialized to the value returned
     * by an invocation of the {@link #initialValue} method.
     *
     * @return the current thread's value of this thread-local
     */
    @Override
    public T get() {
        Reference<T> reference = (Reference<T>) super.get();
        if (reference != null) {
            T value = reference.get();
            if (value != null) {
                return value;
            } else {
                remove();
            }
        }
        return null;
    }

    /**
     * Sets the current thread's copy of this thread-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of thread-locals.
     *
     * @param value the value to be stored in the current thread's copy of
     *              this thread-local.
     */
    @Override
    public void set(T value) {
        super.set((T) ReferenceType.create(this.referenceType, value));
    }
}
