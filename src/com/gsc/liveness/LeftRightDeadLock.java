package com.gsc.liveness;

/**
 * Lock-Ordering Lock
 * 连个线程试图以不同的顺序获取相同的锁
 * @author shichaogeng
 *
 * 2017年9月28日
 */
public class LeftRightDeadLock {

	private final Object left = new Object();
	private final Object right = new Object();
	
	public void leftRight() {
		synchronized (left) {
			synchronized (right) {
				doSomething();
			}
		}
	}
	
	public void rightLeft() {
		synchronized (right) {
			synchronized (left) {
				doSomething();
			}
		}
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月28日
	 */
	private void doSomething() {}
}
