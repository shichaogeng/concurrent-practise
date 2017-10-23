package com.gsc.blog;

/**
 * @author shichaogeng
 *
 * 2017年9月28日
 */
public class Static {

	private static String someField1 = someMethod1();
	
	private static String someField2;
	
	static {
		someField2 = someMethod2();
	}

	private static String someMethod1() {
		return "someField1";
	}

	private static String someMethod2() {
		return "someField2";
	}
	
}
