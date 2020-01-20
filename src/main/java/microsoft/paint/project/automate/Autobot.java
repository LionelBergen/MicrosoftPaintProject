package microsoft.paint.project.automate;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for a java.awt.Robot class, with added features
 * @author Lionel Bergen
 */
public class Autobot extends Robot {
	// Used for releasing a key after being pressed
	private static final int MS_DELAY = 50;
	
	// parallel lists to map a KeyEvent to a Char
	private static final List<Character> CHARACTERS = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
	private static final List<Integer> KEY_EVENTS = Arrays.asList(KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_0);
	
	public Autobot() throws AWTException {
		super();
	}
	
	// TODO: Delete once project is finished. I use this a lot for figuring out coordinates. (screen shotting and checking on paint is not accurate)
	public void printCurrentMouseInfo() {
		Point point = MouseInfo.getPointerInfo().getLocation();
		
		System.out.println("X: " + point.getX() + " Y: " + point.getY() + " " + getPixelColor((int) point.getX(), (int) point.getY())); 
	}
	
	public void enterKeys(String keysToType) {
		for (int i = 0; i < keysToType.length(); i++) {
			char character = keysToType.charAt(i);
			
			if (!CHARACTERS.contains(character)) {
				throw new RuntimeException("Character not found: " + character);
			}
			
			int keyToType = KEY_EVENTS.get(CHARACTERS.indexOf(character));
			pressAndReleaseKey(keyToType);
			delay(MS_DELAY);
		}
	}
	
	public void enterKeys(int keysToType) {
		enterKeys(String.valueOf(keysToType));
	}
	
	public void pressCtrlKeyWithAnotherKey(int keyCode) {
		keyPress(KeyEvent.VK_CONTROL);
		delay(MS_DELAY);
		keyPress(keyCode);
		delay(MS_DELAY);
		keyRelease(keyCode);
		delay(MS_DELAY);
		keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressAndReleaseKey(int keyCode) {
		keyPress(keyCode);
		delay(MS_DELAY);
		keyRelease(keyCode);
	}
	
	public void mouseMove(Point point) {
		mouseMove((int) point.getX(), (int) point.getY());
	}
	
	public void mouseClick(int x, int y) {
		mouseMove(x, y);
		delay(50);
		mouseClick();
	}
	
	public void mouseClick(Point point) {
		mouseClick((int) point.getX(), (int) point.getY());
	}
	
	public void mouseDoubleClick(Point point) {
		mouseClick(point);
		mouseClick(point);
	}
	
	public void mouseClick() {
		mousePress(InputEvent.BUTTON1_DOWN_MASK);
		delay(50);
		mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		delay(50);
	}
}
