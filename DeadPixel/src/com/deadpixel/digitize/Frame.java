package com.deadpixel.digitize;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.FloatBuffer;
import java.util.concurrent.Callable;

import org.jtransforms.dct.FloatDCT_2D;

public class Frame implements Callable {
	public byte[] hqFrame, lqFrame;
	float[] rBlock, gBlock, bBlock, lqRBlock, lqGBlock, lqBBlock;
	public BufferedImage hqBufferedImage, lqBufferedImage;
	int frameIndex;
	FloatBuffer DCTframe;
	
	Frame(FloatBuffer DCTframe) {
		hqBufferedImage = new BufferedImage(960,544,BufferedImage.TYPE_3BYTE_BGR);
		lqBufferedImage = new BufferedImage(960,544,BufferedImage.TYPE_3BYTE_BGR);
		hqFrame = ((DataBufferByte)hqBufferedImage.getRaster().getDataBuffer()).getData();
		lqFrame = ((DataBufferByte)lqBufferedImage.getRaster().getDataBuffer()).getData();
		rBlock = new float[64];
		gBlock = new float[64];
		bBlock = new float[64];
		lqRBlock = new float[64];
		lqGBlock = new float[64];
		lqBBlock = new float[64];
		
		this.DCTframe = DCTframe.duplicate();
		this.frameIndex = (int)DCTframe.get();
	}
	
	@Override
	public Boolean call() throws Exception {
		int blockIndex, pos;
		for(int k=0; k<8160; k++) {
			DCTframe.get();
			blockIndex = (int)DCTframe.get();
			
			float blockType = DCTframe.get();
			
			// Decide quantization value
			int quantizer = (blockType >= 2.0f && blockType < 5.0f)? FramesUtil.n1 : FramesUtil.n2;
			//System.out.println("Frame position is=" + DCTframe.position() + " and blockIndex="+blockIndex + " and blockType=" + blockType);
			
			pos = DCTframe.position();
			// Setup HQ DCT frame
			((FloatBuffer)DCTframe.duplicate().limit(pos+64)).get(bBlock);
			((FloatBuffer)DCTframe.duplicate().position(pos+64).limit(pos+128)).get(gBlock);
			((FloatBuffer)DCTframe.duplicate().position(pos+128).limit(pos+192)).get(rBlock);
			
			// Setup LQ DCT frame
			((FloatBuffer)DCTframe.duplicate().limit(pos+64)).get(lqBBlock);
			((FloatBuffer)DCTframe.duplicate().position(pos+64).limit(pos+128)).get(lqGBlock);
			((FloatBuffer)DCTframe.duplicate().position(pos+128).limit(pos+192)).get(lqRBlock);
			
			
			if(quantizer!=1)
				for(int i=0; i<64; i++) {
					lqBBlock[i] = Math.round(lqBBlock[i]/quantizer);
					lqGBlock[i] = Math.round(lqGBlock[i]/quantizer);
					lqRBlock[i] = Math.round(lqRBlock[i]/quantizer);
				}
			
			doIDCTBlocks();
			
			int base = (blockIndex%120)*24 + (blockIndex-(blockIndex%120))*192;
			for(int i=0, iter=0; iter<64; iter++) {
				i = (iter!=0 && iter%8==0)? i+2880-24 : i;
//				hqFrame[base+i] = (byte)((int)(bBlock[iter]+128.5));
//				hqFrame[base+i+1] = (byte)((int)(gBlock[iter]+128.5));
//				hqFrame[base+i+2] = (byte)((int)(rBlock[iter]+128.5));
//				
//				lqFrame[base+i] = (byte)((int)(lqBBlock[iter]+128.5));
//				lqFrame[base+i+1] = (byte)((int)(lqGBlock[iter]+128.5));
//				lqFrame[base+i+2] = (byte)((int)(lqRBlock[iter]+128.5));
				
				hqFrame[base+i] = (byte)((int)(bBlock[iter]+0.5));
				hqFrame[base+i+1] = (byte)((int)(gBlock[iter]+0.5));
				hqFrame[base+i+2] = (byte)((int)(rBlock[iter]+0.5));
				
				lqFrame[base+i] = (byte)((int)(lqBBlock[iter]+0.5));
				lqFrame[base+i+1] = (byte)((int)(lqGBlock[iter]+0.5));
				lqFrame[base+i+2] = (byte)((int)(lqRBlock[iter]+0.5));
				
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
		DCTConvertor.inverse(lqRBlock, true);
		DCTConvertor.inverse(lqGBlock, true);
		DCTConvertor.inverse(lqBBlock, true);
	}
}
