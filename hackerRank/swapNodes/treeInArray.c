#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>

#define MAX_N  (1025)

int left[MAX_N];
int right[MAX_N];
int depth[MAX_N];

void calculateDepth(int root, int d) {
    depth[root] = d;
    // if root has left child
    if (left[root] > 0) {
        calculateDepth(left[root], d + 1);
    }
    if (right[root] > 0) {
        calculateDepth(right[root], d + 1);
    }
}

void inOrder(int root) {
    // if it has left child
    if (left[root] > 0) {
        // recurse
        inOrder(left[root]);
    }
    // print this node
    printf("%d ", root);
    // Now check right child
    if (right[root] > 0) {
        inOrder(right[root]);
    }
}

__inline__ void swap(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

int main() {

    /* Enter your code here. Read input from STDIN. Print output to STDOUT */
    int N = 0, i = 0, T = 0, k = 0;
    scanf("%d", &N);
    for (i = 1; i <= N; i++) {
        scanf("%d %d", &left[i], &right[i]);
    }
    calculateDepth(1, 1);
    // get number of queries
    scanf("%d", &T);
    // for each test case
    while (T--) {
        scanf("%d", &k);
        for (i = 1; i <= N; i++) {
            if ((depth[i] % k) == 0) {
                // swap left and right children
                swap(&left[i], &right[i]);
            }
        }
        inOrder(1);
        printf("\n");
    }
    return 0;
}

