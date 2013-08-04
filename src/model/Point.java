package model;

/**
 * A point in a tetris board. Measured with the x-axis going right and the y-axis going downwards.
 * @author Andreas
 * @version 1.0
 */
public class Point {
	private int x;
	private int y;
	
	/**
	 * Contructs a Point with the given coordinates
	 * @param  x  the x-coordinate 
	 * @param  y  the y-coordinate 
	 */
	public Point(int x, int y) {
	this.x = x;
		this.y = y;
	}
	
	/**
	 * @return x-coordinate of the point
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return y-coordinate of the point
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * returns the neighboring point to this point.
	 * @param  direction  The direction in which side to get a neighbour
	 * @return  Neighbouring point
	 */
	public Point getNeighbour(Direction direction) {
		switch (direction) {
			case UP:	return new Point(x, y-1);
			case DOWN:	return new Point(x, y+1);
			case LEFT:  return new Point(x-1, y);
			default:	return new Point(x+1, y);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj.getClass() != this.getClass())
			return false;
		Point test = (Point)obj;
		return x == test.x && y == test.y;
	}
	
	@Override
	public int hashCode() {
		return x + 1000*y;
	}
	
	@Override
	public String toString() {
		return "x: " + x + "  y: " + y;
	}
	
	/**
	 * Adds the x-coordinates and y-coordinates of another point to this point.
	 * @param  otherPoint  Coordinates of the point to translate by
	 * @return  this point. After translation
	 */
	public Point translate(Point otherPoint) {
		return translate(otherPoint.getX(), otherPoint.getY());
	}
	
	/**
	 * Adds the given x- and y-coordinates to this point.
	 * @param  x  x-coordinate to be added
	 * @param  y  y-coordinate to be added
	 * @return  this point. After translation
	 */
	public Point translate(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public static Point translate(int x1, int y1, int x2, int y2) {
		return new Point(x1+x2, y1+y2);
	}
	
	public static Point translate(Point point1, Point point2) {
		return translate(point1.getX(), point1.getY(), point2.getX(), point2.getY());
	}
}
