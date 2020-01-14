package microsoft.paint.project.component;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ColourTest {
	private static final Colour BLACK = new Colour(0, 0, 0);
	private static final Colour WHITE = new Colour(255, 255, 255);
	
	private static final Colour WHITE_1 = new Colour(250, 242, 242);
	private static final Colour WHITE_2 = new Colour(236, 234, 234);
	
	@Test
	public void testSimilairToTrue() {
		assertTrue(WHITE.similairTo(WHITE_1));
		assertTrue(WHITE_1.similairTo(WHITE));
		
		assertTrue(WHITE_2.similairTo(WHITE_1));
		assertTrue(WHITE_1.similairTo(WHITE_2));
	}
	
	@Test
	public void testSimilairToFalse() {
		assertFalse(BLACK.similairTo(WHITE));
		assertFalse(WHITE.similairTo(BLACK));

		assertFalse(WHITE.similairTo(WHITE_2));
		assertFalse(WHITE_2.similairTo(WHITE));
	}
	
	@Test
	public void testEquals() {
		Colour firstColour = new Colour(0, 0, 0);
		Colour secondColour = new Colour(0, 0, 0);
		
		assertFalse(firstColour.equals(null));
		
		assertTrue(firstColour.equals(firstColour));
		
		assertTrue(firstColour.equals(secondColour));
		assertTrue(secondColour.equals(firstColour));
		
		Colour differentColour = new Colour(1, 0, 0);
		assertFalse(differentColour.equals(firstColour));
		assertFalse(firstColour.equals(differentColour));
	}
}
