package union_find;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int n;
    private final int trials;
    private static final double P = 1.96;

    private double mean;
    private double stddev;
    private final double[] thresholds;

    public PercolationStats(int n, int trials) {
        validate(n, trials);
        this.n = n;
        this.trials = trials;
        thresholds = new double[trials];
    }

    // sample mean of percolation threshold
    public double mean() {
        if (stddev <= 0f)
            for (int i = 0; i < trials; ++i)
                thresholds[i] = findThreshold();

        return mean = StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (mean <= 0f)
            for (int i = 0; i < trials; ++i)
                thresholds[i] = findThreshold();
        return stddev = StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        checkMeanAndStddev();
        return mean - ((P * stddev) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        checkMeanAndStddev();
        return mean + ((P * stddev) / Math.sqrt(trials));
    }

    private void checkMeanAndStddev() {
        if (stddev <= 0f)
            stddev = stddev();
        if (mean <= 0f)
            mean = mean();
    }

    private double findThreshold() {
        int size = n;
        Percolation perc = new Percolation(n);
        double gridArea = Math.pow(size, 2);
        while (!perc.percolates()) {
            int row = StdRandom.uniform(1, size + 1);
            int col = StdRandom.uniform(1, size + 1);
            if (!perc.isOpen(row, col))
                perc.open(row, col);
        }
        return perc.numberOfOpenSites() / gridArea;
    }

    /*/public double runningTime(int N) {
        perc = new union_find.Percolation(N);
        Stopwatch timer = new Stopwatch();
        while(!perc.percolates()) {
            int row = StdRandom.uniform(1, N + 1);
            int col = StdRandom.uniform(1, N + 1);
            if(!perc.isOpen(row, col)) {
                perc.open(row, col);
            }
        }
        return timer.elapsedTime();
    }*/

    private void validate(int n, int t) {
        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.println("mean = " + percolationStats.mean());
        StdOut.println("stddev = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " +
                percolationStats.confidenceHi() + "]");
    }
}