#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>
#include <stdbool.h>

#define ALPHABET_SIZE       ('j' - 'a' + 1)
#define MAX_NAME_LEN        (61)

typedef struct sTrie {
    bool isLeaf;
    struct sTrie *next[ALPHABET_SIZE];
} trieNode;

trieNode *getNewTrieNode( void ) {
    trieNode *pNode = (trieNode *)malloc(sizeof(trieNode));
    if (pNode != NULL) {
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            pNode->next[i] = NULL;
        }
        pNode->isLeaf = false;
    }
    return pNode;
}
// index of a character in array
#define INDEX_OF(c)    (c - 'a')

bool insertKey(trieNode *r, char key[]) {
    int l = strlen(key);
    trieNode *trav = r;
    char *ptr = key;
    bool nodeExisted = false;
    while(*ptr != '\0') {
        if (trav->next[INDEX_OF(*ptr)] == NULL) {
            trav->next[INDEX_OF(*ptr)] = getNewTrieNode();
            nodeExisted = false;
        }
        else {
            nodeExisted = true;
        }
        trav = trav->next[INDEX_OF(*ptr)];
        if (trav->isLeaf) {
            return false;
        }
        ptr++;
    }
    if (nodeExisted) return false;
    trav->isLeaf = true;
    return true;
}
int main() {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT */
    int N = 0;
    char inputString[MAX_NAME_LEN];
    scanf("%d", &N);
    trieNode *root = getNewTrieNode();
    while (N--) {
        // get the input String
        scanf("%s", inputString);
        if (!insertKey(root, inputString)) {
            printf("BAD SET\n%s\n", inputString);
            break;
        }
    }
    //printf("%d\n", N);
    if (N < 0) {
        printf("GOOD SET\n");
    }
    return 0;
}
