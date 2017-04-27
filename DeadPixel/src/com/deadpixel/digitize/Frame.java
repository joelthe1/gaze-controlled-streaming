package com.deadpixel.digitize;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.FloatBuffer;
import java.util.concurrent.Callable;

import org.jtransforms.dct.DoubleDCT_1D;
import org.jtransforms.dct.DoubleDCT_2D;
import org.jtransforms.dct.FloatDCT_2D;

public class Frame implements Callable {
	public byte[] frame;
	float[] rBlock, gBlock, bBlock;
	public BufferedImage bufferedImage;
	int frameIndex;
	FloatBuffer DCTframe;
	
	/*
	 * Create block from a row of the CSV
	 */
	Frame(Object[] row) {
		bufferedImage = new BufferedImage(960,544,BufferedImage.TYPE_3BYTE_BGR);
		frame = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
		rBlock = new float[64];
		gBlock = new float[64];
		bBlock = new float[64];
		this.frameIndex = ((Double)row[0]).intValue();
		setBlock(row);
	}
	
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
		FramesUtil.imageQueue.offer(this);
		return true;
	}
	
	public void setBlock(Object[] row) {
		int blockIndex = ((Double)row[1]).intValue();
				//quantization = (int) Math.pow(2, (int)row[194]);
		for(int i=0; i<64; i++) {
			rBlock[i] = (float)row[i+2];///quantization;
			gBlock[i] = (float)row[i+2+64];///quantization;
			bBlock[i] = (float)row[i+2+128];///quantization;
		}
		doIDCTBlocks();
		
		// 68x120 of 8x8 blocks
		int base = (blockIndex%120)*24 + (blockIndex-(blockIndex%120))*192;
		for(int i=0, iter=0; iter<64; iter++) {
			i = (iter!=0 && iter%8==0)? i+2880-24 : i;
			//(int) (doubleVar + 0.5)
			frame[base+i] = (byte)((int)(bBlock[iter]+128.5));
			frame[base+i+1] = (byte)((int)(gBlock[iter]+128.5));
			frame[base+i+2] = (byte)((int)(rBlock[iter]+128.5));
			i+=3;
		}
	}
	
	private void printBlock() {
		for(int x=0; x<256; x++){
			System.out.print(frame[x] + " ");
		}
		System.out.println("");
		for(int x=0; x<64; x++){
			System.out.print(bBlock[x] + " ");
		}	
	}
	
	/*
	 * Perform IDCT on blocks of R,G and B channels
	 */
	private void doIDCTBlocks() {
		FloatDCT_2D DCTConvertor = new FloatDCT_2D(8,8);
		DCTConvertor.inverse(rBlock, true);
		DCTConvertor.inverse(gBlock, true);
		DCTConvertor.inverse(bBlock, true);
		//printBlock();
	}
	
	private void testIDCTBlocks() {
		double[][] testMatrixOrig = {{154, 123, 123, 123, 123, 123, 123, 136},
				{192, 180, 136, 154, 154, 154, 136, 110},
				{254, 198, 154, 154, 180, 154, 123, 123},
				{239, 180, 136, 180, 180, 166, 123, 123},
				{180, 154, 136, 167, 166, 149, 136, 136},
				{128, 136, 123, 136, 154, 180, 198, 154},
				{123, 105, 110, 149, 136, 136, 180, 166},
				{110, 136, 123, 123, 123, 136, 154, 136}};
		
		double[] test1D = {154, 123, 123, 123, 123, 123, 123, 136,
				192, 180, 136, 154, 154, 154, 136, 110,
				254, 198, 154, 154, 180, 154, 123, 123,
				239, 180, 136, 180, 180, 166, 123, 123,
				180, 154, 136, 167, 166, 149, 136, 136,
				128, 136, 123, 136, 154, 180, 198, 154,
				123, 105, 110, 149, 136, 136, 180, 166,
				110, 136, 123, 123, 123, 136, 154, 136};
		
		DoubleDCT_2D DCT2dConvertor = new DoubleDCT_2D(8,8);
		DoubleDCT_1D DCTConvertor = new DoubleDCT_1D(64);
		//float[][] idctMatrix = new float[16][16];
		
		DCT2dConvertor.forward(testMatrixOrig, true);
		//DCTConvertor.forward(test1D, true);
		for(int x=0; x<64; x++) {
			//for(int y=0; y<8; y++)
				System.out.print(test1D[x] + ",");//+test1D[x] + ","+test1D[x] + ",");
			//System.out.println("");
		}
		System.out.println("");
		
		int k=0;
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				test1D[k++] = testMatrixOrig[i][j];
		
		//DCTConvertor.inverse(test1D, true);
		DCTConvertor.inverse(test1D, true);
		
		for(int x=0; x<64; x++) {
			//for(int y=0; y<8; y++)
				System.out.print(test1D[x] + " ");
			//System.out.println("");
		}
		System.exit(1);
	}
}
