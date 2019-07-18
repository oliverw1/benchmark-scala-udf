import numpy as np
import matplotlib.pyplot as plt

plt.ion()
timings = np.array([int(x[:-2]) for x in s.split()]).reshape(-1, 4)

plt.figure()
for enu, color in enumerate('rgbm'):
    plt.plot(timings[:, enu], c=color)
