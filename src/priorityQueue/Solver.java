package priorityQueue;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private final ArrayList<Board> solutionSequence;
    private final boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        validateBoard(initial);
        isSolvable = isSolvable(initial, initial.twin());
        if (isSolvable) solutionSequence = aStarSearch(initial);
        else solutionSequence = new ArrayList<>();
    }

    private ArrayList<Board> aStarSearch(Board board) {
        SearchNode node = new SearchNode(board, null);
        MinPQ<SearchNode> pQ = new MinPQ<>();
        pQ.insert(node);
        node = pQ.delMin();

        while (!node.board.isGoal()) {
            addNeighborsToPQ(pQ, node);
            node = pQ.delMin();
        }
        return solutionExtract(node);
    }

    private ArrayList<Board> solutionExtract(SearchNode node) {
        SearchNode n = node;
        ArrayList<Board> solution = new ArrayList<>();
        solution.add(n.board);
        while (n.parent != null) {
            solution.add(n.parent.board);
            n = n.parent;
        }
        return solution;
    }

    private void addNeighborsToPQ(MinPQ<SearchNode> priorityQueue, SearchNode min) {
        if (min.parent != null) {
            for (Board n : min.board.neighbors())
                if (!n.equals(min.parent.board)) priorityQueue.insert(new SearchNode(n, min));
        } else {
            for (Board n : min.board.neighbors()) priorityQueue.insert(new SearchNode(n, min));
        }
    }

    private void validateBoard(Board board) {
        if (board == null) throw new IllegalArgumentException();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }


    private boolean isSolvable(Board initial, Board twin) {
        SearchNode node = new SearchNode(initial, null);
        MinPQ<SearchNode> pQ = new MinPQ<>();
        pQ.insert(node);
        node = pQ.delMin();

        SearchNode twinNode = new SearchNode(twin, null);
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        twinPQ.insert(twinNode);
        twinNode = twinPQ.delMin();

        boolean toggle = false;
        while (true) {
            if (toggle) {
                addNeighborsToPQ(pQ, node);
                node = pQ.delMin();
                if (node.board.isGoal()) return true;
                toggle = false;
            } else {
                addNeighborsToPQ(twinPQ, twinNode);
                twinNode = twinPQ.delMin();
                if (twinNode.board.isGoal()) return false;
                toggle = true;
            }
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutionSequence.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable) return new Solution();
        else return null;
    }

    private class Solution implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }

        private class SolutionIterator implements Iterator<Board> {
            int current = solutionSequence.size() - 1;
            @Override
            public boolean hasNext() {
                return current > -1;
            }

            @Override
            public Board next() {
                if (current == -1) throw new NoSuchElementException();
                return solutionSequence.get(current--);
            }
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int numberOfMoves;
        private final SearchNode parent;
        private final int manhattan;

        public SearchNode(Board sBoard, SearchNode sParent) {
            board = sBoard;
            parent = sParent;
            manhattan = sBoard.manhattan();
            if (parent != null)
                numberOfMoves = parent.numberOfMoves + 1;
            else numberOfMoves = 0;
        }

        private int manhattanPriority() {
            return manhattan + numberOfMoves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(manhattanPriority(), that.manhattanPriority());
        }

        @Override
        public String toString() {
            return board +
                    "manhattan= " +
                    board.manhattan() + "\n" +
                    "moves= " +
                    numberOfMoves + "\n" +
                    "priority= " +
                    manhattanPriority();
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        /* /int[][] tiles = new int[3][3];

        tiles[0][0] = 2;
        tiles[0][1] = 8;
        tiles[0][2] = 5;
        tiles[1][0] = 3;
        tiles[1][1] = 6;
        tiles[1][2] = 1;
        tiles[2][0] = 7;
        tiles[2][1] = 0;
        tiles[2][2] = 4;

        tiles[0][0] = 8;
        tiles[0][1] = 2;
        tiles[0][2] = 3;
        tiles[1][0] = 0;
        tiles[1][1] = 4;
        tiles[1][2] = 7;
        tiles[2][0] = 6;
        tiles[2][1] = 5;
        tiles[2][2] = 1;

        tiles[0][0] = 1;
        tiles[0][1] = 0;
        tiles[0][2] = 2;
        tiles[1][0] = 7;
        tiles[1][1] = 5;
        tiles[1][2] = 4;
        tiles[2][0] = 8;
        tiles[2][1] = 6;
        tiles[2][2] = 3;

        tiles[0][0] = 8;
        tiles[0][1] = 1;
        tiles[0][2] = 3;
        tiles[1][0] = 4;
        tiles[1][1] = 0;
        tiles[1][2] = 2;
        tiles[2][0] = 7;
        tiles[2][1] = 6;
        tiles[2][2] = 5;

        tiles[0][0] = 0;
        tiles[0][1] = 1;
        tiles[0][2] = 3;
        tiles[1][0] = 4;
        tiles[1][1] = 2;
        tiles[1][2] = 5;
        tiles[2][0] = 7;
        tiles[2][1] = 8;
        tiles[2][2] = 6;*/
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println();
            }
        }
    }
}