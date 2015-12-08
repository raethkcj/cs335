package edu.uwec.cs.raethkcj.seamcarving;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.*;

public class UWECImage {
	private BufferedImage im;
	private ImagePanel theDisplay;
	
	// Nested class for display
	private class ImagePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		UWECImage im;
		JFrame frame;

		public ImagePanel(UWECImage im) {
			this.im = im;  // Reference to the image - not a copy
						   // So if you call repaint() on this class it will redraw the updated pixels

			this.setSize(im.getWidth(), im.getHeight());

			// Setup the frame that I belong in
			frame = new JFrame("Image Viewer");
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(1);
				}
			});
			frame.getContentPane().add(this);
			
			// Set a fake size for now to get it up - we will never see this show up in these dimensions
			frame.setSize(im.getWidth()+10, im.getHeight()+10);
			frame.setVisible(true);
			
			// Get the insets now that it is visible
			// The insets will be 0 until it is visible
			Insets fInsets = frame.getInsets();
			
			// Resize the image based on the visible insets
			frame.setSize(im.getWidth() + fInsets.left + fInsets.right, im.getHeight() + fInsets.top + fInsets.bottom);
		}
		
		// Resets the image being displayed and redoes the frame to fit it
		public void changeImage(UWECImage im) {
			this.im = im;
			
			Insets fInsets = frame.getInsets();
			
			// Resize the image based on the visible insets
			frame.setSize(im.getWidth() + fInsets.left + fInsets.right, im.getHeight() + fInsets.top + fInsets.bottom);
			
			this.repaint();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			im.draw(g);
		}
	}

	// Make an image from the given filename
	public UWECImage(String filename) {
		File f = new File(filename);

		try {
			this.im = ImageIO.read(f);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// Make a blank image given the size
	public UWECImage(int x, int y) {
		this.im = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

		// Create a graphics context for the new BufferedImage
		Graphics2D g2 = this.im.createGraphics();

		// Fill in the image with black
		g2.setColor(Color.black);
		g2.fillRect(0, 0, x, y);
	}

	public int getWidth() {
		return im.getWidth();
	}

	public int getHeight() {
		return im.getHeight();
	}

	public int getRed(int x, int y) {
		int value = 0;

		if ((x >= 0) && (y >= 0) && (x < im.getWidth()) && (y < im.getHeight())) {
			value = ((im.getRGB(x, y) >> 16) & 255);
		}

		return value;
	}

	public int getGreen(int x, int y) {
		int value = 0;

		if ((x >= 0) && (y >= 0) && (x < im.getWidth()) && (y < im.getHeight())) {
			value = ((im.getRGB(x, y) >> 8) & 255);
		}

		return value;
	}

	public int getBlue(int x, int y) {
		int value = 0;

		if ((x >= 0) && (y >= 0) && (x < im.getWidth()) && (y < im.getHeight())) {
			value = ((im.getRGB(x, y) >> 0) & 255);
		}

		return value;
	}

	public void setRGB(int x, int y, int red, int green, int blue) {
		int pixel = (red << 16) + (green << 8) + (blue);

		im.setRGB(x, y, pixel);
	}

	public void draw(Graphics g) {
		g.drawImage(im, 0, 0, null);
	}

	public void write(String filename) {
		if (filename.endsWith(".png")) {
			writePNG(filename);
		} else {
			System.out.println("Filetype not supported");
		}
	}

	private void writePNG(String filename) {

		File f = new File(filename);
		try {
			// Write out the image to the file as a png
			ImageIO.write(this.im, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// To pop up a new window for this image
	public void openNewDisplayWindow() {
		this.theDisplay = new ImagePanel(this);
	}
	
	// To mutate the existing window to fit the image's current dimensions and pixels
	public void repaintCurrentDisplayWindow() {
		this.theDisplay.changeImage(this);
		this.theDisplay.repaint();		
	}
	
	public void switchImage(UWECImage theNewImage) {
		this.im = theNewImage.im;
	}

	public void transpose() {
		UWECImage transposedImage = new UWECImage(getHeight(), getWidth());
		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				transposedImage.setRGB(y, x, getRed(x,y), getGreen(x,y), getBlue(x,y));
			}
		}
		switchImage(transposedImage);
	}
}