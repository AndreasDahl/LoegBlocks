package view;

/**
 * Created with IntelliJ IDEA.
 * User: Andreas
 * Date: 21-10-13
 * Time: 17:53
 */
public interface IFont {
    void render(int x, int y, String text, Screen screen);
    void render(int x, int y, int height, String text, Screen screen);
    void render(int x, int y, int height, String text, int color, Screen screen);

    int getStringWidth(String text);
    int getStringWidth(String text, int height);
    int getHeight();
}
