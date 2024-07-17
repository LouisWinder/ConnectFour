package connectfour;

public class Column {
    private int rowsFilled;
    
    public Column() {
    	rowsFilled = 0;
    }
    
    public int getRowsFilled() {
    	return rowsFilled;
    }
    
    public void incrementRowsFilled() {
    	rowsFilled += 1;
    }
    
    public void decrementRowsFilled() {
    	rowsFilled -= 1;
    }
    
    public void setRowsFilled(int rowsFilled) {
    	this.rowsFilled = rowsFilled;
    }
}
