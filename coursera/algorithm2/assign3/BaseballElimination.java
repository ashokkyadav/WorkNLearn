import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

/**
 * The {@code BaseballElimination} class takes a filename as input for the current standing of teams in the tournament.
 * It then uses MaxFlow algorithm to predict if any team has been already mathematically eliminated.
 * 
 * Code uses algs4.jar package. <a href="https://algs4.cs.princeton.edu/code/">Algo4 Princeton</a> 
 * 
 * @author Ashok Yadav ashok.kyadav@gmail.com
 *
 */

public class BaseballElimination {
    private static final boolean DEBUG_PRINTS = false;
    private final int numTeams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final String[] teams;
    private final int[][] r;    // rest of the matches between teams
    private int maxWins;
    private int maxWinTeam;
    private boolean[] isEliminated;
    private ArrayList<ArrayList<String>> eliminatingTeams;
    private final HashMap<String, Integer> teamToIndex;

    /**
     * @param filename Name of the input file. Format of the file has to be as follows - 
     * number of teams in first line. Then each line contains team-name, number of wins, number of losses, number of remaining games
     * and number of remaining games against each other teams! Team-name cannot contain whiteSpace.
     */
    public BaseballElimination(String filename) {
        if (filename == null) throw new java.lang.IllegalArgumentException("The filename cannot be null!!");
        In in = new In(filename);
        numTeams = in.readInt();
        // Initialize all private variables now
        wins = new int[numTeams];
        losses = new int[numTeams];
        remains = new int[numTeams];
        teams = new String[numTeams];
        r = new int[numTeams][numTeams];
        isEliminated = new boolean[numTeams];
        eliminatingTeams = new ArrayList<ArrayList<String>>(numTeams);
        teamToIndex = new HashMap<String, Integer>(numTeams);
        
        maxWins = Integer.MIN_VALUE;
        maxWinTeam = -1;
        for (int i = 0; i < numTeams; i++) {
            // process line by line
            String team = in.readString().trim();
            teamToIndex.put(team, i);
            teams[i] = team;
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remains[i] = in.readInt();
            if (maxWins < wins[i]) {
                maxWins = wins[i];
                maxWinTeam = i;
            }
            if (DEBUG_PRINTS) {
                System.out.printf("%s %d %d %d ", teams[i], wins[i], losses[i], remains[i]);
            }
            // now match matrix with other teams.
            for (int j = 0; j < numTeams; j++) {
                r[i][j] = in.readInt();
                // debug
                if (DEBUG_PRINTS) System.out.printf("%d ", r[i][j]);
            }
            if (DEBUG_PRINTS) System.out.println();
        }
        in.close();
        // Calculate for all teams if they are eliminated and store them. Queries could then be in constant time!\
        for (int i = 0; i < numTeams; i++) {
            // check if trivially eliminated
            if (isTriviallyEliminated(i)) {
                isEliminated[i] = true;
                ArrayList<String> t = new ArrayList<String>();
                t.add(teams[maxWinTeam]);
                eliminatingTeams.add(i, t);
            }
            else {
                // Non-trivial case
                eliminatingTeams.add(i, getEliminatingTeams(i));
                if (eliminatingTeams.get(i) != null) isEliminated[i] = true;
            }
        }
    }
    
