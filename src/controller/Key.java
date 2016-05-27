package controller;

public class Key {
	private boolean wasDown;
	private boolean isDown;

	Key(InputHandler handler) {
		clear();
		handler.addToKeys(this);
	}

	synchronized void toggle(boolean pressed) {
		wasDown = isDown;
		isDown = pressed;
        if (isDown && !wasDown) {
            InputHandler.getInstance().notifyListeners(this, true);
        } else if (wasDown && !isDown) {
            InputHandler.getInstance().notifyListeners(this, false);
        }
	}

	void clear() {
		wasDown = false;
		isDown = false;
	}


}


