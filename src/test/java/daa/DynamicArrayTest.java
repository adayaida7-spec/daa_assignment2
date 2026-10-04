package daa;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DynamicArrayTest {

    @Test
    void growsAndPreservesElements() {
        DynamicArray array = new DynamicArray();
        for (int i = 0; i < 10; i++) {
            array.add(i * 3);
        }
        assertEquals(10, array.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i * 3, array.get(i));
        }
    }
    @Test
    void rejectsInvalidIndices() {
        DynamicArray array = new DynamicArray();
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.get(0)
        );
        array.add(42);
        assertEquals(42, array.get(0));
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.get(-1)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.get(1)
        );
    }
    @Test
    void insertsAtHeadMiddleAndEnd() {
        DynamicArray array = new DynamicArray();
        array.add(20);
        array.add(40);
        array.add(0, 10);
        array.add(2, 30);
        array.add(4, 50); // Проверяет вставку с расширением.
        assertEquals(5, array.size());
        for (int i = 0; i < 5; i++) {
            assertEquals((i + 1) * 10, array.get(i));
        }
    }
    @Test
    void removesAtHeadMiddleAndEnd() {
        DynamicArray array = new DynamicArray();
        for (int i = 1; i <= 5; i++) {
            array.add(i * 10);
        }
        assertEquals(10, array.remove(0));
        assertEquals(30, array.remove(1));
        assertEquals(50, array.remove(2));

        assertEquals(2, array.size());
        assertEquals(20, array.get(0));
        assertEquals(40, array.get(1));
    }
    @Test
    void searchesDuplicatesAndHandlesEmptyArray() {
        DynamicArray array = new DynamicArray();
        assertFalse(array.contains(7));
        array.add(7);
        array.add(7);
        array.remove(0);
        assertTrue(array.contains(7));
        assertEquals(7, array.remove(0));
        assertEquals(0, array.size());
        assertFalse(array.contains(7));
    }
    @Test
    void rejectsInvalidInsertAndRemoveIndices() {
        DynamicArray array = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(-1, 10));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(1, 10));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(0));
        array.add(10);
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(2, 20));
    }
}