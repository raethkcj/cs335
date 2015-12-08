package edu.uwec.cs.raethkcj.seamcarving;

public class Seam {
	static UWECImage image;
	private static boolean transposed;

	public static UWECImage verticalSeamShrink(UWECImage theImage) throws InterruptedException {
		image = theImage;
		int[][] energies = findEnergies();

		int[][] optimalEnergies = new int[image.getWidth()][image.getHeight()];
		int[][] traceback = new int[image.getWidth()][image.getHeight()];
		
		for(int x = 0; x < image.getWidth(); x++) {
			optimalEnergies[x][0] = energies[x][0];
		}

		//Calculate all optimal energies and fill out the traceback table
        for(int y = 1; y < image.getHeight(); y++) {
        	for(int x = 0; x < image.getWidth(); x++) {
                int left = x != 0 ? optimalEnergies[x-1][y-1] : Integer.MAX_VALUE;
        		int center = optimalEnergies[x][y-1];
        		int right = x != image.getWidth()-1 ? optimalEnergies[x+1][y-1] : Integer.MAX_VALUE;
        		
        		int min = Integer.MAX_VALUE;
        		int relativeLocation = 0;
        		if(left < min) {
        			min = left;
        			relativeLocation = -1;
        		}
        		if(center < min) {
        			min = center;
        			relativeLocation = 0;
        		}
        		if(right < min) {
        			min = right;
        			relativeLocation = 1;
        		}

        		optimalEnergies[x][y] = min + energies[x][y];
        		traceback[x][y] = relativeLocation;
			}
		}
        
        //Find the minimum energy in the last row
        int minOptimalEnergy = Integer.MAX_VALUE;
        int location = -1;
        for(int x = 0; x < image.getWidth(); x++) {
        	if(optimalEnergies[x][image.getHeight()-1] < minOptimalEnergy) {
        		minOptimalEnergy = optimalEnergies[x][image.getHeight()-1];
        		location = x;
        	}
        }
        
        //Highlight the seam and display it
        highlightAndDisplaySeam(traceback, location); 
        
        Thread.sleep(100);
        
        //Remove the seam from the image
        UWECImage resizedImage = removeSeam(traceback, location);

		return resizedImage;
	}

	public static UWECImage horizontalSeamSrhink(UWECImage image) throws InterruptedException {
		image.transpose();
		transposed = true;
		image = verticalSeamShrink(image);
		image.transpose();
		transposed = false;
		return image;
	}
	
	private static int[][] findEnergies() {
		int[][] energies = new int[image.getWidth()][image.getHeight()];
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				energies[x][y] = findEnergy(x, y);
			}
		}
		return energies;
	}
	
	private static int findEnergy(int x, int y) {
		int left = Math.floorMod(x-1, image.getWidth());
		int right = Math.floorMod(x+1, image.getWidth());
		int squaredGradientX = (int)Math.pow(Math.abs(image.getRed(right, y) - image.getRed(left, y)), 2)
								+ (int)Math.pow(Math.abs(image.getBlue(right, y) - image.getBlue(left, y)), 2)
								+ (int)Math.pow(Math.abs(image.getGreen(right, y) - image.getGreen(left, y)), 2);
		
		int up = Math.floorMod(y-1, image.getHeight());
		int down = Math.floorMod(y+1, image.getHeight());
		int squaredGradientY = (int)Math.pow(Math.abs(image.getRed(x, down) - image.getRed(x, up)), 2)
								+ (int)Math.pow(Math.abs(image.getBlue(x, down) - image.getBlue(x, up)), 2)
								+ (int)Math.pow(Math.abs(image.getGreen(x, down) - image.getGreen(x, up)), 2);
		return squaredGradientX + squaredGradientY;
	}
	
	private static void highlightAndDisplaySeam(int[][] traceback, int location) {
		int x = location;
        for(int y = image.getHeight() - 1; y > 0; y--) {
        	image.setRGB(x, y, 0, 255, 0);
        	x += traceback[x][y];
        }
        if(transposed) { 
        	image.transpose();
        	image.repaintCurrentDisplayWindow();
        	image.transpose();
        } else {
        	image.repaintCurrentDisplayWindow();
        }
	}
	
	private static UWECImage removeSeam(int[][] traceback, int location) {
        UWECImage resizedImage = new UWECImage(image.getWidth() - 1, image.getHeight());
        int x = location;
        for(int y = image.getHeight() - 1; y >= 0; y--) {
        	for(int i = 0; i < x; i++) {
        		resizedImage.setRGB(i, y, image.getRed(i, y), image.getGreen(i, y), image.getBlue(i, y));
        	}
        	for(int j = x; j < resizedImage.getWidth(); j++) {
        		resizedImage.setRGB(j, y, image.getRed(j+1, y), image.getGreen(j+1, y), image.getBlue(j+1, y));
        	}
        	x += traceback[x][y];
        }
        return resizedImage;
	}
}
