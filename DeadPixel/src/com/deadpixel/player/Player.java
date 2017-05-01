package com.deadpixel.player;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.event.MouseAdapter;
import java.nio.ByteBuffer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.deadpixel.digitize.FrameReader;
import com.deadpixel.digitize.FramesUtil;

public class Player extends JPanel implements ActionListener, MouseMotionListener
{
    JLabel timeLabel;
    //JLabel imageLabel;
    ImageIcon icon;
    
    int frameIndex, mouseX, mouseY;
    ByteBuffer renderedFrameByteBuffer;
    BufferedImage renderedFrame;
    byte[] renderedFrameByteArray;
    boolean isPaused;

    public Player()
    {
        setLayout( new BorderLayout() );

        timeLabel = new JLabel();
        timeLabel.setPreferredSize(new Dimension(960, 540));
        //imageLabel = new JLabel( timeLabel.getText() );

        add(timeLabel, BorderLayout.NORTH);
        //add(imageLabel, BorderLayout.SOUTH);
 
        addMouseMotionListener(this);
        
        frameIndex=0;
        renderedFrameByteBuffer = ByteBuffer.allocateDirect(1566720);
        renderedFrame = new BufferedImage(960,544,BufferedImage.TYPE_3BYTE_BGR);
        renderedFrameByteArray = ((DataBufferByte)renderedFrame.getRaster().getDataBuffer()).getData();
        
        javax.swing.Timer timer = new javax.swing.Timer(41, this);
        timer.start();
        
        isPaused = false;
        addMouseListener(new MouseAdapter() { 
            public void mousePressed(MouseEvent me) { 
            	isPaused = isPaused? false: true;
            } 
          });
    }

    public void actionPerformed(ActionEvent e)
    {
    	if(frameIndex >= FramesUtil.framesCount)
    		frameIndex=0;
        
	    ((ByteBuffer) renderedFrameByteBuffer.rewind()).put(((DataBufferByte)FramesUtil.frameMap.get(frameIndex).lqBufferedImage.getRaster().getDataBuffer()).getData());
	    ((ByteBuffer) renderedFrameByteBuffer.rewind()).get(renderedFrameByteArray);
	    if(FramesUtil.isGazeControlled == 1) {
	        	renderedFrame.setData(FramesUtil.getGazeArea(mouseX, mouseY, frameIndex));
    	}
        ImageIcon icon = new ImageIcon(renderedFrame);
        icon.getImage().flush();
        timeLabel.setIcon(icon);
        
        if(!isPaused)
        	frameIndex++;
    }

    public static void createAndShowUI()
    {
        JFrame frame = new JFrame("DeadPixel Player");
        frame.add( new Player() );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform( true );
        frame.getContentPane().setSize(960,540);
        frame.pack();
        frame.setVisible( true );
    }

    public static void main(String[] args)
    {
    	FramesUtil.ensureExistence();
    	
    	// Initialize input params
		FramesUtil.inputFile = args[0];
		FramesUtil.n1 = (int)Math.pow(2, Integer.parseInt(args[1]));
		FramesUtil.n2 = (int)Math.pow(2, Integer.parseInt(args[2]));
		FramesUtil.isGazeControlled = Integer.parseInt(args[3]);
		
		// Initialize and read binary
		FrameReader reader = new FrameReader();
		reader.readBinary();
    }

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println(e.getX() + ", " + e.getY());
		mouseX = e.getX();
		mouseY = e.getY();
	}
}