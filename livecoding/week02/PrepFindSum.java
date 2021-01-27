import java.util.Random;
import java.util.concurrent.CyclicBarrier;

class PrepFindSum {
  int n = 100;
  int[] a = randomArray(n);
  // int globalSum = 0;
  int[] localSums;
  CyclicBarrier cb;


  class Worker implements Runnable {

    int ind, startindex, endindex;

    Worker(int ind, int startindex, int endindex) {
        this.ind = ind;
        this.startindex = startindex;
        this.endindex = endindex;
    }

    public void run() {
      localSums[ind] = 0;

      for (int i = startindex; i < endindex; i++) {
        localSums[ind] += a[i];
      }

      //updateGlobalSum(localSum);

      try {
        cb.await();
      } catch(Exception e) {
        e.printStackTrace();
      }

    }
  }

  int findSumPar() {
    int globalSum = 0;
    int cores = Runtime.getRuntime().availableProcessors();
    localSums = new int[cores];
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

    for (int localSum : localSums) {
      globalSum += localSum;
    }



    return globalSum;
  }

  // synchronized void updateGlobalSum(int localSum) {
  //   globalSum += localSum;
  // }


  public static void main(String[] args) {
    PrepFindSum fs = new PrepFindSum();

    int sum = fs.findSumSeq();
    System.out.println("Sum\t:\t" + sum);

    sum = fs.findSumPar();
    System.out.println("Sum\t:\t" + sum);
  }


  int findSumSeq() {
    int sum = 0;

    for (int num : a) {
      sum += num; // sum = sum + num
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
