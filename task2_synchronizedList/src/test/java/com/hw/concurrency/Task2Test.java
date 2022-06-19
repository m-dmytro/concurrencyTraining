package com.hw.concurrency;

import org.junit.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Task2Test {

  @Test
  public void test() throws InterruptedException {
    ThreadSafeCollection list = new ThreadSafeCollection(new ArrayList<Integer>());

    Thread threadPut = new Thread(new PutThread(list), "put-thread");
    Thread threadSum = new Thread(new SumThread(list), "sum-thread");
    Thread threadSquareRoot = new Thread(new SquareRootThread(list), "sr-thread");

    threadPut.start();
    threadSum.start();
    threadSquareRoot.start();

    threadPut.join();
    threadSum.join();
    threadSquareRoot.join();
  }

  private class PutThread implements Runnable {

    private final ThreadSafeCollection list;

    PutThread(ThreadSafeCollection list) {
      this.list = list;
    }

    @Override
    public void run() {
      while(true) {
        list.add(1);
        System.out.println(Thread.currentThread() + ": put ");
      }
    }
  }

  private class SumThread implements Runnable {

    private final ThreadSafeCollection list;

    SumThread(ThreadSafeCollection list) {
      this.list = list;
    }

    @Override
    public void run() {
      int sumOfValues = 0;

      while (true) {
        try {
          for (Integer item: (List<Integer>) list.getList()) {
            sumOfValues+=item;
            System.out.println(Thread.currentThread() + ": sum " + sumOfValues);
          }
        } catch (ConcurrentModificationException ex) {
          ex.printStackTrace();
          System.out.println("ConcurrentModificationException happened");
        }
      }
    }
  }

  private class SquareRootThread implements Runnable {

    private final ThreadSafeCollection list;

    SquareRootThread(ThreadSafeCollection list) {
      this.list = list;
    }

    @Override
    public void run() {
      int sumOfSquares = 0;
      while (true) {
        try {
          for (Integer item: (List<Integer>) list.getList()) {
            sumOfSquares = sumOfSquares + item*item;
            double result = Math.sqrt(sumOfSquares);
            System.out.println(Thread.currentThread() + ": square root " + result);
          }
        } catch (ConcurrentModificationException ex) {
          ex.printStackTrace();
          System.out.println("ConcurrentModificationException happened");
        }
      }
    }
  }

}
