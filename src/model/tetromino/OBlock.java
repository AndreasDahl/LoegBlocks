package model.tetromino;

import model.Board;
import model.Point;

public class OBlock extends Tetromino {
	public static final int COLOR = 0xbfffff00;
	
	public OBlock() {
		super(COLOR);
	}
	
	@Override
	public void initializePositions() {
        super.initializePositions();

		Point[] select = new Point[3];
		select[0] = new Point(1, 0);
		select[1] = new Point(0, 1);
		select[2] = new Point(1, 1);
		
		setBase(new Point(Board.LINE_WIDTH/2-1, 1));
		setRel(select);
	}
	
	@Override
	public Type getType() {
		return Type.O;
	}
	
	@Override
	public void rotateClockwise(Board board) {
		// OBlocks don't rotate
	}
	
	@Override
	public void rotateCounterClockwise(Board board) {
		// OBlocks don't rotate
	}
	
	@Override
	public void rotate180(Board board) {
		// OBlocks don't rotate
	}
}
