package microsoft.paint.project;

import java.awt.Robot;
import java.util.concurrent.TimeUnit;

import microsoft.paint.project.screen.MSPaintScreen;

public class MicrosoftPaintProject {
	public static void main(String[] args) throws Exception {
		Process process = new ProcessBuilder("MSPaint").start();
		
		final Robot robot = new Robot();
		
		while (!MSPaintScreen.isMSPaintOpen(robot)) {
			TimeUnit.SECONDS.sleep(1);
		}
		
		// MSPaint is now open :-)
		
	}
}
