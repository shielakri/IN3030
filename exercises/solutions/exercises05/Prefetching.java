import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

class Prefetching {

  byte[] a;
  int[] rand;
  Random r = new Random();

  Prefetching(int n) {
    a = randomByteArray(n);
  }


  /**
   * Traversing the array sequentially. Total number of reads is 'n'.
   * @return temp The last index checked. This is to avoid dead code optimisation by compiler
   */
  int sequentialRead() {
    Random r = new Random();
    int temp = 0;

    for (int i = 0; i < a.length; i++) {
      int index = Math.abs(r.nextInt(a.length - 1));
      temp = a[i];
    }

    return temp;
  }

  /**
   * Traversing the array stepwise. Total number of reads is 'n'.
   * @param  k The step number.
   * @return temp The last element accessed. Done do avoid dead code compilation.
   */
  int stepwiseRead(int k) {
    int temp = 0;

    for (int i = 0; i < k; i++) {
      for (int j = i; j < a.length; j += k) {
        int index = Math.abs(r.nextInt(a.length - 1));
        temp = a[j];
      }
    }

    return temp;
  }

  /**
   * Reading random elements in the array 'n' times.
   * @return temp The last element accessed. Done to avoid dead code compilation.
   */
  int randomRead() {
    int temp = 0;
    for (int i = 0; i < a.length; i++) {
      int index = Math.abs(r.nextInt(a.length - 1));
      temp = a[index];
    }
    return temp;
  }

  /* Creating a random byte array of size 'n'. */
  byte[] randomByteArray(int n) {
    byte[] a = new byte[n];
    new Random().nextBytes(a);
    return a;
  }


  /* Testing the different reads. The program takes 'n' as an argument */
  public static void main(String[] args) {

    int n;

    try {
      n = Integer.parseInt(args[0]);
    } catch(Exception e) {
      System.out.println("Correct use: java Prefetching <n> where <n> is the size of array and number of reads.");
      return;
    }

    Prefetching pf = new Prefetching(n);

    int runs = 7;
    int temp = 0;
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
