package daa;

public interface IntList {
    int size();

    void add(int value);

    void add(int index, int value);

    int get(int index);

    int remove(int index);

    boolean contains(int value);

    Metrics metrics();
}
