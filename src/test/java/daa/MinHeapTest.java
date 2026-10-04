package daa;

import org.junit.jupiter.api.Test;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {
    @Test
    void rejectsEmptyHeapOperations() {
        MinHeap heap = new MinHeap();

        assertEquals(0, heap.size());
        assertTrue(heap.hasHeapProperty());
        assertThrows(
                IllegalStateException.class,
                () -> heap.peekMin()
        );
        assertThrows(
                IllegalStateException.class,
                () -> heap.extractMin()
        );
    }
    @Test
    void handlesOneElementAndReuse() {
        MinHeap heap = new MinHeap();
        heap.insert(42);
        assertTrue(heap.hasHeapProperty());
        assertEquals(42, heap.peekMin());
        assertEquals(1, heap.size());
        assertEquals(42, heap.extractMin());
        assertTrue(heap.hasHeapProperty());
        assertEquals(0, heap.size());

        heap.insert(-5);
        assertTrue(heap.hasHeapProperty());
        assertEquals(-5, heap.extractMin());
        assertTrue(heap.hasHeapProperty());
    }
    @Test
    void extractsDuplicatesAndExtremeValuesInOrder() {
        MinHeap heap = new MinHeap();
        int[] values = {
                5, -3, 5, 0, Integer.MAX_VALUE, Integer.MIN_VALUE
        };
        int[] expected = {
                Integer.MIN_VALUE, -3, 0, 5, 5, Integer.MAX_VALUE
        };
        for (int value : values) {
            heap.insert(value);
            assertTrue(heap.hasHeapProperty());
        }
        for (int value : expected) {
            assertEquals(value, heap.extractMin());
            assertTrue(heap.hasHeapProperty());
        }
        assertEquals(0, heap.size());
    }
    @Test
    void matchesReferenceOnRandomOperations() {
        MinHeap heap = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            if (expected.isEmpty() || random.nextBoolean()) {
                int value = random.nextInt(2001) - 1000;
                heap.insert(value);
                expected.add(value);
            } else {
                assertEquals(
                        expected.remove().intValue(),
                        heap.extractMin()
                );
            }
            assertEquals(expected.size(), heap.size());
            assertTrue(heap.hasHeapProperty());
            if (!expected.isEmpty()) {
                assertEquals(
                        expected.peek().intValue(),
                        heap.peekMin()
                );
            }
        }
        while (!expected.isEmpty()) {
            assertEquals(
                    expected.remove().intValue(),
                    heap.extractMin()
            );
            assertTrue(heap.hasHeapProperty());
        }
        assertEquals(0, heap.size());
    }
    @Test
    void extractsRandomDataInNonDecreasingOrder() {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);
        for (int i = 0; i < 1000; i++) {
            heap.insert(random.nextInt());
            assertTrue(heap.hasHeapProperty());
        }
        int previous = heap.extractMin();
        assertTrue(heap.hasHeapProperty());
        for (int i = 1; i < 1000; i++) {
            int current = heap.extractMin();

            assertTrue(previous <= current);
            assertTrue(heap.hasHeapProperty());
            previous = current;
        }
        assertEquals(0, heap.size());
    }
    @Test
    void countsBubbleUpAndPeek() {
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.metrics().reset();
        heap.insert(5);
        assertEquals(2L, heap.metrics().steps);
        assertEquals(2L, heap.metrics().moves);
        assertEquals(1L, heap.metrics().comparisons);
        assertTrue(heap.hasHeapProperty());

        heap.metrics().reset();
        assertEquals(5, heap.peekMin());
        assertEquals(1L, heap.metrics().steps);
        assertEquals(0L, heap.metrics().moves);
        assertEquals(0L, heap.metrics().comparisons);
    }
}
