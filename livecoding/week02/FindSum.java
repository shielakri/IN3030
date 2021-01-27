import java.util.concurrent.CyclicBarrier;
import java.util.Random;

class FindSum {
  int n = 1000;
  int[] a = randomArray(n);
  int localSums[];
  CyclicBarrier cb;

  class Worker implements Runnable {

    int ind, startIndex, endIndex;

    Worker(int ind, int startIndex, int endIndex) {
      this.ind = ind;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    public void run() {

      localSums[ind] = 0;

      for (int i = startIndex; i < endIndex; i++) {
        localSums[ind] += a[i];
      }

      try {
        cb.await();
      } catch(Exception e) {
        e.printStackTrace();
      }

    }
  }

  int findSum() {
    int cores = Runtime.getRuntime().availableProcessors();
    localSums = new int[cores];

    cb = new CyclicBarrier(cores + 1);

    int numberOfElements = a.length / cores;

    for (int i = 0; i < cores - 1; i++) {
      (new Thread(new Worker(i, i * numberOfElements, (i + 1) * numberOfElements))).start();
    }

    (new Thread(new Worker(cores - 1, (cores - 1) * numberOfElements, a.length))).start();

    try {
      cb.await();
    } catch(Exception e) {
      e.printStackTrace();
    }

    int globalSum = 0;

    for (int localSum : localSums) {
      globalSum += localSum;
    }

    return globalSum;

  }

  public static void main(String[] args) {
    FindSum fs = new FindSum();

    int sum = fs.findSum();

    System.out.println("Sum\t:\t" + sum);
  }

  int[] randomArray(int n) {
    Random r = new Random(5);
    int[] a = new int[n];

    for (int i = 0; i < a.length; i++) {
      a[i] = r.nextInt(n);
    }

    return a;
  }
}
