package com.hw.concurrency;

import org.junit.Test;

import java.util.*;

public class CustomMapWithSynchronizationTest {

  @Test
  public void test() throws InterruptedException {

    CustomMapWithSynchronization map = new CustomMapWithSynchronization(new HashMap<Integer, Integer>());

    Thread threadPut = new Thread(new PutThread(map), "put-thread");
    Thread threadSum = new Thread(new SumThread(map), "sum-thread");

    threadPut.start();
    threadSum.start();

    threadPut.join();
    threadSum.join();
  }

  private class PutThread implements Runnable {

    private final CustomMapWithSynchronization map;

    PutThread(CustomMapWithSynchronization map) {
      this.map = map;
    }

    @Override
    public void run() {
      int i = 0;
      long startTime = System.currentTimeMillis();

      while(true) {
        i++;
        map.put(i, 1);
        //        System.out.println(Thrmead.currentThread() + ": put " + i);

        if (i%1_000_000 == 0) {
          long now = System.currentTimeMillis();
          long elapsed = now - startTime;
          System.out.println(Thread.currentThread() + ": "+ 1_000_000 +" put operations took " + elapsed + " msec");
          startTime = now;
        }
      }
    }
  }

  private class SumThread implements Runnable {

    private final CustomMapWithSynchronization map;

    SumThread(CustomMapWithSynchronization map) {
      this.map = map;
    }

    @Override
    public void run() {
      int numberOfOperations = 0;
      int sumOfValues = 0;
      long startTime = System.currentTimeMillis();

      while (true) {
        Iterator<Integer> it = map.values().iterator();
        try {
          while (it.hasNext()) {
            Integer key = it.next();
            sumOfValues += key;
            //            System.out.println(Thread.currentThread() + ": sum " + sumOfValues);

            numberOfOperations++;
            if (numberOfOperations%1_000_000 == 0) {
              long now = System.currentTimeMillis();
              long elapsed = now - startTime;
              System.out.println(Thread.currentThread() + ": "+ 1_000_000 +" sum operations took " + elapsed + " msec");
              startTime = now;
            }
          }
        } catch (ConcurrentModificationException ex) {
          ex.printStackTrace();
          System.out.println("ConcurrentModificationException happened");
        }
      }
    }
  }

}
