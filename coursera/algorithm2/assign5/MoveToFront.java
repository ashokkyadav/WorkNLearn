import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * @author Ashok Yadav ashok.kyadav@gmail.com
 *
 */
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int R = 256;
        char[] alpha = new char[R];
        for (char x = 0; x < R; x++) 
            alpha[x] = x;
        // System.out.println("Inside encode.");
        String text = BinaryStdIn.readString();
        for (int iter = 0; iter < text.length(); iter++) {
            char a = text.charAt(iter);
            int index = -1;
            for (int i = 0; i < R; i++) {
                if (alpha[i] == a) {
                    index = i;
                    break;
                }
            }
            char tempAlpha = alpha[0];
            alpha[0] = a;
            for (int i = 1; i <= index; i++) {
                char tempAlpha1 = alpha[i];
                alpha[i] = tempAlpha;
                tempAlpha = tempAlpha1;
            }
            // System.out.println(index);
            BinaryStdOut.write((char) (index & 0xff));
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int R = 256;
        char[] alpha = new char[R];
        for (char x = 0; x < R; x++) 
            alpha[x] = x;
        // System.out.println("Inside decode.");
        String text = BinaryStdIn.readString();
        for (int iter = 0; iter < text.length(); iter++) {
            char a = text.charAt(iter);
            BinaryStdOut.write(alpha[a]);
            // System.out.println(index + " " + alpha.charAt(index));
            char tempAlpha = alpha[0];
            alpha[0] = alpha[a];
            for (int i = 1; i <= a; i++) {
                char tempAlpha1 = alpha[i];
                alpha[i] = tempAlpha;
                tempAlpha = tempAlpha1;
            }
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
        BinaryStdIn.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        boolean encode = args[0].equals("-");
        if (encode) {
            // System.out.println("Encoding...");
            MoveToFront.encode();
        }
        else {
            MoveToFront.decode();
        }
    }
}
