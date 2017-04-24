package com.deadpixel.digitize;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.jtransforms.dct.DoubleDCT_1D;
import org.jtransforms.dct.FloatDCT_1D;

public class Frame {
	byte[] frame;
	double[] rBlock, gBlock, bBlock;
	public BufferedImage bufferedImage;
	int frameIndex;
	int blockIndex;
	int seq;
	
	/*
	 * Create block from a row of the CSV
	 */
	Frame(double[] row) {
		bufferedImage = new BufferedImage(960,544,BufferedImage.TYPE_3BYTE_BGR);
		frame = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();//new byte[1566720];
		rBlock = new double[64];
		gBlock = new double[64];
		bBlock = new double[64];
		this.frameIndex = (int)row[0];
		this.blockIndex = (int)row[1];
		setBlock(row);
	}
	
	/*
	 * Set block from a row of the CSV in to the frame
	 
	public void setBlockInFrame(Object[] row) {
		for(int i=2, xBase=blockIndex%120, yBase=blockIndex/120, x=0, y=0; i<66; i++, x++) {
			r[xBase+x][yBase+y] = (float)row[i];
			g[xBase+x][yBase+y] = (float)row[i];
			b[xBase+x][yBase+y] = (float)row[i];
			x = x%8==0? 0:x;
			y = x==0? y++:y;
		}
	}*/
	
	public void setBlock(double[] row) {
		for(int i=0; i<64; i++) {
			rBlock[i] = row[i+2];
			gBlock[i] = row[i+2+64];
			bBlock[i] = row[i+2+128];
		}
		doIDCTBlocks();
		//printBlock();
		
		// 68x120 of 8x8 blocks
		int base = (blockIndex%120)*8 + (blockIndex-(blockIndex%120))*64;
		for(int i=0, iter=0; iter<64; iter++) {
			i = (iter!=0 && iter%8==0)? i+960-8 : i;
			frame[base+i] = (byte)(bBlock[iter]+0.5d);
			frame[base+i+1] = (byte)(gBlock[iter]+0.5d);
			frame[base+i+2] = (byte)(rBlock[iter]+0.5d);
			i+=3;
		}
	}
	
	private void printBlock() {
		for(int x=0; x<64; x++)
				System.out.print(rBlock[x] + " ");
			System.out.println("");
	}
	
	/*
	 * Perform IDCT on blocks of R,G and B channels
	 */
	private void doIDCTBlocks() {
		DoubleDCT_1D DCTConvertor = new DoubleDCT_1D(64);
		DCTConvertor.inverse(rBlock, true);
		DCTConvertor.inverse(gBlock, true);
		DCTConvertor.inverse(bBlock, true);
		//printBlock();
	}
	
	private void testIDCTBlocks() {
		float[][] testMatrixOrig = {{154, 123, 123, 123, 123, 123, 123, 136},
				{192, 180, 136, 154, 154, 154, 136, 110},
				{254, 198, 154, 154, 180, 154, 123, 123},
				{239, 180, 136, 180, 180, 166, 123, 123},
				{180, 154, 136, 167, 166, 149, 136, 136},
				{128, 136, 123, 136, 154, 180, 198, 154},
				{123, 105, 110, 149, 136, 136, 180, 166},
				{110, 136, 123, 123, 123, 136, 154, 136}};
		
		float[] test1D = {154, 123, 123, 123, 123, 123, 123, 136,
				192, 180, 136, 154, 154, 154, 136, 110,
				254, 198, 154, 154, 180, 154, 123, 123,
				239, 180, 136, 180, 180, 166, 123, 123,
				180, 154, 136, 167, 166, 149, 136, 136,
				128, 136, 123, 136, 154, 180, 198, 154,
				123, 105, 110, 149, 136, 136, 180, 166,
				110, 136, 123, 123, 123, 136, 154, 136};
		
		FloatDCT_1D DCTConvertor = new FloatDCT_1D(64);
		//float[][] idctMatrix = new float[16][16];
		DCTConvertor.forward(test1D, true);
		for(int x=0; x<64; x++) {
			//for(int y=0; y<8; y++)
				System.out.print(test1D[x] + " ");
			//System.out.println("");
		}
		System.out.println("");
		
		DCTConvertor.inverse(test1D, true);
		for(int x=0; x<64; x++) {
			//for(int y=0; y<8; y++)
				System.out.print(test1D[x] + " ");
			//System.out.println("");
		}
		System.exit(1);
	}
}
