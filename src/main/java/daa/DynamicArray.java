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
}