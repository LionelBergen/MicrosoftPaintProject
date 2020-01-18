package microsoft.paint.project.image.processor;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import microsoft.paint.project.component.ColourCoordinate;

// TODO: rename this class
public class CoordinateProcessor {
	public static boolean isTouching(ColourCoordinate a, ColourCoordinate b) {
		return (a.getX() == b.getX() && Math.abs(a.getY() - b.getY()) <= 1) ||
			   (a.getY() == b.getY() && Math.abs(a.getX() - b.getX()) <= 1);
	}
	
	public static Set<Set<ColourCoordinate>> getTouchingCoordinates(Collection<ColourCoordinate> colourCoords) {
		Set<Set<ColourCoordinate>> results = new HashSet<>();
		
		if (colourCoords.isEmpty()) {
			return results;
		}
		
		final int numberOfCoordsToProcess = colourCoords.size();
		
		Set<ColourCoordinate> processedCoords = getNextTouchingCoordinates(colourCoords);
		results.add(processedCoords);
		
		while (results.stream().mapToInt(i -> i.size()).sum() != numberOfCoordsToProcess) {
			Set<ColourCoordinate> coordsProcessed = colourCoords.stream().filter(e -> !processedCoords.contains(e)).collect(Collectors.toSet());
			results.add(coordsProcessed);
		}
		
		return results;
	}
	
	private static Set<ColourCoordinate> getNextTouchingCoordinates(Collection<ColourCoordinate> colourCoords) {
		// start with the first coord
		ColourCoordinate firstCoord = colourCoords.iterator().next();
		
		Set<ColourCoordinate> unprocessedCoords = new HashSet<>();
		Set<ColourCoordinate> processedCoords = new HashSet<>();
		unprocessedCoords.add(firstCoord);
		
		while (!unprocessedCoords.isEmpty()) {
			ColourCoordinate coordToProcess = unprocessedCoords.iterator().next();
			List<ColourCoordinate> touchingCoords = colourCoords.stream().filter(e -> e != coordToProcess && isTouching(coordToProcess, e)).collect(Collectors.toList());
			
			unprocessedCoords.addAll(touchingCoords);
			
			processedCoords.add(coordToProcess);
			unprocessedCoords = unprocessedCoords.stream().filter(e -> !processedCoords.contains(e)).collect(Collectors.toSet());
		}
		
		return processedCoords;
	}
	
	public static Set<ColourCoordinate> getTopSide(Collection<ColourCoordinate> colourCoords) {
		Set<ColourCoordinate> results = new HashSet<>();
		final int leftMostX = colourCoords.stream().mapToInt(v -> v.getX()).min().getAsInt();
		final int rightMostX = colourCoords.stream().mapToInt(v -> v.getX()).max().getAsInt();
		
		for (int i = leftMostX; i <= rightMostX; i++) {
			final int x = i;
			ColourCoordinate result = colourCoords.stream().filter(e -> e.getX() == x).min(Comparator.comparing(ColourCoordinate::getY)).get();

			results.add(result);
		}
		
		return results;
	}
}
