package com.deadpixel.digitize;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class FramesUtil {
	public static ConcurrentHashMap<Integer, Frame> frameMap;
	private static FramesUtil instance = null;
	public static int n1, n2, isGazedControlled;
	private FramesUtil() {
		frameMap = new ConcurrentHashMap<>();
	}
	
	public static void ensureExistence() {
		if(instance == null)
			instance = new FramesUtil();
		//return instance;
	}
}
