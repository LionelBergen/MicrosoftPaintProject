package microsoft.paint.project.component;

import java.awt.Color;

/**
 * The Canadian wrapper for a Java.awt.color
 * Adds a {@link similarTo} method.
 * 
 * @author Lionel Bergen
 */
public class Colour {
	private final Color color;
	
	public Colour(int red, int green, int blue) {
		this.color = new Color(red, green, blue);
	}
	
	public int getRed() {
		return this.color.getRed();
	}
	
	public boolean similairTo(Colour colour) {
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || other.getClass() != this.getClass()) {
			return false;
		}
		
		return this.color.equals(((Colour)other).color);
	}
}
