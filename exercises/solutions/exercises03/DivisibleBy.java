/**
 * Prints out an ordered sequence of number from 'start' up until and including
 * 'end', and highlights all the numbers divisble by 'n'.
 *
 * This is done using two different Semaphores and two different Workers.
 *
 * 
 * To run:
 * javac DivisibleBy.java
 * java DivisibleBy <n> <start> <end>
 *
 */

import java.util.concurrent.Semaphore;

class DivisibleBy {

  int n, start, end;
  Semaphore notDiv = new Semaphore(0);
  Semaphore div = new Semaphore(0);

  DivisibleBy(int n, int start, int end) {
    this.n = n;
    this.start = start;
    this.end = end;
  }

  /**
   * Prints out the numbers not divisble by n
   */
  class NotDivisibleWorker implements Runnable {

    public void run() {

      for (int i = start; i <= end; i++) {

        /**
         * If divisble, release the divisible semaphore so that
         * DivisbleWorker can print out the number.
         */
        if (i % n == 0) {
          div.release();

          /**
           * Try to acquire the not divisible semaphore so that this worker
           * can continue printing out numbers not divisble by n.
           */
          try {
            notDiv.acquire();
          } catch(Exception e) {
            e.printStackTrace();
          }

        } else {
          System.out.print("\u001B[0m" + i + "\t");
        }
      }
    }

  }

  /**
   * Prints out the numbers divisble by n
   */
  class DivisibleWorker implements Runnable {

    public void run() {
      /**
       * Correct starting point is very important! If it is not correct, you
       * will acquire more than you release, and you will get a deadlock.
       */
      int localStart = (start % n == 0) ? start : (start - (start % n) + n);

      for (int i = localStart; i <= end; i += n) {

        /**
         * When the divisible semaphore is aquired, it can print out the
         * number divisible by n.
         */
        try {
          div.acquire();
        } catch(Exception e) {
          e.printStackTrace();
        }

        System.out.print("\u001B[33m" + i + "\t");
        /**
         * After printing the number, it releases the not divisible semaphore,
         * to let NotDivisbleWorker continue printing the not divisible numbers.
         */
        notDiv.release();
      }
    }

  }

  /**
   * Prints out the sequence of numbers, where the numbers divisble by n
   * are highlighted.
   */
  void printHighlightedDividables() {
    Thread notDiv = new Thread(new NotDivisibleWorker());
    Thread div = new Thread(new DivisibleWorker());

    notDiv.start();
    div.start();

    try {
      notDiv.join();
      div.join();
    } catch(Exception e) {
      e.printStackTrace();
    }

    System.out.println("\u001B[0m");

  }

  public static void main(String[] args) {

    int start, end, n;

    try {
      n = Integer.parseInt(args[0]);
      start = Integer.parseInt(args[1]);
      end = Integer.parseInt(args[2]);
    } catch(Exception e) {
      System.out.println("Correct use of program is: java DivisibleBy <n> <start> <end>");
      return;
    }

    DivisibleBy db = new DivisibleBy(n, start, end);
    db.printHighlightedDividables();
  }
}
