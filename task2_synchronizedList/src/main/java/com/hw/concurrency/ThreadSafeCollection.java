package com.hw.concurrency;

import java.util.ArrayList;
import java.util.List;

public class ThreadSafeCollection<V> {

  private final List<V> list;

  ThreadSafeCollection(List<V> list) {
    this.list = list;
  }

  public void add(V item) {
    synchronized (list) {
      list.add(item);
    }
  }

  public List<V> getList() {
    synchronized (list) {
      return new ArrayList<>(list);
    }
  }

}
