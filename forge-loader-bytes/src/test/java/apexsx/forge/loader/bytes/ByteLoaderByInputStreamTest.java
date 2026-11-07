/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByInputStreamTest {
    @Test
    public void test() throws Exception {
        byte[] data = "Hello from InputStream".getBytes();
        try (ByteLoader loader = ByteLoaderFactory.of(new ByteArrayInputStream(data), 3)) {
            for (byte b : data) {
                assertTrue(loader.hasNextByteValue());
                assertEquals(b, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        }
    }
}
