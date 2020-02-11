package microsoft.paint.project.image.processor;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import microsoft.paint.project.component.Colour;
import microsoft.paint.project.component.ColourCoordinate;
import microsoft.paint.project.screen.MSPaintScreen;

// TODO: Rename and/or move methods to another class
public class Helper {
	public static Set<ColourCoordinate> GetUniqueColoursFromImage(BufferedImage image) {
		// Get all colours from the image
		Set<ColourCoordinate> colourCoords = new HashSet<ColourCoordinate>();
		for(int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				ColourCoordinate colourCoordinate = new ColourCoordinate(ImageProcessor.GetColourFromImage(image, x, y), x, y);
				
				colourCoords.add(colourCoordinate);
			}
		}
		
		System.out.println("Number of colours: " + colourCoords.stream().map(e -> e.getColour()).distinct().count());
		
		return colourCoords;
	}
	
	public static void ReplaceColoursWithOnesFromPallet(Collection<ColourCoordinate> colourCoords) {
		// Replace colours with ones from our default palette where possible
		for (ColourCoordinate colourCoord : colourCoords) {
			ColourCoordinate fromPallet = MSPaintScreen.getColourFromPallet(colourCoord.getColour());
			
			if (fromPallet != null) {
				colourCoord.setColour(fromPallet.getColour());
			}
		}
		System.out.println("Number of colours after replacing with default palette: " + colourCoords.stream().map(e -> e.getColour()).distinct().count());
	}

	public static void SimplifyColourCoordinates(Collection<ColourCoordinate> colourCoords) {
		colourCoords.forEach(e -> replaceColourWithSimilair(e, colourCoords));
	}
	
	public static Map<Colour, Integer> GetColoursWithOccurances(Collection<ColourCoordinate> colourCoords) {
		List<Colour> uniqueColours = colourCoords.stream().map(e -> e.getColour()).distinct().collect(Collectors.toList());
		Map<Colour, Integer> coloursWithCount = new HashMap<Colour, Integer>();
		for (Colour colourCoord : uniqueColours) {
			coloursWithCount.put(colourCoord, Integer.valueOf(String.valueOf(colourCoords.stream().filter(e -> e.getColour().equals(colourCoord)).count())));
		}
		
		return coloursWithCount.entrySet().stream()
			    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	private static void replaceColourWithSimilair(ColourCoordinate colourCoordinate, Collection<ColourCoordinate> allCoords) {
		Colour colourToReplaceWith = findSimilair(colourCoordinate, allCoords);
		
		if (colourToReplaceWith != null) {
			colourCoordinate.setColour(colourToReplaceWith);
		}
	}
	
	private static Colour findSimilair(ColourCoordinate colourCoord, Collection<ColourCoordinate> colourCoords) {
		for (ColourCoordinate otherColourCoord : colourCoords) {
			if (colourCoord != otherColourCoord && otherColourCoord.getColour().similairTo(colourCoord.getColour())) {
				return otherColourCoord.getColour();
			}
		}
		
		return null;
	}
}
