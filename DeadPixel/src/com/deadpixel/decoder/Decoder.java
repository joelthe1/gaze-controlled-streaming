package com.deadpixel.decoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.deadpixel.digitize.FramesUtil;
import com.deadpixel.player.Player;

public class Decoder implements Runnable {
	
	@Override
	public void run() {
		int width = 960, height = 540;

		try {
			File file = new File("/Users/fox/Documents/sem4/project/given/oneperson_960_540.rgb");
			InputStream inputStream = new FileInputStream(file);
			long fileLength = file.length();
			byte[] bytes = new byte[(int) fileLength];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			System.out.println(offset);
			
			int ind = 0, fs = 0;
			while (ind < offset) {
				BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						byte r = bytes[ind];
						byte g = bytes[ind + height * width];
						byte b = bytes[ind + height * width * 2];
						int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
						frame.setRGB(x, y, pix);
						ind++;
					}
				}
				//fs++;
				//System.out.println(fs);
				FramesUtil.frameQueue.offer(frame);
				ind += height * width * 2;
			}
			System.out.println(ind);
			System.out.print(FramesUtil.frameQueue.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
