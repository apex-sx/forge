/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.loader.bytes;

import org.junit.jupiter.api.Test;
import terraqo.primitive.ByteRawIterator;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class ByteLoaderFactoryTest {

    @Test
    void testOfString() {
        String input = "Hello, World!";
        ByteRawIterator iterator = ByteLoaderFactory.of(input, StandardCharsets.US_ASCII, 1024);
        byte[] expected = input.getBytes();

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfByteArray() {
        byte[] input = "Hello, World!".getBytes();
        ByteRawIterator iterator = ByteLoaderFactory.of(input);

        for (byte b : input) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfShortArray() {
        short[] input = {0x0048, 0x0065, 0x006C, 0x006C, 0x006F};
        ByteRawIterator iterator = ByteLoaderFactory.of(input);

        byte[] expected = new byte[]{0x00, 0x48, 0x00, 0x65, 0x00, 0x6C, 0x00, 0x6C, 0x00, 0x6F};

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfIntArray() {
        int[] input = {0x48656C6C, 0x6F2C2057, 0x6F726C64};
        ByteRawIterator iterator = ByteLoaderFactory.of(input);

        byte[] expected = new byte[]{
                (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C,
                (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x57,
                (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64
        };

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfLongArray() {
        long[] input = {0x48656C6C6F2C2057L, 0x6F726C6400000000L};
        ByteRawIterator iterator = ByteLoaderFactory.of(input);

        byte[] expected = new byte[]{
                (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C,
                (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x57,
                (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64,
                0x00, 0x00, 0x00, 0x00
        };

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfFileChannel() throws Exception {
        File tempFile = File.createTempFile("test", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("Hello, World!".getBytes());
        }

        try (FileInputStream fis = new FileInputStream(tempFile);
             FileChannel fileChannel = fis.getChannel()) {
            ByteRawIterator iterator = ByteLoaderFactory.of(fileChannel);

            byte[] expected = "Hello, World!".getBytes();

            for (byte b : expected) {
                assertTrue(iterator.hasNextByteValue());
                assertEquals(b, iterator.nextByteValue());
            }
            assertFalse(iterator.hasNextByteValue());
        }
    }

    @Test
    void testOfInputStream() {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteRawIterator iterator = ByteLoaderFactory.of(inputStream);

        byte[] expected = "Hello, World!".getBytes();

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfIntStream() {
        IntStream intStream = IntStream.of(0x48656C6C, 0x6F2C2057, 0x6F726C64);
        ByteRawIterator iterator = ByteLoaderFactory.of(intStream);

        byte[] expected = new byte[]{
                (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C,
                (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x57,
                (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64
        };

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }

    @Test
    void testOfLongStream() {
        LongStream longStream = LongStream.of(0x48656C6C6F2C2057L, 0x6F726C6400000000L);
        ByteRawIterator iterator = ByteLoaderFactory.of(longStream);

        byte[] expected = new byte[]{
                (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C,
                (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x57,
                (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64,
                0x00, 0x00, 0x00, 0x00
        };

        for (byte b : expected) {
            assertTrue(iterator.hasNextByteValue());
            assertEquals(b, iterator.nextByteValue());
        }
        assertFalse(iterator.hasNextByteValue());
    }
}
