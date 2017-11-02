package com.gsc.comsumerproducer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 15:44
 */
public class LockConditionModel implements Model {

    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    private final Lock BUFFER_LOCK = new ReentrantLock();
    private final Condition BUFFER_COND = BUFFER_LOCK.newCondition();
    private final Queue<Task> BUFFER = new LinkedList<>();
    private int cap;

    public LockConditionModel(int cap) {
        this.cap = cap;
    }

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

            BUFFER_LOCK.lockInterruptibly();
            try {
                while (BUFFER.size() == 0) {
                    BUFFER_COND.await();
                }
                Task task = BUFFER.poll();
                assert task != null;
                Thread.sleep(500 + (long) Math.random() * 500);
                System.out.println("consume：" + task.getNo());
                BUFFER_COND.signalAll();
            } finally {
                BUFFER_LOCK.unlock();
            }
        }
    }

    private class ProducerImpl extends AbstractProducer {

        @Override
        public void produce() throws InterruptedException {

            Thread.sleep((long) Math.random() * 1000);
            BUFFER_LOCK.lockInterruptibly();
            try {
                while (BUFFER.size() == cap) {
                    BUFFER_COND.await();
                }
                Task task = new Task(increTaskNo.getAndIncrement());
                BUFFER.offer(task);
                System.out.println("produce:" + task.getNo() + " size=" + BUFFER.size());
                BUFFER_COND.signalAll();
            } finally {
                BUFFER_LOCK.unlock();
            }

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
