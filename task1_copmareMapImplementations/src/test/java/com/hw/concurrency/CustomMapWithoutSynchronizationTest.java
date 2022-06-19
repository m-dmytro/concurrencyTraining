package com.hw.concurrency;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

public class CustomMapWithoutSynchronizationTest {

  @Test
  public void test() throws InterruptedException {

    CustomMapWithoutSynchronization map = new CustomMapWithoutSynchronization();

    Thread threadPut = new Thread(new PutThread(map), "put-thread");
    Thread threadSum = new Thread(new SumThread(map), "sum-thread");

    threadPut.start();
    threadSum.start();

    threadPut.join();
    threadSum.join();
  }

  private class PutThread implements Runnable {

    private final CustomMapWithoutSynchronization map;

    PutThread(CustomMapWithoutSynchronization map) {
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

    private final CustomMapWithoutSynchronization map;

    SumThread(CustomMapWithoutSynchronization map) {
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
