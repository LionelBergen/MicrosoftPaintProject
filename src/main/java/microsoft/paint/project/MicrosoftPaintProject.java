package microsoft.paint.project;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import microsoft.paint.project.component.Colour;
import microsoft.paint.project.image.processor.ImageProcessor;
import microsoft.paint.project.screen.MSPaintScreen;

public class MicrosoftPaintProject {
	public static void main(String[] args) throws Exception {
		Process process = new ProcessBuilder("MSPaint").start();
		
		final Robot robot = new Robot();

		// MSPaint will be open after this while loop finishes
		while (!MSPaintScreen.isMSPaintOpen(robot)) {
			TimeUnit.SECONDS.sleep(1);
		}
		
		File testFile = new File("C:\\Users\\Lionel\\Desktop\\redditstuff\\MicrosoftPaintProject\\stuff\\testimage.jpg");
		BufferedImage testImage = ImageIO.read(testFile);
		
		System.out.println("Image width: " + testImage.getWidth());
		System.out.println("Image height: " + testImage.getHeight());
		
		long startTime = System.currentTimeMillis();
		Set<Colour> colourCoords = new HashSet<Colour>();
		for(int x=0; x<testImage.getWidth(); x++) {
			for (int y=0; y<testImage.getHeight(); y++) {
				colourCoords.add(ImageProcessor.GetColourFromImage(testImage, x, y));
			}
		}
		
		System.out.println(colourCoords.size());
		System.out.println("Took: " + (System.currentTimeMillis() - startTime) + " milliseconds"); 
	}
}
