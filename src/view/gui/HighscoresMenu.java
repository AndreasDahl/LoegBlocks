package view.gui;

import view.Art;
import view.GameFrame;

class HighscoresMenu extends GuiMenu {

	HighscoresMenu() {
		super();

        this.setBackgroundColor(Art.BACKGROUND);

		Button backButton = new Button(200, 50, Art.BUTTON, "Back");
		backButton.addButtonListener(() -> GameFrame.getInstance().setComponent(new MainMenu()));
		addButton(backButton, getWidth()/2- backButton.getWidth()/2, getHeight()- backButton.getHeight()-10);

		HighscoreView highscoreView = new HighscoreView(getWidth() - 100, getHeight() - 200);
        highscoreView.setBackgroundColor(0xffffffff);
        addChild(highscoreView, 50, 50);
	}
}
