package com.deadpixel.digitize;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.deadpixel.player.Player;

public class FrameReader {

	public void readBinary() {
		try {
			final long startTime = System.currentTimeMillis();
			File file = new File(FramesUtil.inputFile);
			long fileLength = file.length();

			FileInputStream f = new FileInputStream(FramesUtil.inputFile);
			FileChannel ch = f.getChannel();
			
			// For 1 frame
			int frameLength = 6364800;

			ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			ArrayList<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
			FramesUtil.framesCount = (int)(fileLength / frameLength);
			int threadCount = 48;
			for (int k = 0; k < FramesUtil.framesCount; k++) {
				ByteBuffer bb = ByteBuffer.allocateDirect(frameLength);
				bb.order(ByteOrder.nativeOrder());
				int n = ch.read(bb);
				//System.out.println(n + " and bb pos=" + bb.position() + " and ch pos=" + ch.position());
				tasks.add(new Frame(((ByteBuffer) bb.rewind()).asFloatBuffer()));

				if((k!=0 && k%threadCount == 0) || (k == (fileLength / frameLength)-1 && (fileLength / frameLength)%threadCount != 0)) {
					
					if(k==threadCount) {
						System.out.println("Starting player");
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								Player.createAndShowUI();
							}
						});
					}
					exec.invokeAll(tasks);
					System.out.println("Time taken for " + (k+1) +" frames=" + (System.currentTimeMillis() - startTime) + 
							" and tasks size=" + tasks.size());
					tasks.clear();
				}
			}
			exec.shutdown();
			ch.close();
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
