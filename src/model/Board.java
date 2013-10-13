package model;

import controller.Key;
import view.gui.GuiComponent;
import view.gui.MainMenu;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import controller.InputHandler;

import view.Art;

import view.GameFrame;
import view.Screen;
import model.tetromino.Tetromino;
import model.tetromino.Tetromino.Type;


public class Board extends GuiComponent implements InputHandler.OnToggleListener {
    /**
     * Width of the board in blocks
     */
    public static final int LINE_WIDTH = 10;
    /**
     * Amount of lines in the board, including hidden lines
     */
	public static final int LINE_AMOUNT = 23;
    /**
     * Amount of fall moves per second
     */
	public static final int MOVES_PER_SECOND = 1;
    /**
     * Amount of ticks between fall moves
     */
	public static final int TICKS_PER_MOVE = GameFrame.TICKS_PER_SECOND/MOVES_PER_SECOND;
	public static final int PLACEMENT_X = 148;
	public static final int PLACEMENT_Y = 24;
    /**
     * Size of a block in pixels
     */
	public static final int BLOCK_SCALE = GameFrame.BLOCK_SCALE;
    /**
     * Width of the progress bar to the left of the board
     */
    public static final int PROGRESS_BAR_WIDTH = 6;
    /**
     * Amount of hidden rows in the top of the board
     */
	public static final int HIDDEN_ROWS = 3;
    /**
     * Total width of the window
     */
    public static final int WIDTH = LINE_WIDTH*BLOCK_SCALE + PROGRESS_BAR_WIDTH;
    /**
     * Total height of the window
     */
	public static final int HEIGHT = (LINE_AMOUNT-HIDDEN_ROWS)*BLOCK_SCALE;
    /**
     * Lines required to win the game
     */
	public static final int GOAL_LINES = 40;
    /**
     * Number of preview tetrominoes
     */
    public static final int PREVIEW_COUNT = 4;
    /**
     * Tick between auto repeats
     */
    public static final int AUTO_REPEAT_TICKS = 4;
    /**
     * Ticks before auto repeat kicks in
     */
    public static final int AUTO_REPEAT_INIT_TICKS = 15;

	private TetrominoBag bag;
	private Block[][] rows;
	private Tetromino tetromino;
	private LinkedList<Tetromino> next = new LinkedList<Tetromino>();
	private Preview hold;
	private int ticksSinceMove;
	private int linesCleared;
    private OnGameOverListener gameOverListener;

	private DbScoreboard scoreboard;
    private NextTetrominoesChangedListener nextTetrominoesChangedListener;
    private int timeToNextLeftMove = -1, timeToNextRightMove = -1, timeToNextDownMove = 0;
    private boolean leftPressed, rightPressed, downPressed;
    private boolean paused;

	//private static Board current;



   	public Board() {
   		super();
		bag = new TetrominoBag();
        next = new LinkedList<Tetromino>();
        hold = new Preview();

        setWidth(WIDTH);
        setHeight(HEIGHT);

		scoreboard = new DbScoreboard();
	}

	/*public static Board getCurrent() {
		if (current == null)
			current = new Board();
		return current;
	}*/



	public void addBlock(Point position, Type type) {
		rows[position.getY()][position.getX()] = new Block(type);
	}
	
	public void newGame() {
		clearBoard();
		bag.refillGoodStart();
		initializeTetrominoes();

		linesCleared = 0;
        initializePositions();
	}

    private void initializePositions() {
        tetromino.initializePositions();
        timeToNextLeftMove = -1;
        timeToNextRightMove = -1;
        timeToNextDownMove = 0;
    }
	
	private void clearBoard() {
		rows = new Block[LINE_AMOUNT][LINE_WIDTH];
		next = new LinkedList<Tetromino>();
        hold.setTetromino(null);
	}

    public Preview getHoldPreivew() {
        return hold;
    }
	
	public void clearFullLines() {
		for (int i = 0; i < rows.length; i++) {
			if (clearLineIfFull(i))
				linesCleared++;
		}
	}

    public Collection<Tetromino> getNextTetrominoes() {
        return next;
    }
	
	public boolean clearLineIfFull(int lineN) {
		Block[] line = rows[lineN];
		if (isLineFull(line)) {
			for (int i = 0; i < line.length; i++) {
				line[i] = null;
			}
			moveLinesDown(lineN);
			return true;
		}
		return false;
	}
	
	public Block getBlock(int x, int y) {
		if (x >= 0 && x < LINE_WIDTH && y >= 0 && y < LINE_AMOUNT)
			return rows[y][x];
		return null;
	}
	
