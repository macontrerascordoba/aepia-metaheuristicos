import pandas as pd

algorithms = {
    "ea": ["Population", "BitFlip Prob"],
    "sa": ["Initial Temp", "Alpha"]
}

for algorithm, params in algorithms.items():
    df = pd.read_csv(f'resutls_{algorithm}.csv')
    df = df.drop('Algoritm', axis=1)

    unique_max_iters = sorted(df['Max Iter'].unique())

    for max_iter in unique_max_iters:
        print("\n" + "=" * 80)
        print(f"Max Iter = {max_iter}")
        print("=" * 80)

        subset = df[df['Max Iter'] == max_iter]

        # ==============================================================
        # ENERGY TABLE
        # ==============================================================
        energy_stats = (
            subset
            .groupby([params[0], params[1]])['Energy']
            .agg(['mean', 'std'])
            .reset_index()
        )

        energy_stats['Energy'] = energy_stats.apply(
            lambda row: f"{row['mean']:.2f} $\\pm$ {row['std']:.2f}", axis=1
        )

        energy_table = energy_stats.pivot(
            index=params[0],
            columns=params[1],
            values='Energy'
        )

        energy_csv = f"{algorithm}/results_energy_max_iter_{max_iter}.csv"
        energy_tex = f"{algorithm}/table_energy_max_iter_{max_iter}.tex"

        energy_table.to_csv(energy_csv)

        energy_table.to_latex(
            energy_tex,
            escape=False,
            column_format='l' + 'c' * len(energy_table.columns),
            caption=f"Energy results (mean $\\pm$ std) for Max Iter = {max_iter}",
            label=f"tab:energy_max_iter_{max_iter}"
        )

        print(f"Saved: {energy_csv}")
        print(f"Saved: {energy_tex}")

        # ==============================================================
        # TIME TABLE
        # ==============================================================
        time_stats = (
            subset
            .groupby([params[0], params[1]])['Time (s)']
            .agg(['mean', 'std'])
            .reset_index()
        )

        time_stats['Time (s)'] = time_stats.apply(
            lambda row: f"{row['mean']:.3f} $\\pm$ {row['std']:.3f}", axis=1
        )

        time_table = time_stats.pivot(
            index=params[0],
            columns=params[1],
            values='Time (s)'
        )

        time_csv = f"{algorithm}/results_time_max_iter_{max_iter}.csv"
        time_tex = f"{algorithm}/table_time_max_iter_{max_iter}.tex"

        time_table.to_csv(time_csv)

        time_table.to_latex(
            time_tex,
            escape=False,
            column_format='l' + 'c' * len(time_table.columns),
            caption=f"Runtime results (mean $\\pm$ std) for Max Iter = {max_iter}",
            label=f"tab:time_max_iter_{max_iter}"
        )

        print(f"Saved: {time_csv}")
        print(f"Saved: {time_tex}")
