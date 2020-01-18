package microsoft.paint.project.image.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import microsoft.paint.project.component.Colour;
import microsoft.paint.project.component.ColourCoordinate;

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
		List<ColourCoordinate> testCoords = new ArrayList<>();
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 5));
		testCoords.add(new ColourCoordinate(0, 0, 0, 6, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 7, 15));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 0));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 6));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 7));
		testCoords.add(new ColourCoordinate(0, 0, 0, 5, 8));
		testCoords.add(new ColourCoordinate(0, 0, 0, 8, 5));
		
		Set<ColourCoordinate> results = CoordinateProcessor.getTopSide(testCoords);
		assertEquals(4, results.size());
		
		assertEquals(5, results.stream().filter(e -> e.getX() == 5).findAny().get().getY());
		assertEquals(8, results.stream().filter(e -> e.getX() == 6).findAny().get().getY());
		assertEquals(15, results.stream().filter(e -> e.getX() == 7).findAny().get().getY());
		assertEquals(0, results.stream().filter(e -> e.getX() == 8).findAny().get().getY());
	}
}