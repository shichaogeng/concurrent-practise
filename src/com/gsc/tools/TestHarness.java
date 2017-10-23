package com.gsc.tools;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁CountDownLatch
 * 计算并发执行某个任务所需时间
 * 
 * @author shichaogeng
 *
 * 2017年9月27日
 */
public class TestHarness {

	public long TimeTask(int nThread, final Runnable task) throws InterruptedException {
		
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThread);
		
		for (int i = 0; i < nThread; i++) {
			new Thread(){
				public void run() {
					try {
						//线程阻塞等待所有线程准备完成
						startGate.await();
						try {
							task.run();
						} finally {
							//表示当前线程执行结束
							endGate.countDown();
						}
					} catch (InterruptedException e) {}
				};
			}.start();
		}
		
		long start = System.nanoTime();
		//准备完成，开始所有线程，await阻塞消失
		startGate.countDown();
		//等待所有线程释放完毕
		endGate.await();
		long end = System.nanoTime();

		return end -start;
	}
}
