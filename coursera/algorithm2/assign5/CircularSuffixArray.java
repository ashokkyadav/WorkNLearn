import edu.princeton.cs.algs4.In;

/**
 * @author Ashok Yadav ashok.kyadav@gmail.com
 *
 */
public class CircularSuffixArray {
    private final int R = 256;
    private String originalString;
    private int[] indices;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
     if (s == null) throw new java.lang.IllegalArgumentException("The filename cannot be null!!");
        originalString = new String(s);
        indices = new int[originalString.length()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        int[] auxiliary = new int[originalString.length()];
        MSDSort(auxiliary, 0, indices.length - 1, 0);
    }
    
    private void MSDSort(int[] aux, int lo, int hi, int depth) {
        // base case
        if (hi <= lo) return;
        int[] count = new int[R + 2];
        // count occurrance of each characters 
        for (int i = lo; i <= hi; i++) {
            count[originalString.charAt(indexInString(indices[i], depth)) + 2]++;
        }
        // accumulative counts
        for (int i = 0; i < R; i++) {
            count[i + 2] += count[i + 1];
        }
        // place at correct position
        for (int i = lo; i <= hi; i++) {
            aux[count[originalString.charAt(indexInString(indices[i], depth)) + 1]++] = indices[i];
        }
        // boolean isSortReqd = isChanged(indices, aux, lo, hi);
        // update to original string
        for (int i = lo; i <= hi; i++) {
            indices[i] = aux[i - lo];
        }
        boolean isSortReqd = (depth < originalString.length() - 1);
        // recusion for all substrings
        if (isSortReqd)
        {
            for (int i = 0; i < R; i++) {
                MSDSort(aux, lo + count[i], lo + count[i + 1] - 1, depth + 1);
            }
        }
    }
/*    private boolean isChanged(int[] index, int[] aux, int lo, int hi) {
        for (int i = lo; i <= hi; i++) {
            if (index[i] != aux[i - lo]) return true;
        }
        return false;
    }
*/    private int indexInString(int ithString, int depth) {
        return ((ithString + depth) % originalString.length());
    }
    // length of s
    public int length() {
        return originalString.length();
    }
    // returns index of ith sorted suffix
    public int index(int i) {
     if (i < 0 || i >= originalString.length()) throw new java.lang.IllegalArgumentException("Given index is out of bound!!");
        return indices[i];
    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);
        String text = in.readAll();
        CircularSuffixArray csa = new CircularSuffixArray(text);
        for (int i = 0; i < csa.length(); i++) {
            System.out.println("Index[" + i + "] = " + csa.index(i));
        }
        in.close();
    }
}
