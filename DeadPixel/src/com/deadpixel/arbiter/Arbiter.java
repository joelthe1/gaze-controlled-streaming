package com.deadpixel.arbiter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.deadpixel.digitize.FrameReader;
import com.deadpixel.digitize.FramesUtil;

public class Arbiter {

	public Arbiter() {
	};

	public static void main(String[] args) {
		try {
			FramesUtil.ensureExistence();
			
			// Sanity check for arguments
			if(args.length < 3) {
				System.out.println("Not enough arguments to run program. Exiting!");
				System.exit(1);
			}
			
			// Initialize input params
			FramesUtil.inputFile = args[0];
			FramesUtil.n1 = (int)Math.pow(2, Integer.parseInt(args[1]));
			FramesUtil.n2 = (int)Math.pow(2, Integer.parseInt(args[2]));
			FramesUtil.isGazedControlled = Integer.parseInt(args[3]);
			
			// Initialize and read binary
			FrameReader reader = new FrameReader();
			reader.readBinary();
			
			testVideo(1);
//			testVideo(20);
//			testVideo(30);
//			testVideo(50);
//			testVideo(60);
//			testVideo(80);
//			testVideo(90);
//			testVideo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testVideo(int frameNum) {
		try {
			BufferedImage img = FramesUtil.frameMap.get(frameNum).lqBufferedImage;
			//img.setData(FramesUtil.getGazeArea(940, 300, frameNum));
			JLabel lbIm1 = new JLabel(new ImageIcon(img));//FramesUtil.imageQueue.take().bufferedImage));
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 1;
			JFrame frame = new JFrame();
			GridBagLayout gLayout = new GridBagLayout();
			frame.getContentPane().setLayout(gLayout);
			//frame.getContentPane().add(lbIm1, c);
//			frame.pack();
			frame.setVisible(true);
			
			for(int i=0; i<100; i++) {
				lbIm1.setIcon(new ImageIcon(FramesUtil.frameMap.get(i).hqBufferedImage));
				frame.getContentPane().add(lbIm1, c);
				frame.pack();
				lbIm1.updateUI();
//				frame.revalidate();
				//frame.repaint();
				Thread.sleep(10);
			}
			//frame.addMouseMotionListener(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testVideo() {
		try {
			// Use labels to display the images
			JFrame frame = new JFrame();
			GridBagLayout gLayout = new GridBagLayout();
			frame.getContentPane().setLayout(gLayout);
			String result = String.format("Original vs Compressed Image (%dx%d)", 960, 544);
			JLabel lbText1 = new JLabel(result);
			lbText1.setHorizontalAlignment(SwingConstants.CENTER);
				
			GridBagConstraints constraints = new GridBagConstraints();

			// for label
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.weightx = 0.5;
			constraints.gridx = 0;
			constraints.gridy = 0;
			frame.getContentPane().add(lbText1, constraints);

			// for image
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = 1;	
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(lbText1, constraints);
			
			BufferedImage img = FramesUtil.frameMap.get(0).hqBufferedImage;
			//byte[] f = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
			
			frame.add(new JLabel(new ImageIcon(img)), constraints);
			lbText1 = new JLabel(String.format("Output"));
			for(int i=0; i<70; i++) {
				//f = ((DataBufferByte)FramesUtil.frameMap.get(i).hqBufferedImage.getRaster().getDataBuffer()).getData();
				frame.repaint();
				//frame.add(new JLabel(new ImageIcon(FramesUtil.frameMap.get(i).hqBufferedImage)), constraints);
				
				frame.validate();
				frame.pack();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
