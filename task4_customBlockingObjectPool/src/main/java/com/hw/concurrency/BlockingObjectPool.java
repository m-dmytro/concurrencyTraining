package com.hw.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool that block when it has not any items or it full

 */
public class BlockingObjectPool {

  private final Lock lock = new ReentrantLock();
  private final Condition noDataOverchargingCondition = lock.newCondition();
  private final Condition someDataAvailableCondition = lock.newCondition();
  private int currentNumberOfAvailablePlaces;
  private int initialNumberOfAvailablePlaces;

  private final List<Object> pool;

  /**
   * Creates filled pool of passed size
   *
   * @param size of pool
   */
  public BlockingObjectPool(int size) {
    lock.lock();
    try {
      this.currentNumberOfAvailablePlaces = size;
      this.initialNumberOfAvailablePlaces = size;

      pool = new ArrayList<>(size);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Gets object from pool or blocks if pool is empty
   *
   * @return object from pool
   */
  public Object get() throws InterruptedException {
    lock.lock();
    try {
      while (pool.size() == 0 || currentNumberOfAvailablePlaces >= initialNumberOfAvailablePlaces) {
        someDataAvailableCondition.await();
      }
      ++currentNumberOfAvailablePlaces;
      noDataOverchargingCondition.signal();

      Object result = pool.get(0);
      pool.remove(0);
      return result;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Puts object to pool or blocks if pool is full
   *
   * @param object to be taken back to pool
   */
  public void take(Object object) throws InterruptedException {
    lock.lock();
    try {
      while (currentNumberOfAvailablePlaces <= 0) {
        noDataOverchargingCondition.await();
      }
      pool.add(object);
      --currentNumberOfAvailablePlaces;
      someDataAvailableCondition.signal();
    } finally {
      lock.unlock();
    }
  }

}
