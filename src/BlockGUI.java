/**
 * BlockGUI is the main method to parse code and create the flowchart
 * 
 * @author Minh Vo
 * @version 0.0.3
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class BlockGUI extends JFrame {
	private static final int BUFFER = 50; // space between blocks
	
	private static EditorPanel p = new EditorPanel(); // Edit panel for canvas
	private static BlockCanvas c = new BlockCanvas(p); // BlockCanvas to draw on
	
	/**
	 * Initialize the GUI components
	 */
	public static void init() {
		// Create new frame and set closing action and border layout
		JFrame frame = new JFrame("Flowchart to Code Converter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		// Add the canvas to the center
		frame.getContentPane().add(c, BorderLayout.CENTER);
		frame.getContentPane().add(p, BorderLayout.SOUTH);
		
		// Pack frame content and set visible
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Main method parsing code and create flowchart
	 * @param args cmd line arguments
	 */
	public static void main(String[] args) {
		
		// Run the GUI frame
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Initialize GUI elements
            	init();
                
            	FlowchartBlock pre = null; // previous block handled
            	
            	// Insert 3 blocks, 2 terminator and 1 data
            	c.addBlock(pre = new TerminatorBlock(
        			c.getInit_x(), c.getInit_y(), "Begin class Hello World",
        			null, c));
            	c.addBlock(pre = new DataBlock(pre.getOrigin().getX(), 
        			pre.getOrigin().getY() + FlowchartBlock.getHeight()
        			+ BUFFER, "Print HelloWorld", pre, c));
            	c.addBlock(pre = new TerminatorBlock(pre.getOrigin().getX(), 
        			pre.getOrigin().getY() + FlowchartBlock.getHeight()
        			+ BUFFER, "End class HelloWorld", pre, c));
            	
            	// int i = 0; // loop index
            	// Insert blocks loop-based like the parser (loop is contrived)
        		/*while(i < 2) {
        			// If inserting first block, insert at init coordinate
        			if(pre == null)
            			c.addBlock(pre = new TerminatorBlock(
            				c.getInit_x(), c.getInit_y(), "class Hello World", 
            				null, c));
        			
        			// Else insert below the previous block
        			else
            			c.addBlock(pre = new DataBlock(pre.getOrigin().getX(), 
            				pre.getOrigin().getY() + FlowchartBlock.getHeight()
            				+ BUFFER, "Print HelloWorld", pre, c));
        			
        			// Increment i
        			i++;
        		}*/
            }
        });
	}
}

/**
 * This private class handles the canvas for the program
 */
