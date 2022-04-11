package directed_graphs;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SAP {

    private final Digraph diGraph;
    private boolean[] isVertexMarked;
    private int[] childCount;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        diGraph = G;
        isVertexMarked = new boolean[diGraph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        resetChildCount();
        resetMarkedVertices();
        int ancestorPosition = findCommonAncestor(v, w);
        if (ancestorPosition >= 0) return childCount[ancestorPosition];
        return ancestorPosition;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        resetChildCount();
        resetMarkedVertices();
        return findCommonAncestor(v, w);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        ArrayList<Integer> ancestors = new ArrayList<>(), lengths = new ArrayList<>();
        int ancestor;
        for (Integer i : v) {
            for (Integer j : w) {
                resetChildCount();
                resetMarkedVertices();
                ancestor = findCommonAncestor(i, j);
                if (ancestor >= 0) {
                    if (Collections.binarySearch(ancestors, ancestor) < 0) {
                        ancestors.add(ancestor);
                        lengths.add(childCount[ancestor]);
                    }
                }
            }
        }
        Collections.sort(lengths);
        if (lengths.isEmpty()) return -1; else return lengths.get(0);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        ArrayList<Integer> ancestors = new ArrayList<>(), lengths = new ArrayList<>();
        HashMap<Integer, Integer> lengthHashMap = new HashMap<>();
        int ancestor;
        for (Integer i : v) {
            for (Integer j : w) {
                resetChildCount();
                resetMarkedVertices();
                ancestor = findCommonAncestor(i, j);
                if (ancestor >= 0) {
                    if (Collections.binarySearch(ancestors, ancestor) < 0) {
                        ancestors.add(ancestor);
                        lengths.add(childCount[ancestor]);
                        lengthHashMap.put(childCount[ancestor], ancestor);
                    }
                }
            }
        }
        Collections.sort(lengths);
        if (lengths.isEmpty()) return -1; else return lengthHashMap.get(lengths.get(0));
    }

    private int findCommonAncestor(int v, int w) {
        Queue<Integer> A = new Queue<>();
        Queue<Integer> B = new Queue<>();

        boolean toggleToV = true;

        A.enqueue(v);
        B.enqueue(w);

        markVertex(v);
        markVertex(w);

        while (true) {
            int x;
            if (toggleToV) {
                x = findCommonAncestor(A);
                if (x != -1) return x;
                toggleToV = false;
            } else {
                x = findCommonAncestor(B);
                if (x != -1) return x;
                toggleToV = true;
            }
        }
    }

    private int findCommonAncestor(Queue<Integer> a) {
        int d;
        d = a.dequeue();
        if (diGraph.outdegree(d) > 0) return enqueueNeighboursOfDAndReturnIfMarked(a, d);
        return -1;
    }

    private int enqueueNeighboursOfDAndReturnIfMarked(Queue<Integer> a, int d) {
        for (int x : diGraph.adj(d)) {
            childCount[x] += childCount[d] + 1;
            if (isVertexMarked[x]) return x;
            else {
                a.enqueue(x);
                markVertex(x);
            }
        }
        return -1;
    }

    private void markVertex(int v) {
        isVertexMarked[v] = true;
    }

    private void resetMarkedVertices() {
        isVertexMarked = new boolean[diGraph.V()];
    }

    private void resetChildCount() {
        childCount = new int[diGraph.V()];
    }

    // do unit testing of this class
    public static void main(String[] args) {}

}
