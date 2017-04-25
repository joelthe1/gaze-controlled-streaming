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
			/*Thread t1 = new Thread(new Decoder());
			t1.start();
			t1.join();
			*/
			FrameReader reader = new FrameReader();
			reader.readCSV();
			
			JFrame frame = new JFrame();
			GridBagLayout gLayout = new GridBagLayout();
			frame.getContentPane().setLayout(gLayout);
			
			try {
				System.out.println(FramesUtil.imageQueue.size());
				
				JLabel lbIm1 = new JLabel(new ImageIcon(FramesUtil.imageQueue.take().bufferedImage));
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testVideo() {
		FrameReader reader = new FrameReader();
		//reader.readCSV();
		
		JFrame frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);
		
		try {
			//System.out.println(FramesUtil.imageQueue.size());
			
			/*byte[] f = FramesUtil.imageQueue.take().frame;
			System.out.println(f.length);
			for(int i=0; i<64; i++) {
				System.out.print(f[i]+", ");
			}*/
			
			
			
			JLabel lbIm1 = new JLabel(new ImageIcon(FramesUtil.imageQueue.take().bufferedImage));
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 1;
			frame.getContentPane().add(lbIm1, c);
			frame.pack();
			frame.setVisible(true);
			//Thread.sleep(15000);
			//frame.addMouseMotionListener(this);
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
