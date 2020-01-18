package microsoft.paint.project.component;

public class ColourCoordinate {
	private Colour colour;
	private int x;
	private int y;

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
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int y) {
		this.y = y;
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
