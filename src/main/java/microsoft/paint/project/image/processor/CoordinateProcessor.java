package microsoft.paint.project.image.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	
	public static List<ColourCoordinate> removeMiddleCoordinates(Collection<ColourCoordinate> colourCoords) {
		List<ColourCoordinate> results = new ArrayList<>();
		List<ColourCoordinate> removedCoords = new ArrayList<>();
		
		for (ColourCoordinate coord : colourCoords) {
			if (results.contains(coord) || removedCoords.contains(coord)) {
				continue;
			}
			
			ColourCoordinate leftMostCoord = findLeftMostCoord(coord, colourCoords);
			ColourCoordinate rightMostCoord = findRightMostCoord(coord, colourCoords);
			
			if (leftMostCoord != coord || rightMostCoord != coord) {
				// get the length of the line
				final int lengthOfLine = rightMostCoord.getX() - leftMostCoord.getX();

				results.add(leftMostCoord);
				results.add(rightMostCoord);
				
				if (lengthOfLine > 2) {
					// delete middle entries
					removedCoords.addAll(getCoordinateBetween(colourCoords, leftMostCoord, rightMostCoord));
				}
			} else {
				ColourCoordinate topMostCoord = findTopMostCoord(coord, colourCoords);
				ColourCoordinate bottomMostCoord = findBottomMostCoord(coord, colourCoords);
				
				if (topMostCoord != coord || bottomMostCoord != coord) {
					// get the length of the line
					final int lengthOfLine = bottomMostCoord.getY() - topMostCoord.getY();
	
					results.add(topMostCoord);
					results.add(bottomMostCoord);
					
					if (lengthOfLine > 2) {
						// delete middle entries
						removedCoords.addAll(getCoordinateBetween(colourCoords, topMostCoord, bottomMostCoord));
					}
				} else {
					// single pixel length
					results.add(coord);
				}
			}
		}
		
		return results;
	}
	
	private static List<ColourCoordinate> getCoordinateBetween(Collection<ColourCoordinate> coordinatesList, ColourCoordinate coord1, ColourCoordinate coord2) {
		List<ColourCoordinate> results = new ArrayList<>();
		
		// get the length of the line
		final int lengthOfLineX = Math.abs(coord1.getX() - coord2.getX());
		final int lengthOfLineY = Math.abs(coord1.getY() - coord2.getY());

		if (lengthOfLineX > 2) {
			if (lengthOfLineY > 2) {
				throw new RuntimeException("Two directions not supported");
			}
			
			// delete middle entries
			for (int i = coord1.getX() + 1; i < coord2.getX(); i++) {
				final int x = i;
				ColourCoordinate toRemove = coordinatesList.stream().filter(e -> e.getX() == x && e.getY() == coord1.getY()).findFirst().get();
				
				results.add(toRemove);
			}
		} else if (lengthOfLineY > 2) {
			// delete middle entries
			for (int i = coord1.getY() + 1; i < coord2.getY(); i++) {
				final int y = i;
				ColourCoordinate toRemove = coordinatesList.stream().filter(e -> e.getY() == y && e.getX() == coord1.getX()).findFirst().get();
				
				results.add(toRemove);
			}
		}
		
		return results;
	}
	
	public static List<List<ColourCoordinate>> getAllSides(Collection<ColourCoordinate> colourCoords) {
		return Arrays.asList(getTopSide(colourCoords), getBottomSide(colourCoords), getLeftSide(colourCoords), getRightSide(colourCoords));
	}
	
	public static List<ColourCoordinate> getTopSide(Collection<ColourCoordinate> colourCoords) {
		Set<ColourCoordinate> results = new HashSet<>();
		final int leftMostX = colourCoords.stream().mapToInt(v -> v.getX()).min().getAsInt();
		final int rightMostX = colourCoords.stream().mapToInt(v -> v.getX()).max().getAsInt();
		
		for (int i = leftMostX; i <= rightMostX; i++) {
			final int x = i;
			ColourCoordinate result = colourCoords.stream().filter(e -> e.getX() == x).min(Comparator.comparing(ColourCoordinate::getY)).orElse(null);

			if (result != null) {
				results.add(result);
			}
		}
		
		return results.stream().sorted((a1, a2) -> Integer.compare(a1.getX(), a2.getX())).collect(Collectors.toList());
	}
	
	public static List<ColourCoordinate> getBottomSide(Collection<ColourCoordinate> colourCoords) {
		Set<ColourCoordinate> results = new HashSet<>();
		final int leftMostX = colourCoords.stream().mapToInt(v -> v.getX()).min().getAsInt();
		final int rightMostX = colourCoords.stream().mapToInt(v -> v.getX()).max().getAsInt();
		
		for (int i = leftMostX; i <= rightMostX; i++) {
			final int x = i;
			ColourCoordinate result = colourCoords.stream().filter(e -> e.getX() == x).max(Comparator.comparing(ColourCoordinate::getY)).orElse(null);

			if (result != null) {
				results.add(result);
			}
		}

		return results.stream().sorted((a1, a2) -> Integer.compare(a1.getX(), a2.getX())).collect(Collectors.toList());
	}
	
	public static List<ColourCoordinate> getLeftSide(Collection<ColourCoordinate> colourCoords) {
		Set<ColourCoordinate> results = new HashSet<>();
		final int topMostY = colourCoords.stream().mapToInt(v -> v.getY()).min().getAsInt();
		final int bottomMostY = colourCoords.stream().mapToInt(v -> v.getY()).max().getAsInt();
		
		for (int i = topMostY; i <= bottomMostY; i++) {
			final int y = i;
			ColourCoordinate result = colourCoords.stream().filter(e -> e.getY() == y).min(Comparator.comparing(ColourCoordinate::getX)).orElse(null);

			if (result != null) {
				results.add(result);
			}
		}

		return results.stream().sorted((a1, a2) -> Integer.compare(a1.getY(), a2.getY())).collect(Collectors.toList());
	}
	
	public static List<ColourCoordinate> getRightSide(Collection<ColourCoordinate> colourCoords) {
		Set<ColourCoordinate> results = new HashSet<>();
		final int topMostY = colourCoords.stream().mapToInt(v -> v.getY()).min().getAsInt();
		final int bottomMostY = colourCoords.stream().mapToInt(v -> v.getY()).max().getAsInt();
		
		for (int i = topMostY; i <= bottomMostY; i++) {
			final int y = i;
			ColourCoordinate result = colourCoords.stream().filter(e -> e.getY() == y).max(Comparator.comparing(ColourCoordinate::getX)).orElse(null);

			if (result != null) {
				results.add(result);
			}
		}

		return results.stream().sorted((a1, a2) -> Integer.compare(a1.getY(), a2.getY())).collect(Collectors.toList());
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
	
	private static ColourCoordinate findLeftMostCoord(ColourCoordinate coord, Collection<ColourCoordinate> list) {
		return findCoordInDirection(-1, 0, coord, list);
	}
	
	private static ColourCoordinate findRightMostCoord(ColourCoordinate coord, Collection<ColourCoordinate> list) {
		return findCoordInDirection(1, 0, coord, list);
	}
	
	private static ColourCoordinate findTopMostCoord(ColourCoordinate coord, Collection<ColourCoordinate> list) {
		return findCoordInDirection(0, -1, coord, list);
	}
	
	private static ColourCoordinate findBottomMostCoord(ColourCoordinate coord, Collection<ColourCoordinate> list) {
		return findCoordInDirection(0, 1, coord, list);
	}
	
	private static ColourCoordinate findCoordInDirection(int directionX, int directionY, ColourCoordinate coord, Collection<ColourCoordinate> list) {
		ColourCoordinate foundItem = coord;
		ColourCoordinate result = null;
		
		int startX = coord.getX();
		int startY = coord.getY();
		int offsetX = 0;
		int offsetY = 0;
		
		do {
			final int findX = startX + offsetX;
			final int findY = startY + offsetY;
			result = list.stream().filter(e -> e.getY() == findY && e.getX() == findX).findAny().orElse(null);
			offsetX += directionX;
			offsetY += directionY;
			
			if (result != null) {
				foundItem = result;
			}
		} while (result != null);
		
		return foundItem;
	}
}
