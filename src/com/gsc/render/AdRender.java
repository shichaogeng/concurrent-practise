package com.gsc.render;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.gsc.render.domain.Ad;
import com.gsc.render.domain.Page;

/**
 * @author shichaogeng
 *
 * 2017年9月27日
 */
public class AdRender {

	private static final long TIME_BUDGET = 1000000L;
	private static final Ad DEFAULT_AD = null;
	private ExecutorService executor = Executors.newFixedThreadPool(10);

	public Page renderWithAd() {
		long endNanos = System.nanoTime() + TIME_BUDGET;
		//创建并发任务加载ad
		Callable<Ad> fetchAdTask = new Callable<Ad>() {

			@Override
			public Ad call() throws Exception {
				return null;
			}
		};
		
		Future<Ad> future = executor.submit(fetchAdTask);
		//渲染页面
		Page page = renderBody();
		//渲染广告
		long timeLeft = endNanos - System.nanoTime();
		Ad ad;
		try {
			ad = future.get(timeLeft, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ad = DEFAULT_AD;
			future.cancel(true);
		} catch (ExecutionException e) {
			ad = DEFAULT_AD;
		} catch (TimeoutException e) {
			ad = DEFAULT_AD;
			future.cancel(true);
		}
		
		page.setAd(ad);
		return page;
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月27日
	 * @return
	 */
	private Page renderBody() {
		return null;
	}
	
	private RuntimeException launderThrowable(Throwable cause) {
		if (cause instanceof RuntimeException) {
			return (RuntimeException)cause;
		} else if (cause instanceof Error) {
			throw (Error) cause;
		} else {
			throw new IllegalStateException("Not Checked", cause);
		}
	}
}
