package microsoft.paint.project.image.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.junit.Test;

import microsoft.paint.project.component.Colour;
import microsoft.paint.project.component.ColourCoordinate;
import microsoft.paint.project.screen.MSPaintScreen;

public class CoordinateProcessorTest {
	@Test
	public void testGetTouchingCoordinates() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 2));
		testCoords.add(new ColourCoordinate(0, 0, 0, 2, 2));
		testCoords.add(new ColourCoordinate(0, 0, 0, 3, 2));
		testCoords.add(new ColourCoordinate(0, 0, 0, 0, 1));
		
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 4));
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 2, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 3, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 0, 4));
		
		Set<Set<ColourCoordinate>> results = CoordinateProcessor.getTouchingCoordinates(testCoords);
		assertFalse(results.isEmpty());
		
		Iterator<Set<ColourCoordinate>> resultsIter = results.iterator();
		
		Set<ColourCoordinate> result1 = resultsIter.next();
		assertFalse(result1.isEmpty());
		assertEquals(5, result1.size());
		
		assertEquals(2, results.size());
		
		Set<ColourCoordinate> result2 = resultsIter.next();
		assertFalse(result2.isEmpty());
		assertEquals(5, result2.size());
	}
	
	@Test
	public void testGetTouchingCoordinatesEmpty() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		
		Set<Set<ColourCoordinate>> results = CoordinateProcessor.getTouchingCoordinates(testCoords);
		assertTrue(results.isEmpty());
	}
	
	@Test
	public void testIsTouchingTrue() {
		ColourCoordinate a = new ColourCoordinate(new Colour(0, 0, 0), 10, 10);
		ColourCoordinate b = new ColourCoordinate(new Colour(0, 0, 0), 10, 10);
		
		assertTrue(CoordinateProcessor.isTouching(a, b));
		assertTrue(CoordinateProcessor.isTouching(b, a));
		
		a.setX(11);
		assertTrue(CoordinateProcessor.isTouching(a, b));
		assertTrue(CoordinateProcessor.isTouching(b, a));
		
		b.setX(11);
		b.setY(11);
		assertTrue(CoordinateProcessor.isTouching(a, b));
		assertTrue(CoordinateProcessor.isTouching(b, a));
	}
	
	@Test
	public void testIsTouchingFalse() {
		ColourCoordinate a = new ColourCoordinate(new Colour(0, 0, 0), 10, 10);
		ColourCoordinate b = new ColourCoordinate(new Colour(0, 0, 0), 10, 12);
		
		assertFalse(CoordinateProcessor.isTouching(a, b));
		assertFalse(CoordinateProcessor.isTouching(b, a));
		
		b.setY(10);
		b.setX(12);
		
		assertFalse(CoordinateProcessor.isTouching(a, b));
		assertFalse(CoordinateProcessor.isTouching(b, a));
	}
	
	@Test
	public void testGetTopSide() {
		Collection<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 6, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 7, 15));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 0));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 7));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 5));
		
		Collection<ColourCoordinate> results = CoordinateProcessor.getTopSide(testCoords);
		assertEquals(4, results.size());
		
		assertEquals(5, results.stream().filter(e -> e.getX() == 5).findAny().get().getY());
		assertEquals(8, results.stream().filter(e -> e.getX() == 6).findAny().get().getY());
		assertEquals(15, results.stream().filter(e -> e.getX() == 7).findAny().get().getY());
		assertEquals(0, results.stream().filter(e -> e.getX() == 8).findAny().get().getY());
		
		// assert order. Should be from left -> right
		Iterator<ColourCoordinate> resultsIter = results.iterator();
		assertEquals(5, resultsIter.next().getX());
		assertEquals(6, resultsIter.next().getX());
		assertEquals(7, resultsIter.next().getX());
		assertEquals(8, resultsIter.next().getX());
	}
	
	@Test
	public void testGetBottomSide() {
		Collection<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 6, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 7, 15));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 0));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 7));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 8));
		
		Collection<ColourCoordinate> results = CoordinateProcessor.getBottomSide(testCoords);
		assertEquals(4, results.size());
		
		assertEquals(8, results.stream().filter(e -> e.getX() == 5).findAny().get().getY());
		assertEquals(8, results.stream().filter(e -> e.getX() == 6).findAny().get().getY());
		assertEquals(15, results.stream().filter(e -> e.getX() == 7).findAny().get().getY());
		assertEquals(5, results.stream().filter(e -> e.getX() == 8).findAny().get().getY());
		
		// assert order. Should be from left -> right
		Iterator<ColourCoordinate> resultsIter = results.iterator();
		assertEquals(5, resultsIter.next().getX());
		assertEquals(6, resultsIter.next().getX());
		assertEquals(7, resultsIter.next().getX());
		assertEquals(8, resultsIter.next().getX());
	}
	
	@Test
	public void testGetLeftSide() {
		Collection<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 7));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 10, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 10, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 10, 7));
		
		Collection<ColourCoordinate> results = CoordinateProcessor.getLeftSide(testCoords);
		assertEquals(4, results.size());
		
		assertEquals(5, results.stream().filter(e -> e.getY() == 5).findAny().get().getX());
		assertEquals(5, results.stream().filter(e -> e.getY() == 6).findAny().get().getX());
		assertEquals(5, results.stream().filter(e -> e.getY() == 7).findAny().get().getX());
		assertEquals(5, results.stream().filter(e -> e.getY() == 8).findAny().get().getX());
		
		// assert order. Should be from top -> bottom
		Iterator<ColourCoordinate> resultsIter = results.iterator();
		assertEquals(5, resultsIter.next().getY());
		assertEquals(6, resultsIter.next().getY());
		assertEquals(7, resultsIter.next().getY());
		assertEquals(8, resultsIter.next().getY());
	}
	
	@Test
	public void testGetRightSide() {
		Collection<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 15, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 15, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 15, 7));
		testCoords.add(new ColourCoordinate(0, 0, 0, 15, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 10, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 10, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 10, 7));
		
		Collection<ColourCoordinate> results = CoordinateProcessor.getRightSide(testCoords);
		assertEquals(4, results.size());
		
		assertEquals(15, results.stream().filter(e -> e.getY() == 5).findAny().get().getX());
		assertEquals(15, results.stream().filter(e -> e.getY() == 6).findAny().get().getX());
		assertEquals(15, results.stream().filter(e -> e.getY() == 7).findAny().get().getX());
		assertEquals(15, results.stream().filter(e -> e.getY() == 8).findAny().get().getX());
		
		// assert order. Should be from top -> bottom
		Iterator<ColourCoordinate> resultsIter = results.iterator();
		assertEquals(5, resultsIter.next().getY());
		assertEquals(6, resultsIter.next().getY());
		assertEquals(7, resultsIter.next().getY());
		assertEquals(8, resultsIter.next().getY());
	}
	
	@Test
	public void testRemoveMiddleCoordinates() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 2, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 3, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 4, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 2));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 3));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 4));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 5));
		
		List<ColourCoordinate> results = CoordinateProcessor.removeMiddleCoordinates(testCoords);
		// assert left->right kept
		assertTrue(results.contains(testCoords.get(0)));
		assertTrue(results.contains(testCoords.get(4)));
		
		// assert top->bottom kept
		assertTrue(results.contains(testCoords.get(5)));
		assertTrue(results.contains(testCoords.get(9)));
		
		// assert deleted
		assertEquals(4, results.size());
		
		// Ensure we didn't modify the original list
		assertEquals(10, testCoords.size());
	}
	
	// Ensure we don't depend on the order of the list
	@Test
	public void testRemoveMiddleCoordinatesAnyOrder() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 7, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 3, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 6, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 9, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 4, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 2, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 1));
		
		List<ColourCoordinate> results = CoordinateProcessor.removeMiddleCoordinates(testCoords);
		assertTrue(results.contains(testCoords.get(4)));
		assertTrue(results.contains(testCoords.get(9)));
		assertEquals(2, results.size());
		
		// Ensure we didn't modify the original list
		assertEquals(10, testCoords.size());
	}
	
	@Test
	public void testRemoveMiddleCoordinatesEmpty() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		
		List<ColourCoordinate> results = CoordinateProcessor.removeMiddleCoordinates(testCoords);
		assertNotNull(results);
		assertTrue(results.isEmpty());
	}
	
	@Test
	public void testRemoveMiddleCoordinatesSingleCoord() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 1));
		
		List<ColourCoordinate> results = CoordinateProcessor.removeMiddleCoordinates(testCoords);
		assertTrue(results.contains(testCoords.get(0)));
		assertEquals(1, results.size());
	}
	
	@Test
	public void testRemoveMiddleCoordinatesNoneFiltered() {
		List<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 1, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 3, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 1));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 3));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 5));
		
		List<ColourCoordinate> results = CoordinateProcessor.removeMiddleCoordinates(testCoords);
		assertEquals(testCoords.size(), results.size());
	}
	
	@Test
	public void testGetOutermostCoordinates() throws Exception {
		File testFile = new File("C:\\Users\\Lionel\\Desktop\\redditstuff\\MicrosoftPaintProject\\stuff\\testimage.jpg");
		BufferedImage testImage = ImageIO.read(testFile);
		
		Collection<ColourCoordinate> colourCoords = Helper.GetUniqueColoursFromImage(testImage);
		Helper.SimplifyColourCoordinates(colourCoords);
		Colour blackColour = Helper.GetColoursWithOccurances(colourCoords).keySet().stream().collect(Collectors.toList()).get(1);
		
		// Get all Coordinates that match the colour:
		colourCoords = colourCoords.stream().filter(e -> e.getColour().equals(blackColour)).collect(Collectors.toSet());
		
		// Split the coordinates into groups that are 'touching'
		Set<Set<ColourCoordinate>> splitCoords = CoordinateProcessor.getTouchingCoordinates(colourCoords);
		
		assertEquals(1, splitCoords.size());
		
		colourCoords = splitCoords.iterator().next();
		assertEquals(0, colourCoords.iterator().next().getColour().getBlue());
		assertEquals(1030, colourCoords.size());
		
		colourCoords = CoordinateProcessor.getOutermostCoordinates(colourCoords);
		
		assertEquals(42, colourCoords.size());
		
		
	}
}