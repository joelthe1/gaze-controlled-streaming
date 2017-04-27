package com.deadpixel.digitize;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FrameReader {

	public void readBinary() {
		try {
			final long startTime = System.currentTimeMillis();
			File file = new File("/Users/fox/Documents/sem4/project/given/two_people_encoder_output.bin");
			long fileLength = file.length();

			FileInputStream f = new FileInputStream(
					"/Users/fox/Documents/sem4/project/given/two_people_encoder_output.bin");
			FileChannel ch = f.getChannel();

			int frameLength = 6364800;
			ByteBuffer bb = ByteBuffer.allocateDirect(frameLength);
			bb.order(ByteOrder.nativeOrder());

			// System.out.println(fileLength/frameLength);
			// System.exit(1);

			ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			ArrayList<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();

			for (int k = 0; k < fileLength / frameLength; k++) {
				int n = ch.read(bb);

				// FloatBuffer frameBuffer = ((ByteBuffer)
				// bb.rewind()).asFloatBuffer();

				// for(Object input : inputs){
				tasks.add(new Frame(((ByteBuffer) bb.rewind()).asFloatBuffer()));
				// }
				
				if((k!=0 && k%60 == 0) || (k == (fileLength / frameLength)-1 && (fileLength / frameLength)%60 != 0)) {
					List<Future<Boolean>> results = exec.invokeAll(tasks);
					for (Future f1 : results) {
						System.out.println("Time taken for " + (k+1) +" frames=" + (System.currentTimeMillis() - startTime));
						tasks.clear();
						break;
					}
				}
			}
			exec.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
