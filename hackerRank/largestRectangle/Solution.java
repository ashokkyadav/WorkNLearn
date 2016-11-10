import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int[] h = new int[N];
        for (int i = 0; i < N; i++) {
            h[i] = in.nextInt();
        }
        // Stack to keep heights
        ArrayDeque<Integer> stk = new ArrayDeque<Integer>();
        int idx = 0;
        long maxArea = 0;
        // process all heights
        while(idx < N) {
            /*
            if (stk.isEmpty()) {
                System.out.println("Empty!");
            }
            */
            // if stack is empty OR the new height is more than the one on top of stack, push this
            if (stk.isEmpty() || h[idx] >= h[stk.peek()]) {
                stk.push(idx);
                idx++;
            }
            else {
                // calculate area that is made with the element at the top of stack
                long currArea = (long)h[stk.pop()] * (stk.isEmpty() ? idx : idx - stk.peek() - 1);
                // System.out.println(idx + " " + currArea);
                if (currArea > maxArea)
                    maxArea = currArea;
            }
        }
        while (!stk.isEmpty()) {
            long currArea = (long)h[stk.pop()] * (stk.isEmpty() ? idx : idx - stk.peek() - 1);
            // System.out.println(idx + " " + currArea);
            if (currArea > maxArea)
                maxArea = currArea;
        }
        System.out.println(maxArea);
        in.close();
    }
}