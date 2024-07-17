package connectfour;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Grid extends JPanel {
	private int width;
	private int height;
    private Space[][] spaces;
    private Column[] columns;
    private int[] columnOrder;
    
    public Grid(int width, int height) {
    	this.width = width;
    	this.height = height;
    	spaces = new Space[height][width];
    	columns = new Column[width];
    	this.setLayout(new GridLayout(height, width));
    	for (int i = 0; i < height; i++) {
    		for (int j = 0; j < width; j++) {
    			spaces[i][j] = new Space(i, j, null);
    			this.add(spaces[i][j]);
    			spaces[i][j].setFocusPainted(false);
    			// Create the columns (for determining valid moves)
    			if (i == 0) {
    				Column column = new Column();
    				columns[j] = column;
    			}
    		}
    	}
    	columnOrder = new int[width];
    	setColumnOrder(); // Reorder available spaces (for faster alpha-beta search)
    }
    
    public int getGridWidth() {
    	return width;
    }
    
    public int getGridHeight() {
    	return height;
    }
    
    public Space[][] getSpaces() {
    	return spaces;
    }
    
    public void setColumnOrder() {
    	for (int i = 0; i < width; i++) {
    		columnOrder[i] = width / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
    	}
    }
    
    public ArrayList<Space> availableSpaces() {
    	ArrayList<Space> available = new ArrayList<>();
    	for (int i = 0; i < height; i++) {
    		for (int j = 0; j < width; j++) {
    			if (!spaces[i][columnOrder[j]].isPieceInSpace()) { // Space is available
    				// Only need to evaluate valid spaces
    				if (!Game.isMoveValid(spaces[i][columnOrder[j]])) {
    					continue;
    				}
    				available.add(spaces[i][columnOrder[j]]);
    			}
    		}
    	}
    	return available;
    }
    
    public String gridState() {
    	String gridState = "";
    	for (int i = 0; i < height; i++) {
    		for (int j = 0; j < width; j++) {
    			if (!spaces[i][j].isPieceInSpace()) {
    				gridState += 0;
    				continue;
    			}
    			gridState += this.spaces[i][j].getPieceInSpace().isRed() ? 1 : -1;
    		}
    	}
    	return gridState;
    }
    
    public Column[] getColumns() {
    	return columns;
    } 
    
    public void disableGrid() {
    	for (int i = 0; i < height; i++) {
    		for (int j = 0; j < width; j++) {
    			spaces[i][j].setEnabled(false);
    		}
    	}
    }
}
