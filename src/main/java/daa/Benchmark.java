package daa;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    private static final int WARMUP_RUNS = 2;
    private static final int MEASURED_RUNS = 5;
    private static volatile long sink;
    private record Sample(
            long nanos,
            long steps,
            long moves,
            long comparisons
    ) {
    }
    public static void main(String[] args) throws IOException {
        Path output = Path.of("results", "results.csv");
        Files.createDirectories(output.getParent());
        try (PrintWriter writer =
                     new PrintWriter(Files.newBufferedWriter(output))) {

            writer.println(
                    "workload,variant,structure,n,time_ms,steps,moves,comparisons"
            );
            int[] sizes = {100, 1000, 10000, 100000};
            for (int n : sizes) {
                Random random = new Random(42);

                int[] values = new int[n];
                for (int i = 0; i < n; i++) {
                    values[i] = random.nextInt(1_000_000);
                }
                int[] indices = new int[10000];
                for (int i = 0; i < indices.length; i++) {
                    indices[i] = random.nextInt(n);
                }
                int[] queries = new int[1000];
                for (int i = 0; i < queries.length; i++) {
                    if (i % 2 == 0) {
                        queries[i] = values[random.nextInt(n)];
                    } else {
                        queries[i] = -1 - random.nextInt(1_000_000);
                    }
                }
                for (int structure = 0; structure < 2; structure++) {
                    boolean linked = structure == 1;
                    String name = linked
                            ? "MyLinkedList"
                            : "DynamicArray";

                    writeCase(writer, "W1", "-", name,
                            values, indices, queries, linked);
                    writeCase(writer, "W2", "-", name,
                            values, indices, queries, linked);
                    writeCase(writer, "W3", "head", name,
                            values, indices, queries, linked);
                    writeCase(writer, "W3", "middle", name,
                            values, indices, queries, linked);
                }
                writeCase(writer, "W4", "-", "MinHeap",
                        values, indices, queries, false);
            }

            if (writer.checkError()) {
                throw new IOException("Could not write results.csv");
            }
        }
        System.out.println("Saved: " + output.toAbsolutePath());
    }
    private static void writeCase(
            PrintWriter writer,
            String workload,
            String variant,
            String structure,
            int[] values,
            int[] indices,
            int[] queries,
            boolean linked
    ) {
        Sample[] samples = new Sample[MEASURED_RUNS];
        for (int run = 0;
             run < WARMUP_RUNS + MEASURED_RUNS;
             run++) {

            Sample sample;

            if (workload.equals("W4")) {
                sample = runHeap(values);
            } else {
                sample = runList(
                        workload, variant,
                        values, indices, queries, linked
                );
            }
            if (run >= WARMUP_RUNS) {
                samples[run - WARMUP_RUNS] = sample;
            }
        }
        for (int i = 1; i < samples.length; i++) {
            if (samples[i].steps() != samples[0].steps()
                    || samples[i].moves() != samples[0].moves()
                    || samples[i].comparisons()
                    != samples[0].comparisons()) {

                throw new IllegalStateException(
                        "Operation counts differ between runs"
                );
            }
        }
        for (int i = 1; i < samples.length; i++) {
            Sample current = samples[i];
            int j = i - 1;

            while (j >= 0
                    && samples[j].nanos() > current.nanos()) {
                samples[j + 1] = samples[j];
                j--;
            }
            samples[j + 1] = current;
        }
        Sample median = samples[MEASURED_RUNS / 2];
        writer.printf(
                Locale.ROOT,
                "%s,%s,%s,%d,%.6f,%d,%d,%d%n",
                workload,
                variant,
                structure,
                values.length,
                median.nanos() / 1_000_000.0,
                median.steps(),
                median.moves(),
                median.comparisons()
        );
        writer.flush();
        System.out.printf(
                Locale.ROOT,
                "%s %s %s n=%d: %.3f ms%n",
                workload, variant, structure,
                values.length,
                median.nanos() / 1_000_000.0
        );
    }
    private static Sample runList(
            String workload,
            String variant,
            int[] values,
            int[] indices,
            int[] queries,
            boolean linked
    ) {
        IntList list = linked
                ? new MyLinkedList()
                : new DynamicArray();
        for (int value : values) {
            list.add(value);
        }
        int position = variant.equals("head")
                ? 0
                : values.length / 2;

        Metrics metrics = list.metrics();
        metrics.reset();
        long checksum = 0;
        long start = System.nanoTime();
        switch (workload) {
            case "W1" -> {
                for (int index : indices) {
                    checksum += list.get(index);
                }
            }
            case "W2" -> {
                for (int query : queries) {
                    if (list.contains(query)) {
                        checksum++;
                    }
                }
            }
            case "W3" -> {
                for (int i = 0; i < 1000; i++) {
                    list.add(position, -i - 1);
                }

                for (int i = 0; i < 1000; i++) {
                    checksum += list.remove(position);
                }
            }
            default -> throw new IllegalArgumentException(workload);
        }
        long elapsed = System.nanoTime() - start;

        Sample result = new Sample(
                elapsed,
                metrics.steps,
                metrics.moves,
                metrics.comparisons
        );
        sink = checksum;

        if (workload.equals("W1")) {
            long expected = 0;
            for (int index : indices) {
                expected += values[index];
            }
            if (checksum != expected) {
                throw new IllegalStateException("W1 result mismatch");
            }
        }
        if (workload.equals("W2") && checksum != 500) {
            throw new IllegalStateException("W2 must find 500 queries");
        }
        if (workload.equals("W3")) {
            if (list.size() != values.length || checksum != -500500L) {
                throw new IllegalStateException("W3 result mismatch");
            }
            int[] checks = {0, values.length / 2, values.length - 1};
            for (int index : checks) {
                if (list.get(index) != values[index]) {
                    throw new IllegalStateException(
                            "W3 did not restore original values"
                    );
                }
            }
        }
        return result;
    }
    private static Sample runHeap(int[] values) {
        MinHeap heap = new MinHeap();
        int[] extracted = new int[values.length];
        Metrics metrics = heap.metrics();

        long start = System.nanoTime();
        for (int value : values) {
            heap.insert(value);
        }
        for (int i = 0; i < extracted.length; i++) {
            extracted[i] = heap.extractMin();
        }
        long elapsed = System.nanoTime() - start;

        Sample result = new Sample(
                elapsed,
                metrics.steps,
                metrics.moves,
                metrics.comparisons
        );
        long checksum = 0;

        for (int i = 0; i < extracted.length; i++) {
            if (i > 0 && extracted[i - 1] > extracted[i]) {
                throw new IllegalStateException(
                        "Heap output is not sorted"
                );
            }
            checksum += extracted[i];
        }
        if (heap.size() != 0) {
            throw new IllegalStateException("Heap must be empty");
        }
        sink = checksum;
        return result;
    }
}
