package edu.uwec.cs.raethkcj.seamcarving;

public class SeamDemo {
	public static void main(String[] args) throws InterruptedException {
		UWECImage image = new UWECImage("surfers.png");
		image.openNewDisplayWindow();
		for(int i = 0; i < 150; i++) {
			image.switchImage(Seam.verticalSeamShrink(image));
			image.repaintCurrentDisplayWindow();
		}
		for(int i = 0; i < 100; i++) {
			image.switchImage(Seam.horizontalSeamSrhink(image));
			image.repaintCurrentDisplayWindow();
		}
		image.write("surfers-resized.png");;
	}
}
