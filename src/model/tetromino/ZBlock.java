package model.tetromino;

import model.Board;
import model.Point;

public class ZBlock extends Tetromino {
	public static final int COLOR = 0xbfff0000;
	
	public ZBlock() {
		super(COLOR);
	}
	
	@Override
	public void initializePositions() {
		Point[] relative = new Point[3];
		relative[0] = new Point(1, 0);
		relative[1] = new Point(0, -1);
		relative[2] = new Point(-1, -1);
		
		setBase(new Point(Board.LINE_WIDTH/2, 2));
		setRel(relative);
	}
	
	@Override
	public Type getType() {
		return Type.Z;
	}
}
