package gui;

import view.Art;
import view.GameFrame;
import view.Screen;
import gui.ButtonListener;

public class HighscoresMenu extends GuiMenu {
	private Button backButton;
	private HighscoreView highscoreView;
	
	public HighscoresMenu() {
		super();
		backButton = new Button(getWidth()/2-100, getHeight()-75, 200, 50, Art.BUTTON, "Back");
		backButton.addButtonListener(new BackListener());
		addButton(backButton);
		
		highscoreView = new HighscoreView(50, 50, getWidth()-100, getHeight()-200);
	}
	
	class BackListener implements ButtonListener {
		@Override
		public void buttonPressed() {
			GameFrame.getInstance().gotoMenu(new MainMenu());
		}
	}
	
	@Override
	public void render(Screen screen) {
		screen.renderBlank(getX(), getY(), getX()+getWidth(), getY()+getHeight(), 0xff007f7f);
		
		highscoreView.render(screen);
		
		super.render(screen);
	}
}
