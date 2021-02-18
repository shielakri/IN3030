/**
 * Here we find the sum of all numbers in a matrix using two very
 * similar methods.
 *
 * One of these methods (parallel1) is susceptible to a lot of false sharing,
 * while the other method (parallel2) tries to minimize false sharing.
 *
 * To run:
 *
 * javac FalseSharing.java
 * java FalseSharing <n>
 *
 * where <n> gives a square n x n matrix.
 * Try it with n = 5000, 10000, 150000 and 20000.
 */


import java.util.Random;
import java.util.Arrays;

class FalseSharing {

  int cores = Runtime.getRuntime().availableProcessors();
  int[] localSums = new int[cores];
  int[][] matrix;

  FalseSharing(int n) {
    matrix = randomMatrix(n);
  }


  /**
   * Updates localSums each time it finds an odd number.
   */
  class Worker1 implements Runnable {

    int ind, start, end;

    Worker1(int ind, int start, int end) {
      this.ind = ind;
      this.start = start;
      this.end = end;
    }

    public void run() {
      localSums[ind] = 0;

      for (int row = start; row < end; row++)
        for (int col = 0; col < matrix[0].length; col++)
          if (matrix[row][col] % 2 == 1)
            localSums[ind] += matrix[row][col];
    }
  }



  /**
   * Updates localSums only once by calculating the sum using a local variable
   * first. Otherwise this is exactly the same as Worker1.
   */
  class Worker2 implements Runnable {

    int ind, start, end;

    Worker2(int ind, int start, int end) {
      this.ind = ind;
      this.start = start;
      this.end = end;
    }

    public void run() {
      localSums[ind] = 0;
      int localSum = 0;

      for (int row = start; row < end; row++)
        for (int col = 0; col < matrix[0].length; col++)
          if (matrix[row][col] % 2 == 1)
            localSum += matrix[row][col];

      localSums[ind] = localSum; // updated one time
    }
  }



  /**
   * Finds the sum of all odd numbers in the matrix 'matrix' using Worker1.
   * @return globalSum The sum of all odd numbers in the matrix
   */
  int parallel1() {
    Thread[] threads = new Thread[cores];

    int numberOfRows = matrix.length / cores;

    for (int i = 0; i < cores - 1; i++)
      threads[i] = new Thread(new Worker1(i, i * numberOfRows, (i + 1) * numberOfRows));

    threads[cores - 1] = new Thread(new Worker1(cores - 1, (cores - 1) * numberOfRows, matrix.length));

    for (Thread t : threads) t.start();

    try {
      for (Thread t : threads) t.join();
    } catch(Exception e) {
      e.printStackTrace();
    }

    int globalSum = 0;

    for (int localSum : localSums) globalSum += localSum;

    return globalSum;
  }



  /**
   * Is exactly the same as parallel1, except that it uses Worker2.
   * @return globalSum The sum of all odd numbers in the matrix
   */
  int parallel2() {
    Thread[] threads = new Thread[cores];

    int numberOfRows = matrix.length / cores;

    for (int i = 0; i < cores - 1; i++)
      threads[i] = new Thread(new Worker2(i, i * numberOfRows, (i + 1) * numberOfRows));

    threads[cores - 1] = new Thread(new Worker2(cores - 1, (cores - 1) * numberOfRows, matrix.length));

    for (Thread t : threads) t.start();

    try {
      for (Thread t : threads) t.join();
    } catch(Exception e) {
      e.printStackTrace();
    }

    int globalSum = 0;

    for (int localSum : localSums) globalSum += localSum;

    return globalSum;
  }



  /**
   * Testing the two methods parallel1 and parallel2
   * @param args Takes in <n> as argument (will give an NxN matrix)
   */
  public static void main(String[] args) {

    FalseSharing fs = new FalseSharing(Integer.parseInt(args[0]));

    int runs = 7;
    long start, end;

    double[] par1 = new double[runs];
    double[] par2 = new double[runs];

    for (int i = 0; i < runs; i++) {
      start = System.nanoTime();
      fs.parallel1();
      end = System.nanoTime();
      par1[i] = (end - start) / 1000000.0;

      start = System.nanoTime();
      fs.parallel2();
      end = System.nanoTime();
      par2[i] = (end - start) / 1000000.0;
    }

    Arrays.sort(par1);
    System.out.println("Par1\t:\t" + par1[runs/2]);

    Arrays.sort(par2);
    System.out.println("Par2\t:\t" + par2[runs/2]);

    System.out.println("Speedup\t:\t" + (par1[runs/2] / par2[runs/2]));
  }



  /**
   * Fills and returns an NxN matrix with random integers.
   * @param  n The number of rows and columns in the matrix.
   * @return m The matrix filled with random integers.
   */
  int[][] randomMatrix(int n) {
    Random r = new Random(40);
    int[][] m = new int[n][n];

    for (int row = 0; row < m.length; row++)
      for (int col = 0; col < m[0].length; col++)
        m[row][col] = r.nextInt();

    return m;
  }

}
