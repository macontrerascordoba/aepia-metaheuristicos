import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import os

algorithms = {
    "ea": ["Population", "BitFlip Prob"],
    "sa": ["Initial Temp", "Alpha"]
}

for algorithm, params in algorithms.items():
    df = pd.read_csv(f'resutls_{algorithm}.csv')
    df = df.drop('Algoritm', axis=1)

    os.makedirs(algorithm, exist_ok=True)

    unique_max_iters = sorted(df['Max Iter'].unique())
    param2_values = sorted(df[params[1]].unique())

    for max_iter in unique_max_iters:
        subset = df[df['Max Iter'] == max_iter]

        # Convert X values to sorted array
        x_values = sorted(subset[params[0]].unique())
        x_indices = np.arange(len(x_values))

        offset_width = 0.15
        offsets = np.linspace(
            -offset_width * (len(param2_values) - 1) / 2,
             offset_width * (len(param2_values) - 1) / 2,
            len(param2_values)
        )

        # ==============================================================
        # ENERGY vs X (POINT PLOT)
        # ==============================================================
        plt.figure()

        for offset, p2 in zip(offsets, param2_values):
            p2_data = subset[subset[params[1]] == p2]

            stats = (
                p2_data
                .groupby(params[0])['Energy']
                .agg(['mean', 'std'])
                .reindex(x_values)
                .reset_index()
            )

            plt.errorbar(
                x_indices + offset,
                stats['mean'],
                yerr=stats['std'],
                fmt='o',
                capsize=3,
                label=f'{params[1]} = {p2}'
            )

        plt.xticks(x_indices, x_values)
        plt.xlabel(params[0])
        plt.ylabel('Energy')
        plt.title(f'Energy vs {params[0]} (Max Iter = {max_iter})')
        plt.legend()
        plt.grid(axis='y', linestyle='--', alpha=0.3)


        x_name = params[0].lower().replace(" ", "_")
        energy_fig = f'{algorithm}/energy_vs_{x_name}_max_iter_{max_iter}.png'
        plt.savefig(energy_fig, dpi=300, bbox_inches='tight')
        plt.close()

        # ==============================================================
        # TIME vs X (POINT PLOT)
        # ==============================================================
        plt.figure()

        for offset, p2 in zip(offsets, param2_values):
            p2_data = subset[subset[params[1]] == p2]

            stats = (
                p2_data
                .groupby(params[0])['Time (s)']
                .agg(['mean', 'std'])
                .reindex(x_values)
                .reset_index()
            )

            plt.errorbar(
                x_indices + offset,
                stats['mean'],
                yerr=stats['std'],
                fmt='o',
                capsize=3,
                label=f'{params[1]} = {p2}'
            )

        plt.xticks(x_indices, x_values)
        plt.xlabel(params[0])
        plt.ylabel('Time (s)')
        plt.title(f'Time vs {params[0]} (Max Iter = {max_iter})')
        plt.legend()
        plt.grid(axis='y', linestyle='--', alpha=0.3)

        time_fig = f'{algorithm}/time_vs_{x_name}_max_iter_{max_iter}.png'
        plt.savefig(time_fig, dpi=300, bbox_inches='tight')
        plt.close()

        print(f"Saved: {energy_fig}")
        print(f"Saved: {time_fig}")
