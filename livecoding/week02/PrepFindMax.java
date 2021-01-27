import java.util.Random;
import java.util.Arrays;

class PrepFindMax {
  int n = 100000000;
  int[] globalArray = randomArray(n);
  int cores = Runtime.getRuntime().availableProcessors();


  /**
  * @return the largest number in the array
  */
  int findMaxSeq() {
    int max = globalArray[0];
    for (int i = 0; i < globalArray.length; i++)
      if (max < globalArray[i]) max = globalArray[i];

    return max;
  }



  /**
  * @return array of random numbers. always the same sequence
  */
  int[] randomArray(int n) {
    // sending in a seed in order to get the same random sequence each time
    Random rd = new Random(10L);
    int[] a = new int[n];

    for (int i = 0; i < n; i++)
      a[i] = rd.nextInt();

    return a;
  }



  class Worker implements Runnable {

    int ind, startindex, endindex, localmax;

    Worker(int ind, int startindex, int endindex) {
      this.ind = ind;
      this.startindex = startindex;
      this.endindex = endindex;
    }

    public void run() {
      for (int i = startindex; i < endindex; i++)
        if (localmax < globalArray[i]) localmax = globalArray[i];
    }
  }


  int findMaxPar() {
    int size = n / cores;
    Worker[] workers = new Worker[cores];
    Thread[] threads = new Thread[cores];

    for (int i = 0; i < cores; i++) {

      if (i == cores - 1) {
        workers[i] = new Worker(i, i * size, n);
      } else {
        workers[i] = new Worker(i, i * size, (i + 1) * size);
      }

      threads[i] = new Thread(workers[i]);
      threads[i].start();

    }

    int globalmax = globalArray[0];

    for (int i = 0; i < cores; i++) {

      try {
        threads[i].join();
      } catch(Exception e) {
        e.printStackTrace();
      }

      if (globalmax < workers[i].localmax) globalmax = workers[i].localmax;
    }

    return globalmax;

  }


  public static void main(String[] args) {
    PrepFindMax fm = new PrepFindMax();
    int maxSeq = 0;
    int maxPar = 0;

    System.out.println("Number of cores = " + fm.cores);
    System.out.println("Sequential(ms)\t\tParallel(ms)\t\tSpeedup");

    for (int i = 0; i < 7; i++) {
      // timing sequential algorithm
      long start = System.nanoTime();
      maxSeq = fm.findMaxSeq();
      long end = System.nanoTime();

      double timeSeq = (end - start) / 1000000.0;
      System.out.printf("%.5f", timeSeq);

      // timing parallel algorithm
      start = System.nanoTime();
      maxPar = fm.findMaxPar();
      end = System.nanoTime();

      double timePar = (end - start) / 1000000.0;
      System.out.printf("\t\t\t%.5f", timePar);

      // calculating speedup
      System.out.printf("\t\t\t%.5f\n", timeSeq / timePar);
    }


    System.out.println("");
    System.out.println("Max (seq)\t:\t" + maxSeq);
    System.out.println("Max (par)\t:\t" + maxPar);
    // sorts in ascending order
    //Arrays.sort(fm.globalArray);
    //System.out.println("Max (sort)\t:\t" + fm.globalArray[fm.globalArray.length - 1]);
  }


}
