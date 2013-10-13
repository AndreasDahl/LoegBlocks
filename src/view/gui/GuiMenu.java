package view.gui;

import java.util.ArrayList;

import controller.InputHandler;

import controller.Key;
import view.GameFrame;
import view.Screen;
import view.Sprite;

public class GuiMenu extends GuiComponent implements InputHandler.OnToggleListener {
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private Sprite background;
	private int pointer;
	private Button selected;
	
	public GuiMenu(int width, int height) {
		super(width, height);

		pointer = 0;
	}
	
	public GuiMenu() {
		this(GameFrame.WIDTH, GameFrame.HEIGHT);
	}
	
	public void setBackground(Sprite sprite) {
		this.background = sprite;
	}

    @Override
    public void onToggle(Key key, boolean pressed) {
        InputHandler input = InputHandler.getInstance();

        if (pressed) {
            if (key == input.up)
                selectionUp();
            if (key == input.down)
                selectionDown();
            if (key == input.enter)
                buttons.get(pointer).press();
        }
    }

    @Override
	public void tick() {
		super.tick();
		
		Button oldSelected = selected;
		selected = buttons.get(pointer);
		if (oldSelected == null) {
			selected.hover();
		} else if (!oldSelected.equals(selected)) {
			oldSelected.unHover();
			selected.hover();
		}
		
		for (Button button : buttons) {
			button.tick();
		}
	}

    private void selectionUp() {
        pointer--;
        if (pointer < 0) {
            pointer = buttons.size()-1;
        }
    }
	
	private void selectionDown() {
		pointer++;
		if (pointer >= buttons.size())
			pointer = 0;
	}
	
	@Override
	public void render(Screen screen) {
		if (this.background != null)
			screen.render(0, 0, getHeight(), getWidth(), background);

        super.render(screen);
	}

	protected int getPointer() {
		return pointer;
	}
	
	protected void addButton(Button button, int x, int y) {
        addChild(button, x, y);
		buttons.add(button);
	}

    @Override
    public void activate() {
        super.activate();
        InputHandler.getInstance().addOnToggleListener(this);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        InputHandler.getInstance().removeOnToggleListener(this);
    }
}
