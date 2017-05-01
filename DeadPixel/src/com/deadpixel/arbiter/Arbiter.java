package com.deadpixel.arbiter;

import com.deadpixel.digitize.FrameReader;
import com.deadpixel.digitize.FramesUtil;

public class Arbiter {

	public Arbiter() {
	};

	public static void main(String[] args) {
		try {
			FramesUtil.ensureExistence();

			// Sanity check for arguments
			if (args.length < 3) {
				System.out.println("Not enough arguments to run program. Exiting!");
				System.exit(1);
			}

			// Initialize input params
			FramesUtil.inputFile = args[0];
			FramesUtil.n1 = (int) Math.pow(2, Integer.parseInt(args[1]));
			FramesUtil.n2 = (int) Math.pow(2, Integer.parseInt(args[2]));
			FramesUtil.isGazeControlled = Integer.parseInt(args[3]);

			// Initialize and read binary
			FrameReader reader = new FrameReader();
			reader.readBinary();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
