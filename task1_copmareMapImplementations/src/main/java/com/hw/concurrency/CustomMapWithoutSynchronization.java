package com.hw.concurrency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomMapWithoutSynchronization {

  private Map<Integer, Integer> map;

  CustomMapWithoutSynchronization() {
    this.map = new HashMap<>();
  }

  public void put(Integer key, Integer value) {
    Map<Integer,Integer> result = new HashMap<>(map);
    result.put(key, value);
    map = result;
  }

  public Collection<Integer> values() {
      return new ArrayList<>(map.values());
  }

}
