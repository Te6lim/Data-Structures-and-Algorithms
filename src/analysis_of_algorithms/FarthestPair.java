package analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class FarthestPair {

    public static double[] find(double[] array) {
        double prevL = array[0], prevH = array[0];
        double[] result = new double[2];
        for(int i = 1; i < array.length; ++i) {
            if(array[i] < prevL)
                result[0] = prevL = array[i];

            if(array[i] > prevH)
                result[1] = prevH = array[i];
        }
        return result;
    }

    public static void main(String[] args) {
        int N = 10;
        double[] someValues = new double[N];
        for(int i = 0; i < N; ++i) {
            double value = StdRandom.uniform(-N*2f, N*2f);
            someValues[i] = value;
        }

        StdOut.print("[");
        for(double c : someValues) {
            StdOut.printf("%3.4f,", c);
        }
        StdOut.print("]\n");

        for(double c: FarthestPair.find(someValues)) {
            StdOut.printf("%f,", c);
        }
        StdOut.println();

        Arrays.sort(someValues);
        StdOut.print("[");
        for(double c : someValues) {
            StdOut.printf("%3.4f,", c);
        }
        StdOut.print("]\n");
    }
}