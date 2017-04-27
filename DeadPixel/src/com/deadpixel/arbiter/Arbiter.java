package com.deadpixel.arbiter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

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
			/*System.out.println("here");
			try {// (SeekableByteChannel ch = Files.newByteChannel(Paths.get("/Users/fox/dev/workspace/out.bin"))) {
				FileInputStream f = new FileInputStream( "/Users/fox/Documents/sem4/project/given/test.bin" );
				FileChannel ch = f.getChannel( );
			    ByteBuffer bb = ByteBuffer.allocateDirect(6364800);
			    bb.order(ByteOrder.nativeOrder());
			    for(;;) {
			    	int n = ch.read(bb);
			    	System.out.println(n);
			    	float[] rBytes = new float[6364800/4];
			    	
			    	//bb.rewind();
			        FloatBuffer fb = ((ByteBuffer) bb.rewind()).asFloatBuffer();
			        fb.get(rBytes);
			        System.out.println("Float Buffer");
			        while (fb.hasRemaining())
			          System.out.println(fb.position() + " -> " + fb.get());
			          
			    	for(int i=0,j=0;i<rBytes.length;i++,j+=4){
			    		System.out.println(rBytes[i]);
			            //ByteBuffer.wrap(bb, i*4, 4).putFloat(rBytes[i]);
			            
			        }
			    	System.out.println("and here");
			        
			        // add chars to line
			        // ...
					System.exit(1);
			    }
			} catch(Exception e) {
				e.printStackTrace();
			}
			System.exit(1);
*/
			//final long startTime = System.currentTimeMillis();
			FramesUtil.ensureExistence();
			/*Thread t1 = new Thread(new Decoder());
			t1.start();
			t1.join();
			*/
			FrameReader reader = new FrameReader();
			reader.readBinary();
			//System.out.println("Total time=" + (System.currentTimeMillis() - startTime));
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
