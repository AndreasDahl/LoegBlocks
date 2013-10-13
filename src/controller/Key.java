package controller;

public class Key {
	public boolean wasDown;
	public boolean isDown;

	public Key(InputHandler handler) {
		clear();
		handler.addToKeys(this);
	}

	public synchronized void toggle(boolean pressed) {
		wasDown = isDown;
		isDown = pressed;
        if (isDown && !wasDown) {
            InputHandler.getInstance().notifyListeners(this, true);
        } else if (wasDown && !isDown) {
            InputHandler.getInstance().notifyListeners(this, false);
        }
	}

	public void clear() {
		wasDown = false;
		isDown = false;
	}


}


