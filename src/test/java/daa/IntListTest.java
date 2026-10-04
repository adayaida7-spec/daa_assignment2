package daa;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class IntListTest {
    @Test
    void bothStructuresMatchReference() {
        checkRandomOperations(new DynamicArray());
        checkRandomOperations(new MyLinkedList());
    }
    private void checkRandomOperations(IntList actual) {
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);

        for (int operation = 0; operation < 1000; operation++) {
            int choice = random.nextInt(5);
            int value = random.nextInt(101) - 50;

            if (expected.isEmpty() || choice == 0) {
                actual.add(value);
                expected.add(value);
            } else if (choice == 1) {
                int index = random.nextInt(expected.size() + 1);

                actual.add(index, value);
                expected.add(index, value);
            } else if (choice == 2) {
                int index = random.nextInt(expected.size());

                assertEquals(
                        expected.remove(index).intValue(),
                        actual.remove(index)
                );
            } else if (choice == 3) {
                int index = random.nextInt(expected.size());
                assertEquals(
                        expected.get(index).intValue(),
                        actual.get(index)
                );
            } else {
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
