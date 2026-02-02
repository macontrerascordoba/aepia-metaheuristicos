import subprocess
import time

def extract_fitness(stdout):
    fitness_line = stdout.split("\n")[1]
    fitness_value = fitness_line.split(" ")[1].split("=")[1].split(",")[0]
    return fitness_value


problem_size = "1024"
problem_file = f"qubo_{problem_size}.csv"
seeds = range(10)

with open("resutls_ea.csv", "w") as f:
    f.write("Algoritm,Seed,Max Iter,Population,BitFlip Prob,Energy,Time (s)\n")
    print("Algoritm: EA")
    for seed in seeds:
        print(f"\t{seed+1}/10", end="", flush=True)
        seed_start = time.time()
        for max_iter in ["50000", "75000", "100000"]:
            for population_size in ["20", "50", "100", "200", "500", "1000"]:
                for bitflip_prob in ["0.0005", "0.001", "0.002", "0.005", "0.01"]:
                    start = time.time()

                    cmd = [
                        "java", "-jar", "SimpleEvolutionaryAlgorithm/target/QUBO-Tests-1.0.jar",
                        "EA", problem_size, problem_file, population_size, bitflip_prob, "100000", str(seed)
                    ]

                    result = subprocess.run(
                        cmd,
                        capture_output=True,
                        text=True
                    )

                    stdout = result.stdout
                    stderr = result.stderr

                    if stderr:
                        print(f"Error in {population_size} {bitflip_prob}:")
                        print(stderr)
                        exit()
                    
                    elapsed = time.time() - start
                    f.write(f"EA,{seed},{max_iter},{population_size},{bitflip_prob},{extract_fitness(stdout)},{elapsed:.3f}\n")
        seed_elapsed = time.time() - seed_start
        print(f"\t{seed_elapsed:.3f}s")


with open("resutls_sa.csv", "w") as f:
    f.write("Algoritm,Seed,Max Iter,Initial Temp,Alpha,Energy,Time (s)")
    print("Algoritm: SA")
    for seed in seeds:
        print(f"\t{seed+1}/10", end="", flush=True)
        seed_start = time.time()
        for max_iter in ["50000", "75000", "100000"]:
            for initial_temp in ["10", "50", "100", "200", "500", "1000", "2000", "5000"]:
                for cooling_rate in ["0.7", "0.8", "0.9", "0.99"]:
                    start = time.time()

                    cmd = [
                        "java", "-jar", "SimpleEvolutionaryAlgorithm/target/QUBO-Tests-1.0.jar",
                        "SA", problem_size, problem_file, initial_temp, cooling_rate, "100000", str(seed)
                    ]

                    result = subprocess.run(
                        cmd,
                        capture_output=True,
                        text=True
                    )

                    stdout = result.stdout
                    stderr = result.stderr

                    if stderr:
                        print(f"Error in {initial_temp} {cooling_rate}:")
                        print(stderr)
                        exit()

                    elapsed = time.time() - start
                    f.write(f"SA,{seed},{max_iter},{initial_temp},{cooling_rate},{extract_fitness(stdout)},{elapsed:.3f}\n")
        seed_elapsed = time.time() - seed_start
        print(f"\t{seed_elapsed:.3f}s")
