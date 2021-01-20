# Exercise 2

1. Speedup = speed(sequential) / speed(parallel) = 10 / 5 = **2**
2. The maximum speedup is **4**. Assuming there is no thread overhead (overhead is the cost of creating, executing and destroying threads) and no need for synchronization, we can theoretically make our code run 4 times faster by dividing the work equally between all 4 cores. In practice however, creating thread costs time and resources, the threads often need to be synchronized and our program competes with other programs for the cores. A speedup of 4 will therefore in practice be impossible.
3. If the speedup is less than 1, it means that the parallel algorithm is slower than the sequential algorithm. There is no point in spending time creating a parallel algorithm if it is not faster than the sequential algorithm.
