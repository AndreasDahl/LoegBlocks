package model;

import model.tetromino.Tetromino.Type;

class Block {
	private Type type;
	
	Block(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
