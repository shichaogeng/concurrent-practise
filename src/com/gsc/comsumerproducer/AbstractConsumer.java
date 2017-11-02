package com.gsc.comsumerproducer;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 14:33
 */
public abstract class AbstractConsumer implements Consumer, Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