@SuppressWarnings("serial")
class BlockCanvas extends JPanel 
				  implements MouseListener,
				  			 MouseMotionListener,
				  			 Runnable {
	
	// Default width and height
	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 1000;

	// ArrayList of blocks on the canvas
	ArrayList<FlowchartBlock> blocks = new ArrayList<FlowchartBlock>();
	
	// ArrayList of blocks being acted on
	ArrayList<FlowchartBlock> actionBlocks = new ArrayList<FlowchartBlock>();

	// Initial x and y coordinates
	private final double init_x;
	private final double init_y = 50;

	// Previous and current mouse event points on canvas
	private Point prevPoint;
	private Point currPoint;
	
	// Editor panel for this canvas
	private EditorPanel editPane;

	// Storing previous block handled
	private FlowchartBlock pre;
	
	private boolean editing = false;

	/**
	 * Constructor for BlockCanvas setting up the canvas and adding Event 
	 * Handlers
	 */
	public BlockCanvas(EditorPanel editPane) {
		// set the edit panel of this canvas
		this.editPane = editPane;
		
		// Set background, size, and add event handlers
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setSize(this.getPreferredSize());
		
		// Set init x
		init_x = (this.getWidth() - FlowchartBlock.getWidth())/2;
		
		// Start Runnable thread to check for repaints
		new Thread(this).start();
	}

	/**
	 * Paint objects on the canvas
	 * @param g Graphics object to draw on canvas
	 */
	public void paintComponent(Graphics g) {
		// Invoke Canvas' paintComponent
		super.paintComponent(g);

		// Use a casted Graphics2D object to draw all blocks and messages
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < blocks.size(); i++) {
			g2.draw(blocks.get(i).getBlock());
			g2.drawString(blocks.get(i).getMsg(), blocks.get(i).getxStr(), 
					blocks.get(i).getyStr());

			// If there is a previous block, draw a line connecting them
			if(blocks.get(i).getPre() != null)
				g2.drawLine((int) blocks.get(i).getPre().getBottom().getX(), 
					(int) blocks.get(i).getPre().getBottom().getY(), 
					(int) blocks.get(i).getTop().getX(), 
					(int) blocks.get(i).getTop().getY());
		}
	}

	/**
	 * Get preferred (default) size for the canvas
	 */
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * Add a block to the canvas
	 * @param block block being added to the canvas
	 */
	public void addBlock(FlowchartBlock block) {
		// Add the block to the ArrayList and repaint the canvas
		blocks.add(block);
		this.setPre(block.getPre());
		repaint();
	}

	/**
	 * Event handler for mouse presses, stores prevPoint and get all objects
	 * pressed on
	 * @param e MouseEvent occurred
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// Store prevPoint as location of mouse press
		prevPoint = new Point(e.getX(), e.getY());

		// Loop through blocks to get the ones pressed on
		for (int i = 0; i < blocks.size(); i++) {
			if(blocks.get(i).containsPoint(prevPoint))
				actionBlocks.add(blocks.get(i));
		}
	}

	/**
	 * Event handler for mouse release, clear all action blocks and stores 
	 * prevPoint
	 * @param e MouseEvent occurred
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// set prevPoint to currentPoint
		prevPoint = new Point(e.getX(), e.getY());

		// clear all action blocks
		actionBlocks.clear();
	}

	/**
	 * Event handler for mouse drag, moving blocks pressed on
	 * @param e MouseEvent occurred
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// Calculate change in coordinates
		currPoint = new Point(e.getX(), e.getY());
		double deltaX = currPoint.getX() - prevPoint.getX();
		double deltaY = currPoint.getY() - prevPoint.getY();

		// Move dragged blocks and repaint
		for (int i = 0; i < actionBlocks.size(); i++) {
			actionBlocks.get(i).move(deltaX, deltaY);
			actionBlocks.get(i).setxStr(actionBlocks.get(i).getxStr() + 
					(int)deltaX);
			actionBlocks.get(i).setyStr(actionBlocks.get(i).getyStr() + 
					(int)deltaY);
		}
		repaint();

		// Set prevPoint to be currPoint;
		prevPoint = currPoint;
	}

	/**
	 * Event handler for mouse clicks, editing messages of blocks clicked on
	 * and stores prevPoint
	 * @param e MouseEvent occurred
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// Store prevPoint and set editing to true
		prevPoint = new Point(e.getX(), e.getY());
		editing = true;
		
		// get all blocks clicked on
		for (int i = 0; i < blocks.size(); i++) {
			if(blocks.get(i).containsPoint(prevPoint)) {
				actionBlocks.add(blocks.get(i));
			}
		}

		// Enable the editor pane and add all action blocks for it to edit
		getEditPane().setEnabled(true);
		getEditPane().setEditedBlocks(actionBlocks);
		
		// Set the edit pane's prompt
		getEditPane().getPrompt().setText("Editing " + 
			actionBlocks.get(0).getType() + " block with message: " + 
			actionBlocks.get(0).getMsg() + ". Press any button.");

		// Remove all action blocks
		actionBlocks.clear();
	}

	// Unused MouseMotionListener and MouseListener methods
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	/**
	 * This method is executed in a separate thread to constantly check for repaints
	 */
	@Override
	public void run() {
		// Get the current number of the editor panel's editing blocks
		int prevSize = editPane.getEditedBlocks().size();
		
		// Start an infinite loop
		while(true) {
			// If canvas is editing and number of edited blocks decreased, repaint
			if(editing == true && editPane.getEditedBlocks().size() < prevSize) {
				repaint();
				
				// If editing panel is done with editing, set editing to false
				if(editPane.getEditedBlocks().size() == 0)
					editing = false;
			
			// If not, recalculate prevSize
			} else {
				prevSize = editPane.getEditedBlocks().size();
				System.out.print(""); // it doesn't work without this line lmao
			}
		}
	}
	
	/**
	 * Getter for pre
	 * @return get the previously handled block
	 */
	public FlowchartBlock getPre() {
		return pre;
	}

	/**
	 * Setter for pre
	 * @return set a new previously handled block
	 */
	public void setPre(FlowchartBlock pre) {
		this.pre = pre;
	}

	/**
	 * Getter for editPane
	 * @return the canvas's editor panel
	 */
	public EditorPanel getEditPane() {
		return editPane;
	}

	/**
	 * Setter for editPane
	 * @param editPane new editor panel for canvas
	 */
	public void setEditPane(EditorPanel editPane) {
		this.editPane = editPane;
	}

	/**
	 * Getter for init_x
	 * @return get the initial x coordinate for first block
	 */
	public double getInit_x() {
		return init_x;
	}

	/**
	 * Getter for init_y
	 * @return get the initial y coordinate for first block
	 */
	public double getInit_y() {
		return init_y;
	}
}

