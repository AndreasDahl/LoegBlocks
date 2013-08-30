package view.gui;

import view.GameFrame;
import view.Screen;

import java.util.ArrayList;

public class GuiComponent extends Component {
	private int x, y, width, height;
    private ArrayList<GuiComponent> children;
    private GuiComponent parent;
	
	public GuiComponent(int width, int height) {
		this.width = width;
		this.height = height;
        this.children = new ArrayList<GuiComponent>();
	}
	
	public GuiComponent() {
		this(GameFrame.WIDTH, GameFrame.HEIGHT);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

    public void addChild(GuiComponent child, int x, int y) {
        child.setX(x);
        child.setY(y);
        children.add(child);
        child.parent = this;
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);

        for (GuiComponent child : children) {
            child.render(screen);
        }
    }
}
