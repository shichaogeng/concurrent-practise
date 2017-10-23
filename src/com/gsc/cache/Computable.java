package com.gsc.cache;

/**
 * @author shichaogeng
 *
 * 2017年9月26日
 */
public interface Computable<A, V> {
	
	V compute(A arg) throws InterruptedException;
	
}
