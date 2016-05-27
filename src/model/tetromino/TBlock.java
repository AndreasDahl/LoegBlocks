package model.tetromino;

import model.Board;
import model.Point;

class TBlock extends Tetromino {
	private static final int COLOR = 0xbfff00ff;
	
	TBlock() {
		super(COLOR);
	}
	
	@Override
	public void initializePositions() {
        super.initializePositions();
        
		Point[] relative = new Point[3];
		relative[0] = new Point(0, -1);
		relative[1] = new Point(-1, 0);
		relative[2] = new Point(1, 0);
		
		setBase(new Point(Board.LINE_WIDTH/2, 2));
		setRel(relative);
	}
	
	@Override
	public Type getType() {
		return Type.T;
	}
}
