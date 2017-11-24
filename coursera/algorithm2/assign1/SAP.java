import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph g; // make a defensive copy of graph

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        // make a deep copy of digraph
        g = new Digraph(G);
    }

   // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        if (w < 0 || w >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        int minLen = Integer.MAX_VALUE;
        BreadthFirstDirectedPaths bfsPathsFrmV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsPathsFrmW = new BreadthFirstDirectedPaths(g, w);
        for (int i = 0; i < g.V(); i++) {
            if (bfsPathsFrmV.hasPathTo(i) && bfsPathsFrmW.hasPathTo(i)) {
                int len = bfsPathsFrmV.distTo(i) + bfsPathsFrmW.distTo(i);
                if (len < minLen) minLen = len;
            }   
        }
        return minLen == Integer.MAX_VALUE ? -1 : minLen;
    }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        if (w < 0 || w >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        int minLen = Integer.MAX_VALUE;
        int shortestAncestor = -1;
        BreadthFirstDirectedPaths bfsPathsFrmV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsPathsFrmW = new BreadthFirstDirectedPaths(g, w);
        for (int i = 0; i < g.V(); i++) {
            if (bfsPathsFrmV.hasPathTo(i) && bfsPathsFrmW.hasPathTo(i)) {
                int len = bfsPathsFrmV.distTo(i) + bfsPathsFrmW.distTo(i);
                if (len < minLen) {
                    minLen = len;
                    shortestAncestor = i;
                }
            }   
        }
        return shortestAncestor;
    }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException("Argument cannot be null");
        for (int x : v) {
            if (x < 0 || x >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        }
        for (int x : w) {
            if (x < 0 || x >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        }
        int minLen = Integer.MAX_VALUE;
        BreadthFirstDirectedPaths bfsPathsFrmV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsPathsFrmW = new BreadthFirstDirectedPaths(g, w);
        for (int i = 0; i < g.V(); i++) {
            if (bfsPathsFrmV.hasPathTo(i) && bfsPathsFrmW.hasPathTo(i)) {
                int len = bfsPathsFrmV.distTo(i) + bfsPathsFrmW.distTo(i);
                if (len < minLen) minLen = len;
            }   
        }
        return minLen == Integer.MAX_VALUE ? -1 : minLen;
    }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException("Argument cannot be null");
        for (int x : v) {
            if (x < 0 || x >= g.V()) throw new java.lang.IllegalArgumentException("Index is out of bounds!");
        }
        for (int x : w) {
            if (x < 0 || x >= g.V()) throw new java.lang.IllegalArgumentException ("Index is out of bounds!");
        }
        int minLen = Integer.MAX_VALUE;
        int shortestAncestor = -1;
        BreadthFirstDirectedPaths bfsPathsFrmV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsPathsFrmW = new BreadthFirstDirectedPaths(g, w);
        for (int i = 0; i < g.V(); i++) {
            if (bfsPathsFrmV.hasPathTo(i) && bfsPathsFrmW.hasPathTo(i)) {
                int len = bfsPathsFrmV.distTo(i) + bfsPathsFrmW.distTo(i);
                if (len < minLen) {
                    minLen = len;
                    shortestAncestor = i;
                }
            }   
        }
        return shortestAncestor;
    }

   // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        // StdOut.println(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        return;
    }
}





