package model.tetromino;

import model.Board;
import model.Point;

class IBlock extends Tetromino {
	private static final int COLOR = 0xbf00ffff;
	private static final Point[] BASES = {new Point(0,-1), new Point(1,0), new Point(0,1), new Point(-1,0)};
	private static final Point[][] RELS = {	{new Point(-1,0), new Point(1,0), new Point(2,0)},
											{new Point(0,-1), new Point(0,1), new Point(0,2)},
											{new Point(1,0), new Point(-1,0), new Point(-2,0)},
											{new Point(0,1), new Point(0,-1), new Point(0,-2)},
										};
	private static final Point[][] KICKS = {{new Point(-2,0), new Point(1,0), new Point(-2,1), new Point(1,-2)},
											{new Point(-1,0), new Point(2,0), new Point(-1,-2), new Point(2,1)},
											{new Point(2,0), new Point(-1,0), new Point(2,-1), new Point(-1,2)},
											{new Point(1,0), new Point(-2,0), new Point(1,1), new Point(-2,-1)}};

	
	IBlock() {
		super(COLOR);
	}
	
	@Override
	public void initializePositions() {
        super.initializePositions();

		Point[] relative = new Point[3];
		relative[0] = new Point(-1, 0);
		relative[1] = new Point(1, 0);
		relative[2] = new Point(2, 0);
		
		setBase(new Point(Board.LINE_WIDTH/2, 2));
		setRel(relative);
	}
	
	@Override
	public Type getType() {
		return Type.I;
	}
	
	@Override
	protected Point[][] getKicks() {
		return KICKS;
	}
	
	@Override
	public void rotateClockwise(Board board) {
		int rot = (getRotation()+1) % 4;
		if (rotateAlgorithm(board, Point.translate(getBase(), BASES[rot]), RELS[rot], getKicks()[getRotation()])) {
			addRotation(1);
		}
        System.out.print("Rotate " + this.toString() + "\n");
	}
	
	@Override
	public void rotateCounterClockwise(Board board) {
		int rot = (getRotation()+3) % 4;
		Point inverseBase = new Point(-BASES[getRotation()].getX(), -BASES[getRotation()].getY());
		if (rotateAlgorithm(board, Point.translate(getBase(), inverseBase), RELS[rot], getKicks()[(getRotation()+2)%4])) {
			addRotation(3);
		}
	}
	
	@Override
	public void rotate180(Board board) {
		int rot = (getRotation()+2) % 4;
		if (rotateAlgorithm(board, Point.translate(getBase(), BASES[rot]), RELS[rot], getKicks()[(getRotation()+1)%4])) {
			addRotation(2);
		}
	}
	
	
}
