package com.hw.concurrency;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class SeveralTopicsTest {

  @Test
  public void test_1Producer_1Consumer() throws InterruptedException {
    Map<String, CustomQueue<String>> map = new HashMap<>();
    map.put("topic1", new CustomQueue<>(new PriorityQueue<String>()));
    map.put("topic2", new CustomQueue<>(new PriorityQueue<String>()));

    CustomBroker<String> broker = new CustomBroker<>(map);

    Thread threadProducer = new Thread(new ProducerThread(1, broker.getQueue("topic1")), "producer-thread");
    Thread threadConsumer = new Thread(new ConsumerThread(broker.getQueue("topic1")), "consumer-thread");

    threadProducer.start();
    threadConsumer.start();

    threadProducer.join();
    threadConsumer.join();
  }

  @Test
  public void test_1Producers_2Consumers() throws InterruptedException {
    Map<String, CustomQueue<String>> map = new HashMap<>();
    map.put("topic1", new CustomQueue<>(new PriorityQueue<String>()));
    map.put("topic2", new CustomQueue<>(new PriorityQueue<String>()));

    CustomBroker<String> broker = new CustomBroker<>(map);

    Thread threadProducer1 = new Thread(new ProducerThread(1, broker.getQueue("topic1")), "producer-1-thread");

    Thread threadConsumer1 = new Thread(new ConsumerThread(broker.getQueue("topic1")), "consumer-1-thread");
    Thread threadConsumer2 = new Thread(new ConsumerThread(broker.getQueue("topic1")), "consumer-2-thread");

    threadProducer1.start();
    threadConsumer1.start();
    threadConsumer2.start();

    threadProducer1.join();
    threadConsumer1.join();
    threadConsumer2.join();
  }

  @Test
  public void test_2Producers_2Consumers() throws InterruptedException {
    Map<String, CustomQueue<String>> map = new HashMap<>();
    map.put("topic1", new CustomQueue<>(new PriorityQueue<String>()));
    map.put("topic2", new CustomQueue<>(new PriorityQueue<String>()));

    CustomBroker<String> broker = new CustomBroker<>(map);

    Thread threadProducer1 = new Thread(new ProducerThread(1, broker.getQueue("topic1")), "producer-1-thread");
    Thread threadProducer2 = new Thread(new ProducerThread(2, broker.getQueue("topic1")), "producer-2-thread");

    Thread threadConsumer1 = new Thread(new ConsumerThread(broker.getQueue("topic1")), "consumer-1-thread");
    Thread threadConsumer2 = new Thread(new ConsumerThread(broker.getQueue("topic1")), "consumer-2-thread");

    threadProducer1.start();
    threadProducer2.start();
    threadConsumer1.start();
    threadConsumer2.start();

    threadProducer1.join();
    threadProducer2.join();
    threadConsumer1.join();
    threadConsumer2.join();
  }

  private class ProducerThread implements Runnable {

    private final int id;
    private final CustomQueue<String> queue;

    ProducerThread(int producerId, CustomQueue<String> queue) {
      this.id = producerId;
      this.queue = queue;
    }

    @Override
    public void run() {
      int i = 0;
      while(true) {
        String message = "Message producerId " + id + " text " + i;
        queue.offer(message);
        System.out.println(Thread.currentThread() + ": produce \"" + message + "\"");
        i++;

        try {
          Thread.sleep(0, 100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private class ConsumerThread implements Runnable {

    private final CustomQueue<String> queue;

    ConsumerThread(CustomQueue<String> queue) {
      this.queue = queue;
    }

    @Override
    public void run() {
      while(true) {
        String message = queue.poll();
        if (message != null) {
          System.out.println(Thread.currentThread() + ": consume \"" + message + "\"");
        }
      }
    }
  }

}
