/*
  Here we are creating our custom threads by extending Java's Thread class.
  This is less flexible than implementing the interface Runnable as a class
  in Java can only extend ONE class.
*/

class ExampleThread {

  static class MyThread extends Thread {

    public void run() {
      System.out.println("Hi, I am a thread!");
    }
  }

  public static void main(String[] args) {

    MyThread t = new MyThread();
    t.start();
  }
}
