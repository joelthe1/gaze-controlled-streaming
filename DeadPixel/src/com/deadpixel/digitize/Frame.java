package com.deadpixel.digitize;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.FloatBuffer;
import java.util.concurrent.Callable;

import org.jtransforms.dct.FloatDCT_2D;

public class Frame implements Callable {
	public byte[] frame;
	float[] rBlock, gBlock, bBlock;
	public BufferedImage bufferedImage;
	int frameIndex;
	FloatBuffer DCTframe;
	
	Frame(FloatBuffer DCTframe) {
		bufferedImage = new BufferedImage(960,544,BufferedImage.TYPE_3BYTE_BGR);
		frame = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
		rBlock = new float[64];
		gBlock = new float[64];
		bBlock = new float[64];
		this.DCTframe = DCTframe.duplicate();
		this.frameIndex = (int)DCTframe.get();
	}
	
	@Override
	public Boolean call() throws Exception {
		int blockIndex, pos;
		for(int k=0; k<8160; k++) {
			DCTframe.get();
			blockIndex = (int)DCTframe.get();
			//System.out.println("Frame position is=" + DCTframe.position() + " and blockIndex="+blockIndex);
			DCTframe.get();
			pos = DCTframe.position();
			((FloatBuffer)DCTframe.duplicate().limit(pos+64)).get(bBlock);
			((FloatBuffer)DCTframe.duplicate().position(pos+64).limit(pos+128)).get(gBlock);
			((FloatBuffer)DCTframe.duplicate().position(pos+128).limit(pos+192)).get(rBlock);
			
			doIDCTBlocks();
			
			int base = (blockIndex%120)*24 + (blockIndex-(blockIndex%120))*192;
			for(int i=0, iter=0; iter<64; iter++) {
				i = (iter!=0 && iter%8==0)? i+2880-24 : i;
				frame[base+i] = (byte)((int)(bBlock[iter]+128.5));
				frame[base+i+1] = (byte)((int)(gBlock[iter]+128.5));
				frame[base+i+2] = (byte)((int)(rBlock[iter]+128.5));
				i+=3;
			}
			DCTframe.position(pos+192);
		}
		FramesUtil.frameMap.put(frameIndex, this);
		return true;
	}
	
	/*
	 * Perform IDCT on blocks of R,G and B channels
	 */
	private void doIDCTBlocks() {
		FloatDCT_2D DCTConvertor = new FloatDCT_2D(8,8);
		DCTConvertor.inverse(rBlock, true);
		DCTConvertor.inverse(gBlock, true);
		DCTConvertor.inverse(bBlock, true);
	}
}
