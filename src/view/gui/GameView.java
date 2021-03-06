package view.gui;

import controller.InputHandler;
import controller.Key;
import model.Board;
import model.DbScoreboard;
import model.Preview;
import model.Timer;
import model.tetromino.Tetromino;
import view.Art;
import view.GameFrame;
import view.Screen;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Andreas
 * Date: 30-08-13
 * Time: 22:35
 */
public class GameView extends GuiComponent implements
        Board.NextTetrominoesChangedListener,
        Board.OnGameOverListener,
        InputHandler.OnToggleListener {
    private Board board;
    private final LinkedList<Preview> next;
    private PauseMenu pauseMenu;
    private final Timer timer;

    public GameView() {
        super();
        next = new LinkedList<>();
        initBoard();

        Preview preview = new Preview();
        next.add(preview);
        addChild(preview, board.getX() + board.getWidth() + 24, GameFrame.BLOCK_SCALE);
        preview = new Preview();
        next.add(preview);
        addChild(preview, board.getX() + board.getWidth() + 24, GameFrame.BLOCK_SCALE * 6);
        preview = new Preview();
        next.add(preview);
        addChild(preview, board.getX() + board.getWidth() + 24, GameFrame.BLOCK_SCALE * 11);
        preview = new Preview();
        next.add(preview);
        addChild(preview, board.getX() + board.getWidth() + 24, GameFrame.BLOCK_SCALE * 16);

        board.setNextTetrominoesChangedListener(this);

        addChild(board.getHoldPreivew(), 24, 24);

        HighscoreView highscores = new HighscoreView(board.getX() - 48, 110);
        highscores.setScaleText(false);
        addChild(highscores, 0, getHeight() - 36 - highscores.getHeight());

        timer = new Timer();

        board.newGame();
        timer.restart();
    }

    @Override
    public void onNextTetrominoesChanged(List<Tetromino> tetrominoes) {
        for (int i = 0; i < tetrominoes.size(); i++) {
            next.get(i).setTetromino(tetrominoes.get(i));
        }
    }

    private void initBoard() {
        this.board = new Board();
        board.setGameOverListener(this);
        addChild(board, Board.PLACEMENT_X, Board.PLACEMENT_Y);

    }

    @Override
    public void render(Screen screen) {
        screen.renderBlank(0, 0, getWidth(), getHeight(), 0xffffffff);
        renderProgress(screen);
        renderTimer(screen);
        super.render(screen);
    }

    private void renderProgress(Screen screen) {
        Art.FONT.render(0, GameFrame.HEIGHT-12, "Lines Left " + (board.getLinesLeft()), screen);
        Art.FONT.render(5, 200, 48, Integer.toString(board.getLinesLeft()), screen);
    }

    public Board getBoard() {
        return this.board;
    }

    @Override
    public void tick() {
        timer.newTime();

        super.tick();
    }

    private void pauseGame() {
        board.pause();
        pauseMenu = new PauseMenu(this, getWidth(), getHeight());
        this.addChild(pauseMenu, 0,0);
    }

    public void unPauseGame() {
        board.unpause();
        if (pauseMenu != null) {
            removeChild(pauseMenu);
        }
        pauseMenu = null;
    }

    @Override
    public void activate() {
        super.activate();
        InputHandler.getInstance().addOnToggleListener(this);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        InputHandler.getInstance().removeOnToggleListener(this);
    }

    @Override
    public void onToggle(Key key, boolean pressed) {
        if (key == InputHandler.getInstance().back && pressed) {
            if (pauseMenu == null)
                pauseGame();
            else
                unPauseGame();
        }
    }

    private void renderTimer(Screen screen) {
        Art.FONT.render(0, getHeight()-24, timer.toString(), screen);
    }

    @Override
    public void onGameOver(boolean won) {
        MainMenu mainMenu = new MainMenu();
        if (won) {
            DbScoreboard scoreboard = new DbScoreboard();
            long score = timer.getTimePassed();
            scoreboard.add(score);
            mainMenu.setScoreText(Timer.longToTimeString(score));
        }
        GameFrame.getInstance().setComponent(mainMenu);
    }
}