/**
 * This class handles the editor panel of the program
 */
@SuppressWarnings("serial")
class EditorPanel extends JPanel {
	// Panel GUI elements
	private TextField msgEditor = new TextField(50);
	private JButton editMsgButton = new JButton("Edit message");
	private JButton changePreButton = new JButton("Change pre");
	private JButton changeNextButton = new JButton("Change next");
	private JButton doneButton = new JButton("Done");
	private JLabel prompt = new JLabel("Drag block to move. Click block to edit.");

	// ArrayList of blocks on the canvas
	ArrayList<FlowchartBlock> editedBlocks = new ArrayList<FlowchartBlock>();
	
	private boolean editMsgFlag = false; // flag if a block's msg is edited
	
	/**
	 * Constructor for EditorPanel
	 */
	public EditorPanel() {
		// Call the super constructor
		super();
		
		// Disable GUI elements
		this.setEnabled(false);
		msgEditor.setEnabled(false);
		
		// Set border layout
		this.setLayout(new BorderLayout());
		
		// Set up button subpanel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(editMsgButton);
		buttonPanel.add(changePreButton);
		buttonPanel.add(changeNextButton);
		buttonPanel.add(doneButton);
		
		// Set up text box subpanel
		JPanel textBoxPanel = new JPanel();
		textBoxPanel.add(prompt);
		textBoxPanel.add(msgEditor);
		
		// Add the subpanels to this editor panel
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(textBoxPanel, BorderLayout.SOUTH);
		
		// Add action listener for edit button
		editMsgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msgEditor.setEnabled(true); // Turn on the text box
				getPrompt().setText("Enter new message:"); // Change prompt
				editMsgFlag = true; // set edit msg flag to true
			}
		});
		
		// Add action listener for done button
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If editedBlocks isn't empty proceed with edit
				if(editedBlocks.size() > 0) {
					// If editing msg, change the 0th edited block's msg
					if(editMsgFlag) {
						getEditedBlocks().get(0).setMsg(msgEditor.getText());
						editMsgFlag = false; // set edit msg flag to false
					}
					
					// Remove the edited block from the ArrayList
					editedBlocks.remove(0);
					
					// Disable text box
					msgEditor.setText("");
					msgEditor.setEnabled(false);
				} 
				
				// If there are no more edited blocks, disable the panel and reset prompt
				if(editedBlocks.size() == 0) {
					setEnabled(false);
					getPrompt().setText("Drag block to move. Click block to edit.");
					
				// If not, change prompt to the new 0th block
				} else {
					getPrompt().setText("Editing " + 
						editedBlocks.get(0).getType() + " block with message: " + 
						editedBlocks.get(0).getMsg() + ". Press any button.");
				}
			}
		});
	}
	
	/**
	 * Turn on or off the editor panel
	 * @param b true for on or false for off
	 */
	@Override
	public void setEnabled(boolean b) {
		msgEditor.setEditable(b);
		editMsgButton.setEnabled(b);
		changePreButton.setEnabled(b);
		changeNextButton.setEnabled(b);
		doneButton.setEnabled(b);
	}
	
	public void setEditedBlocks(ArrayList<FlowchartBlock> blocks) {
		editedBlocks.addAll(blocks);
	}
	
	public ArrayList<FlowchartBlock> getEditedBlocks() {
		return editedBlocks;
	}

	public TextField getMsgEditor() {
		return msgEditor;
	}

	public void setMsgEditor(TextField msgEditor) {
		this.msgEditor = msgEditor;
	}

	public JLabel getPrompt() {
		return prompt;
	}

	public void setPrompt(JLabel prompt) {
		this.prompt = prompt;
	}
}