import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

class PrefetchingMatrix {

  byte[][] m;
  int[] rand;
  Random r = new Random();

  PrefetchingMatrix(int n) {
    m = randomByteMatrix(n);
  }


  /**
   * Traversing the matrix rows sequentially. Total number of reads is 'n'.
   * @return temp The last index checked. This is to avoid dead code optimisation by compiler
   */
  byte sequentialRead() {
    Random r = new Random();
    byte temp = 0;

    for (int i = 0; i < m.length; i++) {
      int index = Math.abs(r.nextInt(m.length - 1));
      for (byte b : m[i])
        temp = b;
    }

    return temp;
  }

  /**
   * Traversing the matrix rows stepwise. Total number of reads is 'n'.
   * @param  k The step number.
   * @return temp The last element accessed. Done do avoid dead code compilation.
   */
  byte stepwiseRead(int k) {
    byte temp = 0;

    for (int i = 0; i < k; i++) {
      for (int j = i; j < m.length; j += k) {
        int index = Math.abs(r.nextInt(m.length - 1));
        for (byte b : m[j])
          temp = b;
      }
    }

    return temp;
  }

  /**
   * Reading random rows in the matrix 'n' times.
   * @return temp The last element accessed. Done to avoid dead code compilation.
   */
  byte randomRead() {
    byte temp = 0;
    for (int i = 0; i < m.length; i++) {
      int index = Math.abs(r.nextInt(m.length - 1));
      for (byte b : m[index])
        temp = b;
    }
    return temp;
  }

  /* Creating a random byte array of size 'n'. */
  byte[][] randomByteMatrix(int n) {
    byte[][] m = new byte[n][64];
    for (byte[] row : m)
      new Random().nextBytes(row);
    return m;
  }


  /* Testing the different reads. The program takes 'n' as an argument */
  public static void main(String[] args) {

    int n;

    try {
      n = Integer.parseInt(args[0]);
    } catch(Exception e) {
      System.out.println("Correct use: java PrefetchingMatrix <n> where <n> is the number of rows in the matrix and number of reads.");
      return;
    }

    PrefetchingMatrix pf = new PrefetchingMatrix(n);

    int runs = 7;
    byte temp = 0;
    long t1, t2;


    /**
     * Sequential read
     */
    double[] seqRead = new double[runs];

    for (int i = 0; i < runs; i++) {
      t1 = System.nanoTime();
      temp = pf.sequentialRead();
      t2 = System.nanoTime();
      seqRead[i] = (t2-t1) / 1000000.0;
    }

    /* Finding the median and printing the sequential read time. */
    Arrays.sort(seqRead);
    System.out.println("Seq\t:\t" + seqRead[runs/2]);


    /**
     * Stepwise reads (bytes)
     */
    for (int i = 1; i <= 128; i *= 2) {

      double[] stepwiseRead = new double[runs];

      for (int j = 0; j < runs; j++) {
        t1 = System.nanoTime();
        temp = pf.stepwiseRead(i);
        t2 = System.nanoTime();
        stepwiseRead[j] = (t2-t1) / 1000000.0;
      }

      /* Finding the median and printing the stepwise read time. */
      Arrays.sort(stepwiseRead);
      System.out.println("Stp " + i + "\t:\t" + stepwiseRead[runs/2]);
    }


    /**
     * Random read
     */
    double[] randRead = new double[runs];

    for (int i = 0; i < runs; i++) {
      t1 = System.nanoTime();
      temp = pf.randomRead();
      t2 = System.nanoTime();
      randRead[i] = (t2-t1) / 1000000.0;
    }

    /* Finding the median and printing the random read time. */
    Arrays.sort(randRead);
    System.out.println("Rnd\t:\t" + randRead[runs/2]);
  }
}
