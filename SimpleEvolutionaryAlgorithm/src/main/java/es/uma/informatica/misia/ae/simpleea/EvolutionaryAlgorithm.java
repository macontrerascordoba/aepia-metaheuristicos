package es.uma.informatica.misia.ae.simpleea;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EvolutionaryAlgorithm {
	public static final String MAX_FUNCTION_EVALUATIONS_PARAM = "maxFunctionEvaluations";
	public static final String RANDOM_SEED_PARAM = "randomSeed";
	public static final String POPULATION_SIZE_PARAM = "populationSize";
	
	private Problem problem;
	private int functionEvaluations;
	private int maxFunctionEvaluations;
	private int populationSize;
	private Random rnd;
	
	private Individual bestSolution;
	
	private Selection selection;
	private Replacement replacement;
	private Mutation mutation;
	private Crossover recombination;

	// Uncomment to generate convergence data
	//private FileWriter writer;
	
	public EvolutionaryAlgorithm(Map<String, Double> parameters, Problem problem) {
		configureAlgorithm(parameters, problem);
	}
	
	private void configureAlgorithm(Map<String, Double> parameters, Problem problem) {
		populationSize = parameters.get(POPULATION_SIZE_PARAM).intValue();
		maxFunctionEvaluations = parameters.get(MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
		double bitFlipProb = parameters.get(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM);
		long randomSeed = parameters.get(RANDOM_SEED_PARAM).longValue();
		
		this.problem = problem; 
		
		rnd = new Random(randomSeed);
		
		selection = new BinaryTournament(rnd);
		replacement = new ElitistReplacement();
		mutation = new BitFlipMutation(rnd, bitFlipProb);
		recombination = new SinglePointCrossover(rnd);
	}
	
	public Individual run() throws IOException {
		List<Individual> population = generateInitialPopulation();
		functionEvaluations = 0;
		// Uncomment to generate convergence data
		//writer = new FileWriter("ea_convergence.txt");

		evaluatePopulation(population);
		while (functionEvaluations < maxFunctionEvaluations) {
			Individual parent1 = selection.selectParent(population);
			Individual parent2 = selection.selectParent(population);
			Individual child = recombination.apply(parent1, parent2);
			child = mutation.apply(child);
			evaluateIndividual(child);
			population = replacement.replacement(population, Arrays.asList(child));
		}
		// Uncomment to generate convergence data
		//writer.close();
		
		return bestSolution;
	}
	
	private void evaluateIndividual(Individual individual) throws IOException {
		double fitness = problem.evaluate(individual);
		individual.setFitness(fitness);
		functionEvaluations++;
		checkIfBest(individual);
		// Uncomment to generate convergence data
		//writer.write(functionEvaluations + "," + bestSolution.getFitness() + "\n");
	}

	private void checkIfBest(Individual individual) {
		if (bestSolution == null || individual.getFitness()> bestSolution.getFitness()) {
			bestSolution = individual;
		}
	}

	private void evaluatePopulation(List<Individual> population) throws IOException {
		for (Individual individual: population) {
			evaluateIndividual(individual);
		}
	}

	private List<Individual> generateInitialPopulation() {
		List<Individual> population = new ArrayList<>();
		for (int i=0; i < populationSize; i++) {
			population.add(problem.generateRandomIndividual(rnd));
		}
		return population;
	}
	
}
