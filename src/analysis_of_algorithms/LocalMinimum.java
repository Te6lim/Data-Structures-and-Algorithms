package analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class LocalMinimum {

    public static int find(int[] a) {
        return search(a, 0, a.length - 1);
    }

    private static int search(int[] a, int lo, int hi) {
        int i = (hi + lo) / 2;
        if(i == 0 || a[i - 1] > a[i] && a[i] < a[i + 1] || i == a.length -1)
            return i;
        if(a[i - 1] < a[i + 1])
            return search(a, lo, i - 1);
        if(a[i + 1] < a[i - 1])
            return search(a, i + 1, hi);
        return - 1;
    }

    public static void main(String[] args) {
        int N = 10;
         int[] someValues = new int[10];

         for(int i = 0; i < N; ++i) {
             int value = StdRandom.uniform(-N*2, N*2);
             someValues[i] = value;
         }

        StdOut.print("[");
        for(int c : someValues) {
            StdOut.printf("%3d,", c);
        }
        StdOut.print("]\n");

        StdOut.println("The array has a local minimum at: " + LocalMinimum.find(someValues));
    }
}
