package view.gui;

import controller.InputHandler;
import model.Board;
import model.Preview;
import model.tetromino.Tetromino;
import view.Art;
import view.GameFrame;
import view.Screen;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Andreas
 * Date: 30-08-13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class GameView extends GuiComponent {
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

        hold = new Preview();
        addChild(hold, 24, 24);
    }

    private void initBoard() {
        this.board = new Board();
        board.addHoldListener(new Board.onHoldListener() {
            @Override
            public void onHold(Tetromino tetromino) {
                hold.setTetromino(tetromino);
            }
        });

        addChild(board, Board.PLACEMENT_X, Board.PLACEMENT_Y);

    }

    @Override
    public void render(Screen screen) {
        screen.renderBlank(0, 0, getWidth(), getHeight(), 0xffffffff);
        super.render(screen);
    }

    private void renderHold(Screen screen) {
        if (board.hasHeld())
            hold.renderGray(screen);
        else
            hold.render(screen);
    }

    public Board getBoard() {
        return this.board;
    }

    @Override
    public void tick(InputHandler input) {
        board.tick(input);
    }
}
