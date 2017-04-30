package com.deadpixel.arbiter;

import java.awt.EventQueue;
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
import com.deadpixel.player.Player;

public class Arbiter {

	public Arbiter() {
	};

	public static void main(String[] args) {
		try {
			FramesUtil.ensureExistence();

			// Sanity check for arguments
			if (args.length < 3) {
				System.out.println("Not enough arguments to run program. Exiting!");
				System.exit(1);
			}

			// Initialize input params
			FramesUtil.inputFile = args[0];
			FramesUtil.n1 = (int) Math.pow(2, Integer.parseInt(args[1]));
			FramesUtil.n2 = (int) Math.pow(2, Integer.parseInt(args[2]));
			FramesUtil.isGazedControlled = Integer.parseInt(args[3]);

			// Initialize and read binary
			FrameReader reader = new FrameReader();
			reader.readBinary();

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					Player.createAndShowUI();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
