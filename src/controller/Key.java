package controller;

import java.util.LinkedList;

public class Key {
	public LinkedList<Boolean> nextState;
	public boolean wasDown;
	public boolean isDown;

	public Key(InputHandler handler) {
		nextState = new LinkedList<Boolean>();
		clear();
		handler.addToKeys(this);
	}

	public void toggle(boolean press) {
		if (nextState.isEmpty() || press != nextState.getLast())
			nextState.add(press);
		else {
			nextState.add(!press);
			nextState.add(press);
		}
	}

	public void tick() {
		wasDown = isDown;
		boolean state = nextState.removeFirst();
		if (state)
			isDown = state;
		else if (isDown && !state)
			isDown = false;
	}

	public boolean hasNext() {
		return !nextState.isEmpty();
	}

	public boolean next() {
		if (hasNext()) {
			tick();
			return true;
		}
		return false;
	}

	public boolean isClicked() {
		return !wasDown && isDown;
	}

	public boolean isPressed() {
		return isDown;
	}

	public boolean wasReleased() {
		return wasDown && !isDown;
	}

	public void release() {
		nextState.add(false);
	}

	public void clear() {
		nextState.clear();
		wasDown = false;
		isDown = false;
	}
}

/*public class Key {
	private int presses, absorbs;
	private boolean pressed, clicked;

	public Key(InputHandler handler) {
		pressed = false;
		clicked = false;
		handler.addToKeys(this);
	}

	public void toggle(boolean press) {
		if (pressed != press)
			pressed = press;
		if (press)
			presses++;
	}

	public void tick() {
		if (absorbs < presses) {
			absorbs++;
			clicked = true;
		}
		else
			clicked = false;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isClicked() {
		return clicked;
	}
}*/


