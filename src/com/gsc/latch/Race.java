package com.gsc.latch;

import java.util.concurrent.CountDownLatch;

/**
 * @Description
 * 所有选手就位后，裁判鸣枪开始比赛
 * 选手到达终点或中途退赛后，记录成绩
 * 所有选手都已记录成绩后，结束比赛
 * 就位->开始->终点->结束 每个点一个latch
 * @Author shichaogeng
 * @Create 2017-11-02 17:51
 */
public class Race {

    public static void main(String[] args) throws InterruptedException {
        new Race().raceTask(5);
    }

    public void raceTask(int nThread) throws InterruptedException {
        final CountDownLatch readyRace = new CountDownLatch(nThread);//nThread名参赛者
        final CountDownLatch startRace = new CountDownLatch(1);
        final CountDownLatch endRace = new CountDownLatch(nThread);//nThread名参赛者

        for (int i = 0; i < nThread; i++) {
            final int raceNo = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("ready: "+ raceNo);
                    readyRace.countDown();
                    try {
                        startRace.await();
                        System.out.println("start: "+raceNo);
                    } catch (InterruptedException e) {
                        System.out.println("I am hurt, and have to interrupt the race...");
                        Thread.currentThread().interrupt();
                    } finally {
                        System.out.println("end: "+raceNo);
                        endRace.countDown();
                    }
                }
            }).start();
        }

        //选手准备
        readyRace.await();
        System.out.println("********************** all ready!!! **********************");
        System.out.println("********************** will start soon **********************");
        //开始
        startRace.countDown();
        //等待所有人完成
        endRace.await();
        System.out.println("********************** all end!!! **********************");

    }
}

