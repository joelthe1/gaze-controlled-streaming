package com.deadpixel.digitize;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class FramesUtil {
	public static LinkedBlockingQueue<Frame> imageQueue;
	public static ConcurrentHashMap<String, Frame> frameMap;
	private static FramesUtil instance = null;
	
	private FramesUtil() {
		imageQueue = new LinkedBlockingQueue<>();
		frameMap = new ConcurrentHashMap<>();
	}
	
	public static void ensureExistence() {
		if(instance == null)
			instance = new FramesUtil();
		//return instance;
	}
}
