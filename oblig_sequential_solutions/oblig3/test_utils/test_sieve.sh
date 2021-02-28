#!/bin/bash

# Run this from oblig3 directory

for i in 50 100 229 499 1000 1234 10000
do
  echo $i
  diff <(java SieveOfEratosthenes $i) test_utils/primes$i.txt
done
