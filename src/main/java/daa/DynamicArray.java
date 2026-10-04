package daa;

public class DynamicArray {
    private int[] data = new int[4];
    private int size = 0;
    private final Metrics metrics = new Metrics();
    public int size() {
        return size;
    }
    public Metrics metrics() {
        return metrics;
    }
    private void ensureCapacity() {
        if (size < data.length) {
            return;
        }
        int[] newData = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            metrics.steps++;
            metrics.moves++;
        }
        data = newData;
    }
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size
            );
        }
    }
    public void add(int value) {
        ensureCapacity();
        data[size] = value;
        size++;
    }
    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size
            );
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            metrics.steps++;
            metrics.moves++;
        }
        data[index] = value;
        size++;
    }
    public int get(int index) {
        checkIndex(index);
        metrics.steps++;
        return data[index];
    }
    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            metrics.comparisons++;
            if (data[i] == value) {
                return true;
            }
        }
        return false;
    }
    public int remove(int index) {
        checkIndex(index);
        int removed = data[index];
        metrics.steps++;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            metrics.steps++;
            metrics.moves++;
        }
        size--;
        return removed;
    }
}