package view;

/**
 * Created with IntelliJ IDEA.
 * User: Andreas
 * Date: 21-10-13
 * Time: 17:53
 */
public interface IFont {
    public void render(int x, int y, String text, Screen screen);
    public void render(int x, int y, int height, String text, Screen screen);

    public int getStringWidth(String text);
    public int getHeight();
}