	public void getNextTetromino() {
        // Get next tetromino and generate new one
        Iterator<Tetromino> it = next.iterator();
        if (it.hasNext()) {
            tetromino = next.pop();
            next.add(bag.draw());
        } else {
            tetromino = bag.draw();
        }

        // Notify listeners that tetrominoes preview is updated.
        if (nextTetrominoesChangedListener != null)
            nextTetrominoesChangedListener.onNextTetrominoesChanged(next);

        // Lose player if game is lost.
		if (isLost()) {
            if (gameOverListener != null)
			    gameOverListener.onGameOver(false);
			GameFrame.getInstance().setComponent(new MainMenu());
		}		
	}
	
	public Block[][] getRows() {
		return rows;
	}
	
	public Tetromino getTetromino() {
		return tetromino;
	}
	
	public void hold() {
		if (hold.isEnabled()) {
            hold.setEnabled(false);
			if (hold.getTetromino() == null) {
				initializePositions();
                hold.setTetromino(tetromino);
				getNextTetromino();
			}
			else {
				initializePositions();
				Tetromino tmp = hold.getTetromino();
				hold.setTetromino(tetromino);
                tetromino = tmp;
			}
		}	
	}

    @Override
    public void onToggle(Key key, boolean pressed) {
        InputHandler input = InputHandler.getInstance();

        if (key == input.left)
            moveLeft(pressed);
        if (key == input.right)
            moveRight(pressed);
        if (key == input.down)
            moveDown(pressed);
        if (pressed) {
            if (key == input.hold)
                hold();
            if (key == input.rotate)
                tetromino.rotateClockwise(this);
            if (key == input.rotateCounter)
                tetromino.rotateCounterClockwise(this);
            if (key == input.rotate180)
                tetromino.rotate180(this);
            if (key == input.allLeft)
                moveAllLeft();
            if (key == input.allRight)
                moveAllRight();
            if (key == input.softDrop)
                softDrop();
            if (key == input.hardDrop)
                hardDrop();
        }
    }

    private synchronized void moveLeft(boolean pressed) {
        leftPressed = pressed;
        if (pressed)	 {
            if (timeToNextLeftMove <= 0) {
                tetromino.tryMove(this, Direction.LEFT);
                if (timeToNextLeftMove < 0)	timeToNextLeftMove = AUTO_REPEAT_INIT_TICKS;
                else timeToNextLeftMove = AUTO_REPEAT_TICKS;
            }
            else timeToNextLeftMove--;
        }
        else
            timeToNextLeftMove = -1;
    }

    public synchronized void moveRight(boolean pressed) {
        rightPressed = pressed;
        if (pressed) {
            if (timeToNextRightMove <= 0) {
                tetromino.tryMove(this, Direction.RIGHT);
                if (timeToNextRightMove < 0) timeToNextRightMove = AUTO_REPEAT_INIT_TICKS;
                else timeToNextRightMove = AUTO_REPEAT_TICKS;
            }
            else timeToNextRightMove--;
        }
        else
            timeToNextRightMove = -1;
    }

    public synchronized void moveDown(boolean pressed) {
        downPressed = pressed;
        if (pressed) {
            if (timeToNextDownMove <= 0) {
                tetromino.tryMove(this, Direction.DOWN);
                timeToNextDownMove = AUTO_REPEAT_TICKS;
            }
            else timeToNextDownMove--;
        }
        else timeToNextDownMove = 0;
    }

    public void hardDrop() {
        while (tetromino.tryMove(this, Direction.DOWN));
        placeTetromino();
    }

    public void softDrop() {
        boolean hasDropped = false;
        while (tetromino.tryMove(this, Direction.DOWN)) hasDropped = true;
        if (hasDropped)
            resetTicksSinceMove();
    }

    public void moveAllLeft() {
        tetromino.moveToEnd(this, Direction.LEFT);
    }

    public void moveAllRight() {
        tetromino.moveToEnd(this, Direction.RIGHT);
    }

    private void initializeTetrominoes() {
		tetromino = bag.draw();
		next = new LinkedList<Tetromino>();
        for (int i = 0; i < PREVIEW_COUNT; i++) {
            next.add(bag.draw());
		}
        if (nextTetrominoesChangedListener != null)
		    nextTetrominoesChangedListener.onNextTetrominoesChanged(next);
	}
	
	public boolean isEmpty(Point p) {
		return isInside(p) && rows[p.getY()][p.getX()] == null;
	}
	
	public boolean isEmpty(Point[] points) {
		for (Point point : points) {
			if (!isEmpty(point))
				return false;
		}
		return true;
	}
	
	public boolean isInside(Point p) {
		int x = p.getX();
		int y = p.getY();
		return x >= 0 && y >= 0 && x < rows[0].length  && y < rows.length;
	}
	
	public boolean isLineFull(Block[] line) {
		for (Block block : line) {
			if (block == null)
				return false;
		}
		return true;
	}
	
