/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByByteArrayTest {
    @Test
    public void test() throws Exception {
        byte[] data = "Hello, World!".getBytes();
        try (ByteLoader loader = ByteLoaderFactory.of(data, 3)) {
            for (byte b : data) {
                assertTrue(loader.hasNextByteValue());
                assertEquals(b, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testToContent() throws Exception {
        byte[] data = "Test toContent".getBytes();
        try (ByteLoader loader = ByteLoaderFactory.of(data, 3)) {
            byte[] result = loader.toContent();
            assertEquals(data.length, result.length);
            for (int i = 0; i < data.length; i++) {
                assertEquals(data[i], result[i]);
            }
        }
    }
}
