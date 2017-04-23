package com.deadpixel.arbiter;

import com.deadpixel.decoder.Decoder;
import com.deadpixel.digitize.FramesUtil;
import com.deadpixel.digitize.FrameReader;
import com.deadpixel.player.Player;

public class Arbiter {

	public Arbiter() {
	};

	public static void main(String[] args) {
		try {
			/*FramesUtil.ensureExistence();
			Thread t1 = new Thread(new Decoder());
			t1.start();
			t1.join();
			Thread t2 = new Thread(new Player());
			t2.start();*/
			FrameReader reader = new FrameReader();
			reader.readCSV();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
