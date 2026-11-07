/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByLongArrayTest {
    @Test
    public void test() throws Exception {
        long[] data = {0x123456789ABCDEF0L, 0x0FEDCBA987654321L};
        byte[] expected = new byte[]{
                (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
                (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0,
                (byte) 0x0F, (byte) 0xED, (byte) 0xCB, (byte) 0xA9,
                (byte) 0x87, (byte) 0x65, (byte) 0x43, (byte) 0x21
        };
        try (ByteLoader loader = ByteLoaderFactory.of(data, 8)) {
            for (byte b : expected) {
                assertTrue(loader.hasNextByteValue());
                assertEquals(b, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        }
    }
}
