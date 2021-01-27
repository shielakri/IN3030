import java.util.Random;
import java.util.concurrent.CyclicBarrier;

class FindMax {

  int n = 100000000; // 10 million
  int[] a = randomArray(n);
  int cores = Runtime.getRuntime().availableProcessors();
  int[] localMax = new int[cores];
  CyclicBarrier cb = new CyclicBarrier(cores + 1);


  public static void main(String[] args) {

    FindMax fm = new FindMax();

    long start = System.nanoTime();
    int max = fm.findMaxSeq();
    long end = System.nanoTime();

    System.out.println("Max\t:\t" + max);
    System.out.println("Time\t:\t" + (end - start) / 1000000.0 + " ms"); //double


    start = System.nanoTime();
    max = fm.findMaxPar();
    end = System.nanoTime();

    System.out.println("Max\t:\t" + max);
    System.out.println("Time\t:\t" + (end - start) / 1000000.0 + " ms"); //double





  }

  class Worker implements Runnable {

    int ind, start, end;

    public Worker(int ind, int start, int end) {
      this.ind = ind;
      this.start = start;
      this.end = end;
    }

    public void run() {
      localMax[ind] = a[start];

      for (int i = start; i < end; i++) {
        if (a[i] > localMax[ind]) localMax[ind] = a[i];
      }

      try {
        cb.await();
      } catch(Exception e) {
        e.printStackTrace();
      }

    }
  }



  int findMaxPar() {

    int max = a[0];
    int sizeOfSegment = a.length / cores;

    for (int i = 0; i < cores - 1; i++) {
      (new Thread(new Worker(i, i * sizeOfSegment, (i + 1) * sizeOfSegment))).start();
    }

    (new Thread(new Worker(cores - 1, (cores - 1) * sizeOfSegment, a.length))).start();

    try {
      cb.await();
    } catch(Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < localMax.length; i++) {
      if (localMax[i] > max) max = localMax[i];
    }

    return max;
  }



  int findMaxSeq() {
    int max = a[0];

    for (int i = 0; i < a.length; i++) {
      if (a[i] > max) max = a[i];
    }

    return max;
  }

  int[] randomArray(int n) {
    Random rd = new Random(25);
    int[] a = new int[n];

    for (int i = 0; i < n; i++) {
      a[i] = rd.nextInt(n);
    }

    return a;
  }
}
