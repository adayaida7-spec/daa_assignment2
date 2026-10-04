# DAA Assignment 2 — Data Structures

This project implements DynamicArray, MyLinkedList and MinHeap using primitive `int` values.

It includes operation counters, JUnit 5 tests, four benchmark workloads and performance charts.

## Requirements

- JDK 17 or newer
- Maven 3
- Python 3.9 or newer for charts

Run all commands from the project root, where `pom.xml` is located.

## Run tests

```bash
mvn clean test
```

The tests check insertion, removal, searching, invalid indices, duplicate values, array growth, counters and the heap property. Random operations are compared with Java collections used only in tests.

## Run the benchmark

```bash
mvn compile
java -cp target/classes daa.Benchmark
```

The benchmark writes `results/results.csv`.

It uses sizes 100, 1,000, 10,000 and 100,000, with two warm-up runs and five measured runs per case. The saved time is the median.

Workloads:

- W1: random indexed access.
- W2: searches with 50% present values.
- W3: insertion and removal at the head and middle.
- W4: heap insertion and extraction.

Input is generated using `new Random(42)`. Operation counts are repeatable, but execution times can vary.

## Generate charts

On macOS or Linux:

```bash
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements-plot.txt
.venv/bin/python plot_results.py
```

This creates `W1.png`, `W2.png`, `W3.png` and `W4.png` in `results/plots/`.

## Running in IntelliJ IDEA

Open the project using `pom.xml` and synchronize Maven.

- To run tests, right-click `src/test/java` and run all tests.
- To run measurements, run the `main` method in `Benchmark`.
- Set the benchmark working directory to the project root.

If Maven is not available in the macOS terminal, the installation bundled with IntelliJ IDEA can be used:

```bash
"/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn" clean test
```

## Project files

```text
src/main/java/daa/    Structures, interface, counters and benchmark
src/test/java/daa/    JUnit 5 tests
results/results.csv  Benchmark measurements
results/plots/       Four workload charts
plot_results.py      Chart generation script
requirements-plot.txt Python package versions
REPORT.md            Complexity, proofs and results discussion
```

## Counter rules

- Steps count array reads and reads of list next-links.
- Moves count relocated elements and structural list-reference updates.
- Comparisons count comparisons of element values.

Resizing copies are counted. Validation is excluded from measured counters and time. See `REPORT.md` for details.