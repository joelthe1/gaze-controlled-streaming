package com.deadpixel.digitize;

public class Frame {
	float[][] r, g, b;
	float[][] rBlock, gBlock, bBlock;
	int frameIndex;
	int blockIndex;
	
	Frame(int frameIndex) {
		r = new float[960][544];
		g = new float[960][544];
		b = new float[960][544];
		rBlock = new float[8][8];
		gBlock = new float[8][8];
		bBlock = new float[8][8];
		this.frameIndex = frameIndex;
	}
	
	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
	}
}
