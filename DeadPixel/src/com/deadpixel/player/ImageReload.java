package com.deadpixel.player;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

import com.deadpixel.digitize.FrameReader;
import com.deadpixel.digitize.FramesUtil;

import java.net.*;

public class ImageReload extends JPanel implements ActionListener
{
    JLabel timeLabel;
    JLabel imageLabel;
    ImageIcon icon = new ImageIcon("timeLabel.jpg");
    
    int i=1;

    public ImageReload()
    {
        setLayout( new BorderLayout() );

        timeLabel = new JLabel( new Date().toString() );
        imageLabel = new JLabel( timeLabel.getText() );

        add(timeLabel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.SOUTH);

        javax.swing.Timer timer = new javax.swing.Timer(1000, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        timeLabel.setText( new Date().toString() );
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                i++;
                System.out.println(i);
                
                try
                {
                    String imageName = "timeLabel.jpg";
                    
                    BufferedImage image = ScreenImage.createImage(timeLabel);
                    ScreenImage.writeImage(image, imageName);

                    //  This works using ImageIO

                    //imageLabel.setIcon(  );

                    //  Or you can flush the image
/*
                    ImageIcon icon = new ImageIcon(imageName);
                    icon.getImage().flush();
                    imageLabel.setIcon( icon );
*/
                    ImageIcon icon = new ImageIcon(FramesUtil.frameMap.get(i).hqBufferedImage);
                    icon.getImage().flush();
                    imageLabel.setIcon( icon );
                    imageLabel.setText("");
                    i++;
                }
                catch(Exception e)
                {
                    System.out.println( e );
                }
            }
        });
    }

    private static void createAndShowUI()
    {
        JFrame frame = new JFrame("SSCCE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( new ImageReload() );
        frame.setLocationByPlatform( true );
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