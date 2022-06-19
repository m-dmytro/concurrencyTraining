package com.hw.concurrency;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomBroker<V> {

  private final Map<String, CustomQueue<V>> map;

  CustomBroker(Map<String, CustomQueue<V>> bunchOfTopics) {
    this.map = bunchOfTopics;
  }

  public CustomQueue<V> getQueue(String topicName) {
    CustomQueue<V> queue = map.get(topicName);

    if (queue != null) {
      return queue;
    } else {
      throw new IllegalArgumentException("Topic is not exist");
    }
  }

}
