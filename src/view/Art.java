package view;

import java.io.IOException;
import javax.imageio.ImageIO;

import view.Color;

import model.Board;

public class Art {
	public static Sprite BLOCK = loadSprite("/res/block1.png", 24, 24);
	public static Sprite BLOCK_CYAN = new Sprite(BLOCK, Color.getHue(0x00ffff), 0.75);
	public static Sprite BLOCK_BLUE = new Sprite(BLOCK, Color.getHue(0x0000ff), 0.75);
	public static Sprite BLOCK_RED =  new Sprite(BLOCK, Color.getHue(0xff0000), 0.75);
	public static Sprite BLOCK_GREEN = new Sprite(BLOCK, Color.getHue(0x00ff00), 0.75);
	public static Sprite BLOCK_PURPLE = new Sprite(BLOCK, Color.getHue(0xff00ff), 0.75);
	public static Sprite BLOCK_YELLOW = new Sprite(BLOCK, Color.getHue(0xffff00), 0.75);
	public static Sprite BLOCK_ORANGE = new Sprite(BLOCK, Color.getHue(0xff7f00), 0.75);
	
	public static Sprite BUTTON = loadSprite("/res/button.png", 200, 100);

	
	public static Sprite GHOST = loadSprite("/res/ghost.png", 24, 24);
	public static Sprite BG = loadSprite("/res/pretty.png", 528, 528);
	public static Sprite START_MENU_BG = loadSprite("/res/pretty.png", 528, 528);
	public static Font FONT = loadFont("/res/font3.png", 12, 12);	
	
	private static Sprite loadSprite(String path, int width, int height) {
		try {
			return new Sprite(ImageIO.read(GameFrame.class.getResourceAsStream(path)), width, height);
		} catch (Exception e) {
			return Sprite.placeholder(width, height);
		}
	}
	
	private static Font loadFont(String path, int charWidth, int charHeight) {
		try {
			return new Font(ImageIO.read(Board.class.getResourceAsStream(path)), charWidth, charHeight);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
