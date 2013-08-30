package view.gui;

import java.util.LinkedList;

import model.DbScoreboard;
import model.Timer;
import view.Art;
import view.Screen;

public class HighscoreView extends GuiComponent {
	private int scoresShown = 10;
	private LinkedList<Long> scores;
	
	public HighscoreView(int width, int height) {
		super(width, height);
		
		refresh();
	}
	
	public void refresh() {
		scores = new DbScoreboard().getTop(scoresShown);
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		
		screen.renderBlank(getX(), getY(), getWidth(), getHeight(), 0xffffffff);
		
		int dist = getHeight()/scoresShown;
		int i = 0;
		for (Long score : scores) {
			Art.FONT.render(getX(), getY()+(dist*i), dist, Timer.longToTimeString(score), screen);
			i++;
		}
	}
}
