package com.deadpixel.digitize;

import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FramesUtil {
	public static ConcurrentLinkedQueue<BufferedImage> frameQueue;
	private static FramesUtil instance = null;
	private FramesUtil() {
		frameQueue = new ConcurrentLinkedQueue<>();
	}
	
	public static FramesUtil ensureExistence() {
		if(instance == null)
			instance = new FramesUtil();
		return instance;
	}
}
