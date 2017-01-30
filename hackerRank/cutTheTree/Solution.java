package mathProj;

import java.io.*;
import java.util.*;

public class Solution {
    int V;
    ArrayList<ArrayList<Integer>> adjList;
    int[] weights;
    boolean[] visited;
    int totalWeight;
    int minWt;
    public Solution(int v) {
        this.V = v;
        adjList = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < v; i++) {
            adjList.add(new ArrayList<Integer>());
        }
        weights = new int[v];
        visited = new boolean[v];
        minWt = 0x7fffffff;
    }
    public void addWeight(int v, int w) {
        weights[v] = w;
    }
    public void totalWeight(int w) {
        totalWeight = w;
    }
    
    public void connect(int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }
    public int dfs(int v) {
        visited[v] = true;
        int w = 0;
        for (int u : adjList.get(v)) {
            if (!visited[u]) {
                w += dfs(u);
                //w += weights[u];
            }
        }
        w += weights[v];
        // find difference between 2 trees here
        int restW = totalWeight - w;
        int diff = restW - w > 0 ? (restW - w) : (w - restW);
        if (diff < minWt) minWt = diff;
        //System.out.println(v+" wt "+w);
        return w;
    }
    public int getMinDiff() {
        int x = dfs(0);
        return minWt;
    }
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        Solution solObj = new Solution(N);
        int totalWeight = 0;
        for (int i = 0; i < N; i++) {
            int w = in.nextInt();
            solObj.addWeight(i, w);
            totalWeight += w;
        }
        solObj.totalWeight(totalWeight);
        // connections
        for (int i = 0; i < N - 1; i++) {
            int v = in.nextInt();
            int w = in.nextInt();
            solObj.connect(v - 1, w - 1);
        }
        int minCut = solObj.getMinDiff();
        System.out.println(minCut);
        in.close();
    }
}