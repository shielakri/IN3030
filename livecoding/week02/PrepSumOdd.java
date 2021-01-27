import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

class PrepSumOdd {

  // global variables
  int[] globalArray;
  int cores = Runtime.getRuntime().availableProcessors();
  int[] localSums = new int[cores];

  // CyclicBarrier
  CyclicBarrier cb = new CyclicBarrier(cores + 1);


  // threads
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
        if (globalArray[i] % 2 != 0) localSums[ind] += globalArray[i];
      }

      try {
        cb.await();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }


  // constructor
  PrepSumOdd(int n) {
    globalArray = randomArray(n);
  }


  // Sequential algorithm
  int sumOddSeq() {
    int sum = 0;

    for (int num : globalArray) {
      if (num % 2 != 0) sum += num; // sum = sum + num
    }

    return sum;
  }


  // Parallel algorithm
  int sumOddPar() {

    // localSums = new int[cores];
    int size = globalArray.length / cores;

    for (int i = 0; i < cores - 1; i++) {
      (new Thread(new Worker(i, i * size, (i + 1) * size))).start();
    }

    (new Thread(new Worker(cores - 1, (cores - 1) * size, globalArray.length))).start();

    try {
      cb.await();
    } catch(Exception e) {
      e.printStackTrace();
    }

    int globalSum = 0;

    for (int localSum : localSums) {
      // System.out.print(localSum + "\t");
      globalSum += localSum;
    }

    // System.out.println("\nLENGTH:" + localSums.length);
    return globalSum;
  }


  // generates a random array
  int[] randomArray(int n) {
    int[] array = new int[n];
    Random rd = new Random(5); // send in seed to get the same random sequence each time

    for (int i = 0; i < array.length; i++) {
      array[i] = rd.nextInt(10); // will give the next number in the random sequence that is lower than 10
    }

    return array;
  }


  // main method
  public static void main(String[] args) {
    int sumSeq = 0;
    int sumPar = 0;
    int n = Integer.parseInt(args[0]); // 100.000.000
    PrepSumOdd so = new PrepSumOdd(n);

    double[] timeSeq = new double[7];
    double[] timePar = new double[7];

    for (int i = 0; i < timeSeq.length; i++) {''
      // sequential
      long start = System.nanoTime();
      sumSeq = so.sumOddSeq();
      long end = System.nanoTime();
      timeSeq[i] = (end - start) / 1000000.0;

      // parallel
      start = System.nanoTime();
      sumPar = so.sumOddPar();
      end = System.nanoTime();
      timePar[i] = (end - start) / 1000000.0;
    }

    System.out.println("\nFind sum of odd numbers. n = " + n + ", cores = " + so.cores + "\n");

    // find the median
    Arrays.sort(timeSeq);
    double medianSeq = timeSeq[3];

    Arrays.sort(timePar);
    double medianPar = timePar[3];

    // print the results
    System.out.println("SEQUENTIAL");
    System.out.println("Sum\t:\t" + sumSeq);
    System.out.println("Time\t:\t" + medianSeq + " ms");

    System.out.println("\nPARALLEL");
    System.out.println("Sum\t:\t" + sumPar);
    System.out.println("Time\t:\t" + medianPar + " ms");

    System.out.println("\nSPEEDUP\t:\t" + medianSeq / medianPar);

    // UNSURE IF I SHOULD RUN 7 times...

    // System.out.println("GlobalArray\t:\n");
    // for (int num : so.globalArray) {
    //   System.out.println(num);
    // }

  }
}
