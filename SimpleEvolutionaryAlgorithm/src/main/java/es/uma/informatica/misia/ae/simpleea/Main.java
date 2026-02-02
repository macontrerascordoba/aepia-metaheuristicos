package es.uma.informatica.misia.ae.simpleea;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main (String args []) {
		String algorithm = "";
		if (args.length > 0) {
			algorithm = args[0];
			if (!algorithm.equals("SA") && !algorithm.equals("EA") && !algorithm.equals("BF")) {
				System.out.println("\nAlgorithm " + algorithm + " not recognized.");
				printHelp();
				return;
			}
		} else {
			System.out.println("\nInvalid number of arguments");
			printHelp();
			return;
		}

		if (args.length < 3 && algorithm.equals("BF")) {
			System.out.println("\nInvalid number of arguments for " + algorithm + " algorithm");
			printHelp();
			return;
		}
		if (args.length < 6 && !algorithm.equals("BF")) {
			System.out.println("\nInvalid number of arguments for " + algorithm + " algorithm");
			printHelp();
			return;
		}

		try {
			int n = Integer.parseInt(args[1]);
			String quboFile = args[2];

			//Problem problem = new Onemax(n);
			// Load QUBO instance
			QUBOInstance quboInstance = new QUBOInstance(n, quboFile);
			Problem problem = new QUBOProblem(quboInstance);

			Map<String, Double> parameters = new HashMap<>();
			if (!algorithm.equals("BF")) {
				parameters = readEAParameters(algorithm, args);
			}

			if (algorithm.equals("EA")) {
				EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(parameters, problem);

				Individual bestSolution = ea.run();
				System.out.println("=== EA best solution ===");
				System.out.println(bestSolution);
			} else if (algorithm.equals("SA")) {
				SimulatedAnnealing sa = new SimulatedAnnealing(parameters, problem);

				Individual bestSA = sa.run();
				System.out.println("=== SA best solution ===");
				System.out.println(bestSA);
			} else {
				bruteForceQUBO(problem, n);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, Double> readEAParameters(String algorithm, String[] args) {
		Map<String, Double> parameters = new HashMap<>();
		long randomSeed = System.currentTimeMillis();
		if (args.length > 6) {
			randomSeed = Long.parseLong(args[6]);
		}
		if (algorithm.equals("EA")) {
			parameters.put(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, Double.parseDouble(args[3]));
			parameters.put(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, Double.parseDouble(args[4]));
			parameters.put(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM, Double.parseDouble(args[5]));
			parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double)randomSeed);
		} else {
			parameters.put(SimulatedAnnealing.INITIAL_TEMPERATURE_PARAM, Double.parseDouble(args[3]));
			parameters.put(SimulatedAnnealing.ALPHA_PARAM, Double.parseDouble(args[4]));
			parameters.put(SimulatedAnnealing.MAX_FUNCTION_EVALUATIONS_PARAM, Double.parseDouble(args[5]));
			parameters.put(SimulatedAnnealing.RANDOM_SEED_PARAM, (double)randomSeed);
		}
		return parameters;
	}

	public static void bruteForceQUBO(Problem problem, int n) {
		double bestEnergy = Double.NEGATIVE_INFINITY;

		if (n > 31) {
			// An n > 31, when 2 is powered to n would surpass Integer maximum size
			System.err.println("The execution of Brute Force is not viable with a problem size > 31");
		}

		byte[] x = new byte[n];
		for (int i = 0; i < (int) Math.pow(2, n); i++) {
			BinaryString ind = new BinaryString(n);
			ind.setChromosome(x.clone());

			double energy = problem.evaluate(ind);

			if (energy > bestEnergy) {
				bestEnergy = energy;
			}

			System.out.println("Bruteforce " + (i+1) + "/" + (int)Math.pow(2, n) + " -> Energy: " + energy);

			// Increment binary string x by 1
			for (int j = n-1; j >= 0; j--) {
				if (x[j] == 0) {
					x[j] = 1;
					break;
				} else {
					x[j] = 0;
				}
			}
		}
		System.out.println("Bruteforce best energy = " + bestEnergy);
	}

	public static void printHelp() {
		System.out.println("\nUsage:");
		System.out.println("\tjava -jar <pathToJAR> <algorithm> <algorithmSpecificParameters>");
		System.out.println("\nAlgorithms:");
		System.out.println("\t- BF (Brute Force): java -jar <pathToJAR> <algorithm> <problem size> <qubo file>");
		System.out.println("\n\t- EA (Evolutionary Algorithm): java -jar <pathToJAR> <algorithm> <problem size> " +
				"<qubo file> <population size> <bitflip probability> <function evaluations> [<random seed>]");
		System.out.println("\n\t- SA (Simulated Annealing): java -jar <pathToJAR> <algorithm> <problem size> " +
				"<qubo file> <initial temperature> <alpha> <function evaluations> [<random seed>]");
	}
}
