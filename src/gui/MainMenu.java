package gui;

import view.Art;
import view.GameFrame;
import view.Screen;
import gui.ButtonListener;

public class MainMenu extends GuiMenu {
	private Button startGame;
	
	public MainMenu() {
		super();
		startGame = new Button(getWidth()/2-100, 100, 200, 100, Art.BUTTON, "New Game");
		startGame.addButtonListener(new StartGameListener());
		addButton(startGame);
		Button highscores = new Button(getWidth()/2-150, 200, 300, 100, Art.BUTTON, "Highscores");
		highscores.addButtonListener(new HighscoreListener()); 
		addButton(highscores);
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
