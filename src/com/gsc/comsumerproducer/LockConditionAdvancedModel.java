package com.gsc.comsumerproducer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 16:35
 */
public class LockConditionAdvancedModel implements Model {

    private final Lock CONSUMER_LOCK = new ReentrantLock();
    private final Condition NOT_EMPTY = CONSUMER_LOCK.newCondition();
    private final Lock PRODUCER_LOCK = new ReentrantLock();
    private final Condition NOT_FULL = PRODUCER_LOCK.newCondition();
    private final Buffer<Task> BUFFER = new Buffer<>();
    private int cap;
    private final AtomicInteger increTaskNo = new AtomicInteger();

    @Override
    public Runnable newRunnableConsumer() {
        return new ConsumerImpl();
    }

    @Override
    public Runnable newRunnableProducer() {
        return new ProducerImpl();
    }

    private class ConsumerImpl extends AbstractConsumer {

        @Override
        public void consume() throws InterruptedException {
            CONSUMER_LOCK.lockInterruptibly();
            try {
                while (BUFFER.isEmpty()) {
                    System.out.println("BUFFER is empty");
                    NOT_EMPTY.await();
                }

                Task task = BUFFER.poll();
                assert task != null;
                Thread.sleep(500 + (long) (Math.random() * 500));
                System.out.println("consume:"+task.getNo()+"size="+BUFFER.size());
                if (!BUFFER.isEmpty()) {
                    NOT_EMPTY.signalAll();
                }
            } finally {
                CONSUMER_LOCK.unlock();
            }

            if (BUFFER.size() < cap) {
                PRODUCER_LOCK.lockInterruptibly();
                try {
                    NOT_FULL.signalAll();
                } finally {
                    PRODUCER_LOCK.unlock();
                }
            }
        }
    }

    private class ProducerImpl extends AbstractProducer {

        @Override
        public void produce() throws InterruptedException {
            Thread.sleep((long) (Math.random() * 1000));
            PRODUCER_LOCK.lockInterruptibly();

            try {
                while (BUFFER.size() == cap) {
                    System.out.println("buffer is full...");
                    NOT_FULL.await();
                }

                Task task = new Task(increTaskNo.getAndIncrement());
                BUFFER.offer(task);
                System.out.println("produce:"+task.getNo()+"size="+BUFFER.size());
                if (BUFFER.size() < cap) {
                    NOT_FULL.signalAll();
                }
            } finally {
                PRODUCER_LOCK.unlock();
            }

            if (BUFFER.isEmpty()) {
                CONSUMER_LOCK.lockInterruptibly();

                try {
                    NOT_EMPTY.signalAll();
                } finally {
                    CONSUMER_LOCK.unlock();
                }
            }
        }
    }

    private class Buffer<Item> {

        private Node head;
        private Node tail;
        private final AtomicInteger bufSize = new AtomicInteger(0);

        private class Node {
            Item item;
            Node next;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public int size() {
            return bufSize.get();
        }

        //enqueue,add to the last
        public void offer(Item item) {
            Node oldTail = tail;
            tail = new Node();
            tail.item = item;
            tail.next = null;
            if (!isEmpty()) {
                oldTail.next = tail;
            } else {
                head = tail;
            }

            bufSize.incrementAndGet();
        }

        //dequeue,get from the first
        public Item poll() {
            Item item = null;
            if (!isEmpty()) {
                item = head.item;
                head = head.next;
                if (isEmpty()) {
                    tail = head;
                }
                bufSize.decrementAndGet();
            }
            return item;
        }
    }

    public static void main(String[] args) {
        Model model = new BlockingQueueModel(3);
        for (int i = 0; i < 5; i++) {
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0; i < 1; i++) {
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
