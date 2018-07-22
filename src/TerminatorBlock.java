/**
 * TerminatorBlock is a FlowchartBlock at the beginning and ending of flowchart
 * 
 * @author Minh Vo
 * @version 0.0.2
 */
import java.awt.*;
//import java.awt.event.*;
import java.awt.geom.*;

public class TerminatorBlock extends FlowchartBlock 
							 /*implements MouseListener, 
							            MouseMotionListener*/ {
	
	private static final double arc = 10; // Arc curve radius
	private static final int msgBuffer = 15; /* Space between shape outline
											    and text */
	//private static final double limit = 5;

	//private Point prevPoint;
	//private Point currPoint;
    //boolean isDragged;
    
    private BlockCanvas canv; // canvas to draw on
    
    /**
     * Constructor for TerminatorBlock initializing data fields
     * @param x x coordinate of block
     * @param y y coordinate of block
     * @param msg message of block
     * @param c canvas to draw block on
     */
	public TerminatorBlock(double x, double y, String msg, 
		FlowchartBlock pre, BlockCanvas c) {
		// Call the parent ctor passing in the block type
		super("Terminator");
		
		// Initialize data fields
		this.setBlock(new RoundRectangle2D.Double(x, y, getWidth(), 
			getHeight(), arc, arc));
		this.setMsg(msg);
		this.setxStr((int) x + msgBuffer);
		this.setyStr((int) y + msgBuffer);
		this.setPre(pre);
		if(pre != null)
			this.getPre().setNext(this);
		this.setOrigin(new Point((int) x, (int) y));
		this.setTop(new Point((int) (x + getWidth()/2), (int) y));
		this.setBottom(new Point((int) (x + getWidth()/2), 
			(int) (y + getHeight())));
		this.setLeft(new Point((int) x, (int) (y + getHeight()/2)));
		this.setRight(new Point((int) (x + getWidth()), 
			(int) (y + getHeight()/2)));
		this.setCanv(c);
	}

	/*
	@Override
	public void paintBlock(BlockCanvas c) {
		Graphics2D g2 = (Graphics2D) c.getGraphics();
		g2.draw(this.getBlock());
	}*/
	
	/**
	 * Move the block on canvas
	 * @param deltaX distance to move in x-direction
	 * @param deltaY distance to move in y-direction
	 */
	@Override
	public void move(double deltaX, double deltaY) {
		// Change the coordinates of the shape
		((RoundRectangle2D) getBlock()).setRoundRect(
			((RoundRectangle2D.Double) getBlock()).getX() + deltaX, 
			((RoundRectangle2D.Double) getBlock()).getY() + deltaY,
			getWidth(), getHeight(), arc, arc);
		
		// Set the origin, top, bottom, left, right to their new locations
		this.getOrigin().setLocation(this.getOrigin().getX() + deltaX,
			this.getOrigin().getY() + deltaY);
		this.getTop().setLocation(this.getTop().getX() + deltaX,
			this.getTop().getY() + deltaY);
		this.getBottom().setLocation(this.getBottom().getX() + deltaX,
			this.getBottom().getY() + deltaY);
		this.getLeft().setLocation(this.getLeft().getX() + deltaX,
			this.getLeft().getY() + deltaY);
		this.getRight().setLocation(this.getRight().getX() + deltaX,
			this.getRight().getY() + deltaY);
	}
	
	/**
	 * Check if the shape contains a point
	 * @param p point to check for insideness
	 */
	@Override
	public boolean containsPoint(Point p) {
		// Call the shape's contain method and return the result
		return this.getBlock().contains(p.getX(), p.getY());
	}
	
	/*
	@Override
	public void mousePressed(MouseEvent e) {
		prevPoint = new Point(e.getX(), e.getY());
		
		// If block contains 
		if(this.containsPoint(prevPoint))
			isDragged = true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(isDragged) {
			// Calculate change in coordinates
			currPoint = new Point(e.getX(), e.getY());
			double deltaX = currPoint.getX() - prevPoint.getX();
			double deltaY = currPoint.getY() - prevPoint.getY();

			//Prevent the orb from moving past the 6px borders
			if(prevPoint.getX() + deltaX < limit ||
			   prevPoint.getX() + deltaX > canv.getgetWidth()() - limit){
				deltaX = 0;
			}
			
			if(prevPoint.getY() + deltaY < limit ||
			   prevPoint.getY() + deltaY > canv.getHeight() - limit){
				deltaY = 0;
			} 
			
			// Move the block and repaint
			this.move(deltaX, deltaY);
			//repaint();
			
			// Set prevPoint to be currPoint;
			prevPoint = currPoint;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// set prevPoint to currentPoint
		prevPoint = new Point(e.getX(), e.getY());
				
		// set isDragged to false
		isDragged = false;
	}

	// Unused MouseListener and MouseMotionListener methods
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	*/
	
	/**
	 * Getter for canv
	 * @return the canvas of the block
	 */
	public BlockCanvas getCanv() {
		return canv;
	}

	/**
	 * Setter for canv
	 * @param canv new canvas for block
	 */
	public void setCanv(BlockCanvas canv) {
		this.canv = canv;
	}
}