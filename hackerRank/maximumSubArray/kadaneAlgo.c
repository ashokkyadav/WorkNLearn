#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>
#include <limits.h>

__inline int max(int x, int y) {
    return (((x) > (y)) ? (x) : (y));
}
int main() {

    /* Enter your code here. Read input from STDIN. Print output to STDOUT */
    int T = 0;
    scanf("%d", &T);
    // while all tests are not done
    while(T--) {
        int N = 0;
        scanf("%d", &N);
        int A[N];
        int maximum = INT_MIN;
        int sumNonCont = 0;
        for (int i = 0; i < N; i++) {
            scanf("%d", &A[i]);
            if (A[i] > 0) {
                sumNonCont += A[i];
            }
            if (A[i] > maximum) {
                maximum = A[i];
            }
        }
        int maxCont = A[0];
        int maxSoFar = A[0];
        for (int i = 1; i < N; i++) {
            maxCont = max(A[i], (A[i] + maxCont));
            maxSoFar = max(maxCont, maxSoFar);
        }
        // for all negative numbers
        if (sumNonCont == 0) {
            sumNonCont = maximum;
        }
        printf("%d %d\n", maxSoFar, sumNonCont);
    }
    return 0;
}

