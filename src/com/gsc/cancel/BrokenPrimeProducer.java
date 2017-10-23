package com.gsc.cancel;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * @author shichaogeng
 *
 * 2017年9月27日
 */
public class BrokenPrimeProducer extends Thread {

	private final BlockingQueue<BigInteger> queue;
	
	private volatile boolean cancelled = false;
	
	public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月27日
	 */
	@Override
	public void run() {
		while (!cancelled) {
			BigInteger p = BigInteger.ONE;
			try {
				queue.put(p.nextProbablePrime());
			} catch (InterruptedException e) {}
		}
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public void consumePrimes() throws InterruptedException {
		BlockingQueue<BigInteger> primes = null;
		BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
		producer.start();
		while (needMorePrimes()) {
			try {
				consume(queue.take());
			} finally {
				producer.cancel();
			}
		}
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月27日
	 * @param take
	 */
	private void consume(BigInteger take) {
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月27日
	 * @return
	 */
	private boolean needMorePrimes() {
		return false;
	}
	
}
