package microsoft.paint.project.coordinate.processor;

import java.util.List;

import microsoft.paint.project.component.ColourCoordinate;

public class GrahamScanResult {
	private GrahamScanResultType resultType;
	private List<ColourCoordinate> results;
	
	public GrahamScanResultType getResultType() {
		return this.resultType;
	}
	
	public void setResultType(GrahamScanResultType type) {
		this.resultType = type;
	}

	public List<ColourCoordinate> getResults() {
		return results;
	}

	public void setResults(List<ColourCoordinate> results) {
		this.results = results;
	}
}
