#include <iostream>
#include <vector>
using namespace std;

vector<int> primes;
void generatePrimes(int n) {
    bool isNotPrimes[n + 1];
    fill(isNotPrimes, isNotPrimes + n + 1, 0);
    int i = 2;
    int t = 2;
    int xx = 0;
    while (t * t <= n) {
        if (!isNotPrimes[t]) {
            primes.push_back(t);
            for (i = t; t * i <= n; i++) {
                isNotPrimes[t * i] = true;
            }
        }
        t++;
    }
    while(t <= n){
        if (!isNotPrimes[t]) {
            primes.push_back(t);
        }
        t++;
    }
}

int getTotient(int n) {
    int i = 0;
    double tot = (double)n;
    while(primes[i] <= n) {
        if ((n % primes[i]) == 0) {
            tot *= (1.0 - 1.0/(double)primes[i]);
        }
        i++;
    }
    return (int)tot;
}

int main()
{
    int t;
    cin >> t;
    generatePrimes(1000000);
    //cout << primes.size();
    while(t > 0) {
        t--;
        int N;
        cin >> N;
        int totient = getTotient(N);
        cout << totient << endl;
    }
    return 0;
}

