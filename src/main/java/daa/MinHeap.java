package daa;

public class MinHeap {
    private int[] data = new int[4];
    private int size;
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
    public void insert(int value) {
        ensureCapacity();
        data[size] = value;
        int index = size;
        size++;

        while (index > 0) {
            int parent = (index - 1) / 2;
            int currentValue = data[index];
            int parentValue = data[parent];
            metrics.steps += 2;
            metrics.comparisons++;
            if (parentValue <= currentValue) {
                break;
            }
            data[parent] = currentValue;
            data[index] = parentValue;
            metrics.moves += 2;
            index = parent;
        }
    }
    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        return data[0];
    }
    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int result = data[0];
        metrics.steps++;
        size--;

        if (size == 0) {
            return result;
        }
        data[0] = data[size];
        metrics.steps++;
        metrics.moves++;
        int index = 0;

        while (index < size / 2) {
            int left = 2 * index + 1;
            int right = left + 1;
            int smaller = left;
            int smallerValue = data[left];
            metrics.steps++;

            if (right < size) {
                int rightValue = data[right];
                metrics.steps++;
                metrics.comparisons++;

                if (rightValue < smallerValue) {
                    smaller = right;
                    smallerValue = rightValue;
                }
            }
            int currentValue = data[index];
            metrics.steps++;
            metrics.comparisons++;

            if (currentValue <= smallerValue) {
                break;
            }
            data[index] = smallerValue;
            data[smaller] = currentValue;
            metrics.moves += 2;
            index = smaller;
        }
        return result;
    }
    public boolean hasHeapProperty() {
        for (int child = 1; child < size; child++) {
            int parent = (child - 1) / 2;

            if (data[parent] > data[child]) {
                return false;
            }
        }
        return true;
    }
}
