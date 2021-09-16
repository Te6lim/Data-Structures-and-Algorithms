package analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;

public class FibonacciSearch {

    public static int search(int[] a, int key) {
        return find(a, key, 0, a.length);
    }

    private static int find(int[] a, int key, int M, int N) {
        int i = 0, f = 0, f2, f1 = 0;
        while(f < N) {
            f1 = f;
            f = fib(++i);
        }
        f2 = f - f1;
        if(f <= 1) return -1;
        if(key == a[M + f2 - 1])
            return M + f2 - 1;
        if(key < a[M + f2 - 1])
            return find(a, key, M, f2);
        if(key > a[M + f2 - 1])
            return find(a, key, M + f2, N - f2);

        return -1;
    }

    private static int fib(int N) {
        int i = 0;
        int x1 = 0;
        int x2 = x1++;
        int temp;
        while(i < N) {
            temp = x2;
            x2 += x1;
            x1 = temp;
            ++i;
        }
        return x2;
    }

    public static void main(String[] args) {
        int[] s = {2, 4, 5, 6, 10, 15, 19, 22, 25, 34, 39, 40, 58,63};
        StdOut.println(search(s, 19));
        //find(s, 4, 0, s.length - 1);
    }
}