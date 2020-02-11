package microsoft.paint.project.component;

public class ColourCoordinate extends Coordinate {
	private Colour colour;

	public ColourCoordinate(int red, int green, int blue, int x, int y) {
		this(new Colour(red, green, blue), x, y);
	}
	
	public ColourCoordinate(Colour colour, int x, int y) {
		this.colour = colour;
		this.x = x;
		this.y = y;
	}
	
	public Colour getColour() {
		return this.colour;
	}
	
	public void setColour(Colour colour) {
		this.colour = colour;
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
