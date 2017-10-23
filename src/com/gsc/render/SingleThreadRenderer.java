package com.gsc.render;

import java.util.ArrayList;

import com.gsc.render.domain.ImageData;
import com.gsc.render.domain.ImageInfo;

/**
 * @author shichaogeng
 *
 * 2017年9月26日
 */
public class SingleThreadRenderer {
	
	public void renderPage(CharSequence source) {
		
		//渲染文本
		renderText(source);
		//占位空间
		ArrayList<ImageData> imageData = new ArrayList<>();
		for (ImageInfo	imageInfo : scanForImageInfo(source)) {
			imageData.add(imageInfo.downloadImage());
		}
		//渲染图片
		renderImage(imageData);
	}

	/**
	 * @author shichaogeng
	 * @date 2017年9月26日
	 * @param imageData
	 */
	private void renderImage(ArrayList<ImageData> imageData) {
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
