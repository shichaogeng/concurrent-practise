package com.gsc.comsumerproducer;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2017-11-02 14:36
 */
public interface Model {
    Runnable newRunnableConsumer();
    Runnable newRunnableProducer();
}