    /**
     * @return Number of teams currently being worked on.
     */
    public int numberOfTeams() {
        return numTeams;
    }
    /**
     * @return An iterable of team names in tournament.
     */
    public Iterable<String> teams() {
        return new LinkedList<String>(Arrays.asList(teams));
    }
    /**
     * @param team Team name for which current number of wins required.
     * @return Number of wins given team has currently.
     */
    public int wins(String team) {
        if (team == null) throw new java.lang.IllegalArgumentException("Team name cannot be null!!");
        if (teamToIndex.containsKey(team)) {
            return wins[teamToIndex.get(team)];
        }
        else throw new java.lang.IllegalArgumentException("No such team in the league!!");
    }
    /**
     * @param team Team name for which current number of losses required.
     * @return Number of losses given team has currently.
     */
    public int losses(String team) {
        if (team == null) throw new java.lang.IllegalArgumentException("Team name cannot be null!!");
        if (teamToIndex.containsKey(team)) {
            return losses[teamToIndex.get(team)];
        }
        else throw new java.lang.IllegalArgumentException("No such team in the league!!");
    }
    /**
     * @param team Team name for which current number of losses required.
     * @return Number of remaining games for the given team.
     */
    public int remaining(String team) {
        if (team == null) throw new java.lang.IllegalArgumentException("Team name cannot be null!!");
        if (teamToIndex.containsKey(team)) {
            return remains[teamToIndex.get(team)];
        }
        else throw new java.lang.IllegalArgumentException("No such team in the league!!");
    }
    /**
     * @param team1 Team-name of first team.
     * @param team2 Team-name of second team.
     * @return Number of games still to be played between given teams.
     */
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) throw new java.lang.IllegalArgumentException("Team name cannot be null!!");
        if (teamToIndex.containsKey(team1) && teamToIndex.containsKey(team2)) {
            return r[teamToIndex.get(team1)][teamToIndex.get(team2)];
        }
        else throw new java.lang.IllegalArgumentException("No such team in the league!!");
    }
    /**
     * @param team Team-name for which whether eliminated info is needed.
     * @return True if team is already eliminated else False.c
     */
    public boolean isEliminated(String team) {
        // Sanity check
        if (team == null) throw new java.lang.IllegalArgumentException("Team name cannot be null!!");
        
        if (!teamToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException("No such team in the league!!");
        
        // now the is in the league and is a valid team.
        return isEliminated[teamToIndex.get(team)];
    }
    
    /**
     * @param team Team-name for which whether eliminated info is needed.
     * @return An iterable of team names which elimiated the provided team.
     */
    public Iterable<String> certificateOfElimination(String team) {
        // Sanity check
        if (team == null) throw new java.lang.IllegalArgumentException("Team name cannot be null!!");
        
        if (!teamToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException("No such team in the league!!");

        // return a copy of the list
        return (eliminatingTeams.get(teamToIndex.get(team)) != null) ? (new LinkedList<String>(eliminatingTeams.get(teamToIndex.get(team)))) : null;
    }
    
    private boolean isTriviallyEliminated(int teamIdx) {
        // number of total wins this team can get
        int potentialWins = wins[teamIdx] + remains[teamIdx];
        if (potentialWins < maxWins) return true;
        return false;
    }
    
    private ArrayList<String> getEliminatingTeams(int teamIdx) {
        // MaxFlow formulation of the problem
        // number of remaining games between rest of the teams
        int numRemGamesInRestTeams = ((numTeams - 1) * (numTeams - 2)) / 2; 
        int V = numRemGamesInRestTeams + (numTeams - 1) + 2; // number of vertex in maxFlow network. nC2 + n + 2
        FlowNetwork flowNetwrk = new FlowNetwork(V);
        int restGamesNodes = 0;
        int totalGamesInRestTeams = 0;
        for (int i = 0; i < numTeams - 1; i++) {
            for (int j = i + 1; j < numTeams - 1; j++) {
                int row = (i >= teamIdx) ? (i + 1) : i;
                int col = (j >= teamIdx) ? (j + 1) : j;
                double capacity = (double) r[row][col];
                totalGamesInRestTeams += r[row][col];
                FlowEdge edge = new FlowEdge(teamIdx, numTeams + restGamesNodes, capacity);
                // add it to the network
                flowNetwrk.addEdge(edge);
                // two more edges from this to the teams node
                FlowEdge e1 = new FlowEdge(numTeams + restGamesNodes, row, Double.POSITIVE_INFINITY);
                FlowEdge e2 = new FlowEdge(numTeams + restGamesNodes, col, Double.POSITIVE_INFINITY);
                // add them to the network
                flowNetwrk.addEdge(e1);
                flowNetwrk.addEdge(e2);
                restGamesNodes++; // increment games count
            }
        }
        // now Edges from teams to target which we named as V - 1
        int potentialWins = wins[teamIdx] + remains[teamIdx];
        for (int i = 0; i < numTeams - 1; i++) {
            int curr = (i >= teamIdx) ? i + 1 : i;
            double capacity = (double) (potentialWins - wins[curr]);
            FlowEdge e = new FlowEdge(curr, V - 1, capacity);
            // add to the network
            flowNetwrk.addEdge(e);
        }
        // network completed. 
        if (DEBUG_PRINTS) System.out.println(flowNetwrk);
        FordFulkerson ff = new FordFulkerson(flowNetwrk, teamIdx, V - 1);
        if (ff.value() == totalGamesInRestTeams) return null;
        ArrayList<String> elimTeams = new ArrayList<String>();
        for (int i = 0; i < numTeams; i++) {
            if (i == teamIdx) continue;    // Nothing to do
            if (ff.inCut(i)) elimTeams.add(teams[i]);
        }
        return elimTeams;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
