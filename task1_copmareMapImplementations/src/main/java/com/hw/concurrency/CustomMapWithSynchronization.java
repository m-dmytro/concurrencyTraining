package com.hw.concurrency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomMapWithSynchronization<K, V> {

  private final Map<K, V> map;

  CustomMapWithSynchronization(Map<K, V> map) {
    this.map = map;
  }

  public void put(K key, V value) {
    synchronized (map) {
      map.put(key, value);
    }
  }

  public Collection<V> values() {
    synchronized (map) {
      return new ArrayList<>(map.values());
    }
  }

}
