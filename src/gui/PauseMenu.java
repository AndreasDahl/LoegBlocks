package gui;

import view.GameFrame;
import view.Screen;
import controller.InputHandler;

public class PauseMenu {
	private int width;
	private int height;
	private int xPos;
	private int yPos;
	private Option[] options = {Option.CONTINUE, Option.OPTIONS, Option.RESTART};
	private int selection;
	
	private enum Option {
		CONTINUE, RESTART, OPTIONS
	}
	
	public PauseMenu() {
		width = 400;
		height = 400;
		xPos = 100;
		yPos = 100;
	}
	
	public void selectionDown() {
		selection++;
		if (selection >= options.length)
			selection = 0;
	}
	
	public void selectionUp() {
		selection--;
		if (selection < 0)
			selection = options.length-1;
	}
	
	public void performAction() {
		if (options[selection] == Option.RESTART) {
			//GameFrame.getInstance().getBoard().restart();
		}
		//GameFrame.getInstance().togglePause();
	}
	
	public void tick() {
		InputHandler input = GameFrame.getInstance().getInputHandler();
		if (input.rotate.isClicked()) selectionUp();
		if (input.down.isClicked()) selectionDown();
		if (input.enter.isClicked()) performAction();
	}
	
	public void render(Screen screen) {
		screen.renderBlank(xPos, yPos, width, height, 0);
		screen.renderBlank(xPos, yPos+100*selection, 100, 100, 0xffffff);
	}
}
