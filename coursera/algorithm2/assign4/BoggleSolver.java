import java.util.HashSet;

/**
 * The {@code BoggleSolver} class takes an array of Strings what will be used for dictionary for finding out the words.
 * getAllValidWords takes a <em>BoggleBoard</em> as input.
 * This is defined in <a href="https://algs4.cs.princeton.edu/code/">Algo4 Princeton</a> 
 * 
 * @author Ashok Yadav ashok.kyadav@gmail.com
 *
 */
public class BoggleSolver {
    private static final int R = 26; // ALPHABET_SIZE
    private Node root;    // root of the Trie
    
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isWord;
    }
    /**
     * @param dictionary An array of string which will be used as dictionary for the Boggle game.
     * Assumption is that each word in the dictionary contains only the uppercase letters A through Z.
     */
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String word : dictionary) {
            root = add(root, word, 0);
        }
    }

    /**
     * @param board Input BoggleBoard.
     * @return an Iterable of valid dictionary words that can be made from given board using boggle rules
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> words = new HashSet<String>();
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                StringBuilder prefix = new StringBuilder();
                getAllValidWords(board, words, r, c, marked, prefix, root);
            }
        }
        return words;
    }

    /**
     * @param word Input word
     * @return Returns the score of the given word if it is in the dictionary, zero otherwise.
     * Assumption is that the word contains only the uppercase letters A through Z 
     */
    public int scoreOf(String word) {
        if (!contains(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len < 5) return 1;
        if (len < 7) return len - 3;
        if (len == 7) return 5;
        return 11;
    }
    
    private void getAllValidWords(BoggleBoard b, HashSet<String> w, int row, int col, boolean[][] marked, StringBuilder prefix, Node node) {
        // implement DFS here
        marked[row][col] = true;
        char c = b.getLetter(row, col);
        prefix.append(c);
        Node nd = node.next[indexOf(c)];
        // if this is NOT a valid prefix, return, no further processing required.
        if (nd == null) {
            // go back
            prefix.deleteCharAt(prefix.length() - 1);
            // this cell no more in the path now
            marked[row][col] = false;
            if (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) == 'Q')
                prefix.deleteCharAt(prefix.length() - 1);
            return;
        }
        // special handling of Qu
        if (c == 'Q') {
            prefix.append("U");
            nd = nd.next[indexOf('U')];
            // if in the dictionary there's a word where Q is not followed by u
            if (nd == null) {
                // go back
                prefix.deleteCharAt(prefix.length() - 1);
                // this cell no more in the path now
                marked[row][col] = false;
                if (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) == 'Q')
                    prefix.deleteCharAt(prefix.length() - 1);
                return;
                
            }
        }
        // if this string is present, add them to words set
        if (prefix.length() > 2 && nd.isWord) {
            w.add(prefix.toString());
        }
        
        // check its neighbors now.
        // upper cell
        if (isInsideBoard(b, row - 1, col)) {
            // if not marked already
            if (!marked[row - 1][col]) {
                getAllValidWords(b, w, row - 1, col, marked, prefix, nd);
            }
        }
        // upper-right cell
        if (isInsideBoard(b, row - 1, col + 1)) {
            // if not marked already
            if (!marked[row - 1][col + 1]) {
                getAllValidWords(b, w, row - 1, col + 1, marked, prefix, nd);
            }
        }
        // right cell
        if (isInsideBoard(b, row, col + 1)) {
            // if not marked already
            if (!marked[row][col + 1]) {
                getAllValidWords(b, w, row, col + 1, marked, prefix, nd);
            }
        }
        // right-lower cell
        if (isInsideBoard(b, row + 1, col + 1)) {
            // if not marked already
            if (!marked[row + 1][col + 1]) {
                getAllValidWords(b, w, row + 1, col + 1, marked, prefix, nd);
            }
        }
        // lower cell
        if (isInsideBoard(b, row + 1, col)) {
            // if not marked already
            if (!marked[row + 1][col]) {
                getAllValidWords(b, w, row + 1, col, marked, prefix, nd);
            }
        }
        // lower-left cell
        if (isInsideBoard(b, row + 1, col - 1)) {
            // if not marked already
            if (!marked[row + 1][col - 1]) {
                getAllValidWords(b, w, row + 1, col - 1, marked, prefix, nd);
            }
        }
        // left cell
        if (isInsideBoard(b, row, col - 1)) {
            // if not marked already
            if (!marked[row][col - 1]) {
                getAllValidWords(b, w, row, col - 1, marked, prefix, nd);
            }
        }
        // left-upper cell
        if (isInsideBoard(b, row - 1, col - 1)) {
            // if not marked already
            if (!marked[row - 1][col - 1]) {
                getAllValidWords(b, w, row - 1, col - 1, marked, prefix, nd);
            }
        }
        prefix.deleteCharAt(prefix.length() - 1);
        // this cell no more in the path now
        marked[row][col] = false;
        if (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) == 'Q')
            prefix.deleteCharAt(prefix.length() - 1);
        return;
    }
    private boolean isInsideBoard(BoggleBoard b, int r, int c) {
        return (r >= 0 && r < b.rows() && c >= 0 && c < b.cols()); 
    }
    private int indexOf(char c) { 
        return c - 'A';
    }
    private Node add(Node node, String word, int d) {
        if (node == null) node = new Node();
        if (d == word.length()) {
            // this is a dictionary word
            node.isWord = true;
        }
        else {
            char c = word.charAt(d);
            node.next[indexOf(c)] = add(node.next[indexOf(c)], word, d + 1);
        }
        return node;
    }
    private Node getNode(Node node, String word, int d) {
        if (node == null || d == word.length()) return node;
        char c = word.charAt(d);
        return getNode(node.next[indexOf(c)], word, d + 1);
    }
    private boolean contains(String word) {
        Node node = getNode(root, word, 0);
        if (node != null && node.isWord) return true;
        return false;
    }
}
