package com.gsc.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shichaogeng
 *
 * 2017年9月26日
 */
public class Memoizer2<A, V> implements Computable<A, V> {
	
	private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
	
	private Computable<A, V> c;
	
	public Memoizer2(Computable<A, V> c) {
		this.c = c;
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param arg
	 * @return
	 * @throws InterruptedException
	 */
	@Override
	public synchronized V compute(A arg) throws InterruptedException {
		V result = cache.get(arg);
		if (result == null) {
			result = c.compute(arg);
			cache.put(arg, result);
		}
		return result;
	}

}
