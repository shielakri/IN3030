import java.util.Random;
import java.util.concurrent.CyclicBarrier;

class SumOdd {
  int n = 100;
  int[] a = randomArray(n);
  int globalSum = 0;
  CyclicBarrier cb;


  class Worker implements Runnable {

    int ind, startindex, endindex;

    Worker(int ind, int startindex, int endindex) {
        this.ind = ind;
        this.startindex = startindex;
        this.endindex = endindex;
    }

    public void run() {
      int localSum = 0;

      for (int i = startindex; i < endindex; i++) {
        if (a[i] % 2 == 1) localSum += a[i];
      }

      updateGlobalSum(localSum);

      try {
        cb.await();
      } catch(Exception e) {
        e.printStackTrace();
      }

    }
  }

  int sumOddPar() {
    int cores = Runtime.getRuntime().availableProcessors();
    cb = new CyclicBarrier(cores + 1);

    int numOfElements = a.length / cores;

    for (int i = 0; i < cores - 1; i++) {
      (new Thread(new Worker(i, i * numOfElements, (i + 1) * numOfElements))).start();
    }

    (new Thread(new Worker(cores - 1, (cores - 1) * numOfElements, a.length))).start();

    try {
      cb.await();
    } catch(Exception e) {
      e.printStackTrace();
    }

    return globalSum;
  }

  synchronized void updateGlobalSum(int localSum) {
    globalSum += localSum;
  }


  public static void main(String[] args) {
    SumOdd so = new SumOdd();

    int sum = so.sumOddSeq();
    System.out.println("Sum\t:\t" + sum);

    sum = so.sumOddPar();
    System.out.println("Sum\t:\t" + sum);
  }


  int sumOddSeq() {
    int sum = 0;

    for (int num : a) {
      if (num % 2 == 1) sum += num; // sum = sum + num
    }

    return sum;
  }


  int[] randomArray(int n) {
    Random rd = new Random(2); // seed

    int[] a = new int[n];

    for (int i = 0; i < a.length; i++) {
      a[i] = rd.nextInt(n);
    }

    return a;

  }

}
