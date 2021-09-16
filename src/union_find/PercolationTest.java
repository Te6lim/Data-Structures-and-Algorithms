package union_find;

import edu.princeton.cs.algs4.StdOut;

public class PercolationTest {
    public static void main(String[] args) {
        int N = 200;
        int T = 100;

        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println("Mean is: " + percolationStats.mean());
        StdOut.println("Standard deviation is: " + percolationStats.stddev());
        StdOut.println("95% confidence interval: [" + percolationStats.confidenceLo() + ", " +
                percolationStats.confidenceHi() + "]");

        /*/double prev = percolationStats.runningTime(1);
        double time;
        for(int M = 2; true; M+=M) {
            time = percolationStats.runningTime(M);
            StdOut.printf("%6d %7.1f ", M*M, time);
            StdOut.printf("%5.1f\n", time/prev);
            prev = time;
        }*/

        /*/union_find.Percolation percolation = new union_find.Percolation(N);
        percolation.open(3,2);
        percolation.open(3,3);
        percolation.open(5,2);
        percolation.open(5,5);
        percolation.open(3,5);
        //percolation.open(2,1);
        percolation.open(1,5);
        percolation.open(3,1);
        percolation.open(5,1);
        percolation.open(5,3);
        //percolation.open(1,3);
        percolation.open(4,2);
        //percolation.open(1,4);
        percolation.open(1,1);
        percolation.open(2,5);
        percolation.open(3,4);
        //percolation.open(2,3);

        if(percolation.percolates()) StdOut.println("The system percolates!");
        else StdOut.println("The system does not percolate!");

        int row = 3, col = 2;
        int position = ((row-1) * N) + (col-1);
        if(percolation.isFull(row, col))
            StdOut.println(position + " is a full site!");
        else StdOut.println(position + " is not a full site!");*/
    }
}
