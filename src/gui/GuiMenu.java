package gui;

import java.util.ArrayList;

import controller.InputHandler;

import view.Screen;
import view.Sprite;

public class GuiMenu extends GuiComponent {
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private Sprite background;
	private int pointer;
	private Button selected;
	
	public GuiMenu(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public GuiMenu() {
		super();
	}
	
	public void setBackground(Sprite sprite) {
		this.background = sprite;
	}
	
	@Override
	public void tick(InputHandler input) {
		super.tick(input);
		
		while (input.up.next()) if (input.up.isClicked()) selectionUp();
		while (input.down.next()) if (input.down.isClicked()) selectionDown();
		
		Button oldSelected = selected;
		selected = buttons.get(pointer);
		if (oldSelected == null) {
			selected.hover();
		} else if (!oldSelected.equals(selected)) {
			oldSelected.unHover();
			selected.hover();
		}
		
		while (input.enter.next()) {
			if (input.enter.isClicked())
				buttons.get(pointer).press();
		}
		
		for (Button button : buttons) {
			button.tick();
		}
	}
	
	private void selectionDown() {
		pointer++;
		if (pointer >= buttons.size())
			pointer = 0;
	}
	
	private void selectionUp() {
		pointer--;
		if (pointer < 0) {
			pointer = buttons.size()-1;
		}
	}
	
	@Override
	public void render(Screen screen) {
		if (this.background != null)
			screen.render(0, 0, getHeight(), getWidth(), background);
		
		for (Button button : buttons) {
			button.render(screen);
		}
	}

	protected int getPointer() {
		return pointer;
	}
	
	
	protected void addButton(Button button) {
		buttons.add(button);
	}
}
