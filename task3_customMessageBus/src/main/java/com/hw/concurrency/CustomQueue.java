package com.hw.concurrency;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomQueue<V> {

  private final Queue<V> queue;

  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readL = lock.readLock();
  private final Lock writeL = lock.writeLock();

  CustomQueue(Queue<V> queue) {
    this.queue = queue;
  }

  public boolean offer(V item) {
    writeL.lock();
    try {
      return queue.offer(item);
    } finally {
      writeL.unlock();
    }
  }

  public V poll() {
    readL.lock();
    try {
      return queue.poll();
    } finally {
      readL.unlock();
    }
  }

}
