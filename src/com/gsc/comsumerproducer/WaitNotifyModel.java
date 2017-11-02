package com.gsc.comsumerproducer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 15:44
 */
public class WaitNotifyModel implements Model {

    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    private final Object BUFFER_LOCK = new Object();
    private final Queue<Task> BUFFER = new LinkedList<>();
    private int cap;

    public WaitNotifyModel(int cap) {
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
            synchronized (BUFFER_LOCK) {
                if (BUFFER.size() == 0) {
                    BUFFER_LOCK.wait();
                }
            }

            Task task = BUFFER.poll();
            assert task != null;
            Thread.sleep(500+(long)Math.random()*500);
            System.out.println("consume："+task.getNo());
            BUFFER_LOCK.notifyAll();
        }
    }

    private class ProducerImpl extends AbstractProducer {

        @Override
        public void produce() throws InterruptedException {

            synchronized (BUFFER_LOCK) {
                if (BUFFER.size() == cap) {
                    BUFFER_LOCK.wait();
                }

                Thread.sleep((long)Math.random()*1000);
                Task task = new Task(increTaskNo.getAndIncrement());
                BUFFER.offer(task);
                System.out.println("produce:"+task.getNo()+" size="+BUFFER.size());
                BUFFER_LOCK.notifyAll();

            }
        }
    }


    public static void main(String[] args) {
        Model model = new BlockingQueueModel(3);
        for (int i = 0; i < 2; i++) {
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
