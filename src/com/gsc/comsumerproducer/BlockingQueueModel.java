package com.gsc.comsumerproducer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 14:40
 */
public class BlockingQueueModel implements Model {
    private final BlockingQueue<Task> workQueue;

    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    public BlockingQueueModel(int cap) {
        this.workQueue = new LinkedBlockingDeque<>(cap);
    }

    @Override
    public Runnable newRunnableConsumer() {
        return new ConsumerImpl();
    }

    @Override
    public Runnable newRunnableProducer() {
        return new ProducerImpl();
    }

    private class ConsumerImpl extends AbstractConsumer{

        @Override
        public void consume() throws InterruptedException {
            Task task = workQueue.take();
            Thread.sleep(500+(long)Math.random()*500);
            System.out.println("consume:"+task.getNo()+" size="+workQueue.size());
        }
    }

    private class ProducerImpl extends AbstractProducer {

        @Override
        public void produce() throws InterruptedException {
            Thread.sleep((long)Math.random()*1000);
            Task task = new Task(increTaskNo.getAndIncrement());
            workQueue.put(task);
            System.out.println("produce:"+task.getNo()+" size="+workQueue.size());
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
