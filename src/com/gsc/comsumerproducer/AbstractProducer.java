package com.gsc.comsumerproducer;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 14:34
 */
public abstract class AbstractProducer implements Producer, Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
