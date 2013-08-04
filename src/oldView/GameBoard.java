package oldView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import observer.IObserver;

import controller.ArrowKeyListener;

import model.Block;
import model.Board;
import model.Point;
import model.tetromino.Tetromino;

public class GameBoard extends JPanel implements IObserver {
	private static final long serialVersionUID = -1406077166444402338L;
	private static final int BLOCK_SCALE = 30;
	private static final int INVISIBLE_ROWS = 4;
	
	private static GameBoard instance;
	
	private GameBoard() {
		super();
		this.setPreferredSize(new Dimension(
				BLOCK_SCALE * Board.LINE_WIDTH, 
				BLOCK_SCALE * (Board.LINE_AMOUNT-INVISIBLE_ROWS)+BLOCK_SCALE/2));
		this.setBackground(Color.BLACK);

	}
	
	public static GameBoard getInstance() {
		if (instance == null)
			instance = new GameBoard();
		return instance;
	}
	
	private void paintBlock(Graphics g, int x, int y, Color c) {
		g.setColor(c);
		g.fillRect(x*BLOCK_SCALE+1, y*BLOCK_SCALE+1 - INVISIBLE_ROWS*BLOCK_SCALE+ BLOCK_SCALE/2, BLOCK_SCALE-1, BLOCK_SCALE-1);
		//g.setColor(Color.WHITE);
		//g.drawRect(x*BLOCK_SCALE, y*BLOCK_SCALE, BLOCK_SCALE, BLOCK_SCALE);
	}
	
	private void paintGhostBlock(Graphics g, int x, int y) {
		g.setColor(Color.WHITE);
		g.drawRect(x*BLOCK_SCALE, y*BLOCK_SCALE - INVISIBLE_ROWS*BLOCK_SCALE + BLOCK_SCALE/2, BLOCK_SCALE, BLOCK_SCALE);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Board board = Board.getCurrent();
		Block[][] rows = board.getRows();

		//Paint all the blocks
		for (int y = 0; y < rows.length; y++) {
			Block[] row = rows[y];
			for (int x = 0; x < row.length; x++) {
				if (row[x] != null) {
					paintBlock(g, x, y, row[x].getColor());
				}
			}
		}
		//Paint the tetromino
		Tetromino tetromino = Board.getCurrent().getTetromino();
		for (Point point : tetromino.getSelection()) {
			paintBlock(g, point.getX(), point.getY(), tetromino.getColor());
		}
		
		//Paint the ghost
		Tetromino ghost = tetromino.getGhost();
		for (Point point : ghost.getSelection()) {
			paintGhostBlock(g, point.getX(), point.getY());
		}
	}
	
	public void update() {
		this.repaint();
	}
}
