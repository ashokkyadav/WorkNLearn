#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>

#define DIMENSION       (3)
enum Direction{
    LEFT = 1,
    RIGHT = 2,
    BOTH = LEFT | RIGHT,
};

typedef struct s3DPoint {
    int coordinate[DIMENSION];
} point3D;

typedef struct sNode3D {
    point3D p;  // key
    int v;      // value
    struct sNode3D *left;
    struct sNode3D *right;
} node3D;

node3D *getNewNode(int x, int y, int z, int val) {
    node3D *node = (node3D *)malloc(sizeof(node3D));
    if (node) {
        node->p.coordinate[0] = x;
        node->p.coordinate[1] = y;
        node->p.coordinate[2] = z;
        node->v = val;
        node->left = NULL;
        node->right = NULL;
    }
    return node;
}

node3D * insert(node3D *r, int p[], int v, int axis) {
    if (r == NULL) {
        r = getNewNode(p[0], p[1], p[2], v);
        return r;
    }
	// if this node already exists
	if ((p[0] == r->p.coordinate[0]) && (p[1] == r->p.coordinate[1]) && (p[2] == r->p.coordinate[2])) {
		// just update the value
		r->v = v;
		return r;
	}
    else if (p[axis] <= r->p.coordinate[axis]) {
        // left subtree
        r->left = insert(r->left, p, v, (axis + 1) % DIMENSION);
    }
    else /* (p[axis] > r->p.coordinate[axis]) */ {
        // right subtree
        r->right = insert(r->right, p, v, (axis + 1) % DIMENSION);
    }
    return r;
}
// finds if point p is inside cuboid described by s[] and e[]
int contains(int p[], int s[], int e[], int dim) {
    int cmp = (p[0] >= s[0] && p[0] <= e[0]) && (p[1] >= s[1] && p[1] <= e[1]) && (p[2] >= s[2] && p[2] <= e[2]);
    /*
    for (int i = 1; i < dim; i++) {
        cmp &= (p[i] >= s[i] && p[i] <= e[i]);
    }
    */
    return cmp;
}

int doesIntersect(int p[], int s[], int e[], int axis) {
    if (p[axis] < s[axis]) return RIGHT;
    if (p[axis] > e[axis]) return LEFT;
    return BOTH;
}

void searchAllAndSum(node3D *r, int s[], int e[], int axis, long long *sum) {
    int leftOrRight = 0;
    // if current node is NULL, nothing overlapping
    if (r == NULL) return;
    if (contains(r->p.coordinate, s, e, DIMENSION)) {
        *sum = *sum + r->v;
    }
    leftOrRight = doesIntersect(r->p.coordinate, s, e, axis);
    if (leftOrRight & LEFT) {
        searchAllAndSum(r->left, s, e, (axis + 1) % DIMENSION, sum);
    }
    if (leftOrRight & RIGHT) {
        searchAllAndSum(r->right, s, e, (axis + 1) % DIMENSION, sum);
    }
}
int main() {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT */
    char choice[10];
    int p[3], e[3];
    int T = 0, N = 0, M = 0;
    int v = 0;
    node3D *root = NULL;
    scanf("%d", &T);
    while(T--) {
        scanf("%d %d", &N, &M);
        // new test case, new tree
        root = NULL;
        while(M--) {
            scanf("%s", choice);
            if (choice[0] == 'U') {
                scanf("%d %d %d %d", &p[0], &p[1], &p[2], &v);
                // insert this value to 3D point (x, y, z)
                root = insert(root, p, v, 0);
            }
            else if(choice[0] == 'Q') {
                long long s = 0;
                scanf("%d %d %d %d %d %d", &p[0], &p[1], &p[2], &e[0], &e[1], &e[2]);
                // find all the nodes enclosed in cuboid described by p and e
                searchAllAndSum(root, p, e, 0, &s);
                printf("%lld\n", s);
            }
        }
    }
    return 0;
}

