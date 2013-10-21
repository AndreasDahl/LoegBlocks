package view.gui;

import view.Art;
import view.IFont;
import view.Screen;

/**
 * Created with IntelliJ IDEA.
 * User: Andreas
 * Date: 22-10-13
 * Time: 00:06
 */
public class TextComponent extends GuiComponent {
    private String text;
    private int textColor;
    private IFont font;


    public TextComponent(String text, int height) {
        super(0, height);
        init(text);
    }

    public TextComponent(String text) {
        super(0, Art.FONT.getHeight());
        init(text);
    }

    public void setTextColor(int newColor) {
        this.textColor = newColor;
    }

    public int getTextColor() {
        return this.textColor;
    }

    private void  init(String text) {
        this.textColor = 0xff000000;
        this.text = text;
        this.font = Art.FONT;
        updateWidth();
    }

    private void updateWidth() {
        setWidth(font.getStringWidth(text, getHeight()));
    }

    private void setText(String text) {
        this.text = text;
        updateWidth();
    }

    private String getText() {
        return this.text;
    }

    @Override
    public synchronized void render(Screen screen) {
        super.render(screen);
        font.render(getX(), getY(), getHeight(), text, textColor, screen);
    }
}
