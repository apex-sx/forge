/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;

import java.io.CharArrayReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderByReaderTest {
    @Test
    public void test() throws Exception {
        Reader data = new CharArrayReader("Hello".toCharArray());
        byte[] expected = new byte[]{0x00, 'H', 0x00, 'e', 0x00, 'l', 0x00, 'l', 0x00, 'o'};
        try (ByteLoader loader = ByteLoaderFactory.of(data, 3)) {
            for (byte b : expected) {
                assertTrue(loader.hasNextByteValue());
                assertEquals(b, loader.nextByteValue());
            }
            assertFalse(loader.hasNextByteValue());
        }
    }
}
