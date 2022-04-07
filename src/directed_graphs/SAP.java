package directed_graphs;

import edu.princeton.cs.algs4.Digraph;

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
        childCount = new int[diGraph.V()];
        int ancestorPosition = dfs(v, w, true);
        if (ancestorPosition >= 0) return childCount[ancestorPosition];
        return ancestorPosition;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) { return dfs(v, w, true); }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) { return 0; }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) { return 0; }

    private int dfs(int v, int w, boolean switchToV) {
        if (switchToV) {
            if (isVertexMarked[v]) return v;
            isVertexMarked[v] = true;
            if (diGraph.outdegree(v) > 0) {
                for (int x : diGraph.adj(v)) {
                    childCount[x] = childCount[v] + 1;
                    return dfs(x, w, false);
                }
            }
        } else {
            if (isVertexMarked[w]) return w;
            isVertexMarked[w] = true;
            if (diGraph.outdegree(w) > 0) {
                for (int x : diGraph.adj(w)) {
                    childCount[x] = childCount[w] + 1;
                    return dfs(v, x, true);
                }
            }
        }
        return -1;
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
