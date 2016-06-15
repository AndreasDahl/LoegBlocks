package view.gui;

import view.Art;
import view.GameFrame;

class PauseMenu extends GuiMenu {
    PauseMenu(final GameView gameView, int width, int height) {
        super(width, height);

        Button resume = new Button(200, 100, Art.BUTTON, "Resume");
        resume.addButtonListener(gameView::unPauseGame);
        this.addButton(resume, getWidth() / 2 - 100, 100);

        Button quit = new Button(200, 100, Art.BUTTON, "Quit");
        quit.addButtonListener(() -> GameFrame.getInstance().setComponent(new MainMenu()));
        this.addButton(quit, getWidth() / 2 - 100, 200);

        this.setBackgroundColor(0xBD000000);
    }
}
