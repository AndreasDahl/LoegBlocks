package view.gui;

import view.Art;
import view.GameFrame;

class OptionsMenu extends GuiMenu {

	OptionsMenu() {
		super();

        setBackgroundColor(Art.BACKGROUND);

        Button backButton = new Button(200, 50, Art.BUTTON, "back");
        backButton.addButtonListener(new BackPressedListener());
        this.addButton(backButton, getWidth()/2 - backButton.getWidth()/2, getHeight()-backButton.getHeight()-10);
    }

    private class BackPressedListener implements ButtonListener {
        @Override
        public void buttonPressed() {
            GameFrame.getInstance().setComponent(new MainMenu());
        }
    }
}
