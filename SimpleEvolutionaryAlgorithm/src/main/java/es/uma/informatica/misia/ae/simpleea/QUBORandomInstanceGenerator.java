package es.uma.informatica.misia.ae.simpleea;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class QUBORandomInstanceGenerator    {

    static final int N = 1024;
    static final int MIN = -10;
    static final int MAX = 10;

    // Probabilidad de que exista un término Qij (densidad)
    static final double DENSITY = 0.05; // 5% de los pares

    public static void main(String[] args) throws IOException {
        Random rand = new Random(0); // Fixed Seed
        FileWriter writer = new FileWriter("qubo_1024.csv");

        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {

                // Decidir si este término existe
                if (rand.nextDouble() < DENSITY) {
                    int value = rand.nextInt(MAX - MIN + 1) + MIN;

                    // Evitar coeficientes 0
                    if (value != 0) {
                        writer.write(i + "," + j + "," + value + "\n");
                    }
                }
            }
        }

        writer.close();
        System.out.println("Instancia QUBO 1024 en formato Hiroshima generada.");
    }
}