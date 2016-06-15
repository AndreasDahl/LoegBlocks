package model;

import model.tetromino.Tetromino.Type;

class Block {
	private final Type type;
	
	Block(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}
}
