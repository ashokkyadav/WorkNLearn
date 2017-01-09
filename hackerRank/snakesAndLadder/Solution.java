package mathProj;

import java.io.*;
import java.util.*;

public class Solution {
    static final int BOARD_SIZE = 100;
    static final int DIE_POSSIBILITIES = 6;
    ArrayList<ArrayList<Integer>> brd;
    int[] dist;
    boolean[] visited;
    public Solution(int[] board) {
        brd = new ArrayList<ArrayList<Integer>>(BOARD_SIZE);
        dist = new int[BOARD_SIZE];
        visited = new boolean[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            brd.add(i, new ArrayList<Integer>());
        }
        // from each point, one can go upto 6 positions
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i] == i) {
                // add all 6 destinations
                for (int j = 1; j <= DIE_POSSIBILITIES; j++) {
                    if (i + j >= BOARD_SIZE) break;
                    brd.get(board[i]).add(board[i + j]);
                }
            }
        }
    }
    public ArrayList<Integer> adj(int v) {
        return new ArrayList<Integer>(brd.get(v));
    }
    public int bfs() {
        // start from loc 0
        LinkedList<Integer> q = new LinkedList<Integer>();
        q.add(0);
        visited[0] = true;
        boolean reachedGoal = false;
        // while the queue is not empty, search
        while(!q.isEmpty()) {
            int d = q.remove();
            // if reached 100 or final destination, break;
            if (d == BOARD_SIZE - 1) {
                reachedGoal = true;
                break;
            }
            for (int v : adj(d)) {
                // if not already visited this square
                if (!visited[v]) {
                    dist[v] = dist[d] + 1;
                    q.add(v);
                    visited[v] = true;
                }
            }
        }
        if (reachedGoal) {
            return dist[BOARD_SIZE - 1];
        }
        return -1;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        while(T-- > 0) {
            int[] board = new int[BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                board[i] = i;
            }
            int numLadders = in.nextInt();
            for (int i = 0; i < numLadders; i++) {
                int s = in.nextInt();
                int d = in.nextInt();
                board[s - 1] = d - 1;
            }
            int numSnakes = in.nextInt();
            for (int i = 0; i < numSnakes; i++) {
                int s = in.nextInt();
                int d = in.nextInt();
                board[s - 1] = d - 1;
            }
            Solution boardObj = new Solution(board);
            int numberOfRolls = boardObj.bfs();
            System.out.println(numberOfRolls);
        }
        in.close();
    }
}