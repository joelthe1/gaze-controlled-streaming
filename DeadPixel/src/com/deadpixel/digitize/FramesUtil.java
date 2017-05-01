package com.deadpixel.digitize;

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class FramesUtil {
	public static HashMap<Integer, Frame> frameMap;
	private static FramesUtil instance = null;
	public static int n1, n2, isGazeControlled, framesCount;
	public static String inputFile;
	private FramesUtil() {
		frameMap = new HashMap<>();
	}
	
	public static void ensureExistence() {
		if(instance == null)
			instance = new FramesUtil();
	}
	
	public static Raster getGazeArea(int x, int y, int mapIndex) {
		int rectX = Math.max(0, x-32),
				rectY = Math.max(0, y-32),
				rectWidth = (rectX+64)<960? 64 : 959-rectX,
				rectHeight = (rectY+64)<544? 64 : 543-rectY;
		return FramesUtil.frameMap.get(mapIndex).hqBufferedImage.getData(new Rectangle(rectX, rectY, rectWidth, rectHeight));
	}
}
