package microsoft.paint.project.screen;

import java.awt.Robot;
import java.util.Arrays;
import java.util.List;

import microsoft.paint.project.component.ColourCoordinate;

public class MSPaintScreen {
	private static final List<ColourCoordinate> PALLET = Arrays.asList(
			new ColourCoordinate(0, 0, 0, 728, 57),
			new ColourCoordinate(127, 127, 127, 746, 57),
			new ColourCoordinate(136, 0, 21, 770, 59),
			new ColourCoordinate(237, 28, 36, 797, 59),
			new ColourCoordinate(255, 127, 39, 817, 59),
			new ColourCoordinate(255, 242, 0, 839, 60),
			new ColourCoordinate(34, 177, 76, 861, 60),
			new ColourCoordinate(0, 162, 232, 879, 60),
			new ColourCoordinate(63, 72, 204, 899, 60),
			new ColourCoordinate(245, 246, 247, 750, 136),
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
	
	public static boolean isMSPaintOpen(Robot robot) {
		for (ColourCoordinate colourCoord : PALLET) {
			if (!robot.getPixelColor(colourCoord.getX(), colourCoord.getY()).equals(colourCoord.getColour())) {
				return false;
			}
		}
		
		return true;
	}
}
