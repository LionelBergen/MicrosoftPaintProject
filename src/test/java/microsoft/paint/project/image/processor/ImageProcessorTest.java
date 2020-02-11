package microsoft.paint.project.image.processor;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import microsoft.paint.project.component.Colour;

public class ImageProcessorTest {
	private static File TEST_FILE = new File("C:\\Users\\Lionel\\Desktop\\redditstuff\\MicrosoftPaintProject\\stuff\\testimage.jpg");
	private static final BufferedImage IMAGE;
	
	static
	{
		try
		{
			IMAGE = ImageIO.read(TEST_FILE);
		} 
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testGetColour()
	{
		// White spots
		assertColour(255, 255, 255, ImageProcessor.GetColourFromImage(IMAGE, 0, 0));
		assertColour(255, 255, 255, ImageProcessor.GetColourFromImage(IMAGE, 10, 10));
		assertColour(255, 255, 255, ImageProcessor.GetColourFromImage(IMAGE, 20, 20));
		assertColour(255, 255, 255, ImageProcessor.GetColourFromImage(IMAGE, 147, 240));
		assertColour(255, 255, 255, ImageProcessor.GetColourFromImage(IMAGE, 115, 176));
		
		// Black spots including edges
		assertColour(0, 0, 0, ImageProcessor.GetColourFromImage(IMAGE, 80, 171));
		assertColour(0, 0, 0, ImageProcessor.GetColourFromImage(IMAGE, 160, 179));
		assertColour(0, 0, 0, ImageProcessor.GetColourFromImage(IMAGE, 112, 200));
	}
	
	private void assertColour(int red, int green, int blue, Colour colour)
	{
		assertEquals(red, colour.getRed());
		assertEquals(green, colour.getGreen());
		assertEquals(blue, colour.getBlue());
	}
}
