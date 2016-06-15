package view.gui;

import org.jetbrains.annotations.Nullable;
import view.Art;
import view.Screen;
import view.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Button extends GuiComponent {

	private final List<ButtonListener> listeners = new ArrayList<>();
	
	private final Sprite sprite;
	private boolean isPressed = false;
	private boolean isHovered = false;
	private final @Nullable String text;
	
	
	public Button(int width, int height, Sprite sprite) {
		super(width, height);
		this.sprite = sprite;
		this.text = null;
	}
	
	public Button(int width, int height, Sprite sprite, @Nullable String text) {
		super(width, height);
		this.sprite = sprite;
		this.text = text;
	}
	
	public void tick() {
		if (isPressed) {
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
			Art.FONT.render(getX()+(getWidth()-Art.FONT.getStringWidth(text))/2, getY()+(getHeight())/2 - (Art.FONT.getHeight()/2), text, screen);
		}
		
		// Render highlight if hovered
		if (isHovered)
			screen.renderBlank(getX(), getY(), getWidth(), getHeight(), 0x7fffff00);
	}
	
}
