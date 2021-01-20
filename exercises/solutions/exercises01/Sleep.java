/*
  Solution to Exercise 3 and 4.
*/

class Sleep {

  static class MyRunnable implements Runnable {

    int ind;

    MyRunnable(int ind) {
      this.ind = ind;
    }

    public void run() {
      System.out.println("Thread " + ind + " saying good night!");
      try {
        Thread.sleep(ind * 1000);
      } catch(Exception e) {
        System.out.println("Exception: t.sleep(ind * 1000)");
      }
      System.out.println("Thread " + ind + " saying good morning!");
    }
  }


  public static void main(String[] args) {

    long start = System.nanoTime();

    Thread t1 = new Thread(new MyRunnable(1));
    Thread t2 = new Thread(new MyRunnable(2));
    Thread t3 = new Thread(new MyRunnable(3));

    t1.start();
    t2.start();
    t3.start();

    // The main thread waits for all the threads to finish before checking the time.
    try {
      t1.join();
      t2.join();
      t3.join();
    } catch(Exception e) {
      e.printStackTrace();
    }

    long end = System.nanoTime();

    System.out.println("Time spent: " + (end - start) / 1000000000 + " seconds!");


    /*
      Some of you might have used the sleep() method in the main method,
      instead of in the run() method :

      for (int i = 1; i <= 3; i++) {
        Thread t = new Thread(new MyRunnable(i));
        t.start();

        try {
          t.sleep(i * 1000);
        } catch(Exception e) {
          System.out.println("Exception: t.sleep(i * 1000)");
        }
      }

      This will not work as t.sleep() is equivalent to Thread.sleep(),
      and using Thread.sleep() in the main method will make the main
      thread sleep.
    */

  }

}
