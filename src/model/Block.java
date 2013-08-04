package model;

import model.tetromino.Tetromino.Type;

public class Block {
	private Type type;
	
	public Block(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
