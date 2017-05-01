package com.deadpixel.player;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.deadpixel.digitize.FrameReader;
import com.deadpixel.digitize.FramesUtil;

public class Player extends JPanel implements ActionListener
{
    JLabel timeLabel;
    //JLabel imageLabel;
    ImageIcon icon;
    
    int frameIndex;

    public Player()
    {
        setLayout( new BorderLayout() );

        timeLabel = new JLabel( new Date().toString() );
        timeLabel.setPreferredSize(new Dimension(960, 540));
        //imageLabel = new JLabel( timeLabel.getText() );

        add(timeLabel, BorderLayout.NORTH);
        //add(imageLabel, BorderLayout.SOUTH);

        frameIndex=0;
        javax.swing.Timer timer = new javax.swing.Timer(33, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {
    	if(frameIndex>350)
    		frameIndex=0;
    	
        timeLabel.setText( new Date().toString() );
        //System.out.println(frameIndex);
        ImageIcon icon = new ImageIcon(FramesUtil.frameMap.get(frameIndex).hqBufferedImage);
        icon.getImage().flush();
        timeLabel.setIcon(icon);
        frameIndex++;
    }

    public static void createAndShowUI()
    {
        JFrame frame = new JFrame("DeadPixel Player");    	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( new Player() );
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
		FramesUtil.isGazedControlled = Integer.parseInt(args[3]);
		
		// Initialize and read binary
		FrameReader reader = new FrameReader();
		reader.readBinary();
		
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {   			
                createAndShowUI();
            }
        });
    }
}