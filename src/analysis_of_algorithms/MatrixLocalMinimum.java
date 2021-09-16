package analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class MatrixLocalMinimum {

    public static int find(int[][] M) {

        return 0;
    }

    public static void main(String[] args) {
        int n = 3; int t = 5;
        int[][] m = new int[n][n];

        for(int b = 0; b < t; ++b) {
            for(int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j)
                    m[i][j] = StdRandom.uniform(0, n*n*n);
            }

            for(int[] c : m) {
                for (int j : c) StdOut.printf("[%2d]", j);
                StdOut.println();
            }
            StdOut.println();
        }
    }
}
