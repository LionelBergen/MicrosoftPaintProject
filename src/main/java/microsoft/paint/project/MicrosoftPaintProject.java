package microsoft.paint.project;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import microsoft.paint.project.automate.Autobot;
import microsoft.paint.project.component.Colour;
import microsoft.paint.project.component.ColourCoordinate;
import microsoft.paint.project.image.processor.CoordinateProcessor;
import microsoft.paint.project.image.processor.ImageProcessor;
import microsoft.paint.project.screen.MSPaintScreen;

public class MicrosoftPaintProject {
	public static void main(String[] args) throws Exception {
		Process process = new ProcessBuilder("MSPaint").start();
		
		final Autobot robot = new Autobot();

		// MSPaint will be open after this while loop finishes
		while (!MSPaintScreen.isMSPaintOpen(robot)) {
			TimeUnit.SECONDS.sleep(2);
		}

		File testFile = new File("C:\\Users\\Lionel\\Desktop\\redditstuff\\MicrosoftPaintProject\\stuff\\testimage.jpg");
		BufferedImage testImage = ImageIO.read(testFile);
		
		System.out.println("Image width: " + testImage.getWidth());
		System.out.println("Image height: " + testImage.getHeight());
		
		//enterPaintScreenSize(robot, testImage.getWidth(), testImage.getHeight());
		
		// Get all colours from the image
		Set<ColourCoordinate> colourCoords = new HashSet<ColourCoordinate>();
		for(int x=0; x<testImage.getWidth(); x++) {
			for (int y=0; y<testImage.getHeight(); y++) {
				colourCoords.add(new ColourCoordinate(ImageProcessor.GetColourFromImage(testImage, x, y), x, y));
			}
		}
		System.out.println(colourCoords.stream().map(e -> e.getColour()).distinct().count());
		
		// Replace colours with ones from our default palette where possible
		for (ColourCoordinate colourCoord : colourCoords) {
			Colour fromPallet = MSPaintScreen.getColourFromPallet(colourCoord.getColour());
			
			if (fromPallet != null) {
				colourCoord.setColour(fromPallet);
			}
		}
		System.out.println(colourCoords.stream().map(e -> e.getColour()).distinct().count());
		
		// Replace colours with other ones that are similar
		for (ColourCoordinate colourCoord : colourCoords) {
			Colour colourToReplaceWith = findSimilair(colourCoord, colourCoords);
			
			if (colourToReplaceWith != null) {
				colourCoord.setColour(colourToReplaceWith);
			}
		}
		
		// Get a list of unique colours and count the number of times that colour is used in our image
		List<Colour> uniqueColours = colourCoords.stream().map(e -> e.getColour()).distinct().collect(Collectors.toList());
		Map<Colour, Integer> coloursWithCount = new HashMap<Colour, Integer>();
		
		for (Colour colourCoord : uniqueColours) {
			coloursWithCount.put(colourCoord, Integer.valueOf(String.valueOf(colourCoords.stream().filter(e -> e.getColour().equals(colourCoord)).count())));
		}
		
		coloursWithCount = coloursWithCount.entrySet().stream()
			    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		for (Entry<Colour, Integer> y : coloursWithCount.entrySet()) {
			System.out.println("key: " + y.getKey() + " value: " + y.getValue());
		}

		System.out.println(coloursWithCount.size());
		
		// Zoom in 3 times. This will make it so our coordinates match up with each pixel. For example, X 100 Y 100 will be pixel 100 100
		robot.mouseClick(MSPaintScreen.getZoomInButton());
		robot.delay(50);
		robot.mouseClick(MSPaintScreen.getZoomInButton());
		robot.delay(50);
		robot.mouseClick(MSPaintScreen.getZoomInButton());
		
		Iterator<Colour> coloursFromSet = coloursWithCount.keySet().iterator();
		Colour firstColour = coloursFromSet.next();
		
		// fill the screen with the most common colour
		enterCustomColour(robot, firstColour);
		robot.mouseClick(MSPaintScreen.getPaintCanButton());
		robot.delay(50);
		robot.mouseClick(MSPaintScreen.getDrawArea());
		
		// remove it from our list since it's been painted
		coloursWithCount.remove(firstColour);

		// for each colour, draw it
		while (coloursFromSet.hasNext()) {
			
		}
	}
	
	private void drawColour(Colour colourToDraw, Integer numberOfPixels, Set<ColourCoordinate> fullColourList) {
		// Get all Coordinates that match the colour:
		Set<ColourCoordinate> colourCoords = fullColourList.stream().filter(e -> e.getColour().equals(colourToDraw)).collect(Collectors.toSet());
		
		// Split the coordinates into groups that are 'touching'
		Set<Set<ColourCoordinate>> splitCoords = CoordinateProcessor.getTouchingCoordinates(colourCoords);
		
		// draw the outline of each & fill
		for (Set<ColourCoordinate> coords : splitCoords) {
			
		}
	}
	
	private static Colour findSimilair(ColourCoordinate colourCoord, Collection<ColourCoordinate> colourCoords) {
		for (ColourCoordinate otherColourCoord : colourCoords) {
			if (colourCoord != otherColourCoord && otherColourCoord.getColour().similairTo(colourCoord.getColour())) {
				return otherColourCoord.getColour();
			}
		}
		
		return null;
	}
	
	private static void enterCustomColour(Autobot robot, Colour colour) {
		while (!MSPaintScreen.isCustomColourPromptOpen(robot)) {
			robot.mouseClick(MSPaintScreen.getEditColoursButton());
			robot.delay(2000);
		}
		
		List<Point> RGB_INPUTS = MSPaintScreen.getRedGreenBlueColourPointsForCustomColourDialog();
		
		// Red
		robot.mouseDoubleClick(RGB_INPUTS.get(0));
		robot.delay(50);
		robot.enterKeys(colour.getRed());
		robot.delay(50);
		
		// Green
		robot.mouseDoubleClick(RGB_INPUTS.get(1));
		robot.delay(50);
		robot.enterKeys(colour.getGreen());
		robot.delay(50);
		
		// Blue
		robot.mouseDoubleClick(RGB_INPUTS.get(2));
		robot.delay(50);
		robot.enterKeys(colour.getBlue());
		robot.delay(50);
		
		robot.keyPress(KeyEvent.VK_ENTER);
	}
	
	private static void enterPaintScreenSize(Autobot robot, int width, int height) {
		while (!MSPaintScreen.isImagePropertiesPromptOpen(robot)) {
			robot.pressCtrlKeyWithAnotherKey(KeyEvent.VK_E);
			robot.delay(2000);
		}
		
		System.out.println("Image properties is open, entering: " + width + " and " + height);
		
		robot.enterKeys(width);
		robot.delay(200);
		robot.pressAndReleaseKey(KeyEvent.VK_TAB);
		robot.delay(200);
		robot.enterKeys(height);
		robot.delay(200);
		robot.pressAndReleaseKey(KeyEvent.VK_ENTER);
	}
}
