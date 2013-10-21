package view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SmartFont implements IFont {
    private static int ID_COLOR = 0xffff0000;

	private ArrayList<Sprite> sprites;
    private int charHeight;
    private int asciiInit;
    private int spacing = 5;

	public SmartFont(BufferedImage sheet, int charHeight, int asciiInit) {
		this.charHeight = charHeight;
        this.asciiInit = asciiInit;
		this.sprites = new ArrayList<Sprite>();

        for (int y = 0 ; y < sheet.getHeight() - charHeight; y += charHeight) {
            int left = -1;
            for (int x = 0; x < sheet.getWidth(); x++) {
                if (ID_COLOR == sheet.getRGB(x, y)) {
                    if (left >= 0) { // If any left has been found
                        sprites.add(new Sprite(sheet.getSubimage(left+1, y+1, x - left - 1, charHeight-1)));
                    }
                    left = x;
                }
            }
        }
	}
	 @Override
	public void render(int x, int y, String text, Screen screen) {
		text = text.toUpperCase();
		char[] c = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			Sprite sprite = getSprite(c[i]);
			screen.render(x, y, sprite);
            x += sprite.getWidth() + spacing;
		}
	}

    @Override
	public void render(int x, int y, int height, String text, Screen screen) {
		text = text.toUpperCase();
        float scale = height / charHeight;
		char[] c = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			Sprite sprite = getSprite(c[i]);
			screen.render(x, y, height, (int)(sprite.getWidth() * scale), sprite);
            x += (sprite.getWidth() + spacing) * scale;
		}
	}

    @Override
    public int getStringWidth(String text) {
        int sum = 0;
        text = text.toUpperCase();
        char[] chars = text.toCharArray();
        for (char c : chars) {
            sum += getSprite(c).getWidth() + spacing;
        }
        return sum;
    }

    private Sprite getSprite(char ascii) {
        return sprites.get(ascii - asciiInit);
    }

    public int getCharWidth(char character) {
		return getSprite(character).getWidth();
	}
	
	public int getHeight() {
		return charHeight;
	}
}
