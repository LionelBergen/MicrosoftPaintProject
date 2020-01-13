package microsoft.paint.project.screen;

import microsoft.paint.project.Colour;

public class ColourCoordinate {
	private final Colour colour;
	private final int x;
	private final int y;

	public ColourCoordinate(int red, int green, int blue, int x, int y) {
		this.colour = new Colour(red, green, blue);
		this.x = x;
		this.y = y;
	}
	
	public Colour getColour() {
		return this.colour;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	@Override
	public boolean equals(Object otherObj) {
		if (otherObj == null || otherObj.getClass() != this.getClass()) {
			return false;
		}
		
		ColourCoordinate other = (ColourCoordinate) otherObj;
		
		return this.colour.equals(other.getColour()) && this.x == other.x && this.y == other.getY();
	}
}
