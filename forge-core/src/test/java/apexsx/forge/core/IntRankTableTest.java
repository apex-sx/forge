package apexsx.forge.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntRankTableTest {


    @Test
    void testBasicPutAndGet() {
        IntRankTable table = IntRankTable.of()
                .add(100)
                .add(200)
                .add(300)
                .build();

        assertEquals(3, table.size());
        assertTrue(table.has(100));
        assertTrue(table.has(200));
        assertTrue(table.has(300));

        assertEquals(0, table.get(100));
        assertEquals(1, table.get(200));
        assertEquals(2, table.get(300));

        assertFalse(table.has(999));
        assertTrue(IntRankTable.isEmptyValue(table.get(999)));
    }

    @Test
    void testDuplicateAdd() {
        IntMap intMap = IntRankTable.of();
        intMap.add(5);
        intMap.add(5);
        intMap.add(5);
        IntRankTable table = intMap.build();

        assertEquals(1, table.size());
        assertEquals(0, table.get(5));
    }

    @Test
    void testDisallowMinValue() {
        IntMap intMap = IntRankTable.of();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> intMap.add(Integer.MIN_VALUE));
        assertEquals("value cannot be Integer.MIN_VALUE", ex.getMessage());
    }

    @Test
    void testInitialCapacityNegative() {
        assertThrows(IllegalArgumentException.class, () -> IntRankTable.of(-1));
    }

    @Test
    void testEmptyTable() {
        IntRankTable table = IntRankTable.of().build();
        assertEquals(0, table.size());
        assertFalse(table.has(1));
        assertTrue(IntRankTable.isEmptyValue(table.get(1)));
        assertEquals(0, table.keys().length);
    }

    @Test
    void testSingleElement() {
        IntRankTable table = IntRankTable.of().add(77).build();
        assertEquals(1, table.size());
        assertTrue(table.has(77));
        assertEquals(0, table.get(77));
        assertArrayEquals(new int[]{77}, table.keys());
    }

    @Test
    void testKeysArray() {
        IntRankTable table = IntRankTable.of()
                .add(11)
                .add(22)
                .add(33)
                .build();
        int[] keys = table.keys();
        assertEquals(3, keys.length);
        assertTrue(contains(keys, 11));
        assertTrue(contains(keys, 22));
        assertTrue(contains(keys, 33));
    }

    @Test
    void testMassInsertTriggerResize() {
        IntMap b = IntRankTable.of(4);
        int count = 100;
        for (int i = 0; i < count; i++) {
            b.add(i * 13);
        }
        IntRankTable table = b.build();
        assertEquals(count, table.size());
        for (int i = 0; i < count; i++) {
            int key = i * 13;
            assertTrue(table.has(key));
            assertEquals(i, table.get(key));
        }
    }

    @Test
    void testToStringFormat() {
        IntRankTable t = IntRankTable.of().add(1).add(10).build();
        String str = t.toString();
        assertTrue(str.contains("1->0"));
        assertTrue(str.contains("10->1"));
    }

    @Test
    void testOfWithInitialCapacity() {
        IntRankTable table = IntRankTable.of(100)
                .add(99)
                .add(101)
                .build();
        assertEquals(2, table.size());
        assertEquals(0, table.get(99));
        assertEquals(1, table.get(101));
    }

    private boolean contains(int[] arr, int v) {
        for (int x : arr) {
            if (x == v) return true;
        }
        return false;
    }
}
