package com.deadpixel.player;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.deadpixel.digitize.FramesUtil;

public class Player extends JFrame implements MouseMotionListener, Runnable {
	JFrame frame;
	JLabel label;
	JLabel lbIm1;
	JLabel lbIm2;
	int height, width, fps;
	GridBagConstraints constraints;
	BufferedImage img;

	public static void main(String[] args) {
		Thread t1 = new Thread(new Player());
		t1.start();
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		System.out.println(e.getX() + "-" + e.getY());
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		int pix = 0xff000000;
		// To do : Optimize window movement
		for (int i = 0; i < 90; ++i) {
			for (int j = 0; j < 60; ++j) {
				img.setRGB(e.getX() + i, e.getY() + j, pix);
			}
		}
	}

	@Override
	public void run() {
		width = 960;
		height = 540;
		fps = 30;

		//img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);
		
		try {
			//System.out.println(FramesUtil.imageQueue.size());
			
			lbIm1 = new JLabel(new ImageIcon(FramesUtil.frameMap.get(1).bufferedImage));
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 1;
			frame.getContentPane().add(lbIm1, c);
			frame.pack();
			frame.setVisible(true);
			//frame.addMouseMotionListener(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*try {
			Iterator<BufferedImage> frameIterator = FramesUtil.imageQueue.iterator();
			long timePerFrame = (long) ((1.0 / Long.valueOf(fps)) * 1000);
			while (frameIterator.hasNext()) {
					img = frameIterator.next();
					//System.out.println(img);
					TimeUnit.MILLISECONDS.sleep(timePerFrame);
					lbIm1 = new JLabel(new ImageIcon(img));
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;
					c.anchor = GridBagConstraints.CENTER;
					c.fill = GridBagConstraints.HORIZONTAL;
					c.gridx = 0;
					c.gridy = 1;
					frame.getContentPane().add(lbIm1, c);
					frame.pack();
					frame.setVisible(true);
					//frame.addMouseMotionListener(this);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

	}

}
