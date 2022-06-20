package com.hw.concurrency;

import org.junit.Test;

public class BlockingObjectPoolTest {

  @Test
  public void test() throws InterruptedException {

    BlockingObjectPool pool = new BlockingObjectPool(10);

    Thread threadPut = new Thread(new PutThread(pool), "put-thread");
    Thread threadGet = new Thread(new GetThread(pool), "get-thread");

    threadPut.start();
    threadGet.start();

    threadPut.join();
    threadGet.join();
  }

  private class PutThread implements Runnable {

    private final BlockingObjectPool pool;

    PutThread(BlockingObjectPool pool) {
      this.pool = pool;
    }

    @Override
    public void run() {
      while(true) {
        try {
          pool.take(new Object());
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread() + ": Exception during put operations");
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread() + ": put");
      }
    }
  }

  private class GetThread implements Runnable {

    private final BlockingObjectPool pool;

    GetThread(BlockingObjectPool pool) {
      this.pool = pool;
    }

    @Override
    public void run() {
      while(true) {
        try {
          pool.get();
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread() + ": Exception during get operations");
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread() + ": get ");
      }
    }
  }

}
