package microsoft.paint.project.coordinate.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import microsoft.paint.project.component.ColourCoordinate;
import microsoft.paint.project.component.Coordinate;

// TODO: ensure MIT license is on project, link to: https://opensource.org/licenses/MIT
/**
 * Stolen from: https://github.com/bkiers/GrahamScan/blob/master/src/main/cg/GrahamScan.java
 */
public class GrahamScan {
	private static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }
	
	public static GrahamScanResult getConvexHull(Collection<ColourCoordinate> points) throws IllegalArgumentException {
        List<ColourCoordinate> sorted = new ArrayList<>(getSortedPointSet(points));
        
        GrahamScanResult result = new GrahamScanResult();

        if(sorted.size() < 3) {
        	result.setResultType(GrahamScanResultType.LESS_THAN_3_POINTS);
        } else if(areAllCollinear(sorted)) {
        	result.setResultType(GrahamScanResultType.STRAIGHT_LINE);
        } else {
	        Stack<ColourCoordinate> stack = new Stack<>();
	        stack.push(sorted.get(0));
	        stack.push(sorted.get(1));
	
	        for (int i = 2; i < sorted.size(); i++) {
	        	ColourCoordinate head = sorted.get(i);
	        	ColourCoordinate middle = stack.pop();
	        	ColourCoordinate tail = stack.peek();
	
	            Turn turn = getTurn(tail, middle, head);
	
	            switch(turn) {
	                case COUNTER_CLOCKWISE:
	                    stack.push(middle);
	                    stack.push(head);
	                    break;
	                case CLOCKWISE:
	                    i--;
	                    break;
	                case COLLINEAR:
	                    stack.push(head);
	                    break;
	            }
	        }
	
	        // close the hull
	        stack.push(sorted.get(0));
	        
	        result.setResults(new ArrayList<>(stack));
        }

        return result;
    }
	
	protected static boolean areAllCollinear(List<ColourCoordinate> points) {
        if(points.size() < 2) {
            return true;
        }

        final Coordinate a = points.get(0);
        final Coordinate b = points.get(1);

        for(int i = 2; i < points.size(); i++) {
        	Coordinate c = points.get(i);

            if(getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }
	
	private static Turn getTurn(Coordinate a, Coordinate b, Coordinate c) {

        // use longs to guard against int-over/underflow
        long crossProduct = (((long)b.getX() - a.getX()) * ((long)c.getY() - a.getY())) -
                            (((long)b.getY() - a.getY()) * ((long)c.getX() - a.getX()));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
	
	private static Set<ColourCoordinate> getSortedPointSet(Collection<ColourCoordinate> points) {
        final Coordinate lowest = getLowestPoint(points);

        TreeSet<ColourCoordinate> set = new TreeSet<>(new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate a, Coordinate b) {
                if(a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long)a.getY() - lowest.getY(), (long)a.getX() - lowest.getX());
                double thetaB = Math.atan2((long)b.getY() - lowest.getY(), (long)b.getX() - lowest.getX());

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long)lowest.getX() - a.getX()) * ((long)lowest.getX() - a.getX())) +
                                                (((long)lowest.getY() - a.getY()) * ((long)lowest.getY() - a.getY())));
                    double distanceB = Math.sqrt((((long)lowest.getX() - b.getX()) * ((long)lowest.getX() - b.getX())) +
                                                (((long)lowest.getY() - b.getY()) * ((long)lowest.getY() - b.getY())));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }
	
	private static Coordinate getLowestPoint(Collection<? extends Coordinate> points) {
		Iterator<? extends Coordinate> pointIterator = points.iterator();
		Coordinate lowest = pointIterator.next();

        while(pointIterator.hasNext()) {
        	Coordinate temp = pointIterator.next();

            if(temp.getY() < lowest.getY() || (temp.getY() == lowest.getY() && temp.getX() < lowest.getX())) {
                lowest = temp;
            }
        }

        return lowest;
    }
}
