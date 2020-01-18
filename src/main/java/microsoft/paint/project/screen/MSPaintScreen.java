package microsoft.paint.project.screen;

import java.awt.Point;
import java.awt.Robot;
import java.util.List;
import java.util.stream.Collectors;

import microsoft.paint.project.component.Colour;
import microsoft.paint.project.component.ColourCoordinate;

import static microsoft.paint.project.screen.MSPaintScreenProperties.IMAGE_PROPERTIES_PROMPT;
import static microsoft.paint.project.screen.MSPaintScreenProperties.PALETTE;
import static microsoft.paint.project.screen.MSPaintScreenProperties.EDIT_COLOURS_BTN;
import static microsoft.paint.project.screen.MSPaintScreenProperties.EDIT_COLOURS_DIALOG;
import static microsoft.paint.project.screen.MSPaintScreenProperties.RED_GREEN_BLUE_EDIT_COLOURS_DIALOG;
import static microsoft.paint.project.screen.MSPaintScreenProperties.PAINT_CAN;
import static microsoft.paint.project.screen.MSPaintScreenProperties.DRAW_AREA;
import static microsoft.paint.project.screen.MSPaintScreenProperties.ZOOM_IN_BUTTON;

public class MSPaintScreen {
	public static boolean isMSPaintOpen(Robot robot) {
		return isOpen(robot, PALETTE);
	}
	
	public static boolean isImagePropertiesPromptOpen(Robot robot) {
		return isOpen(robot, IMAGE_PROPERTIES_PROMPT);
	}
	
	public static boolean isCustomColourPromptOpen(Robot robot) {
		return isOpen(robot, EDIT_COLOURS_DIALOG);
	}
	
	public static Point getDrawArea() {
		return DRAW_AREA;
	}
	
	public static Point getZoomInButton() {
		return ZOOM_IN_BUTTON;
	}
	
	public static Point getEditColoursButton() {
		return new Point(EDIT_COLOURS_BTN.get(0).getX(), EDIT_COLOURS_BTN.get(0).getY());
	}
	
	public static Point getPaintCanButton() {
		return new Point(PAINT_CAN.get(0).getX(), PAINT_CAN.get(0).getY());
	}
	
	public static List<Point> getRedGreenBlueColourPointsForCustomColourDialog() {
		return RED_GREEN_BLUE_EDIT_COLOURS_DIALOG.stream().map(e -> new Point(e.getX(), e.getY())).collect(Collectors.toList());
	}
	
	public static Colour getColourFromPallet(Colour colourToMatch) {
		for (ColourCoordinate colourCoord : PALETTE) {
			if (colourCoord.getColour().similairTo(colourToMatch)) {
				return colourCoord.getColour();
			}
		}
		
		return null;
	}
	
	private static boolean isOpen(Robot robot, List<ColourCoordinate> coordinates) {
		for (ColourCoordinate colourCoord : coordinates) {
			if (!new Colour(robot.getPixelColor(colourCoord.getX(), colourCoord.getY())).equals(colourCoord.getColour())) {
				System.out.println("expected: " + colourCoord.getColour() + " but was: " + new Colour(robot.getPixelColor(colourCoord.getX(), colourCoord.getY())));
				return false;
			}
		}
		
		return true;
	}
}
