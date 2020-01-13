package microsoft.paint.project.image.processor;

import java.awt.image.BufferedImage;

import microsoft.paint.project.component.Colour;

public class ImageProcessor {
	private ImageProcessor() { }
	
	public static Colour GetColourFromImage(BufferedImage image, int x, int y) {
		// Getting pixel color by position x and y 
		int clr = image.getRGB(0, 0); 
		int red = (clr & 0x00ff0000) >> 16;
		int green = (clr & 0x0000ff00) >> 8;
		int blue =  clr & 0x000000ff;
		
		return new Colour(red, green, blue);
	}
}
