package com.deadpixel.arbiter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.deadpixel.digitize.FrameReader;
import com.deadpixel.digitize.FramesUtil;

public class Arbiter {

	public Arbiter() {
	};

	public static void main(String[] args) {
		try {
			FramesUtil.ensureExistence();
			
			// Initialize input params
			FramesUtil.n1 = Integer.parseInt(args[0]);
			FramesUtil.n2 = Integer.parseInt(args[1]);
			FramesUtil.isGazedControlled = Integer.parseInt(args[2]);
			
			// Initialize and read binary
			FrameReader reader = new FrameReader();
			reader.readBinary();
			
			testVideo(1);
			testVideo(100);
			testVideo(200);
			testVideo(300);
			testVideo(400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testVideo(int frameNum) {
		try {
			JLabel lbIm1 = new JLabel(new ImageIcon(FramesUtil.frameMap.get(frameNum).lqBufferedImage));//FramesUtil.imageQueue.take().bufferedImage));
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 1;
			JFrame frame = new JFrame();
			GridBagLayout gLayout = new GridBagLayout();
			frame.getContentPane().setLayout(gLayout);
			frame.getContentPane().add(lbIm1, c);
			frame.pack();
			frame.setVisible(true);
			//frame.addMouseMotionListener(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
