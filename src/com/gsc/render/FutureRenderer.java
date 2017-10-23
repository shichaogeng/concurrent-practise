package com.gsc.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.gsc.render.domain.ImageData;
import com.gsc.render.domain.ImageInfo;

/**
 * @author shichaogeng
 *
 * 2017年9月26日
 */
public class FutureRenderer {
	
	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	
	public void renderPage(CharSequence source) {
		
		Callable<List<ImageData>> downImageTask = new Callable<List<ImageData>>() {

			@Override
			public List<ImageData> call() throws Exception {
				//占位空间
				ArrayList<ImageData> imageData = new ArrayList<>();
				for (ImageInfo	imageInfo : scanForImageInfo(source)) {
					imageData.add(imageInfo.downloadImage());
				}
				return imageData;
			}
		};
		
		Future<List<ImageData>> future = executor.submit(downImageTask);
		//渲染文本
		renderText(source);
		
		try {
			//渲染图片
			renderImage(future.get());
		} catch (InterruptedException e) {
			//取消中断状态
			Thread.currentThread().interrupt();
			//取消任务
			future.cancel(true);
		} catch (ExecutionException e) {
			throw launderThrowable(e.getCause());
		}
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param cause
	 * @return
	 */
	private RuntimeException launderThrowable(Throwable cause) {
		if (cause instanceof RuntimeException) {
			return (RuntimeException)cause;
		} else if (cause instanceof Error) {
			throw (Error) cause;
		} else {
			throw new IllegalStateException("Not Checked", cause);
		}
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param imageData
	 */
	private void renderImage(List<ImageData> imageData) {
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param source
	 * @return
	 */
	private Iterable<ImageInfo> scanForImageInfo(CharSequence source) {
		return null;
	}

	/**
	 * 
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param source
	 */
	private void renderText(CharSequence source) {
	}
}
