package com.deadpixel.digitize;

import org.jtransforms.dct.FloatDCT_2D;

public class Frame {
	float[][] r, g, b;
	float[][] rBlock, gBlock, bBlock;
	int frameIndex;
	int blockIndex;
	int seq;
	
	/*
	 * Create Frame from just the frame and block indices
	 */
	Frame(int frameIndex, int blockIndex) {
		r = new float[960][544];
		g = new float[960][544];
		b = new float[960][544];
		rBlock = new float[8][8];
		gBlock = new float[8][8];
		bBlock = new float[8][8];
		this.frameIndex = frameIndex;
		this.blockIndex = blockIndex;
		seq = Integer.valueOf(String.format("%d%d", frameIndex, blockIndex));
	}
	
	/*
	 * Create block from a row of the CSV
	 */
	Frame(Object[] row) {
		r = new float[960][544];
		g = new float[960][544];
		b = new float[960][544];
		rBlock = new float[8][8];
		gBlock = new float[8][8];
		bBlock = new float[8][8];
		this.frameIndex = (int)row[0];
		this.blockIndex = (int)row[1];
		seq = Integer.valueOf(String.format("%d%d", frameIndex, blockIndex));
		setBlock(row);
	}
	
	/*
	 * Set block from a row of the CSV in to the frame
	 */
	public void setBlockInFrame(Object[] row) {
		for(int i=2, xBase=blockIndex%120, yBase=blockIndex/120, x=0, y=0; i<66; i++, x++) {
			r[xBase+x][yBase+y] = (float)row[i];
			g[xBase+x][yBase+y] = (float)row[i];
			b[xBase+x][yBase+y] = (float)row[i];
			x = x%8==0? 0:x;
			y = x==0? y++:y;
		}
	}
	
	/*
	 * Set values in the R,G and B blocks.
	 */
	public void setBlock(Object[] row) {
		for(int i=2, y=0, x=-1; i<66; i++, y++) {
			x = y%8==0? x+1:x;
			y = y%8==0? 0:y;
			//System.out.print(x + "," + y + " ");
			rBlock[x][y] = (float)row[i];
			gBlock[x][y] = (float)row[i+64];
			bBlock[x][y] = (float)row[i+128];
		}
		//printBlock();
		doIDCTBlocks();
	}
	
	private void printBlock() {
		for(int x=0; x<8; x++) {
			for(int y=0; y<8; y++)
				System.out.print(rBlock[x][y] + " ");
			System.out.println("");
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
		printBlock();
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
		
		FloatDCT_2D DCTConvertor = new FloatDCT_2D(8,8);
		//float[][] idctMatrix = new float[16][16];
		DCTConvertor.forward(testMatrixOrig, true);
		for(int x=0; x<8; x++) {
			for(int y=0; y<8; y++)
				System.out.print(testMatrixOrig[x][y] + " ");
			System.out.println("");
		}
		System.out.println("");
		
		DCTConvertor.inverse(testMatrixOrig, true);
		for(int x=0; x<8; x++) {
			for(int y=0; y<8; y++)
				System.out.print(testMatrixOrig[x][y] + " ");
			System.out.println("");
		}
		System.exit(1);
	}
}
