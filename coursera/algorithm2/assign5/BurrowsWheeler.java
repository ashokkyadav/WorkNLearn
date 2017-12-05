import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Ashok Yadav ashok.kyadav@gmail.com
 *
 */
public class BurrowsWheeler {
    /**
     * Apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
     */
    public static void transform() {
        String text = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(text);
        // get the index of first in the sorted suffix array
        int front = 0;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                front = i;
                break;
            }
        }
        // form the t[] array
        BinaryStdOut.write(front);
        for (int i = 0; i < text.length(); i++) {
            int idx = csa.index(i);
            idx--;
            if (idx < 0) idx += csa.length();
            BinaryStdOut.write(text.charAt(idx));
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }
    /**
     * Apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
     */
    public static void inverseTransform() {
        int R = 256;
        int front = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        // form the first column now by sorting t
        char[] f = new char[t.length()];
        int[] count = new int[R + 1];
        ArrayList<LinkedList<Integer>> charIndices = new ArrayList<LinkedList<Integer>>(R);
        // count chars
        for (int i = 0; i < t.length(); i++) {
            count[t.charAt(i) + 1]++;
        }
        // cumsum
        for (int i = 0; i < R; i++) {
            charIndices.add(new LinkedList<Integer>());
            count[i + 1] += count[i];
        }
        // sort them now
        for (int i = 0; i < t.length(); i++) {
            f[count[t.charAt(i)]++] = t.charAt(i);
            charIndices.get(t.charAt(i)).add(i);
        }
        // form the next array now
        int[] next = new int[t.length()];
        // boolean[] marked = new boolean[t.length()];
        for (int i = 0; i < t.length(); i++) {
            next[i] = charIndices.get(f[i]).remove(); //findIndex(t, f[i], marked);
        }
        // decode the msg now
        int runningIndex = front;
        for (int i = 0; i < t.length(); i++) {
            BinaryStdOut.write(t.charAt(next[runningIndex]));
            runningIndex = next[runningIndex];
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }
    
//    private static int findIndex(String t, char f, boolean[] marked) {
//        int idx = -1;
//        for (int i = 0; i < t.length(); i++) {
//            if (t.charAt(i) == f && !marked[i]) {
//                marked[i] = true;
//                return i;
//            }
//        }
//        return idx;
//    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        boolean encode = args[0].equals("-");
        if (encode) {
            BurrowsWheeler.transform();
        }
        else {
            BurrowsWheeler.inverseTransform();
        }
    }
}
