package es.uma.informatica.misia.ae.simpleea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QUBOInstance {

    private final int n;
    private final List<QUBOTerm> terms;

    public QUBOInstance(int n) {
        this.n = n;
        this.terms = new ArrayList<>();
    }

    public QUBOInstance(int n, String filename) throws IOException {
        this(n);
        loadFromFile(filename);
    }

    private void loadFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                int i = Integer.parseInt(parts[0].trim());
                int j = Integer.parseInt(parts[1].trim());
                double value = Double.parseDouble(parts[2].trim());

                if (value != 0.0) {
                    addTerm(i, j, value);
                }
            }
        }
    }

    public int getN() {
        return n;
    }

    public List<QUBOTerm> getTerms() {
        return terms;
    }

    public void addTerm(int i, int j, double value) {
        terms.add(new QUBOTerm(i, j, value));
    }
}