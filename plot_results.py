import csv
from pathlib import Path

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parent
CSV_PATH = ROOT / "results" / "results.csv"
PLOTS_DIR = ROOT / "results" / "plots"
SIZES = [100, 1000, 10000, 100000]
with CSV_PATH.open(encoding="utf-8", newline="") as file:
    reader = csv.DictReader(file)
    expected_columns = [
        "workload", "variant", "structure", "n",
        "time_ms", "steps", "moves", "comparisons"
    ]
    if reader.fieldnames != expected_columns:
        raise ValueError("Unexpected CSV columns")

    rows = list(reader)
expected_cases = set()

for n in SIZES:
    for structure in ("DynamicArray", "MyLinkedList"):
        expected_cases.add(("W1", "-", structure, n))
        expected_cases.add(("W2", "-", structure, n))
        expected_cases.add(("W3", "head", structure, n))
        expected_cases.add(("W3", "middle", structure, n))

    expected_cases.add(("W4", "-", "MinHeap", n))
actual_cases = [
    (row["workload"], row["variant"], row["structure"], int(row["n"]))
    for row in rows
]
if len(rows) != 36 or set(actual_cases) != expected_cases:
    raise ValueError("Expected all 36 benchmark cases exactly once")

PLOTS_DIR.mkdir(parents=True, exist_ok=True)
metrics = [
    ("time_ms", "Median time", "Time (ms)"),
    ("steps", "Array reads / next-link reads", "Steps (count)"),
    ("moves", "Element moves / link updates", "Moves (count)"),
    ("comparisons", "Element comparisons", "Comparisons (count)")
]
titles = {
    "W1": "W1: 10,000 random accesses",
    "W2": "W2: 1,000 searches, 50% present",
    "W3": "W3: 1,000 insertions then 1,000 removals",
    "W4": "W4: n heap insertions then n extractions"
}
colors = {
    "DynamicArray": "#2563eb",
    "MyLinkedList": "#ea580c",
    "MinHeap": "#059669"
}
for workload in ("W1", "W2", "W3", "W4"):
    selected = [
        row for row in rows
        if row["workload"] == workload
    ]
    groups = sorted({
        (row["structure"], row["variant"])
        for row in selected
    })
    fig, axes = plt.subplots(2, 2, figsize=(13, 8))
    fig.suptitle(titles[workload], fontsize=15)

    for ax, (metric, title, ylabel) in zip(axes.flat, metrics):
        for structure, variant in groups:
            series = sorted(
                [
                    row for row in selected
                    if row["structure"] == structure
                    and row["variant"] == variant
                ],
                key=lambda row: int(row["n"])
            )

            x = [int(row["n"]) for row in series]
            y = [float(row[metric]) for row in series]

            label = structure
            if variant != "-":
                label += f" ({variant})"

            ax.plot(
                x,
                y,
                label=label,
                color=colors[structure],
                linestyle="--" if variant == "middle" else "-",
                marker="s" if variant == "middle" else "o",
                linewidth=2
            )
        ax.set_title(title)
        ax.set_xlabel("Initial number of elements (n)")
        ax.set_ylabel(ylabel)
        ax.set_xscale("log")
        ax.set_xticks(SIZES)
        ax.set_xticklabels(["100", "1,000", "10,000", "100,000"])
        ax.set_ylim(bottom=0)
        ax.grid(True, alpha=0.25)
        ax.legend(fontsize=8)

    fig.tight_layout(rect=(0, 0, 1, 0.95))

    destination = PLOTS_DIR / f"{workload}.png"
    fig.savefig(destination, dpi=180)
    plt.close(fig)
    print(f"Saved: {destination}")

print("Done: 36 CSV rows checked, 4 charts created.")