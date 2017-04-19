package com.deadpixel.arbiter;

import com.deadpixel.decoder.Decoder;
import com.deadpixel.digitize.FramesUtil;

public class Arbiter {
	
	public Arbiter() {};
	
	public static void main(String[] args) {
		FramesUtil.ensureExistence();
		Thread t1 = new Thread(new Decoder());
		t1.start();
	}
}
