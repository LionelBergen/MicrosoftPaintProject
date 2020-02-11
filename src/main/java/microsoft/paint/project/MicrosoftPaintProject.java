package microsoft.paint.project;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
import microsoft.paint.project.component.Cursor;
import microsoft.paint.project.coordinate.processor.GrahamScan;
import microsoft.paint.project.coordinate.processor.GrahamScanResult;
import microsoft.paint.project.image.processor.CoordinateProcessor;
import microsoft.paint.project.image.processor.Helper;
import microsoft.paint.project.image.processor.ImageProcessor;
import microsoft.paint.project.screen.MSPaintScreen;
import microsoft.paint.project.screen.MSPaintScreenProperties;

public class MicrosoftPaintProject {
	private static Date startTime = new Date();
	private static final long SECONDS_PAUSE_EVERY = 50L;
	
	private static final int CANVAS_START_X = 5;
	private static final int CANVAS_START_Y = 143;
	private static final double CANVAS_NORMALIZE = 2.631578947368421;
	
	private static Cursor currentPaintCursor = Cursor.BRUSH;
	
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
		
		// enterPaintScreenSize(robot, testImage.getWidth(), testImage.getHeight());
		
		// Get all colours from the image
		Set<ColourCoordinate> colourCoords = Helper.GetUniqueColoursFromImage(testImage);
		
		// Replace colours with other ones that are similar
		Helper.SimplifyColourCoordinates(colourCoords);
		
		Helper.ReplaceColoursWithOnesFromPallet(colourCoords);
		
		// Get a list of unique colours and count the number of times that colour is used in our image
		Map<Colour, Integer> coloursWithCount = Helper.GetColoursWithOccurances(colourCoords);

		for (Entry<Colour, Integer> y : coloursWithCount.entrySet()) {
			System.out.println("key: " + y.getKey() + " value: " + y.getValue());
		}
		System.out.println("Number of colours after merging: " + coloursWithCount.size());
		
		// Zoom in 3 times. This will make it so our coordinates match up with each pixel. For example, X 100 Y 100 will be pixel 100 100
		robot.mouseClick(MSPaintScreen.getZoomInButton());
		robot.delay(50);
		robot.mouseClick(MSPaintScreen.getZoomInButton());
		robot.delay(50);
		robot.mouseClick(MSPaintScreen.getZoomInButton());
		
		Iterator<Colour> coloursFromSet = coloursWithCount.keySet().iterator();
		Colour firstColour = coloursFromSet.next();
		
		// Fill if the default colour is not the first colour
		if (!MSPaintScreenProperties.DEFAULT_COLOUR.equals(firstColour)) {
			// fill the screen with the most common colour
			enterColour(robot, firstColour);
			robot.mouseClick(MSPaintScreen.getPaintCanButton());
			robot.delay(50);
			robot.mouseClick(MSPaintScreen.getDrawArea());
		} else {
			System.out.println("default colour used. No need to fill screen");
		}
		
		// remove it from our list since it's been painted
		coloursWithCount.remove(firstColour);
		
		coloursFromSet = coloursWithCount.keySet().iterator();

		startTime = new Date();
		
		// for each colour, draw it
		while (coloursFromSet.hasNext()) {
			final Colour colour = coloursFromSet.next();
			drawColour(robot, colour, colourCoords);
		}
	}
	
	private static void drawColour(Autobot robot, Colour colourToDraw, Set<ColourCoordinate> fullColourList) {
		System.out.println("Drawing colour: " + colourToDraw);
		enterColour(robot, colourToDraw);
		
		// Get all Coordinates that match the colour:
		Set<ColourCoordinate> colourCoords = fullColourList.stream().filter(e -> e.getColour().equals(colourToDraw)).collect(Collectors.toSet());
		System.out.println("got colour coords");
		
		// Split the coordinates into groups that are 'touching'
		Set<Set<ColourCoordinate>> splitCoords = CoordinateProcessor.getTouchingCoordinates(colourCoords);
		System.out.println("got split coords");
		
		// draw the outline of each & fill
		for (Set<ColourCoordinate> coords : splitCoords) {
			System.out.println("processing: " + coords.size() + " coords");
			
			if(coords.size() < 3) {
				continue;
			}
			
			// Get the outer coordinates:
			GrahamScanResult grahamScan = GrahamScan.getConvexHull(coords);
			List<ColourCoordinate> outerCoords = grahamScan.getResults();
			
			if (outerCoords == null) {
				continue;
			}
			
			changeCursor(robot, Cursor.PENCIL);
			
			// draw the outer coordinates
			for (int i = 0; i < outerCoords.size(); i++) {
				ColourCoordinate coord = outerCoords.get(i);
				
				if (i ==0) {
					mousePress(robot, coord.getX(), coord.getY());
				} else if (i == outerCoords.size() - 1) {
					// go to last coordinate
					mouseMove(robot, coord.getX(), coord.getY());
					
					// go back to the first coordinate and release
					mouseRelease(robot, outerCoords.get(0).getX(), outerCoords.get(0).getY());
				} else {
					mouseMove(robot, coord.getX(), coord.getY());
				}
				
				robot.delay(50);
			}
			
			//changeCursor(robot, Cursor.FILL_BUCKET);
			//mouseClick(robot, outerCoords.get(0).getX() + 2, outerCoords.get(0).getY() + 2);
		}
	}
	
	private static void changeCursor(Autobot robot, Cursor newCursor) {
		if (currentPaintCursor != newCursor) {
			switch (newCursor) {
				case PENCIL:
					robot.mouseClick(MSPaintScreen.getPencilButton());
					break;
				case FILL_BUCKET:
					robot.mouseClick(MSPaintScreen.getPaintCanButton());
					break;
				default:
					throw new RuntimeException("Unknown cursor type: " + newCursor);
			}
			
			currentPaintCursor = newCursor;
			robot.delay(50);
		}
	}
	
	private static void mouseMove(Autobot robot, int x, int y) {
		x *= CANVAS_NORMALIZE;
		y *= CANVAS_NORMALIZE;
		robot.mouseMove(CANVAS_START_X + x, CANVAS_START_Y + y);
	}
	
	private static void mousePress(Autobot robot, int x, int y) {
		x *= CANVAS_NORMALIZE;
		y *= CANVAS_NORMALIZE;
		robot.mousePress(CANVAS_START_X + x, CANVAS_START_Y + y);
	}
	
	private static void mouseRelease(Autobot robot, int x, int y) {
		x *= CANVAS_NORMALIZE;
		y *= CANVAS_NORMALIZE;
		robot.mouseRelease(CANVAS_START_X + x, CANVAS_START_Y + y);
	}
	
	private static void mouseClick(Autobot robot, int x, int y) {
		x *= CANVAS_NORMALIZE;
		y *= CANVAS_NORMALIZE;
		robot.mouseClick(CANVAS_START_X + x, CANVAS_START_Y + y);
	}
	
	private static void enterColour(Autobot robot, Colour colour) {
		ColourCoordinate colourFromPallet = MSPaintScreen.getColourFromPallet(colour, true);
		if (colourFromPallet != null) {
			robot.mouseClick(colourFromPallet.getX(), colourFromPallet.getY());
		} else {
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
