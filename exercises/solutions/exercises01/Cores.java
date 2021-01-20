class Cores {

  static class MyRunnable implements Runnable {

    int ind;

    MyRunnable(int ind) {
      this.ind = ind;
    }

    public void run() {
      System.out.println("Thread " + ind + " says hi!");
    }
  }


  public static void main(String[] args) {

    int cores = Runtime.getRuntime().availableProcessors();

    for (int i = 0; i < cores; i++) {
      (new Thread(new MyRunnable(i + 1))).start();
    }

    /*
      Note that '(new Thread(new MyRunnable(i + 1))).start()' is equivalent to:

      MyRunnable runnable = new MyRunnable(i + 1);
      Thread t = new Thread(runnable);
      t.start();

      (except that in the oneliner, you do not have a reference 't' to the Thread object)
    */

  }
}
