package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;
import java.util.List;

public class QUBOProblem implements Problem {

    private final int n;
    private final QUBOInstance qubo;

    public QUBOProblem(QUBOInstance qubo) {
        this.n = qubo.getN();
        this.qubo = qubo;
    }

    @Override
    public double evaluate(Individual individual) {
        BinaryString bin = (BinaryString) individual;
        byte[] x = bin.getChromosome();

        double energy = 0.0;
        List<QUBOTerm> terms = qubo.getTerms();
        for (QUBOTerm term : terms) {
            if (term.i <= term.j) {
                energy += term.value * x[term.i] * x[term.j];
            }
        }

        return energy;
    }

    @Override
    public BinaryString generateRandomIndividual(Random rnd) {
        return new BinaryString(n, rnd);
    }

    public int getProblemSize() {
        return n;
    }
}
