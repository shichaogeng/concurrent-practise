package com.gsc.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author shichaogeng
 *
 * 2017年9月26日
 */
public class Memoizer3<A, V> implements Computable<A, V> {
	
	private final Map<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
	
	private Computable<A, V> c;
	
	public Memoizer3(Computable<A, V> c) {
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
		Future<V> f = cache.get(arg);
		if (f == null) {
			FutureTask<V> ft = new FutureTask<>(new Callable<V>() {

				@Override
				public V call() throws Exception {
					return c.compute(arg);
				}
			});
			
			f = ft;
			cache.put(arg, f);
			ft.run();
		}
		try {
			return f.get();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
