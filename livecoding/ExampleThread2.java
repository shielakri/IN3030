/*
Here we are creating our custom threads by implementing the Runnable interface.
This is more flexible than extending the Thread class as you can implement
multiple interfaces, but only extend one class.

This is the method I will use in this course.
*/

class ExampleThread2 {

  static class MyRunnable implements Runnable {

    int ind;

    MyRunnable(int ind) {
      this.ind = ind;
    }

    public void run() {
      System.out.println("Hi, I am a thread with id " + ind + "!");
    }

  }

  public static void main(String[] args) {

    MyRunnable r1 = new MyRunnable(1);
    Thread t1 = new Thread(r1);
    t1.start();

    try {
      // The main thread waits until thread t1 has finished running
      // Note: join is a non-static method and can be called on thread objects
      t1.join();
    } catch (Exception e) {
      System.out.println("Exception: t1.join()");
    }

    // This runs after t1 has finished executing (due to t1.join())
    MyRunnable r2 = new MyRunnable(2);
    Thread t2 = new Thread(r2);
    t2.start();


    try {
      // The main thread sleeps for 5 seconds
      // Note: sleep is a static method and should be called on the Thread class
      // 't2.sleep(5000)' will compile and run, but it's the same as 'Thread.sleep(5000)'
      // In other words:
      // It is not thread t2 that sleeps, but the thread that runs the line <<t2.sleep(5000)>>
      // In this case it is the main thread (the one that runs the main method)
      Thread.sleep(5000);
    } catch (Exception e) {
      System.out.println("Exception: Thread.sleep(5000)");
    }

    // This will run after the main thread has slept for 5 seconds (due to Thread.sleep(5000))
    MyRunnable r3 = new MyRunnable(3);
    Thread t3 = new Thread(r3);
    t3.start();

  }
}
