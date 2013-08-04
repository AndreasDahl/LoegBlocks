package gui;

import view.Art;
import view.GameFrame;
import view.Screen;
import gui.ButtonListener;

public class HighscoresMenu extends GuiMenu {
	
	public HighscoresMenu() {
		super();
		Button backButton = new Button(getWidth()/2-150, GameFrame.HEIGHT-100, 300, 100, Art.BUTTON, "Back");
		backButton.addButtonListener(new BackListener());
		addButton(backButton);
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
		
		super.render(screen);
	}
}
