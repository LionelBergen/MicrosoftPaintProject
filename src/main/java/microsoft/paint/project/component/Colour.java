package microsoft.paint.project.component;

import java.awt.Color;

/**
 * The Canadian wrapper for a Java.awt.color
 * Adds a {@link similarTo} method.
 * 
 * @author Lionel Bergen
 */
public class Colour {
	private static final double MAX_DISTANCE = 1000.0; //9950.0;
	private final Color color;
	
	public Colour(int red, int green, int blue) {
		this.color = new Color(red, green, blue);
	}
	
	public Colour(Color color) {
		this.color = color;
	}
	
	public int getRed() {
		return this.color.getRed();
	}
	
	public int getGreen() {
		return this.color.getGreen();
	}
	
	public int getBlue() {
		return this.color.getBlue();
	}
	
	public boolean similairTo(Colour colour) {
		return similairTo(colour, MAX_DISTANCE);
	}
	
	public boolean similairTo(Colour colour, double maxDistance) {
		double distance = (colour.getRed() - this.getRed()) * (colour.getRed() - this.getRed()) 
				+ (colour.getGreen() - this.getGreen()) * (colour.getGreen() - this.getGreen()) 
				+ (colour.getBlue() - this.getBlue()) * (colour.getBlue() - this.getBlue());
    
		return distance <= maxDistance;
	}
	
	@Override
	public int hashCode() {
        return this.color.hashCode();
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
	
	@Override
	public String toString() {
		return "Red: " + this.getRed() + " Green: " + this.getGreen() + " Blue: " + this.getBlue();
	}
}
