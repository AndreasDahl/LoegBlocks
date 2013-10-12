package view.gui;

import controller.InputHandler;
import model.Board;
import model.Preview;
import model.tetromino.Tetromino;
import view.Art;
import view.GameFrame;
import view.Screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Andreas
 * Date: 30-08-13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class GameView extends GuiComponent implements Board.NextTetrominoesChangedListener {
    private Board board;
    private LinkedList<Preview> next;
    private Preview hold;

    public GameView() {
        super();
        next = new LinkedList<Preview>();
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

        this.hold = board.getHoldPreivew();
        addChild(hold, 24, 24);
    }

    @Override
    public void onNextTetrominoesChanged(List<Tetromino> tetrominoes) {
        for (int i = 0; i < tetrominoes.size(); i++) {
            next.get(i).setTetromino(tetrominoes.get(i));
        }
    }

    private void initBoard() {
        this.board = new Board();
        addChild(board, Board.PLACEMENT_X, Board.PLACEMENT_Y);

    }

    @Override
    public void render(Screen screen) {
        screen.renderBlank(0, 0, getWidth(), getHeight(), 0xffffffff);
        renderProgress(screen);
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
        board.tick();
    }
}
