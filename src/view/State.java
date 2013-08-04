package view;

public interface State {
	public abstract void tick();
	public abstract void render(Screen screen);

}
