package view.gui;

import view.Art;
import view.GameFrame;

class HighscoresMenu extends GuiMenu {
	private final Button backButton;
	private final HighscoreView highscoreView;
	
	HighscoresMenu() {
		super();

        this.setBackgroundColor(Art.BACKGROUND);

		backButton = new Button(200, 50, Art.BUTTON, "Back");
		backButton.addButtonListener(new BackListener());
		addButton(backButton, getWidth()/2-backButton.getWidth()/2, getHeight()-backButton.getHeight()-10);
		
		highscoreView = new HighscoreView(getWidth()-100, getHeight()-200);
        highscoreView.setBackgroundColor(0xffffffff);
        addChild(highscoreView, 50, 50);
	}
	
	class BackListener implements ButtonListener {
		@Override
		public void buttonPressed() {
			GameFrame.getInstance().setComponent(new MainMenu());
		}
	}
}
