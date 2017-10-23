package com.gsc.cancel;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * @author shichaogeng
 *
 * 2017年9月27日
 */
public class PrimeProducer extends Thread {

	private final BlockingQueue<BigInteger> queue;
	
	public PrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月27日
	 */
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			BigInteger p = BigInteger.ONE;
			try {
				queue.put(p.nextProbablePrime());
			} catch (InterruptedException e) {}
		}
	}
	
	public void cancel() {
		interrupt();
	}
	
	public void consumePrimes() throws InterruptedException {
		BlockingQueue<BigInteger> primes = null;
		PrimeProducer producer = new PrimeProducer(primes);
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
