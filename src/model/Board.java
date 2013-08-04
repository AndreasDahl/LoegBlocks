package model;

import gui.Component;
import gui.MainMenu;

import java.util.Iterator;
import java.util.LinkedList;

import controller.InputHandler;

import view.Art;

import view.GameFrame;
import view.Screen;
import model.tetromino.Tetromino;
import model.tetromino.Tetromino.Type;


public class Board extends Component {
	public static final int LINE_WIDTH = 10;
	public static final int LINE_AMOUNT = 23;
	public static final int MOVES_PER_SECOND = 1;
	public static final int TICKS_PER_MOVE = GameFrame.TICKS_PER_SECOND/MOVES_PER_SECOND;
	public static final int PLACEMENT_X = 148;
	public static final int PLACEMENT_Y = 24;
	public static final int BLOCK_SCALE = GameFrame.BLOCK_SCALE;
	public static final int WIDTH = LINE_WIDTH*BLOCK_SCALE;
	public static final int HIDDEN_ROWS = 3;
	public static final int HEIGHT = (LINE_AMOUNT-HIDDEN_ROWS)*BLOCK_SCALE;
	public static final int GOAL_LINES = 40;
	
	private TetrominoBag bag;
	private Block[][] rows;
	private Tetromino tetromino;
	private LinkedList<Preview> next = new LinkedList<Preview>();
	private Preview hold;
	private int ticksSinceMove;
	private int linesCleared;
	private Timer timer;
	private DbScoreboard scoreboard;
	private LinkedList<Long> scores;
	private long latestScore;
	private boolean hasHeld;

	//private static Board current;
	
   	public Board() {
   		super();
		bag = new TetrominoBag();
		
		next.add(new Preview(PLACEMENT_X + WIDTH + 24, BLOCK_SCALE));
		next.add(new Preview(PLACEMENT_X + WIDTH + 24, BLOCK_SCALE*6));
		next.add(new Preview(PLACEMENT_X + WIDTH + 24, BLOCK_SCALE*11));
		next.add(new Preview(PLACEMENT_X + WIDTH + 24, BLOCK_SCALE*16));
		hold = new Preview(24, 24);
		timer = new Timer();
		scoreboard = new DbScoreboard();
		
		newGame();
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
		timer.restart();
		linesCleared = 0;
		scores = scoreboard.getTop(10);
	}
	
	private void clearBoard() {
		rows = new Block[LINE_AMOUNT][LINE_WIDTH];
		for (Preview preview : next) {
			preview.setTetromino(null);
		}
		hold.setTetromino(null);
	}
	
	public void clearFullLines() {
		for (int i = 0; i < rows.length; i++) {
			if (clearLineIfFull(i))
				linesCleared++;
		}
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
		Iterator<Preview> it = next.iterator();
		Preview p;
		p = it.next();
		tetromino = p.getTetromino();
		while (it.hasNext()) {
			Preview p2 = it.next();
			p.setTetromino(p2.getTetromino());
			p = p2;
		}
		p.setTetromino(bag.draw());
		if (isLost()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// Do nothing
			}
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
		if (!hasHeld) {
			hasHeld = true;
			if (hold.getTetromino() == null) {
				tetromino.initializePositions();
				hold.setTetromino(tetromino);
				getNextTetromino();
			}
			else {
				tetromino.initializePositions();
				Tetromino tmp = hold.getTetromino();
				hold.setTetromino(tetromino);
				tetromino = tmp;
			}
		}	
	}
	
	private void initializeTetrominoes() {
		tetromino = bag.draw();
		for (Preview preview : next) {
			preview.setTetromino(bag.draw());
		}
		
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
		hasHeld = false;
		for (Point point : tetromino.getSelection()) {
			addBlock(point, tetromino.getType());
		}
		this.getNextTetromino();
		clearFullLines();
	}
	
	@Override
	public void render(Screen screen) {
		screen.render(0, 0, Art.BG);
		renderProgressBar(screen);
		renderBlocks(screen);
		renderGhost(screen);
		renderTetromino(screen);
		renderPreviews(screen);
		renderHold(screen);
		renderTimer(screen);
		renderOldTimes(screen);
	}
	
