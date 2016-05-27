package view.gui;

import model.DbScoreboard;
import model.Timer;
import view.Art;
import view.Screen;

import java.util.LinkedList;

class HighscoreView extends GuiComponent {
	private int scoresShown = 10;
	private LinkedList<Long> scores;
    private boolean scaleText;
	
	HighscoreView(int width, int height) {
		super(width, height);

        scaleText = true;
		
		refresh();
	}
	
	private void refresh() {
		scores = new DbScoreboard().getTop(scoresShown);
	}

    void setScaleText(boolean scaleText) {
        this.scaleText = scaleText;
    }
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		
		int dist = getHeight()/scoresShown;
		int i = 0;
		for (Long score : scores) {
            if (scaleText)
			    Art.FONT.render(getX(), getY()+(dist*i), dist, Timer.longToTimeString(score), screen);
            else {
                Art.FONT.render(getX(), getY()+(Art.FONT.getHeight()*i), Timer.longToTimeString(score), screen);
            }
			i++;
		}
	}
}
