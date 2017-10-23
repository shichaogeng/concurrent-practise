package com.gsc.cache;

import java.math.BigInteger;

/**
 * @author shichaogeng
 *
 * 2017年9月26日
 */
public class ExpensiveFunction implements Computable<String, BigInteger> {

	/**
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param arg
	 * @return
	 * @throws InterruptedException
	 */
	@Override
	public BigInteger compute(String arg) throws InterruptedException {
		//超级计算
		
		return new BigInteger(arg);
	}

}
