package model;

import view.Art;
import view.GameFrame;
import view.Screen;
import model.tetromino.Tetromino;

public class Preview {
	private Tetromino tetromino;
	private int xPos, yPos;
	
	public Preview(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public void setTetromino(Tetromino tetromino) {
		this.tetromino = tetromino;
	}
	
	public Tetromino getTetromino() {
		return tetromino;
	}
	
	public void render(Screen screen) {
		screen.renderBlank(xPos, yPos, 96, 96, 0xff000000);
		
		if (tetromino != null) {
			tetromino.renderStatic(screen, 1*GameFrame.BLOCK_SCALE + xPos, 1 * GameFrame.BLOCK_SCALE + yPos);
		}
	}
	
	public void renderGray(Screen screen) {
		screen.renderBlank(xPos, yPos, 96, 96, 0);
		
		if (tetromino != null) {
			tetromino.renderStatic(screen, 1*GameFrame.BLOCK_SCALE + xPos, 1 * GameFrame.BLOCK_SCALE + yPos, Art.BLOCK);
		}
	}
}
