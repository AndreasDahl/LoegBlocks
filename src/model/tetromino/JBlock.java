package model.tetromino;

import model.Board;
import model.Point;

public class JBlock extends Tetromino {
	public static final int COLOR = 0xbf0000ff;
	
	public JBlock() {
		super(COLOR);
	}
	
	@Override
	public void initializePositions() {
		Point[] relative = new Point[3];
		relative[0] = new Point(1, 0);
		relative[1] = new Point(-1, 0);
		relative[2] = new Point(-1, -1);
		
		setBase(new Point(Board.LINE_WIDTH/2, 2));
		setRel(relative);
	}
	
	@Override
	public Type getType() {
		return Type.J;
	}
}
