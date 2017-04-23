package com.deadpixel.digitize;

import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;

public class FramesUtil {
	public static ConcurrentLinkedQueue<BufferedImage> imageQueue;
	public static PriorityBlockingQueue<Frame> frameQueue;
	private static FramesUtil instance = null;
	
	private FramesUtil() {
		imageQueue = new ConcurrentLinkedQueue<>();
		frameQueue = new PriorityBlockingQueue<>(400, new Comparator<Frame>() {
			public int compare(Frame f1, Frame f2) {
				return f1.seq - f2.seq;
			}
		});
	}
	
	public static void ensureExistence() {
		if(instance == null)
			instance = new FramesUtil();
		//return instance;
	}
}
