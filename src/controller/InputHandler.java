package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class InputHandler implements KeyListener {
    private static InputHandler instance;

    private HashSet<OnToggleListener> onToggleListeners;
    private LinkedList<Toggle> toggles;
    private ArrayList<Key> keys = new ArrayList<Key>();
	public Key left, right, up, down, softDrop, hardDrop, rotate, rotateCounter, rotate180, menu, hold, allLeft, allRight, enter, back;
	
	private InputHandler() {
        toggles = new LinkedList<Toggle>();
        onToggleListeners = new HashSet<OnToggleListener>();

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
        back = new Key(this);
	}

    public static InputHandler getInstance() {
        if (instance == null)
            instance = new InputHandler();
        return instance;
    }

    public synchronized void tick() {
        while (!toggles.isEmpty()) {
            Toggle toggle = toggles.removeFirst();
            toggle(toggle.keyEvent, toggle.down);
        }
    }

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		store(e, true);
	}
	
	@Override
	public synchronized void keyReleased(KeyEvent e) {
		store(e, false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

    private synchronized void store(KeyEvent e, boolean pressed) {
        toggles.add(new Toggle(e, pressed));
    }
	
	private void toggle(KeyEvent e, boolean pressed) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD2) down.toggle(pressed);
		if (key == KeyEvent.VK_END || key == KeyEvent.VK_NUMPAD1 || (key == KeyEvent.VK_LEFT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_STANDARD)) left.toggle(pressed);
		if (key == KeyEvent.VK_PAGE_DOWN || key == KeyEvent.VK_NUMPAD3 || (key == KeyEvent.VK_RIGHT && e.getKeyLocation() == KeyEvent.KEY_LOCATION_STANDARD))	right.toggle(pressed);
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_NUMPAD5) up.toggle(pressed);
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
         if (key == KeyEvent.VK_ESCAPE) back.toggle(pressed);
	}
	
	protected void addToKeys(Key key) {
		keys.add(key);
	}

	public synchronized void clearAll() {
		for (Key key : keys) {
			key.clear();
		}
	}

    public synchronized void notifyListeners(Key key, boolean pressed) {
        HashSet<OnToggleListener> tmpList = (HashSet<OnToggleListener>) onToggleListeners.clone();
        for (OnToggleListener listener : tmpList) {
            listener.onToggle(key, pressed);
        }
    }

    public synchronized void addOnToggleListener(OnToggleListener listener) {
        onToggleListeners.add(listener);
    }

    public synchronized boolean removeOnToggleListener(OnToggleListener listener) {
        boolean test = onToggleListeners.remove(listener);
        return test;
    }

    public interface OnToggleListener {
        public void onToggle(Key key, boolean pressed);
    }

    private class Toggle {
        public KeyEvent keyEvent;
        public boolean down;

        Toggle(KeyEvent keyEvent, boolean down) {
            this.keyEvent = keyEvent;
            this.down = down;
        }
    }
}
