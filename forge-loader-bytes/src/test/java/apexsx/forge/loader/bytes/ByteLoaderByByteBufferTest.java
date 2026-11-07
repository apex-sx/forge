/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByByteBufferTest {
    @Test
    public void test() throws Exception {
        byte[] data = "Hello from ByteBuffer".getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        try (ByteLoader loader = ByteLoaderFactory.of(data, 3)) {
            for (byte b : data) {
                assertTrue(loader.hasNextByteValue());
                assertEquals(b, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        }
    }
}
