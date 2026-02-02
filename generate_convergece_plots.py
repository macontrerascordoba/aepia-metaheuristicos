import pandas as pd
import matplotlib.pyplot as plt

# --------------------------------------------------
# Load convergence files (NO header)
# --------------------------------------------------
ea = pd.read_csv("ea_convergence.txt", header=None)
sa = pd.read_csv("sa_convergence.txt", header=None)

# Rename columns for clarity
ea.columns = ["Iter", "Energy"]
sa.columns = ["Iter", "Energy"]

# --------------------------------------------------
# Plot
# --------------------------------------------------
plt.figure(figsize=(8, 5))

plt.plot(ea["Iter"], ea["Energy"], label="EA", linewidth=2)
plt.plot(sa["Iter"], sa["Energy"], label="SA", linewidth=2)

plt.xlabel("Iterations")
plt.ylabel("Energy")
plt.title("Convergence comparison: EA vs SA")

plt.legend()
plt.grid(axis="y", linestyle="--", alpha=0.3)

plt.tight_layout()
plt.savefig("convergence_ea_vs_sa.png", dpi=300)
plt.show()
