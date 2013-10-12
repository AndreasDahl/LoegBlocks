package model.tetromino;

import controller.InputHandler;
import controller.Key;
import view.Art;
import view.GameFrame;
import view.Screen;
import view.Sprite;

import model.Board;
import model.Direction;
import model.Point;

/**
 * Controllable collection of four blocks
 * @author Andreas
 */
public abstract class Tetromino {	
	private Point base;
	private Point[] rel;
	private int color;
	private int rotation; // 0 = no, 1 = r, 2 = 180, 3 = l;

	
	protected static Point[][] KICKS = {{new Point(-1,0), new Point(-1,-1), new Point(0,2), new Point(-1,2)},
										{new Point(1,0), new Point(1,1), new Point(0,-2), new Point(1,-2)},
										{new Point(1,0), new Point(1,-1), new Point(0,2), new Point(-1,-2)},
										{new Point(-1,0), new Point(-1,1), new Point(0,-2), new Point(-1,-2)}};
	
	public enum Type {
		I, O, T, Z, S, L, J;
	}
	
	public Tetromino(int color) {
		this.color = color;

        initializePositions();
	}

    public abstract void initializePositions();
	
	public abstract Type getType();
	
	public Point[] getSelection() {
		Point[] selection = new Point[4];
		selection[0] = base;
		for (int i = 0; i < 3; i++) {
			selection[i+1] = new Point(base.getX() + rel[i].getX() , base.getY() + rel[i].getY());
		}
		return selection;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public Point[] getSelection(Point[] otherRel) {
		Point[] selection = new Point[4];
		selection[0] = base;
		for (int i = 0; i < 3; i++) {
			selection[i+1] = new Point(base.getX() + otherRel[i].getX(), 
					base.getY() + otherRel[i].getY());
		}
		return selection;
	}
	
	public Point[] getSelection(Point otherBase) {
		Point[] selection = new Point[4];
		selection[0] = otherBase;
		for (int i = 0; i < 3; i++) {
			selection[i+1] = new Point(otherBase.getX() + rel[i].getX(), 
					otherBase.getY() + rel[i].getY());
		}
		return selection;
	}
	
	public int getColor() {
		return color;
	}
	
	public Sprite getSprite() {
		return getSprite(getType());
	}
	
	public static Sprite getSprite(Type type) {
		switch (type) {
			case I: return Art.BLOCK_CYAN; 
			case O: return Art.BLOCK_YELLOW;
			case T: return Art.BLOCK_PURPLE;
			case Z: return Art.BLOCK_RED;
			case S: return Art.BLOCK_GREEN;
			case L: return Art.BLOCK_ORANGE;
			default: return Art.BLOCK_BLUE;
		}
	}
	
	public static Tetromino getTetromino(Type type) {
		switch (type) {
		case I: return new IBlock(); 
		case O: return new OBlock();
		case T: return new TBlock();
		case Z: return new ZBlock();
		case S: return new SBlock();
		case L: return new LBlock();
		default: return new JBlock();
		}
	}

	public boolean tryMove(Board board, Direction direction) {
		if (canMove(board, direction)) {
			this.base = base.getNeighbour(direction);
			if (direction == Direction.DOWN && board.getTetromino() == this)
				board.resetTicksSinceMove();
			return true;
		}
		return false;
	}
	
	public boolean canMove(Board board, Direction direction) {
		Point[] selection = getSelection();

		for (Point point : selection) {
			if (!board.isEmpty(point.getNeighbour(direction)))
				return false;
		}
		return true;
	}
	
	/*
	private boolean isSelectionEmpty(Point[] selection) {
		Board board = GameFrame.getInstance().getBoard();
		for (Point point : selection) {
			if (!board.isEmpty(point))
				return false;
		}
		return true;
	}
	*/
	
	/*
	public void place() {
		Point[] selection = getSelection();
		Board board = GameFrame.getInstance().getBoard();
		for (Point point : selection) {
			board.addBlock(point, this.getType());
		} 
	}
	*/
	
	public Tetromino getGhost(Board board) {
		Tetromino ghost = getCopy();
		while (ghost.tryMove(board, Direction.DOWN));
		return ghost;
	}
	
	protected Tetromino getCopy() {
		Tetromino t = null;
		try {
			t = this.getClass().newInstance();
			if (t instanceof Tetromino) {
				t.setBase(this.base);
				t.setRel(this.rel);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}

	public void moveDownAndPlace(Board board) {
		if (!tryMove(board, Direction.DOWN))
			board.placeTetromino();
	}
	

	
	public void moveToEnd(Board board, Direction dir) {
		while (tryMove(board, dir));
	}
	
	/**
	 * <p>Adds a given number to this tetromino's current rotation. Where 0 is the
	 * starting position and every number added is equal to a clockwise 
	 * rotation.</p>
	 * <p>The rotation will wrap around from 3 to 0 as there are only 4 possible
	 * rotations of a tetromino.
	 * @param n
	 */
	protected void addRotation(int n) {
		rotation += n;
		rotation = rotation % 4;
	}

    public void rotateClockwise(Board board) {
        Point[] newRel = new Point[3];
        for (int i = 0; i < rel.length; i++) {
            newRel[i] = new Point(-rel[i].getY(), rel[i].getX());
        }
        if (rotateAlgorithm(board, base, newRel, getKicks()[rotation])) {
            addRotation(1);
        }
    }

    public void rotateCounterClockwise(Board board) {
        Point[] newRel = new Point[3];
        for (int i = 0; i < rel.length; i++) {
            newRel[i] = new Point(rel[i].getY(), -rel[i].getX());
        }
        int r = 0;
        if (getRotation() == 0) r = 2;
        if (getRotation() == 1) r = 1;
        if (getRotation() == 2) r = 0;
        if (getRotation() == 3) r = 3;
        if (rotateAlgorithm(board, base, newRel, getKicks()[r]))
            addRotation(3);
    }
	
	public void rotate180(Board board) {
		Point[] newRel = new Point[3];
		for (int i = 0; i < rel.length; i++) {
			newRel[i] = new Point(-rel[i].getX(), -rel[i].getY());
		}
		if (rotateAlgorithm(board, base, newRel, getKicks()[(rotation+1)%4])) {
			addRotation(2);
		}
	}
	
	/**
	 * Rotates the Tetromino if it can find an empty spot in the list of kicks
	 * with the given base and new relative positions
	 * @param board		The board the tetromino tries to rotate in
	 * @param base		The desired base of the tetromino. Will be altered
	 * 					if a kick occurs
	 * @param newRel	The new relative positions.
	 * @param kickList	The list of possible kicks
	 * @return
	 */
	protected boolean rotateAlgorithm(Board board, Point base, Point[] newRel, Point[] kickList) {
		Point[] tmp = kickList;
		kickList = new Point[kickList.length+1];
		kickList[0] = new Point(0,0);
		for (int i = 0; i < tmp.length; i++) {
			kickList[i+1] = tmp[i];
		}
		
		for (Point p : kickList) {
			Point newBase = Point.translate(base, p);
			if (board.isEmpty(genSelection(newBase, newRel))) {
				this.base = newBase;
				this.rel = newRel;
				board.resetTicksSinceMove();
				return true;
			}
		}
		return false;
	}
	
	private Point[] genSelection(Point base, Point[] rel) {
		Point[] selection = new Point[4];
		selection[0] = base;
		int x = base.getX();
		int y = base.getY();
		for (int i = 0; i < rel.length; i++) {
			selection[i+1] = new Point(x+rel[i].getX(), y+rel[i].getY());
		}
		return selection;
	}
	
	protected void setRel(Point[] rel) {
		this.rel = rel;
	}
	
	protected void setBase(Point point) {
		this.base = point;
	}
	
	public Point getBase() {
		return base;
	}
	
	public Point[] getRel() {
		return rel;
	}
	
	protected Point[][] getKicks() {
		return KICKS;
	}
	
	public void render(Screen screen, int xoff, int yoff) {
		render(screen, xoff, yoff, getSprite());
	}
	
	public void renderStatic(Screen screen, int xoff, int yoff) {
		renderStatic(screen, xoff, yoff, getSprite());
	}
	
	public void renderStatic(Screen screen, int xoff, int yoff, Sprite sprite) {
		screen.render(xoff, yoff, sprite);
		for (Point point : rel) {
			screen.render(xoff + GameFrame.BLOCK_SCALE * point.getX(), yoff + GameFrame.BLOCK_SCALE * point.getY(), sprite);
		}
	}
	
	public void render(Screen screen, int xoff, int yoff, Sprite sprite) {
		for (Point point : getSelection()) {
			screen.render(xoff + GameFrame.BLOCK_SCALE * point.getX(), yoff + GameFrame.BLOCK_SCALE * point.getY(), sprite);
		}
	}
}
