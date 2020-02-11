package microsoft.paint.project.screen;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import microsoft.paint.project.component.Colour;
import microsoft.paint.project.component.ColourCoordinate;

public class MSPaintScreenProperties {
	private MSPaintScreenProperties() { }
	
	static final Point DRAW_AREA = new Point(4, 144);
	static final Point ZOOM_IN_BUTTON = new Point(2546, 1383);
	
	public static final Colour DEFAULT_COLOUR = new Colour(245, 246, 247);
	
	static final List<ColourCoordinate> PALETTE = Arrays.asList(
			new ColourCoordinate(0, 0, 0, 728, 57),
			new ColourCoordinate(127, 127, 127, 746, 57),
			new ColourCoordinate(136, 0, 21, 770, 59),
			new ColourCoordinate(237, 28, 36, 797, 59),
			new ColourCoordinate(255, 127, 39, 817, 59),
			new ColourCoordinate(255, 242, 0, 839, 60),
			new ColourCoordinate(34, 177, 76, 861, 60),
			new ColourCoordinate(0, 162, 232, 879, 60),
			new ColourCoordinate(63, 72, 204, 899, 60),
			new ColourCoordinate(DEFAULT_COLOUR, 750, 136),
			new ColourCoordinate(255, 255, 255, 729, 89),
			new ColourCoordinate(255, 255, 255, 729, 86),
			new ColourCoordinate(255, 255, 255, 729, 86),
			new ColourCoordinate(195, 195, 195, 753, 81),
			new ColourCoordinate(185, 122, 87, 772, 79),
			new ColourCoordinate(255, 174, 201, 793, 78),
			new ColourCoordinate(255, 201, 14, 813, 78),
			new ColourCoordinate(239, 228, 176, 835, 77),
			new ColourCoordinate(239, 228, 176, 839, 77),
			new ColourCoordinate(181, 230, 29, 856, 78),
			new ColourCoordinate(153, 217, 234, 886, 82),
			new ColourCoordinate(112, 146, 190, 904, 82)
			);
	
	static final List<ColourCoordinate> IMAGE_PROPERTIES_PROMPT = Arrays.asList(
			new ColourCoordinate(240, 240, 240, 1398, 715),
			new ColourCoordinate(225, 225, 225, 1329, 853)
			);
	// X: 232.0 Y: 70.0 java.awt.Color[r=134,g=130,b=117]
	static final List<ColourCoordinate> PENCIL = Arrays.asList(
			new ColourCoordinate(134, 130, 117, 232, 70)
			);
	
	static final List<ColourCoordinate> PAINT_CAN = Arrays.asList(
			new ColourCoordinate(232, 239, 247, 248, 65),
			new ColourCoordinate(237, 244, 247, 257, 71)
			);
	
	static final List<ColourCoordinate> EDIT_COLOURS_BTN = Arrays.asList(
			new ColourCoordinate(232, 239, 247, 959, 93),
			new ColourCoordinate(151, 34, 221, 967, 63),
			new ColourCoordinate(216, 213, 39, 951, 64)
			);
	
	static final List<ColourCoordinate> EDIT_COLOURS_DIALOG = Arrays.asList(
			new ColourCoordinate(147, 30, 225, 1421, 610),
			new ColourCoordinate(167, 225, 30, 1325, 611),
			new ColourCoordinate(240, 240, 240, 1275, 827)
			);
	
	// In order of Red, Green, Blue
	static final List<ColourCoordinate> RED_GREEN_BLUE_EDIT_COLOURS_DIALOG = Arrays.asList(
			new ColourCoordinate(255, 255, 255, 1469, 773),
			new ColourCoordinate(255, 255, 255, 1478, 795),
			new ColourCoordinate(255, 255, 255, 1475, 817)
			);
}
