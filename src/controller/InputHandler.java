package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class InputHandler implements KeyListener {
	private ArrayList<Key> keys = new ArrayList<Key>();
	public Key left, right, up, down, softDrop, hardDrop, rotate, rotateCounter, rotate180, menu, hold, allLeft, allRight, enter;
	
	public InputHandler() {
		left = new Key(this);
		right = new Key(this);
		up =  new Key(this);
		down = new Key(this);
		softDrop = new Key(this);
		hardDrop = new Key(this);
		rotate = new Key(this);
		rotateCounter = new Key(this);
		rotate180 = new Key(this);
		menu = new Key(this);
		enter = new Key(this);
		hold = new Key(this);
		allLeft = new Key(this);
		allRight =  new Key(this);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		toggle(e, true);
	}
	
	/*public void tick() {
		for (Key key : keys) {
			key.tick();
		}
	}*/
	
	@Override
	public void keyReleased(KeyEvent e) {
		toggle(e, false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void toggle(KeyEvent e, boolean pressed) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD2) down.toggle(pressed);
		if (key == KeyEvent.VK_END || key == KeyEvent.VK_NUMPAD1 || (key == KeyEvent.VK_LEFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_STANDARD)) left.toggle(pressed);
		if (key == KeyEvent.VK_PAGE_DOWN || key == KeyEvent.VK_NUMPAD3 || (key == KeyEvent.VK_RIGHT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_STANDARD))	right.toggle(pressed);
		if (key == KeyEvent.VK_UP)	up.toggle(pressed);
		if (key == KeyEvent.VK_SPACE) hardDrop.toggle(pressed);
		if (key == KeyEvent.VK_Z) rotateCounter.toggle(pressed);
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_NUMPAD5 || key == 12) rotate.toggle(pressed);
		if (key == KeyEvent.VK_SHIFT) hold.toggle(pressed);
		if (key == KeyEvent.VK_NUMPAD4 || (key == KeyEvent.VK_LEFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD)) allLeft.toggle(pressed);
		if (key == KeyEvent.VK_NUMPAD6 || (key == KeyEvent.VK_RIGHT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD)) allRight.toggle(pressed);
		if (key == KeyEvent.VK_NUMPAD2 || (key == KeyEvent.VK_DOWN && e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD)) softDrop.toggle(pressed);
		if (key == KeyEvent.VK_X) rotate180.toggle(pressed);
		if (key == KeyEvent.VK_ESCAPE) menu.toggle(pressed);
		if (key == KeyEvent.VK_ENTER) enter.toggle(pressed);
	}
	
	protected void addToKeys(Key key) {
		keys.add(key);
	}

	public void clearAll() {
		for (Key key : keys) {
			key.clear();
		}
	}
}
