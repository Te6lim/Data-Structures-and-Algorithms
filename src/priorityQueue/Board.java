package priorityQueue;

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Board {

    private final int[][] tiles;

    private int manhattan;
    private int hamming;

    private Board twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] nTiles) {
        tiles = new int[nTiles.length][nTiles.length];
        int area = (int) Math.pow(tiles.length, 2);
        for (int i = 0; i < nTiles.length; ++i)
            System.arraycopy(nTiles, 0, tiles, 0, nTiles.length);
        setHamming(area);
        setManhattan(area);
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBoard = new StringBuilder();
        stringBoard.append(tiles.length).append("\n");
        int area = (int) Math.pow(tiles.length, 2);
        for (int i = 0; i < area; ++i) {
            String s = String.format(" %d ", tiles[row(i + 1)][col(i + 1)]);
            stringBoard.append(s);
            if ((i + 1) >= tiles.length && (i + 1) % tiles.length == 0) stringBoard.append("\n");
        }
        return stringBoard.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    private void setHamming(int area) {
        hamming = 0;
        for (int i = 1; i <= area; ++i) {
            int tile = tiles[row(i)][col(i)];
            if (tile != 0 && tile != i) ++hamming;
        }
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    private void setManhattan(int area) {
        manhattan = 0;
        for (int i = 1; i <= area; ++i) {
            int tile = tiles[row(i)][col(i)];

            if (tile != 0) {
                if (row(i) != row(tile))
                    manhattan += Math.abs(row(i) - row(tile));
                if (col(i) != col(tile))
                    manhattan += Math.abs(col(i) - col(tile));
            }
        }
    }

    private int getZero() {
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles.length; ++j)
                if (tiles[i][j] == 0) {
                    return (i * tiles.length) + j + 1;
                }
        }
        throw new NoSuchElementException();
    }

    private int row(int i) {
        return (i - 1) / tiles.length;
    }

    private int col(int i) {
        return (i - 1) % tiles.length;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object that) {
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (this == that) return true;
        if (tiles.length == ((Board) that).tiles.length) {
            for (int i = 0; i < tiles.length; ++i) {
                for (int j = 0; j < tiles.length; ++j)
                    if (tiles[i][j] != ((Board) that).tiles[i][j]) return false;
            }
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zero = getZero();
        return getNeighbours(getZero(), neighborCount(zero));
    }

    private int neighborCount(int zero) {
        if ((row(zero) == 0 || row(zero) == tiles.length - 1)
                && (col(zero) == 0 || col(zero) == tiles.length - 1)) {
            return 2;
        } else {
            if (((row(zero) == 0 || row(zero) == tiles.length - 1)
                    && (col(zero) < tiles.length - 1 && col(zero) > 0)) ||
                    ((row(zero) > 0 && row(zero) < tiles.length - 1)
                            && (col(zero) == tiles.length - 1) || (col(zero) == 0))) {
                return 3;
            } else return 4;
        }
    }

    private ArrayList<Board> getNeighbours(int zero, int count) {
        ArrayList<Board> neighbours = new ArrayList<>();
        int i = 0;
        while (neighbours.size() < count) {
            int[][] nTiles = new int[tiles.length][tiles.length];

            for (int j = 0; j < tiles.length; ++j) {
                System.arraycopy(tiles[j], 0, nTiles[j], 0, tiles.length);
            }

            switch (i) {
                case 0 :
                    int up = row(zero) - 1;
                    if (up >= 0) {
                        exch(row(zero), col(zero), up, col(zero), nTiles);
                        neighbours.add(new Board(nTiles));
                    }
                    break;

                case 1 :
                    int down = row(zero) + 1;
                    if (down < nTiles.length) {
                        exch(row(zero), col(zero), down, col(zero), nTiles);
                        neighbours.add(new Board(nTiles));
                    }
                    break;

                case 2 :
                    int left = col(zero) - 1;
                    if (left >= 0) {
                        exch(row(zero), col(zero), row(zero), left, nTiles);
                        neighbours.add(new Board(nTiles));
                    }
                    break;

                case 3 :
                    int right = col(zero) + 1;
                    if (right < nTiles.length) {
                        exch(row(zero), col(zero), row(zero), right, nTiles);
                        neighbours.add(new Board(nTiles));
                    }
                    break;
            }
            ++i;
        }
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin == null) {
            int area = (int) Math.pow(tiles.length, 2);
            int[][] twinTiles = new int[tiles.length][tiles.length];
            int zero = getZero();
            for (int i = 0; i < tiles.length; ++i)
                System.arraycopy(tiles[i], 0, twinTiles[i], 0, tiles.length);

            int p = StdRandom.uniform(1, area + 1);
            int q = StdRandom.uniform(1, area + 1);

            while (p == q || p == zero || q == zero) {
                p = StdRandom.uniform(1, area + 1);
                q = StdRandom.uniform(1, area + 1);
            }

            exch(row(p), col(p), row(q), col(q), twinTiles);
            twin = new Board(twinTiles);
            return twin;
        }
        return twin;
    }

    private void exch(int a, int b, int i, int j, int[][] t) {
        int temp = t[a][b];
        t[a][b] = t[i][j];
        t[i][j] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // Test
    }
}