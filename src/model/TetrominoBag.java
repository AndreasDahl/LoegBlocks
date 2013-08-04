package model;

import java.util.Random;

import model.tetromino.Tetromino;
import model.tetromino.Tetromino.Type;

/**
 * A bag of tetrominos.
 * @author Andreas
 */
public class TetrominoBag {
	private static final Type[] index = { Type.I, Type.O, Type.T, Type.Z, Type.S, Type.L, Type.J };

	
	private Type[] tetrominos;
	private Random rand;
	private int pointer;

	public TetrominoBag() {
		tetrominos = new Type[7];
		rand = new Random();
	}

	/**
	 * Draw the next tetromino from the bag, refills the bag first if all tetrominos have been drawn.
	 * @return next tetromino drawn.
	 */
	public Tetromino draw() {
		if (!hasNext()) {
			refill();
		}
		Type value = tetrominos[pointer];
		pointer++;
		return Tetromino.getTetromino(value);
	}

	public boolean hasNext() {
		return (pointer < 7);
	}

	/**
	 * Fills a bag with the 7 different tetrominos, in a random order.
	 */
	public void refill() {
		System.out.println("new Bag");
		pointer = 0;
		DynamicArray<Type> left = new DynamicArray<Type>(index);
		for (int n = 6; n >= 0; n--) {
			int i = rand.nextInt(n+1);
			tetrominos[n] = left.remove(i);
		}
	}

	/**
	 * fills a bag, so that it does not start with a Z, S or O block.
	 */
	public void refillGoodStart() {
		System.out.println("good Bag");
		pointer = 0;
		DynamicArray<Type> left = new DynamicArray<Type>(index);
		boolean gotOne = false;
		while (!gotOne) {
			int i = rand.nextInt(6);
			if (left.get(i) != Type.S && left.get(i) != Type.Z & left.get(i) != Type.O) {
				tetrominos[0] = left.remove(i);
				gotOne = true;
			}
		}
		for (int n = 5; n >= 0; n--) {
			int i = rand.nextInt(n+1);
			tetrominos[6-n] = left.remove(i);
		}
	}
}