package model;

import java.util.ArrayList;

public class DynamicArray<T> {
	private T[] array;
	
	public DynamicArray(T[] array) {
		this.array = array;
	}
	
	public T get(int index) {
		return array[index];
	}
	
	@SuppressWarnings("unchecked")
	public T remove(int index) {
		T value = array[index];
		ArrayList<T> tmp = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			if (i != index) {
				tmp.add(array[i]);
			}
		}
		array = (T[])tmp.toArray();
		return value;
	}
}
