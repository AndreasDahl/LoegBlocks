package model;

import java.util.ArrayList;

class DynamicArray<T> {
	private T[] array;
	
	DynamicArray(T[] array) {
		this.array = array;
	}
	
	T get(int index) {
		return array[index];
	}
	
	T remove(int index) {
		T value = array[index];
		ArrayList<T> tmp = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			if (i != index) {
				tmp.add(array[i]);
			}
		}
		array = (T[]) tmp.toArray();
		return value;
	}
}
