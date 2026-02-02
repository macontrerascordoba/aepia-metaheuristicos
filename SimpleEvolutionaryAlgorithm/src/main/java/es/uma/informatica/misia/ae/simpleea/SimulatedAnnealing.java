package es.uma.informatica.misia.ae.simpleea;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class SimulatedAnnealing {

    public static final String MAX_FUNCTION_EVALUATIONS_PARAM = "maxFunctionEvaluations";
    public static final String RANDOM_SEED_PARAM = "randomSeed";
    public static final String INITIAL_TEMPERATURE_PARAM = "initialTemperature";
    public static final String ALPHA_PARAM = "alpha";

    private Problem problem;
    private int functionEvaluations;
    private int maxFunctionEvaluations;
    private double temperature;
    private double alpha;
    private Random rnd;

    // Uncomment to generate convergence data
    //private FileWriter writer;

    private Individual bestSolution;

    public SimulatedAnnealing(Map<String, Double> parameters, Problem problem) {
        configure(parameters, problem);
    }

    private void configure(Map<String, Double> parameters, Problem problem) {
        maxFunctionEvaluations = parameters.get(MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
        temperature = parameters.get(INITIAL_TEMPERATURE_PARAM);
        alpha = parameters.get(ALPHA_PARAM);
        long randomSeed = parameters.get(RANDOM_SEED_PARAM).longValue();

        this.problem = problem;

        rnd = new Random(randomSeed);
    }

    public Individual run() throws IOException {
        functionEvaluations = 0;
        // Uncomment to generate convergence data
        //writer = new FileWriter("sa_convergence.txt");

        Individual currentSolution = problem.generateRandomIndividual(rnd);
        evaluate(currentSolution);

        while (functionEvaluations < maxFunctionEvaluations) {
            BinaryString neighbor = generateNeighbor((BinaryString) currentSolution);

            evaluate(neighbor);

            double delta =
                    neighbor.getFitness() - currentSolution.getFitness();

            if (accept(delta)) {
                currentSolution = neighbor;
            }

            temperature *= alpha;
        }
        // Uncomment to generate convergence data
        //writer.close();

        return bestSolution;
    }

    private void evaluate(Individual individual) throws IOException {
        double fitness = problem.evaluate(individual);
        individual.setFitness(fitness);
        functionEvaluations++;

        if (bestSolution == null ||
                individual.getFitness() > bestSolution.getFitness()) {
            bestSolution = new BinaryString((BinaryString) individual);
        }
        // Uncomment to generate convergence data
        //writer.write(functionEvaluations + "," + bestSolution.getFitness() + "\n");
    }

    private BinaryString generateNeighbor(BinaryString current) {
        BinaryString neighbor = new BinaryString(current);

        byte[] chromosome = neighbor.getChromosome();
        int pos = rnd.nextInt(chromosome.length);

        chromosome[pos] = (byte) (1 - chromosome[pos]);

        return neighbor;
    }

    private boolean accept(double delta) {
        if (delta >= 0) {
            return true;
        }
        double probability = Math.exp(delta / temperature);
        return rnd.nextDouble() < probability;
    }
}
