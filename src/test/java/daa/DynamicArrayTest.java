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
}