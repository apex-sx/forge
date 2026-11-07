/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * Cleans a direct {@link ByteBuffer}. Without manual intervention, direct ByteBuffers will be cleaned eventually upon
 * garbage collection. However, this should not be relied upon since it may not occur in a timely fashion -
 * especially since off heap ByeBuffers don't put pressure on the garbage collector.
 * <p>
 * <strong>Warning:</strong> Do not attempt to use a direct {@link ByteBuffer} that has been cleaned or bad things will happen.
 * Don't use this class unless you can ensure that the cleaned buffer will not be accessed anymore.
 * </p>
 * <p>
 * See <a href=https://bugs.openjdk.java.net/browse/JDK-4724038>JDK-4724038</a>
 * </p>
 */
final class ByteBufferCleaner {

    private interface Cleaner {
        void clean(ByteBuffer buffer) throws ReflectiveOperationException;
    }

    private static final class Java8Cleaner implements Cleaner {

        private final Method cleanerMethod;
        private final Method cleanMethod;

        private Java8Cleaner() throws ReflectiveOperationException, SecurityException {
            cleanMethod = Class.forName("sun.misc.Cleaner").getMethod("clean");
            cleanerMethod = Class.forName("sun.nio.ch.DirectBuffer").getMethod("cleaner");
        }

        @Override
        public void clean(final ByteBuffer buffer) throws ReflectiveOperationException {
            final Object cleaner = cleanerMethod.invoke(buffer);
            if (cleaner != null) {
                cleanMethod.invoke(cleaner);
            }
        }
    }

    private static final class Java9Cleaner implements Cleaner {

        private final Object theUnsafe;
        private final Method invokeCleaner;

        private Java9Cleaner() throws ReflectiveOperationException, SecurityException {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            theUnsafe = field.get(null);
            invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
        }

        @Override
        public void clean(final ByteBuffer buffer) throws ReflectiveOperationException {
            invokeCleaner.invoke(theUnsafe, buffer);
        }
    }

    private static final Cleaner INSTANCE = getCleaner();

    /**
     * Releases memory held by the given {@link ByteBuffer}.
     *
     * @param buffer to release.
     * @throws IllegalStateException on internal failure.
     */
    static void clean(final ByteBuffer buffer) {
        try {
            INSTANCE.clean(buffer);
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to clean direct buffer.", e);
        }
    }

    private static Cleaner getCleaner() {
        try {
            return new Java8Cleaner();
        } catch (final Exception e) {
            try {
                return new Java9Cleaner();
            } catch (final Exception e1) {
                throw new IllegalStateException("Failed to initialize a Cleaner.", e);
            }
        }
    }

    /**
     * Tests if were able to load a suitable cleaner for the current JVM. Attempting to call
     * {@code ByteBufferCleaner#clean(ByteBuffer)} when this method returns false will result in an exception.
     *
     * @return {@code true} if cleaning is supported, {@code false} otherwise.
     */
    static boolean isSupported() {
        return INSTANCE != null;
    }
}
