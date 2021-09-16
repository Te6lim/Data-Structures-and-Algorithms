package union_find;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF virtual;

    private final boolean[][] grid;
    private final boolean[][] isFull;

    private int n;

    private int virtualTop;
    private int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        validateSize(n);
        grid = new boolean[n][n];
        isFull = new boolean[n][n];

        int area = (int) Math.pow(n, 2);

        uf = new WeightedQuickUnionUF(area);
        virtual = new WeightedQuickUnionUF(area);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateSite(row, col);
        if (!isOpen(row, col)) {
            int newRow = row - 1;
            int newCol = col - 1;
            int position = getValue(newRow, newCol);
            grid[newRow][newCol] = true;

            if (position <= grid.length) {
                isFull[newRow][newCol] = true;

                if (virtualTop == 0) virtualTop = position;
                else virtual.union(position - 1, virtualTop - 1);
            }

            if (position > grid.length * (grid.length - 1)) {
                if (virtualBottom == 0) virtualBottom = position;
                else virtual.union(position - 1, virtualBottom - 1);
            }

            ++n;

            if (newCol + 1 < grid.length && isOpen(row, col + 1)) {
                union(newRow, newCol, newRow, newCol + 1);
            }
            if (newRow - 1 >= 0 && isOpen(row - 1, col)) {
                union(newRow, newCol, newRow - 1, newCol);
            }
            if (newCol - 1 >= 0 && isOpen(row, col - 1)) {
                union(newRow, newCol, newRow, newCol - 1);
            }
            if (newRow + 1 < grid.length && isOpen(row + 1, col)) {
                union(newRow, newCol, newRow + 1, newCol);
            }

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateSite(row, col);
        if (isOpen(row, col)) {
            int root = uf.find(getValue(row - 1, col - 1) - 1);
            return isFull[row(root)][col(root)];
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return n;
    }

    // does the system percolate?
    public boolean percolates() {
        if (virtualTop != 0 && virtualBottom != 0)
            return virtual.find(virtualTop - 1) == virtual.find(virtualBottom - 1);
        return false;
    }

    private void union(int a, int b, int i, int j) {
        int rootP = uf.find(getValue(a, b) - 1);
        int rootQ = uf.find(getValue(i, j) - 1);

        uf.union(getValue(a, b) - 1, getValue(i, j) - 1);
        virtual.union(getValue(a, b) - 1, getValue(i, j) - 1);

        boolean pIsFull = isFull[row(rootP)][col(rootP)];
        boolean qIsFull = isFull[row(rootQ)][col(rootQ)];

        if (qIsFull && !pIsFull)
            isFull[row(rootP)][col(rootP)] = true;
        else if (pIsFull && !qIsFull)
            isFull[row(rootQ)][col(rootQ)] = true;
    }

    private void validateSite(int i, int j) {
        if (i < 1 || i > grid.length || j > grid.length || j < 1)
            throw new IllegalArgumentException();
    }

    private void validateSize(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
    }

    private int row(int c) {
        return c / grid.length;
    }

    private int col(int c) {
        return c % grid.length;
    }

    private int getValue(int row, int col) {
        return (row * grid.length) + col + 1;
    }
}