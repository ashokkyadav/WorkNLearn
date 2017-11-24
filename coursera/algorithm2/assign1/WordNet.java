import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class WordNet {
    private final Digraph G;
    private final HashMap<Integer, String> idToNouns; 
    private final HashMap<String, Bag<Integer>> nounToSets;
    private final SAP lowestAncestor;

    // copy constructor
    private WordNet(WordNet wordnet) {
        G = new Digraph(wordnet.getDigraph());
        idToNouns = new HashMap<Integer, String>(wordnet.getIdToNouns());
        nounToSets = new HashMap<String, Bag<Integer>>(wordnet.getNounToSets());
        lowestAncestor = new SAP(G);
    }
   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException("Argument cannot be null!");
        }
        idToNouns = new HashMap<Integer, String>();
        nounToSets = new HashMap<String, Bag<Integer>>();
        // Open synsets file.
        In in = new In(synsets);
        int v = 0;
        // read all the synsets
        while (in.hasNextLine()) {
            v++;
            String line = in.readLine();
            String[] syns = line.split(",");
            int id = Integer.parseInt(syns[0]);
            String[] nouns = syns[1].split(" ");
            idToNouns.put(id, syns[1]);
            for (String s : nouns) {
                s = s.trim();
                // if (s.equals("marginality")) System.out.println("marginality " + id);
                // if (s.equals("Saint_Christopher-Nevis")) System.out.println("Saint_Christopher-Nevis " + id);
                if (nounToSets.containsKey(s)) {
                    nounToSets.get(s).add(id);
                    // System.out.println(s + " -> " + id);
                }
                else {
                    nounToSets.put(s, new Bag<Integer>());
                    nounToSets.get(s).add(id);
                }
                // idToNouns.get(id).add(s);
            }
        }
        in.close();
        G = new Digraph(v);
        in = new In(hypernyms);
        // read all hypernyms
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] hypers = line.split(",");
            int id = Integer.parseInt(hypers[0]);
            for (int i = 1; i < hypers.length; i++) {
                G.addEdge(id, Integer.parseInt(hypers[i]));
            }
        }
        DirectedCycle dirCyclGraph = new DirectedCycle(G);
        if (dirCyclGraph.hasCycle()) throw new java.lang.IllegalArgumentException("Graph made by input has cycle!!");
        // check number of roots
        int numRoots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) numRoots++;
        }
        if (numRoots != 1) throw new java.lang.IllegalArgumentException("Graph made by input is NOT a DAG!!");
        lowestAncestor = new SAP(G);
    }
    private Digraph getDigraph() {
        return G;
    }
    private Map<Integer, String> getIdToNouns() {
        return idToNouns;
    }
    private Map<String, Bag<Integer>> getNounToSets() {
        return nounToSets;
    }

   // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new LinkedList<String>(nounToSets.keySet());
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.IllegalArgumentException("Argument cannot be null!");
        return nounToSets.containsKey(word);
    }

   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.IllegalArgumentException("Argument cannot be null!");
        nounA = nounA.trim();
        nounB = nounB.trim();
//        for (int i : nounToSets.get(nounA))
//            System.out.printf("%d ");
//        System.out.println();
//        for (int i : nounToSets.get(nounB))
//            System.out.printf("%d ");
//        System.out.println();
        int dist = lowestAncestor.length(nounToSets.get(nounA), nounToSets.get(nounB));
        return dist;
    }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.IllegalArgumentException("Argument cannot be null!");
        // SAP lowestAncestor = new SAP(G);
        int ancestor = lowestAncestor.ancestor(nounToSets.get(nounA), nounToSets.get(nounB));
        return idToNouns.get(ancestor);
    }

   // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length   = wordnet.distance(v, w);
            StdOut.printf("distance between %s and %s is %d\n", v, w, length);
        }
    }
}