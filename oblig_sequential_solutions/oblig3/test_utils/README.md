# Test files
These files, with names primesN.txt, contain the prime numbers from 1 and up to (and including) N.

If your terminal supports it, you can use `diff` to compare the output of your program with the contents of the text files. For this to work, you have to print out one prime number on each line and nothing else.

For example:

```
diff <(java SieveOfEratosthenes 100) test_utils/primes100.txt
```
If they are the same, `diff` returns nothing (this is what we want). If they are not the same, `diff` gives feedback on which lines are different.
