package com.gsc.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
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
public class Renderer {
	
	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	
	public void renderPage(CharSequence source) {
		
		
		CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);
		ArrayList<ImageData> imageData = new ArrayList<>();
		List<ImageInfo> info = (List<ImageInfo>) scanForImageInfo(source);
		for (ImageInfo	imageInfo : info) {
			completionService.submit(new Callable<ImageData>() {

				@Override
				public ImageData call() throws Exception {
					return imageInfo.downloadImage();
				}
			});
		}
		
		
		//渲染文本
		renderText(source);
		
		try {
			//渲染图片
			for (int i = 0; i < info.size(); i++) {
				Future<ImageData> future = completionService.take();
				ImageData data = future.get();
				renderImage(data);
			}
		} catch (InterruptedException e) {
			//取消中断状态
			Thread.currentThread().interrupt();
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
	 * @param data
	 */
	private void renderImage(ImageData data) {
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
