package analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class ClosestPair {

    public static double[] find(double[] array) {
        Arrays.sort(array);
        double prev = Math.abs(array[1] - array[0]);
        double result;
        double[] pair = new double[2];
        for(int i = 1; i < array.length; ++i)
            if(i + 1 < array.length) {
                result = Math.abs(array[i+1] - array[i]);
                if(result < prev) {
                    prev = result;
                    pair[0] = array[i]; pair[1] = array[i + 1];
                }
            }
        return pair;
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

        for(double c: ClosestPair.find(someValues)) {
            StdOut.printf("%f,", c);
        }
        StdOut.println();
    }
}