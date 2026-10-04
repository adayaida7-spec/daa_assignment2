package daa;

public class DynamicArray {
    private int[] data = new int[4];
    private int size = 0;

    public int size() {
        return size;
    }
    public void add(int value) {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];

            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
        data[size] = value;
        size++;
    }
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size
            );
        }
        return data[index];
    }
    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            if (data[i] == value) {
                return true;
            }
        }
        return false;
    }
    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size
            );
        }
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", size: " + size
            );
        }
        int removed = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        return removed;
    }
}