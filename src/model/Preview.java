package model;

import view.Art;
import view.GameFrame;
import view.Screen;
import model.tetromino.Tetromino;
import view.gui.GuiComponent;

public class Preview extends GuiComponent {
	private Tetromino tetromino;
    private boolean enabled;

    public Preview() {
        enabled = true;
        setWidth(96);
        setHeight(96);
    }

	public void setTetromino(Tetromino tetromino) {
		this.tetromino = tetromino;
	}
	
	public Tetromino getTetromino() {
		return tetromino;
	}
	
	@Override
    public void render(Screen screen) {
		screen.renderBlank(getX(), getY(), 96, 96, 0xff000000);
		
		if (tetromino != null) {
            if (enabled) {
                tetromino.renderStatic(screen, GameFrame.BLOCK_SCALE + getX(), GameFrame.BLOCK_SCALE + getY());
            } else {
                tetromino.renderStatic(screen, GameFrame.BLOCK_SCALE + getX(), GameFrame.BLOCK_SCALE + getY(), Art.BLOCK);
            }
		}
	}

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    boolean isEnabled() {
        return this.enabled;
    }
}
