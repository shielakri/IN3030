class SieveOfEratosthenes {

  int n, root, numOfPrimes;
  byte[] oddNumbers;

  // limit n
  SieveOfEratosthenes(int n) {
    this.n = n;
    root = (int) Math.sqrt(n);
    // it is filled with only 0
    oddNumbers = new byte[(n / 16) + 1]; // +1 for integer divison
  }


  // mark 'num' as not prime (composite)
  void mark(int num) {
    int byteIndex = num / 16;
    int bitIndex = (num % 16) / 2;

    // change bit value to 1
    int mask = 1 << bitIndex;
    //int mask = 0b10000000 >> bitIndex
    oddNumbers[byteIndex] |= mask; // oddNumbers[byteIndex] = oddNumbers[byteIndex] | mask
  }

  // check if 'num' is prime
  boolean isPrime(int num) {
    int byteIndex = num / 16;
    int bitIndex = (num % 16) / 2;

    int mask = 1 << bitIndex;

    return (oddNumbers[byteIndex] & mask) == 0;
  }

  // return the next prime in the list
  int nextPrime(int prime) {
    for (int i = prime + 2; i <= root; i += 2)
      if (isPrime(i))
        return i;

    return -1;
  }

  // mark multiples
  void traverse(int prime) {
    for (int i = prime * prime; i <= n; i += prime * 2)
      mark(i);
  }


  int[] getPrimes() {

    if (n <= 1) return new int[]{};

    sieve();
    return collectPrimes();
  }


  int[] collectPrimes() {

    int start = (root % 2 == 0) ? root + 1 : root + 2;

    for (int i = start; i <= n; i += 2)
      if (isPrime(i))
        numOfPrimes++;

    int[] primes = new int[numOfPrimes];

    primes[0] = 2;
    int j = 1;

    // Thank you Kasper for noticing that it should be an odd number and not 0 :D
    for (int i = 3; i <= n; i += 2)
      if (isPrime(i))
        primes[j++] = i;

    return primes;

  }


  void sieve() {

    numOfPrimes = 1; // to count 2
    mark(1);
    int prime = nextPrime(1); // returns 3

    while (prime != -1) {
      traverse(prime);
      prime = nextPrime(prime);
      numOfPrimes++;
    }

  }

  void printPrimes(int[] primes) {
    for (int p : primes)
      System.out.println(p);
  }

  public static void main(String[] args) {

    int n = Integer.parseInt(args[0]);

    SieveOfEratosthenes soe = new SieveOfEratosthenes(n);

    int[] primes = soe.getPrimes();
    soe.printPrimes(primes);
  }
}
