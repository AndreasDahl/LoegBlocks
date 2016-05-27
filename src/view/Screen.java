package view;

public class Screen {
	private final int[] pixels;
	private final int width;
	
	public Screen(int[] pixels, int width) {
		this.width = width;
		this.pixels = pixels;
	}
	
	public void renderBlank(int xp, int yp, int w, int h, int color) {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				paintPixel((x+xp) + (y+yp) * width, color);
			}
		}
	}
	
	private int applyTransparency(int color, int bgColor) {
		int t = (color >> 24) & 0xff;
		int nred = ((color & 0xff0000) * t >> 8) & 0xff0000;
		int ngreen = ((color & 0xff00) * t >> 8) & 0xff00;
		int nblue = ((color & 0xff) * t >> 8) & 0xff;
		
		int t2 = 0xff - t;
		int ored = ((bgColor & 0xff0000) * t2 >> 8) & 0xff0000;
		int ogreen = ((bgColor & 0xff00) * t2 >> 8) & 0xff00; 
		int oblue = ((bgColor & 0xff) * t2 >> 8) & 0xff;			
		
		int red = nred + ored;
		int green = ngreen + ogreen; 
		int blue = nblue + oblue;
		
		return red | green | blue;
	}
	
	private void paintPixelHue(int position, int color, double hue, double saturation) {
		int trans = ((color >> 24) & 0xff);
		if (trans == 0);
		else if (trans == 0xff) {
			color = Color.applyHue(color, hue, saturation);
			pixels[position] = color;
		}
		else {
			color = Color.applyHue(color, hue, saturation);
			pixels[position] = applyTransparency(color, pixels[position]);
		}
	}
	
	private void paintPixel(int position, int color) {
		int trans = ((color >> 24) & 0xff);
		if (trans == 0);
		else if (trans == 0xff)
			pixels[position] = color;
		else {
			pixels[position] = applyTransparency(color, pixels[position]);
		}		
	}
	
	public void render(int xp, int yp, Sprite sprite) {
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				paintPixel((x + xp) + (y+yp) * width, sprite.getPixel(x, y));
			}
		}
	}
	
	public void render(int xp, int yp, int height, int width, Sprite sprite) {
		int sw = sprite.getWidth();
		int sh = sprite.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				paintPixel((x + xp) + (y+yp) * this.width, sprite.getPixel(x*sw/width, y*sh/height));
			}
		}
	}

    public void render(int xp, int yp, int height, int width, Sprite sprite, int color) {
        int sw = sprite.getWidth();
        int sh = sprite.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = sprite.getPixel(x*sw/width, y*sh/height);
                int newColor = (color & 0xffffff) + (pixel & 0xff000000);
                paintPixel((x + xp) + (y+yp) * this.width, newColor);
            }
        }
    }
	
	public void renderHue(int xp, int yp, Sprite sprite, int color) {
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		
		double hue = Color.getHue(color);
		double saturation = (color >> 24 & 0xff) / 255.0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int c = sprite.getPixel(x, y);
				int pos = (x + xp) + (y+yp) * width;
				
				paintPixelHue(pos, c, hue, saturation);
			}
		}
	}
	
	public void renderWidthInvisColor(int xp, int yp, Sprite sprite, int color) {
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		for (int y = 0; y < w; y++) {
			for (int x = 0; x < h; x++) {
				int col = sprite.getPixel(x + y * w) & 0xffffff;
				if (color != col)
					pixels[(x + xp) + (y+yp) * width] = col;
			}
		}
	}
	
}
