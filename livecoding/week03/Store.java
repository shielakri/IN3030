import java.util.concurrent.Semaphore;

class Store {
  int maxCustomers = 2;
  Semaphore guard = new Semaphore(maxCustomers);


  void shop(int ind) {
    System.out.println("Customer " + ind + " is shopping! YEY!");

    try {
      Thread.sleep((ind + 1) * 4000);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  class Customer implements Runnable {
    int ind;

    Customer(int ind) {
      this.ind = ind;
    }

    public void run() {

      try {
        System.out.println("\u001B[31mCustomer " + ind + " is waiting to enter the store.\u001B[0m");
        guard.acquire();
      } catch (Exception e) {
        e.printStackTrace();
      }

      shop(ind);

      System.out.println("\u001B[33mCustomer " + ind + " has finished shopping and is leaving the store.\u001B[0m");
      guard.release();
    }
  }

  public static void main(String[] args) {

    Store kiwi = new Store();

    for (int i = 0; i < 3; i++) {
      (new Thread(kiwi.new Customer(i))).start();
    }
  }
}
