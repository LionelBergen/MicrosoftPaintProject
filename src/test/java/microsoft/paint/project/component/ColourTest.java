package microsoft.paint.project.component;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ColourTest {
	private static final Colour BLACK = new Colour(0, 0, 0);
	private static final Colour WHITE = new Colour(255, 255, 255);
	
	private static final Colour WHITE_1 = new Colour(250, 242, 242);
	
	@Test
	public void testSimilairTo() {
		Colour blackColour = new Colour(0, 0, 0);
		
		assertFalse(WHITE.similairTo(WHITE_1, 0));
		assertFalse(WHITE_1.similairTo(WHITE, 0));
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
