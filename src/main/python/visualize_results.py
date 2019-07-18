import matplotlib.pyplot as plt
import numpy as np
from pathlib import Path

plt.ion()
results_path = Path(__file__).parent / "results.log"


def extract_timings():
   with open(results_path) as fh:
        for line in fh:
            if line.startswith("Elapsed time"):
                yield int(line.split(":", 1)[-1][:-3])


timings = np.fromiter(extract_timings(), np.uint64).reshape(-1, 4)

plt.figure()
for enu, color in enumerate('rgbm'):
    plt.plot(timings[:, enu], c=color)
plt.savefig(results_path.with_suffix(".png"))
