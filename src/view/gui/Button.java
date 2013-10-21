package view.gui;

import java.util.ArrayList;
import java.util.List;

import view.Art;
import view.Screen;
import view.Sprite;

public class Button extends GuiComponent {

	private List<ButtonListener> listeners = new ArrayList<ButtonListener>();	
	
	private Sprite sprite;
	private boolean isPressed = false;
	private boolean isHovered = false;
	private String text;
	
	
	public Button(int width, int height, Sprite sprite) {
		super(width, height);
		this.sprite = sprite;
		this.text = null;
	}
	
	public Button(int width, int height, Sprite sprite, String text) {
		super(width, height);
		this.sprite = sprite;
		this.text = text;
	}
	
	public void tick() {
		if (isPressed == true) {
			for (ButtonListener listener : listeners) {
				System.out.println("Handling press");
				listener.buttonPressed();
				isPressed = false;
			}
		}
	}
	
	public void addButtonListener(ButtonListener listener) {
		this.listeners.add(listener);
	}
	
	public void press() {
		isPressed = true;
	}
	
	public void hover() {
		isHovered = true;
	}
	
	public void unHover() {
		isHovered = false;
	}
	 
	@Override
	public void render(Screen screen) {
		super.render(screen);
		
		screen.render(getX(), getY(), getHeight(), getWidth(), sprite);
		if (text != null) {
			int textLength = text.length();
			Art.FONT.render(getX()+(getWidth()-Art.FONT.getStringWidth(text))/2, getY()+(getHeight())/2, text, screen);
		}
		
		// Render highlight if hovered
		if (isHovered)
			screen.renderBlank(getX(), getY(), getWidth(), getHeight(), 0x7fffff00);
	}
	
}
