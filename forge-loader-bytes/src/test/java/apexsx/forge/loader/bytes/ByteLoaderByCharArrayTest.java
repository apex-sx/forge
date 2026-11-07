/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByCharArrayTest {
    @Test
    public void test() throws Exception {
        char[] data = "Hello, World!".toCharArray();
        try (ByteLoader loader = ByteLoaderFactory.of(data, 3)) {
            for (char c : data) {
                assertTrue(loader.hasNextByteValue());
                assertEquals((byte) (c >> 8), loader.nextByteValue());
                assertTrue(loader.hasNextByteValue());
                assertEquals((byte) c, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        }
    }
}
