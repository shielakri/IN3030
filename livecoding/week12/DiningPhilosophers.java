/**
 * @author Shiela Kristoffersen.
 *
 * To compile:
 * javac DiningPhilosophers.java
 *
 * To run:
 * java DiningPhilosophers <X>
 *
 * where X is the number of philosophers.
 *
 * Note that you will have a deadlock in all of the solutions
 * if you set X = 1.
 *
 * Can you figure out why? :D.
 *
 */

import java.util.concurrent.Semaphore;

class DiningPhilosophers {


  class Philosopher implements Runnable {

    int ind;
    Semaphore rightFork, leftFork;

    Philosopher(int ind, Semaphore rightFork, Semaphore leftFork) {
      this.ind = ind;
      this.rightFork = rightFork;
      this.leftFork = leftFork;
    }


    // Simulating a philosopher thinking.
    void think() {
      System.out.println("Philosopher " + ind + " is thinking.");
    }


    // Simulating a philosopher eating.
    void eat() {

      try {

        System.out.println("\u001B[33mPhilosopher " + ind + " wants to eat\u001B[0m");
        rightFork.acquire();

        System.out.println("Philosopher " + ind + " has acquired right fork.");
        leftFork.acquire();

        System.out.println("\u001B[32mPhilosopher " + ind + " is eating.\u001B[0m");

      } catch (Exception e) {

        System.out.println("Perform heimlich");

      } finally {

        rightFork.release();
        leftFork.release();
        System.out.println("Philosopher " + ind + " has finished eating.");

      }
    }


    // Simulating a philosopher's life.
    public void run() {

      int i = 0;

      while (i++ < 20) {
        think();
        eat();
      }
    }
  }


  void startDining(int numOfPhilosophers) {

    Semaphore rightFork = new Semaphore(1);
    Semaphore leftFork, firstFork = rightFork;

    // Setting the table clockwise
    for (int i = 0; i < numOfPhilosophers; i++) {


      // If we have reached the last philosopher, then his left fork will
      // be the first philosophers right fork, thus closing the circle.
      leftFork = (i == numOfPhilosophers - 1) ? firstFork : new Semaphore(1);


      // if (false)         // Uncomment to see the solution that leads to a deadlock.
      // if (i == 0)        // Solution: One philosopher picks up his left fork first.
      if (i % 2 == 0)       // Better solution: Every other philosopher picks up the left fork first.
        (new Thread(new Philosopher(i, leftFork, rightFork))).start();
      else
        (new Thread(new Philosopher(i, rightFork, leftFork))).start();


      // The next philosopher's right fork will be this philosopher's left fork.
      rightFork = leftFork;
    }
  }


  // Send in the number of philosophers as argument.
  public static void main(String[] args) {

    DiningPhilosophers dp = new DiningPhilosophers();
    dp.startDining(Integer.parseInt(args[0]));

  }
}
