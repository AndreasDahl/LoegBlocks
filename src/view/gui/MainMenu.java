package view.gui;

import view.Art;
import view.GameFrame;
import view.Screen;

public class MainMenu extends GuiMenu {
	private Button startGame;
	
	public MainMenu() {
		super();

		startGame = new Button(200, 100, Art.BUTTON, "New Game");
		startGame.addButtonListener(new StartGameListener());
		addButton(startGame, getWidth()/2-100, 100);

		Button highScores = new Button(300, 100, Art.BUTTON, "Highscores");
		highScores.addButtonListener(new HighscoreListener());
		addButton(highScores, getWidth()/2-150, 200);
	}
	
	class StartGameListener implements ButtonListener {
		@Override
		public void buttonPressed() {
			GameFrame.getInstance().startGame();
		}
	}
	
	class HighscoreListener implements ButtonListener {
		@Override
		public void buttonPressed() {
			GameFrame.getInstance().setComponent(new HighscoresMenu());
		}
	}
	
	@Override
	public void render(Screen screen) {
		screen.renderBlank(getX(), getY(), getX()+getWidth(), getY()+getHeight(), 0xff007f7f);
		
		super.render(screen);
	}
}
