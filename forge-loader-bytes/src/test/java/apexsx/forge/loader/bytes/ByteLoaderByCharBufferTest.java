/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByCharBufferTest {
    @Test
    public void test() throws Exception {
        CharBuffer data = CharBuffer.wrap("Hello");
        byte[] expected = new byte[]{
                0x00, 'H', 0x00, 'e', 0x00, 'l', 0x00, 'l', 0x00, 'o'
        };
        try (ByteLoader loader = ByteLoaderFactory.of(data, 3)) {
            for (byte b : expected) {
                assertTrue(loader.hasNextByteValue());
                assertEquals(b, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        }
    }
}
