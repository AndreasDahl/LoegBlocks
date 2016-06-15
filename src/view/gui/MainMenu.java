package view.gui;

import view.Art;
import view.GameFrame;

public class MainMenu extends GuiMenu {
	private final Button startGame;
    private TextComponent scoreText;
	
	public MainMenu() {
		super();

        this.setBackgroundColor(Art.BACKGROUND);

        // start game button
		startGame = new Button(200, 48, Art.BUTTON, "New Game");
		startGame.addButtonListener(() -> GameFrame.getInstance().startGame());
		addButton(startGame, getWidth()/2-startGame.getWidth()/2, 300);

        // High scores button
		Button highScores = new Button(200, 48, Art.BUTTON, "Highscores");
		highScores.addButtonListener(() -> GameFrame.getInstance().setComponent(new HighscoresMenu()));
		addButton(highScores, getWidth()/2-highScores.getWidth()/2, startGame.getBottom()+8);

        // Options Button
        Button options = new Button(200, 48, Art.BUTTON, "Options");
        options.addButtonListener(() -> GameFrame.getInstance().setComponent(new OptionsMenu()));
        addButton(options, getWidth()/2-options.getWidth()/2, highScores.getBottom()+8);

        TextComponent title = new TextComponent(GameFrame.TITLE, 36);
        title.setTextColor(0xffffffff);
        this.addChild(title, getWidth()/2 - title.getWidth()/2, 100);
	}

    void setScoreText(String text) {
        if (scoreText != null) {
            removeChild(scoreText);
        }
        scoreText = new TextComponent(text, 24);
        scoreText.setTextColor(0xffffffff);
        this.addChild(scoreText, getWidth()/2 - scoreText.getWidth()/2, startGame.getTop() - scoreText.getHeight()- 10);
    }
}
