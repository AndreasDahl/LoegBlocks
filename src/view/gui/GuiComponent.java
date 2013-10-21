package view.gui;

import view.GameFrame;
import view.Screen;

import java.util.ArrayList;

public abstract class GuiComponent {
	private int x, y, width, height;
    private ArrayList<GuiComponent> children;
    private GuiComponent parent;
    private int backgroundColor = 0x00000000;
	
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

    public int getLeft() {
        return this.x;
    }

    public int getTop() {
        return this.y;
    }

    public int getRight() {
        return this.x + getWidth();
    }

    public int getBottom() {
        return this.y + getHeight();
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

    public GuiComponent getParent() {
        return this.parent;
    }

    public synchronized void addChild(GuiComponent child, int x, int y) {
        child.setX(x);
        child.setY(y);
        children.add(child);
        child.parent = this;
        child.activate();
    }

    public synchronized boolean removeChild(GuiComponent child) {
        boolean removed = children.remove(child);
        if (removed)
            child.deactivate();
        return removed;
    }

    public synchronized void tick() {
        ArrayList<GuiComponent> tmpList = (ArrayList<GuiComponent>) children.clone();
        for (GuiComponent child : tmpList) {
            child.tick();
        }
    }

    public synchronized void render(Screen screen) {
        screen.renderBlank(getX(), getY(), getWidth(), getHeight(), backgroundColor);
        for (GuiComponent child : children) {
            child.render(screen);
        }
    }

    public synchronized void activate() {
        for (GuiComponent child : children) {
            child.activate();
        }
    }

    public synchronized void deactivate() {
        for (GuiComponent child : children) {
            child.deactivate();
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }
}
