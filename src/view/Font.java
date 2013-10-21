package view;

import java.awt.image.BufferedImage;

public class Font implements IFont {
	private static String L = 	"ABCDEFGHIJKLMNOPQRSTUVWXYZ���1234567890 " +
								"().,-!?%'_/&\\\"#";
	
	private Sprite[] sprites;
	private int charWidth, charHeight;
	
	public Font(BufferedImage sheet, int charWidth, int charHeight) {
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		sprites = new Sprite[L.length()];
		int sheetWidth = sheet.getWidth();
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = new Sprite(sheet.getSubimage(i * this.charWidth % sheetWidth, (i * charWidth / sheetWidth)*charHeight, charWidth, charHeight));
		}
	}

    @Override
	public void render(int x, int y, String text, Screen screen) {
		text = text.toUpperCase();
		char[] c = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			Sprite sprite = sprites[L.indexOf(c[i])];
			screen.render(x + i * charWidth, y, sprite);
		}
	}

    @Override
	public void render(int x, int y, int height, String text, Screen screen) {
		text = text.toUpperCase();
		char[] c = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			Sprite sprite = sprites[L.indexOf(c[i])];
			screen.render(x + i * (charWidth * height / charHeight), y, height, height, sprite);
		}
	}

    @Override
    public void render(int x, int y, int height, String text, int color, Screen screen) {
        text = text.toUpperCase();
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Sprite sprite = sprites[L.indexOf(c[i])];
            screen.render(x + i * (charWidth * height / charHeight), y, height, height, sprite, color);
        }
    }

    @Override
    public int getStringWidth(String text) {
        return this.charWidth * text.length();
    }

    @Override
    public int getStringWidth(String text, int height) {
        return text.length() * charWidth * height / charHeight;
    }

    @Override
    public int getHeight() {
        return charHeight;
    }

    public int getCharWidth() {
		return charWidth;
	}
}