	public boolean isLost() {
		for (Point point : tetromino.getSelection()) {
			if (!isEmpty(point))
				return true;
		}
		return false;
	}
	
	private void moveLinesDown(int n) {
		while (n >= 0) {
			if (n > 0) {
				rows[n] = rows[n-1];
			}
			else {
				rows[n] = new Block[LINE_WIDTH];
			}
			n--;
		}
	}
	
	public void placeTetromino() {
		hold.setEnabled(true);
		for (Point point : tetromino.getSelection()) {
			addBlock(point, tetromino.getType());
		}
		this.getNextTetromino();
		clearFullLines();
	}
	
	@Override
	public void render(Screen screen) {
        screen.renderBlank(getX(), getY(), getWidth()-6, getHeight(), 0xff000000);
		renderProgressBar(screen);
        renderBlocks(screen);
		renderGhost(screen);
		renderTetromino(screen);

		super.render(screen);
	}
	
	private void renderBlocks(Screen screen) {
		for (int y = HIDDEN_ROWS; y < rows.length; y++) {
			Block[] row = rows[y];
			for (int x = 0; x < row.length; x++) {
				Block block = row[x];
				if (block == null) {
					//screen.renderBlank(x*BLOCK_SCALE+getX(), y*BLOCK_SCALE+getY(), BLOCK_SCALE, BLOCK_SCALE, 0x00ff00);
				}
				else {
					screen.render(x*BLOCK_SCALE+getX(), (y-HIDDEN_ROWS)*BLOCK_SCALE+getY(), Tetromino.getSprite(block.getType()));
				}
			}
		}
	}
	
	private void renderGhost(Screen screen) {
		Point[] gSelection = tetromino.getGhost(this).getSelection();
		for (Point point : gSelection) {
			int yy = point.getY()-HIDDEN_ROWS;
			if (yy >= 0) {
				int x = point.getX()*BLOCK_SCALE+getX();
				int y = yy*BLOCK_SCALE+getY();
				screen.renderHue(x, y, Art.GHOST, tetromino.getColor() & 0xffffff | 0xff000000);
			}
		}
	}
	


    @Override
    public int getX() {
        return super.getX()+6;
    }

    public int getLinesLeft() {
        return GOAL_LINES - linesCleared;
    }

    private void renderProgressBar(Screen screen) {
		int lClear = linesCleared;
		if (lClear < 0)
			lClear = 0;
		int offset = HEIGHT/GOAL_LINES * lClear;
		if (offset > HEIGHT) offset = HEIGHT;
		
		screen.renderBlank(getX(), getY()+offset, WIDTH-PROGRESS_BAR_WIDTH, HEIGHT-offset, 0xff220000);
		screen.renderBlank(getX(), getY()+offset, WIDTH-PROGRESS_BAR_WIDTH, 2, 0xff550000);
		screen.renderBlank(getX()-PROGRESS_BAR_WIDTH, getY()+offset, PROGRESS_BAR_WIDTH, HEIGHT-offset, 0xffff0000);
	}
	
	private void renderTetromino(Screen screen) {
		for (Point point : tetromino.getSelection()) {
			int yy = getY() + GameFrame.BLOCK_SCALE * point.getY() - BLOCK_SCALE * HIDDEN_ROWS;
			if (yy >= getY())
				screen.render(getX() + GameFrame.BLOCK_SCALE * point.getX(), yy, tetromino.getSprite());
		}
	}
	

	
	public void resetTicksSinceMove() {
		ticksSinceMove = 0;
	}
	
	public boolean isWon() {
		return linesCleared >= GOAL_LINES;
	}
	
	private void addOldTime(Timer timer) {

	}
	
	@Override
	public void tick() {
        if (!paused) {
            if (isWon()) {
                if (gameOverListener != null)
                    gameOverListener.onGameOver(true);
                GameFrame.getInstance().setComponent(new MainMenu());
            }
            ticksSinceMove++;
            if (ticksSinceMove >= TICKS_PER_MOVE) {
                tetromino.moveDownAndPlace(this);
            }

            moveLeft(leftPressed);
            moveRight(rightPressed);
            moveDown(downPressed);
        }
	}



    public void setNextTetrominoesChangedListener(NextTetrominoesChangedListener nextTetrominoesChangedListener) {
        this.nextTetrominoesChangedListener = nextTetrominoesChangedListener;
    }

    public interface NextTetrominoesChangedListener {
        public void onNextTetrominoesChanged(List<Tetromino> tetrominoes);
    }

    public interface OnGameOverListener {
        public void onGameOver(boolean won);
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

    public void pause() {
        deactivate();
        paused = true;
    }

    public void unpause() {
        activate();
        paused = false;
    }

    public void setGameOverListener(OnGameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }
}