	private void renderBlocks(Screen screen) {
		for (int y = HIDDEN_ROWS; y < rows.length; y++) {
			Block[] row = rows[y];
			for (int x = 0; x < row.length; x++) {
				Block block = row[x];
				if (block == null) {
					//screen.renderBlank(x*BLOCK_SCALE+PLACEMENT_X, y*BLOCK_SCALE+PLACEMENT_Y, BLOCK_SCALE, BLOCK_SCALE, 0x00ff00);
				}
				else {
					screen.render(x*BLOCK_SCALE+PLACEMENT_X, (y-HIDDEN_ROWS)*BLOCK_SCALE+PLACEMENT_Y, Tetromino.getSprite(block.getType()));
				}
			}
		}
	}
	
	private void renderGhost(Screen screen) {
		Point[] gSelection = tetromino.getGhost(this).getSelection();
		for (Point point : gSelection) {
			int yy = point.getY()-HIDDEN_ROWS;
			if (yy >= 0) {
				int x = point.getX()*BLOCK_SCALE+PLACEMENT_X;
				int y = yy*BLOCK_SCALE+PLACEMENT_Y;
				screen.renderHue(x, y, Art.GHOST, tetromino.getColor() & 0xffffff | 0xff000000);
			}
		}
	}
	
	private void renderOldTimes(Screen screen) {
		int j = scores.size() - 1;
		for (int i = j; i >= 0; i--) {
			long score = scores.get(i);
			if (score == latestScore)
				Art.FONT.render(12, GameFrame.HEIGHT-((j-i)*12+40), Timer.longToTimeString(scores.get(i)), screen);
			else
				Art.FONT.render(0, GameFrame.HEIGHT-((j-i)*12+40), Timer.longToTimeString(scores.get(i)), screen);
		}
		
	}
	
	private void renderPreviews(Screen screen) {
		for (Preview preview : next) {
			preview.render(screen);
		}
	}
	
	private void renderHold(Screen screen) {
		if (hasHeld)
			hold.renderGray(screen);
		else
			hold.render(screen);
	}
	
	private void renderProgressBar(Screen screen) {
		int lClear = linesCleared;
		if (lClear < 0)
			lClear = 0;
		int offset = HEIGHT/GOAL_LINES * lClear;
		if (offset > HEIGHT) offset = HEIGHT;
		
		screen.renderBlank(PLACEMENT_X, PLACEMENT_Y+offset, WIDTH, HEIGHT-offset, 0xff220000);
		screen.renderBlank(PLACEMENT_X, PLACEMENT_Y+offset, WIDTH, 2, 0xff550000);
		screen.renderBlank(PLACEMENT_X-6, PLACEMENT_Y+offset, 6, HEIGHT-offset, 0xffff0000);
		
		Art.FONT.render(0, GameFrame.HEIGHT-12, "Lines Left " + (GOAL_LINES - linesCleared), screen);
		Art.FONT.render(5, 200, 48, Integer.toString(GOAL_LINES - linesCleared), screen);
	}
	
	private void renderTetromino(Screen screen) {
		for (Point point : tetromino.getSelection()) {
			int yy = PLACEMENT_Y + GameFrame.BLOCK_SCALE * point.getY() - BLOCK_SCALE * HIDDEN_ROWS;
			if (yy >= PLACEMENT_Y)
				screen.render(PLACEMENT_X + GameFrame.BLOCK_SCALE * point.getX(), yy, tetromino.getSprite());
		}
	}
	
	private void renderTimer(Screen screen) {
		Art.FONT.render(0, GameFrame.HEIGHT-24, timer.toString(), screen);
	}
	
	public void resetTicksSinceMove() {
		ticksSinceMove = 0;
	}
	
	public void restart() {
		newGame();
	}
	
	public boolean isWon() {
		return linesCleared >= GOAL_LINES;
	}
	
	private void addOldTime(Timer timer) {
		scoreboard.add(timer.getTimePassed());
		latestScore = timer.getTimePassed();
	}
	
	@Override
	public void tick(InputHandler input) {
		if (isWon()) {
			addOldTime(timer.clone());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// Do nothing
			}
			GameFrame.getInstance().setComponent(new MainMenu());
		}
		timer.newTime();
		ticksSinceMove++;
		if (ticksSinceMove >= TICKS_PER_MOVE) {
			tetromino.moveDownAndPlace(this);
		}
		while (input.hold.next()) if (input.hold.isClicked()) hold();
		tetromino.tick(this, input);
	}
}
