package daa;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {
    @Test
    void insertsAtHeadMiddleAndEnd() {
        MyLinkedList list = new MyLinkedList();
        list.add(0, 20);
        list.add(0, 10);
        list.add(2, 40);
        list.add(2, 30);

        assertEquals(4, list.size());
        for (int i = 0; i < 4; i++) {
            assertEquals((i + 1) * 10, list.get(i));
        }
    }
    @Test
    void removesAndUpdatesTail() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(20, list.remove(1));
        assertEquals(30, list.remove(1));
        list.add(40);
        assertEquals(40, list.get(1));
        assertEquals(10, list.remove(0));
        assertEquals(40, list.remove(0));
        assertEquals(0, list.size());
        list.add(50);
        assertEquals(50, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    void searchesEmptyListAndDuplicates() {
        MyLinkedList list = new MyLinkedList();
        assertFalse(list.contains(7));
        list.add(7);
        list.add(7);
        list.remove(0);
        assertTrue(list.contains(7));
        assertFalse(list.contains(8));
        list.remove(0);
        assertFalse(list.contains(7));
    }
    @Test
    void rejectsInvalidIndices() {
        MyLinkedList list = new MyLinkedList();

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(0));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(-1, 10));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(1, 10));
        list.add(10);
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(2, 20));
    }
    @Test
    void countsTraversalAndLinkUpdates() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.metrics().reset();

        assertEquals(30, list.get(2));
        assertEquals(2L, list.metrics().steps);
        assertEquals(0L, list.metrics().moves);
        list.metrics().reset();

        assertFalse(list.contains(99));
        assertEquals(3L, list.metrics().steps);
        assertEquals(3L, list.metrics().comparisons);
        list.metrics().reset();

        list.add(0, 5);
        assertEquals(0L, list.metrics().steps);
        assertEquals(2L, list.metrics().moves);
        list.metrics().reset();

        assertEquals(5, list.remove(0));
        assertEquals(1L, list.metrics().steps);
        assertEquals(1L, list.metrics().moves);
    }
    @Test
    void matchesReferenceOnRandomOperations() {
        MyLinkedList actual = new MyLinkedList();
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);
        for (int operation = 0; operation < 1000; operation++) {
            int choice = random.nextInt(4);

            if (expected.isEmpty() || choice == 0) {
                int index = random.nextInt(expected.size() + 1);
                int value = random.nextInt(100);
                actual.add(index, value);
                expected.add(index, value);
            } else if (choice == 1) {
                int index = random.nextInt(expected.size());
                assertEquals(
                        expected.remove(index).intValue(),
                        actual.remove(index)
                );
            } else if (choice == 2) {
                int index = random.nextInt(expected.size());
                assertEquals(
                        expected.get(index).intValue(),
                        actual.get(index)
                );
            } else {
                int value = random.nextInt(100);
                assertEquals(
                        expected.contains(value),
                        actual.contains(value)
                );
            }
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(
                        expected.get(i).intValue(),
                        actual.get(i)
                );
            }
        }
    }
}
