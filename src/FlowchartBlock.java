/**
 * FlowchartBlock is an abstract class serving as a basis for other blocks
 * to be handled polymorphically
 * 
 * @author Minh Vo
 * @version 0.0.3
 */
import java.awt.*;

public abstract class FlowchartBlock {
	// width and height and arc curve of all flowchart blocks
	private static final double WIDTH = 200; // Width of block
	private static final double HEIGHT = 100; // Height of block

	private String type; // Type of block
	private Shape block; // Shape of block
	private String msg; // Message of block
	private int xStr; // x coordinate of msg drawing
	private int yStr; // y coordinate of msg drawing
	
	// Pre and next FlowchartBlocks connected to this one
	private FlowchartBlock pre;
	private FlowchartBlock next;
	
	private Point origin; // top left corner of shape box
	
	// Top, bottom, right, left points to draw lines from
	private Point top;
	private Point bottom;
	private Point right;
	private Point left;

	/** 
	 * Constructor initializes all the private variables based on input 
	 * values
	 * @param type Type of block
	 */
	public FlowchartBlock (String type) {
		this.type = type;
	}
	
	// Abstract methods to be implemented by children classes
	// public abstract void paintBlock(BlockCanvas c);
	public abstract void move(double deltaX, double deltaY);
	public abstract boolean containsPoint(Point p);

	/**
	 * Getter for type
	 * @return type of block
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter for type
	 * @param type new type of block
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter for block
	 * @return the block shape object
	 */
	public Shape getBlock() {
		return block;
	}

	/**
	 * Setter for block
	 * @param block the new block shape object
	 */
	public void setBlock(Shape block) {
		this.block = block;
	}
	
	/**
	 * Getter for message
	 * @return message of the block
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Setter for message
	 * @param msg new message contained by block
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * Getter for xStr
	 * @return x coordinate of string drawing on canvas
	 */
	public int getxStr() {
		return xStr;
	}

	/**
	 * Setter for xStr
	 * @param xStr new x coordinate of string drawing on canvas
	 */
	public void setxStr(int xStr) {
		this.xStr = xStr;
	}

	/**
	 * Getter for yStr
	 * @return y coordinate of string drawing on canvas
	 */
	public int getyStr() {
		return yStr;
	}

	/**
	 * Setter for yStr
	 * @param yStr new y coordinate of string drawing on canvas
	 */
	public void setyStr(int yStr) {
		this.yStr = yStr;
	}
	
	/**
	 * Getter for pre
	 * @return the previous block connected to this one
	 */
	public FlowchartBlock getPre() {
		return pre;
	}

	/**
	 * Setter for pre
	 * @param pre a new previous block connected to this one
	 */
	public void setPre(FlowchartBlock pre) {
		this.pre = pre;
	}

	/**
	 * Getter for next
	 * @return the next block connected to this one
	 */
	public FlowchartBlock getNext() {
		return next;
	}

	/**
	 * Setter for next
	 * @param next a new next block connected to this one
	 */
	public void setNext(FlowchartBlock next) {
		this.next = next;
	}

	/**
	 * Getter for origin
	 * @return get the origin x and y point of this block
	 */
	public Point getOrigin() {
		return origin;
	}

	/**
	 * Setter for origin
	 * @param origin set a new origin x and y point of this block
	 */
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	/**
	 * Getter for top
	 * @return the top Point
	 */
	public Point getTop() {
		return top;
	}

	/**
	 * Setter for top
	 * @param top the new top Point
	 */
	public void setTop(Point top) {
		this.top = top;
	}

	/**
	 * Getter for bottom
	 * @return the bottom Point
	 */
	public Point getBottom() {
		return bottom;
	}

	/**
	 * Setter for bottom
	 * @param top the new bottom Point
	 */
	public void setBottom(Point bottom) {
		this.bottom = bottom;
	}

	/**
	 * Getter for right
	 * @return the right Point
	 */
	public Point getRight() {
		return right;
	}

	/**
	 * Setter for right
	 * @param top the new right Point
	 */
	public void setRight(Point right) {
		this.right = right;
	}

	/**
	 * Getter for left
	 * @return the left Point
	 */
	public Point getLeft() {
		return left;
	}

	/**
	 * Setter for left
	 * @param top the new left Point
	 */
	public void setLeft(Point left) {
		this.left = left;
	}
	
	/**
	 * Getter for WIDTH
	 * @return the default width of the block
	 */
	public static double getWidth() {
		return WIDTH;
	}

	/**
	 * Getter for HEIGHT
	 * @return the default height of the block
	 */
	public static double getHeight() {
		return HEIGHT;
	}
}