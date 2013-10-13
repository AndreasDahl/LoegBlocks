package view.gui;

import view.Art;
import view.GameFrame;
import view.Screen;

public class PauseMenu extends GuiMenu {
    private GameView gameView;

    public PauseMenu(final GameView gameView, int width, int height) {
        super(width, height);

        this.gameView = gameView;

        Button resume = new Button(200, 100, Art.BUTTON, "Resume");
        resume.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed() {
                gameView.unPauseGame();
            }
        });
        this.addButton(resume, getWidth() / 2 - 100, 100);

        Button quit = new Button(200, 100, Art.BUTTON, "Quit");
        quit.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed() {
                GameFrame.getInstance().setComponent(new MainMenu());
            }
        });
        this.addButton(quit, getWidth() / 2 - 100, 200);

        this.setBackgroundColor(0xBD000000);
    }
}
