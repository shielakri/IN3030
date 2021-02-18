/**
 * A class containing one sequential algorithm and four parallel algorithms
 * that all find the largest integer in an integer array.
 *
 * Parallel algorithm 1_1 uses the n/k method and uses the semaphore
 * for each element.
 *
 * Parallel algorithm 1_2 uses the n/k method and uses the semaphore
 * once for each thread.
 *
 * Parallel algorithm 2_1 uses the ind + xk method and uses the semaphore
 * for each element.
 *
 * Parallel algorithm 2_2 uses the ind + xk method and uses the semaphore
 * once for each thread.
 *
 * The semaphore referred to in the description above is 'semUpdateGlobal',
 * the one responsible for ensuring no threads write to/read from the global
 * variable 'globalMax' at the same time.
 *
 * The code that is different from the previous version of FindMax, is
 * marked as CHANGED.
 *
 * This class is tested in file Exercise1.java.
 *
 * @author Shiela Kristoffersen
 *
 */

import java.util.Random;
import java.util.concurrent.Semaphore;

class FindMax {

  int[] a;
  int n, globalMax;
  int cores = Runtime.getRuntime().availableProcessors();
  Semaphore semUpdateGlobal = new Semaphore(1);
  Semaphore semBarrier;

  /**
   * FindMax constructor that initialiszes 'n' and the global array 'a'.
   * @param n    The size of the array 'a'.
   * @param seed An integer that determines the random array sequence.
   */
  FindMax(int n, int seed) {
    this.n = n;
    a = randomArray(n, seed);
  }


  /**
   * These workers call updateGlobalMax for each element they're responsible for.
   */
  class Worker1 implements Runnable {

    int ind, start, end, jump;

    /**
     * Worker constructor.
     * @param ind   The id of the thread.
     * @param start The first index the thread is to check.
     * @param end   The last index the thread is to check.
     * @param jump  The numer of jumps the thread should do to
     *              reach the next index it has to check.
     *              One jump is the same as moving one index ahead.
     */
    public Worker1(int ind, int start, int end, int jump) {
      this.ind = ind;
      this.start = start;
      this.end = end;
      this.jump = jump;
    }

    /**
     * CHANGED
     */
    public void run() {

      for (int i = start; i < end; i += jump) {
        try {
          semUpdateGlobal.acquire(); /* CHANGED */
          updateGlobalMax(a[i]);
          semUpdateGlobal.release(); /* CHANGED */
        } catch(Exception e) {
          e.printStackTrace();
        }
      }

      /**
       * CHANGED
       */
      semBarrier.release();

    }
  }


  /**
   * These workers call updateGlobalMax after they have found their 'localMax'.
   */
  class Worker2 implements Runnable {

    int ind, start, end, jump, localMax;

    /**
     * Worker constructor.
     * @param ind   The id of the thread.
     * @param start The first index the thread is to check.
     * @param end   The last index the thread is to check.
     * @param jump  The numer of jumps the thread should do to
     *              reach the next index it has to check.
     *              One jump is the same as moving one index ahead.
     */
    public Worker2(int ind, int start, int end, int jump) {
      this.ind = ind;
      this.start = start;
      this.end = end;
      this.jump = jump;
    }

    public void run() {

      localMax = a[start];

      for (int i = start; i < end; i += jump) {
        if (localMax < a[i]) localMax = a[i];
      }

      try {
        semUpdateGlobal.acquire(); /* CHANGED */
        updateGlobalMax(localMax);
        semUpdateGlobal.release(); /* CHANGED */
      } catch(Exception e) {
        e.printStackTrace();
      }

      /**
       * CHANGED
       */
      semBarrier.release();

    }
  }


  /**
   * CHANGED (removed 'synchronized' keyword)
   *
   * Updates the globalMax to equal 'num' if 'num' is larger than globalMax.
   * @param num The number with which globalMax is compared.
   */
  void updateGlobalMax(int num) {
    if (globalMax < num) globalMax = num;
  }


  /**
   * Finds the largest integer in array 'a' by assigning each thread its
   * own segment of the array. It uses Worker1, which uses the synchronized
   * method for each element.
   */
  void findMaxPar_11() {
    semBarrier = new Semaphore(-cores + 1); /* CHANGED */
    globalMax = a[0];
    int sizeOfSegment = a.length / cores;
    int jump = 1;

    for (int i = 0; i < cores - 1; i++) {
      (new Thread(
        new Worker1(i, i * sizeOfSegment, (i + 1) * sizeOfSegment, jump))).start();
    }

    (new Thread(
      new Worker1(cores - 1, (cores - 1) * sizeOfSegment, a.length, jump))).start();

    /**
     * CHANGED
     */
    try {
      semBarrier.acquire();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Finds the largest integer in array 'a' by assigning each thread its
   * own segment of the array. It uses Worker2, which uses the synchronized
   * method once.
   */
  void findMaxPar_12() {
    semBarrier = new Semaphore(-cores + 1); /* CHANGED */
    globalMax = a[0];
    int sizeOfSegment = a.length / cores;
    int jump = 1;

    for (int i = 0; i < cores - 1; i++) {
      (new Thread(
        new Worker2(i, i * sizeOfSegment, (i + 1) * sizeOfSegment, jump))).start();
    }

    (new Thread(
      new Worker2(cores - 1, (cores - 1) * sizeOfSegment, a.length, jump))).start();

    /**
     * CHANGED
     */
    try {
      semBarrier.acquire();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Finds the largest integer in array 'a' by assigning each thread its
   * own elements using the ind + xk method. It uses Worker1, which uses the
   * synchronized method for each element.
   */
  void findMaxPar_21() {
    semBarrier = new Semaphore(-cores + 1); /* CHANGED */
    globalMax = a[0];
    int jump = cores;

    for (int i = 0; i < cores; i++) {
      (new Thread(new Worker1(i, i, a.length, jump))).start();
    }

    /**
     * CHANGED
     */
    try {
      semBarrier.acquire();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Finds the largest integer in array 'a' by assigning each thread its
   * own elements using the ind + xk method. It uses Worker2, which uses the
   * synchronized method once.
   */
  void findMaxPar_22() {
    semBarrier = new Semaphore(-cores + 1); /* CHANGED */
    globalMax = a[0];
    int jump = cores;

    for (int i = 0; i < cores; i++) {
      (new Thread(new Worker2(i, i, a.length, jump))).start();
    }

    /**
     * CHANGED
     */
    try {
      semBarrier.acquire();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Sequentially finds the largest number in the global array 'a'.
   */
  void findMaxSeq() {
    globalMax = a[0];

    for (int i = 0; i < a.length; i++) {
      if (globalMax < a[i]) globalMax = a[i];
    }
  }


  /**
   * Places numbers from a random number sequence inside an int array and
   * returns the array.
   * @param  n    The size of the array.
   * @param  seed The seed used to generate a specific random sequence.
   * @return      The array filled with random numbers from the sequence.
   */
  int[] randomArray(int n, int seed) {
    Random rd = new Random(seed);
    int[] a = new int[n];

    for (int i = 0; i < n; i++) {
      a[i] = rd.nextInt(n);
    }

    return a;
  }
}
