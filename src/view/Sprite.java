package view;

import java.awt.image.BufferedImage;

import view.Color;

public class Sprite {
	private int width, height;
	private int[] pixels;
	
	public Sprite(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}
	
	public Sprite(BufferedImage image, int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[height * width];
		
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = image.getRGB(x*width/imgWidth, y*height/imgHeight);
			}
		}
	}
	
	public Sprite(Sprite sprite, double hue, double saturation) {
		this.width = sprite.width;
		this.height = sprite.height;		
		this.pixels = new int[width * height];
		
		for (int n = 0; n < pixels.length; n++) {
			int trans = sprite.pixels[n] & 0xff000000;
			pixels[n] = Color.applyHue(sprite.pixels[n], hue, saturation) | trans;
		}
	}
	
	private Sprite(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[height * width];
		
		int w2 = width/2;
		int h2 = width/2;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((y < h2) == (x < w2))
					pixels[y * width + x] = 0xffffffff;
				else
					pixels[y * width + x] = 0xff000000;
			}
		}
	}
	
	public static Sprite placeholder(int width, int height) {
		return new Sprite(width, height);
	}
	
	/**
	 * Sprite, where the colors of the sprite is neutralised and added a hue;
	 * @param  image  the original image
	 * @param  color  color of the hue
	 */
	public Sprite(BufferedImage image, int color) {
		width = image.getWidth();
		height = image.getHeight();
		int[] sat = image.getRGB(0, 0, width, height, null, 0, width);
		pixels = new int[sat.length];
		for (int i = 0; i < sat.length; i++) {
			int sr = (sat[i] >> 16) & 0xff ;
			int sg = (sat[i] >> 8) & 0xff;
			int sb = sat[i] & 0xff;
			int saturation = (sr+sg+sb)/3;
			
			int r = ((color & 0xff0000) * saturation / 0xff) & 0xff0000;
			int g = ((color & 0xff00) * saturation / 0xff) & 0xff00;
			int b = ((color & 0xff) * saturation / 0xff) & 0xff;
			pixels[i] = r+g+b;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPixel(int index) {
		return pixels[index];
	}
	
	public int getPixel(int x, int y) {
		return pixels[x + y * width];
	}
}
