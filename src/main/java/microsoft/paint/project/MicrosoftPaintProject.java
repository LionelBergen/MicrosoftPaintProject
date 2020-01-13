package microsoft.paint.project;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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
		System.out.println(testFile);
		BufferedImage testImage = ImageIO.read(testFile);

		// Getting pixel color by position x and y 
		int clr =  testImage.getRGB(0, 0); 
		int red   = (clr & 0x00ff0000) >> 16;
		int green = (clr & 0x0000ff00) >> 8;
		int blue  =  clr & 0x000000ff;
		System.out.println("Red Color value = "+ red);
		System.out.println("Green Color value = "+ green);
		System.out.println("Blue Color value = "+ blue);
	}
}
