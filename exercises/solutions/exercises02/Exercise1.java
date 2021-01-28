/**
 * A class that measures the runtimes of the algorithms implemented in the
 * 'FindMax' class.
 *
 * Note: When you measure the algorithms, you should run them at least 7 times,
 * and take the median of these times before comparing the algorithms.
 *
 *
 * To use:
 *
 * Compile:
 * javac FindMax.java Exercise1.java
 *
 * Run:
 * java Exercise1 <n> (where <n> is the size of the array)
 *
 * 
 * @author Shiela Kristoffersen
 *
 */

class Exercise1 {

  /**
   * @param args Should recieve an integer 'n' that determines the size of the array.
   */
  public static void main(String[] args) {

    int n, seed = 25;

    /**
     * Catching excpetions in the case of wrong user input.
     */
    try {
      n = Integer.parseInt(args[0]);
    } catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("\nCorrect use of program is 'java Exercise1 <n>', where <n> is an integer.\n");
      return;
    } catch(NumberFormatException e) {
      System.out.println("\nPlease send in an integer <n>: 'java Exercise1 <n>'.\n");
      return;
    }


    FindMax fm = new FindMax(n, seed);

    System.out.println("\nTesting FindMax with n = " + n + " and cores = " + fm.cores + "\n");

    System.out.println("Sequential Algorithm");
    long start = System.nanoTime();
    fm.findMaxSeq();
    long end = System.nanoTime();
    double runtimeSeq = (end - start) / 1000000.0;
    System.out.println("Max\t:\t" + fm.globalMax);
    System.out.println("Time\t:\t" + runtimeSeq + " ms");


    System.out.println("\nParallel Algorithm 1.1");
    start = System.nanoTime();
    fm.findMaxPar_11();
    end = System.nanoTime();
    printInfo(fm.globalMax, start, end, runtimeSeq);


    System.out.println("\nParallel Algorithm 1.2");
    start = System.nanoTime();
    fm.findMaxPar_12();
    end = System.nanoTime();
    printInfo(fm.globalMax, start, end, runtimeSeq);


    System.out.println("\nParallel Algorithm 2.1");
    start = System.nanoTime();
    fm.findMaxPar_21();
    end = System.nanoTime();
    printInfo(fm.globalMax, start, end, runtimeSeq);


    System.out.println("\nParallel Algorithm 2.2");
    start = System.nanoTime();
    fm.findMaxPar_22();
    end = System.nanoTime();
    printInfo(fm.globalMax, start, end, runtimeSeq);

  }

  /**
   * Calculates runtime and speedup, and prints out information to the terminal.
   * @param max        The maximum integer found.
   * @param start      The start time of the algorithm.
   * @param end        The end time of the algorithm.
   * @param runtimeSeq The runtime of the sequential algorithm.
   */
  static void printInfo(int max, long start, long end, double runtimeSeq) {

    double runtimePar = (end - start) / 1000000.0;
    System.out.println("Max\t:\t" + max);
    System.out.println("Time\t:\t" + runtimePar + " ms");
    System.out.println("Speedup\t:\t" + runtimeSeq / runtimePar);
  }

}
