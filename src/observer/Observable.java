package observer;
import java.util.ArrayList;


public abstract class Observable {
	private ArrayList<IObserver> observers;
	
	public Observable() {
		observers = new ArrayList<IObserver>();
	}
	
	public void addObserver(IObserver observer) {
		observers.add(observer);
	}
	
	public void notifyObservers() {
		for (IObserver observer : observers) {
			observer.update();
		}
	}
}
