package microsoft.paint.project.coordinate.processor;

import java.util.Comparator;

/**
 * Puts valid points first then sorts by smallest -> largest
 */
public class GrahamScanComparator implements Comparator<GrahamScanResult> {
	@Override
	public int compare(GrahamScanResult o2, GrahamScanResult o1) {
		int resultTypeComparison = Integer.compare(getResultTypeValue(o1), getResultTypeValue(o2));
		
		if (resultTypeComparison != 0) {
			return resultTypeComparison;
		}
		
		return Integer.compare(o2.getResults().size(), o1.getResults().size());
	}
	
	private int getResultTypeValue(GrahamScanResult o) {
		switch (o.getResultType()) {
			case OK:
				return 3;
			case LESS_THAN_3_POINTS:
				return 2;
			case STRAIGHT_LINE:
				return 1;
			default:
				throw new RuntimeException("Unknwon type: " + o.getResultType());
		}
	}
}
