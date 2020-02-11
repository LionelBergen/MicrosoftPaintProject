package microsoft.paint.project.image.processor;

import java.util.Comparator;

import microsoft.paint.project.component.ColourCoordinate;

public class CoordinateComparator implements Comparator<ColourCoordinate> {
	private ColourCoordinate origin;
	
	public CoordinateComparator() {
		this(null);
	}
	
	public CoordinateComparator(ColourCoordinate origin) {
		this.origin = origin;
	}
	
	@Override
	public int compare(ColourCoordinate o1, ColourCoordinate o2) {
		double angle;
		
		if(origin != null) {
			angle = (o1.getX() - origin.getX()) * (o2.getY() - origin.getY()) - (o2.getX() - origin.getX()) * (o1.getY() - origin.getY());
		} else {
			angle = (o1.getX() * o2.getY()) - (o1.getY() * o2.getX());
		}
		
		return (int) angle;
	}

}
